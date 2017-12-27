package magica.projects.flowtree.core;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import magica.projects.flowtree.analytics.DomainAnalytics;
import magica.projects.flowtree.analytics.GlobalAnalytics;
import magica.projects.flowtree.scheduler.SchedulerData;

/**
 *
 * @author emelyanov
 */
public class MetaDomain {

    protected int id;
    protected Double costRating;    // cost rating for jobs execution; the higher - the better
    protected Double mipsRating;    // mips rating for jobs execution; the higher - the better
    protected Double baseMIPSRating;
    protected Double volumeCapacity;

    protected JobFlow assignedJobFlow;

    protected List<MetaDomain> nextLevelDomains;
    protected List<MetaDomain> previousLevelDomains;

    protected DomainAnalytics localAnalytics;
    public SchedulerData sdata = new SchedulerData();

    public MetaDomain(int id) {
        this.id = id;

        assignedJobFlow = new JobFlow();

        nextLevelDomains = new LinkedList<>();
        previousLevelDomains = new LinkedList<>();
        localAnalytics = new DomainAnalytics();
    }

    public MetaDomain(int id, Double costRating, Double mipsRating, Double volumeCapacity) {
        this.id = id;
        this.costRating = costRating;
        this.mipsRating = mipsRating;
        this.baseMIPSRating = mipsRating;
        this.volumeCapacity = volumeCapacity;

        assignedJobFlow = new JobFlow();

        nextLevelDomains = new LinkedList<>();
        previousLevelDomains = new LinkedList<>();
        localAnalytics = new DomainAnalytics();
    }

    public void updateRatingsSimple(boolean recursively) {
        if (nextLevelDomains.isEmpty()) {
            return;
        }

        Double sumCostRating = 0d;
        Double sumMIPSRating = 0d;
        Double sumVolumeCapacity = 0d;

        for (MetaDomain domain : nextLevelDomains) {
            if (recursively) {    // hope we don't have cycles!
                domain.updateRatingsSimple(recursively);
            }
            sumCostRating += domain.getCostRating();
            sumMIPSRating += domain.getMIPSRating();
            sumVolumeCapacity += domain.getVolumeCapacity();
        }

        this.costRating = sumCostRating / nextLevelDomains.size();
        this.mipsRating = sumMIPSRating / nextLevelDomains.size();
        this.volumeCapacity = sumVolumeCapacity;
    }

    public void updateRatingsWeighted(boolean recursively) {
        if (nextLevelDomains.isEmpty()) {
            return;
        }

        Double sumCostRating = 0d;
        Double sumMIPSRating = 0d;
        Double sumVolumeCapacity = 0d;

        for (MetaDomain domain : nextLevelDomains) {
            if (recursively) {    // hope we don't have cycles!
                domain.updateRatingsWeighted(recursively);
            }
            sumCostRating += domain.getCostRating() * domain.getVolumeCapacity();
            sumMIPSRating += domain.getMIPSRating() * domain.getVolumeCapacity();
            sumVolumeCapacity += domain.getVolumeCapacity();
        }

        this.costRating = sumCostRating / sumVolumeCapacity;
        this.mipsRating = sumMIPSRating / sumVolumeCapacity;
        this.volumeCapacity = sumVolumeCapacity;
    }

    public void executeJobFlow(boolean recursively) {

        if (nextLevelDomains.isEmpty()) {   // if that's a computing leaf domain
            returnUnfitJobs(assignedJobFlow, true /* up recursively */);
            localAnalytics.recalculateMIPS(this, assignedJobFlow); // updating real MIPS based on real load for current round 
            GlobalAnalytics.analyze(this, assignedJobFlow); // global info on users satisfaction based on real cost and MIPS
            //System.out.println("Domain " + id + " Jobs: " + assignedJobFlow.jobs.size());
        }

        // update ratings based on local info on domain efficiencey and load
        // recalulate values for the next round
        localAnalytics.update(this, assignedJobFlow);

        assignedJobFlow.clear(); // executed

        if (recursively) {
            for (MetaDomain subDomain : nextLevelDomains) {
                subDomain.executeJobFlow(recursively);
            }
        }
    }

    protected void returnUnfitJobs(JobFlow flow, boolean recursive) {
        List<Job> unfitJobs = new LinkedList<>();
        for (Iterator<Job> it = flow.jobs.iterator(); it.hasNext();) {
            Job job = it.next();
            if (job.getBudget() > this.costRating) {  // we can't process jobs which don't have enough money
                JobAnalytics.updateReturnedJob(job);
                unfitJobs.add(job);
                it.remove();
            }
        }

        GlobalAnalytics.reportJobsReturned(unfitJobs);
        returnUnfitJobs(unfitJobs, recursive);
    }

    /* returning jobs to higher level */
    protected void returnUnfitJobs(List<Job> jobs, boolean recursive) {
        if (!previousLevelDomains.isEmpty() && recursive) {    // returning jobs up
            MetaDomain prevDomain = previousLevelDomains.get(0);
            prevDomain.returnUnfitJobs(jobs, recursive);
        } else {
            this.assignedJobFlow.jobs.addAll(jobs);     // and adding to high level job flow
        }
    }

    public Double calculateLoad() {
        if (volumeCapacity > 0) {
            return assignedJobFlow.volume / volumeCapacity;
        } else {
            return 0d;
        }
    }

    public void printAnalytics(boolean recursive, int level) {
        System.out.println(localAnalytics.getData());

        if (recursive) {
            for (MetaDomain domain : nextLevelDomains) {
                domain.printAnalytics(recursive, level + 1);
            }
        }

    }

    public void addSubDomain(MetaDomain subDomain) {
        nextLevelDomains.add(subDomain);
        subDomain.previousLevelDomains.add(this);
    }

    public void notifyAboutChanges() {
        for (MetaDomain domain : previousLevelDomains) {
            domain.updateRatingsWeighted(false);    // one level update is enough
            domain.notifyAboutChanges();    // send update message up
        }
    }

    public void addJob(Job job) {
        this.assignedJobFlow.jobs.add(job);
    }

    public void removeJob(Job job) {
        this.assignedJobFlow.jobs.remove(job);
    }

    /* clones only structure and characteristics (without temp scheduling or stats data): */
    public MetaDomain clone() {
        MetaDomain newDomain = new MetaDomain(id, costRating, mipsRating, volumeCapacity);
        newDomain.baseMIPSRating = baseMIPSRating;

        newDomain.localAnalytics = new DomainAnalytics();

        /* creating subdomains and links */
        newDomain.nextLevelDomains = new LinkedList<>();
        for (MetaDomain subDomain : nextLevelDomains) {
            MetaDomain newSubDomain = subDomain.clone();
            newSubDomain.getPreviousLevelDomains().add(newDomain);
            newDomain.nextLevelDomains.add(newSubDomain);
        }

        return newDomain;
    }

    /**
     * @return the costRating
     */
    public Double getCostRating() {
        return costRating;
    }

    /**
     * @param costRating the costRating to set
     */
    public void setCostRating(Double costRating) {
        this.costRating = costRating;
    }

    /**
     * @return the mipsRating
     */
    public Double getMIPSRating() {
        return mipsRating;
    }

    /**
     * @param mipsRating the mipsRating to set
     */
    public void setMIPSRating(Double mipsRating) {
        this.mipsRating = mipsRating;
    }

    /**
     * @return the volumeCapacity
     */
    public Double getVolumeCapacity() {
        return volumeCapacity;
    }

    /**
     * @param volumeCapacity the volumeCapacity to set
     */
    public void setVolumeCapacity(Double volumeCapacity) {
        this.volumeCapacity = volumeCapacity;
    }

    /**
     * @return the nextLevelDomains
     */
    public List<MetaDomain> getNextLevelDomains() {
        return nextLevelDomains;
    }

    /**
     * @return the previousLevelDomains
     */
    public List<MetaDomain> getPreviousLevelDomains() {
        return previousLevelDomains;
    }

    /**
     * @return the assignedJobFlow
     */
    public JobFlow getAssignedJobFlow() {
        return assignedJobFlow;
    }

    /**
     * @return the baseMIPSsRating
     */
    public Double getBaseMIPSsRating() {
        return baseMIPSRating;
    }

    /**
     * @param baseMIPSsRating the baseMIPSsRating to set
     */
    public void setBaseMIPSsRating(Double baseMIPSsRating) {
        this.baseMIPSRating = baseMIPSsRating;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

}

package magica.projects.flowtree.scheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magica.projects.flowtree.core.FlowTreeOperations;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.JobFlow;
import magica.projects.flowtree.core.MetaDomain;
import magica.projects.flowtree.scheduler.job.JobAssigner;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

/**
 *
 * @author emelyanov
 */
public class DoubleFlowMetaScheduler extends FlowScheduler {

    protected DoubleFlowSettings settings;
    
    protected JobAssigner costJobAssigner;
    protected JobAssigner mipsJobAssigner;

    public DoubleFlowMetaScheduler(DoubleFlowSettings settings, JobAssigner costJobAssigner, JobAssigner mipsJobAssigner) {
        this.settings = settings;
        this.costJobAssigner = costJobAssigner;
        this.mipsJobAssigner = mipsJobAssigner;
    }
    
    public DoubleFlowMetaScheduler(JobAssigner costJobAssigner, JobAssigner mipsJobAssigner) {
        this.settings = new DoubleFlowSettings();
        this.costJobAssigner = costJobAssigner;
        this.mipsJobAssigner = mipsJobAssigner;
    }

    public void distributeDomainFlow(MetaDomain domain, boolean recursively) {

        if (domain.getNextLevelDomains().isEmpty()) {
            return;     // it's a leaf domain, no dstribution is required
        }

        /* categorizing jobs to cost and mips demanding */
        domain.sdata.costFlow = new JobFlow();
        domain.sdata.mipsFlow = new JobFlow();
        categorizeFlow(domain);

        /* getting LP solution for cost and mips flows*/
        calculateJobFlowDistribution(domain);
        /* saving solution for analysis */
        mergeAndSaveFlows(domain);
        /* distributing jobs based on LP */
        distributeJobs(domain);

        if (recursively) {
            for (MetaDomain subDomain : domain.getNextLevelDomains()) {
                distributeDomainFlow(subDomain, recursively);
            }
        }
    }

    public void distributeDomainFlow(MetaDomain domain) {
        distributeDomainFlow(domain, false);
    }

    public void distributeWholeFlow(MetaDomain startingDomain) {
        distributeDomainFlow(startingDomain, true);
    }

    protected void categorizeFlow(MetaDomain domain) {
        domain.sdata.mipsFlow.updateVolume();
        domain.sdata.costFlow.updateVolume();

        for (Job job : domain.getAssignedJobFlow().jobs) {
            if (job.getBudget() > domain.getCostRating() * settings.budgetCategoryLevel) {
                domain.sdata.costFlow.jobs.add(job);         // we need treat it as more cost-demanding job
            } else if ((job.getCostExpect() > job.getMIPSExpect())
                    //&& (domain.sdata.costFlow.volume < domain.sdata.mipsFlow.volume) // some balancing maybe? 
                    ) {
                domain.sdata.costFlow.jobs.add(job);
            } else {
                domain.sdata.mipsFlow.jobs.add(job);
            }
            domain.sdata.mipsFlow.updateVolume();
            domain.sdata.costFlow.updateVolume();
        }
    }

    public void calculateJobFlowDistribution(MetaDomain domain) {

        /* two variables for each sub-domain: amount of cost and mips flows */
 /* first half is for cost variables, second half - for mips variables */
        int domainsNum = domain.getNextLevelDomains().size();
        int variablesNum = domainsNum * 2;

        /* all constraints for LP problem */
        Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        double[] objectiveCoefs = new double[variablesNum];
        double[] totalCostLimitCoefs = new double[variablesNum];

        Double volumeFeasibilityEnlarger = calculateFeasibilityEnlarger(domain);

        int i = 0;
        for (MetaDomain subDomain : domain.getNextLevelDomains()) {
            /* objective function definition */
 /* maximizing cost for costFlow (0-domainsNum) and mips fro mipsFlow (domainsNum-domainsNum*2) */
            objectiveCoefs[i] = subDomain.getCostRating() * settings.costWeight;
            objectiveCoefs[i + domainsNum] = subDomain.getMIPSRating() * settings.mipsWeight;

            /* coefficients for total cost limit if we will need this */
 /* each flow fraction is multiplied by subdomain cost */
//            totalCostLimitCoefs[i] = subDomain.getCostRating();
//            totalCostLimitCoefs[i+domainsNum] = subDomain.getCostRating();

            /* each domain has it's own volume limit (cost volume + mips volume < domain volume) */
            double[] domainVolumeLimitCoefs = new double[variablesNum];
            for (int j = 0; j < variablesNum; j++) {
                if (j == i || j == (i + domainsNum)) {
                    domainVolumeLimitCoefs[j] = 1;    // 1 only for cost and mips flows variables for this i-th domain
                } else {
                    domainVolumeLimitCoefs[j] = 0;
                }
            }
            /* constraint on i-th subdomain flow volume */
            constraints.add(new LinearConstraint(domainVolumeLimitCoefs, Relationship.LEQ, subDomain.getVolumeCapacity() * volumeFeasibilityEnlarger));

            /* cost flow for current subdomain should be non-negative (cost volume >= 0) */
            double[] costVolumeLimitCoefs = new double[variablesNum];
            for (int j = 0; j < variablesNum; j++) {
                if (j == i) {
                    costVolumeLimitCoefs[j] = 1;
                } else {
                    costVolumeLimitCoefs[j] = 0;
                }
            }
            constraints.add(new LinearConstraint(costVolumeLimitCoefs, Relationship.GEQ, 0));

            /* mips flow for current subdomain should be non-negative (mips volume >= 0) */
            double[] mipsVolumeLimitCoefs = new double[variablesNum];
            for (int j = 0; j < variablesNum; j++) {
                if (j == (i + domainsNum)) {
                    mipsVolumeLimitCoefs[j] = 1;
                } else {
                    mipsVolumeLimitCoefs[j] = 0;
                }
            }
            constraints.add(new LinearConstraint(mipsVolumeLimitCoefs, Relationship.GEQ, 0));

            /* next subdomain */
            i++;
        }

        /* specifying total volume to dispatch */
        double[] totalCostVolumeCoefs = new double[variablesNum];
        double[] totalMIPSVolumeCoefs = new double[variablesNum];
        for (int j = 0; j < variablesNum; j++) {
            if (j < domainsNum) {
                totalCostVolumeCoefs[j] = 1;
                totalMIPSVolumeCoefs[j] = 0;
            } else {
                totalCostVolumeCoefs[j] = 0;
                totalMIPSVolumeCoefs[j] = 1;
            }
        }
        constraints.add(new LinearConstraint(totalCostVolumeCoefs, Relationship.EQ, domain.sdata.costFlow.updateVolume()));
        constraints.add(new LinearConstraint(totalMIPSVolumeCoefs, Relationship.EQ, domain.sdata.mipsFlow.updateVolume()));

        /* total cost limit */
        //constraints.add(new LinearConstraint(totalCostLimitCoefs, Relationship.LEQ, maxcost));
        /* Objective function -> maximize cost/mips satisfaction */
        LinearObjectiveFunction f = new LinearObjectiveFunction(objectiveCoefs, 0 /* constant term */);

        /* solution API */
        PointValuePair solution = new SimplexSolver().optimize(f, new LinearConstraintSet(constraints), GoalType.MAXIMIZE);

        /* prepare result */
        domain.sdata.costFlow.distributionMap = new HashMap<>();
        domain.sdata.mipsFlow.distributionMap = new HashMap<>();

        for (i = 0; i < domainsNum; i++) {
            domain.sdata.costFlow.distributionMap.put(domain.getNextLevelDomains().get(i), solution.getPoint()[i]);
            domain.sdata.mipsFlow.distributionMap.put(domain.getNextLevelDomains().get(i), solution.getPoint()[i + domainsNum]);
        }

    }

    protected void mergeAndSaveFlows(MetaDomain domain) {

        /* merging cost and mips flow in general assigned flow */
        domain.getAssignedJobFlow().distributionMap = new HashMap<>();
        for (Map.Entry<MetaDomain, Double> entry : domain.sdata.costFlow.distributionMap.entrySet()) {
            MetaDomain keyDomain = entry.getKey();
            Double mipsFlowValue = domain.sdata.mipsFlow.distributionMap.get(keyDomain);
            domain.getAssignedJobFlow().distributionMap.put(keyDomain, mipsFlowValue + entry.getValue());
        }

        /* saving plan to special maps for future analysis */
        domain.sdata.costFlow.distributionMapPlan = new HashMap<>();
        domain.sdata.costFlow.distributionMapPlan.putAll(domain.sdata.costFlow.distributionMap);
        domain.sdata.mipsFlow.distributionMapPlan = new HashMap<>();
        domain.sdata.mipsFlow.distributionMapPlan.putAll(domain.sdata.mipsFlow.distributionMap);
        domain.getAssignedJobFlow().distributionMapPlan = new HashMap<>();
        domain.getAssignedJobFlow().distributionMapPlan.putAll(domain.getAssignedJobFlow().distributionMap);
    }

    public void distributeJobs(MetaDomain domain) {

        /* sorting jobs to allocate resources for low budget jobs first */
        FlowTreeOperations.sortJobFlowByBudget(domain.sdata.costFlow);
        FlowTreeOperations.sortJobFlowByBudget(domain.sdata.mipsFlow);

        for (Job job : domain.sdata.costFlow.jobs) {
            domain.removeJob(job);   // removing job from higher lebel job-flow
            assignSingleJob(job, domain.sdata.costFlow.distributionMap, domain.getAssignedJobFlow().distributionMap, costJobAssigner);
        }

        for (Job job : domain.sdata.mipsFlow.jobs) {
            domain.removeJob(job);   // removing job from higher lebel job-flow
            assignSingleJob(job, domain.sdata.mipsFlow.distributionMap, domain.getAssignedJobFlow().distributionMap, mipsJobAssigner);
        }
    }

    protected void assignSingleJob(Job job, Map<MetaDomain, Double> localFlowPlan, Map<MetaDomain, Double> globalFlowPlan, JobAssigner jobAssigner) {
        MetaDomain bestDomain = jobAssigner.assignJob(job, localFlowPlan);
        bestDomain.addJob(job);
        localFlowPlan.put(bestDomain, localFlowPlan.get(bestDomain) - job.getVolume());       // updating local flow distribution
        globalFlowPlan.put(bestDomain, globalFlowPlan.get(bestDomain) - job.getVolume());     // updating global flow distribution
    }

    /* sometimes we may have more jobs then domains capacity
       in this case LP optimization will say the problem is infesible!
       so we are gonna uniformly increase all domains capacities when solving the problem */
    protected Double calculateFeasibilityEnlarger(MetaDomain domain) {
        Double jobFlowVolume = domain.getAssignedJobFlow().updateVolume();
        Double subDomainsVolume = 0d;
        for (MetaDomain subDomain : domain.getNextLevelDomains()) {
            subDomainsVolume += subDomain.getVolumeCapacity();
        }

        if (jobFlowVolume > subDomainsVolume) {
            return (jobFlowVolume / subDomainsVolume) + 0.001;  // addong small value to be 100% sure the rounding is ok
        } else {
            return 1d;
        }

    }

    private Double calculateExecutionCriterion(Job job, MetaDomain domain) {
        return job.getCostExpect() * domain.getCostRating()
                + job.getMIPSExpect() * domain.getMIPSRating();
    }

    @Override
    public void distributeDomainFlow(JobFlow flow, List<MetaDomain> subDomains, boolean recursively) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

package magica.projects.flowtree.scheduler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import magica.projects.flowtree.core.FlowTreeOperations;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.JobFlow;
import magica.projects.flowtree.core.MetaDomain;
import magica.projects.flowtree.scheduler.job.JobAssigner;
import magica.projects.math.MathUtils;

/**
 *
 * @author dmieter
 */
public class HierarchicalGreedyScheduler extends HierarchicalRandomScheduler {

    
    public HierarchicalGreedyScheduler(JobAssigner jobAssigner){
        super(jobAssigner);
    }
    
    @Override
    public void distributeDomainFlow(JobFlow flow, List<MetaDomain> subDomains, boolean recursively) {

        Map<MetaDomain, Double> subDomainsCapacity = new HashMap<>();
        for (MetaDomain subDomain : subDomains) {
            subDomainsCapacity.put(subDomain, subDomain.getVolumeCapacity());
        }

        FlowTreeOperations.sortJobFlowByBudget(flow);
        
        /* reassigning jobs to next tier domains */
        for (Iterator<Job> it = flow.jobs.iterator();it.hasNext();) {
            assignSingleJob(it.next(), flow, subDomainsCapacity);
            it.remove();
        }

        if (recursively) {
            for (MetaDomain subDomain : subDomains) {
                try {
                    distributeDomainFlow(subDomain, recursively);
                } catch (Exception e) {
                    throw new RuntimeException("We may have cycles in the domains structure!", e);
                }
            }
        }
    }

    private Double calculateExecutionCriterion(Job job, MetaDomain domain) {
        return job.getCostExpect() * domain.getCostRating()
                + job.getMIPSExpect() * domain.getMIPSRating();
    }

    

}

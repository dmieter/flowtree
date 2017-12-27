/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author emelyanov
 */
public class HierarchicalRandomScheduler  extends FlowScheduler {

    protected JobAssigner jobAssigner;
    
    public HierarchicalRandomScheduler(JobAssigner jobAssigner){
        this.jobAssigner = jobAssigner;
    }
    
    
    public void distributeWholeFlow(MetaDomain startingDomain) {
        distributeDomainFlow(startingDomain, true);
    }

    public void distributeDomainFlow(MetaDomain domain, boolean recursively) {

        if (domain.getNextLevelDomains().isEmpty()) {
            return;     // it's a leaf domain, no dstribution is required
        }

        distributeDomainFlow(domain.getAssignedJobFlow(), domain.getNextLevelDomains(), recursively);
    }

    public void distributeDomainFlow(JobFlow flow, List<MetaDomain> subDomains, boolean recursively) {

        Map<MetaDomain, Double> subDomainsCapacity = new HashMap<>();
        for (MetaDomain subDomain : subDomains) {
            subDomainsCapacity.put(subDomain, subDomain.getVolumeCapacity());
        }

        FlowTreeOperations.sortJobFlowByBudget(flow);

        /* reassigning jobs to next tier domains */
        for (Iterator<Job> it = flow.jobs.iterator(); it.hasNext();) {
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
    
     protected void assignSingleJob(Job job, JobFlow flow, Map<MetaDomain, Double> subDomainsCapacity) {
        MetaDomain bestDomain = jobAssigner.assignJob(job, subDomainsCapacity);
        bestDomain.addJob(job);
        subDomainsCapacity.put(bestDomain, subDomainsCapacity.get(bestDomain) - job.getVolume());
     }
}

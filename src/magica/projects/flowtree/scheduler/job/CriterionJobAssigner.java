package magica.projects.flowtree.scheduler.job;

import java.util.Map;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public class CriterionJobAssigner extends JobAssigner {

    @Override
    public MetaDomain assignJob(Job job, Map<MetaDomain, Double> subDomainsCapacity) {

        Double maxCriterion = Double.NEGATIVE_INFINITY;
        MetaDomain bestDomain = null;

        for (Map.Entry<MetaDomain, Double> entry : subDomainsCapacity.entrySet()) {
            MetaDomain domain = entry.getKey();
            Double criterion = calculateExecutionCriterion(job, domain);
            
            if (entry.getValue() > 0 && domain.getCostRating() > job.getBudget() && criterion > maxCriterion) {
                maxCriterion = criterion;
                bestDomain = domain;
            }
        }

        return bestDomain;
    }

    private Double calculateExecutionCriterion(Job job, MetaDomain domain) {
        return job.getCostExpect() * domain.getCostRating()
                + job.getMIPSExpect() * domain.getMIPSRating();
    }

}

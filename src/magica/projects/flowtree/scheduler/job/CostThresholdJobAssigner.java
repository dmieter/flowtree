
package magica.projects.flowtree.scheduler.job;

import java.util.Map;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public class CostThresholdJobAssigner extends JobAssigner {
    
    @Override
    public MetaDomain assignJob(Job job, Map<MetaDomain, Double> subDomainsCapacity) {

        Double minThreshold = Double.POSITIVE_INFINITY;
        MetaDomain bestDomain = null;

        for (Map.Entry<MetaDomain, Double> entry : subDomainsCapacity.entrySet()) {
            MetaDomain domain = entry.getKey();
            Double threshold = domain.getCostRating()- job.getCostExpect();
            if (entry.getValue() > 0 && domain.getCostRating() > job.getBudget() && threshold > 0 && threshold < minThreshold) {
                minThreshold = threshold;
                bestDomain = domain;
            }
        }

        return bestDomain;
    }
}

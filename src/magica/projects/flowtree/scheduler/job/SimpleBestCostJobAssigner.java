package magica.projects.flowtree.scheduler.job;

import java.util.Map;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public class SimpleBestCostJobAssigner extends JobAssigner {

    @Override
    public MetaDomain assignJob(Job job, Map<MetaDomain, Double> subDomainsCapacity) {

        Double bestCost = Double.NEGATIVE_INFINITY;
        MetaDomain bestDomain = null;

        for (Map.Entry<MetaDomain, Double> entry : subDomainsCapacity.entrySet()) {
            MetaDomain domain = entry.getKey();
            if (domain.getCostRating() > bestCost) {
                bestCost = domain.getCostRating();
                bestDomain = domain;
            }
        }

        return bestDomain;
    }
}

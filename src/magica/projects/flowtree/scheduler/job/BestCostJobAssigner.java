/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magica.projects.flowtree.scheduler.job;

import java.util.Map;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public class BestCostJobAssigner extends JobAssigner {

    @Override
    public MetaDomain assignJob(Job job, Map<MetaDomain, Double> subDomainsCapacity) {

        Double bestCost = Double.NEGATIVE_INFINITY;
        MetaDomain bestDomain = null;

        for (Map.Entry<MetaDomain, Double> entry : subDomainsCapacity.entrySet()) {
            MetaDomain domain = entry.getKey();
            if (entry.getValue() > 0 && domain.getCostRating() > job.getBudget() && domain.getCostRating()> bestCost) {
                bestCost = domain.getCostRating();
                bestDomain = domain;
            }
        }

        return bestDomain;
    }

}

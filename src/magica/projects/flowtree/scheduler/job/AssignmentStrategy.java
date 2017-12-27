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
public abstract class AssignmentStrategy extends JobAssigner {

    public boolean saveEffort = true;

    public AssignmentStrategy() {
    }

    public AssignmentStrategy(boolean saveEffort) {
        this.saveEffort = saveEffort;
    }

    public MetaDomain assignJobLastHope(Job job, Map<MetaDomain, Double> subDomainsCapacity) {

        MetaDomain domain = null;

        if (saveEffort) {
            domain = new ThresholdAffordableJobAssigner().assignJob(job, subDomainsCapacity);

            if (domain == null) {
                domain = new SimpleBestCostJobAssigner().assignJob(job, subDomainsCapacity);
            }
        } else {
            domain = new SimpleMaxCapacityJobAssigner().assignJob(job, subDomainsCapacity);
        }

        return domain;
    }
}

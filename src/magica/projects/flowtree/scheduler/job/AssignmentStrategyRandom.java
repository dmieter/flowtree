package magica.projects.flowtree.scheduler.job;

import java.util.Map;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public class AssignmentStrategyRandom extends AssignmentStrategy {

    public AssignmentStrategyRandom() {
        
    }
    
    public AssignmentStrategyRandom(boolean saveEffort) {
        super(saveEffort);
    }

    @Override
    public MetaDomain assignJob(Job job, Map<MetaDomain, Double> subDomainsCapacity) {
        MetaDomain domain = null;
        domain = new FirstFitJobAssigner().assignJob(job, subDomainsCapacity);

        if (domain == null) {
            domain = assignJobLastHope(job, subDomainsCapacity);
        }

        return domain;
    }

}

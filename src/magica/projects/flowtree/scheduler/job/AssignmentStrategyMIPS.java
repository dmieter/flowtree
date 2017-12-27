package magica.projects.flowtree.scheduler.job;

import java.util.Map;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public class AssignmentStrategyMIPS extends AssignmentStrategy {

    public AssignmentStrategyMIPS() {
        
    }
    
    public AssignmentStrategyMIPS(boolean saveEffort) {
        super(saveEffort);
    }

    @Override
    public MetaDomain assignJob(Job job, Map<MetaDomain, Double> subDomainsCapacity) {
        MetaDomain domain = null;
        domain = new MIPSThresholdJobAssigner().assignJob(job, subDomainsCapacity);

        if (domain == null) {
            domain = new BestMIPSJobAssigner().assignJob(job, subDomainsCapacity);
        }
        
        if (domain == null) {
            domain = assignJobLastHope(job, subDomainsCapacity);
        }

        return domain;
    }

}

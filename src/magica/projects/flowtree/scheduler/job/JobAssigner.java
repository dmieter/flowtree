
package magica.projects.flowtree.scheduler.job;

import java.util.Map;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public abstract class JobAssigner {
    
    public abstract MetaDomain assignJob(Job job, Map<MetaDomain, Double> subDomainsCapacity);
    
}

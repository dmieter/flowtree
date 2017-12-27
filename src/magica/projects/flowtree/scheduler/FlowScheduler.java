
package magica.projects.flowtree.scheduler;

import java.util.List;
import magica.projects.flowtree.core.JobFlow;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public abstract class FlowScheduler {
    
    
    
    public abstract void distributeDomainFlow(JobFlow flow, List<MetaDomain> subDomains, boolean recursively);
    
    public void distributeDomainFlow(MetaDomain domain, boolean recursively){
        distributeDomainFlow(domain.getAssignedJobFlow(), domain.getNextLevelDomains(), recursively);
    }
    
}

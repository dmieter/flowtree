package magica.projects.flowtree.scheduler;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import magica.projects.flowtree.core.JobFlow;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author dmieter
 */
public class PlainScheduler extends FlowScheduler {

    private FlowScheduler flowScheduler;
    
    public void distributeWholeFlow(MetaDomain startingDomain) {
        distributeDomainFlow(startingDomain, true);
    }

    public void distributeDomainFlow(MetaDomain domain, boolean recursively) {
        
        /* restructure domains */
        List<MetaDomain> leafDomains = retrieveLeafDomainsList(domain);
        domain.getNextLevelDomains().clear();
        domain.getNextLevelDomains().addAll(leafDomains);
        
        for(MetaDomain leafDomain : leafDomains){
            leafDomain.getPreviousLevelDomains().clear();
            leafDomain.getPreviousLevelDomains().add(domain);
        }
        
        /* we need only one round between leaf domains */
        flowScheduler.distributeDomainFlow(domain, false);
    }

    private List<MetaDomain> retrieveLeafDomainsList(MetaDomain domain) {
        List<MetaDomain> leafDomains = new LinkedList<>();

        try {

            if (domain.getNextLevelDomains().isEmpty()) {
                /* this is leaf domain */
                leafDomains.add(domain);
            } else {
                for (MetaDomain subDomain : domain.getNextLevelDomains()) {
                    leafDomains.addAll(retrieveLeafDomainsList(subDomain));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Most possible we have cycles in the domain structure!", e);
        }

        return leafDomains;
    }

    /**
     * @param flowScheduler the flowScheduler to set
     */
    public void setFlowScheduler(FlowScheduler flowScheduler) {
        this.flowScheduler = flowScheduler;
    }

    @Override
    public void distributeDomainFlow(JobFlow flow, List<MetaDomain> subDomains, boolean recursively) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

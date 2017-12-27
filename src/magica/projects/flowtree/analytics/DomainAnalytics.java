package magica.projects.flowtree.analytics;

import java.util.LinkedList;
import java.util.List;
import magica.projects.flowtree.core.JobFlow;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public class DomainAnalytics {

    public long domainID = 0;

    public Double mipsRegressionCoef = 1d;
    public Double minorLoadProblemThreshold = 0.3;
    public Double majorLoadProblemThreshold = 0.05;
    public Double loadSuccessThreshold = 0.9;
    public Double cautiousChangePercents = 0.05;
    public Double majorChangePercents = 0.1;

    protected List<Double> loadHistory;
    protected List<Double> costHistory;
    protected List<Double> mipsHistory;
    protected List<Double> volumeHistory;
    protected Double averageLoad;

    public DomainAnalytics() {
        loadHistory = new LinkedList<>();
        costHistory = new LinkedList<>();
        volumeHistory = new LinkedList<>();
        mipsHistory = new LinkedList<>();
        averageLoad = 0d;
    }

    public void clear() {
        loadHistory.clear();
        costHistory.clear();
        mipsHistory.clear();
        volumeHistory.clear();
        averageLoad = 0d;
    }

    public void update(MetaDomain domain, JobFlow flow) {

        this.domainID = domain.getId();

        Double load = calculateDomainLoad(domain, flow);

        loadHistory.add(load);
        costHistory.add(domain.getCostRating());
        mipsHistory.add(domain.getMIPSRating());
        volumeHistory.add(domain.getVolumeCapacity());

        averageLoad = ((averageLoad * (loadHistory.size() - 1)) + load) / loadHistory.size();

        if (domain.getNextLevelDomains().isEmpty()) {   // if that's a computing leaf domain
            updateDomainCharacteristics(domain, load);
        }
    }

    public void recalculateMIPS(MetaDomain domain, JobFlow flow) {
        Double load = calculateDomainLoad(domain, flow);
        if (load <= 1) {
            domain.setMIPSRating(domain.getBaseMIPSsRating());  // if we aren't overloaded - we have base MIPS rating
        } else {
            domain.setMIPSRating(domain.getBaseMIPSsRating() / (load * mipsRegressionCoef));  // else our MIPS is lower proportionally to load
        }
    }

    protected Double calculateDomainLoad(MetaDomain domain, JobFlow flow) {
        Double usedVolume = 0d;
        if (domain.getAssignedJobFlow() != null) {
            domain.getAssignedJobFlow().updateVolume();
            usedVolume = domain.getAssignedJobFlow().volume;
        }

        return usedVolume / domain.getVolumeCapacity();
    }

    protected void updateDomainCharacteristics(MetaDomain domain, Double load) {

        if (load < minorLoadProblemThreshold) {     // minor problems
            domain.setCostRating(domain.getCostRating() * (1 + cautiousChangePercents));    // cost is little better
            domain.setVolumeCapacity(domain.getVolumeCapacity() * (1 - cautiousChangePercents)); // volume is little smaller
        } else if (load < majorLoadProblemThreshold) {
            domain.setCostRating(domain.getCostRating() * (1 + majorChangePercents));   // cost is really better
            domain.setVolumeCapacity(domain.getVolumeCapacity() * (1 - majorChangePercents));   // volume is really smaller
        } else if (load > loadSuccessThreshold) {
            domain.setCostRating(domain.getCostRating() * (1 - cautiousChangePercents));    // cost is little worse
            domain.setVolumeCapacity(domain.getVolumeCapacity() * (1 + cautiousChangePercents));  // volume is little bigger
        }
        
        if(domain.getCostRating() > 1){
            domain.setCostRating(1d);
        }
        
        if(domain.getMIPSRating()> 1){
            domain.setMIPSRating(1d);
        }

        domain.notifyAboutChanges();
    }

    public String getData() {

        StringBuilder sb = new StringBuilder("Domain Analytics " + domainID + "\n");
        sb.append("Average Load: ").append(averageLoad).append("\n");
        sb.append("Load History: ").append(loadHistory).append("\n");
        sb.append("Cost History: ").append(costHistory).append("\n");
        sb.append("MIPS History: ").append(mipsHistory).append("\n");
        sb.append("Volume History: ").append(volumeHistory).append("\n");

        return sb.toString();
    }
}

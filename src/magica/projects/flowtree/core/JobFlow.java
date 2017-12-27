
package magica.projects.flowtree.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author emelyanov
 */
public class JobFlow {

    public Double volume;
    public List<Job> jobs;
    public Map<MetaDomain, Double> distributionMapPlan;
    public Map<MetaDomain, Double> distributionMap;
    
    public JobFlow() {
        jobs = new ArrayList<>();
    }
    
    public Double updateVolume(){
        volume = 0d;
        for(Job job : jobs){
            volume += job.getVolume();
        }
        
        return volume;
    }
    
    public void clear(){
        jobs.clear();
    }
    
    public JobFlow clone(){
        JobFlow newFlow = new JobFlow();
        newFlow.jobs = new LinkedList<Job>();
        for(Job job : jobs){
            newFlow.jobs.add(job.clone());
        }
        newFlow.updateVolume();
        
        return newFlow;
    }
}

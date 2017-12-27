
package magica.projects.flowtree.analytics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.JobFlow;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public class GlobalAnalytics {
    
    protected static Map<String, DomainsCumulativeAnalytics> analytics = new HashMap<>();
    protected static String analyticsStream = "default";
  
    public static void setMode(String stream){
        analyticsStream = stream;
    }
    
    protected static DomainsCumulativeAnalytics getCurrentDomainsAnalytics(){
        if(analytics.containsKey(analyticsStream)){
            return analytics.get(analyticsStream);
        }else{
            DomainsCumulativeAnalytics da = new DomainsCumulativeAnalytics();
            analytics.put(analyticsStream, da);
            return da;
        }
    }
    
    public static void clear(){
        getCurrentDomainsAnalytics().clear();
    }
    
    public static void analyze(MetaDomain domain, JobFlow flow){
        getCurrentDomainsAnalytics().analyze(domain, flow);
    }
    
    public static void reportJobsReturned(List<Job> jobs){
        getCurrentDomainsAnalytics().reportJobsReturned(jobs);
    }
    
    public static String getData(){
        
        return getCurrentDomainsAnalytics().getData();
    }
    
    public static int getReturnedJobsNumber(){
        return getCurrentDomainsAnalytics().jobsReturnedNumber;
    }
}

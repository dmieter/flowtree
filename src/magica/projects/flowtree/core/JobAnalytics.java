package magica.projects.flowtree.core;

/**
 *
 * @author dmieter
 */
public class JobAnalytics {

    protected static final Double BUDGET_INCREASE_STANDARD = 0.2d;
    protected static final Double COST_EXPECT_INCREASE_STANDARD = 0.05d;

    public static void updateReturnedJob(Job job) {
        
        // job returned, we need stronger budget
        job.budget *= (1 - BUDGET_INCREASE_STANDARD);    
        
        // we increased budget, we have higher expectations on cost criterion
        job.costExpext *= (1 + COST_EXPECT_INCREASE_STANDARD);
        
        if(job.costExpext > 1){
            job.costExpext = 1d;
        }
    }
}


package magica.projects.flowtree.scheduler.job;

/**
 *
 * @author emelyanov
 */
public class JobAssignmentStrategies {
    
    /* _S - SAVE Effort strategies which try to assign poor jobs even if there are no spare place or cost available */
    public static JobAssigner COST_S = new AssignmentStrategyCost(true);
    public static JobAssigner MIPS_S = new AssignmentStrategyMIPS(true);
    public static JobAssigner CRITERION_S = new AssignmentStrategyCriterion(true);
    public static JobAssigner FIRST_FIT_S = new AssignmentStrategyRandom(true);
    
    /* _D - DISMISS strategies which assign poor jobs to domains with most capacity available */
    public static JobAssigner COST_D = new AssignmentStrategyCost(false);
    public static JobAssigner MIPS_D = new AssignmentStrategyMIPS(false);
    public static JobAssigner CRITERION_D = new AssignmentStrategyCriterion(false);
    public static JobAssigner FIRST_FIT_D = new AssignmentStrategyRandom(false);
}


package magica.projects.flowtree.scheduler;

/**
 *
 * @author emelyanov
 */
public class DoubleFlowSettings {
    
    public Double costWeight = 1d;
    public Double mipsWeight = 1d;
    public Double budgetCategoryLevel = 0.9d;
    
    public DoubleFlowSettings(){
        
    }
    
    public DoubleFlowSettings(Double costWeight, Double mipsWeight, Double budgetCategoryLevel){
        this.costWeight = costWeight;
        this.mipsWeight = mipsWeight;
        this.budgetCategoryLevel = budgetCategoryLevel;
    }
    
}

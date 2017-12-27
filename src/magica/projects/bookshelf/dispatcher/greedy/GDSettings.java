/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.dispatcher.greedy;

/**
 *
 * @author magica
 */
public class GDSettings {
    protected boolean limited = false;
    protected double budgetLimit = 0;
    
    public GDSettings(boolean limited, double budgetLimit){
        this.limited = limited;
        this.budgetLimit = budgetLimit;
    }

    /**
     * @return the limited
     */
    public boolean isLimited() {
        return limited;
    }

    /**
     * @param limited the limited to set
     */
    public void setLimited(boolean limited) {
        this.limited = limited;
    }

    /**
     * @return the budgetLimit
     */
    public double getBudgetLimit() {
        return budgetLimit;
    }

    /**
     * @param budgetLimit the budgetLimit to set
     */
    public void setBudgetLimit(double budgetLimit) {
        this.budgetLimit = budgetLimit;
    }
}

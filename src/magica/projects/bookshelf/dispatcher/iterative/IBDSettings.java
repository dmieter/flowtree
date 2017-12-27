/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.dispatcher.iterative;

import magica.projects.bookshelf.dispatcher.BooksDispatcher;

/**
 *
 * @author magica
 */
public class IBDSettings {
    protected boolean limited = false;
    protected double budgetLimit = 0;
    protected BooksDispatcher dispatcher;
    protected ShelfEliminator shelfEliminator;
    
    protected int fillerAmount = 10;
    
    public IBDSettings(boolean limited, double budgetLimit){
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

    /**
     * @return the fillerAmount
     */
    public int getFillerAmount() {
        return fillerAmount;
    }

    /**
     * @param fillerAmount the fillerAmount to set
     */
    public void setFillerAmount(int fillerAmount) {
        this.fillerAmount = fillerAmount;
    }

    /**
     * @param shelfEliminator the shelfEliminator to set
     */
    public void setShelfEliminator(ShelfEliminator shelfEliminator) {
        this.shelfEliminator = shelfEliminator;
    }

    /**
     * @return the shelfEliminator
     */
    public ShelfEliminator getShelfEliminator() {
        return shelfEliminator;
    }
}

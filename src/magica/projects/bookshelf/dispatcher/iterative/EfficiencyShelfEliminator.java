/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.dispatcher.iterative;

import magica.projects.bookshelf.core.Shelf;
import magica.projects.bookshelf.core.Wall;

/**
 *
 * @author magica
 */
public class EfficiencyShelfEliminator implements ShelfEliminator {

    @Override
    public Shelf getEliminatedShelf(Wall wall, double budgetLimit){
        Shelf worstShelf = null;
        
        double minEfficiency = Double.POSITIVE_INFINITY;
        
        for(Shelf shelf : wall.getShelves()){
            double efficiency = calculateShelfEfficiency(shelf, wall, budgetLimit);
            if(efficiency < minEfficiency){
                minEfficiency = efficiency;
                worstShelf = shelf;
            }
        }
        
        return worstShelf;
    }
    
    protected double calculateShelfEfficiency(Shelf shelf,Wall wall, double budgetLimit){
        
        return -shelf.getPrice();    /* The lower the cost - the higher the efficiency */
    }
    
}

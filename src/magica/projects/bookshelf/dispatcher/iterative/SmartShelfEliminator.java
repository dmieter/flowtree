/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.dispatcher.iterative;

import magica.projects.bookshelf.dispatcher.*;
import java.util.List;
import magica.projects.bookshelf.core.Shelf;
import magica.projects.bookshelf.core.Wall;

/**
 *
 * @author magica
 */
public class SmartShelfEliminator extends EfficiencyShelfEliminator {

    
    @Override
    protected double calculateShelfEfficiency(Shelf shelf,Wall wall, double budgetLimit){
        
        double avHeight = wall.getAverageHeight();
        double sumCost = wall.getSumCost();
        int booksCount = wall.getBooksCount();
        
        double costWeight = 1;
        double heightWeight = 1;
        
        double costFactor = -shelf.getPrice()*shelf.getCost()/sumCost;
        
        double heightFactor = (shelf.getHeight()*shelf.getBooksCount())
                                    /(avHeight*booksCount);
        
        return heightWeight*heightFactor + costWeight*costFactor;
        
        //return 0;
    }
    
}

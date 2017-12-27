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
public interface ShelfEliminator {
    
    public abstract Shelf getEliminatedShelf(Wall wall, double budgetLimit); 
    
}

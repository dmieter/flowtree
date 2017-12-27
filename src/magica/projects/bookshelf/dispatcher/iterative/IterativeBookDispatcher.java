/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.dispatcher.iterative;

import java.util.List;
import magica.projects.bookshelf.core.Book;
import magica.projects.bookshelf.core.Shelf;
import magica.projects.bookshelf.core.Wall;
import magica.projects.bookshelf.dispatcher.BooksDispatcher;
import magica.projects.bookshelf.dispatcher.VolumeOrderSimpleDispatcher;

/**
 *
 * @author magica
 */
public class IterativeBookDispatcher extends BooksDispatcher {

    protected IBDSettings settings;
    protected BooksDispatcher fastDispatcher;
    protected Wall wall;
    protected List<Book> books;
    
    public IterativeBookDispatcher(IBDSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public boolean putBooks(Wall wall, List<Book> books) {
        this.wall = wall;
        this.books = books;
        
        return fillWall();
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(IBDSettings settings) {
        this.settings = settings;
    }

    protected boolean fillWall() {
        if(!settings.isLimited()){
            fillWallSimple();
            return true;
        }else{
            return fillWallIteratively();
        }
    }

    protected boolean fillWallIteratively() {
        
        double sumCost = Double.MAX_VALUE;
        int maxCycles = 1000;
        int cycles = 0;
        boolean allBooksPut = false;
        
        while(cycles < maxCycles){
            wall.pullBooks();
            allBooksPut = fillWallSimple();
            sumCost = wall.getSumCost();
            if(allBooksPut && sumCost < settings.getBudgetLimit()){
                return true;
            }else{
                Shelf shelf = settings.getShelfEliminator().getEliminatedShelf(wall, settings.getBudgetLimit());
                shelf.addFiller(settings.getFillerAmount());
            }
            cycles++;
        }
        return false;
    }
    
    protected boolean fillWallSimple(){
        BooksDispatcher dispatcher = new VolumeOrderSimpleDispatcher();
        return dispatcher.putBooks(wall, books);
    }
    
}

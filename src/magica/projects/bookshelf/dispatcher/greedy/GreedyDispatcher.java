/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.dispatcher.greedy;

import java.util.ArrayList;
import java.util.List;
import magica.projects.bookshelf.core.Book;
import magica.projects.bookshelf.core.Shelf;
import magica.projects.bookshelf.core.Wall;
import magica.projects.bookshelf.dispatcher.BooksDispatcher;

/**
 *
 * @author magica
 */
public class GreedyDispatcher extends BooksDispatcher{

    protected GDSettings settings;
    
    protected List<BookAssignment> bestAssignments;
    protected double bestValue = Double.NEGATIVE_INFINITY;
    protected double curCost = Double.MAX_VALUE;
    
    protected ArrayList<BookAssignment> assignments;

    public GreedyDispatcher() {
        settings = new GDSettings(false, 0);
    }
    
    public GreedyDispatcher(GDSettings settings) {
        this.settings = settings;
    }
    
    @Override
    public boolean putBooks(Wall wall, List<Book> books) {
        initAssignments(wall, books);
        checkAssignment(wall);  //Initial assignment check
        while(nextAssignment()){    // Full search
            //printStatus();
            checkAssignment(wall);  // Check next assignment
        }
        
        if(bestAssignments != null){
            assignBooks(wall, bestAssignments);
            return true;
        }else{
            return false;
        }
    }
 
    protected void checkAssignment(Wall wall){
         if(assignBooks(wall, assignments)){
                double value = wall.getAverageHeight();
                if(value > bestValue){
                    double cost = wall.getSumCost();
                    if(!settings.limited || cost <= settings.getBudgetLimit()){
                        bestValue = value;
                        curCost = cost;
                        saveCurrentAssignment();
                        System.out.println("New best value: "+bestValue+" Cost: "+cost);
                    }
                }else if(value == bestValue){
                    double cost = wall.getSumCost();
                    if(cost < curCost){
                        curCost = cost;
                        saveCurrentAssignment();
                        System.out.println("New best cost, value: "+bestValue+" Cost: "+cost);
                    }
                }
            }
    }
    
    protected void initAssignments(Wall wall, List<Book> books){
        assignments = new ArrayList<BookAssignment>();
        int shelvesNum = wall.getShelves().size();
        for(Book book : books){
            assignments.add(new BookAssignment(book, shelvesNum));
        }
    }
    
    protected boolean assignBooks(Wall wall, List<BookAssignment> bas){
        wall.pullBooks();
        for(BookAssignment ba : bas){
            Shelf shelf = wall.getShelves().get(ba.value);
            if(!shelf.putBook(ba.book)){
                return false;
            }
        }
        return true;
    }
    
    protected boolean nextAssignment(){
        return incrementAssignment(0);
    }
    
    protected boolean incrementAssignment(int num){
        if(num > assignments.size() - 1){
            return false;   // last combination achieved
        }
        
        BookAssignment ba = assignments.get(num);
        ba.value++;
        if(ba.value == ba.maxValue){
            ba.value = 0;
            return incrementAssignment(num+1);  // goto next assignment
        }else{
            return true;
        }
    }
    
    protected void saveCurrentAssignment(){
        bestAssignments = new ArrayList<BookAssignment>(assignments.size());
        try{
            for(BookAssignment ba : assignments){
                bestAssignments.add(ba.clone());
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    protected void printStatus(){
        String status = "";
        for(BookAssignment ba : assignments){
            status += ba.value+" ";
        }
        System.out.println(status);
    }
    
}

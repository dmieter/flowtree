/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.core;

import java.util.ArrayList;

/**
 *
 * @author magica
 */
public class Wall implements Cloneable{
    
    protected ArrayList<Shelf> shelves;
    protected int id;

    
    public Wall(int id){
        this.id = id;
        shelves = new ArrayList<Shelf>();
    }
    
    public String getDetails(){
        return "Wall #" + id;
    }
    
    public String getFullDetails(){
        String details = "Wall #" + id;
        
        if(shelves.size() > 0){
            details += ": ";
            for(Shelf shelf : shelves){
                details += shelf.getHeight() +"["+shelf.getVolume()+"v | "
                                                 +shelf.getFreeVolume()+"fv + "+shelf.getFiller()+"f ]" + "(";
                if(shelf.getBooks().size() > 0){
                    for(Book book : shelf.getBooks()){
                        details += book.getId()+"["+book.getVolume()+"] ";
                    }
                }
                details += ") ";
            }
        }else{
            details = "The wall #" + id + " is empty!";
        }
        
        return details;
    }
    
    public String getCostDetails(){
       String details = "Sum cost: " + getSumCost()+" ";
       double cost = 0;
       for(Shelf shelf : shelves){
           details += "Shelf #" + shelf.getOrderNumber()+ ": " + shelf.getCost() + " ";
       }
       return details;
    }
            
    /**
     * @return the shelves
     */
    public ArrayList<Shelf> getShelves() {
        return shelves;
    }

    /**
     * @param shelves the shelves to set
     */
    public void setShelves(ArrayList<Shelf> shelves) {
        this.shelves = shelves;
    }
    
    
    public void addShelf(Shelf shelf){
        if(shelves.contains(shelf)){
            throw new RuntimeException("The shelf "+ shelf.getDetails() +"is already on the wall "+ getDetails());
        }else{
            shelves.add(shelf);
        }
    }
    
    public boolean removeShelf(Shelf shelf){
        if(shelves.contains(shelf)){
            shelves.remove(shelf);
            return true;
        }else{
            return false;
        }
            
    }
    
    public double getAverageHeight(){
       double height = 0;
       int booksNum = 0;
       for(Shelf shelf : shelves){
           booksNum += shelf.getBooksCount();
           height += shelf.getHeight()*shelf.getBooksCount();
       }
       if(booksNum > 0){
           return height/booksNum;
       }else{
           return 0;
       }
    }
    
    public double getSumCost(){
       double cost = 0;
       for(Shelf shelf : shelves){
           cost += shelf.getCost();
       }
       return cost;
    }
    
    public int getBooksCount(){
        int cnt = 0;
        for(Shelf shelf : shelves){
            cnt += shelf.getBooksCount();
        }
        
        return cnt;
    }
    
    public void pullBooks(){
        for(Shelf shelf : shelves){
            shelf.pullBooks();
            if(shelf.freeVolume < 0){
                int a = 0;
            }
        }
        
    }
    
    @Override
    public Wall clone() throws CloneNotSupportedException{
        Wall wall = new Wall(id);
        
        for(Shelf shelf : shelves){
            wall.addShelf(shelf.clone());
        }
        
        return wall;
    }
}

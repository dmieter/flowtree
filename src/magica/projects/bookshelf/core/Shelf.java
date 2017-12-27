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
public class Shelf implements Cloneable{
    protected int orderNumber;
    protected double height;
    protected double price;
    protected double volume;
    protected double freeVolume;
    protected double filler;
    
    protected ArrayList<Book> books;
    
    public Shelf(int orderNumber, double height, double price, double volume){
        this.orderNumber = orderNumber;
        this.height = height;
        this.price = price;
        this.volume = volume;
        freeVolume = volume;
        filler = 0;
        
        books = new ArrayList<Book>();
    }
    
    public boolean putBook(Book book){
        if(freeVolume < book.getVolume()){
            return false;
        }
        
        if(book.getShelf() != null){
            throw new RuntimeException("The book "+ book.getDetails() +"is already assigned to "+ book.getShelf().getDetails());
        }
        
        if(books.contains(book)){
            throw new RuntimeException("The book "+ book.getDetails() +"is already assigned to "+ getDetails());
        }
        
        books.add(book);
        freeVolume -= book.getVolume();
        book.setShelf(this);
        
        return true;
    }
    
    public boolean pullBook(Book book){
        if(!books.contains(book)){
            return false;
        }
        
        books.remove(book);
        freeVolume += book.getVolume();
        book.setShelf(null);
        
        return true;
    }
    
    public double getCost(){
        double cost = 0;
        if(!books.isEmpty() && price != 0){
            for(Book book : books){
                cost += book.getVolume()*price;
            }
        }
        
        return cost;
    }
    
    public void pullBooks(){
        for(Book book : books){
            book.setShelf(null);
        }
        
        books = new ArrayList<Book>();
        freeVolume = volume - filler;
    }

    
     @Override
    public boolean equals(Object arg0) {

        Shelf shelf=(Shelf)arg0;
        
        return this.orderNumber == shelf.getOrderNumber();
    }


    @Override
    public int hashCode() {
        return orderNumber;
    }
    
    
    public String getDetails(){
        return " Shelf #"+orderNumber+"(Height: "+height
                +", Volume: "+freeVolume+"("+volume+"), Filler: "+ filler
                +", Books: "+books.size()+")";
    }
    
    public String getFullDetails(){
        String details = " Shelf #"+orderNumber+"(Height: "+height
                +", Volume: "+freeVolume+"("+volume+"), Filler: "+ filler
                +", Books: "+books.size();
        
        details += ": ";
        if(books.size() > 0){
            for(Book book : books){
                details += book.getDetails();
            }
        }
        
        details += ")";
        
        return details;
    }
    
    public int getBooksCount(){
        return books.size();
    }

    /**
     * @return the height
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * @return the volume
     */
    public double getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(double volume) {
        this.volume = volume;
    }

    /**
     * @return the orderNumber
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param orderNumber the orderNumber to set
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * @return the books
     */
    public ArrayList<Book> getBooks() {
        return books;
    }

    /**
     * @param books the books to set
     */
    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    /**
     * @return the freeVolume
     */
    public double getFreeVolume() {
        return freeVolume;
    }
    
    public double recalculateFreeVolume(){
        freeVolume = volume - filler;
        for(Book book : books){
            freeVolume -= book.getVolume();
        }
        
        return freeVolume;
    }
    
    @Override
    public Shelf clone() throws CloneNotSupportedException{
        Shelf shelf = new Shelf(orderNumber, height, getPrice(), volume);
        shelf.setFiller(filler);
        for(Book book : books){
            shelf.putBook(book.clone());
        }
        
        return shelf;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the filler
     */
    public double getFiller() {
        return filler;
    }

    /**
     * @param filler the filler to set
     */
    public void setFiller(double filler) {
        double newFiller = filler;
        if(volume < filler){
            newFiller = volume;
        }
        freeVolume += this.filler;
        this.filler = newFiller;
        freeVolume -= this.filler;
    }
    
    public void addFiller(double filler){
        double deltaFiller = filler;
        if(volume < this.filler +filler){       /* We can't place more filler than maximum shelf volume */
            deltaFiller = volume - this.filler;
        }
        this.filler += deltaFiller;
        freeVolume -= deltaFiller;
    }
}

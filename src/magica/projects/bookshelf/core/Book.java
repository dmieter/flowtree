/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.core;

/**
 *
 * @author magica
 */
public class Book implements Cloneable{
    protected int id;
    protected String name;
    protected double volume;
    
    private Shelf shelf;

    public Book(int id, String name, double volume){
        this.id = id;
        this.name = name;
        this.volume = volume;
    }
    
    public boolean pull(){
        
        if(shelf != null){
            if(!shelf.pullBook(this)){
                throw new RuntimeException(" Exception when pullin book from shelf "+ shelf.getDetails());
            }
        }
        
        return true;
    }
    
    public boolean put(Shelf shelf){
        return shelf.putBook(this);
    }
    
    public String getDetails(){
        return " Book \""+name+"\" (Volume: "+volume+", id: "+id + ");";
    }
    
     @Override
    public boolean equals(Object arg0) {

        Book book=(Book)arg0;
        
        return this.id == book.getId();
    }


    @Override
    public int hashCode() {
        return id;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the shelf
     */
    public Shelf getShelf() {
        return shelf;
    }

    /**
     * @param shelf the shelf to set
     */
    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    
    public Book clone() throws CloneNotSupportedException{
        Book book = (Book) super.clone();
        book.setShelf(null);
        return book;
    }

}

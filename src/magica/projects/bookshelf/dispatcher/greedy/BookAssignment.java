/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.dispatcher.greedy;

import magica.projects.bookshelf.core.Book;

/**
 *
 * @author magica
 */
public class BookAssignment implements Cloneable{
    public Book book;
    
    public int value;
    public int maxValue;
    
    public BookAssignment(Book book, int maxValue){
        this.book = book;
        this.maxValue = maxValue;
        value = 0;
    }
    
    @Override
    public BookAssignment clone() throws CloneNotSupportedException{
        return (BookAssignment)super.clone();
    }
    
    public BookAssignment copy(){
        BookAssignment ba = new BookAssignment(book, maxValue);
        ba.value = value;
        return ba;
    }
}
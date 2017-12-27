/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import magica.projects.bookshelf.core.Book;
import magica.projects.bookshelf.core.Shelf;
import magica.projects.bookshelf.core.Wall;

/**
 *
 * @author magica
 */
public class BookShelfFunctions {
    
    public static void orderBooksByVolumeDesc(ArrayList<Book> books){
        Comparator<Book> comparator = new Comparator<Book>() {
            public int compare(Book b1, Book b2) {
                if(b1.getVolume() < b2.getVolume()){
                    return 1;
                }else if(b1.getVolume() > b2.getVolume()){
                    return -1;
                }else{
                    return 0;
                }
            }
        };

        Collections.sort(books, comparator); // use the comparator as much as u want
    }
    
    public static void orderBooksByVolumeInc(List<Book> books){
        Comparator<Book> comparator = new Comparator<Book>() {
            public int compare(Book b1, Book b2) {
                if(b1.getVolume() > b2.getVolume()){
                    return 1;
                }else if(b1.getVolume() < b2.getVolume()){
                    return -1;
                }else{
                    return 0;
                }
            }
        };

        Collections.sort(books, comparator); // use the comparator as much as u want
    }
    
    public static void orderShelvesByHeightDesc(ArrayList<Shelf> shelves){
        Comparator<Shelf> comparator = new Comparator<Shelf>() {
            public int compare(Shelf s1, Shelf s2) {
                if(s1.getHeight() < s2.getHeight()){
                    return 1;
                }else if(s1.getHeight() > s2.getHeight()){
                    return -1;
                }else{
                    return 0;
                }
            }
        };

        Collections.sort(shelves, comparator); // use the comparator as much as u want
    }
    
    
    public static void orderShelvesByHeightInc(ArrayList<Shelf> shelves){
        Comparator<Shelf> comparator = new Comparator<Shelf>() {
            public int compare(Shelf s1, Shelf s2) {
                if(s1.getHeight() > s2.getHeight()){
                    return 1;
                }else if(s1.getHeight() < s2.getHeight()){
                    return -1;
                }else{
                    return 0;
                }
            }
        };

        Collections.sort(shelves, comparator); // use the comparator as much as u want
    }
    
}

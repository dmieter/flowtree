/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.dispatcher;

import java.util.Iterator;
import java.util.List;
import magica.projects.bookshelf.BookShelfFunctions;
import magica.projects.bookshelf.core.Book;
import magica.projects.bookshelf.core.Shelf;
import magica.projects.bookshelf.core.Wall;

/**
 *
 * @author magica
 */
public class VolumeOrderSimpleDispatcher extends BooksDispatcher {

    @Override
    public boolean putBooks(Wall wall, List<Book> books) {
        if(wall.getShelves().isEmpty() || books.isEmpty()){
            return true;     // Nothing to dispatch
        }
        
        Iterator<Shelf> shelfIt = wall.getShelves().iterator();
        Shelf shelf = shelfIt.next();
        
        BookShelfFunctions.orderBooksByVolumeInc(books);
        
        for(Book book : books){
            while(!shelf.putBook(book)){
                if(shelfIt.hasNext()){
                    shelf = shelfIt.next();
                }else{
                    return false;  // Can't poot book on the shelf
                }
            }
        }
        
        return true;    // all books are assigned
    }
    
}

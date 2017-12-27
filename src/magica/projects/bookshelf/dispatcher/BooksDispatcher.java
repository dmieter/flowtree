/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.dispatcher;

import java.util.List;
import magica.projects.bookshelf.core.Book;
import magica.projects.bookshelf.core.Wall;

/**
 *
 * @author magica
 */
public abstract class BooksDispatcher {
    abstract public boolean putBooks(Wall wall, List<Book> books);
}
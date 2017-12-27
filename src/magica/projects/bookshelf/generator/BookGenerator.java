/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.generator;

import java.util.ArrayList;
import java.util.List;
import magica.projects.bookshelf.core.Book;
import magica.projects.math.MathUtils;

/**
 *
 * @author magica
 */
public class BookGenerator {
    
    protected BookGeneratorSettings settings;
    protected double budget = 0;
    
    public BookGenerator(BookGeneratorSettings settings){
        this.settings = settings;
    }
    
    public List<Book> generate(){
        
        ArrayList<Book> books = new ArrayList<Book>();
        double sumVolume = 0;
        for(int i=0;i<settings.booksCount;i++){
            double volume = MathUtils.getGaussian(settings.minVolume, settings.maxVolume);
            Book book = new Book(settings.idPrefix+i, settings.namePrefix+"#"+i, volume);
            books.add(book);
            sumVolume += volume;
        }
        
        double budgetCoef = MathUtils.getGaussian(settings.minPriceCoef, settings.minPriceCoef);
        budget = sumVolume*budgetCoef;
        
        return books;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.launch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import magica.projects.bookshelf.BookShelfFunctions;
import magica.projects.bookshelf.core.Book;
import magica.projects.bookshelf.core.Shelf;
import magica.projects.bookshelf.core.Wall;
import magica.projects.bookshelf.dispatcher.BooksDispatcher;
import magica.projects.bookshelf.dispatcher.VolumeOrderSimpleDispatcher;
import magica.projects.bookshelf.dispatcher.greedy.GDSettings;
import magica.projects.bookshelf.dispatcher.greedy.GreedyDispatcher;
import magica.projects.bookshelf.dispatcher.iterative.EfficiencyShelfEliminator;
import magica.projects.bookshelf.dispatcher.iterative.IBDSettings;
import magica.projects.bookshelf.dispatcher.iterative.IterativeBookDispatcher;
import magica.projects.bookshelf.dispatcher.iterative.SmartShelfEliminator;
import magica.projects.bookshelf.dispatcher.lp.LPSolver;
import magica.projects.bookshelf.generator.BookGenerator;
import magica.projects.bookshelf.generator.BookGeneratorSettings;
import magica.projects.bookshelf.generator.ShelfGenerator;
import magica.projects.bookshelf.generator.ShelfGeneratorSettings;
import magica.projects.bookshelf.generator.WallGenerator;
import magica.projects.bookshelf.generator.WallGeneratorSettings;

/**
 *
 * @author magica
 */
public class BookShelfMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try{

            LPSolver lp = new LPSolver();   
            
            System.out.println("BookShelf start");
            List<Book> books = new ArrayList<Book>();
            Book book1 = new Book(1, "Trololo", 10);
            Book book2 = new Book(2, "War and Peace", 60);
            Book book3 = new Book(3, "Dune", 50);
            Book book4 = new Book(4, "History", 11);
            Book book5 = new Book(5, "History", 22);
            Book book6 = new Book(6, "History", 30);
            Book book7 = new Book(7, "History", 20);
            Book book8 = new Book(8, "History", 23);
            books.add(book1);
            //books.add(book2);
            //books.add(book3);
            //books.add(book4);
            books.add(book5);
            books.add(book6);
            books.add(book7);
            books.add(book8);
            
            books = generateBooks();

            Shelf shelf1 = new Shelf(1, 2, 0, 140);
            Shelf shelf2 = new Shelf(2, 8, 1, 110);
            Shelf shelf3 = new Shelf(3, 15, 2, 80);

            Wall wall = new Wall(1);

            //shelf1.putBook(book1);
            //shelf1.putBook(book2);


            wall.addShelf(shelf3);
            wall.addShelf(shelf2);
            wall.addShelf(shelf1);
            

            Wall wall2 = generateTestWall();
            wall2.addShelf(new Shelf(100,0,0,1000));
            BookShelfFunctions.orderShelvesByHeightDesc(wall2.getShelves());
            
            Map<Shelf, Double> res = lp.calculateOptimalLoad(wall2, books, 200d);
            
            BooksDispatcher sd = new VolumeOrderSimpleDispatcher();
            
            GDSettings gds = new GDSettings(true, 200);
            GreedyDispatcher gd = new GreedyDispatcher(gds);
            
            IBDSettings ids =  new IBDSettings(true, 200);
            ids.setShelfEliminator(new SmartShelfEliminator());
            ids.setFillerAmount(5);
            IterativeBookDispatcher ibd = new IterativeBookDispatcher(ids);
            
            
            System.out.println("ITERATIVE DISPATCHER RESULTS");
            BooksDispatcher dispatcher = ibd;
            System.out.println(dispatcher.putBooks(wall2, books));
            
            System.out.println("Final height: " + wall2.getAverageHeight());
            System.out.println("Final cost: " + wall2.getSumCost());
            System.out.println(wall2.getFullDetails());
            wall2.pullBooks();
            
            
            System.out.println("SIMPLE ITERATIVE DISPATCHER RESULTS");
            
            ids.setShelfEliminator(new EfficiencyShelfEliminator());
            ibd = new IterativeBookDispatcher(ids);
            dispatcher = ibd;
            System.out.println(dispatcher.putBooks(wall2, books));
            
            System.out.println("Final height: " + wall2.getAverageHeight());
            System.out.println("Final cost: " + wall2.getSumCost());
            System.out.println(wall2.getFullDetails());
            wall2.pullBooks();

            
            System.out.println("GREEDY DISPATCHER RESULTS");
            dispatcher = gd;
            System.out.println(dispatcher.putBooks(wall2, books));
            
            System.out.println("Final height: " + wall2.getAverageHeight());
            System.out.println("Final cost: " + wall2.getSumCost());
            System.out.println(wall2.getFullDetails());
        
        } catch(Exception e){
            throw new RuntimeException(e);
        }

    }
    
    public static Wall generateTestWall(){
        ShelfGeneratorSettings sgs = new ShelfGeneratorSettings();
        ShelfGenerator sg = new ShelfGenerator(sgs);
        
        sgs.setVolumeDistribution(20, 100);
        sgs.setHeightDistribution(0, 15);
        sgs.setBookVolumeDistribution(10, 100);
        sgs.setFreeVolumeRatioDistribution(0.1, 0.4);
        sgs.setIntegerValues(true);
        sgs.setPriceMutationFactor(0.6);
        
        WallGeneratorSettings wgs = new WallGeneratorSettings(2 /* id */, 4/* min shelves */, 10/* max shelves */);
        wgs.setShelfGenerator(sg);
        WallGenerator wg = new WallGenerator(wgs);
        
        return wg.generate();
    }
    
    public static List<Book> generateBooks(){
        BookGeneratorSettings bgs = new BookGeneratorSettings(100, "Tolstoy", 6);
        bgs.setVolumeDistribution(10, 25);
        bgs.setPriceCoefDistribution(1.5, 3);
        BookGenerator bg = new BookGenerator(bgs);
        return bg.generate();
    }
    
}

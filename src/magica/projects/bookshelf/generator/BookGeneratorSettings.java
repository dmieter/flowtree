/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package magica.projects.bookshelf.generator;

/**
 *
 * @author magica
 */
public class BookGeneratorSettings {
    
    protected String namePrefix = "Book";
    protected int idPrefix = 0;
    
    protected int booksCount;
    
    protected double minVolume;
    protected double maxVolume;
    
    protected double minPriceCoef = 1;
    protected double maxPriceCoef = 1;
    
    public BookGeneratorSettings(String namePrefix, int booksCount){
        this.namePrefix = namePrefix;
        this.booksCount = booksCount;
    }
    
    public BookGeneratorSettings(int idPrefix, String namePrefix, int booksCount){
        this.idPrefix = idPrefix;
        this.namePrefix = namePrefix;
        this.booksCount = booksCount;
    }
    
    public void setVolumeDistribution(double minVolume, double maxVolume){
        this.maxVolume = maxVolume;
        this.minVolume = minVolume;
    }
    
    public void setPriceCoefDistribution(double minPriceCoef, double maxPriceCoef){
        this.minPriceCoef = minPriceCoef;
        this.maxPriceCoef = maxPriceCoef;
    }
}

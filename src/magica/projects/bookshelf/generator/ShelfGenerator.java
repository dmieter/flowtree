

package magica.projects.bookshelf.generator;

import magica.projects.math.MathUtils;
import magica.projects.bookshelf.core.Shelf;

/**
 *
 * @author magica
 */
public class ShelfGenerator {
    
    protected ShelfGeneratorSettings settings;
    
    public ShelfGenerator(ShelfGeneratorSettings settings){
        this.settings = settings;
    }
    
    public Shelf generate(){
        
        double height = MathUtils.getUniform(getSettings().getMinHeight(), getSettings().getMaxHeight());
        double volume = MathUtils.getGaussian(getSettings().getMinVolume(), getSettings().getMaxVolume());
        
        if(settings.isIntegerValues()){
            height = MathUtils.nextUp(height);
            volume = MathUtils.nextUp(volume);
        }
        
        double price = generatePrice(height);
        Shelf shelf = new Shelf(getSettings().getOrderNumber(), height, price, volume);
        
        return shelf;
    }

    /**
     * @return the settings
     */
    public ShelfGeneratorSettings getSettings() {
        return settings;
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(ShelfGeneratorSettings settings) {
        this.settings = settings;
    }

    private double generatePrice(double height) {
        double mutationCoef = 1 + MathUtils.getGaussian(-settings.getPriceMutationFactor(), 
                                                         settings.getPriceMutationFactor());
        if(mutationCoef < 0){
            mutationCoef = 0;
        }
        
        return height*mutationCoef;
    }
}

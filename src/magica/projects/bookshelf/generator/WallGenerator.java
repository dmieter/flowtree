

package magica.projects.bookshelf.generator;

import magica.projects.math.MathUtils;
import magica.projects.bookshelf.core.Shelf;
import magica.projects.bookshelf.core.Wall;

/**
 *
 * @author magica
 */
public class WallGenerator {

    protected WallGeneratorSettings settings;
    
    public WallGenerator(WallGeneratorSettings settings){
        this.settings = settings;
    }
    
    public Wall generate(){
        
        Wall wall = new Wall(settings.getId());
        
        int shelvesCnt = (int) MathUtils.nextUp(MathUtils.getGaussian(
                                (double) settings.getMinShelvesCnt(), 
                                (double) settings.getMaxShelvesCnt()));
        
        
        for(int i=0;i<shelvesCnt;i++){
           settings.shelfGenerator.getSettings().setOrderNumber(i);
           Shelf shelf = settings.shelfGenerator.generate();
           wall.addShelf(shelf);
        }
        
        return wall;
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(WallGeneratorSettings settings) {
        this.settings = settings;
    }
}


package magica.projects.bookshelf.generator;

/**
 *
 * @author magica
 */
public class WallGeneratorSettings {
    
    protected int id;
    
    protected int minShelvesCnt;
    protected int maxShelvesCnt;
    
    protected ShelfGenerator shelfGenerator;

    public WallGeneratorSettings(int id, int minShelvesCnt, int maxShelvesCnt){
        this.id =id;
        this.minShelvesCnt = minShelvesCnt;
        this.maxShelvesCnt = maxShelvesCnt;
    }


    /**
     * @return the minShelvesCnt
     */
    public int getMinShelvesCnt() {
        return minShelvesCnt;
    }

    /**
     * @return the maxShelvesCnt
     */
    public int getMaxShelvesCnt() {
        return maxShelvesCnt;
    }

    /**
     * @return the shelfGenerator
     */
    public ShelfGenerator getShelfGenerator() {
        return shelfGenerator;
    }

    /**
     * @param shelfGenerator the shelfGenerator to set
     */
    public void setShelfGenerator(ShelfGenerator shelfGenerator) {
        this.shelfGenerator = shelfGenerator;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    
}

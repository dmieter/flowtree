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
public class ShelfGeneratorSettings {
    
    protected int orderNumber= 0;
    
    protected double minVolume;
    protected double maxVolume;
    
    protected double minHeight;
    protected double maxHeight;
    
    protected double priceMutationFactor = 0;
    
    protected double minFreeVolumeRatio;
    protected double maxFreeVolumeRatio;
    
    protected double minBookVolume;
    protected double maxBookVolume;
    
    protected boolean integerValues = false;

    public void setVolumeDistribution(double minVolume, double maxVolume){
        this.minVolume = minVolume;
        this.maxVolume = maxVolume;
    }
    
    public void setHeightDistribution(double minHeight, double maxHeight){
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }
    
    public void setFreeVolumeRatioDistribution(double minFreeVolumeRatio, double maxFreeVolumeRatio){
        this.minFreeVolumeRatio = minFreeVolumeRatio;
        this.maxFreeVolumeRatio = maxFreeVolumeRatio;
    }
    
    public void setBookVolumeDistribution(double minBookVolume, double maxBookVolume){
        this.minBookVolume = minBookVolume;
        this.maxBookVolume = maxBookVolume;
    }
    
    /**
     * @return the minVolume
     */
    public double getMinVolume() {
        return minVolume;
    }

    /**
     * @return the maxVolume
     */
    public double getMaxVolume() {
        return maxVolume;
    }

    /**
     * @return the minFreeVolumeRatio
     */
    public double getMinFreeVolumeRatio() {
        return minFreeVolumeRatio;
    }

    /**
     * @return the maxFreeVolumeRatio
     */
    public double getMaxFreeVolumeRatio() {
        return maxFreeVolumeRatio;
    }

    /**
     * @return the minBookVolume
     */
    public double getMinBookVolume() {
        return minBookVolume;
    }

    /**
     * @return the maxBookVolume
     */
    public double getMaxBookVolume() {
        return maxBookVolume;
    }

    /**
     * @return the minHeight
     */
    public double getMinHeight() {
        return minHeight;
    }

    /**
     * @return the maxHeight
     */
    public double getMaxHeight() {
        return maxHeight;
    }

    /**
     * @return the orderNumber
     */
    public int getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param orderNumber the orderNumber to set
     */
    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * @return the integerValues
     */
    public boolean isIntegerValues() {
        return integerValues;
    }

    /**
     * @param integerValues the integerValues to set
     */
    public void setIntegerValues(boolean integerValues) {
        this.integerValues = integerValues;
    }

    /**
     * @return the priceMutationFactor
     */
    public double getPriceMutationFactor() {
        return priceMutationFactor;
    }

    /**
     * @param priceMutationFactor the priceMutationFactor to set
     */
    public void setPriceMutationFactor(double priceMutationFactor) {
        this.priceMutationFactor = priceMutationFactor;
    }
    
    
    
}

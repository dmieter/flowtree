
package magica.projects.flowtree.core;

/**
 *
 * @author emelyanov
 */
public class Job {
    protected long id;
    protected Double budget;  // can't execute job in domain with lower cost rating
    protected Double volume;
    protected Double mipsExpext;   // the higher the mips expect - the more mips rating is important
    protected Double costExpext;   // the higher the cost expect - the more cost rating is important
    protected boolean wilco = true;
    
    protected static long JOB_COUNTER = 0; 

    public Job(Double budget, Double costExpectations, Double mipsExpectations, Double volume){
        id = JOB_COUNTER++;
        this.budget = budget;
        this. costExpext = costExpectations;
        this.mipsExpext = mipsExpectations;
        this. volume = volume;
        wilco = true;
    }
    
    public Job clone(){
        Job newJob = new Job(budget, costExpext, mipsExpext, volume);
        newJob.id = id;
        newJob.wilco = wilco;
        
        return newJob;
    }
    
    /**
     * @return the budget
     */
    public Double getBudget() {
        return budget;
    }

    
    /**
     * @return the volume
     */
    public Double getVolume() {
        return volume;
    }

    /**
     * @return the mipsExpext
     */
    public Double getMIPSExpect() {
        return mipsExpext;
    }

    /**
     * @return the costExpext
     */
    public Double getCostExpect() {
        return costExpext;
    }

    /**
     * @return the wilco
     */
    public boolean isWilco() {
        return wilco;
    }

    /**
     * @param wilco the wilco to set
     */
    public void setWilco(boolean wilco) {
        this.wilco = wilco;
    }
}

package magica.projects.flowtree.core.generator;

import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.JobFlow;
import magica.projects.math.MathUtils;

/**
 *
 * @author emelyanov
 */
public class JobFlowGenerator {

    protected Double volumeMin = 10d;
    protected Double volumeMax = 80d;
    
    protected Double costMin = 0.1d;
    protected Double costMax = 0.7d;
    
    protected Double mipsMin = 0.01d;
    protected Double mipsMax = 1d;
    
    protected Double budgetMutationFactor = 0.6;
    
    
    
    
    public JobFlow generate(int jobsMin, int jobsMax) {
        JobFlow flow = new JobFlow();

        int jobsNumber = MathUtils.getUniform(jobsMin, jobsMax);
        sleep(10);
        
        for (int i = 0; i < jobsNumber; i++) {
            flow.jobs.add(generateSingleJob());
        }

        return flow;
    }

    protected Job generateSingleJob() {
        
        Double cost = MathUtils.getUniform(costMin, costMax);
        sleep(10);
        Double mips = MathUtils.getUniform(mipsMin, mipsMax);
        sleep(10);
        Double volume = MathUtils.getUniform(volumeMin, volumeMax);
        
        Double baseExpectation = cost;
        if(mips >= cost){
            baseExpectation = mips;
        }
        
        Double budget = MathUtils.getGaussian(-1, 1, 0)*budgetMutationFactor*baseExpectation + (1-baseExpectation);
        if(budget > 0.9d){
            budget = 0.9d;
        }
        
        if(budget < 0.1d){
            budget = 0.1d;
        }
        
        return new Job(budget, cost, mips, volume);
    }
    
    protected void sleep(long t){
        try {
            Thread.sleep(t);
        }catch( Exception e){
            //do none
        }
    }
}

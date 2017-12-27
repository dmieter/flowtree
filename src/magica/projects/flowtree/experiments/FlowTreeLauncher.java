package magica.projects.flowtree.experiments;

/**
 *
 * @author dmieter
 */
public class FlowTreeLauncher {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        DistributionCompareExperiment exp = new DistributionCompareExperiment();
        //DoubleFlowModesExperiment exp = new DoubleFlowModesExperiment();
        exp.runSingleExperiment();
    }
    
}

package magica.projects.flowtree.core;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author emelyanov
 */
public class FlowTreeOperations {

    public static String strValue(Double value) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(value);
    }

    public static String strValueWhole(Double value) {
        DecimalFormat df = new DecimalFormat("#");
        return df.format(value);
    }

    public static void sortJobFlowByBudget(JobFlow flow) {
        
        Collections.sort(flow.jobs, new Comparator<Job>() {
            @Override
            public int compare(Job j1, Job j2) {
                return -j1.getBudget().compareTo(j2.getBudget());
            }

        });
    }

}

package magica.projects.flowtree.analytics;

import java.util.List;
import magica.projects.flowtree.core.Job;
import magica.projects.flowtree.core.JobFlow;
import magica.projects.flowtree.core.MetaDomain;

/**
 *
 * @author emelyanov
 */
public class DomainsCumulativeAnalytics {

    protected Double averageUserSatisfaction = 0d;  // the higher - the better
    protected Double averageCostDeviation = 0d;  // the smaller - the better
    protected Double averageMIPSDeviation = 0d;  // the smaller - the better
    protected Double averageTotalDeviation = 0d;  // the smaller - the better
    protected Integer jobsNumber = 0;
    protected Integer jobsReturnedNumber = 0;
    protected Integer costJobsNumber = 0;
    protected Integer mipsJobsNumber = 0;
    protected Integer domainsNumber = 0;
    protected Double averageCost = 0d;
    protected Double averageMIPS = 0d;

    public void clear() {
        averageUserSatisfaction = 0d;
        averageCostDeviation = 0d;
        averageMIPSDeviation = 0d;
        averageTotalDeviation = 0d;
        averageCost = 0d;
        averageMIPS = 0d;
        jobsNumber = 0;
        jobsReturnedNumber = 0;
        costJobsNumber = 0;
        mipsJobsNumber = 0;
        domainsNumber = 0;
    }

    public void analyze(MetaDomain domain, JobFlow flow) {
        domainsNumber++;

        averageCost = (averageCost * (domainsNumber - 1) + domain.getCostRating()) / domainsNumber;
        averageMIPS = (averageMIPS * (domainsNumber - 1) + domain.getMIPSRating()) / domainsNumber;

        for (Job job : flow.jobs) {
            jobsNumber++;
            Double userSatisfaction = job.getCostExpect() * domain.getCostRating()
                    + job.getMIPSExpect() * domain.getMIPSRating();
            averageUserSatisfaction = (averageUserSatisfaction * (jobsNumber - 1) + userSatisfaction) / jobsNumber;

            Double costDeviation = 0d;
            if (job.getCostExpect() > 0
                    && job.getCostExpect() > domain.getCostRating() // in case we have cost worse then expected
                    ) {
                costDeviation = Math.abs(job.getCostExpect() - domain.getCostRating()) / job.getCostExpect();
            }
            Double mipsDeviation = 0d;
            if (job.getMIPSExpect() > 0
                    && job.getMIPSExpect() > domain.getMIPSRating() // in case we have mips worse then expected
                    ) {
                mipsDeviation = Math.abs(job.getMIPSExpect() - domain.getMIPSRating()) / job.getMIPSExpect();
            }

            if (job.getCostExpect() > job.getMIPSExpect()) {
                /* calculating cost deviation for jobs with cost priority */
                costJobsNumber++;
                averageCostDeviation = (averageCostDeviation * (costJobsNumber - 1) + costDeviation) / costJobsNumber;

            } else {
                /* calculating mips deviation for jobs with mips priority */
                mipsJobsNumber++;
                averageMIPSDeviation = (averageMIPSDeviation * (mipsJobsNumber - 1) + mipsDeviation) / mipsJobsNumber;
            }
            
            averageTotalDeviation = (averageTotalDeviation * (jobsNumber - 1) + mipsDeviation+costDeviation) / jobsNumber;

        }
    }

    public String getData() {

        StringBuilder sb = new StringBuilder("Global Analytics \n");
        sb.append("User Satisfaction: ").append(averageUserSatisfaction).append("\n");
        sb.append("Cost Deviation: ").append(averageCostDeviation).append("\n");
        sb.append("MIPS Deviation: ").append(averageMIPSDeviation).append("\n");
        sb.append("Total Deviation: ").append(averageTotalDeviation).append("\n");
        sb.append("Number of jobs processed: ").append(jobsNumber).append("\n");
        sb.append("Number of jobs returned: ").append(jobsReturnedNumber).append("\n");
        sb.append("Number of domains processed: ").append(domainsNumber).append("\n");
        sb.append("Average Cost: ").append(averageCost).append("\n");
        sb.append("Average MIPS: ").append(averageMIPS).append("\n");

        return sb.toString();
    }

    void reportJobsReturned(List<Job> jobs) {
        jobsReturnedNumber += jobs.size();
    }
}

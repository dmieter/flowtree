package magica.projects.flowtree.experiments;

import magica.projects.flowtree.analytics.GlobalAnalytics;
import magica.projects.flowtree.core.JobFlow;
import magica.projects.flowtree.core.MetaDomain;
import magica.projects.flowtree.core.generator.DomainsGenerator;
import magica.projects.flowtree.core.generator.JobFlowGenerator;
import magica.projects.flowtree.graph.FlowGraph;
import magica.projects.flowtree.scheduler.DoubleFlowMetaScheduler;
import magica.projects.flowtree.scheduler.HierarchicalGreedyScheduler;
import magica.projects.flowtree.scheduler.HierarchicalRandomScheduler;
import magica.projects.flowtree.scheduler.PlainScheduler;
import magica.projects.flowtree.scheduler.job.JobAssignmentStrategies;

/**
 *
 * @author dmieter
 */
public class DistributionCompareExperiment {

    public void runSingleExperiment() {
        //MetaDomain rootDomain = generateDomainsStructure();

        FlowGraph.display();

//        FlowGraph.reset();
//        FlowGraph.buildFlowTreeGraph(rootDomain);
//        FlowGraph.updateFlowTreeGraphValues(rootDomain);

        for (int i = 0; i < 500; i++) {

            MetaDomain rootDomain = generateDomainsStructure();

            FlowGraph.reset();
            FlowGraph.buildFlowTreeGraph(rootDomain);
            FlowGraph.updateFlowTreeGraphValues(rootDomain);

            /* domains for Double Flow scheduler */    
            MetaDomain domainDF2 = rootDomain.clone();
            MetaDomain domainDF3 = rootDomain.clone();
            
            /* domains for Greedy scheduler */ 
            MetaDomain domainGreedy1 = rootDomain.clone();
            MetaDomain domainGreedy2 = rootDomain.clone();
            MetaDomain domainGreedy3 = rootDomain.clone();
            MetaDomain domainGreedy4 = rootDomain.clone();
            MetaDomain domainGreedy5 = rootDomain.clone();
            
            /* domains for Random scheduler */ 
            MetaDomain domainRand1 = rootDomain.clone();
            MetaDomain domainRand2 = rootDomain.clone();
            
            /* adding jobs */
            rootDomain.getAssignedJobFlow().jobs.addAll(generateJobFlow(80, 120).jobs);
            domainDF2.getAssignedJobFlow().jobs.addAll(rootDomain.getAssignedJobFlow().clone().jobs);
            domainDF3.getAssignedJobFlow().jobs.addAll(rootDomain.getAssignedJobFlow().clone().jobs);
            domainGreedy1.getAssignedJobFlow().jobs.addAll(rootDomain.getAssignedJobFlow().clone().jobs);
            domainGreedy2.getAssignedJobFlow().jobs.addAll(rootDomain.getAssignedJobFlow().clone().jobs);
            domainGreedy3.getAssignedJobFlow().jobs.addAll(rootDomain.getAssignedJobFlow().clone().jobs);
            domainGreedy4.getAssignedJobFlow().jobs.addAll(rootDomain.getAssignedJobFlow().clone().jobs);
            domainGreedy5.getAssignedJobFlow().jobs.addAll(rootDomain.getAssignedJobFlow().clone().jobs);
            domainRand1.getAssignedJobFlow().jobs.addAll(rootDomain.getAssignedJobFlow().clone().jobs);
            domainRand2.getAssignedJobFlow().jobs.addAll(rootDomain.getAssignedJobFlow().clone().jobs);


            GlobalAnalytics.setMode("DoubleFlow: Save");
            DoubleFlowMetaScheduler mc1 = new DoubleFlowMetaScheduler(JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc1.distributeWholeFlow(rootDomain);
            rootDomain.executeJobFlow(true);
            
            GlobalAnalytics.setMode("DoubleFlow: Criterion");
            DoubleFlowMetaScheduler mc2 = new DoubleFlowMetaScheduler(JobAssignmentStrategies.CRITERION_S, JobAssignmentStrategies.CRITERION_S);
            mc2.distributeWholeFlow(domainDF2);
            domainDF2.executeJobFlow(true);
            
            GlobalAnalytics.setMode("DoubleFlow: Plain Save");
            PlainScheduler mc3 = new PlainScheduler();
            mc3.setFlowScheduler(new DoubleFlowMetaScheduler(JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S));
            mc3.distributeWholeFlow(domainDF3);
            domainDF3.executeJobFlow(true);

            GlobalAnalytics.setMode("Greedy: Save");
            HierarchicalGreedyScheduler hgs1 = new HierarchicalGreedyScheduler(JobAssignmentStrategies.CRITERION_S);
            hgs1.distributeWholeFlow(domainGreedy1);
            domainGreedy1.executeJobFlow(true);
            
            GlobalAnalytics.setMode("Greedy: Dismiss");
            HierarchicalGreedyScheduler hgs2 = new HierarchicalGreedyScheduler(JobAssignmentStrategies.CRITERION_D);
            hgs2.distributeWholeFlow(domainGreedy2);
            domainGreedy2.executeJobFlow(true);
            
            GlobalAnalytics.setMode("Greedy: Save Cost");
            HierarchicalGreedyScheduler hgs3 = new HierarchicalGreedyScheduler(JobAssignmentStrategies.COST_S);
            hgs3.distributeWholeFlow(domainGreedy3);
            domainGreedy3.executeJobFlow(true);
            
            GlobalAnalytics.setMode("Greedy: Save MIPS");
            HierarchicalGreedyScheduler hgs4 = new HierarchicalGreedyScheduler(JobAssignmentStrategies.MIPS_S);
            hgs4.distributeWholeFlow(domainGreedy4);
            domainGreedy4.executeJobFlow(true);
            
            GlobalAnalytics.setMode("Greedy: Plain Save");
            PlainScheduler hgs5 = new PlainScheduler();
            hgs5.setFlowScheduler(new HierarchicalGreedyScheduler(JobAssignmentStrategies.CRITERION_S));
            hgs5.distributeWholeFlow(domainGreedy5);
            domainGreedy5.executeJobFlow(true);

            GlobalAnalytics.setMode("Random: Save");
            HierarchicalRandomScheduler hrs1 = new HierarchicalRandomScheduler(JobAssignmentStrategies.FIRST_FIT_S);
            hrs1.distributeWholeFlow(domainRand1);
            domainRand1.executeJobFlow(true);
            
            GlobalAnalytics.setMode("Random: Dismiss");
            HierarchicalRandomScheduler hrs2 = new HierarchicalRandomScheduler(JobAssignmentStrategies.FIRST_FIT_D);
            hrs2.distributeWholeFlow(domainRand2);
            domainRand2.executeJobFlow(true);

            GlobalAnalytics.setMode("DoubleFlow: Save");

        }

        //rootDomain.printAnalytics(true, 0);
        System.out.println("\n========== DOUBLE FLOW SAVE ===========\n");
        GlobalAnalytics.setMode("DoubleFlow: Save");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== DOUBLE FLOW Criterion ===========\n");
        GlobalAnalytics.setMode("DoubleFlow: Criterion");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== DOUBLE FLOW PLAIN SAVE ===========\n");
        GlobalAnalytics.setMode("DoubleFlow: Plain Save");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== GREEDY SAVE ===========\n");
        GlobalAnalytics.setMode("Greedy: Save");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== GREEDY DISMISS ===========\n");
        GlobalAnalytics.setMode("Greedy: Dismiss");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== GREEDY SAVE COST ===========\n");
        GlobalAnalytics.setMode("Greedy: Save Cost");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== GREEDY SAVE MIPS ===========\n");
        GlobalAnalytics.setMode("Greedy: Save MIPS");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== GREEDY PLAIN SAVE ===========\n");
        GlobalAnalytics.setMode("Greedy: Plain Save");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== RANDOM SAVE ===========\n");
        GlobalAnalytics.setMode("Random: Save");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== RANDOM DISMISS ===========\n");
        GlobalAnalytics.setMode("Random: Dismiss");
        System.out.println(GlobalAnalytics.getData());

    }

    protected MetaDomain generateDomainsStructure() {
        MetaDomain rootDomain = new MetaDomain(0);
        
        DomainsGenerator dg = new DomainsGenerator();
        rootDomain = dg.generateDomainsStructure();

        rootDomain.updateRatingsWeighted(true);

        return rootDomain;
    }

    protected JobFlow generateJobFlow(int minJobs, int maxJobs) {
        JobFlowGenerator jg = new JobFlowGenerator();
        return jg.generate(minJobs, maxJobs);
    }
}

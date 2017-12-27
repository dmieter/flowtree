package magica.projects.flowtree.experiments;

import magica.projects.flowtree.analytics.GlobalAnalytics;
import magica.projects.flowtree.core.JobFlow;
import magica.projects.flowtree.core.MetaDomain;
import magica.projects.flowtree.core.generator.DomainsGenerator;
import magica.projects.flowtree.core.generator.JobFlowGenerator;
import magica.projects.flowtree.graph.FlowGraph;
import magica.projects.flowtree.scheduler.DoubleFlowMetaScheduler;
import magica.projects.flowtree.scheduler.DoubleFlowSettings;
import magica.projects.flowtree.scheduler.HierarchicalGreedyScheduler;
import magica.projects.flowtree.scheduler.HierarchicalRandomScheduler;
import magica.projects.flowtree.scheduler.PlainScheduler;
import magica.projects.flowtree.scheduler.job.JobAssignmentStrategies;

/**
 *
 * @author dmieter
 */
public class DoubleFlowModesExperiment {

    public void runSingleExperiment() {
        //runSingleWeightsExperiment();
        runSingleBudgetLevelExperiment();
    }

    protected void runSingleWeightsExperiment() {
        //MetaDomain rootDomain0 = generateDomainsStructure();

        FlowGraph.display();

//        FlowGraph.reset();
//        FlowGraph.buildFlowTreeGraph(rootDomain);
//        FlowGraph.updateFlowTreeGraphValues(rootDomain);
        for (int i = 0; i < 500; i++) {

            System.out.println(i);

            MetaDomain rootDomain0 = generateDomainsStructure();
            FlowGraph.reset();
            FlowGraph.buildFlowTreeGraph(rootDomain0);
            FlowGraph.updateFlowTreeGraphValues(rootDomain0);

            /* domains for Double Flow scheduler */
            MetaDomain rootDomain1 = rootDomain0.clone();
            MetaDomain rootDomain2 = rootDomain0.clone();

            /* adding jobs */
            rootDomain0.getAssignedJobFlow().jobs.addAll(generateJobFlow(80, 120).jobs);
            rootDomain1.getAssignedJobFlow().jobs.addAll(rootDomain0.getAssignedJobFlow().clone().jobs);
            rootDomain2.getAssignedJobFlow().jobs.addAll(rootDomain0.getAssignedJobFlow().clone().jobs);

            GlobalAnalytics.setMode("COST = MIPS");
            DoubleFlowSettings settings0 = new DoubleFlowSettings(1d, 1d, 1d);
            DoubleFlowMetaScheduler mc0 = new DoubleFlowMetaScheduler(settings0, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc0.distributeWholeFlow(rootDomain0);
            rootDomain0.executeJobFlow(true);

            GlobalAnalytics.setMode("COST");
            DoubleFlowSettings settings1 = new DoubleFlowSettings(1d, 0d, 1d);
            DoubleFlowMetaScheduler mc1 = new DoubleFlowMetaScheduler(settings1, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc1.distributeWholeFlow(rootDomain1);
            rootDomain1.executeJobFlow(true);

            GlobalAnalytics.setMode("MIPS");
            DoubleFlowSettings settings2 = new DoubleFlowSettings(0d, 1d, 1d);
            DoubleFlowMetaScheduler mc2 = new DoubleFlowMetaScheduler(settings2, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc2.distributeWholeFlow(rootDomain2);
            rootDomain2.executeJobFlow(true);

            GlobalAnalytics.setMode("COST = MIPS");
            System.out.println(GlobalAnalytics.getReturnedJobsNumber() + "returned");

        }

        //rootDomain0.printAnalytics(true, 0);
        System.out.println("\n========== COST ===========\n");
        GlobalAnalytics.setMode("COST");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== COST = MIPS ===========\n");
        GlobalAnalytics.setMode("COST = MIPS");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== MIPS ===========\n");
        GlobalAnalytics.setMode("MIPS");
        System.out.println(GlobalAnalytics.getData());

    }

    protected void runSingleBudgetLevelExperiment() {
        //MetaDomain rootDomain0 = generateDomainsStructure();

        FlowGraph.display();

//        FlowGraph.reset();
//        FlowGraph.buildFlowTreeGraph(rootDomain);
//        FlowGraph.updateFlowTreeGraphValues(rootDomain);
        for (int i = 0; i < 500; i++) {
            
            System.out.println(i);

            MetaDomain rootDomain0 = generateDomainsStructure();
            FlowGraph.reset();
            FlowGraph.buildFlowTreeGraph(rootDomain0);
            FlowGraph.updateFlowTreeGraphValues(rootDomain0);

            /* domains for Double Flow scheduler */
            MetaDomain rootDomain1 = rootDomain0.clone();
            MetaDomain rootDomain2 = rootDomain0.clone();
            MetaDomain rootDomain3 = rootDomain0.clone();
            MetaDomain rootDomain4 = rootDomain0.clone();
            MetaDomain rootDomain5 = rootDomain0.clone();
            MetaDomain rootDomain6 = rootDomain0.clone();
            MetaDomain rootDomain7 = rootDomain0.clone();

            /* adding jobs */
            rootDomain0.getAssignedJobFlow().jobs.addAll(generateJobFlow(80, 120).jobs);
            rootDomain1.getAssignedJobFlow().jobs.addAll(rootDomain0.getAssignedJobFlow().clone().jobs);
            rootDomain2.getAssignedJobFlow().jobs.addAll(rootDomain0.getAssignedJobFlow().clone().jobs);
            rootDomain3.getAssignedJobFlow().jobs.addAll(rootDomain0.getAssignedJobFlow().clone().jobs);
            rootDomain4.getAssignedJobFlow().jobs.addAll(rootDomain0.getAssignedJobFlow().clone().jobs);
            rootDomain5.getAssignedJobFlow().jobs.addAll(rootDomain0.getAssignedJobFlow().clone().jobs);
            rootDomain6.getAssignedJobFlow().jobs.addAll(rootDomain0.getAssignedJobFlow().clone().jobs);
            rootDomain7.getAssignedJobFlow().jobs.addAll(rootDomain0.getAssignedJobFlow().clone().jobs);

            GlobalAnalytics.setMode("0");
            DoubleFlowSettings settings0 = new DoubleFlowSettings(1d, 1d, 0.1d);
            DoubleFlowMetaScheduler mc0 = new DoubleFlowMetaScheduler(settings0, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc0.distributeWholeFlow(rootDomain0);
            rootDomain0.executeJobFlow(true);

            GlobalAnalytics.setMode("0.4");
            DoubleFlowSettings settings1 = new DoubleFlowSettings(1d, 1d, 0.4d);
            DoubleFlowMetaScheduler mc1 = new DoubleFlowMetaScheduler(settings1, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc1.distributeWholeFlow(rootDomain1);
            rootDomain1.executeJobFlow(true);

            GlobalAnalytics.setMode("0.6");
            DoubleFlowSettings settings2 = new DoubleFlowSettings(1d, 1d, 0.6d);
            DoubleFlowMetaScheduler mc2 = new DoubleFlowMetaScheduler(settings2, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc2.distributeWholeFlow(rootDomain2);
            rootDomain2.executeJobFlow(true);

            GlobalAnalytics.setMode("0.8");
            DoubleFlowSettings settings3 = new DoubleFlowSettings(1d, 3d, 0.8d);
            DoubleFlowMetaScheduler mc3 = new DoubleFlowMetaScheduler(settings3, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc3.distributeWholeFlow(rootDomain3);
            rootDomain3.executeJobFlow(true);

            GlobalAnalytics.setMode("0.9");
            DoubleFlowSettings settings4 = new DoubleFlowSettings(1d, 1d, 0.9d);
            DoubleFlowMetaScheduler mc4 = new DoubleFlowMetaScheduler(settings4, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc4.distributeWholeFlow(rootDomain4);
            rootDomain4.executeJobFlow(true);

            GlobalAnalytics.setMode("1");
            DoubleFlowSettings settings5 = new DoubleFlowSettings(1d, 1d, 1d);
            DoubleFlowMetaScheduler mc5 = new DoubleFlowMetaScheduler(settings5, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc5.distributeWholeFlow(rootDomain5);
            rootDomain5.executeJobFlow(true);

            GlobalAnalytics.setMode("1.1");
            DoubleFlowSettings settings6 = new DoubleFlowSettings(1d, 1d, 1.1d);
            DoubleFlowMetaScheduler mc6 = new DoubleFlowMetaScheduler(settings6, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc6.distributeWholeFlow(rootDomain6);
            rootDomain6.executeJobFlow(true);

            GlobalAnalytics.setMode("1.5");
            DoubleFlowSettings settings7 = new DoubleFlowSettings(1d, 1d, 1.5d);
            DoubleFlowMetaScheduler mc7 = new DoubleFlowMetaScheduler(settings7, JobAssignmentStrategies.COST_S, JobAssignmentStrategies.MIPS_S);
            mc7.distributeWholeFlow(rootDomain7);
            rootDomain7.executeJobFlow(true);

            GlobalAnalytics.setMode("0");

        }

        //rootDomain0.printAnalytics(true, 0);
        System.out.println("\n========== 0 ===========\n");
        GlobalAnalytics.setMode("0");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== 0.4 ===========\n");
        GlobalAnalytics.setMode("0.4");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== 0.6 ===========\n");
        GlobalAnalytics.setMode("0.6");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== 0.8 ===========\n");
        GlobalAnalytics.setMode("0.8");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== 0.9 ===========\n");
        GlobalAnalytics.setMode("0.9");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== 1 ===========\n");
        GlobalAnalytics.setMode("1");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== 1.1 ===========\n");
        GlobalAnalytics.setMode("1.1");
        System.out.println(GlobalAnalytics.getData());
        System.out.println("\n========== 1.5 ===========\n");
        GlobalAnalytics.setMode("1.5");
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

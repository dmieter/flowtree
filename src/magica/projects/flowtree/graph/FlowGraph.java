package magica.projects.flowtree.graph;

import java.util.Map;
import magica.projects.flowtree.core.MetaDomain;
import static magica.projects.flowtree.core.FlowTreeOperations.*;
import magica.projects.flowtree.core.JobFlow;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Element;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

/**
 *
 * @author emelyanov
 */
public class FlowGraph {

    public enum FlowMode {
        ALL, COST, MIPS
    };

    protected static Graph graph = new SingleGraph("Flow Graph");
    public static FlowMode flowMode = FlowMode.ALL;

    public static Graph getGraph() {
        return graph;
    }

    public static void display() {
        applyStyles();
        graph.display();
    }

    public static void reset() {
        graph.clear();
        applyStyles();
    }

    public static Node buildFlowTreeGraph(MetaDomain domain) {
        Node node = createNode(domain);

        for (MetaDomain subDomain : domain.getNextLevelDomains()) {
            Node subNode = buildFlowTreeGraph(subDomain);
            String edgeID = getEdgeID(domain, subDomain);
            graph.addEdge(edgeID, node, subNode);
        }

        return node;
    }

    public static void updateFlowTreeGraphValues(MetaDomain domain) {
        updateNode(domain);
        for (MetaDomain subDomain : domain.getNextLevelDomains()) {
            updateFlowTreeGraphValues(subDomain);

            JobFlow flow = null;
            switch (flowMode) {
                case ALL:
                    flow = domain.getAssignedJobFlow();
                    break;
                case COST:
                    flow = domain.sdata.costFlow;
                    break;
                case MIPS:
                    flow = domain.sdata.mipsFlow;
                    break;
                default:
                    flow = domain.getAssignedJobFlow();
                    break;
            };

            Double capacity = subDomain.getVolumeCapacity();
            Double plannedFlow = 0d;
            Double actualFlow = 0d;
            Double actualLoad = 0d;

            if (flow != null && flow.distributionMapPlan != null) {
                plannedFlow = flow.distributionMapPlan.get(subDomain);
                actualFlow = plannedFlow - flow.distributionMap.get(subDomain);
                if (capacity > 0) {
                    actualLoad = actualFlow / capacity;
                }
            }

            updateEdge(domain, subDomain, strValueWhole(plannedFlow), strValueWhole(actualFlow), strValueWhole(capacity), actualLoad);
        }
    }

    protected static Node createNode(MetaDomain domain) {
        String nodeID = getNodeID(domain);
        graph.addNode(nodeID);
        return updateNode(domain);
    }

    protected static Node updateNode(MetaDomain domain) {
        String label = domain.getId() + ": " + strValue(domain.getCostRating()) + "C " + strValue(domain.getMIPSRating()) + "M  " + strValueWhole(domain.getVolumeCapacity());
        String nodeID = getNodeID(domain);
        Node node = graph.getNode(nodeID);
        if (node != null) {
            setLabel(node, label);
        } else {
            throw new IllegalStateException("We've got node " + domain.getId() + " which doesn't exist in graph!");
        }

        return node;
    }

    protected static void updateEdge(MetaDomain fromDomain, MetaDomain toDomain, String planFlow, String actualFlow, String maxFlow, Double actualLoad) {
        Edge e = graph.getEdge(getEdgeID(fromDomain, toDomain));
        setLabel(e, planFlow + "P / " + actualFlow + "A / " + maxFlow);
        e.setAttribute("ui.color", actualLoad);
    }

    protected static void setLabel(Element element, String label) {
        element.setAttribute("ui.label", label);
    }

    protected static String getNodeID(MetaDomain domain) {
        return String.valueOf(domain.getId());
    }

    protected static String getEdgeID(MetaDomain fromDomain, MetaDomain toDomain) {
        return fromDomain.getId() + "x" + toDomain.getId();
    }

    protected static void applyStyles() {
        String css
                = "node {\n"
                + "	size: 30px;\n"
                + "	fill-color: #111;\n"
                + "	z-index: 0;\n"
                + "}\n"
                + "edge {\n"
                + "	fill-mode: dyn-plain;\n"
                + "	fill-color: blue, green, yellow, red;\n"
                + "	size: 2px;\n"
                + "}";

        graph.addAttribute("ui.quality");
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.stylesheet", css);
    }
}

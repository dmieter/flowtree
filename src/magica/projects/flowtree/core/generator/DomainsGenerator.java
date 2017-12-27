package magica.projects.flowtree.core.generator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import magica.projects.flowtree.core.MetaDomain;
import magica.projects.math.MathUtils;

/**
 *
 * @author dmieter
 */
public class DomainsGenerator {

    protected int leafsNumber = 20;
    protected int connectionsMin = 3;
    protected int connectionsMax = 5;

    protected Double volumeMin = 100d;
    protected Double volumeMax = 800d;

    protected Double costMin = 0.2d;
    protected Double costMax = 0.95d;

    protected Double mipsMin = 0.2d;
    protected Double mipsMax = 1d;

    protected MetaDomain generateLeafDomain(int id) {
        Double volume = MathUtils.getUniform(volumeMin, volumeMax);
        Double cost = MathUtils.getUniform(costMax, costMin);
        Double mips = MathUtils.getUniform(mipsMin, mipsMax);

        MetaDomain domain = new MetaDomain(id, cost, mips, volume);

        return domain;
    }

    protected MetaDomain generateStructuredDomain(int id, List<MetaDomain> nextLevelDomains) {
        MetaDomain domain = new MetaDomain(id);
        domain.getNextLevelDomains().addAll(nextLevelDomains);

        return domain;
    }

    public MetaDomain generateDomainsStructure() {

        List<MetaDomain> lowDomains = new LinkedList<>();
        List<MetaDomain> highDomains = new LinkedList<>();

        int id = 0;

        for (int i = 0; i < leafsNumber; i++) {
            lowDomains.add(generateLeafDomain(id++));
        }

        while (lowDomains.size() != 1 || !highDomains.isEmpty()) {

            /* checking if we are done connecting low level domains */
            if (lowDomains.isEmpty()) {
                lowDomains.addAll(highDomains); // all higher tier domains are now low
                highDomains.clear();
                continue;
            }

            /* determining number of sub domains for next parent domains */
            int connectionsNum = MathUtils.getUniform(connectionsMin, connectionsMax);
            if (connectionsNum > lowDomains.size()) {
                connectionsNum = lowDomains.size();
            }

            /* creating new parent domain */
            MetaDomain parentDomain = new MetaDomain(id++);
            highDomains.add(parentDomain);

            /* getting next connectionsNum subdomains */
            int c = 0;
            for (Iterator<MetaDomain> it = lowDomains.iterator(); it.hasNext();) {

                parentDomain.addSubDomain(it.next());
                it.remove();

                if (++c == connectionsNum) {
                    break;
                }
            }
        }

        return lowDomains.get(0);

    }
}

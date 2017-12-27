package magica.projects.bookshelf.dispatcher.lp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import magica.projects.bookshelf.core.Book;
import magica.projects.bookshelf.core.Shelf;
import magica.projects.bookshelf.core.Wall;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

/**
 *
 * @author emelyanov
 */
public class LPSolver {

    public Map<Shelf, Double> calculateOptimalLoad(Wall wall, List<Book> books, Double budgetLimit) {
        
        /* full volume of books to dispatch */
        double fullVolume = 0;
        for (Book book : books) {
            fullVolume += book.getVolume();
        }
        
        /* each variable - how much volume to send to each shelf */
        int variableNum = wall.getShelves().size();

        /* all constraints for LP problem */
        Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();

        double[] objectiveCoefs = new double[variableNum];
        double[] costLimitCoefs = new double[variableNum];

        int i = 0;
        for (Shelf shelf : wall.getShelves()) {
            /* height is objective criterion for maximization */
            objectiveCoefs[i] = shelf.getHeight();
            /* price is a general limiting coefficient */
            costLimitCoefs[i] = shelf.getPrice();

            /* each shelf (variable) has it's own volume limit */
            double[] volumeLimitCoefs = new double[variableNum];
            for (int j = 0; j < variableNum; j++) {
                if(j==i){
                    volumeLimitCoefs[j] = 1;
                }else{
                    volumeLimitCoefs[j] = 0;
                }
            }
            /* constraint on i-th shelf volume */
            constraints.add(new LinearConstraint(volumeLimitCoefs, Relationship.LEQ, shelf.getFreeVolume()));
            /* constraint on i-th shelf result positivity */
            constraints.add(new LinearConstraint(volumeLimitCoefs, Relationship.GEQ, 0));
            /* next shelf */
            i++;
        }

        /* constraint on total cost */
        constraints.add(new LinearConstraint(costLimitCoefs, Relationship.LEQ, budgetLimit));
        
        /* specifying total volume to dispatch */
        double[] totalVolumeCoefs = new double[variableNum];
        for (int j = 0; j < variableNum; j++) {
            totalVolumeCoefs[j] = 1;
        }
        constraints.add(new LinearConstraint(totalVolumeCoefs, Relationship.EQ, fullVolume));
        
        /* Objective function -> maximize weighted height */
        LinearObjectiveFunction f = new LinearObjectiveFunction(objectiveCoefs, 0 /* constant term */);

        /* solution API */
        PointValuePair solution = new SimplexSolver().optimize(f, new LinearConstraintSet(constraints), GoalType.MAXIMIZE);

        /* prepare result */
        Map<Shelf, Double> resultMap = new HashMap<>();
        for(i = 0; i<solution.getPoint().length; i++){
            resultMap.put(wall.getShelves().get(i), solution.getPoint()[i]);
        }
        return resultMap;
    }
}

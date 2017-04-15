/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator;

import vacuumCleaner.simulator.objects.Dirt;

/**
 *
 * @author santi
 */
public class PerformanceMeasure {
    public static double goalAchievement(State initial, State end) {
        double initial_dirt = initial.getObjects(Dirt.class).size();
        double end_dirt = end.getObjects(Dirt.class).size();

        if (initial_dirt>0) return (1.0 - (end_dirt / initial_dirt));
        
        return 1.0;
    }
}

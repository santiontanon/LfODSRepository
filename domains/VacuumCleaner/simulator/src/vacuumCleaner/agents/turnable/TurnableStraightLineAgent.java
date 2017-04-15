/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.agents.turnable;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.perception.Perception;
import java.util.Random;
import vacuumCleaner.agents.Agent;
import vacuumCleaner.simulator.perception.TurnableFourRayDistancePerception;

/**
 *
 * @author santi
 */

public class TurnableStraightLineAgent extends Agent {
    Random r = new Random();
    int turnState = 0;  // last direction of turn (1 : left, 2: right)
    
    public Action cycle(int id,Perception p, double timeStep) {
        boolean canGoForward = true;
        boolean canGoLeft = true;
        boolean canGoRight = true;

        if (p instanceof TurnableFourRayDistancePerception) {
            if (p.getInteger("du")==0 && p.getInteger("u")==0) canGoForward = false;
            if (p.getInteger("dl")==0 && p.getInteger("l")==0) canGoLeft = false;
            if (p.getInteger("dr")==0 && p.getInteger("r")==0) canGoRight = false;
        }

        if (canGoForward) {
            return new Action("forward",id);
        } else {
            if (canGoLeft) {
                turnState = 1;
                return new Action("left",id);
            } else if (canGoRight) {
                turnState = 2;
                return new Action("right",id);            
            } else {
                if (turnState==1) {
                    return new Action("left",id);
                } else {
                    return new Action("right",id);
                }
            }
        }

    }
}

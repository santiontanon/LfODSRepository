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

// LEVEL 3 agent with bounded internal state (IOHMM)
public class TurnableSmartStraightLineAgent extends Agent {
    Random r = new Random();
    int turnState = 0;  // last direction of turn (1 : left, 2: right)
    int dirtState = 0;  // 0 : standard, 1: dirt seen on the left, 2: dirt seen on the right
    
    public Action cycle(int id,Perception p, double timeStep) {
        boolean canGoForward = true;
        boolean canGoLeft = true;
        boolean canGoRight = true;
        boolean dirtForward = false;
        boolean dirtLeft = false;
        boolean dirtRight = false;

        if (p instanceof TurnableFourRayDistancePerception) {
            if (p.getInteger("du")==0 && p.getInteger("u")==0) canGoForward = false;
            if (p.getInteger("dl")==0 && p.getInteger("l")==0) canGoLeft = false;
            if (p.getInteger("dr")==0 && p.getInteger("r")==0) canGoRight = false;
            if (p.getInteger("u")==1) dirtForward = true;
            if (p.getInteger("l")==1) dirtLeft = true;
            if (p.getInteger("r")==1) dirtRight = true;
        }
        
        switch(dirtState) {
            case 0:
                if (dirtForward) {
                    return new Action("forward",id);
                } else {
                    if (dirtLeft) {
                        dirtState = 1;
                        turnState = 1;
                        return new Action("left",id);
                    }
                    if (dirtRight) {
                        dirtState = 2;
                        turnState = 2;
                        return new Action("right",id);
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
            case 1:
                if (dirtForward) {
                    dirtState = 0;
                    return new Action("forward",id);
                }
                turnState = 1;
                return new Action("left",id);
            case 2:
                if (dirtForward) {
                    dirtState = 0;
                    return new Action("forward",id);
                }
                turnState = 2;
                return new Action("right",id);
        }
        return null;
    }
}

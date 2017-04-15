/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.agents.discrete;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.perception.Perception;
import vacuumCleaner.simulator.perception.WindowPerception;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import vacuumCleaner.agents.Agent;
import vacuumCleaner.simulator.perception.FourRayDistancePerception;

/**
 *
 * @author santi
 */

// LEVEL 3 agent with bounded internal state (IOHMM)
public class SmartWallFollowerAgent extends Agent {
    
    public Action cycle(int id,Perception p, double timeStep) {
//        Action l[]={new Action("up",id),new Action("down",id),new Action("left",id),new Action("right",id),null};
        // Are there walls around?
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;
        boolean upDirt = false;
        boolean downDirt = false;
        boolean leftDirt = false;
        boolean rightDirt = false;

        if (p instanceof FourRayDistancePerception) {
            if (p.getInteger("du")==0 && p.getInteger("u")==0) up = true;
            if (p.getInteger("dd")==0 && p.getInteger("d")==0) down = true;
            if (p.getInteger("dl")==0 && p.getInteger("l")==0) left = true;
            if (p.getInteger("dr")==0 && p.getInteger("r")==0) right = true;
            if (p.getInteger("u") == 1) upDirt = true;
            if (p.getInteger("d") == 1) downDirt = true;
            if (p.getInteger("l") == 1) leftDirt = true;
            if (p.getInteger("r") == 1) rightDirt = true;
        }
        
        if (upDirt) return new Action("up",id);
        if (rightDirt) return new Action("right",id);
        if (downDirt) return new Action("down",id);
        if (leftDirt) return new Action("left",id);
        
        if (!up && !right && !down && !left) return new Action("right",id);
        if (!up && !right && !down &&  left) return new Action("up",id);
        if (!up && !right &&  down && !left) return new Action("left",id);
        if (!up && !right &&  down &&  left) return new Action("up",id);
        if (!up &&  right && !down && !left) return new Action("down",id);
        if (!up &&  right && !down &&  left) return new Action("down",id);
        if (!up &&  right &&  down && !left) return new Action("left",id);
        if (!up &&  right &&  down &&  left) return new Action("up",id);
        if ( up && !right && !down && !left) return new Action("right",id);
        if ( up && !right && !down &&  left) return new Action("right",id);
        if ( up && !right &&  down && !left) return new Action("right",id);
        if ( up && !right &&  down &&  left) return new Action("left",id);
        if ( up &&  right && !down && !left) return new Action("down",id);
        if ( up &&  right && !down &&  left) return new Action("down",id);
        if ( up &&  right &&  down && !left) return new Action("left",id);
        if ( up &&  right &&  down &&  left) return null;
        return null;
    }
}
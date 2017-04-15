/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.agents.discrete;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.perception.Perception;
import vacuumCleaner.agents.Agent;
import vacuumCleaner.simulator.perception.FourRayDistancePerception;

/**
 *
 * @author santi
 */

public class WallFollowerAgent extends Agent {
    
    public Action cycle(int id,Perception p, double timeStep) {
//        Action l[]={new Action("up",id),new Action("down",id),new Action("left",id),new Action("right",id),null};
        // Are there walls around?
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;

        if (p instanceof FourRayDistancePerception) {
            if (p.getInteger("du")==0 && p.getInteger("u")==0) up = true;
            if (p.getInteger("dd")==0 && p.getInteger("d")==0) down = true;
            if (p.getInteger("dl")==0 && p.getInteger("l")==0) left = true;
            if (p.getInteger("dr")==0 && p.getInteger("r")==0) right = true;
        }
        
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

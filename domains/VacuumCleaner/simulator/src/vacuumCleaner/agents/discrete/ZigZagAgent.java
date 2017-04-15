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

/**
 *
 * @author santi
 */

// LEVEL 3 agent with bounded internal state (IOHMM)
public class ZigZagAgent extends Agent {
    Random r = new Random();
    int vertical_state = 0; // 0, going down; 1, going up
    int horizontal_state = 0; // 0, going left; 1, going down
    
    public Action cycle(int id,Perception p, double timeStep) {
//        Action l[]={new Action("up",id),new Action("down",id),new Action("left",id),new Action("right",id),null};
        // Are there walls around?
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;

        if (p instanceof WindowPerception) {
            if (p.getInteger("u") == 0) up = true;
            if (p.getInteger("d") == 0) down = true;
            if (p.getInteger("l") == 0) left = true;
            if (p.getInteger("r") == 0) right = true;
        } else {
            if (p.getInteger("du")==0 && p.getInteger("u")==0) up = true;
            if (p.getInteger("dd")==0 && p.getInteger("d")==0) down = true;
            if (p.getInteger("dl")==0 && p.getInteger("l")==0) left = true;
            if (p.getInteger("dr")==0 && p.getInteger("r")==0) right = true;
        }
        
        if (horizontal_state == 0 && vertical_state == 0) {
            if (left) {
                if (down) {
                    if (right) {
                        vertical_state = 1;
                        return new Action("up",id);
                    } else {
                        vertical_state = 1;
                        horizontal_state = 1;
                        return new Action("right",id);
                    }
                } else {
                    horizontal_state = 1;
                    return new Action("down",id);
                }
            } else {
                return new Action("left",id);
            }
        } else if (horizontal_state !=0 && vertical_state == 0) {
            if (right) {
                if (down) {
                    if (left) {
                        vertical_state = 0;
                        return new Action("up",id);
                    } else {
                        vertical_state = 1;
                        horizontal_state = 0;
                        return new Action("left",id);
                    }
                } else {
                    horizontal_state = 0;
                    return new Action("down",id);
                }
            } else {
                return new Action("right",id);
            }
        } else if (horizontal_state ==0 && vertical_state != 0) {
            if (left) {
                if (up) {
                    if (right) {
                        vertical_state = 0;
                        return new Action("down",id);
                    } else {
                        vertical_state = 0;
                        horizontal_state = 1;
                        return new Action("right",id);
                    }
                } else {
                    horizontal_state = 1;
                    return new Action("up",id);
                }
            } else {
                return new Action("left",id);
            }
        } else {
            if (right) {
                if (up) {
                    if (left) {
                        vertical_state = 1;
                        return new Action("down",id);
                    } else {
                        vertical_state = 0;
                        horizontal_state = 0;
                        return new Action("left",id);
                    }
                } else {
                    horizontal_state = 0;
                    return new Action("up",id);
                }
            } else {
                return new Action("right",id);
            }
        }

    }

    public List<Integer> internalStateToBayesNetVariables() {
        List<Integer> l = new LinkedList<Integer>();
        l.add(vertical_state+1);    // we add 1, since for Matlab, it's better if the lowest value is 1
        l.add(horizontal_state+1);
        return l;
    }

}

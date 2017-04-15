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
public class SmartStraightLineAgent extends Agent {
    Random r = new Random();
    int state = -1; // last direction
    
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

        if (p instanceof WindowPerception) {
            if (p.getInteger("u") == 0) up = true;
            if (p.getInteger("d") == 0) down = true;
            if (p.getInteger("l") == 0) left = true;
            if (p.getInteger("r") == 0) right = true;
            if (p.getInteger("u") == 1) upDirt = true;
            if (p.getInteger("d") == 1) downDirt = true;
            if (p.getInteger("l") == 1) leftDirt = true;
            if (p.getInteger("r") == 1) rightDirt = true;
        } else {
            if (p.getInteger("du")==0 && p.getInteger("u")==0) up = true;
            if (p.getInteger("dd")==0 && p.getInteger("d")==0) down = true;
            if (p.getInteger("dl")==0 && p.getInteger("l")==0) left = true;
            if (p.getInteger("dr")==0 && p.getInteger("r")==0) right = true;
            if (p.getInteger("u") == 1) upDirt = true;
            if (p.getInteger("d") == 1) downDirt = true;
            if (p.getInteger("l") == 1) leftDirt = true;
            if (p.getInteger("r") == 1) rightDirt = true;
        }
        if (upDirt) {
            state = 0;
            return new Action("up",id);
        }
        if (downDirt) {
            state = 1;
            return new Action("down",id);
        }
        if (leftDirt) {
            state = 2;
            return new Action("left",id);
        }
        if (rightDirt) {
            state = 3;
            return new Action("right",id);
        }

        if (state==0 && up) state = -1;
        if (state==1 && down) state = -1;
        if (state==2 && left) state = -1;
        if (state==3 && right) state = -1;

        switch(state) {
            case 0: return new Action("up",id);
            case 1: return new Action("down",id);
            case 2: return new Action("left",id);
            case 3: return new Action("right",id);
            default:
                {
                    List<Action> candidates = new LinkedList<Action>();
                    if (!up) candidates.add(new Action("up",id));
                    if (!down) candidates.add(new Action("down",id));
                    if (!left) candidates.add(new Action("left",id));
                    if (!right) candidates.add(new Action("right",id));
                    if (candidates.size()==0) return null;
                    Action a= candidates.get(r.nextInt(candidates.size()));
                    if (a.getName().equals("up")) state = 0;
                    if (a.getName().equals("down")) state = 1;
                    if (a.getName().equals("left")) state = 2;
                    if (a.getName().equals("right")) state = 3;
                    return a;
                }
        }
    }

    public List<Integer> internalStateToBayesNetVariables() {
        List<Integer> l = new LinkedList<Integer>();
        l.add(state+2); // we add 2, since for Matlab, it's better if the lowest value is 1
        return l;
    }
    
    public void setLastDirection(int s) {
        state = s;
    }

    
    public void setLastDirection(Action a) {
        if (a.getName().equals("up")) state = 0;
        if (a.getName().equals("down")) state = 1;
        if (a.getName().equals("left")) state = 2;
        if (a.getName().equals("right")) state = 3;
    }
    
}

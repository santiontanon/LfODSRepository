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
import util.Pair;

/**
 *
 * @author santi
 */

// LEVEL 2 agent
public class SmartRandomAgent extends Agent {
    
    public List<Pair<Action,Double>> cycleDistribution(int id, Perception p, double timeStep) {
        List<Pair<Action,Double>> l = new LinkedList<Pair<Action,Double>>();

        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;
        boolean upDirt = false;
        boolean downDirt = false;
        boolean leftDirt = false;
        boolean rightDirt = false;

//        System.out.println(p);

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
            l.add(new Pair<Action,Double>(new Action("up",id),1.0));
            return l;
        }
        if (downDirt) {
            l.add(new Pair<Action,Double>(new Action("down",id),1.0));
            return l;
        }
        if (leftDirt) {
            l.add(new Pair<Action,Double>(new Action("left",id),1.0));
            return l;
        }   
        if (rightDirt) {
            l.add(new Pair<Action,Double>(new Action("right",id),1.0));
            return l;
        }   

        if (!up) l.add(new Pair<Action,Double>(new Action("up",id),1.0));
        if (!down) l.add(new Pair<Action,Double>(new Action("down",id),1.0));
        if (!left) l.add(new Pair<Action,Double>(new Action("left",id),1.0));
        if (!right) l.add(new Pair<Action,Double>(new Action("right",id),1.0));
        if (l.size()==0) l.add(new Pair<Action,Double>(null,1.0));
        
        // normalize their probabilities:
        for(Pair<Action,Double> pair:l) {
            pair.m_b = 1.0/l.size();
        }
        
        return l;
    }
    

}

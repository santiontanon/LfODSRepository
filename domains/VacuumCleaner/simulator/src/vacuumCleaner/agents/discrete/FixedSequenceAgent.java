/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.agents.discrete;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.perception.Perception;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import vacuumCleaner.agents.Agent;

/**
 *
 * @author santi
 */

// LEVEL 1 agent
public class FixedSequenceAgent extends Agent {
    String sequence[] = {"up", "up", "right", "right", "down", "down", "left", "left",
                         "up", "up", "up", "right", "right", "right", "down", "down", "down", "left", "left", "left",
                        };
    int time_step = 0;

    public Action cycle(int id,Perception p, double timeStep) {
        Action a = new Action(sequence[time_step%sequence.length],id);
        time_step++;
        return a;
    }

    public List<Integer> internalStateToBayesNetVariables() {
        List<Integer> l = new LinkedList<Integer>();
        l.add((time_step%sequence.length)+1); // we add 1, since for Matlab, it's better if the lowest value is 1
        return l;
    }

}

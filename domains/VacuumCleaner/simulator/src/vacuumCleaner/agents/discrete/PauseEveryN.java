/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.agents.discrete;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.perception.Perception;
import java.util.List;
import java.util.Random;
import vacuumCleaner.agents.Agent;
import vacuumCleaner.agents.Agent;

/**
 *
 * @author santi
 */

// LEVEL 3 agent (whatever the underneath agent is)
public class PauseEveryN extends Agent {
    int N = 3;
    int cycle = 0;
    Agent a = null;

    public PauseEveryN(int a_N, Agent a_a) {
        N = a_N;
        a = a_a;
    }

    public Action cycle(int id,Perception p, double timeStep) throws Exception {
        cycle++;
        if ((cycle%N)!=0) return a.cycle(id,p, timeStep);
        return null;
    }

    public String name() {
        return "PauseEvery" + N + a.getClass().getSimpleName();
    }

    public List<Integer> internalStateToBayesNetVariables() {
        List<Integer> l = a.internalStateToBayesNetVariables();
        l.add((cycle%N)+1); // we add 1, since for Matlab, it's better if the lowest value is 1
        return l;
    }

}

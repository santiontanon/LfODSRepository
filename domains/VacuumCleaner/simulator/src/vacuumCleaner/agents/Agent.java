/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.agents;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.perception.Perception;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import util.Pair;

/**
 *
 * @author santi
 */
public abstract class Agent {
    public Random r = new Random();
    
    public Agent() {

    }

    public void start()
    {
    }

    public void end()
    {
    }

    
    public Action cycle(int id,Perception p, double timeStep) throws Exception {
        List<Pair<Action,Double>> l = cycleDistribution(id,p,timeStep);
        double random = r.nextDouble();
        double tmp = 0;
        for(Pair<Action,Double> pair:l) {
            tmp+=pair.m_b;
            if (random<=tmp) return pair.m_a;
        }
        return l.get(l.size()-1).m_a;
    }
    
    public List<Pair<Action,Double>> cycleDistribution(int id, Perception p, double timeStep) throws Exception {
        throw new Exception("Uninmplemented Method!");
    }
    

    public String name() {
        return getClass().getSimpleName();
    }

    // This method returns the internal state as a fixed length list of integers.
    // It is thought to be used to translate the internal state to a collection of variables to be used in
    // Dynamic Bayesian Models, like HMMs.
    public List<Integer> internalStateToBayesNetVariables() {
        List<Integer> l = new LinkedList<Integer>();
        l.add(1);
        return l;
    }
}

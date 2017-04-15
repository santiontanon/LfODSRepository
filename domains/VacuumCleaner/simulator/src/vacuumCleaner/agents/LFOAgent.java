/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.agents;

import vacuumCleaner.learning.LFO;
import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.LearningTrace;
import vacuumCleaner.simulator.perception.Perception;
import java.util.List;
import vacuumCleaner.agents.Agent;

/**
 *
 * @author santi
 */
public class LFOAgent extends Agent {
    LFO m_lfo;
    int time_step = 0;
    boolean started = false;

    public LFOAgent(LFO lfo, List<LearningTrace> learningTraces) {
        m_lfo = lfo;

        m_lfo.learn(learningTraces);
    }

    public void start()
    {
        m_lfo.start();
        started = true;
    }

    public void end()
    {
        m_lfo.end();
        started = false;
    }

    public Action cycle(int id,Perception p, double step) {
        if (!started) {
            m_lfo.start();
            started = true;
        }

        return m_lfo.cycle(p, id, time_step++);
    }

}

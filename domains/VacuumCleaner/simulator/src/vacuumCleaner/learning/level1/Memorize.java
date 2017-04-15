/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.learning.level1;

import vacuumCleaner.learning.LFO;
import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.LearningTrace;
import vacuumCleaner.simulator.LearningTraceEntry;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.perception.Perception;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author santi
 */
public class Memorize implements LFO {

    List<LearningTrace> memory = new LinkedList<LearningTrace>();

    int time = 0;
    LearningTrace current = null;

    public void learn(List<LearningTrace> traces) {
        memory.addAll(traces);
    }

    public void start() {
        Random r = new Random();
        current = memory.get(r.nextInt(memory.size()));
        time = 0;
    }

    public Action cycle(Perception s, int ID, int time) {
        for(LearningTraceEntry e:current.getEntries()) {
            if (e.time==time) {
                if (e.action == null) return null;
                Action a = (Action)(e.action.clone());
                a.setObjectID(ID);
                return a;
            }
            if (e.time>time) return null;
        }
        return null;
    }

    public void end() {
        current = null;
    }
    
}

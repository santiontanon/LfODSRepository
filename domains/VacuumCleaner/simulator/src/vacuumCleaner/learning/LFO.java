/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.learning;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.LearningTrace;
import vacuumCleaner.simulator.perception.Perception;
import java.util.List;

/**
 *
 * @author santi
 */
public interface LFO {
    public void learn(List<LearningTrace> traces);

    public void start();
    public Action cycle(Perception s, int ID, int time);
    public void end();
}

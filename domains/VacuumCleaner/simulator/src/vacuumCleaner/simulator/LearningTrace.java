/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator;

import vacuumCleaner.simulator.perception.Perception;
import util.XMLWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author santi
 */
public class LearningTrace {
    State start;
    int subject;
    List<LearningTraceEntry> entries;

    public LearningTrace(Trace t, Perception p) {
        start = t.start;
        subject = t.subject;
        entries = new LinkedList<LearningTraceEntry>();
        for(TraceEntry te:t.getEntries()) {
            Perception tmp = p.perceive(te.state, te.state.get(te.subject));
            entries.add(new LearningTraceEntry(te,tmp));
/*
            if (te.actions.size()==1 && te.actions.get(0).name.equals("left") &&
                tmp.get("l").equals("W") && tmp.get("r").equals("D")) {
                System.out.println("WTF!!!! " + te.time);
            }
 */
        }
    }

    public LearningTrace(State s, int objectID) {
        start = (State) s.clone();
        subject = objectID;
        entries = new LinkedList<LearningTraceEntry>();
    }

    public void addEntry(LearningTraceEntry e) {
        entries.add(e);
    }

    public List<LearningTraceEntry> getEntries() {
        return entries;
    }

    public State getState() {
        return start;
    }

    public int getSubject() {
        return subject;
    }
}

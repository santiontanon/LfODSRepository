/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator;

import vacuumCleaner.simulator.perception.Perception;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author santi
 */
public class LearningTraceEntry {
    public double time;
    public Perception perception;
    public int subject;
    public Action action;

    public LearningTraceEntry(TraceEntry te, Perception p) {
        time = te.time;
        perception = p;
        subject = te.subject;
        action = te.action;
    }

    public LearningTraceEntry(double t, Perception p, int a_subject, Action a) {
        time = t;
        perception = p;
        subject = a_subject;
        action = a;
    }

    public double getTime() {
        return time;
    }

}

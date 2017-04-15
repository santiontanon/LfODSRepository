/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator;

import vacuumCleaner.simulator.perception.Perception;
import util.XMLWriter;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author santi
 */
public class TraceEntry {
    public double time;
    public State state;
    public int subject;
    public Action action;

    public TraceEntry(double t, State p, int a_subject, Action a) {
        time = t;
        state = p;
        subject = a_subject;
        action = a;
    }

    public TraceEntry(Element e) {
        Element time_e = e.getChild("time");
        Element state_e = e.getChild("simulationstate");
        Element subject_e = e.getChild("subject");
        Element action_e = e.getChild("action");

        time = Double.parseDouble(time_e.getText());
        state = new State(state_e);
        subject = Integer.parseInt(subject_e.getText());

        if (action_e!=null) action = new Action(action_e);
                       else action = null;
    }

    public double getTime() {
        return time;
    }
    
    public void toxml(XMLWriter w) {
       w.tag("entry");
       w.tag("time",time);
       state.toxml(w);
       w.tag("subject",subject);
       if (action!=null) action.toxml(w);
       w.tag("/entry");
    }
}

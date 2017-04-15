/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator;

import vacuumCleaner.simulator.objects.PhysicalObject;
import vacuumCleaner.simulator.perception.Perception;
import util.XMLWriter;
import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author santi
 */
public class Trace {
    State start;
    int subject;
    List<TraceEntry> entries;

    public Trace(State s, int objectID) {
        start = (State) s.clone();
        subject = objectID;
        entries = new LinkedList<TraceEntry>();
    }

    public Trace(Element e) {
        Element start_e = e.getChild("simulationstate");
        Element subject_e = e.getChild("subject");
        Element entries_e = e.getChild("entries");

        start = new State(start_e);
        subject = Integer.parseInt(subject_e.getText());

        entries = new LinkedList<TraceEntry>();
        for(Object entry_o:entries_e.getChildren("entry")) {
            Element entry_e = (Element)entry_o;
            entries.add(new TraceEntry(entry_e));
        }
    }

    public void addEntry(TraceEntry e) {
        entries.add(e);
    }

    public List<TraceEntry> getEntries() {
        return entries;
    }

    public State getState() {
        return start;
    }

    public int getSubject() {
        return subject;
    }

    public Trace(State s, List<Action> actions, int objectID, double timeInterval) throws Exception
    {
        double time = 0;
        start = (State) s.clone();
        subject = objectID;
        entries = new LinkedList<TraceEntry>();
        for(Action a:actions) {
            PhysicalObject po = s.get(objectID);
            TraceEntry te = new TraceEntry(time, (State)s.clone(), objectID, a);
            entries.add(te);
            s.cycle(a, timeInterval);
            time+= timeInterval;
        }
    }

    public void toxml(XMLWriter w) {
       w.tag("trace");
       start.toxml(w);
       w.tag("subject",subject);
       w.tag("entries");
       for(TraceEntry te:entries) te.toxml(w);
       w.tag("/entries");
       w.tag("/trace");
    }


    public String toString() {
        StringWriter sw = new StringWriter();
        XMLWriter xmlw = new XMLWriter(sw);
        toxml(xmlw);
        return sw.toString();
    }

}

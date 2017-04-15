/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.learning.level3;

import vacuumCleaner.learning.LFO;
import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.LearningTrace;
import vacuumCleaner.simulator.LearningTraceEntry;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.TraceEntry;
import vacuumCleaner.simulator.perception.Perception;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author santi
 */
public class Level2WrapperWithoutActions implements LFO {
    int order = 3;
    LFO internalAlgorithm = null;

    List<Perception> pastStates = new LinkedList<Perception>();
    
    public Level2WrapperWithoutActions(int a_order, LFO a_internalAlgorithm) {
        order = a_order;
        internalAlgorithm = a_internalAlgorithm;
    }

    public void learn(List<LearningTrace> traces) {
        List<LearningTrace> processedTraces = new LinkedList<LearningTrace>();
        for(LearningTrace t:traces) {
            processedTraces.add(process(t));
        }
        internalAlgorithm.learn(processedTraces);
    }

    public void start() {
        pastStates.clear();
        internalAlgorithm.start();
    }
    
    public Action cycle(Perception s, int ID, int time) {
        Perception processed_s = new Perception();
        processed_s = append(processed_s,s,"0-");
        for(int j = 0;j<order;j++) {
            if (pastStates.size()<=j) {
                processed_s = append(processed_s,s,j + "-");
                if (j!=0) processed_s.getValues().put(j + "-action", 1);
            } else {
                processed_s = append(processed_s,pastStates.get(j),j + "-");
            }
        }

        Action a = internalAlgorithm.cycle(processed_s, ID, time);

        pastStates.add(0,s);

        return a;
    }

    public void end() {
        internalAlgorithm.end();
    }


    public LearningTrace process(LearningTrace t) {

        LearningTrace nt = new LearningTrace(t.getState(), t.getSubject());
        int l = t.getEntries().size();

        for(int i = 0;i<l;i++) {
            // create a trace entry with the combined perceptions form the past "order" entries:
            Perception p = new Perception();

            for(int j=0;j<order;j++) {
                int index = i-j;
                LearningTraceEntry e = null;
                if (index>=0) {
                    e = t.getEntries().get(index);
                } else {
                    e = t.getEntries().get(0);
                }
                p = append(p,e.perception,j + "-");
            }

//            System.out.println("Learning: " + p);

            LearningTraceEntry original = t.getEntries().get(i);
            LearningTraceEntry te = new LearningTraceEntry(original.getTime(), p, original.subject, original.action);
            nt.addEntry(te);
        }

//        System.out.println(nt.toString());

        return nt;
    }

    public Perception append(Perception p, Perception p2, String prefix) {
        Perception res = new Perception(p);
        for(String n:p2.getValues().keySet()) {
            res.getValues().put(prefix+n, p2.getValues().get(n));
        }

        return res;
    }
}

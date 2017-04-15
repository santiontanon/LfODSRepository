/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vacuumCleaner.metrics;

import java.util.LinkedList;
import java.util.List;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.TraceEntry;
import vacuumCleaner.simulator.perception.Perception;

/**
 * 
 * @author santi
 */
public class Equation11Level2Risk {
    
    public static double risk(Trace t1, Trace t2, Perception p, double laplace1, double laplace2) {
        List<Trace> lt1 = new LinkedList<Trace>();
        List<Trace> lt2 = new LinkedList<Trace>();
        lt1.add(t1);
        lt2.add(t2);
        return risk(lt1,lt2,p, laplace1, laplace2);
    }
    
    
    public static double risk(List<Trace> lt1, List<Trace> lt2, Perception p, double laplace1, double laplace2) {
        double risk = 0;
        double total = 0;
        
        for(Trace t1:lt1) {
            for(TraceEntry te1:t1.getEntries()) {
                Perception p1 = p.perceive(te1.state, te1.state.get(te1.subject));
                double risk_tmp = 0;
                double total_tmp = 0;
                for(Trace t2:lt2) {
                    for(TraceEntry te2:t2.getEntries()) {
                        Perception p2 = p.perceive(te2.state, te2.state.get(te2.subject));
                        if (p1.equals(p2)) {
                            if (te1.action==null) {
                                if (te2.action==null) risk_tmp++;
                            } else {
                                if (te1.action.equals(te2.action)) risk_tmp++;
                            }
                        }
                        total_tmp++;
                    }
                }
                risk += Math.log((risk_tmp+laplace1)/(total_tmp+laplace2));
                total++;
            }
        }
        
        return -risk/total;
    }   
        
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vacuumCleaner.metrics;

import java.util.LinkedList;
import java.util.List;
import vacuumCleaner.simulator.LearningTrace;
import vacuumCleaner.simulator.LearningTraceEntry;
import vacuumCleaner.simulator.perception.Perception;

/**
 * 
 * @author santi
 */
public class Equation11Level2RiskLearningTrace {
    
    
    public static double risk(LearningTrace t1, LearningTrace t2, Perception p, double laplace1, double laplace2) {
        List<LearningTrace> lt1 = new LinkedList<LearningTrace>();
        List<LearningTrace> lt2 = new LinkedList<LearningTrace>();
        lt1.add(t1);
        lt2.add(t2);
        return risk(lt1,lt2,p, laplace1, laplace2);
    }
    
    
    // if "laplace = 0", it estimates raw probabilities. 
    public static double risk(List<LearningTrace> lt1, List<LearningTrace> lt2, Perception p, double laplace1, double laplace2) {
        double risk = 0;
        double total = 0;
        
        for(LearningTrace t1:lt1) {
            for(LearningTraceEntry te1:t1.getEntries()) {
                Perception p1 = te1.perception;
                double risk_tmp = 0;
                double total_tmp = 0;
                for(LearningTrace t2:lt2) {
                    for(LearningTraceEntry te2:t2.getEntries()) {
                        Perception p2 = te2.perception;
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
                risk += Math.log((risk_tmp + laplace1)/(total_tmp + laplace2));
                total++;
            }
        }
        
        return -risk/total;
    }       
    
    
}

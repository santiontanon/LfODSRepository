/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vacuumCleaner.metrics;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.TraceEntry;
import vacuumCleaner.simulator.perception.Perception;
import util.StatisticalTests;

/**
 * 
 * @author santi
 */
public class KLLevel3TraceDistance {
    
    /*
     * This class is like 'KLLevel2TraceDistance' but accepts an additional parameters 'order'
     * When order == 1, it acts like the old class
     * When order > 1 it takes past actions and perceptions into account to create the histograms
     */
    
    /*
     * This method computes, for each possible state, the histogram of actions
     * Then computes the KL distance for each state, and returns the average
     */
    public static double traceExpectedKL(Trace t1, Trace t2, Perception p, int order) {
        List<Action> actions = new LinkedList<Action>();
        HashMap<List<Double>,int []> histograms_t1 = new HashMap<List<Double>,int []>();
        HashMap<List<Double>,int []> histograms_t2 = new HashMap<List<Double>,int []>();
        
        getAllActions(t1,actions);
        getAllActions(t2,actions);
        
//        System.out.println(actions);
                
        generateHistograms(t1, histograms_t1, actions, p, order);
        generateHistograms(t2, histograms_t2, actions, p, order);
        
        return compareHistogramsSmoothing(histograms_t1, histograms_t2, actions.size());
    }
    
    
    /*
     * This method computes, for each possible state, the histogram of actions
     * Then computes the KL distance for each state, and returns the average
     */
    public static double tracesExpectedKL(List<Trace> tl1, List<Trace> tl2, Perception p, int order) {
        List<Action> actions = new LinkedList<Action>();
        HashMap<List<Double>,int []> histograms_t1 = new HashMap<List<Double>,int []>();
        HashMap<List<Double>,int []> histograms_t2 = new HashMap<List<Double>,int []>();
        
        for(Trace t1:tl1) getAllActions(t1,actions);
        for(Trace t2:tl2) getAllActions(t2,actions);
        
        for(Trace t1:tl1) generateHistograms(t1, histograms_t1, actions, p, order);
        for(Trace t2:tl2) generateHistograms(t2, histograms_t2, actions, p, order);
        
        return compareHistogramsSmoothing(histograms_t1, histograms_t2, actions.size());
    }    
    
    
    
    static void getAllActions(Trace t, List<Action> actions) {
        for(TraceEntry te:t.getEntries()) 
            if (!actions.contains(te.action)) actions.add(te.action);
    }
    
    
    static void generateHistograms(Trace t, HashMap<List<Double>,int []> histograms, List<Action> actions, Perception p, int order) {
        // Compute the histograms for t1:
        for(int i = 0;i<t.getEntries().size();i++) {
            List<Double> accum = new LinkedList<Double>();
            for(int j = i-(order-1);j<=i;j++) {
                if (j<0) {
                    TraceEntry te = t.getEntries().get(0);
                    Perception tep = p.perceive(te.state, te.state.get(te.subject));
                    List<Double> tmp = tep.getAsArrayOfDoubles();
                    if (j!=i) {
                        // also add the action:
                        if (actions.indexOf(null)==-1) actions.add(null);
                        tmp.add(new Double(actions.indexOf(null)));
                    }
                    accum.addAll(tmp);                    
                } else {
                    TraceEntry te = t.getEntries().get(j);
                    Perception tep = p.perceive(te.state, te.state.get(te.subject));
                    List<Double> tmp = tep.getAsArrayOfDoubles();
                    if (j!=i) {
                        // also add the action:
                        tmp.add(new Double(actions.indexOf(te.action)));
                    }
                    accum.addAll(tmp);
                }
            }
            int []histogram = null;

            histogram = histograms.get(accum);
            if (histogram==null) {
                histogram = new int[actions.size()];
                histograms.put(accum,histogram);
            }

            histogram[actions.indexOf(t.getEntries().get(i).action)]++;
        }
    }
    
    
    
    /*
     * E is the expected distribution
     * O is the observed one
     */
    static double compareHistogramsSmoothing(HashMap<List<Double>,int []> histograms_E,
                                             HashMap<List<Double>,int []> histograms_O, int size) {
        // Compare histograms (only in the shared ones):
        double accum = 0;
        int nTotalSamples = 0;
        
        double []freq1 = new double[size];
        double []freq2 = new double[size];
        for(List<Double> state:histograms_E.keySet()) {
            int []h1 = histograms_E.get(state);
            int []h2 = histograms_O.get(state);
            
//            System.out.println(state + " -> " + h1[0] + "," + h1[1]);
            
            if (h2!=null) {
                int total1 = 0, total2 = 0;
                for(int c:h1) total1+=c;
                for(int c:h2) total2+=c;
                for(int i = 0;i<h1.length;i++) {
                    // Laplace smoothing:
                    freq1[i] = ((double)h1[i]+1)/((double)total1+size);
                    freq2[i] = ((double)h2[i]+1)/((double)total2+size);
                }
                double KL = StatisticalTests.KLNonZero(freq1, freq2);
                /* 
                {
                    System.out.print("[ ");
                    for(double tmp:freq1) System.out.print(tmp + " ");
                    System.out.print("] [ ");
                    for(double tmp:freq2) System.out.print(tmp + " ");
                    System.out.println("] -> " + KL);
                }
                */
                accum += KL*total1;
                nTotalSamples+=total1;
            }
        }
        
        if (nTotalSamples==0) return 0;        
        return accum/nTotalSamples;        
    }    
    
}

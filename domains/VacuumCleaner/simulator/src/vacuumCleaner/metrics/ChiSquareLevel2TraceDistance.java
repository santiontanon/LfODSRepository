/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vacuumCleaner.metrics;

import java.util.LinkedList;
import java.util.List;
import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.TraceEntry;
import vacuumCleaner.simulator.perception.Perception;
import util.Pair;
import util.StatisticalTests;

/**
 * 
 * @author santi
 */
public class ChiSquareLevel2TraceDistance {
    
    /*
     * This method computes, for each possible state, the histogram of pairs (x,y)
     * Then computes the Chi-Square distance 
     * 
     */
    public static double traceAverageChiSquare(Trace t1, Trace t2, Perception p) {
        List<Trace> l1 = new LinkedList<Trace>();
        List<Trace> l2 = new LinkedList<Trace>();
        l1.add(t1);
        l2.add(t2);
        return tracesAverageChiSquare(l1,l2,p);
    }
    
    
    /*
     * This method computes, for each possible state, the histogram of actions
     * Then computes the Chi-Square test for each state, and returns the average
     * 
     */
    public static double tracesAverageChiSquare(List<Trace> tl1, List<Trace> tl2, Perception p) {
        List<Pair<List<Double>,Action>> differentValues = new LinkedList<Pair<List<Double>,Action>>();
        int [] histogram_t1 = null;
        int [] histogram_t2 = null;
        
        for(Trace t1:tl1) getAllValues(t1,differentValues, p);
        for(Trace t2:tl2) getAllValues(t2,differentValues, p);
        
        for(Trace t1:tl1) histogram_t1 = generateHistograms(t1, differentValues, p);
        for(Trace t2:tl2) histogram_t2 = generateHistograms(t2, differentValues, p);
        
//        return compareHistograms(histograms_t1, histograms_t2, actions.size());
        return compareHistogramsSmoothing(histogram_t1, histogram_t2);
    }    
    
    
    static void getAllValues(Trace t, List<Pair<List<Double>,Action>> values, Perception p) {
        for(TraceEntry te:t.getEntries()) {
            Perception tep = p.perceive(te.state, te.state.get(te.subject));
            Pair<List<Double>, Action> value = new Pair<List<Double>, Action>(tep.getAsArrayOfDoubles(), te.action);
            if (!values.contains(value)) values.add(value);
        }
    }
    
    
    static int [] generateHistograms(Trace t, List<Pair<List<Double>,Action>> values, Perception p) {
        int []histogram = new int[values.size()];
        // Compute the histograms for t1:
        for(TraceEntry te:t.getEntries()) {
            Perception tep = p.perceive(te.state, te.state.get(te.subject));
            Pair<List<Double>, Action> value = new Pair<List<Double>, Action>(tep.getAsArrayOfDoubles(), te.action);
            int v = values.indexOf(value);
            histogram[v]++;
        }        
        return histogram;
    }
    
    
    /*
     * E is the expected distribution
     * O is the observed one
     */
    static double compareHistograms(int [] histogram_E, int [] histogram_O) {
        int size = histogram_E.length;
        
        double []freq1 = new double[size];
        double []freq2 = new double[size];
        
        int total1 = 0, total2 = 0;
        for(int c:histogram_E) total1+=c;
        for(int c:histogram_O) total2+=c;
        for(int i = 0;i<histogram_E.length;i++) {
            freq1[i] = ((double)histogram_E[i])/((double)total1);
            freq2[i] = ((double)histogram_O[i])/((double)total2);
        }
        double CHISQ = StatisticalTests.chiSquaredNonZero(freq1, freq2);
        return CHISQ;
    }
    
    
    /*
     * E is the expected distribution
     * O is the observed one
     */
    static double compareHistogramsSmoothing(int [] histogram_E, int [] histogram_O) {
        int size = histogram_E.length;
        
        double []freq1 = new double[size];
        double []freq2 = new double[size];
        
        int total1 = 0, total2 = 0;
        for(int c:histogram_E) total1+=c;
        for(int c:histogram_O) total2+=c;
        for(int i = 0;i<histogram_E.length;i++) {
            freq1[i] = ((double)histogram_E[i]+1)/((double)total1+size);
            freq2[i] = ((double)histogram_O[i]+1)/((double)total2+size);
        }
        double CHISQ = StatisticalTests.chiSquaredNonZero(freq1, freq2);
        return CHISQ;                
    }    
}

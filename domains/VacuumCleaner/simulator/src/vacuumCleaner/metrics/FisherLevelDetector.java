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
import vacuumCleaner.simulator.perception.Perception;

/**
 *
 * @author santi
 */
public class FisherLevelDetector {
    
    public static double correlation(Trace t1, Perception p) {
        List<Action> actions = new LinkedList<Action>();
        HashMap<List<Double>,int []> histograms_t1 = new HashMap<List<Double>,int []>();
        
        ChiSquareConditionalLevel2TraceDistance.getAllActions(t1,actions);
                
        ChiSquareConditionalLevel2TraceDistance.generateHistograms(t1, histograms_t1, actions, p);
        
//        return compareHistograms(histograms_t1, histograms_t2, actions.size());
        return FisherCorrelation(histograms_t1, actions.size());
    }
    
    public static double logCorrelation(Trace t1, Perception p) {
        List<Action> actions = new LinkedList<Action>();
        HashMap<List<Double>,int []> histograms_t1 = new HashMap<List<Double>,int []>();
        
        ChiSquareConditionalLevel2TraceDistance.getAllActions(t1,actions);
                
        ChiSquareConditionalLevel2TraceDistance.generateHistograms(t1, histograms_t1, actions, p);
        
//        return compareHistograms(histograms_t1, histograms_t2, actions.size());
        return logFisherCorrelation(histograms_t1, actions.size());
    }
    
    /*
     * This method computes, for each possible state, the histogram of actions
     * Then computes the Chi-Square test for each state, and returns the average
     * 
     * How is this related to accuracy?
     */
    public static double correlation(List<Trace> tl1, Perception p) {
        List<Action> actions = new LinkedList<Action>();
        HashMap<List<Double>,int []> histograms_t1 = new HashMap<List<Double>,int []>();
        
        for(Trace t1:tl1) ChiSquareConditionalLevel2TraceDistance.getAllActions(t1,actions);
        
        for(Trace t1:tl1) ChiSquareConditionalLevel2TraceDistance.generateHistograms(t1, histograms_t1, actions, p);
        
//        return compareHistograms(histograms_t1, histograms_t2, actions.size());
        return FisherCorrelation(histograms_t1, actions.size());
    }  

    
    public static double logCorrelation(List<Trace> tl1, Perception p) {
        List<Action> actions = new LinkedList<Action>();
        HashMap<List<Double>,int []> histograms_t1 = new HashMap<List<Double>,int []>();
        
        for(Trace t1:tl1) ChiSquareConditionalLevel2TraceDistance.getAllActions(t1,actions);
        
        for(Trace t1:tl1) ChiSquareConditionalLevel2TraceDistance.generateHistograms(t1, histograms_t1, actions, p);
        
//        return compareHistograms(histograms_t1, histograms_t2, actions.size());
        return logFisherCorrelation(histograms_t1, actions.size());
    }  

    
    public static double FisherCorrelation(HashMap<List<Double>, int []> histograms_O, int size) {
        
        int row_size = size;
        int col_size = histograms_O.keySet().size();
        int table[][] = new int[col_size][row_size];
        int R[] = new int[col_size];
        int C[] = new int[row_size];
        int N = 0;

        int i = 0;
        for(int j = 0;j<col_size;j++) R[j] = 0;
        for(int j = 0;j<row_size;j++) C[j] = 0;
        
        for(List<Double> tmp:histograms_O.keySet()) {
            int []tmp2 = histograms_O.get(tmp);
            for(int j = 0;j<row_size;j++) {
                table[i][j] = tmp2[j];
                R[i]+=tmp2[j];
                C[j]+=tmp2[j];
                N+=tmp2[j];
            }
            i++;
        }

        /*
        // print table:
        {
            for(i = 0;i<col_size;i++) {
                System.out.print("R(" + R[i] + ") =  ");                
                for(int j = 0;j<row_size;j++) {
                    System.out.print(table[i][j] + "  ");
                }
                System.out.println("");
            }
            System.out.println("-");
            for(int j = 0;j<row_size;j++) {
                System.out.print(C[j] + "  ");
            }
            System.out.println("N = " + N);
        }
        /*
        // print table for R:
        System.out.print("matrix(c(");
        for(i = 0;i<col_size;i++) {
            for(int j = 0;j<row_size;j++) {
                System.out.print(table[i][j] + ",");
            }
        }
        System.out.println("),nr = " + col_size + ")");
        */
        
//        for(int j = 0;j<col_size;j++) System.out.println(R[j] + "! = " + factorial(R[j]));
                
        // Compute the fisher's exact test:
        double numerator = 1;
        double denominator = factorial(N);
        
        for(int j = 0;j<col_size;j++) numerator*=factorial(R[j]);
        for(int j = 0;j<row_size;j++) numerator*=factorial(C[j]);

        for(i = 0;i<col_size;i++) 
            for(int j = 0;j<row_size;j++) denominator*=factorial(table[i][j]);
        
        return numerator/denominator;
    }
    
    
    public static double logFisherCorrelation(HashMap<List<Double>, int []> histograms_O, int size) {
        
        int row_size = size;
        int col_size = histograms_O.keySet().size();
        int table[][] = new int[col_size][row_size];
        int R[] = new int[col_size];
        int C[] = new int[row_size];
        int N = 0;

        int i = 0;
        for(int j = 0;j<col_size;j++) R[j] = 0;
        for(int j = 0;j<row_size;j++) C[j] = 0;
        
        for(List<Double> tmp:histograms_O.keySet()) {
            int []tmp2 = histograms_O.get(tmp);
            for(int j = 0;j<row_size;j++) {
                table[i][j] = tmp2[j];
                R[i]+=tmp2[j];
                C[j]+=tmp2[j];
                N+=tmp2[j];
            }
            i++;
        }
        
        // Compute the Fisher's exact test using logarithms:
        double log_numerator = 0;
        double log_denominator = log_factorial(N);
        
        for(int j = 0;j<col_size;j++) log_numerator+=log_factorial(R[j]);
        for(int j = 0;j<row_size;j++) log_numerator+=log_factorial(C[j]);

        for(i = 0;i<col_size;i++) 
            for(int j = 0;j<row_size;j++) log_denominator+=log_factorial(table[i][j]);
        
        return log_numerator - log_denominator;
    }
    
        
    // since we will compute the factorial of large numbers, we want the resutl to be a double:
    public static double factorial(int n) {
        if (n<=1) return 1.0;
        return ((double)n)*factorial(n-1);
    }
    
    // since we will compute the factorial of large numbers, we want the resutl to be a double:
    public static double log_factorial(int n) {
        if (n<=1) return 0;
        return ((double)Math.log(n)) + log_factorial(n-1);
    }

}

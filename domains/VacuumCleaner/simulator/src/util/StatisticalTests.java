/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author santi
 */
public class StatisticalTests {
    
    // Pearson's Chi-Square test:
    public static double chiSquared(double []E, double []O) {
        double accum = 0;
        for(int i = 0;i<E.length;i++) {
            accum += (O[i]-E[i])*(O[i]-E[i])/E[i];
        }
        return accum;
    }

    
    // Pearson's Chi-Square distance only on the non-zero Es:
    public static double chiSquaredNonZero(double []E, double []O) {
        double accum = 0;
        for(int i = 0;i<E.length;i++) {
            if (E[i]>0)
                accum += ((O[i]-E[i])*(O[i]-E[i]))/E[i];
        }
        if (Double.isNaN(accum)) {
            System.out.print("chiSquaredNonZero [ ");
            for(double tmp:E) System.out.print(tmp + " ");
            System.out.print("] [ ");
            for(double tmp:O) System.out.print(tmp + " ");
            System.out.println("] -> " + accum);
        }
        return accum;
    }
    
    
    // KL divergence only on the non-zero Es:
    public static double KLNonZero(double []E, double []O) {
        double accum = 0;
        for(int i = 0;i<E.length;i++) {
            if (E[i]>0)
                accum += Math.log(O[i]/E[i]) * O[i];
        }
        if (Double.isNaN(accum)) {
            System.out.print("KLNonZero [ ");
            for(double tmp:E) System.out.print(tmp + " ");
            System.out.print("] [ ");
            for(double tmp:O) System.out.print(tmp + " ");
            System.out.println("] -> " + accum);
        }
        return accum;
    }
        
    
    
    public static boolean chiSquaredStatisticallySignificantDifference(double result, int freedom, double p) {
        double pvalues[] = {0.95,  0.9,  0.8,  0.7,  0.5,  0.3,  0.2,  0.1,  0.05,  0.01,  0.001};
        double chisq[][] ={{0.004, 0.02, 0.06, 0.15, 0.46, 1.07, 1.64, 2.71, 3.84,  6.64, 10.83},
                           {0.1,   0.21, 0.45, 0.71, 1.39, 2.41, 3.22, 4.60, 5.99,  9.21, 13.82},
                           {0.35,  0.58, 1.01, 1.42, 2.37, 3.68, 4.64, 6.25, 7.82, 11.34, 16.27},
                           {0.71,  1.06, 1.65, 2.20, 3.36, 4.88, 5.99, 7.78, 9.49, 13.28, 18.47},
                          };
        double []tbl = chisq[freedom-1];
        for(int i = 0;i<pvalues.length;i++) {
            if (result>tbl[i] && pvalues[i]<=p) return true;
        }
        return false;
    }
    

    
    // G-test (recommended over Chi-squared):
    public static double GTest(double []E, double []O) {
        double accum = 0;
        for(int i = 0;i<E.length;i++) {
            accum += O[i]*Math.log(O[i]/E[i]);
        }
        return 2*accum;
    }

    
    // G-test (recommended over Chi-squared):
    public static double GTestNonZero(double []E, double []O) {
        double accum = 0;
        for(int i = 0;i<E.length;i++) {
            if (E[i]>0 && O[i]>0)
                accum += O[i]*Math.log(O[i]/E[i]);
        }
        return 2*accum;
    }
    
    
    public static void main(String args[]) {
        double []E =  {0.2, 0.6, 0.2, 0.0, 0.0};
        double []O1 = {0.2, 0.5, 0.2, 0.1, 0.0};
        double []O2 = {0.0, 0.0, 0.2, 0.4, 0.4};
        double []O3 = {0.0, 0.0, 0.0, 0.5, 0.5};

        double []T1 = {0.0, 0.0, 0.0, 1.0, 0.5};
        double []T2 = {0.3125, 0.1875, 0.25, 0.125, 0.125};
        
        System.out.println(chiSquaredNonZero(T1, T2));
        
//        [ 0.0 0.0 0.0 1.0 0.0 ] [ 0.3125 0.1875 0.25 0.125 0.125 ]

        System.out.println("Same distributions: -------------------------");
        System.out.println("Chi-square: " + chiSquared(E, E) + " / " + chiSquared(E,E));
        System.out.println("Chi-square (non-zero): " + chiSquaredNonZero(E, E) + " / " + chiSquaredNonZero(E, E));
        System.out.println("G-test: " + GTest(E, E) + " / " + GTest(E, E));
        System.out.println("G-test (non-zero): " + GTestNonZero(E, E) + " / " + GTestNonZero(E, E));
        
        System.out.println("Very similar distributions: -------------------------");
        System.out.println("Chi-square: " + chiSquared(E, O1) + " / " + chiSquared(O1,E));
        System.out.println("Chi-square (non-zero): " + chiSquaredNonZero(E, O1) + " / " + chiSquaredNonZero(O1, E));
        System.out.println("G-test: " + GTest(E, O1) + " / " + GTest(O1, E));
        System.out.println("G-test (non-zero): " + GTestNonZero(E, O1) + " / " + GTestNonZero(O1, E));
    
        System.out.println("Very different distributions: -------------------------");
        System.out.println("Chi-square: " + chiSquared(E, O2) + " / " + chiSquared(O2,E));
        System.out.println("Chi-square (non-zero): " + chiSquaredNonZero(E, O2) + " / " + chiSquaredNonZero(O2, E));
        System.out.println("G-test: " + GTest(E, O2) + " / " + GTest(O2, E));
        System.out.println("G-test (non-zero): " + GTestNonZero(E, O2) + " / " + GTestNonZero(O2, E));
    
        System.out.println("Disjoint distributions: -------------------------");
        System.out.println("Chi-square: " + chiSquared(E, O3) + " / " + chiSquared(O3,E));
        System.out.println("Chi-square (non-zero): " + chiSquaredNonZero(E, O3) + " / " + chiSquaredNonZero(O3, E));
        System.out.println("G-test: " + GTest(E, O3) + " / " + GTest(O3, E));
        System.out.println("G-test (non-zero): " + GTestNonZero(E, O3) + " / " + GTestNonZero(O3, E));
    }

}

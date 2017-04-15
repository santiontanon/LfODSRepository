/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.metrics;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationInteger;
import be.ac.ulg.montefiore.run.jahmm.OpdfInteger;
import be.ac.ulg.montefiore.run.jahmm.OpdfIntegerFactory;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;
import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.Trace;
import java.util.LinkedList;
import java.util.List;
import vacuumCleaner.simulator.TraceEntry;

/**
 *
 * @author santi
 *
 * Note: this class assumes that there is only a single action in each trace entry
 */
public class HMMDistanceGivenTraces {

    /*
     * This function constructs an HMM of 'nStates' states for each set of traces,
     * and then compares them using the Kullback-Lieber Distance
     */

    static List<Action> action_table = new LinkedList<Action>();
                                // this list contains one instance of each type of action,
                                // and serves as the translation table from actions to observations.

    // nTrials: is the number of times to evalaute the distance (since initial random weight evaluation has an impact)
    //          the minimum distance obtained in all trials will be returned
    // nIterations: the number of iterations to run the BaumWelchScaledLearner in each trial
    public static double distance(List<Trace> traces1,
                                  List<Trace> traces2, int nStates, int nIterations, boolean symmetric) {

        List<List<ObservationInteger>> lo1 = new LinkedList<List<ObservationInteger>>();
        List<List<ObservationInteger>> lo2 = new LinkedList<List<ObservationInteger>>();

        for(Trace t:traces1) lo1.add(toObservationSequence(t));
        for(Trace t:traces2) lo2.add(toObservationSequence(t));

//        for(List<ObservationInteger> t:lo1) System.out.println(t);
//        for(List<ObservationInteger> t:lo2) System.out.println(t);

        Hmm<ObservationInteger> hmm1 = new Hmm<ObservationInteger>(nStates,new OpdfIntegerFactory(action_table.size()));
        Hmm<ObservationInteger> hmm2 = new Hmm<ObservationInteger>(nStates,new OpdfIntegerFactory(action_table.size()));

        // randomize the probabilities (if they are uniform, the learning algorithm struggles):
        {
            double total1 = 0.;
            double total2 = 0.;
            for(int i = 0;i<nStates;i++) {
                hmm1.setPi(i, Math.random());
                hmm2.setPi(i, Math.random());
                total1+=hmm1.getPi(i);
                total2+=hmm2.getPi(i);
            }
            for(int i = 0;i<nStates;i++) {
                hmm1.setPi(i, hmm1.getPi(i)/total1);
                hmm2.setPi(i, hmm2.getPi(i)/total2);
            }
        }


        // randomize the probabilities (if they are uniform, the learning algorithm struggles):
        {
            double total1 = 0.;
            double total2 = 0.;
            for(int i = 0;i<nStates;i++) {
                hmm1.setPi(i, Math.random());
                hmm2.setPi(i, Math.random());
                total1+=hmm1.getPi(i);
                total2+=hmm2.getPi(i);

                double totalAij1 = 0.;
                double totalAij2 = 0.;
                for(int j = 0;j<nStates;j++) {
                    hmm1.setAij(i, j, Math.random());
                    totalAij1 += hmm1.getAij(i, j);
                    hmm2.setAij(i, j, Math.random());
                    totalAij2 += hmm2.getAij(i, j);
                }
                for(int j = 0;j<nStates;j++) {
                    hmm1.setAij(i, j, hmm1.getAij(i, j)/totalAij1);
                    hmm2.setAij(i, j, hmm2.getAij(i, j)/totalAij2);
                }

                {
                    // create a random OpdfInteger for the actions
                    double []aopdf1 = new double[action_table.size()];
                    double []aopdf2 = new double[action_table.size()];
                    double totalA1 = 0.;
                    double totalA2 = 0.;
                    for(int j = 0;j<action_table.size();j++) {
                        aopdf1[j] = Math.random();
                        totalA1 += aopdf1[j];
                        aopdf2[j] = Math.random();
                        totalA2 += aopdf2[j];
                    }
                    for(int j = 0;j<action_table.size();j++) {
                        aopdf1[j]/=totalA1;
                        aopdf2[j]/=totalA2;
                    }
                    hmm1.setOpdf(i, new OpdfInteger(aopdf1));
                    hmm2.setOpdf(i, new OpdfInteger(aopdf2));
                }

            }
            for(int i = 0;i<nStates;i++) {
                hmm1.setPi(i, hmm1.getPi(i)/total1);
                hmm2.setPi(i, hmm2.getPi(i)/total2);
            }
        }



//        System.out.println("HMM1:\n" + hmm1);
//        System.out.println("HMM2:\n" + hmm2);

//		BaumWelchLearner learner = new BaumWelchLearner();
//            BaumWelchScaledLearner learner = new BaumWelchScaledLearner();
        KMeansLearner learner1 = new KMeansLearner(nStates,new OpdfIntegerFactory(action_table.size()),lo1);
        KMeansLearner learner2 = new KMeansLearner(nStates,new OpdfIntegerFactory(action_table.size()),lo2);
        for (int i = 0; i < nIterations; i++) {
//            hmm1 = learner.iterate(hmm1, lo1);
//            hmm2 = learner.iterate(hmm2, lo2);
            hmm1 = learner1.iterate();
            hmm2 = learner2.iterate();
        }


        KullbackLeiblerDistanceCustom klc = new KullbackLeiblerDistanceCustom();
        if (symmetric) {
            return (klc.distance(hmm1,hmm2,lo1) +
                    klc.distance(hmm2,hmm1,lo2)/2);
        } else {
            return klc.distance(hmm1,hmm2,lo1);
        }
    }

    static List<ObservationInteger> toObservationSequence(Trace t) {
        List<ObservationInteger> l = new LinkedList<ObservationInteger>();

        for(TraceEntry te:t.getEntries()) {
            Action a = te.action;

            int index = action_table.indexOf(a);
            if (index==-1) {
                index = action_table.size();
                action_table.add(a);
            }
            l.add(new ObservationInteger(index));
        }
        return l;
    }
}

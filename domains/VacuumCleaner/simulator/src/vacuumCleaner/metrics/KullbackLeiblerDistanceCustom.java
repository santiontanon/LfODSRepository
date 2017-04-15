/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vacuumCleaner.metrics;

import be.ac.ulg.montefiore.run.jahmm.ForwardBackwardScaledCalculator;
import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.Observation;
import java.util.List;

/**
 *
 * @author santi
 */
public class KullbackLeiblerDistanceCustom {

    public <O extends Observation> double distance(Hmm<O> hmm1, Hmm<? super O> hmm2, List<List<O>> sequences) {
        double distance = 0.;

        for (int i = 0; i < sequences.size(); i++) {

            List<O> oseq = sequences.get(i);
            double p1 = new ForwardBackwardScaledCalculator(oseq, hmm1).lnProbability();
            double p2 = new ForwardBackwardScaledCalculator(oseq, hmm2).lnProbability();
            distance += (p1 - p2) / oseq.size();
//            System.out.println(p1 + " - " + p2 + " / " + oseq.size() + " -> " + distance);
        }

        return distance / sequences.size();
    }
}

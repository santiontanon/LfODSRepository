/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.learning.level2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import vacuumCleaner.learning.LFO;
import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.LearningTrace;
import vacuumCleaner.simulator.LearningTraceEntry;
import vacuumCleaner.simulator.perception.Perception;
import util.Pair;

/**
 *
 * @author santi
 */
public class KNN implements LFO {
    Random r = new Random();

    public class Example {
        Perception perception;
        HashMap<Action,Integer> histogram;

        public Example(Perception p, Action a) {
            perception = p;
            histogram = new HashMap<Action,Integer>();
            histogram.put(a,1);
        }

        public Action getAction() {
            Action a = null;
            int c = 0;
            for(Action a2:histogram.keySet()) {
                int c2 = histogram.get(a2);
                if (c2>c) {
                    c = c2;
                    a = a2;
                }
            }
            return a;
        }

        public Action getActionStochastic() {
            List<Action> l = new LinkedList<Action>();
            l.addAll(histogram.keySet());
            int total = 0;
            for(Action a:l) {
                int c = histogram.get(a);
                total += c;
            }
            int num = r.nextInt(total);
            for(Action a:l) {
                int c = histogram.get(a);
                if (num<c) return a;
                num-=c;
            }
            return null;
        }

        public String toString() {
            String s1 = "(" + perception + ", ";
            String s2 = "[ ";
            for(Action a:histogram.keySet()) {
                s2 += a.getName() + " = " + histogram.get(a) + " ";
            }
            s2 += "])";
            return s1 + s2;
        }
    }
    

    public static int DEBUG = 0;
    
    int K = 3;
    List<Example> trainingSet = new LinkedList<Example>();
    HashMap<String,Pair<Double,Double>> featureRanges = new HashMap<String,Pair<Double,Double>>();


    public KNN(int a_K) {
        K = a_K;
    }

    public void learn(List<LearningTrace> traces) {
        for(LearningTrace t:traces) {
            for(LearningTraceEntry e:t.getEntries()) addExample(e);
        }

        for(Example e:trainingSet) updateRange(e);

        System.out.println(trainingSet.size() + " examples learnt");
    }

    public void addExample(LearningTraceEntry lte) {
        Example found = null;
        Perception s = lte.perception;
        Action a = null;
        if (lte.action!=null) a = lte.action;
        for(Example e:trainingSet) {
            double sim = similarity(s, e.perception);
            if (sim==1.0) {
                found = e;
                break;
            }
        }
        if (found == null) {
            trainingSet.add(new Example(lte.perception,a));
        } else {
            Integer count = found.histogram.get(a);
            if (count==null) {
                found.histogram.put(a,1);
            } else {
                found.histogram.put(a,count+1);
            }
        }
    }

    public void start() {
    }

    public Action cycle(Perception s, int ID, int time) {
    
        //find the K nearest neighbors:
        List<Pair<Double,Example>> mostSimilar = KNN(s, trainingSet, K);
//        System.out.println("KNN perception: " + s);

        if (DEBUG>0) {
            System.out.println("KNN retrieved the following examples with perception: " + s);
            for(Pair<Double,Example> e:mostSimilar) {
                System.out.println(e.m_a + " - " + e);
            }
        }

        // vote for actions:
        List<Pair<Double,Action>> votes = new LinkedList<Pair<Double,Action>>();

        for(Pair<Double,Example> example:mostSimilar) {
            Pair<Double,Action> found = null;
//            Action a = example.m_b.getAction();
            Action a = example.m_b.getActionStochastic();
            for(Pair<Double,Action> vote:votes) {
                if (vote.m_b==null) {
                    if (a==null) {
                        found = vote;
                        break;
                    }
                } else {
                    if (vote.m_b.equals(a)) {
                        found = vote;
                        break;
                    }
                }
            }
            if (found==null) {
                votes.add(new Pair<Double,Action>(example.m_a,a));
            } else {
                found.m_a += example.m_a;
            }
        }

        // select the top action:
        Pair<Double,Action> mostVoted = null;
        for(Pair<Double,Action> vote:votes) {
            if (mostVoted==null || vote.m_a > mostVoted.m_a) mostVoted = vote;
        }

        return mostVoted.m_b;
    }

    public void end() {
    }


    List<Pair<Double,Example>> KNN(Perception s, List<Example> examples, int K) {
        List<Pair<Double,Example>> mostSimilar = new LinkedList<Pair<Double,Example>>();

        for(Example e:examples) {
            try {
                double sim = similarity(s, e.perception);

//                System.out.println(sim + " : " + (action!=null ? action.getName() : "."));

                if (mostSimilar.size()>=K) {
                    Pair<Double,Example> worst = null;
                    for(Pair<Double,Example> tmp:mostSimilar) {
                        if (worst == null || tmp.m_a<worst.m_a) worst = tmp;
                    }
                    if (worst.m_a<sim) {
                        mostSimilar.remove(worst);
                        mostSimilar.add(new Pair<Double,Example>(sim,e));
                    }
                } else {
                    mostSimilar.add(new Pair<Double,Example>(sim,e));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

//        System.out.println("KNN: ");
//       for(Pair<Double,Example> tmp:mostSimilar) {
//            System.out.println(s.toBayesNetVariables() + " -> " + tmp.m_a + " : " + tmp.m_b);
//        }
//        System.out.println("----");

        return mostSimilar;
    }


    double similarity(Perception p1, Perception p2) {
        int total = 0;
        double sim = 0;

        for(String f:p1.getValues().keySet()) {
            Number v1 = p1.getValues().get(f);
            Number v2 = p2.getValues().get(f);
            double v1d = 0;
            double v2d = 0;
            if (v1 instanceof Integer) v1d = (Integer)v1;
            if (v2 instanceof Integer) v2d = (Integer)v2;
            if (v1 instanceof Double) v1d = (Double)v1;
            if (v2 instanceof Double) v2d = (Double)v2;
            sim += similarity(f,v1d,v2d);
            total ++;
        }

        for(String f:p2.getValues().keySet()) {
            Number v1 = p1.getValues().get(f);
            Number v2 = p2.getValues().get(f);
            double v1d = 0;
            double v2d = 0;
            if (v1 instanceof Integer) v1d = (Integer)v1;
            if (v2 instanceof Integer) v2d = (Integer)v2;
            if (v1 instanceof Double) v1d = (Double)v1;
            if (v2 instanceof Double) v2d = (Double)v2;
            sim += similarity(f,v1d,v2d);
            total ++;
        }

        if (total == 0) return 1.0;

        return sim / total;
    }


    double similarity(String feature, double v1, double v2) {
        double dv1 = v1;
        double dv2 = v2;

        updateRange(feature, dv1);
        Pair<Double,Double> range = updateRange(feature, dv2);

        return 1.0 - Math.abs(dv1-dv2)/(range.m_b-range.m_a);
    }


    void updateRange(Example te) {
        for(String feature:te.perception.getValues().keySet()) {
            Object v = te.perception.getValues().get(feature);
            if (v instanceof Integer) updateRange(feature,(Integer)v);
            if (v instanceof Float) updateRange(feature,(Float)v);
            if (v instanceof Double) updateRange(feature,(Double)v);       
        }
    }


    Pair<Double,Double> updateRange(String feature,double value) {
        Pair<Double,Double> range = featureRanges.get(feature);

        if (range==null) {
            range = new Pair<Double,Double>(value,value);
            featureRanges.put(feature, range);
        } else {
            if (value<range.m_a) range.m_a = value;
            if (value>range.m_b) range.m_b = value;
        }
        
        return range;
    }

}

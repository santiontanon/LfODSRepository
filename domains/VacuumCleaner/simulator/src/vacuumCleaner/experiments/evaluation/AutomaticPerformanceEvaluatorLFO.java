/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.experiments.evaluation;

import vacuumCleaner.agents.Agent;
import vacuumCleaner.simulator.LearningTrace;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.perception.FourRayDistancePerception;
import vacuumCleaner.simulator.perception.Perception;
import java.util.LinkedList;
import java.util.List;
import vacuumCleaner.agents.LFOAgent;
import vacuumCleaner.experiments.generatetraces.GenerateDiscreteTraces;
import vacuumCleaner.learning.LFO;
import vacuumCleaner.learning.level2.KNN;
import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.TraceEntry;
import vacuumCleaner.simulator.objects.Dirt;
import vacuumCleaner.simulator.objects.VacuumCleaner;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author santi
 * 
 * This class shows an example of loading a collection of traces, learning an agent
 * (using just k-nearest neighbors), and then evaluating the performance of the agent using some metrics.
 * This file uses 2 simple metrics:
 * - classification accuracy: percentage of actions of the expert correctly predicted
 * - performance in the task: number of pieces of dirt cleaned
 */
public class AutomaticPerformanceEvaluatorLFO {
    public static void main(String []args) throws Exception {
        String experts[] = {
                            "FixedSequenceAgent",
                            "RandomAgent",
                            "SmartRandomAgent", 
                            "WallFollowerAgent",
                            "SmartWallFollowerAgent",
                            "ZigZagAgent", 
                            "StraightLineAgent",
                            "SmartStraightLineAgent", 
                            "PauseEvery3SmartRandomAgent", 
                            "PauseEvery3SmartStraightLineAgent",
                            "RandomExplorerAgent", 
                            "SmartRandomExplorerAgent"
                            };

        
        int REPEATS = 1;
        int mapN = 0;
        List<State> maps = new LinkedList<>();
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-2.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-3.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-4.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-5.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-32x32.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-32x32-2.xml").getRootElement()));
//        Perception perception = new WindowPerception();
        Perception perception = new FourRayDistancePerception();

        List<Agent> agents = new LinkedList<>();

        List<List<Trace>> traces = new LinkedList<>();
        List<List<String>> MatlabTraceNames = new LinkedList<>();
        List<List<LearningTrace>> learningTraces = new LinkedList<>();

        // load all the learning traces:
        {
            for(int i = 0;i<experts.length;i++) {
                List<Trace> tmp = new LinkedList<>();
                List<String> tmp2 = new LinkedList<>();
                for(int j = 0;j<maps.size();j++) {
                    System.out.println("Loading " + i + " " + j);
                    tmp.add(new Trace(new SAXBuilder().build("data/traces-FourRayDistancePerception/trace-m" + j + "-" + experts[i] + ".xml").getRootElement()));
                    tmp2.add("data/traces-FourRayDistancePerception/trace-m" + j + "-" + experts[i] + ".txt");
                }
                MatlabTraceNames.add(tmp2);
                traces.add(tmp);
                List<LearningTrace> tmp_lt = new LinkedList<>();
                for(Trace t:tmp) tmp_lt.add(new LearningTrace(t,perception));
                learningTraces.add(tmp_lt);
            }
        }

        for(int i = 0;i<learningTraces.size();i++)
        {
            mapN = 0;
            double avg_initialDirts = 0;
            double avg_minimumDirts = 0;
            double avg_timeAchieved = 0;
            double avg_predicted = 0;
            for(State map:maps)
            {
                
                LFO lfo = new KNN(1);
                List<LearningTrace> l = new LinkedList<>();
                l.addAll(learningTraces.get(i));
                l.remove(mapN);
                LFOAgent agent = new LFOAgent(lfo, l);
                
                for(int j = 0;j<REPEATS;j++) {
                    double initialDirts;
                    double minimumDirts;
                    double timeAchieved = 0;
                    double predicted = 0;

                    System.out.println("Generating trace from the learning agent...");

                    agent.start();
                    Trace t = GenerateDiscreteTraces.generateTrace(map,agent,1000,perception);
                    agent.end();
//                    TraceVisualizer.newWindow("-", 800, 600, t).show();

                    initialDirts = minimumDirts = t.getEntries().get(0).state.getObjects(Dirt.class).size();
                    for(TraceEntry e:t.getEntries()) {
                        double tmp = e.state.getObjects(Dirt.class).size();
                        if (tmp<minimumDirts) {
                            minimumDirts = tmp;
                            timeAchieved = e.getTime();
                        }
                    }
                    avg_initialDirts += initialDirts;
                    avg_minimumDirts += minimumDirts;
                    avg_timeAchieved += timeAchieved;
                    
                    System.out.print("Evaluating output... ");
                    agent.start();
                    int vacuumID = traces.get(i).get(mapN).getEntries().get(0).state.get(VacuumCleaner.class).getID();
                    LearningTrace targetTrace = learningTraces.get(i).get(mapN);
                    for(int time = 0;time<targetTrace.getEntries().size();time++) {
                        Action al2;
                        Action a1 = agent.cycle(vacuumID, targetTrace.getEntries().get(time).perception, 1.0);
                        al2 = targetTrace.getEntries().get(time).action;
                        if (a1 == null && al2 ==null) {
                            predicted++;
                        } else {
                            if (a1!=null && al2!=null) {
                                if (a1.equals(al2)) predicted++;
                            }
                        }
                    }
                    System.out.println(predicted/targetTrace.getEntries().size());
                    avg_predicted += predicted/targetTrace.getEntries().size();
                }
                mapN++;
            }

            avg_initialDirts /= mapN*REPEATS;
            avg_minimumDirts /= mapN*REPEATS;
            avg_timeAchieved /= mapN*REPEATS;
            avg_predicted /= mapN*REPEATS;

            System.out.println("Agent " + experts[i]);
            System.out.println("Performance evaluation: " + (avg_initialDirts-avg_minimumDirts)/avg_initialDirts + " at " + avg_timeAchieved);
            System.out.println("Output evaluation (prediction accuracy): " + avg_predicted);
        }
    }

}

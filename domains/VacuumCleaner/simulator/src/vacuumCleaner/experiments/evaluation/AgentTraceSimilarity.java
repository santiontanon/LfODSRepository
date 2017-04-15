/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.experiments.evaluation;

import vacuumCleaner.metrics.ChiSquareConditionalLevel3TraceDistance;
import vacuumCleaner.metrics.ChiSquareConditionalLevel2TraceDistance;
import vacuumCleaner.metrics.HMMDistanceGivenTraces;
import java.util.LinkedList;
import java.util.List;
import vacuumCleaner.agents.Agent;
import vacuumCleaner.experiments.generatetraces.GenerateDiscreteTraces;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.perception.FourRayDistancePerception;
import org.jdom.input.SAXBuilder;
import vacuumCleaner.agents.discrete.FixedSequenceAgent;
import vacuumCleaner.agents.discrete.RandomAgent;
import vacuumCleaner.agents.discrete.RandomExplorerAgent;
import vacuumCleaner.agents.discrete.SmartRandomAgent;
import vacuumCleaner.agents.discrete.SmartRandomExplorerAgent;
import vacuumCleaner.agents.discrete.SmartStraightLineAgent;
import vacuumCleaner.agents.discrete.SmartWallFollowerAgent;
import vacuumCleaner.agents.discrete.StraightLineAgent;
import vacuumCleaner.agents.discrete.WallFollowerAgent;
import vacuumCleaner.agents.discrete.ZigZagAgent;

/**
 *
 * @author santi
 * 
 * This file shows an example of how to calculate the similarity between the behavior of several agents
 * using some of the metrics provided in the "metrics" package
 * 
 */
public class AgentTraceSimilarity {
    public static void main(String args[]) throws Exception {
        List<List<Trace>> traces_1 = new LinkedList<>();
        List<List<Trace>> traces_2 = new LinkedList<>();
        List<State> maps = new LinkedList<>();
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-2.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-3.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-4.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-5.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-32x32.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-32x32-2.xml").getRootElement()));
        Agent agents[]={
                        new FixedSequenceAgent(),
                        new RandomAgent(),
                        new SmartRandomAgent(),
                        new WallFollowerAgent(),
                        new SmartWallFollowerAgent(),
                        new StraightLineAgent(),
                        new SmartStraightLineAgent(),
                        new ZigZagAgent(),
                        new RandomExplorerAgent(),
                        new SmartRandomExplorerAgent()
        };
        int NSTATES = 4;
        int NMAPS = maps.size();
        

        for(int agent = 0;agent<agents.length;agent++) {
            List<Trace> traces_agent = new LinkedList<>();
            for(int map = 0;map<NMAPS;map++) {
                System.out.println("Loading trace... " + agent + " - " + map);
                traces_agent.add(new Trace(new SAXBuilder().build("data/traces-FourRayDistancePerception/trace-m" + map + "-" + agents[agent].name() + ".xml").getRootElement()));
            }
            traces_1.add(traces_agent);
        }
        
        System.out.println("Generating alternative traces for comparison purposes...");
        for(int agent = 0;agent<agents.length;agent++) {
            List<Trace> traces_agent = new LinkedList<>();
            for(int map = 0;map<NMAPS;map++) {
                System.out.println("Generating trace... " + agent + " - " + map);
                traces_agent.add(GenerateDiscreteTraces.generateTrace((State)maps.get(map).clone(), agents[agent], 1000, new FourRayDistancePerception()));
//                traces_agent.add(new Trace(new SAXBuilder().build("traces-fourraydistance/trace-m" + map + "-" + agents[agent].name() + ".xml").getRootElement()));
            }
            traces_2.add(traces_agent);
        }

        double matrix1[][] = new double[agents.length][agents.length];
        double matrix3[][] = new double[agents.length][agents.length];
        double matrix4[][] = new double[agents.length][agents.length];
        double matrix5[][] = new double[agents.length][agents.length];
        double matrix6[][] = new double[agents.length][agents.length];
        
        // Compute the statistics directly from all the traces:
        for(int agent1 = 0;agent1<agents.length;agent1++) {
            for(int agent2 = 0;agent2<agents.length;agent2++) {
                List<Trace> traces_agent1 = traces_1.get(agent1);
                List<Trace> traces_agent2 = traces_1.get(agent2);
                if (agent2==agent1) traces_agent2 = traces_2.get(agent2);

                matrix1[agent1][agent2] = HMMDistanceGivenTraces.distance(traces_agent1, traces_agent2, NSTATES, 10, true);
                matrix3[agent1][agent2] = ChiSquareConditionalLevel2TraceDistance.tracesAverageChiSquare(traces_agent1,traces_agent2,new FourRayDistancePerception());
                matrix4[agent1][agent2] = ChiSquareConditionalLevel2TraceDistance.tracesRatioOfStatisticallySignificantDifferentStates(traces_agent1,traces_agent2,new FourRayDistancePerception(), 0.1);
                matrix5[agent1][agent2] = ChiSquareConditionalLevel3TraceDistance.tracesAverageChiSquare(traces_agent1,traces_agent2,new FourRayDistancePerception(), 2);
                matrix6[agent1][agent2] = ChiSquareConditionalLevel3TraceDistance.tracesRatioOfStatisticallySignificantDifferentStates(traces_agent1,traces_agent2,new FourRayDistancePerception(), 0.1, 2);
                
                System.out.println("KL Distance A" + agent1 + " - A" + agent2 + ":" + matrix1[agent1][agent2]);
                System.out.println("Distance A" + agent1 + " - A" + agent2 + ":" + matrix3[agent1][agent2]);
                System.out.println("Statistically significant different states (p = 0.1) A" + agent1 + " - A" + agent2 + ":" + matrix4[agent1][agent2]);
            }
        }
        
        System.out.println("HMM distance between the traces:");
        printMatrix(matrix1);

        System.out.println("CHI-square (level 2, average) distance between the traces:");
        printMatrix(matrix3);

        System.out.println("CHI-square (level 2, raio of statistically significant different states) distance between the traces:");
        printMatrix(matrix4);

        System.out.println("CHI-square (level 3, average) distance between the traces:");
        printMatrix(matrix5);

        System.out.println("CHI-square (level 3, raio of statistically significant different states) distance between the traces:");
        printMatrix(matrix6);
    }
    
    public static void printMatrix(double matrix[][]) {
        for(int agent1 = 0;agent1<matrix.length;agent1++) {
            for(int agent2 = 0;agent2<matrix.length;agent2++) {
                System.out.print(matrix[agent1][agent2] + " , ");
            }
            System.out.println("");
        }
        System.out.println("");
    }    
}

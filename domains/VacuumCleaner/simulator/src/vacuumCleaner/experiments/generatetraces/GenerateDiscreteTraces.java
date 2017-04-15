/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.experiments.generatetraces;

import java.io.File;
import vacuumCleaner.agents.Agent;
import vacuumCleaner.agents.discrete.FixedSequenceAgent;
import vacuumCleaner.agents.discrete.PauseEveryN;
import vacuumCleaner.agents.discrete.RandomAgent;
import vacuumCleaner.agents.discrete.RandomExplorerAgent;
import vacuumCleaner.agents.discrete.SmartRandomAgent;
import vacuumCleaner.agents.discrete.SmartRandomExplorerAgent;
import vacuumCleaner.agents.discrete.SmartStraightLineAgent;
import vacuumCleaner.agents.discrete.StraightLineAgent;
import vacuumCleaner.agents.discrete.ZigZagAgent;
import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.TraceEntry;
import vacuumCleaner.simulator.objects.VacuumCleaner;
import vacuumCleaner.simulator.perception.FourRayDistancePerception;
import vacuumCleaner.simulator.perception.Perception;
import util.XMLWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.List;
import org.jdom.input.SAXBuilder;
import vacuumCleaner.agents.discrete.SmartWallFollowerAgent;
import vacuumCleaner.agents.discrete.WallFollowerAgent;

/**
 *
 * @author santi
 */
public class GenerateDiscreteTraces {
    public static void main(String []args) throws Exception {
        int traceLength = 1000;
        int mapN = 0;
        List<State> maps = new LinkedList<>();
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-2.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-3.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-4.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-8x8-5.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-32x32.xml").getRootElement()));
        maps.add(new State(new SAXBuilder().build("simulator/maps/discrete-32x32-2.xml").getRootElement()));

        for(State map:maps) {
            List<Agent> agents = new LinkedList<>();
            // Level 1:
            agents.add(new FixedSequenceAgent());
            agents.add(new RandomAgent());

            // Level 2:
            agents.add(new SmartRandomAgent());
            agents.add(new WallFollowerAgent());
            agents.add(new SmartWallFollowerAgent());

            // Level 3 
            agents.add(new ZigZagAgent());
            agents.add(new StraightLineAgent());
            agents.add(new SmartStraightLineAgent());

            // Level 3 
            agents.add(new PauseEveryN(3,new SmartRandomAgent()));
            agents.add(new PauseEveryN(3,new SmartStraightLineAgent()));

            // Level 3 
            agents.add(new RandomExplorerAgent());
            agents.add(new SmartRandomExplorerAgent());
            
//            Perception perception = new WindowPerception();
            Perception perception = new FourRayDistancePerception();

            // create folder if needed:
            File dir = new File("data/traces-"+perception.getClass().getSimpleName()); 
            if (!dir.exists()) dir.mkdir(); 
            
            for(Agent agent:agents) {
                String fileName = "data/traces-"+perception.getClass().getSimpleName()+"/trace-m" + mapN + "-" + agent.name();

                // This generates a trace, while saving it to Matlab format:
                Trace t = generateTrace(map,agent,traceLength,perception);
                FileWriter fw = new FileWriter(fileName + ".xml");
                t.toxml(new XMLWriter(fw));
                fw.close();
                
//                JFrame w = TraceVisualizer.newWindow(agent.getClass().getSimpleName(), 800, 600, t);
//                w.setVisible(true);
            }
            mapN++;
        }
    }

    public static Trace generateTrace(State s, Agent agent, int maxCycles, Perception perception) throws Exception {
        State state = (State)s.clone();
        int vacuumID = state.get(VacuumCleaner.class).getID();
        Trace t = new Trace(state,vacuumID);

        boolean first = true;

        for(int time = 0;time<maxCycles;time++) {
            Perception p = perception.perceive(state, state.get(vacuumID));
            Action a = agent.cycle(vacuumID, p, 1);
            if (a!=null) {
                a.setObjectID(vacuumID);
            }
            t.addEntry(new TraceEntry(time, (State)state.clone(), vacuumID, a));
            state.cycle(a, 1.0);
//            System.out.println("cycle " + time);

        }

        return t;
    }
    
}

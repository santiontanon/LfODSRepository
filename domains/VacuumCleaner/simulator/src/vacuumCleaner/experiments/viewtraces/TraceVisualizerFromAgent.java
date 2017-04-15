/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.experiments.viewtraces;

import vacuumCleaner.agents.Agent;
import vacuumCleaner.agents.LFOAgent;
import vacuumCleaner.learning.LFO;
import vacuumCleaner.simulator.LearningTrace;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.perception.FourRayDistancePerception;
import vacuumCleaner.simulator.perception.Perception;
import vacuumCleaner.simulator.gui.TraceVisualizer;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JFrame;
import vacuumCleaner.experiments.generatetraces.GenerateDiscreteTraces;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import vacuumCleaner.agents.discrete.WallFollowerAgent;


/**
 *
 * @author santi
 */
public class TraceVisualizerFromAgent {

    public static void main(String []args) throws Exception {
        Agent a = null;
//        a = new SmartRandomAgent();
//        a = new SmartStraightLineAgent();
        a = new WallFollowerAgent();
//        a = new SmartWallFollowerAgent();
//        Agent a = new SmartRandomExplorerAgent();

        Perception p = new FourRayDistancePerception();
//        State s = new State(new SAXBuilder().build("simulator/maps/discreet-8x8.xml").getRootElement());
//        State s = new State(new SAXBuilder().build("simulator/maps/discreet-8x8-2.xml").getRootElement());
//        State s = new State(new SAXBuilder().build("simulator/maps/discreet-8x8-5.xml").getRootElement());
        State s = new State(new SAXBuilder().build("simulator/maps/discreet-32x32.xml").getRootElement());
//        State s = new State(new SAXBuilder().build("simulator/maps/discreet-32x32-2.xml").getRootElement());
        Trace t = GenerateDiscreteTraces.generateTrace(s, a, 1000, p);
        TraceVisualizer ad = new TraceVisualizer(t, 800, 600, 1);
        JFrame frame = new JFrame("Visualizing Agent " + a.getClass().getSimpleName());
        frame.getContentPane().add(ad);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public static Agent learnAgent(LFO lfo, String expert, int leaveOutMap, Perception p) throws JDOMException, IOException {
        List<LearningTrace> learningTraces = new LinkedList<LearningTrace>();

        // load all the learning traces:
        {
            List<Trace> tmp = new LinkedList<Trace>();
            for(int j = 0;j<7;j++) {
                if (j!=leaveOutMap) {
                    System.out.println("Loading " + j);
                    String fileName = "traces\\trace-m" + j + "-" + expert + ".xml";
                    Trace t = new Trace(new SAXBuilder().build(fileName).getRootElement());
                    learningTraces.add(new LearningTrace(t,p));
                }
            }
        }

        return new LFOAgent(lfo, learningTraces);
    }
}

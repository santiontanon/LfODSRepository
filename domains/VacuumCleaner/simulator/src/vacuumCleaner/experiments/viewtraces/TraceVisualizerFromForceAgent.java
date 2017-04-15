/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.experiments.viewtraces;

import javax.swing.JFrame;
import vacuumCleaner.agents.Agent;
import vacuumCleaner.agents.force.ForceSmartStraightLineAgent;
import vacuumCleaner.experiments.generatetraces.GenerateForceTraces;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.gui.TraceVisualizer;
import vacuumCleaner.simulator.perception.ForceFourRayDistancePerception;
import vacuumCleaner.simulator.perception.Perception;
import org.jdom.input.SAXBuilder;


/**
 *
 * @author santi
 */
public class TraceVisualizerFromForceAgent {

    public static void main(String []args) throws Exception {
        Agent a = null;
//        a = new ForceStraightLineAgent();
        a = new ForceSmartStraightLineAgent();
//        a = new ForceSmarterStraightLineAgent();
//        a = learnForceNNetAgent("ForceStraightLineAgent",10);
//        a = learnForceBNetAgent("ForceStraightLineAgent",10);
//        a = learnForceWekaAgent("ForceStraightLineAgent");
//        a = learnForceWekaAgent("ForceSmartStraightLineAgent");
//        a = learnForceWekaAgent("ForceSmarterStraightLineAgent");
        
        Perception p = new ForceFourRayDistancePerception(0.1);
//        State s = new State(new SAXBuilder().build("simulator/maps/force-8x8-4.xml").getRootElement());
        State s = new State(new SAXBuilder().build("simulator/maps/force-32x32.xml").getRootElement());
//        State s = new State(new SAXBuilder().build("simulator/maps/force-32x32-2.xml").getRootElement());
        Trace t = GenerateForceTraces.generateContinuousTrace(s, a, 5000, 0.1, p, 1);
        TraceVisualizer ad = new TraceVisualizer(t, 800, 600, 1);
        JFrame frame = new JFrame("Visualizing Agent " + a.getClass().getSimpleName());
        frame.getContentPane().add(ad);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

}

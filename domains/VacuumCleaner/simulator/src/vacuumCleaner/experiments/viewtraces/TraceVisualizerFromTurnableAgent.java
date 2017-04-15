/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.experiments.viewtraces;

import vacuumCleaner.agents.Agent;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.perception.Perception;
import vacuumCleaner.simulator.gui.TraceVisualizer;
import javax.swing.JFrame;
import vacuumCleaner.agents.turnable.TurnableSmartStraightLineAgent;
import vacuumCleaner.experiments.generatetraces.GenerateForceTraces;
import vacuumCleaner.simulator.perception.TurnableFourRayDistancePerception;
import org.jdom.input.SAXBuilder;


/**
 *
 * @author santi
 */
public class TraceVisualizerFromTurnableAgent {

    public static void main(String []args) throws Exception {
//        Agent a = new ContinuousStraightLineAgent();
        Agent a = new TurnableSmartStraightLineAgent();
        Perception p = new TurnableFourRayDistancePerception(0.1,0.1);
//        State s = new State(new SAXBuilder().build("simulator/maps/turnable-8x8.xml").getRootElement());
        State s = new State(new SAXBuilder().build("simulator/maps/turnable-8x8-4.xml").getRootElement());
        Trace t = GenerateForceTraces.generateContinuousTrace(s, a, 1000, 0.1, p, 1);
        TraceVisualizer ad = new TraceVisualizer(t, 800, 600, 1);
        JFrame frame = new JFrame("Visualizing Agent " + a.getClass().getSimpleName());
        frame.getContentPane().add(ad);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}

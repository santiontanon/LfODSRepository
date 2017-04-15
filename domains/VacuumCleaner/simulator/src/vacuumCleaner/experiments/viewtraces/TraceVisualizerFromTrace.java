/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.experiments.viewtraces;

import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.gui.TraceVisualizer;
import javax.swing.JFrame;
import org.jdom.input.SAXBuilder;


/**
 *
 * @author santi
 */
public class TraceVisualizerFromTrace {

    public static void main(String []args) throws Exception {

//        Trace t = new Trace(new SAXBuilder().build("data/traces-FourRayDistancePerception/trace-m0-SmartWallFollowerAgent.xml").getRootElement());
        Trace t = new Trace(new SAXBuilder().build("data/traces-TurnableFourRayDistancePerception/trace-m3-TurnableSmartStraightLineAgent.xml").getRootElement());
        TraceVisualizer ad = new TraceVisualizer(t, 800, 600, 1);
        JFrame frame = new JFrame("Visualizing Trace");
        frame.getContentPane().add(ad);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }
}

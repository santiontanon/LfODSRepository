/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator.gui;

import vacuumCleaner.simulator.Map;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.objects.Dirt;
import vacuumCleaner.simulator.objects.Obstacle;
import vacuumCleaner.simulator.objects.PhysicalObject;
import vacuumCleaner.simulator.objects.VacuumCleaner;
import vacuumCleaner.simulator.objects.VacuumCleanerTurnable;
import vacuumCleaner.simulator.shapes.Circle;
import vacuumCleaner.simulator.shapes.Shape;
import vacuumCleaner.simulator.shapes.Square;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author santi
 */
public class TrajectoryStatePanel extends StatePanel {

    List<Point2D.Double> m_trajectory;  // The trajectory to be drawn
    int m_first;    // first element of the trajectory to display
    int m_last;     // last element to display
    
    public TrajectoryStatePanel(State s, List<Point2D.Double> trajectory, int first, int last) {
        super(s);
        m_trajectory = trajectory;
        m_first = first;
        m_last = last;
    }
    
    public void setLast(int last) {
        m_last = last;
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D)g;
        
        for(int i = m_first;i<m_last;i++) {
            g.setColor(Color.BLUE);
            for(double x = 0;x<=dx;x++) {
                Point2D.Double p1 = m_trajectory.get(i);
                Point2D.Double p2 = m_trajectory.get(i+1);
                g.drawLine(toScreenX(p1.x), toScreenY(p1.y), toScreenX(p2.x), toScreenY(p2.y));
            }            
        }
    }
}

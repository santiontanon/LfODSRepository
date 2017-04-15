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
import javax.swing.JPanel;
import vacuumCleaner.simulator.objects.VacuumCleanerForce;

/**
 *
 * @author santi
 */
public class StatePanel extends JPanel {
    State state;
    double dx,dy;

    public StatePanel(State s) {
        setState(s);
    }

    public void setState(State s) {
        state = s;
        Map m = state.getMap();
        dx = m.getDx();
        dy = m.getDy();
    }

    public int toScreenX(double x) {
        Rectangle r = getBounds();
        if (dx/r.width>dy/r.height) {
            return (int)((x/dx)*r.width);
        } else {
            return (int)((x/dy)*r.height);
        }

//        return (int)((x/Math.max(dx,dy))*Math.min(r.width,r.height));
    }

    public int toScreenY(double y) {
        Rectangle r = getBounds();
        if (dx/r.width>dy/r.height) {
            return (int)((y/dx)*r.width);
        } else {
            return (int)((y/dy)*r.height);
        }

//        return (int)((x/Math.max(dx,dy))*Math.min(r.width,r.height));
    }

    public void paint(Graphics g) {
		super.paint(g);
        Graphics2D g2 = (Graphics2D)g;

        // draw map:
        g.setColor(Color.LIGHT_GRAY);
        for(double x = 0;x<=dx;x++) {
            g.drawLine(toScreenX(x), toScreenY(0), toScreenX(x), toScreenY(dy));
        }
        for(double y = 0;y<=dy;y++) {
            g.drawLine(toScreenX(0), toScreenY(y), toScreenX(dx), toScreenY(y));
        }

        // draw objects:
        for(PhysicalObject o:state.getObjects()) {
            Point2D.Double pos = o.getPosition();
            Shape s = o.getShape();

            AffineTransform at = g2.getTransform();

            if (o instanceof Obstacle) {
                g.setColor(Color.LIGHT_GRAY);
            } else if (o instanceof Dirt) {
                g.setColor(Color.RED);
            } else if (o instanceof VacuumCleaner) {
                g.setColor(Color.GREEN);
            } else if (o instanceof VacuumCleanerTurnable) {
                g.setColor(Color.GREEN);
            } else if (o instanceof VacuumCleanerForce) {
                g.setColor(Color.GREEN);
            } else {
                g.setColor(Color.BLACK);
            }

            g2.translate(toScreenX(pos.x), toScreenY(pos.y));
            g2.rotate(o.getAngle()*Math.PI/180.0);

            if (s instanceof Square) {
                Square ss = (Square)s;
                g.fillRect(toScreenX(-ss.getDx()/2), toScreenY(-ss.getDy()/2), toScreenX(ss.getDx()), toScreenY(ss.getDy()));
                g.setColor(Color.BLACK);
                g.drawRect(toScreenX(-ss.getDx()/2), toScreenY(-ss.getDy()/2), toScreenX(ss.getDx()), toScreenY(ss.getDy()));
            } else if (s instanceof Circle) {
                Circle cs = (Circle)s;
                g.fillOval(toScreenX(-cs.getRadius()), toScreenY(-cs.getRadius()), toScreenX(2*cs.getRadius()), toScreenY(2*cs.getRadius()));
                g.setColor(Color.BLACK);
                g.drawOval(toScreenX(-cs.getRadius()), toScreenY(-cs.getRadius()), toScreenX(2*cs.getRadius()), toScreenY(2*cs.getRadius()));
            } else {
               System.err.println("Drawing " + s.getClass().getName() + " shape still not implemented!");
            }

            // draw a pointing arrow towards the facing direction:
            {
                g.setColor(Color.BLACK);
                g.drawLine(0,0,0,toScreenY(-0.3));
                g.drawLine(0,toScreenY(-0.5),toScreenX(-0.125),toScreenY(-0.3));
                g.drawLine(0,toScreenY(-0.5),toScreenX(0.125),toScreenY(-0.3));
                g.drawLine(toScreenX(-0.125),toScreenY(-0.3),toScreenX(0.125),toScreenY(-0.3));
            }

            g2.setTransform(at);
        }
        
    }
}

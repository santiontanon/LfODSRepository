/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator.shapes;

import vacuumCleaner.simulator.objects.BoundingBox;
import util.XMLWriter;
import java.awt.geom.Point2D;
import org.jdom.Element;

/**
 *
 * @author santi
 */
public class Square extends Shape {
    double dx,dy;

    public Square(double a_dx, double a_dy) {
        dx = a_dx;
        dy = a_dy;
    }

    public Square(Element e) {
        dx = Double.parseDouble(e.getAttributeValue("dx"));
        dy = Double.parseDouble(e.getAttributeValue("dy"));

    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public boolean collision(Point2D.Double p, double a, Shape s2, Point2D.Double p2, double a2) throws Exception {
        if (s2 instanceof Square) {
            Square ss2 = (Square)s2;
            if (a!=0 || a2!=0) throw new Exception("Collision between Square and Square with angle different than 0 is not yet implemented!");
            if (p.x+dx/2 <= p2.x-ss2.dx/2) return false;
            if (p2.x+ss2.dx/2 <= p.x-dx/2) return false;
            if (p.y+dy/2 <= p2.y-ss2.dy/2) return false;
            if (p2.y+ss2.dy/2 <= p.y-dy/2) return false;
            return true;
        } else if (s2 instanceof Circle) {
            if (a!=0) throw new Exception("Collision between Square and Circle with angle different than 0 is not yet implemented!");

            // Find the closest point to the circle within the rectangle
            double closestX = Math.min(Math.max(p2.x, p.x-dx/2), p.x+dx/2);
            double closestY = Math.min(Math.max(p2.y, p.y-dy/2), p.y+dy/2);

            // Calculate the distance between the circle's center and this closest point
            double distanceX = p2.x - closestX;
            double distanceY = p2.y - closestY;

            // If the distance is less than the circle's radius, an intersection occurs
            double distanceSquared = (distanceX * distanceX) + (distanceY * distanceY);
            return distanceSquared < (((Circle)s2).getRadius() * ((Circle)s2).getRadius());
        } else {
            throw new Exception("Collision between Square and " + s2.getClass().getName() + " not yet implemented!");
        }
    }

    public void toxml(XMLWriter w) {
        w.tagWithAttributes("square","dx = \"" + dx + "\" dy = \"" + dy + "\"");
        w.tag("/square");
    }

    public BoundingBox getBoundingBox() {
        Square s = new Square(dx,dy);
        return new BoundingBox(s,0,0);
    }

}

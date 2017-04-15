/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator;

import vacuumCleaner.simulator.objects.BoundingBox;
import vacuumCleaner.simulator.shapes.Shape;
import vacuumCleaner.simulator.objects.PhysicalObject;
import vacuumCleaner.simulator.shapes.Circle;
import vacuumCleaner.simulator.shapes.Square;
import util.XMLWriter;
import java.awt.geom.Point2D;
import org.jdom.Element;

/**
 *
 * @author santi
 */
public  class Map implements Cloneable {

    double dx,dy;

    public Map(Element e) {
        dx = Double.parseDouble(e.getAttributeValue("dx"));
        dy = Double.parseDouble(e.getAttributeValue("dy"));
    }

    public Map(double dx_a, double dy_a) {
        dx = dx_a;
        dy = dy_a;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public Object clone() {
        return new Map(dx,dy);
    }

    public void cycle() {
        
    }

    public void toxml(XMLWriter w) {
        w.tagWithAttributes("map", "dx = \"" + dx + "\" dy = \"" + dy + "\"");
        w.tag("/map");
    }

    public boolean collision(PhysicalObject o) throws Exception {
        return collision(o,o.getPosition(),o.getAngle());
    }

    public boolean collision(PhysicalObject o, Point2D p, double angle) throws Exception {
        return collision(o.getShape(), p, angle);
    }

    public boolean collision(Shape s, Point2D p, double angle) throws Exception {
        if (s instanceof Square) {
            if (angle!=0) throw new Exception("Collisions with the map with angle != 0 are not yet implemented!");
            BoundingBox bb = s.getBoundingBox();
            Square s2 = (Square) bb.getShape();

            double minx = p.getX()+bb.getPosition().getX()-s2.getDx()/2;
            double maxx = p.getX()+bb.getPosition().getX()+s2.getDx()/2;
            double miny = p.getY()+bb.getPosition().getY()-s2.getDy()/2;
            double maxy = p.getY()+bb.getPosition().getY()+s2.getDy()/2;

            if (minx<0 || miny<0 || maxx>dx || maxy>dy) {
                return true;
            }
        } else if (s instanceof Circle) {
            Circle c = (Circle)s;
            if (p.getX()-c.getRadius()<0 ||
                p.getX()+c.getRadius()>dx ||
                p.getY()-c.getRadius()<0 ||
                p.getY()+c.getRadius()>dy) return true;
            return false;
        } else {
            throw new Exception("Collisions with the map with shape " + s.getClass().getName() + " are not yet implemented!");
        }

        return false;
    }

}

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
public class Circle extends Shape {
    double radius;

    public Circle(double r) {
        radius = r;
    }

    public Circle(Element e) {
        radius = Double.parseDouble(e.getAttributeValue("radius"));
    }

    public boolean collision(Point2D.Double p, double a, Shape s2, Point2D.Double p2, double a2) throws Exception {
        if (s2 instanceof Circle) {
            if (p.distance(p2)<radius+((Circle)s2).radius) return true;
            return false;
        } if (s2 instanceof Square) {
            return s2.collision(p2, a2, this, p, a);
        } else {
            throw new Exception("Collision between Circle and " + s2.getClass().getName() + " not yet implemented!");
        }
    }

    public void toxml(XMLWriter w) {
        w.tagWithAttributes("circle","radius = \"" + radius + "\"");
        w.tag("/circle");
    }

    public BoundingBox getBoundingBox() {
        Square s = new Square(radius*2,radius*2);
        return new BoundingBox(s,0,0);
    }

    public double getRadius() {
        return radius;
    }

    
}

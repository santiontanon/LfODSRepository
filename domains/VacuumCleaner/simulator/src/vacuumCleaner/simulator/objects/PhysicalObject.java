/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator.objects;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.shapes.Shape;
import vacuumCleaner.simulator.State;
import util.XMLWriter;
import java.awt.geom.Point2D;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author santi
 */
public abstract class PhysicalObject implements Cloneable {

    static int ID_counter = 1;

    public int ID;
    public Shape shape;
    public Point2D.Double position = new Point2D.Double();
    public Double angle;

    public PhysicalObject() {
        ID = ID_counter++;        
    }

    public PhysicalObject(Shape b, double x, double y, double a) {
        shape = b;
        position = new Point2D.Double(x,y);
        angle = a;
        ID = ID_counter++;
    }

    public PhysicalObject(Shape b, Point2D.Double p, double a) {
        shape = b;
        position = p;
        angle = a;
        ID = ID_counter++;
    }

    public Point2D.Double getPosition() {
        return position;
    }

    public double getAngle() {
        return angle;
    }

    public Shape getShape() {
        return shape;
    }

    public void setID(int aID) {
        ID = aID;
    }

    public int getID() {
        return ID;
    }

    public boolean collision(PhysicalObject o) throws Exception {
        return shape.collision(position, angle, o.shape, o.position, o.angle);
    }

    public boolean collision(Point2D.Double p, double a, PhysicalObject o, Point2D.Double p2, double a2) throws Exception {
        return shape.collision(p, a, o.shape, p2, a2);
    }

    public void cycle(List<Action> actions, State s, double time) throws Exception {

    }

    public List<Action> executableActions() {
        return new LinkedList<>();
    }

    public void moveTo(double x, double y, State s) throws Exception {
        if (!s.collision(this,new Point2D.Double(x,y), angle)) {
            position.x = x;
            position.y = y;
        }
    }

    // this function only returns the distance among centers:
    public double distance(PhysicalObject pe) {
        return position.distance(pe.position);
    }

    public abstract void toxml(XMLWriter w);
    
    @Override
    public abstract Object clone();

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        XMLWriter xmlw = new XMLWriter(sw);
        toxml(xmlw);
        return sw.toString();
    }

}

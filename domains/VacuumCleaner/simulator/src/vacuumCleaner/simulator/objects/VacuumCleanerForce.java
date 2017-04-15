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
import java.util.LinkedList;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author santi
 */
public class VacuumCleanerForce extends PhysicalObject {

    double speedWheel1 = 0;
    double speedWheel2 = 0;
    
    public VacuumCleanerForce(Shape s, double x, double y, double a, double sw1, double sw2) {
        position.x = x;
        position.y = y;
        angle = a;
        shape = s;
        speedWheel1 = sw1;
        speedWheel2 = sw2;
    }

    public VacuumCleanerForce(Element e) {
        position.x = Double.parseDouble(e.getChildText("x"));
        position.y = Double.parseDouble(e.getChildText("y"));
        angle = Double.parseDouble(e.getChildText("angle"));
        speedWheel1 = Double.parseDouble(e.getChildText("speedWheel1"));
        speedWheel2 = Double.parseDouble(e.getChildText("speedWheel2"));
        shape = Shape.fromxml(e.getChild("shape"));
        ID = Integer.parseInt(e.getAttributeValue("id"));
    }
    
    public double getSpeedWheel1() {
        return speedWheel1;
    }

    public double getSpeedWheel2() {
        return speedWheel2;
    }

    public List<Action> executableActions() {
        List<Action> l = new LinkedList<Action>();
        l.add(new Action("move",1));
        return l;
    }

    public void toxml(XMLWriter w) {
        w.tagWithAttributes("vacuumforce", "id = \"" + ID + "\"");
        w.tag("x",position.x);
        w.tag("y",position.y);
        w.tag("angle",angle);
        w.tag("speedWheel1",speedWheel1);
        w.tag("speedWheel2",speedWheel2);
        w.tag("shape");
        shape.toxml(w);
        w.tag("/shape");
        w.tag("/vacuumforce");
    }

    public void cycle(List<Action> actions, State s, double time) throws Exception {
        for(Action a:actions) {
            if (a.getObjectID()==ID) {
                // execute action:
                if (a.getName().equals("move")) {
                    double wheel1 = (Double)a.getParameter("wheel1");
                    double wheel2 = (Double)a.getParameter("wheel2");
                    
                    // over time, wheels lose speed:
                    double slowdownFactor = Math.pow(2, -time);
                    speedWheel1*=slowdownFactor;
                    speedWheel2*=slowdownFactor;
                    
                    speedWheel1 += time*wheel1;
                    speedWheel2 += time*wheel2;
                    
                    if (speedWheel1<-1) speedWheel1=-1;
                    if (speedWheel2<-1) speedWheel2=-1;
                    if (speedWheel1>1) speedWheel1=1;
                    if (speedWheel2>1) speedWheel2=1;
                    
                    double angular = speedWheel1-speedWheel2;
                    double average = (speedWheel1 + speedWheel2)/2;
                    
                    angle += time*angular*45;   // At maximum turning speed it turns 90 degrees per unit of time
                    moveTo(position.x - (Math.sin((-angle/180)*Math.PI)*average*time),
                           position.y - (Math.cos((-angle/180)*Math.PI)*average*time), s);                    
                }
            }
        }
    }

    public void moveTo(double x, double y, State s) throws Exception {
        if (!s.collision(this,new Point2D.Double(x,y), angle)) {
            position.x = x;
            position.y = y;
        } else {
            PhysicalObject c = s.collisionWithObjects(this,new Point2D.Double(x,y), angle);
            if (c!=null && c instanceof Dirt) {
                // Collect dirt:
                s.removeObject(c);
                moveTo(x,y,s);
            }
        }
    }

    public Object clone() {
        return new VacuumCleanerForce(shape,position.x,position.y, angle, speedWheel1, speedWheel2);
    }

    

}

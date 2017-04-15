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
public class VacuumCleanerTurnable extends PhysicalObject {

    public VacuumCleanerTurnable(Shape s, double x, double y, double a) {
        position.x = x;
        position.y = y;
        angle = a;
        shape = s;
    }

    public VacuumCleanerTurnable(Element e) {
        position.x = Double.parseDouble(e.getChildText("x"));
        position.y = Double.parseDouble(e.getChildText("y"));
        angle = Double.parseDouble(e.getChildText("angle"));
        shape = Shape.fromxml(e.getChild("shape"));
        ID = Integer.parseInt(e.getAttributeValue("id"));
    }


    @Override
    public List<Action> executableActions() {
        List<Action> l = new LinkedList<>();
        l.add(new Action("forward",1));
        l.add(new Action("left",1));
        l.add(new Action("right",1));
        return l;
    }

    @Override
    public void toxml(XMLWriter w) {
        w.tagWithAttributes("vacuumturnable", "id = \"" + ID + "\"");
        w.tag("x",position.x);
        w.tag("y",position.y);
        w.tag("angle",angle);
        w.tag("shape");
        shape.toxml(w);
        w.tag("/shape");
        w.tag("/vacuumturnable");
    }

    @Override
    public void cycle(List<Action> actions, State s, double time) throws Exception {
        for(Action a:actions) {
            if (a.getObjectID()==ID) {
                // execute action:
                if (a.getName().equals("right")) {
                    angle+=time*90;
                } else if (a.getName().equals("left")) {
                    angle-=time*90;
                } else if (a.getName().equals("forward")) {
                    moveTo(position.x - (Math.sin((-angle/180)*Math.PI)*time),
                           position.y - (Math.cos((-angle/180)*Math.PI)*time), s);
//                } else if (a.getName().equals("backwards")) {
//                    moveTo(position.x + (Math.sin((-angle/180)*Math.PI)*time),
//                           position.y + (Math.cos((-angle/180)*Math.PI)*time), s);
                }
            }
        }
    }

    @Override
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

    @Override
    public Object clone() {
        return new VacuumCleanerTurnable(shape,position.x,position.y, angle);
    }

    

}

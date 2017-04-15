/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator.objects;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.shapes.Shape;
import vacuumCleaner.simulator.shapes.Square;
import util.XMLWriter;
import java.util.List;
import org.jdom.Element;

/**
 *
 * @author santi
 */
public class BoundingBox extends PhysicalObject {

    public BoundingBox(Square s, double x, double y)
    {
        position.x = x;
        position.y = y;
        angle = 0.0;
        shape = s;
    }

    public BoundingBox(Element e) {
        position.x = Double.parseDouble(e.getChildText("x"));
        position.y = Double.parseDouble(e.getChildText("y"));
        angle = 0.0;
        shape = Shape.fromxml(e.getChild("shape"));
        ID = Integer.parseInt(e.getAttributeValue("id"));
    }

    @Override
    public void toxml(XMLWriter w) {
        w.tagWithAttributes("nn", "id = \"" + ID + "\"");
        w.tag("x",position.x);
        w.tag("y",position.y);
        w.tag("shape");
        shape.toxml(w);
        w.tag("/shape");
        w.tag("/nn");
    }

    @Override
    public Object clone() {
        return new BoundingBox((Square)shape,position.x,position.y);
    }

    @Override
    public void cycle(List<Action> actions, State s, double time) throws Exception {
    }
}

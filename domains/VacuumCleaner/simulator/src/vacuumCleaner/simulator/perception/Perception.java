/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator.perception;

import java.io.StringWriter;
import java.util.*;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.objects.PhysicalObject;
import org.jdom.Element;
import util.XMLWriter;

/**
 *
 * @author santi
 */
public class Perception {
    protected LinkedHashMap<String,Number> values;

    public Perception() {
        values = new LinkedHashMap<>();
    }

    public Perception(Perception p) {
        values = new LinkedHashMap<>();
        values.putAll(p.values);
    }

    public LinkedHashMap<String,Number> getValues() {
        return values;
    }

    public Number get(String feature) {
        return values.get(feature);
    }

    public Integer getInteger(String feature) {
        return (Integer)values.get(feature);
    }

    public Double getDouble(String feature) {
        return (Double)values.get(feature);
    }
    
    public List<Double> getAsArrayOfDoubles() {
        List<Double> tmp = new ArrayList<>();
        for(String feature:getValues().keySet()) {
            tmp.add((double)values.get(feature));
        }
        return tmp;
    }

    public Perception(Element e) {
        values = new LinkedHashMap<>();
        for(Object o:e.getChildren()) {
            Element e2 = (Element)o;
            values.put(e2.getName(),Integer.parseInt(e2.getText()));
        }
    }

    public void toxml(XMLWriter w) {
       w.tag("perception");
       for(String pname:values.keySet()) {
           if (values.get(pname)==null) {
               w.rawXML("<" + pname + "></" + pname + ">");
           } else {
               w.rawXML("<" + pname + ">" + values.get(pname) + "</" + pname + ">");
           }
       }
       w.tag("/perception");
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        XMLWriter xmlw = new XMLWriter(sw);
        toxml(xmlw);
        return sw.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Perception) {
            Perception p = (Perception)o;
            if (p.values.keySet().size() != values.keySet().size()) return false;
            for(String k:p.values.keySet()) {
                Number n1 = values.get(k);
                Number n2 = p.values.get(k);
                if (!n1.equals(n2)) return false;
            }
        } 
        return true;
    }

    @Override
    public int hashCode() {
        return values.hashCode();
    }
        
    public Perception perceive(State s, PhysicalObject subject) {
        return new Perception();
    }
}

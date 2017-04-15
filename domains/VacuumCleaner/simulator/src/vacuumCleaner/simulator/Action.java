/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator;

import util.XMLWriter;
import java.io.StringWriter;
import java.util.HashMap;
import org.jdom.Element;

/**
 *
 * @author santi
 */
public class Action implements Cloneable  {
    String name;
    int objectID;
    HashMap<String,Number> parameters;

    public Action(String a_name, int id) {
        name = a_name;
        objectID = id;
        parameters = new HashMap<>();
    }

    public Action(String a_name, int id, String p1, Number v1) {
        name = a_name;
        objectID = id;
        parameters = new HashMap<>();
        parameters.put(p1, v1);
    }

    public Action(String a_name, int id, String p1, Number v1, String p2, Number v2) {
        name = a_name;
        objectID = id;
        parameters = new HashMap<>();
        parameters.put(p1, v1);
        parameters.put(p2, v2);
    }

    public Action(Element e) {
        name = e.getAttributeValue("name");
        objectID = Integer.parseInt(e.getAttributeValue("target"));
        parameters = new HashMap<>();
        for(Object o:e.getChildren()) {
            Element e2 = (Element)o;
            String type = e2.getAttributeValue("type");
            if (type.equals("Integer")) {
                parameters.put(e2.getName(),Integer.parseInt(e2.getText()));
            } else if (type.equals("Double")) {
                parameters.put(e2.getName(),Double.parseDouble(e2.getText()));
            }
        }
    }
    
    
    public void addParameter(String name, Number value) {
        parameters.put(name,value);
    }

    @Override
    public Object clone() {
        Action a = new Action(name, objectID);
        a.parameters.putAll(parameters);
        return a;
    }

    public int getObjectID() {
        return objectID;
    }

    public void setObjectID(int ID) {
        objectID = ID;
    }

    public String getName() {
        return name;
    }
    
    public Number getParameter(String name) {
        return parameters.get(name);
    }

    public Double getParameterDouble(String name) {
        return (Double)parameters.get(name);
    }

    public Integer getParameterInteger(String name) {
        return (Integer)parameters.get(name);
    }

    @Override
    public boolean equals(Object o) {
        if (o==null || !(o instanceof Action)) return false;
        Action a = (Action)o;
        if (!name.equals(a.name)) return false;
//        if (objectID!=a.objectID) return false;
        for(String p:parameters.keySet()) {
            if (!parameters.get(p).equals(a.parameters.get(p))) return false;
        }
        return true;
    }
    
    public HashMap<String,Number> getAttributes() {
        return parameters;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void toxml(XMLWriter w) {
       w.tagWithAttributes("action","name = \"" + name + "\" target = \"" + objectID + "\"");
       for(String pname:parameters.keySet()) {
           w.rawXML("<" + pname + " type = \""  + parameters.get(pname).getClass().getSimpleName() + "\">" + parameters.get(pname) + "</" + pname + ">");
       }
       w.tag("/action");
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        XMLWriter xmlw = new XMLWriter(sw);
        toxml(xmlw);
        return sw.toString();
    }

    public String toSimpleString() {
        String tmp = name + "(";
        for(String pname:parameters.keySet()) {
            tmp += parameters.get(pname) + " ";
        }
        return tmp + ")";
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.agents.discrete;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.perception.Perception;
import vacuumCleaner.simulator.perception.WindowPerception;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import vacuumCleaner.agents.Agent;

/**
 *
 * @author santi
 *
 * This agent is equivalent to the random explorer, but when it sees a piece of dirt
 * in a straight line, it goes for it!
 */

// LEVEL 3 agent with unbounded internal state (LFOHMM, not IOHMM)
public class SmartRandomExplorerAgent extends Agent {
    public static double tolerance = 0.01;
    Random r = new Random();
    Point2D.Double pos = new Point2D.Double(0,0);
    List<Point2D.Double> closed = new LinkedList<Point2D.Double>();
    List<Point2D.Double> open = new LinkedList<Point2D.Double>();

    public Action cycle(int id,Perception p, double timeStep) {
        // Are there walls around?
        boolean up = false;
        boolean down = false;
        boolean left = false;
        boolean right = false;
        boolean upDirt = false;
        boolean downDirt = false;
        boolean leftDirt = false;
        boolean rightDirt = false;

        if (p instanceof WindowPerception) {
            if (p.getInteger("u") == 0) up = true;
            if (p.getInteger("d") == 0) down = true;
            if (p.getInteger("l") == 0) left = true;
            if (p.getInteger("r") == 0) right = true;
            if (p.getInteger("u") == 1) upDirt = true;
            if (p.getInteger("d") == 1) downDirt = true;
            if (p.getInteger("l") == 1) leftDirt = true;
            if (p.getInteger("r") == 1) rightDirt = true;
        } else {
            if (p.getInteger("du")==0 && p.getInteger("u")==0) up = true;
            if (p.getInteger("dd")==0 && p.getInteger("d")==0) down = true;
            if (p.getInteger("dl")==0 && p.getInteger("l")==0) left = true;
            if (p.getInteger("dr")==0 && p.getInteger("r")==0) right = true;
            if (p.getInteger("u") == 1) upDirt = true;
            if (p.getInteger("d") == 1) downDirt = true;
            if (p.getInteger("l") == 1) leftDirt = true;
            if (p.getInteger("r") == 1) rightDirt = true;
        }
        
        // update visited/open positions:
        open.remove(pos);
        if (!find(pos,closed)) closed.add(new Point2D.Double(pos.x,pos.y));
        if (!up) {
            Point2D.Double newPos = new Point2D.Double(pos.x,pos.y-1);
            if (!find(newPos,closed) && !find(newPos,open)) open.add(newPos);
        }
        if (!down) {
            Point2D.Double newPos = new Point2D.Double(pos.x,pos.y+1);
            if (!find(newPos,closed) && !find(newPos,open)) open.add(newPos);
        }
        if (!left) {
            Point2D.Double newPos = new Point2D.Double(pos.x-1,pos.y);
            if (!find(newPos,closed) && !find(newPos,open)) open.add(newPos);
        }
        if (!right) {
            Point2D.Double newPos = new Point2D.Double(pos.x+1,pos.y);
            if (!find(newPos,closed) && !find(newPos,open)) open.add(newPos);
        }

        if (upDirt) {
            pos = new Point2D.Double(pos.x,pos.y-1);
            return new Action("up",id);
        }
        if (downDirt) {
            pos = new Point2D.Double(pos.x,pos.y+1);
            return new Action("down",id);
        }
        if (leftDirt) {
            pos = new Point2D.Double(pos.x-1,pos.y);
            return new Action("left",id);
        }
        if (rightDirt) {
            pos = new Point2D.Double(pos.x+1,pos.y);
            return new Action("right",id);
        }

        // find the closest open position:
        Point2D.Double closest = null;
        double distance = 0;
        for(Point2D.Double pos2:open) {
            double d = pos.distanceSq(pos2);
            if (closest==null || d<distance) {
                closest = pos2;
                distance = d;
            }
        }

        // Move towards that point (move randmoly, but favoring moves towards the closest):
        if (closest!=null) {
            List<Action> candidates = new LinkedList<Action>();
            List<Double> distances = new LinkedList<Double>();
            List<Point2D.Double> newPositions = new LinkedList<Point2D.Double>();
            if (!up) {
                candidates.add(new Action("up",id));
                distances.add(new Point2D.Double(pos.x,pos.y-1).distance(closest));
                newPositions.add(new Point2D.Double(pos.x,pos.y-1));
            }
            if (!down) {
                candidates.add(new Action("down",id));
                distances.add(new Point2D.Double(pos.x,pos.y+1).distance(closest));
                newPositions.add(new Point2D.Double(pos.x,pos.y+1));
            }
            if (!left) {
                candidates.add(new Action("left",id));
                distances.add(new Point2D.Double(pos.x-1,pos.y).distance(closest));
                newPositions.add(new Point2D.Double(pos.x-1,pos.y));
            }
            if (!right) {
                candidates.add(new Action("right",id));
                distances.add(new Point2D.Double(pos.x+1,pos.y).distance(closest));
                newPositions.add(new Point2D.Double(pos.x+1,pos.y));
            }

            // sort the actions by how much do they advance to the goal:
            {
                boolean change = false;
                do{
                    change = false;
                    for(int i = 0;i<candidates.size()-1;i++) {
                        if (distances.get(i)>distances.get(i+1)) {
                            Action tmpa = candidates.get(i);
                            Double tmpd = distances.get(i);
                            Point2D.Double tmpp = newPositions.get(i);
                            candidates.set(i, candidates.get(i+1));
                            distances.set(i, distances.get(i+1));
                            newPositions.set(i, newPositions.get(i+1));
                            candidates.set(i+1, tmpa);
                            distances.set(i+1, tmpd);
                            newPositions.set(i+1, tmpp);
                            change = true;
                        }
                    }
                }while(change);
            }

//            for(int i = 0;i<candidates.size();i++) {
//                System.out.print(candidates.get(i).getName() + " [" + distances.get(i) + "] - ");
//            }
//            System.out.println("");

            // choose an action biased towards the first actions:
            if (candidates.size()>0) {
                int selected = 0;
                while(selected<candidates.size()-1 && r.nextInt(4)==0) selected++;
                pos = newPositions.get(selected);
                return candidates.get(selected);
            }
        }

        return null;
    }

    boolean find(Point2D.Double p, List<Point2D.Double> l) {
        for(Point2D.Double p2:l) {
            if (p.distanceSq(p2)<tolerance) return true;
        }
        return false;
    }

    void remove(Point2D.Double p, List<Point2D.Double> l) {
        List<Point2D.Double> toDelete = new LinkedList<Point2D.Double>();
        for(Point2D.Double p2:l) {
            if (p.distanceSq(p2)<tolerance) {
                toDelete.add(p2);
            }
        }
        l.removeAll(toDelete);
    }

}

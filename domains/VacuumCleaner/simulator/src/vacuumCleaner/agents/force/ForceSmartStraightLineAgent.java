/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.agents.force;

import vacuumCleaner.simulator.Action;
import vacuumCleaner.simulator.perception.Perception;
import vacuumCleaner.simulator.perception.WindowPerception;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import vacuumCleaner.agents.Agent;
import vacuumCleaner.simulator.perception.TurnableFourRayDistancePerception;

/**
 *
 * @author santi
 */

// LEVEL 3 agent with bounded internal state (IOHMM)
public class ForceSmartStraightLineAgent extends Agent {
    Random r = new Random();
    double maxForce = 5.0;
    int turnState = 0;  // last direction of turn (1 : left, 2: right)
    int dirtState = 0;  // 0 : standard, 1: dirt seen on the left or back, 2: dirt seen on the right
    
    public Action cycle(int id,Perception p, double timeStep) {
        boolean canGoForward = true;
        boolean canGoLeft = true;
        boolean canGoRight = true;
        boolean dirtForward = false;
        boolean dirtLeft = false;
        boolean dirtRight = false;
        
        double targetSpeed1 = 0;
        double targetSpeed2 = 0;

        if (p.getDouble("du")<timeStep*5 && p.getInteger("u")==0) canGoForward = false;
        if (p.getDouble("dl")<timeStep*5 && p.getInteger("l")==0) canGoLeft = false;
        if (p.getDouble("dr")<timeStep*5 && p.getInteger("r")==0) canGoRight = false;
        if (p.getInteger("u")==1) dirtForward = true;
        if (p.getInteger("l")==1) dirtLeft = true;
        if (p.getInteger("d")==1) dirtLeft = true;  // if we see it behind, assume we see it in the left
        if (p.getInteger("r")==1) dirtRight = true;
        
        switch(dirtState) {
            case 0:
                if (dirtForward) {
                    targetSpeed1 = 1; targetSpeed2 = 1;
                } else {
                    if (dirtLeft) {
                        dirtState = 1;
                        turnState = 1;
                        targetSpeed1 = -1; targetSpeed2 = 1;
                    } else if (dirtRight) {
                        dirtState = 2;
                        turnState = 2;
                        targetSpeed1 = 1; targetSpeed2 = -1;
                    } else if (canGoForward) {
                        targetSpeed1 = 1; targetSpeed2 = 1;
                    } else {
                        if (canGoLeft) {
                            turnState = 1;
                            targetSpeed1 = -1; targetSpeed2 = 1;
                        } else if (canGoRight) {
                            turnState = 2;
                            targetSpeed1 = 1; targetSpeed2 = -1;
                        } else {
                            if (turnState==1) {
                                targetSpeed1 = -1; targetSpeed2 = 1;
                            } else {
                                targetSpeed1 = 1; targetSpeed2 = -1;
                            }
                        }
                    }
                }
                break;
            case 1:
                if (dirtForward) {
                    dirtState = 0;
                    targetSpeed1 = 1; targetSpeed2 = 1;
                }
                turnState = 1;
                targetSpeed1 = -1; targetSpeed2 = 1;
                break;
            case 2:
                if (dirtForward) {
                    dirtState = 0;
                    targetSpeed1 = 1; targetSpeed2 = 1;
                }
                turnState = 2;
                targetSpeed1 = 1; targetSpeed2 = -11;
                break;
        }
        
//        System.out.println(dirtState + " " + turnState);
        
        double dif1 = targetSpeed1 - p.getDouble("wheel1");
        double dif2 = targetSpeed2 - p.getDouble("wheel2");
        
        dif1/=timeStep;
        dif2/=timeStep;
        
        if (dif1>maxForce) dif1 = maxForce;
        if (dif1<-maxForce) dif1 = -maxForce;
        if (dif2>maxForce) dif2 = maxForce;
        if (dif2<-maxForce) dif2 = -maxForce;

        return new Action("move",id,"wheel1",dif1,"wheel2", dif2);
    }
}

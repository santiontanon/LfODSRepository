/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator.perception;

import java.awt.geom.Point2D;
import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.objects.Dirt;
import vacuumCleaner.simulator.objects.PhysicalObject;

/**
 *
 * @author santi
 */
public class WindowPerception extends Perception {

    public WindowPerception() {
        
    }

    public WindowPerception(State s, PhysicalObject subject) throws Exception {
        Point2D.Double pos = new Point2D.Double(subject.getPosition().x,subject.getPosition().y);
        String feature[] = {"l","r","u","d"};
        double offsx[] = {-1,1,0,0};
        double offsy[] = {0,0,-1,1};

        for(int i = 0;i<feature.length;i++) {
            pos.x+=offsx[i];
            pos.y+=offsy[i];
            if (s.collision(subject, pos, 1)) {
                PhysicalObject c = s.collisionWithObjects(subject, pos, 0);
                if (c==null) {
                    values.put(feature[i], 1);
                } else {
                    if (c instanceof Dirt) {
                        values.put(feature[i],2);
                    } else {
                        values.put(feature[i],1);
                    }
                }
            } else {
                values.put(feature[i],0);
            }
            pos.x-=offsx[i];
            pos.y-=offsy[i];
        }
    }


    @Override
    public Perception perceive(State s, PhysicalObject subject) {
        try {
            return new WindowPerception(s, subject);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}

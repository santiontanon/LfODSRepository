/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package vacuumCleaner.simulator.perception;

import vacuumCleaner.simulator.objects.Dirt;
import vacuumCleaner.simulator.objects.PhysicalObject;
import vacuumCleaner.simulator.State;
import java.awt.geom.Point2D;

/**
 *
 * @author santi
 */
public class FourRayDistancePerception extends Perception {

    public FourRayDistancePerception() {

    }


    public FourRayDistancePerception(State s, PhysicalObject subject) throws Exception {
        // create a fake object to use for collision detection:
        Point2D.Double pos = new Point2D.Double(subject.getPosition().x,subject.getPosition().y);
        String feature[] = {"l","r","u","d"};
        
        double offsx[] = {-1,1,0,0};
        double offsy[] = {0,0,-1,1};
        double pos_x = pos.x;
        double pos_y = pos.y;

        for(int i = 0;i<feature.length;i++) {
            int distance = 0;
            do{
                pos.x+=offsx[i];
                pos.y+=offsy[i];
                if (s.collision(subject, pos, 0)) {
                    PhysicalObject c = s.collisionWithObjects(subject, pos, 0);
                    if (c==null) {
                        values.put("d" + feature[i], Math.min(distance,1));
                        values.put(feature[i], 0);
                        break;
                    } else {
                        if (c instanceof Dirt) {
                            values.put("d" + feature[i], Math.min(distance,1));
                            values.put(feature[i], 1);
                            break;
                        } else {
                            values.put("d" + feature[i], Math.min(distance,1));
                            values.put(feature[i], 0);
                            break;
                        }
                    }
                }
                distance++;
            }while(true);
            pos.x = pos_x;
            pos.y = pos_y;
        }
    }

    @Override
    public Perception perceive(State s, PhysicalObject subject) {
        try {
            return new FourRayDistancePerception(s, subject);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

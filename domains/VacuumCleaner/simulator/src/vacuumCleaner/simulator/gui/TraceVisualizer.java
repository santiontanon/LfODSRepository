/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vacuumCleaner.simulator.gui;

import vacuumCleaner.simulator.State;
import vacuumCleaner.simulator.Trace;
import vacuumCleaner.simulator.TraceEntry;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author santi
 */
public class TraceVisualizer extends JPanel implements ListSelectionListener {

    int current_step = 0;
    Trace trace = null;

    JPanel statePanel = null;
    JList Selector = null;
    List<State> states = new LinkedList<>();
    List<Point2D.Double> trajectory = new LinkedList<>();

    public static JFrame newWindow(String name, int dx, int dy, Trace t, int subjectID) throws Exception {
        TraceVisualizer ad = new TraceVisualizer(t, dx, dy, subjectID);
        JFrame frame = new JFrame(name);
        frame.getContentPane().add(ad);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return frame;
    }

    public TraceVisualizer(Trace t, int dx, int dy, int subject) throws Exception {
        current_step = 0;
        trace = t;

        // run the trace and get the states:
        // this method assumes that the interval among entries is always the same:
        {
//            State current = (State) t.getState().clone();
//            states.add((State)current.clone());
            for (TraceEntry te : trace.getEntries()) {
                states.add((State) te.state.clone());
                trajectory.add(te.state.get(te.subject).getPosition());
            }
        }

        setPreferredSize(new Dimension(dx, dy));
        setSize(dx, dy);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting native LAF: " + e);
        }

        setBackground(Color.WHITE);

        removeAll();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        statePanel = new TrajectoryStatePanel(t.getState(), trajectory, 0, 1000);
        statePanel.setPreferredSize(new Dimension((int) (dx * 0.6), dy));
        add(statePanel);

        String[] actionList = new String[t.getEntries().size()];
        Selector = new JList();
        JScrollPane ListScrollPane = new JScrollPane(Selector);

        for (int i = 0; i < t.getEntries().size(); i++) {
            if (t.getEntries().get(i).action != null) {
                actionList[i] = t.getEntries().get(i).action.toSimpleString();
            } else {
                actionList[i] = "-";
            }
        }

        Selector.setListData(actionList);
        Selector.addListSelectionListener(this);
        Selector.setSelectedIndex(0);
        Selector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        Selector.setPreferredSize(new Dimension(100,dy*2));
//        ListScrollPane.setPreferredSize(new Dimension(100,dy*2));

        add(ListScrollPane);
    }

    public void valueChanged(ListSelectionEvent e) {
        int selection = Selector.getSelectedIndex();

        ((StatePanel) statePanel).setState(states.get(selection));
        ((TrajectoryStatePanel) statePanel).setLast(selection);
        this.repaint();
    }

}

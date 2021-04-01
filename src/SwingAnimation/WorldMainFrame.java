package SwingAnimation;

import Constants.Parameters;

import javax.swing.JFrame;
import java.awt.*;

public class WorldMainFrame extends JFrame {

    public WorldMainFrame(Parameters parameters, int id, int locationOffset){
        WorldPanel world = new WorldPanel(parameters, id);
        StatsPanel stats = new StatsPanel(world);
        world.attachObserver(stats);
        add(world);
        add(stats);

        setLayout(new FlowLayout());
        setTitle(Parameters.TITLE + "     scenario " + id);
        pack();
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocation(locationOffset, locationOffset);
        setResizable(false);
        setVisible(true);
    }
}

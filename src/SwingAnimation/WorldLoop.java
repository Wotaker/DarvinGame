package SwingAnimation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorldLoop implements ActionListener {

    private final WorldPanel worldPanel;

    public WorldLoop(WorldPanel worldPanel){
        this.worldPanel = worldPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.worldPanel.nextDay();
    }
}

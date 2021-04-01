package SwingAnimation;

import WorldModel.Statistics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatsPanel extends JPanel {

    private final WorldPanel worldPanel;
    private final Statistics stats;
    JLabel statsLabel;

    public StatsPanel(WorldPanel worldPanel){
        this.worldPanel = worldPanel;
        this.stats = this.worldPanel.getLivingSpace().stats;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 500));
        setBackground(new Color(204, 191, 191));
        addPauseButton(this);
        addGenesButton(this);
        addParametersInfo(this);
        statsLabel = new JLabel("<html><br>Statistics:</html>");
        add(statsLabel);
    }

    private void addPauseButton(JPanel panel) {
        JButton pauseButton = new JButton("Pause");
        final boolean[] paused = {false};
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseSimulation(paused[0]);
                if (paused[0]) {
                    pauseButton.setText("Pause");
                }
                else pauseButton.setText("Resume");
                paused[0] = !paused[0];
            }
        });
        panel.add(pauseButton);
    }

    private void addGenesButton(JPanel panel) {
        JButton genesButton = new JButton("Dominant");
        genesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                worldPanel.showDominant();
            }
        });



        panel.add(genesButton);
    }

    public void pauseSimulation(boolean paused){
        if (paused) this.worldPanel.startTimer();
        else this.worldPanel.stopTimer();
    }

    private void addParametersInfo(JPanel panel) {
        JLabel parameters = new JLabel(worldPanel.getParameters().toString());
        panel.add(parameters);
    }

    public void updateStatsLabels(){
        statsLabel.setText(stats.toString());
    }
}

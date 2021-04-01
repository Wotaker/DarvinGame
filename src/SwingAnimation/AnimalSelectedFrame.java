package SwingAnimation;

import Constants.Parameters;
import WorldModel.Animal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnimalSelectedFrame extends JFrame implements ActionListener {
    private final Animal animal;
    private JTextField trackField;
    private int trackingDuration;

    public AnimalSelectedFrame(Animal animal) {
        this.animal = animal;

        addMainPanel();

        setLayout(new FlowLayout());
        setTitle("Animal Info");
        pack();
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void addMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setPreferredSize(new Dimension(500, 165));

        JLabel animalInfo = new JLabel(animal.toString());
        mainPanel.add(animalInfo);
        mainPanel.add(new JLabel("Track for:"));
        trackField = new JTextField("500", 9);
        mainPanel.add(trackField);
        trackField.addActionListener(this);
        mainPanel.add(new JLabel("[Enter number of days and press ENTER]"));
        this.add(mainPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        trackingDuration = Integer.parseInt(trackField.getText());
        animal.track(trackingDuration);
    }
}

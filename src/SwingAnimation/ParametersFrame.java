package SwingAnimation;

import Constants.Parameters;

import javax.swing.*;
import java.awt.*;

public class ParametersFrame extends JFrame {

    public ParametersFrame() {
        ParametersPanel parametersPanel = new ParametersPanel();
        add(parametersPanel);

        setLayout(new FlowLayout());
        setTitle(Parameters.TITLE);
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);    // Centruje okno aplikacji
        setResizable(false);
        setVisible(true);
    }
}

package SwingAnimation;

import Constants.Parameters;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ParametersPanel extends JPanel {

    private boolean additionalMap = false;
    private Parameters parameters;
    private JTextField statsSaveText;
    private int simulationsRunning = 0;

    public ParametersPanel() {
        loadParameters();
        initializeLayout();
    }

    // Loading parameters from Json file, if failed - loading default parameters
    private void loadParameters() {
        File parametersFile = new File(".\\parameters.json");
        String parametersFileContent = "";
        Scanner scanner = null;

        try {
            scanner = new Scanner(parametersFile);
            while (scanner.hasNextLine()){
                parametersFileContent = parametersFileContent.concat(scanner.nextLine());
            }
            Gson gson = new Gson();
            this.parameters = gson.fromJson(parametersFileContent, Parameters.class);
        } catch (FileNotFoundException e) {
            System.out.println("Can't find your file with simulation parameters!");
            e.printStackTrace();
            this.parameters = new Parameters();
        }

        Parameters.setDefaultSize(this.parameters.getSize());
    }

    private void initializeLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(400, 100));

        addMapCheckBox();

        JPanel statsSavePanel = new JPanel();
        JLabel statsSaveLabel = new JLabel("Save stats after [days] ");
        statsSaveText = new JTextField("1000", 9);
        statsSavePanel.add(statsSaveLabel);
        statsSavePanel.add(statsSaveText);
        add(statsSavePanel);

        addRunButton();
    }

    private void addMapCheckBox() {
        JCheckBox newMapCheckBox = new JCheckBox("Add additional World");
        newMapCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                additionalMap = !additionalMap;
            }
        });
        this.add(newMapCheckBox);
    }

    private void addRunButton(){
        JButton runButton = new JButton("Run Simulation!");
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Parameters.setStatsSaveAfter(Integer.parseInt(statsSaveText.getText()));
                int offSet = 10;
                if (additionalMap) {
                    simulationsRunning += 1;
                    new WorldMainFrame(parameters,
                            simulationsRunning,
                            simulationsRunning * offSet);
                    simulationsRunning += 1;
                    new WorldMainFrame(parameters,
                            simulationsRunning,
                            simulationsRunning * offSet);
                }
                else {
                    simulationsRunning += 1;
                    new WorldMainFrame(parameters,
                            simulationsRunning,
                            simulationsRunning * offSet);
                }
            }
        });
        this.add(runButton);
    }
}

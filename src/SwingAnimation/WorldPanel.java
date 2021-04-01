package SwingAnimation;

import Constants.Parameters;
import WorldModel.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class WorldPanel extends JPanel implements MouseListener {

    private LivingSpace livingSpace;
    private Timer timer;
    private boolean running = true;
    private Parameters parameters;
    private StatsPanel observerStats;

    public WorldPanel(Parameters parameters, int id){
        addMouseListener(this);

        // initialize variables:
        this.parameters = parameters;
        this.livingSpace = new LivingSpace(parameters, id);
        this.timer = new Timer(parameters.getRefreshSpeed(), new WorldLoop(this));
        this.timer.start();

        // initialize layout:
        setPreferredSize(new Dimension(parameters.getWidth(), parameters.getHeight()));
        setBackground(new Color(143, 190, 129));
    }

    public void nextDay() {
        updateWorld();
        repaint();
        observerStats.updateStatsLabels();
    }

    private void updateWorld() {
        livingSpace.nextDay();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        // Drawing Jungle
        g.setColor(new Color(34, 103, 10));
        g.fillRect(livingSpace.junglePosition.getX(),
                livingSpace.junglePosition.getY(),
                livingSpace.jungleWidth,
                livingSpace.jungleHeight);

        drawPlants(g);
        drawAnimals(g);
    }

    private void drawAnimals(Graphics g) {
        Collection<PriorityQueue<Animal>> animalQueues = livingSpace.animals.values();
        for (PriorityQueue<Animal> queueOfAnimal : animalQueues){
            for (Animal animal : queueOfAnimal) {
                g.setColor(animal.getColor());
                g.fillRect(animal.getCoords().getX(),
                        animal.getCoords().getY(),
                        parameters.getDefaultSize(),
                        parameters.getDefaultSize());
            }
        }
    }

    private void drawPlants(Graphics g){
        for (Vector2D plantCoords : livingSpace.plants){
            g.setColor(Parameters.PLANT_COLOR);
            g.fillRect(plantCoords.getX(),
                    plantCoords.getY(),
                    parameters.getDefaultSize(),
                    parameters.getDefaultSize());
        }
    }

    public void stopTimer(){
        this.timer.stop();
        this.running = false;
    }

    public void startTimer(){
        this.timer.start();
        this.running = true;
    }

    public void showDominant() {
        if (running) return;
        int[] dominantGenome = livingSpace.stats.getDominantGenome();
        Set<Vector2D> dominantAnimalsCoords = new LinkedHashSet<>();
        for (Map.Entry<Vector2D, PriorityQueue<Animal>> suspectedSpot
                : livingSpace.animals.entrySet()){
            for (Animal suspectedAnimal : suspectedSpot.getValue()) {
                if (Arrays.equals(dominantGenome, suspectedAnimal.getGenes().getGeneSet())){
                    suspectedAnimal.setColor(Color.RED);
                    dominantAnimalsCoords.add(suspectedAnimal.getCoords());
                }
            }
        }
        for (Vector2D coors : dominantAnimalsCoords)
            repaint(coors.getX(), coors.getY(), Parameters.defaultSize, Parameters.defaultSize);
    }

    public void attachObserver(StatsPanel statsPanel) {
        this.observerStats = statsPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (running) return;
        Vector2D clickCoords = new Vector2D(e.getX(), e.getY());
        if (livingSpace.animals.containsKey(clickCoords)){
            if (livingSpace.animals.get(clickCoords).size() > 0){
                selectAnimal(livingSpace.animals.get(clickCoords).peek());
            }
        }
    }

    private void selectAnimal(Animal animal){
        animal.setColor(Color.RED);
        repaint(animal.getCoords().getX(),
                animal.getCoords().getY(),
                parameters.getDefaultSize(),
                parameters.getDefaultSize());
        new AnimalSelectedFrame(animal);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public LivingSpace getLivingSpace() {
        return livingSpace;
    }

    public Parameters getParameters() {
        return parameters;
    }
}

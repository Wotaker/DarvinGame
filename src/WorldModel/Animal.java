package WorldModel;

import Constants.Parameters;

import java.awt.*;
import java.lang.Math;
import java.util.Arrays;

public class Animal implements Comparable<Animal> {

    private Vector2D coords;
    private int energyLeft;
    private int direction;
    private int numberOfChildren = 0;
    private int daysAlive = 0;
    private Color color;
    private final double colorCoefficient;
    private final Genes genes;
    private int trackingCounter = -1;


    /**
     If an Animal is Created, constructor should assign to energyLeft - startEnergy
     If an Animal is Born, constructor should assign to energyLeft - quarter of parents energy
     */

    public Animal(Vector2D coords, int energyLeft, Genes genes, double colorCoefficient) {
        this.coords = coords;
        this.energyLeft = energyLeft;
        this.genes = genes;
        direction = (int)(Math.random() * 8);
        this.colorCoefficient = colorCoefficient;

    }

    public Animal(Animal mom, Animal dad, Vector2D babyCoords){
        this.coords = babyCoords;
        this.energyLeft = (int) (0.25 * mom.getEnergyLeft()) +
                (int) (0.25 * dad.getEnergyLeft());
        this.direction = (int)(Math.random() * 8);
        this.genes = new Genes(mom.getGenes(), dad.getGenes());
        this.colorCoefficient = mom.colorCoefficient;
        updateColor(this.energyLeft);
    }

    public boolean move(int planeWidth, int planeHeight, int delta, int energyBurnt){
        if (trackingCounter > 0) {
            trackingCounter -= 1;
            if (trackingCounter == 0 || energyLeft < 0){
                boolean alive = energyLeft >= 0;
                System.out.println(String.format("Tracked Animal Info:" +
                        "\nAlive: %b" +
                        "\nNumber of children: %d",
                        alive, numberOfChildren));
            }
        }
        if (energyLeft < 0) return false;   // kill animal with no energy
        daysAlive += 1;

        Vector2D moveVector;
        switch (direction){
            case 0 -> moveVector = new Vector2D(0, delta);
            case 1 -> moveVector = new Vector2D(delta, delta);
            case 2 -> moveVector = new Vector2D(delta, 0);
            case 3 -> moveVector = new Vector2D(delta, -delta);
            case 4 -> moveVector = new Vector2D(0, -delta);
            case 5 -> moveVector = new Vector2D(-delta, -delta);
            case 6 -> moveVector = new Vector2D(-delta, 0);
            case 7 -> moveVector = new Vector2D(-delta, delta);
            default -> moveVector = new Vector2D(0, 0);
        }

        coords = new Vector2D(
                Parameters.modulo(coords.getX() + moveVector.getX(), planeWidth),
                Parameters.modulo(coords.getY() + moveVector.getY(), planeHeight));

        direction = Parameters.modulo(direction + this.genes.getDirection(), 8);
        energyLeft -= energyBurnt;
        if (energyLeft > 0) updateColor(this.energyLeft);
        return true;
    }

    private void updateColor(int energy){
        int colorIntensity = (int) (255 * Math.exp(- (colorCoefficient * energy)));
        this.color = new Color(colorIntensity, colorIntensity, 255);
    }

    public void track(int days){
        trackingCounter = days;
        System.out.print("tracking for days: ");
        System.out.println(days);
    }

    @Override
    // Animal with the most energy is less than any other animal -
    // - it has the highest priority in the Queue
    public int compareTo(Animal otherAnimal) {
        if (otherAnimal.energyLeft > this.energyLeft) return 1;
        if (otherAnimal.energyLeft == this.energyLeft){
            if (otherAnimal.genes.getGeneSet() == this.genes.getGeneSet() &&
                    otherAnimal.coords == this.coords &&
                    otherAnimal.direction == this.direction) return 0;
        }
        return -1;
    }

    @Override
    public String toString() {
        return String.format(
                "<html>Genome:" +
                "<br>%s" +
                "<br><br>Days Alive: %d" +
                "<br>Energy level: %d" +
                "<br>Number of children: %d" +
                "<br><br></html>",
                Arrays.toString(genes.getGeneSet()),
                daysAlive,
                energyLeft,
                numberOfChildren);
    }

    // Getters and Setters:
    public Vector2D getCoords() {
        return coords;
    }

    public int getEnergyLeft() {
        return energyLeft;
    }

    public void setEnergyLeft(int energyLeft) {
        this.energyLeft = energyLeft;
    }

    public Genes getGenes() {
        return genes;
    }

    public Color getColor() {
        return color;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public int getDaysAlive() {
        return daysAlive;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

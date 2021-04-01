package Constants;

import java.awt.*;

public class Parameters {
    public static final String TITLE = "Darwin Game of Life";
    public static final Color PLANT_COLOR = new Color(12, 240, 12);
    public static int defaultSize = 10;
    public static int statsSaveAfter = 1000;

    private int width = 500;
    private int height = 500;
    private int startEnergy = 1000;
    private int moveEnergy = 10;
    private int plantEnergy = 500;
    private float jungleRatio = 0.1f;

    private int size = 10;
    private int refreshSpeed = 100;
    private int startPopulation = 20;
    private float copulationEnergyRatio = 0.5f;
    private int plantsPerDay = 2;




    public static int normalize(double val){
        return ((int)val / defaultSize) * defaultSize;
    }

    public static int normalize(int val){
        return (val / defaultSize) * defaultSize;
    }

    public static int modulo(int val, int divider){
        return ((val % divider) + divider) % divider;
    }

    @Override
    public String toString(){
        return String.format("<html><br>World parameters:" +
                        "<br>World Size: %dx%d" +
                        "<br>Jungle ratio: %.2f" +
                        "<br>Plants per Day: %d" +
                        "<br>Start population size: %d" +
                        "<br>Start energy level: %d" +
                        "<br>Move energy waste: %d" +
                        "<br>Plant energy bonus: %d" +
                        "<br>Copulation energy required: %d" +
                        "</html>",
                width / defaultSize, height / defaultSize,
                jungleRatio,
                plantsPerDay,
                startPopulation,
                startEnergy,
                moveEnergy,
                plantEnergy,
                (int) (copulationEnergyRatio * startEnergy));
    }

    // Setters:
    public static void setDefaultSize(int defaultSize) {
        Parameters.defaultSize = defaultSize;
    }

    public static void setStatsSaveAfter(int statsSaveAfter) {
        Parameters.statsSaveAfter = statsSaveAfter;
    }

    // Getters:
    public int getStartEnergy() {
        return startEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public float getCopulationEnergyRatio() {
        return copulationEnergyRatio;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public float getJungleRatio() {
        return jungleRatio;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getStartPopulation() {
        return startPopulation;
    }

    public int getDefaultSize() {
        return defaultSize;
    }

    public int getRefreshSpeed() {
        return refreshSpeed;
    }

    public int getSize() {
        return size;
    }

    public int getPlantsPerDay() {
        return plantsPerDay;
    }
}

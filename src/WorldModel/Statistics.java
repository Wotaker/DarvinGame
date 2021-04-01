package WorldModel;

import Constants.Parameters;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Statistics {
    private final LivingSpace livingSpace;
    private final int id;
    private int dayNumber = 0;
    private int numberOfAnimals;
    private int numberOfPlants;
    private int[] dominantGenome;
    private int dominantGenomeQuantity;
    private float avgEnergy;
    private float avgNumberOfChildren;
    private float avgDaysAliveFinal;

    List<Integer> deadAnimalsLiveSpan = new LinkedList<>();
    List<Integer> avgAnimalsList = new LinkedList<>();
    List<Integer> avgPlantsList = new LinkedList<>();
    Map<int[], Integer> dominantGenomeMap = new HashMap<>();
    List<Float> avgEnergyList = new LinkedList<>();
    List<Float> avgChildrenList = new LinkedList<>();

    public Statistics(LivingSpace livingSpace, int id) {
        this.livingSpace = livingSpace;
        this.id = id;
    }

    public void updateStats(){
        int sumLiveSpan = 0;
        for (int animalLiveSpan : deadAnimalsLiveSpan){
            sumLiveSpan += animalLiveSpan;
        }
        this.avgDaysAliveFinal = ((float) sumLiveSpan) / deadAnimalsLiveSpan.size();

        // Gather data to export in a file
        if (dayNumber <= Parameters.statsSaveAfter){
            avgAnimalsList.add(numberOfAnimals);
            avgPlantsList.add(numberOfPlants);
            avgEnergyList.add(avgEnergy);
            avgChildrenList.add(avgNumberOfChildren);
            if (dominantGenomeMap.containsKey(dominantGenome)){
                dominantGenomeMap.put(
                        dominantGenome,
                        dominantGenomeMap.get(dominantGenome) + dominantGenomeQuantity);
            }
            else dominantGenomeMap.put(dominantGenome, dominantGenomeQuantity);
            if (dayNumber == Parameters.statsSaveAfter) {
                exportStats();
            }
        }
    }

    private void exportStats() {
        float avgAnimalsFinal = (float)
                avgAnimalsList.stream().mapToInt(Integer::intValue).sum() / dayNumber;
        float avgPlantsFinal = (float)
                avgPlantsList.stream().mapToInt(Integer::intValue).sum() / dayNumber;
        float avgEnergyFinal = (float)
                avgEnergyList.stream().mapToDouble(Float::doubleValue).sum() / dayNumber;
        float avgChildrenFinal = (float)
                avgChildrenList.stream().mapToDouble(Float::doubleValue).sum() / dayNumber;
        OptionalInt maxGenome =
                dominantGenomeMap.values().stream().mapToInt(Integer::intValue).max();
        int[] dominantGenomeFinal = new int[0];
        if (maxGenome.isPresent()) {
            for (Map.Entry<int[], Integer> entry : dominantGenomeMap.entrySet()){
                if (Objects.equals(maxGenome.getAsInt(), entry.getValue()))
                    dominantGenomeFinal = entry.getKey();
            }
        }
        try(
                FileWriter fileWriter = new FileWriter(String.format("statistics%s.txt", id));
                PrintWriter printWriter = new PrintWriter(fileWriter);
                ){

            printWriter.printf("Simulation %s\nStatistics after %s days:" +
                    "\n\nAverage number of animals: %.2f" +
                    "\nAverage number of plants: %.2f" +
                    "\nDominant genome:\n%s" +
                    "\nAverage energy level: %.2f" +
                    "\nAverage life span: %.2f" +
                    "\nAverage number of children: %.2f",
                    id,
                    dayNumber,
                    avgAnimalsFinal,
                    avgPlantsFinal,
                    Arrays.toString(dominantGenomeFinal),
                    avgEnergyFinal,
                    avgDaysAliveFinal,
                    avgChildrenFinal);
        }
        catch (IOException e){
            System.out.println("Can not create file \"statistics.txt\"");
            e.printStackTrace();
        }

    }

    @Override
    public String toString(){
        return String.format("<html><br>Statistics:" +
                "<br>Number of plants: %d" +
                "<br>Number of Animals: %d" +
                "<br>Dominant genome: " +
                genomeStringHTML(dominantGenome) +
                "<br> Average Energy Level: %.2f" +
                "<br> Average Number of Children: %.2f" +
                "<br> Average Days Alive: %.2f" +
                "<br><br> Day %d</html>",
                numberOfPlants,
                numberOfAnimals,
                avgEnergy,
                avgNumberOfChildren,
                avgDaysAliveFinal,
                dayNumber);
    }

    private String genomeStringHTML(int[] genome){
        if (genome.length == 0) return "NaN";
        StringBuilder result = new StringBuilder("<br>");
        int currentGene = 0;
        for (int gene : genome){
            if (gene == currentGene) result.append(String.format("%d ", gene));
            else {
                result.append(String.format("<br>%d ", gene));
                currentGene = gene;
            }
        }
        return result.toString();
    }

    // Getters and Setters
    public int getDayNumber() {
        return dayNumber;
    }

    public int[] getDominantGenome() {
        return dominantGenome;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public void setNumberOfAnimals(int numberOfAnimals) {
        this.numberOfAnimals = numberOfAnimals;
    }

    public void setNumberOfPlants(int numberOfPlants) {
        this.numberOfPlants = numberOfPlants;
    }

    public void setDominantGenome(int[] dominantGenome) {
        this.dominantGenome = dominantGenome;
    }

    public void setDominantGenomeQuantity(int dominantGenomeQuantity) {
        this.dominantGenomeQuantity = dominantGenomeQuantity;
    }

    public void setAvgEnergy(float avgEnergy) {
        this.avgEnergy = avgEnergy;
    }

    public void setAvgNumberOfChildren(float avgNumberOfChildren) {
        this.avgNumberOfChildren = avgNumberOfChildren;
    }
}

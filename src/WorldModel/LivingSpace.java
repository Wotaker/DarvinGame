package WorldModel;

import Constants.Parameters;
import java.lang.Math;
import java.util.*;

public class LivingSpace {

    public Statistics stats;
    public Parameters parameters;
    public int jungleWidth;
    public int jungleHeight;
    public Vector2D junglePosition;
    public double colorCoefficient;

    public List<Vector2D> plants = new LinkedList<>();
    public Map<Vector2D, PriorityQueue<Animal>> animals = new HashMap<>();


    public LivingSpace(Parameters parameters, int id){
        stats = new Statistics(this, id);
        this.parameters = parameters;
        float jungleRatio1D = (float) Math.sqrt(parameters.getJungleRatio());
        jungleWidth = Parameters.normalize(parameters.getWidth() * jungleRatio1D);
        jungleHeight = Parameters.normalize(parameters.getHeight() * jungleRatio1D);
        junglePosition = (new Vector2D(
                parameters.getWidth() / 2,
                parameters.getHeight() / 2)).add(new
                Vector2D(-(jungleWidth / 2), -(jungleHeight / 2)));
        colorCoefficient = - (1 / ((double) parameters.getStartEnergy())) *
                Math.log(100 / ((double) 255));

        addStartPopulation();
    }

    private void addStartPopulation() {
        Vector2D newAnimalCoords;
        Animal newAnimal;
        for (int i = 0; i < parameters.getStartPopulation(); i++){
            newAnimalCoords = Vector2D.randomizeVector(parameters.getWidth(), parameters.getHeight());
            newAnimal = new Animal(
                    newAnimalCoords,
                    parameters.getStartEnergy(),
                    new Genes(),
                    colorCoefficient);
            if (!animals.containsKey(newAnimalCoords)) {
                animals.put(newAnimalCoords, new PriorityQueue<>());
            }
            animals.get(newAnimalCoords).add(newAnimal);
        }
    }

    public void nextDay() {
        stats.setDayNumber(stats.getDayNumber() + 1);

        moveAnimals();  // removes dead animals and computes stats as well
        eatPlants();
        copulate();
        addPlants();
    }

    private void moveAnimals() {
        List<Animal> allAnimals = new LinkedList<>();
        for (Vector2D animalCoords : animals.keySet()){
            allAnimals.addAll(animals.get(animalCoords));
        }

        animals.clear();

        int numberOfAnimals = 0;
        int SumEnergy = 0;
        int SumChildren = 0;
        Map<int[], Integer> uniqueGenes = new HashMap<>();

        for (Animal animal : allAnimals) {
            if (animal.move(
                    parameters.getWidth(),
                    parameters.getHeight(),
                    parameters.getSize(),
                    parameters.getMoveEnergy())){

                // animal is still alive
                numberOfAnimals += 1;
                SumEnergy += animal.getEnergyLeft();
                SumChildren += animal.getNumberOfChildren();
                int[] geneSet = animal.getGenes().getGeneSet();
                if (uniqueGenes.containsKey(geneSet)){
                    uniqueGenes.replace(geneSet, uniqueGenes.get(geneSet) + 1);
                }
                else uniqueGenes.put(geneSet, 1);

                addAnimal(animal, animals);
            }
            else stats.deadAnimalsLiveSpan.add(animal.getDaysAlive());  // animal is dead
        }

        int[] dominantGeneSet = new int[0];
        int max = 0;
        for (int[] geneSet : uniqueGenes.keySet()){
            if (uniqueGenes.get(geneSet) > max) {
                dominantGeneSet = geneSet;
                max = uniqueGenes.get(geneSet);
            }
        }
        stats.setDominantGenome(dominantGeneSet);
        stats.setDominantGenomeQuantity(max);
        stats.setNumberOfAnimals(numberOfAnimals);
        stats.setAvgEnergy(((float) SumEnergy) / numberOfAnimals);
        stats.setAvgNumberOfChildren(((float) SumChildren) / numberOfAnimals);

        stats.updateStats();
    }

    private void eatPlants() {
        List<Animal> hungryAnimals = new LinkedList<>();
        int maxEnergy;
        for (Vector2D mapCoords : animals.keySet()) {
            if (plants.contains(mapCoords)) {
                // solving conflicts:
                maxEnergy = animals.get(mapCoords).peek().getEnergyLeft();
                while (animals.get(mapCoords).peek().getEnergyLeft() == maxEnergy) {
                    hungryAnimals.add(animals.get(mapCoords).poll());
                    if (animals.get(mapCoords).size() == 0) break;
                }
            }
            if (hungryAnimals.size() > 0){
                int plantEnergySplit = parameters.getPlantEnergy() / hungryAnimals.size();
                plants.remove(hungryAnimals.get(0).getCoords());
                for (Animal animal : hungryAnimals){
                    animal.setEnergyLeft(animal.getEnergyLeft() + plantEnergySplit);
                    animals.get(animal.getCoords()).add(animal);
                }
            }
            hungryAnimals.clear();
        }
    }

    private void copulate() {
        List<Vector2D> sexSpots = new LinkedList<>();
        for (Vector2D sexSpot : animals.keySet()){
            if (animals.get(sexSpot).size() >= 2) {
                sexSpots.add(sexSpot.copy());
            }
        }

        for (Vector2D sexSpot : sexSpots){
            Animal mom = animals.get(sexSpot).poll();
            Animal dad = animals.get(sexSpot).poll();
            if (
                    mom.getEnergyLeft() >=
                            parameters.getCopulationEnergyRatio() * parameters.getStartEnergy() &&
                    dad.getEnergyLeft() >=
                            parameters.getCopulationEnergyRatio() * parameters.getStartEnergy()){
                mom.setEnergyLeft((int) (0.75 * mom.getEnergyLeft()));
                dad.setEnergyLeft((int) (0.75 * dad.getEnergyLeft()));
                mom.setNumberOfChildren(mom.getNumberOfChildren() + 1);
                dad.setNumberOfChildren(dad.getNumberOfChildren() + 1);
                Vector2D babyCoords = spawnCoords(mom.getCoords());
                Animal puppy = new Animal(mom, dad, babyCoords);
                addAnimal(puppy, animals);
            }
            addAnimal(mom, animals);
            addAnimal(dad, animals);
        }
        sexSpots.clear();
    }

    private Vector2D spawnCoords(Vector2D parentsCoords) {
        int[] dx = {0, 1, 1, 1, 0, -1, -1, -1};
        int[] dy = {-1, -1, 0, 1, 1, 1, 0, -1};
        List<Vector2D> availableCoords = new ArrayList<>();
        Vector2D checkedCoords;
        for (int i = 0; i < 8; i ++){
            checkedCoords = parentsCoords.add(new Vector2D(
                    parameters.getSize() * dx[i],
                    parameters.getSize() * dy[i]));
            if (!animals.containsKey(checkedCoords) &&
                    !plants.contains(checkedCoords))
                availableCoords.add(checkedCoords);
        }

        if (availableCoords.size() == 0){
            int positionIdx = (int) (Math.random() * 8);
            return parentsCoords.add(
                    parameters.getSize() * dx[positionIdx],
                    parameters.getSize() * dy[positionIdx]);
        }
        return availableCoords.get((int) (Math.random() * availableCoords.size()));
    }

    private void addPlants(){
        Vector2D regularPlantCoords;
        int planted = 0;
        int tries = 3;
        while(planted < parameters.getPlantsPerDay() / 2 && tries > 0){
            regularPlantCoords = Vector2D.randomizeVector(parameters.getWidth(), parameters.getHeight());
            if (!inJungle(regularPlantCoords)){
                if (!plants.contains(regularPlantCoords) &&
                        animals.get(regularPlantCoords) == null){   // kolejka pusta nie jest null
                    plants.add(regularPlantCoords);
                    planted += 1;
                }
            }

            tries -= 1;
        }

        Vector2D junglePlantCoords;
        planted = 0;
        tries = 3;
        while(planted < parameters.getPlantsPerDay() / 2 && tries > 0){
            junglePlantCoords =
                    (Vector2D.randomizeVector(jungleWidth, jungleHeight)).add(junglePosition);

            if (!plants.contains(junglePlantCoords) && animals.get(junglePlantCoords) == null){
                plants.add(junglePlantCoords);
                planted += 1;
            }
            
            tries -= 1;
        }

        stats.setNumberOfPlants(plants.size());
    }

    // If you want a line of plants on your map
    private void addPlantsLine(){
        Vector2D positionIter = new Vector2D(0, parameters.getHeight() / 2);
        while (positionIter.getX() < parameters.getWidth()){
            plants.add(positionIter);
            positionIter = positionIter.add(parameters.getSize(), 0);
        }
    }

    // If you want your whole map filled with plants
    private void addPlantsPlane(){
        for (Vector2D xIter = new Vector2D(0, 0);
        xIter.getX() < parameters.getWidth();
        xIter = xIter.add(parameters.getSize(), 0)){
            for (Vector2D yIter = new Vector2D(xIter.getX(), 0);
            yIter.getY() < parameters.getHeight();
            yIter = yIter.add(0, parameters.getSize())){
                plants.add(yIter);
            }
        }
    }

    private void addAnimal(Animal animal, Map<Vector2D, PriorityQueue<Animal>> animalMap){
        if (!animalMap.containsKey(animal.getCoords())){
            animalMap.put(animal.getCoords(), new PriorityQueue<>());
        }
        animalMap.get(animal.getCoords()).add(animal);
    }

    private boolean inJungle(Vector2D coords){
        return coords.getX() >= junglePosition.getX() &&
                coords.getX() < junglePosition.getX() + jungleWidth &&
                coords.getY() >= junglePosition.getY() &&
                coords.getY() < junglePosition.getY() + jungleHeight;
    }
}

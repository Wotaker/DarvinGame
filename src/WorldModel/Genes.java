package WorldModel;

import Constants.Parameters;

import java.lang.Math;
import java.util.Arrays;

public class Genes {
    private final int[] geneSet;

    // Constructor for start population
    public Genes(){
        geneSet = new int[32];
        for (int i = 0; i < 32; i++){
            geneSet[i] = (int)(Math.random() * 8);
        }
        boolean validatedGenes = validate();
        while (!validatedGenes) validatedGenes = validate();
        Arrays.sort(geneSet);
    }

    // additional constructor, in case of creating gene set with particular gene enhanced
    public Genes(int dominantGene, float ratio){
        geneSet = new int[32];
        int divideIdx = Parameters.modulo((int) (ratio * 32), 33);
        for (int i = 0; i < divideIdx; i++){
            geneSet[i] = Parameters.modulo(dominantGene, 8);

        }
        for (int i = divideIdx; i < 32; i++){
            geneSet[i] = (int)(Math.random() * 8);
        }
        boolean validatedGenes = validate();
        while (!validatedGenes) validatedGenes = validate();
        Arrays.sort(geneSet);
    }

    // Constructor for born animals
    public Genes(Genes momsGenes, Genes dadsGenes){
        geneSet = new int[32];
        int divideIdx = Math.max((int) (Math.random() * 32), (int) (Math.random() * 32));
        for (int i = 0; i < divideIdx; i++){
            geneSet[i] = momsGenes.getGeneSet()[i];
        }
        for (int i = divideIdx; i < 32; i++){
            geneSet[i] = dadsGenes.getGeneSet()[i];
        }

        boolean validatedGenes = validate();
        while (!validatedGenes) validatedGenes = validate();
        Arrays.sort(geneSet);
    }

    public int getDirection() {
        int[] directionDistribution = new int[8];
        for (int i = 0; i < 8; i++) directionDistribution[i] = 0;

        for (int gene : geneSet){
            directionDistribution[gene] += 1;
        }

        int acc = 0;
        for (int i = 0; i < 8; i++) {
            directionDistribution[i] += acc;
            acc = directionDistribution[i];
        }

        int randomVal = (int)(Math.random() * 32);
        int direction = 0;
        while (randomVal > directionDistribution[direction]){
            direction += 1;
        }

        return direction;
    }

    private boolean validate() {
        int[] genesDistribution = new int[8];
        for (int i = 0; i < 8; i++) genesDistribution[i] = 0;
        for (int gene : geneSet){
            genesDistribution[gene] += 1;
        }
        boolean validated = true;
        for (int gene = 0; gene < 8; gene++){
            if (genesDistribution[gene] == 0){
                validated = false;
                geneSet[(int) (Math.random() * 32)] = gene;
            }
        }
        return validated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genes)) return false;
        Genes genes = (Genes) o;
        return Arrays.equals(geneSet, genes.geneSet);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(geneSet);
    }

    @Override
    public String toString(){
        StringBuilder result = new StringBuilder("[ ");
        for(int gene : geneSet) {
            result.append(gene);
            result.append(" ");
        }
        return result + "]";
    }

    public int[] getGeneSet() {
        return geneSet;
    }
}

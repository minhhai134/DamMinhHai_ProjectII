package GeneticAlgorithm.ClassScheduler;

import javax.swing.*;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.IntStream;

public class GeneticAlgorithm {

    private static int cnt =0;
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    protected int tournamentSize;

    // implementing multi heuristics
    private double temperature = 1.0;

    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount,
                            int tournamentSize) {

        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    public Population initPopulation(Schedule schedule) {
        // Initialize population
        Population population = new Population(this.populationSize, schedule);
        return population;
    }

    public boolean isTerminationConditionMet(int generationsCount, int maxGenerations) {
        return (generationsCount > maxGenerations);
    }

    public boolean isTerminationConditionMet(Population population) {
        return population.getFittest(0).getFitness() == 1.0;
    }

    // Create fitness hashtable
    private Map<Individual, Double> fitnessHash = Collections.synchronizedMap(
            new LinkedHashMap<Individual, Double>() {
                @Override
                protected boolean removeEldestEntry(Entry<Individual, Double> eldest) {
                    // Store a maximum of 1500 fitness values
                    return this.size() > 1500;
                }
            });    // DAY LA KHAI BAO THUOC TINH KET HOP VOI KHOI TAO GIA TRI LUON


    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public int getElitismCount() {
        return elitismCount;
    }

    public void setElitismCount(int elitismCount) {
        this.elitismCount = elitismCount;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Map<Individual, Double> getFitnessHash() {
        return fitnessHash;
    }

    public void setFitnessHash(Map<Individual, Double> fitnessHash) {
        this.fitnessHash = fitnessHash;
    }



    //************************************************************************************
    // Hash table was used to store the values of fitness calculated
    // so it helped in saving extra calculations fitness of same individual can be taken from hash table
    //************************************************************************************
    public double calcFitness(Individual individual, Schedule schedule) {
        Double storedFitness = this.fitnessHash.get(individual);  // Kiem tra xem individual da ton tai trong hashtable chua
        if (storedFitness != null) {
            return storedFitness;
        }

        // Neu Individual chua ton tai trong hashtable:
        // Create new Schedule object for thread
        Schedule threadSchedule = new Schedule(schedule);  // Ly do vi sao phai tao mot constructor ma tham so truyen vao chinh la mot instance
                                                           // cua class do, la de clone
        threadSchedule.createTaskList(individual);    // Neu individual chua ton tai trong hashtable thi can tinh fitness cho no
                                                         // bang cach giai ma no su dung createUnivClasses()
                                       // ???(Ly do phai tao clone cho schedule la vi schedule chua thong tin dung cho ca chuong trinh)
        // Calculate fitness
        int clashes = threadSchedule.calcClashes();
        double fitness = 1 / (double) (clashes + 1);

        individual.setFitness(fitness);

        // Store fitness in hashtable
        this.fitnessHash.put(individual, fitness);

        return fitness;
    }

    public void evalPopulation(Population population, Schedule schedule) {   // TRUYEN VAO schedule de lam gi??
                                                                             // -> de dung cho method calcFitness(), vay hay xem calcFitness
        IntStream.range(0, population.size()).parallel()   // Tinh Fitness cho moi ca the trong quan the
                .forEach((int i) -> {
                    GeneticAlgorithm.this.calcFitness(population.getIndividual(i), schedule);
                });

        double populationFitness = 0;

        // Loop over population evaluating individuals and summing population
        // fitness
        for (Individual individual : population.getIndividuals()) {
            double a= this.calcFitness(individual, schedule);
 //           if(cnt<10){ JOptionPane.showMessageDialog(null,a);} cnt++;
            populationFitness += a;
        }

        population.setPopulationFitness(populationFitness);
    }

    public Individual selectParent(Population population) {
        // Create tournament
        Population tournament = new Population(this.tournamentSize);

        // Add random individuals to the tournament
        population.shuffle();  // TRAO DOI THU TU NGAU NHIEN CAC CA THE TRONG QUAN THE
        for (int i = 0; i < this.tournamentSize; i++) {
            Individual tournamentIndividual = population.getIndividual(i);
            tournament.setIndividual(i, tournamentIndividual);
        }

        // Return the best
        return tournament.getFittest(0);
    }


    public Population crossoverPopulation(Population population) {
        // Create new population
        Population newPopulation = new Population(population.size());

        // Loop over current population by fitness
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual parent1 = population.getFittest(populationIndex);

            // Apply crossover to this individual?
            if (this.crossoverRate > Math.random() && populationIndex >= this.elitismCount) {
                // Initialize offspring
                Individual offspring = new Individual(parent1.getChromosomeLength());

                // Find second parent
                Individual parent2 = selectParent(population);

                // Loop over genome
                for (int geneIndex = 0; geneIndex < parent1.getChromosomeLength(); geneIndex++) {
                    // Use half of parent1's genes and half of parent2's genes
                    if (0.5 > Math.random()) {
                        offspring.setGene(geneIndex, parent1.getGene(geneIndex));
                    } else {
                        offspring.setGene(geneIndex, parent2.getGene(geneIndex));
                    }
                }

                // Add offspring to new population
                newPopulation.setIndividual(populationIndex, offspring);
            } else {
                // Add individual to new population without applying crossover
                newPopulation.setIndividual(populationIndex, parent1);
            }
        }

        return newPopulation;
    }



    public Population mutatePopulation(Population population, Schedule schedule) {   // Dung schedule nay de lam gi?
        // Initialize new population                                                 // -> de tao ngau nhien mot ca the
        Population newPopulation = new Population(this.populationSize);

        // Get best fitness
        double bestFitness = population.getFittest(0).getFitness();

        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);  // DAI KHAI LA DUYET TUNG CA THE

            // Create random individual to swap genes with
            Individual randomIndividual = new Individual(schedule);

            // Calculate adaptive mutation rate
            double adaptiveMutationRate = this.mutationRate;
            if (individual.getFitness() > population.getAvgFitness()) {
                double fitnessDelta1 = bestFitness - individual.getFitness();
                double fitnessDelta2 = bestFitness - population.getAvgFitness();
                adaptiveMutationRate = (fitnessDelta1 / fitnessDelta2) * this.mutationRate;
            }

            for (int geneIndex = 0; geneIndex < individual.getChromosomeLength(); geneIndex++) {
                // Skip mutation if this is an elite individual
                if (populationIndex > this.elitismCount) {
                    // Does this gene need mutating?
                    //  if (adaptiveMutationRate > Math.random()) {
                    if ((adaptiveMutationRate * this.getTemperature()) > Math.random()) {

                        // Swap for new gene
                        individual.setGene(geneIndex, randomIndividual.getGene(geneIndex));
                    }
                }
            }

            // Add individual to population
            newPopulation.setIndividual(populationIndex, individual);
        }

        // Return mutated population
        return newPopulation;
    }

    public double getTemperature() {
        return temperature;
    }






}


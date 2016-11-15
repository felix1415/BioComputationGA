/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bio.computation.ga;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexgray
 */
public class Popultation
{
    public final int POPULATION_NUM = 100;
    public final int GENE_NUM;
    public int RULE_LENGTH;
    public final int NUMBER_OF_RULES = 20;
    public final int GENERATIONS = 1500;
    public final double CROSSOVER_NUM = 0.9;
    public double MUTATION_NUM = 0.02;
    public float MUTATION_RANGE = 0.4f;
    
    public final float VALIDATION_RULES_PERCENT = 40f;
    
    public final float FINE_TUNE_TRIGGER_PERCENT = 80f;
    public final float FINE_TUNE_PERCENT = 30f;
    
    public final String OUTPUT_FILE = "graph_data.csv";
    public final String INPUT_FILE = "input_data.dsv";
    
    private float [] trainingRules;
    private int numberOfTrainingRules;
    private float [] validationRules;
    private int numberOfValidationRules;
    
    private RuleSet [] population;
    private final RuleSet [] offspring;
    
    private final Random random;
    
    private double meanFitness;
    private int fittest;
    private int worst;
    private RuleSet fittestSolution;

    public Popultation()
    {
        this.population = new RuleSet[POPULATION_NUM];
        this.offspring = new RuleSet[POPULATION_NUM];
        this.random = new Random();
        this.fittestSolution = null;
        
        //delete exsisting output file
        File file = new File(this.OUTPUT_FILE);
        if (file.exists()){
            file.delete();
        }
        
        this.RULE_LENGTH = 0;
        this.readInFile();
        this.GENE_NUM = RULE_LENGTH * NUMBER_OF_RULES;
        
        for (int i = 0; i < POPULATION_NUM; i++)
        {
            this.population[i] = new RuleSet(GENE_NUM, trainingRules, RULE_LENGTH, i);
        }

    }

    void run()
    {
        boolean fineTuneTriggered = false;
        this.calculateFitness();
        this.print(0);
        for (int i = 0; i < GENERATIONS; i++)
        {
            this.selection();
            this.crossover();
            this.mutation();
            this.calculateFitness();
            this.addBestSolutionBack();
            this.print(i+1);
            if (this.fittest == this.numberOfTrainingRules)
            {
                break;
            } else if (((float)this.fittest / this.numberOfTrainingRules) > (this.FINE_TUNE_TRIGGER_PERCENT / 100))
            {
                if(!fineTuneTriggered)
                {
                    System.out.println("@@@Fine Tuning Triggered@@@");
                    fineTuneTriggered = true;
                    //decrease the range in which a gene can mutate
                    this.MUTATION_RANGE = this.MUTATION_RANGE * (this.FINE_TUNE_PERCENT / 100);
                    //decrese the chance of mutatation
                    this.MUTATION_NUM = this.MUTATION_NUM / 2;
                }
            }
            
        }
        
//        this.fittestSolution.print();
        
        int numberOfValidatedRules = this.fittestSolution.validateRules(this.validationRules, this.numberOfValidationRules);
        if(this.numberOfValidationRules == numberOfValidatedRules)
        {
            System.out.println("Fitness of fittest soloution: " + this.fittestSolution.getFitness()
        +                  "/" + this.numberOfTrainingRules + " Validation Rules Success: " +
        +                  numberOfValidatedRules + "/" + this.numberOfValidationRules);
        }
        else
        {
            System.err.println("Fitness of fittest soloution: " + this.fittestSolution.getFitness()
            +                  "/" + this.numberOfTrainingRules + " Validation Rules Success: " +
            +                  numberOfValidatedRules + "/" + this.numberOfValidationRules);
        }
    }
    
    void selection()
    {
        for (int i = 0; i < POPULATION_NUM; i++)
        {
            int parent1 = random.nextInt(POPULATION_NUM);
            int parent2 = random.nextInt(POPULATION_NUM);
            
            if(population[parent1].getFitness() >= population[parent2].getFitness())
            {
                offspring[i] = new RuleSet(population[parent1]);
            }
            else
            {
                offspring[i] = new RuleSet(population[parent2]);
            }
        }
        population = offspring.clone();
    }
    
    void crossover()
    {
        //for half the population
        for (int i = 0; i < (POPULATION_NUM/2); i++)
        {
            //if crossover is triggered
            if(random.nextDouble() > CROSSOVER_NUM)
            {
                //get crossover point
                int crossOverPoint = random.nextInt(GENE_NUM);
                float temp;
                //for each element after the crossover point
                for (int j = crossOverPoint; j < GENE_NUM; j++)
                {
                    //swap (i*2 element) and the (i*2 + 1)
                    temp = population[i*2].getGene()[j];
                    population[i*2].setGene(j, population[i*2 + 1].getGene()[j]);
                    population[i*2 + 1].setGene(j, temp);
                }
                
            }
        }
    }
    
    void mutation()
    {
        //for each member of the population, process mutation
        for (int i = 0; i < POPULATION_NUM; i++)
        {
            this.population[i].mutation(MUTATION_NUM, MUTATION_RANGE);
        }
    }
    
    void calculateFitness()
    {
        this.meanFitness = 0;
        this.fittest = 0;
        this.worst = 2000;
        for (int i = 0; i < POPULATION_NUM; i++)
        {
            this.population[i].calcFitness();
            int fitness = this.population[i].getFitness();
            if(fitness > this.fittest)
            {
                this.fittest = fitness;
                this.fittestSolution = this.population[i];
            }
            if(fitness < this.worst)
            {
                this.worst = i;
            }
            this.meanFitness += fitness;
        }
        this.meanFitness = this.meanFitness / POPULATION_NUM;
    }
    
    void print(int generationNum)
    {
        System.out.println("Mean Fitness: " + this.meanFitness +
                            " Fittest: " + this.fittest +
                            " Gen:" + generationNum);
        this.outputFitnessToFile();
    }
    
    public void printPopulation()
    {
        for (int i = 0; i < POPULATION_NUM; i++)
        {
            this.population[i].print();
        }
    }
    
    public void outputFitnessToFile()
    {
        try(FileWriter fw = new FileWriter(this.OUTPUT_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);)
        {
            out.println(this.meanFitness + "," + this.fittest);
        } catch (IOException e) 
        {
        }        
    }
    
    private void readInFile()
    {
        int geneNumber = 0;
        try
        {
            int numOfLines = 0;
            Scanner fileIn = new Scanner(new File(INPUT_FILE));
            while(fileIn.hasNext())
            {
                if(numOfLines == 0)
                {
                    Scanner line = new Scanner(fileIn.nextLine());
                    numOfLines++; // increment for taking one off the fileIn
                    while(line.hasNext())
                    {
                        line.next();
                        this.RULE_LENGTH++;
                    }
                }
                numOfLines++;
                fileIn.nextLine();
            }
            this.numberOfValidationRules = (int) (numOfLines * ((float)VALIDATION_RULES_PERCENT / 100));
            this.numberOfTrainingRules = numOfLines - this.numberOfValidationRules;
            this.trainingRules = new float [this.numberOfTrainingRules * this.RULE_LENGTH];
            this.validationRules = new float [this.numberOfValidationRules * this.RULE_LENGTH];
            boolean [] isValidationSet = this.getValidationBooleanArray(numOfLines, this.numberOfValidationRules);
            fileIn = new Scanner(new File(INPUT_FILE));
            int trainingRuleNumber = 0;
            int validationRuleNumber = 0;
            while(fileIn.hasNext())
            {
                Scanner line = new Scanner(fileIn.nextLine());
                //for each gene in the line
                for (int i = 0; i < this.RULE_LENGTH; i++)
                {
                    //add gene to position in rule and rule position in array and correct array (training or validation)
                    if(isValidationSet[geneNumber] == true)
                    {
                        this.validationRules[validationRuleNumber + i] = Float.parseFloat(String.valueOf(line.next()));
                    }
                    else
                    {
                        this.trainingRules[trainingRuleNumber + i] = Float.parseFloat(String.valueOf(line.next()));
                    }
                }
                if(isValidationSet[geneNumber] == true)
                {
                    validationRuleNumber += this.RULE_LENGTH;
                }
                else
                {
                    trainingRuleNumber += this.RULE_LENGTH;
                }
                geneNumber++;
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Popultation.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.RULE_LENGTH = (this.RULE_LENGTH * 2); // bounds plus one for the output
        System.out.println(geneNumber);
        System.out.println(RULE_LENGTH);
    }
    
    private boolean [] getValidationBooleanArray(int sizeOfDataSet, int validationSets)
    {
        int currentNumberOfValidationSets = 0;
        boolean [] isValidationSet = new boolean [sizeOfDataSet];
        while (currentNumberOfValidationSets < validationSets)
        {
            int newIndex = this.random.nextInt(sizeOfDataSet);
            if(isValidationSet[newIndex] != true)
            {
                isValidationSet[newIndex] = true;
                currentNumberOfValidationSets++;
            }
        }
        return isValidationSet;
    }

    private void printFitnessRules()
    {
        Util.printArray(trainingRules, this.RULE_LENGTH);
    }

    private void addBestSolutionBack()
    {
        this.population[this.worst] = this.fittestSolution;
    }
}

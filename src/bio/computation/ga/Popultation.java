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
    public final int GENERATIONS = 300;
    public final double CROSSOVER_NUM = 0.9;
    public final double MUTATION_NUM = 0.009;
    public final float MUTATION_RANGE = (float) 0.1;
    
    public final String OUTPUT_FILE = "graph_data.csv";
    public final String INPUT_FILE = "input_data.dsv";
    
    private float [] fitnessRules;
    
    private RuleSet [] population;
    private final RuleSet [] offspring;
    
    private final Random random;
    
    private double meanFitness;
    private int fittest;
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
            this.population[i] = new RuleSet(GENE_NUM, fitnessRules, RULE_LENGTH, i);
        }

    }

    void run()
    {
//        System.out.println(this.population[1].getFitnessOutput(32));
//        System.out.println(this.population[1].getFitnessOutput(643));
//        System.out.println(this.population[1].getFitnessOutput(343));
//        System.out.println(this.population[1].getFitnessOutput(312));
//        System.out.println(this.population[1].getFitnessOutput(37));
//        System.out.println(this.population[1].getFitnessOutput(764));
//        System.out.println(this.population[1].getFitnessOutput(1500));
//        return;
        
        this.calculateFitness();
//        this.printPopulation();
        this.print();
//        this.printFitnessRules();
        for (int i = 0; i < GENERATIONS; i++)
        {
            this.selection();
            this.crossover();
            this.mutation();
            this.calculateFitness();
//            this.addBestSolutionBack();
//            this.printPopulation();
            this.print();
        }
        this.fittestSolution.print();
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
        for (int i = 0; i < POPULATION_NUM; i++)
        {
            this.population[i].calcFitness();
            int fitness = this.population[i].getFitness();
            if(fitness > this.fittest)
            {
                this.fittest = fitness;
                this.fittestSolution = this.population[i];
            }
            this.meanFitness += fitness;
        }
        this.meanFitness = this.meanFitness / POPULATION_NUM;
    }
    
    void print()
    {
        System.out.println("Mean Fitness: " + this.meanFitness + " Fittest: " + this.fittest);
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
            this.fitnessRules = new float [numOfLines * this.RULE_LENGTH];
            fileIn = new Scanner(new File(INPUT_FILE));
            int ruleNumber = 0;
            //while there is a next line
            while(fileIn.hasNextLine())
            {
                Scanner line = new Scanner(fileIn.nextLine());
                //for each gene in the line
                for (int i = 0; i < this.RULE_LENGTH; i++)
                {
                    //add gene to position in rule and rule position in array
                    this.fitnessRules[ruleNumber + i] = Float.parseFloat(String.valueOf(line.next()));
                    
                }
                ruleNumber += this.RULE_LENGTH;
                geneNumber++;
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Popultation.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.RULE_LENGTH = (this.RULE_LENGTH * 2); // bounds plus one for the output
//        System.out.println(geneNumber);
//        System.out.println(RULE_LENGTH);
    }

    private void printFitnessRules()
    {
        Util.printArray(fitnessRules, this.RULE_LENGTH);
    }

    private void addBestSolutionBack()
    {
        int target = random.nextInt(POPULATION_NUM);
        this.population[target] = this.fittestSolution;
    }
}

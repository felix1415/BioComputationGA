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
    public final int POPULATION_NUM = 50;
    public final int GENE_NUM = 50;
    public final int GENERATIONS = 0;
    public final double CROSSOVER_NUM = 0.9;
    public final double MUTATION_NUM = 0.01;
    
    public final String OUTPUT_FILE = "graph_data.csv";
    public final String INPUT_FILE = "input_data.dsv";
    
    private Individual [] population;
    private Individual [] offspring;
    
    private final Random random;
    
    private double meanFitness;
    private int fittest;

    public Popultation()
    {
        this.population = new Individual[POPULATION_NUM];
        this.offspring = new Individual[POPULATION_NUM];
        this.random = new Random();
        
        File file = new File(this.OUTPUT_FILE);
        if (file.exists()){
            file.delete();
        } 
        
        for (int i = 0; i < POPULATION_NUM; i++)
        {
            this.population[i] = new Individual(GENE_NUM, i);
        }
        
        this.readInFile();
    }

    void run()
    {
        this.calculateFitness();
        this.printPopulation();
        this.print();
        for (int i = 0; i < GENERATIONS; i++)
        {
            this.selection();
            this.mutation();
            this.crossover();
            this.calculateFitness();
            this.printPopulation();
            this.print();
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
                offspring[i] = new Individual(population[parent1]);
            }
            else
            {
                offspring[i] = new Individual(population[parent2]);
            }
        }
        population = offspring.clone();
    }
    
    void crossover()
    {
        for (int i = 0; i < (POPULATION_NUM/2); i++)
        {
            if(random.nextDouble() > CROSSOVER_NUM)
            {
                int crossOverPoint = random.nextInt(GENE_NUM);
                int temp;
                for (int j = crossOverPoint; j < GENE_NUM; j++)
                {
                    temp = population[i*2].getGene()[j];
                    population[i*2].setGene(j, population[i*2 + 1].getGene()[j]);
                    population[i*2 + 1].setGene(j, temp);
                }
                
            }
        }
    }
    
    void mutation()
    {
        for (int i = 0; i < POPULATION_NUM; i++)
        {
            this.population[i].mutation(MUTATION_NUM);
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
    
    public void readInFile()
    {
        try
        {
            Scanner in = new Scanner(new File(INPUT_FILE));
            while(in.hasNext())
            {
                System.out.println(in.next());
            }
        } catch (FileNotFoundException ex)
        {
            Logger.getLogger(Popultation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

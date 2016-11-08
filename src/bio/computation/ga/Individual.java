/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bio.computation.ga;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author alexgray
 */
public class Individual
{
    
    private int[] gene;
    private int fitness;
    private Random r;
    private final int geneLength;

    public Individual(int gene)
    {
        this.gene = new int[gene];
        this.geneLength = gene;
        this.fitness = 0;
        this.r = new Random();
    }

    public Individual(Individual in)
    {
        this.gene = in.getGene().clone();
        this.geneLength = this.gene.length;
        this.fitness = 0;
        this.r = new Random();
    }

    public Individual(int[] gene)
    {
        this.gene = gene;
        this.geneLength = this.gene.length;
        this.fitness = 0;
        this.r = new Random();
    }
    
    public void incFitness()
    {
        this.fitness++;
    }
    
    public int getFitness()
    {
        return this.fitness;
    }

    public void setFitness(int fitness)
    {
        this.fitness = fitness;
    }

    public int getGeneLength()
    {
        return this.geneLength;
    }    

    public int[] getGene()
    {
        return this.gene;
    }
    
    public String getGeneAsString()
    {
        String geneString = "";
        for (int i = 0; i < gene.length; i++)
        {
            geneString = geneString + Integer.toString(gene[i]);
        }
        return geneString;
    }
    
    public void setGene(int[] geneIn)
    {
        this.gene = geneIn;
    }

    public void setGene(int indexIn, int geneIn)
    {
        this.gene[indexIn] = geneIn;
    }
    
    public void mutation(double mutationVal)
    {
        for (int i = 0; i < this.gene.length; i++)
        {
            if(mutationVal > r.nextDouble())
            {
                this.mutateGene(i);
            }
        }
    }
    
    public void mutateGene(int geneIndex)
    {
        //if gene is 1, gene can flip to be 0 or #
//        boolean splitter = this.r.nextBoolean();
        if(this.gene[geneIndex] == 1)
        {
            this.gene[geneIndex] = 0;
        }
        else
        {
            this.gene[geneIndex] = 1;
        }
    }

    public Random getRandom()
    {
        return r;
    }

    public void print()
    {
        System.out.print(Arrays.toString(this.gene));
        System.out.println("-" + this.fitness);
    }
    
    
    
    
    
}

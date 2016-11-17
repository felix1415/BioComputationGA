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
    
    private float[] gene;
    private int fitness;
    private Random r;
    private final int geneLength;

    public Individual(int gene)
    {
        this.gene = new float[gene];
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

    public Individual(float[] gene)
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

    public float[] getGene()
    {
        return this.gene;
    }
    
    public String getGeneAsString()
    {
        String geneString = "";
        for (int i = 0; i < gene.length; i++)
        {
            geneString = geneString + Float.toString(gene[i]);
        }
        return geneString;
    }
    
    public void setGene(float[] geneIn)
    {
        this.gene = geneIn;
    }

    public void setGene(int indexIn, float geneIn)
    {
        this.gene[indexIn] = geneIn;
    }
    
    public void mutation(double mutationVal, float mutationRange)
    {
        for (int i = 0; i < this.gene.length; i++)
        {
            if(mutationVal > r.nextDouble())
            {
                this.mutateGene(i, mutationRange);
            }
        }
    }
    
    public void flipGene(int geneIndex)
    {
        //if gene is 1, gene flip to be 0
        int geneAsInt = (int)this.gene[geneIndex];
        if(geneAsInt == 1)
        {
            this.gene[geneIndex] = (float) 0.0;
        }
        else
        {
            this.gene[geneIndex] = (float) 1.0;
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

    public void mutateGene(int geneIndex, float mutateRange)
    {
        System.out.println("bio.computation.ga.Individual.mutateGene()");
    }
    
    
    
    
    
}

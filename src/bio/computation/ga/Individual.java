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
    private final int index;

    public Individual(int gene, int index)
    {
        this.index = index;
        this.gene = new int[gene];
        this.fitness = 0;
        this.r = new Random();
        for (int i = 0; i < this.gene.length; i++)
        {
            this.gene[i] = this.r.nextInt(2);
        }
    }

    public Individual(Individual in)
    {
        this.index = in.getIndex() + 100;
        this.gene = in.getGene().clone();
        this.fitness = 0;
        this.r = new Random();
    }

    public int getIndex()
    {
        return index;
    }
    
    public int getFitness()
    {
        return fitness;
    }

    public void calcFitness()
    {
        this.fitness = 0;
        for (int i = 0; i < this.gene.length; i++)
        {
            if(this.gene[i] == 1)
            {
                this.fitness++;
            }
        }
    }

    public int[] getGene()
    {
        return gene;
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
                this.flipGene(i);
            }
        }
    }
    
    public void flipGene(int geneIndex)
    {
        if(this.gene[geneIndex] == 1)
        {
            this.gene[geneIndex] = 0;
        }
        else
        {
            this.gene[geneIndex] = 1;
        }
    }

    public void print()
    {
        System.out.print(this.index + "*");
        System.out.print(Arrays.toString(this.gene));
        System.out.println("-" + this.fitness);
    }
    
    
    
    
    
}

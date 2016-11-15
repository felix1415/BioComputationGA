/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bio.computation.ga;

import java.util.Arrays;

/**
 *
 * @author alexgray
 */
public class RuleSet extends Individual
{
    private final float [] fitnessRules;
    private final int RULE_LENGTH;
    private final int index;
    private final int NUMBER_OF_RULES;
    private final int NUMBER_OF_FITNESS_RULES;
    
    public RuleSet(int numberOfGenes, float [] rulesIn, int ruleLength, int indexIn)
    {
        super(numberOfGenes);
        this.fitnessRules = rulesIn;
        this.RULE_LENGTH = ruleLength;
        this.index = indexIn;
        this.NUMBER_OF_RULES = super.getGene().length / this.RULE_LENGTH;
        this.NUMBER_OF_FITNESS_RULES = this.fitnessRules.length / (this.RULE_LENGTH / 2);
        
        float [] array = new float[numberOfGenes];
        for (int i = 0; i < numberOfGenes; i++)
        {
            if (((i + 1) % (this.RULE_LENGTH - 1)) == 0)
            {
                array[i] = Math.round(super.getRandom().nextFloat());
            }
            else
            {
                array[i] = super.getRandom().nextFloat();
            }
        }
        super.setGene(array);
    }

    public RuleSet(RuleSet crsIn)
    {
        super(crsIn.getGene().clone());
        this.fitnessRules = crsIn.getFitnessRules().clone();
        this.RULE_LENGTH = crsIn.getRuleLength();
        this.NUMBER_OF_RULES = crsIn.getGene().length / crsIn.getRuleLength();
        this.NUMBER_OF_FITNESS_RULES = crsIn.getFitnessRules().length / (crsIn.getRuleLength() / 2);
        this.index = crsIn.getIndex() + 100;
    }

    public int getRuleLength()
    {
        return RULE_LENGTH;
    }
    
    public float [] getFitnessRules()
    {
        return this.fitnessRules;
    }
    
    public void boundsCheckArray()
    {
        int rules = 0;
        for (int i = 0; i < super.getGene().length; i++)
        {
            if (((i + 1) % (this.RULE_LENGTH - 1)) == 0)
            {
                rules++;
                if (rules == this.NUMBER_OF_RULES)
                {
                    break;
                }
                continue;
            }
            if(super.getGene()[i] > super.getGene()[i + 1])
            {
                float temp = super.getGene()[i];
                super.getGene()[i] = super.getGene()[i+1];
                super.getGene()[i + 1] = temp;
            }
            i++;
        }
    }
    
    public void calcFitness()
    {
        this.setFitness(0);
        this.boundsCheckArray();
        //for every data rule
//        System.out.println("fit: " + this.NUMBER_OF_FITNESS_RULES + " cad: " + this.NUMBER_OF_RULES);
        for (int data = 0; data < this.NUMBER_OF_FITNESS_RULES; data++)
        {
            //for every candidate rule
            for (int candidate = 0; candidate < this.NUMBER_OF_RULES; candidate++)
            {
                //if each bit int candidate matches or has a wildcard with a
                //data bit, matches stays true
                boolean matches = RuleSet.ruleMatches(this.getRule(candidate), this.getFitnessRule(data));

                if(matches)
                {
//                    this.incFitness();
                    if(Math.abs(this.getOutput(candidate)) == Math.abs(this.getFitnessOutput(data)))
                    {
                        this.incFitness();
                        break;
                    }
                    else
                    {
                        break;
                    }
                }
                else 
                {
                    break;
                }
            }
        }
    }
    
    public static boolean ruleMatches(float [] candidate, float [] data)
    {
        boolean matches = true;
        for (int i = 0; i < (candidate.length / 2); i++)
        {            
            if(data[i] < candidate[i*2] || data[i] > candidate[i*2 + 1])
            {
                return false;             
            }
        }
        return matches;
    }

    @Override
    public float[] getGene()
    {
        return super.getGene();
    }
    
    public float [] getRule(int rule)
    {
        int start = rule * (this.RULE_LENGTH - 1);
        int end = ((rule + 1) * (this.RULE_LENGTH - 1)) - 1;
        return Arrays.copyOfRange(super.getGene(), 
                start, 
                end);
    }
    
    public float  getOutput(int rule)
    {
        int end = ((rule + 1) * (this.RULE_LENGTH - 1)) - 1;  
        return super.getGene()[end];
    }
    
    public float [] getFitnessRule(int rule)
    {
        int start = rule * (this.RULE_LENGTH / 2);
        int end = ((rule + 1) * (this.RULE_LENGTH / 2)) - 1;
        return Arrays.copyOfRange(this.fitnessRules, 
                start, 
                end);
    }
    
    public float getFitnessOutput(int rule)
    {
        int end = ((rule + 1) * (this.RULE_LENGTH / 2)) - 1;
        return this.fitnessRules[end];
    }
    
    @Override
    public void mutateGene(int geneIndex, float mutateRange)
    {
        float currentGene = super.getGene()[geneIndex];
        float mutationValue = super.getRandom().nextFloat() * mutateRange; 
        boolean plusToGene = super.getRandom().nextBoolean();
        
        //if gene is output flip the gene
        if ((geneIndex + 1) % (this.RULE_LENGTH - 1) == 0)
        {
            super.flipGene(geneIndex);
            return;
        }
        
        if(plusToGene)
        {
            if ((currentGene + mutationValue) <= (float)1.0)
            {
                super.setGene(geneIndex, (currentGene + mutationValue));
            }
            else
            {
                super.setGene(geneIndex, (float) 1.0);
            }
        }
        else
        {
            if ((currentGene - mutationValue) >= (float)0.0)
            {
                super.setGene(geneIndex, (currentGene - mutationValue));
            }
            else
            {
                super.setGene(geneIndex, (float) 0.0);
            }
        }
    }

    @Override
    public void print()
    {   
        System.out.print(this.index);   
        System.out.println("*" + super.getFitness() + "-");
        for (int i = 0; i < this.NUMBER_OF_RULES; i++)
        {
            Util.printArray(this.getRule(i));
            System.out.println("@" + this.getOutput(i));
        }
    }

    private int getIndex()
    {
        return this.index;
    }
}

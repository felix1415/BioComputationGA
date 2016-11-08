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
public class CandidateRuleSet extends Individual
{
    private final int [] fitnessRules;
    private final int RULE_LENGTH;
    private final int index;
    private final int NUMBER_OF_RULES;
    private final int NUMBER_OF_FITNESS_RULES;
    
    public CandidateRuleSet(int numberOfGenes, int [] rulesIn, int ruleLength, int indexIn)
    {
        super(numberOfGenes);
        this.fitnessRules = rulesIn;
        this.RULE_LENGTH = ruleLength;
        this.index = indexIn;
        this.NUMBER_OF_RULES = super.getGene().length / this.RULE_LENGTH;
        this.NUMBER_OF_FITNESS_RULES = this.fitnessRules.length / this.RULE_LENGTH;
    }

    public CandidateRuleSet(CandidateRuleSet crsIn)
    {
        super(crsIn.getGene());
        this.fitnessRules = crsIn.getFitnessRules().clone();
        this.RULE_LENGTH = crsIn.getRuleLength();
        this.NUMBER_OF_RULES = crsIn.getGene().length / crsIn.getRuleLength();
        this.NUMBER_OF_FITNESS_RULES = crsIn.getFitnessRules().length / crsIn.getRuleLength();
        this.index = crsIn.getIndex() + 100;
    }

    public int getRuleLength()
    {
        return RULE_LENGTH;
    }
    
    public int [] getFitnessRules()
    {
        return this.fitnessRules;
    }
    
    public void calcFitness()
    {
//        printArray(super.getGene());
//        printArray(this.fitnessRules);
//        System.out.println("::" + Arrays.toString(super.getGene()));
//        System.out.println(Arrays.toString(this.fitnessRules));
        this.setFitness(0);
        for (int i = 0; i < this.NUMBER_OF_FITNESS_RULES; i++)
        {
            for (int j = 0; j < this.NUMBER_OF_RULES; j++)
            {
                if(Arrays.equals(this.getRule(j), this.getFitnessRule(i)))
                {
                    if(Arrays.equals(this.getResultOfRule(j), this.getResultOfFitnessRule(i)))
                    {
                        this.incFitness();
                        break;
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int[] getGene()
    {
        return super.getGene();
    }
    
    public int [] getWholeRule(int rule)
    {
        return Arrays.copyOfRange(super.getGene(), 
                rule * this.RULE_LENGTH, 
                ((rule + 1) * this.RULE_LENGTH));
    }
    
    public int [] getWholeFitnessRule(int rule)
    {
        return Arrays.copyOfRange(this.fitnessRules, 
                rule * this.RULE_LENGTH, 
                ((rule + 1) * this.RULE_LENGTH));
    }
    
    public int [] getRule(int rule)
    {
        return Arrays.copyOfRange(super.getGene(), 
                rule * this.RULE_LENGTH, 
                ((rule + 1) * this.RULE_LENGTH));
    }
    
    public int [] getFitnessRule(int rule)
    {
        return Arrays.copyOfRange(this.fitnessRules, 
                rule * this.RULE_LENGTH, 
                ((rule + 1) * this.RULE_LENGTH));
    }
    
    public int [] getResultOfRule(int rule)
    {
        return Arrays.copyOfRange(super.getGene(), 
                rule * this.RULE_LENGTH, 
                rule * this.RULE_LENGTH);
    }
    
    public int [] getResultOfFitnessRule(int rule)
    {
        return Arrays.copyOfRange(this.fitnessRules, 
                rule * this.RULE_LENGTH, 
                rule * this.RULE_LENGTH);
    }
    
    @Override
    public void mutateGene(int geneIndex)
    {
        //if gene is 1, gene can flip to be 0 or #
//        boolean splitter = super.r.nextBoolean();
        if(super.getGene()[geneIndex] == 1)
        {
            super.setGene(geneIndex, 0);
        }
        else
        {
            super.setGene(geneIndex, 1);
        }
    }

    @Override
    public void print()
    {
        System.out.print(this.index);   
        System.out.print("*" + super.getFitness() + "-");
        Util.printArray(this.getGene(), this.RULE_LENGTH);
    }

    private int getIndex()
    {
        return this.index;
    }
}

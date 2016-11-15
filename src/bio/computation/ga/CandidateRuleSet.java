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
        
        int [] array = new int[numberOfGenes];
        for (int i = 0; i < numberOfGenes; i++)
        {
            if (((i + 1) % this.RULE_LENGTH) == 0)
            {
                array[i] = super.getRandom().nextInt(2);
            }
            else
            {
                array[i] = super.getRandom().nextInt(3);
            }
        }
        super.setGene(array);
    }

    public CandidateRuleSet(CandidateRuleSet crsIn)
    {
        super(crsIn.getGene().clone());
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
        this.setFitness(0);
        //for every data rule
        for (int data = 0; data < this.NUMBER_OF_FITNESS_RULES; data++)
        {
            //for every candidate rule
            for (int candidate = 0; candidate < this.NUMBER_OF_RULES; candidate++)
            {
                //if each bit int candidate matches or has a wildcard with a
                //data bit, matches stays true
                boolean matches = true;
                for (int x = 0; x < this.getRule(candidate).length; x++)
                {
                    if(this.getRule(candidate)[x] != 2 && this.getRule(candidate)[x] != this.getFitnessRule(data)[x])
                    {
                        matches = false;
                        break;
                    }
                }
                if(matches)
                {
                    if(this.getOutput(candidate) == this.getFitnessOutput(data))
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
    
    public int validateRules(int [] rulesToValidate)
    {
        int numberOfValidatedRules = 0;
        //for every data rule
        for (int data = 0; data < rulesToValidate.length / this.RULE_LENGTH; data++)
        {
            //for every candidate rule
            for (int candidate = 0; candidate < this.NUMBER_OF_RULES; candidate++)
            {
                //if each bit int candidate matches or has a wildcard with a
                //data bit, matches stays true
                boolean matches = true;
                for (int x = 0; x < this.getRule(candidate).length; x++)
                {
                    if(this.getRule(candidate)[x] != 2 && this.getRule(candidate)[x] != this.getValidationRule(data, rulesToValidate)[x])
                    {
                        matches = false;
                        break;
                    }
                }
                if(matches)
                {
                    if(this.getOutput(candidate) == this.getValidationOutput(data, rulesToValidate))
                    {
                        numberOfValidatedRules++;
                        break;
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        return numberOfValidatedRules;
    }
    
    public static boolean matches(int [] one, int [] two)
    {
        boolean matches = true;
        for (int i = 0; i < one.length; i++)
        {
            if(one[i] != 2 && one[i] != two[i])
            {
                matches = false;
            }
        }
        return matches;
    }

    @Override
    public int[] getGene()
    {
        return super.getGene();
    }
    
    public int [] getRule(int rule)
    {
        int start = rule * this.RULE_LENGTH;
        int end = ((rule + 1) * this.RULE_LENGTH) - 1;
        return Arrays.copyOfRange(super.getGene(), 
                start, 
                end);
    }
    
    public int  getOutput(int rule)
    {
        int end = ((rule + 1) * this.RULE_LENGTH) - 1;
        return super.getGene()[end];
    }
    
    public int [] getFitnessRule(int rule)
    {
        int start = rule * this.RULE_LENGTH;
        int end = ((rule + 1) * this.RULE_LENGTH) - 1;
        return Arrays.copyOfRange(this.fitnessRules, 
                start, 
                end);
    }
    
    public int getFitnessOutput(int rule)
    {
        int end = ((rule + 1) * this.RULE_LENGTH) - 1;
        return this.fitnessRules[end];
    }
    
    public int [] getValidationRule(int rule, int [] array)
    {
        int start = rule * this.RULE_LENGTH;
        int end = ((rule + 1) * this.RULE_LENGTH) - 1;
        return Arrays.copyOfRange(array, 
                start, 
                end);
    }
    
    public int getValidationOutput(int rule, int [] array)
    {
        int end = ((rule + 1) * this.RULE_LENGTH) - 1;
        return array[end];
    }
    
    @Override
    public void mutateGene(int geneIndex)
    {
        int number = super.getGene()[geneIndex];
        while (super.getGene()[geneIndex] == number)
        {
            if (((geneIndex + 1) % this.RULE_LENGTH) == 0)
            {
                super.setGene(geneIndex, super.getRandom().nextInt(2));
            }
            else
            {
                super.setGene(geneIndex, super.getRandom().nextInt(3));
            }
        }
    }

    @Override
    public void print()
    {
//        System.out.println("bio.computation.ga.CandidateRuleSet.print()");
        System.out.print(this.index);   
        System.out.print("*" + super.getFitness() + "-");
        Util.printArray(this.getGene(), this.RULE_LENGTH);
    }

    private int getIndex()
    {
        return this.index;
    }
}

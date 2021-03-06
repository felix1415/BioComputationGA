/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bio.computation.ga;

import java.text.DecimalFormat;
import java.util.Arrays;

/**
 *
 * @author alexgray
 */
public class Util
{
    public static void printArray(float [] array, int ruleLength)
    {
        int numberOfRules = array.length / ruleLength;
        for (int i = 0; i < numberOfRules; i++)
        {
            for (int j = 0; j < ruleLength; j++)
            {
                if (((j + 1) % (ruleLength - 1)) == 0)
                {
                    System.out.print("/\\");
                    System.out.print(array[j + (i * ruleLength)]);
                    System.out.print("/\\");
                    break;
                }
                System.out.print(array[j + (i * ruleLength)] + "|");
            }
        }
        System.out.println();
    }

    public static String printArray(float[] gene)
    {
        String s = "";
        for (int i = 0; i < gene.length; i++)
        {
            System.out.print(gene[i]);
            System.out.print("|");
            if (((i + 1) % 2) == 0)
            {
                System.out.print("  ");
            }
            s += gene[i];
        }
        System.out.print("/\\");
        System.out.print("|");
        s += "|";
        return s;
    }
    
    public static void printRule(float[] gene, float output)
    {
        for (int i = 0; i < gene.length / 2; i++)
        {
            System.out.print(String.format("%01.3f", gene[i*2]));
            System.out.print(">input>");
            System.out.print(String.format("%01.3f", gene[i*2 + 1]));
            System.out.print(" | ");
        }
        System.out.println(" - " + output);
    }
}

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
            s += gene[i];
        }
        System.out.print("/\\");
        System.out.print("|");
        s += "|";
        return s;
    }
}

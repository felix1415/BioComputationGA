/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bio.computation.ga;

/**
 *
 * @author alexgray
 */
public class Util
{
    public static void printArray(int [] array, int ruleLength)
    {
        int numberOfRules = array.length / ruleLength;
        for (int i = 0; i < numberOfRules; i++)
        {
            for (int j = 0; j < ruleLength; j++)
            {
                System.out.print(array[j + (i * ruleLength)]);
            }
            System.out.print("|");
        }
        System.out.println();
    }

    static void printArray(int[] gene)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

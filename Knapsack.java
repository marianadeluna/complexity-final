import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/*
 * Final Project for CS306 - Knapsack
 * @author Mariana De Luna
 */

public class Knapsack
{
    // Integer representing infinity
    static int infinity = (int) Double.POSITIVE_INFINITY;

    public static int findMax(int array[])
    {
        int max = 0;
        int index = 0;
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] > max)
            {
                max = array[i];
                index = i;
            }
        }
        return index;
    }

    /*
     * O(nW) dynamic programming algorithm for 0-1 Knapsack
     */

    public static int knapsack(int capacity, int value[], int cost[], int item)
    {
        int table[][] = new int[item + 1][capacity + 1];
        int i; // current item in table
        int w; // current capacity in table
        for (i = 0; i <= item; i++)
        {
            for (w = 0; w <= capacity; w++)
            {
                if (i == 0 || w == 0)
                    table[i][w] = 0; // base case
                else if (value[i - 1] <= w)
                    table[i][w] = Math.max(cost[i - 1] + table[i - 1][w - value[i - 1]], table[i - 1][w]);
                else
                    table[i][w] = table[i - 1][w];
            }
        }
        // for (int[] row : table)
        // System.out.println(Arrays.toString(row));

        return table[item][capacity];
    }

    /*
     * 2-approximation for the Maximum Greedy Knapsack problem
     */

    public static int modifiedGreedyKnapsack(int A, int v[], int c[], int B)
    {
        // set arrays into a table
        int table[][] = new int[A][3];
        for (int i = 0; i < A; i++)
        {
            table[i][0] = v[i];
            table[i][1] = c[i];
            table[i][2] = v[i] / c[i]; // calculate ratios
        }
        // sort table based off ratios column (table[i][3])
        Arrays.sort(table, new Comparator<int[]>()
        {
            @Override
            // Compare values according to columns
            public int compare(final int[] entry1,
            final int[] entry2)
            {
                if (entry1[2] < entry2[2])
                    return 1;
                else
                    return -1;
            }
        });

        List<Integer> G = new ArrayList<Integer>();
        int L = B;
        for (int j = 0; j < A; j++)
        {
            if (table[j][1] <= L) // c[i]
            {
                G.add(j);
                L = L - table[j][1];
            }
            if (L < 0)
            {
                break;
            }
        }
        int sumOfValues = 0;
        for (int k = 0; k < G.size(); k++)
        {
            // add values that are in G
            sumOfValues = sumOfValues + table[G.get(k)][0];
        }
        // System.out.println(Arrays.deepToString(table));

        int maxIndex = findMax(v);
        int max = v[maxIndex];

        if (max > sumOfValues)
        {
            // returns the index of the max
            return maxIndex;
        }
        else
        {
            // TODO return an int[] or int
            for (int k = 0; k < G.size(); k++)
            {
                System.out.println("items in G: " + G.get(k));
            }
            return sumOfValues;
        }
    }

    /*
     * O(n^2 (vmax)) algorithm for knapsack
     * returns MinCost and Take
     */

    public static Object[] solveMaximumKnapsack(int A, int v[], int c[])
    {
        int n = A + 1;
        // set amax to an object with maximum value
        int indexMaxValue = findMax(v);
        int MinCost[][] = new int[n][n * v[indexMaxValue] + 1];
        boolean Take[][] = new boolean[n][n * v[indexMaxValue] + 1];

        // when target is 0, there is no cost
        for (int i = 1; i < n; i++)
        {
            MinCost[i][0] = 0;
        }

        // when t<=v(1), target t can be achieved by taking target 1
        for (int t = 1; t <= v[0]; t++)
        {
            MinCost[1][t] = c[0];
            Take[1][t] = true;
        }

        // when t>v(1), target cannot be reached with only 1 object available
        for (int t = v[0] + 1; t <= n * v[indexMaxValue]; t++)
        {
            MinCost[1][t] = infinity;
            Take[1][t] = false;
        }

        // set target values in first row
        for (int t = 1; t <= n * v[indexMaxValue]; t++)
        {
            MinCost[0][t] = t;
        }

        for (int i = 2; i < n; i++)
        {
            for (int t = 1; t <= n * v[indexMaxValue]; t++)
            {
                int nextT = Math.max(0, t - v[i - 1]);
                if (MinCost[i - 1][t] <= c[i - 1] + MinCost[i - 1][nextT])
                {
                    // don't include object i
                    MinCost[i][t] = MinCost[i - 1][t];
                    Take[i][t] = false;
                }
                else
                {
                    // include object i
                    if (MinCost[i - 1][nextT] == infinity)
                    {
                        MinCost[i][t] = infinity;
                        Take[i][t] = true;
                    }
                    else
                    {
                        MinCost[i][t] = c[i - 1] + MinCost[i - 1][nextT];
                        Take[i][t] = true;
                    }
                }
            }
        }

        // for (int[] row : MinCost)
        // System.out.println(Arrays.toString(row));
        // System.out.println();
        // for (boolean[] row : Take)
        // System.out.println(Arrays.toString(row));

        return new Object[] { MinCost, Take };
    }

    /*
     * algorithm that uses MinCost and Take table from solveMaximumKnapsack()
     * returns solution and optimal value
     */

    public static int constructMaxKnapsackSolution(int A, int n, int v[], int c[], int B, int MinCost[][], boolean Take[][])
    {
        // set amax to an object with maximum value
        int indexMaxValue = findMax(v);
        int optimalValue = n * v[indexMaxValue];
        while (optimalValue > 0 && MinCost[n - 1][optimalValue] > B)
        {
            optimalValue = optimalValue - 1;
        }
        List<Integer> solution = new ArrayList<Integer>();
        int i = A;
        int t = optimalValue;
        while (i > 0 && t > 0)
        {
            if (Take[i][t] == true)
            {
                solution.add(i);
                t = t - v[i - 1];
            }
            i = i - 1;
        }
        return optimalValue;
    }

    /*
     * FTPAS based on scaling with the optimal dynamic programming algorithm from MinCost problem
     */

    public static Object[] knapsackApproxScheme(int A, int v[], int c[], float F)
    {
        int scaled[] = new int[A];

        for (int i = 0; i < A; i++)
        {
            scaled[i] = (int) Math.floor(v[i] / F);
        }
        Object[] result = solveMaximumKnapsack(scaled.length, scaled, c);
        return result;
    }

    

    public static void main(String[] args)
    {
        // running O(nW) knapsack algorithm
        int cost[] = new int[100]; // value
        int value[] = new int[100]; // weight
        for (int i = 0; i < cost.length; i++)
        {
            cost[i] = (int) (Math.random() * 100) + 1;
            value[i] = (int) (Math.random() * 100) + 1;
        }
        int B = 100;

        long startTime = System.currentTimeMillis();
        int maxValue = knapsack(B, value, cost, cost.length);
        long endTime = System.currentTimeMillis();
        System.out.println("knapsack maxValue: " + maxValue + " took " + (endTime - startTime) + " milliseconds");
        System.out.println("---------------------------------");

        // running 2-approximation algorithm
        startTime = System.currentTimeMillis();
        int approx = modifiedGreedyKnapsack(value.length, value, cost, B);
        endTime = System.currentTimeMillis();
        System.out.println("2-approximation: " + approx + " took " + (endTime - startTime) + " milliseconds");

        System.out.println("---------------------------------");
        // running O(n^2 v(max)) algorithm
        startTime = System.currentTimeMillis();
        Object[] obj = solveMaximumKnapsack(value.length, value, cost);
        int optimalValue = constructMaxKnapsackSolution(value.length, value.length + 1, value, cost, B, (int[][]) obj[0], (boolean[][]) obj[1]);
        endTime = System.currentTimeMillis();
        System.out.println("MinCost: " + optimalValue + " took " + (endTime - startTime) + " milliseconds");

        System.out.println("---------------------------------");
        // running approximation scheme for knapsack
        float F = 1;
        startTime = System.currentTimeMillis();
        Object[] scaledTables = knapsackApproxScheme(value.length, value, cost, F);
        int optimalValueWithScaledValues = constructMaxKnapsackSolution(value.length, value.length + 1, value, cost, B, (int[][]) scaledTables[0], (boolean[][]) scaledTables[1]);
        endTime = System.currentTimeMillis();
        System.out.println("MinCost with scaling: " + optimalValueWithScaledValues + " took " + (endTime - startTime) + " milliseconds");

    }
}

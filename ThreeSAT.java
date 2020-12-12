import java.util.ArrayList;
import java.util.List;

/*
 * Final Project for CS306 - 3SAT
 * @author Mariana De Luna
 */

public class ThreeSAT
{

    /*
     * Algorithm for GSAT
     */
    
    public static List<Clause> createClauses(int numVariables) {
        List<Clause> clauses = new ArrayList<Clause>();
        for (int i=1; i <= numVariables - 2 ; i++) {
            Variable v1 = new Variable(i,false);
            Variable v2 = new Variable(i+1,false);
            Variable v3 = new Variable(i+2,false);
            Clause clause1 = new Clause(v1,v2,v3);
            v1 = new Variable(i,true);
            Clause clause2 = new Clause(v1,v2,v3);
            v2 = new Variable(i,true);
            Clause clause3 = new Clause(v1,v2,v3);
            v3 = new Variable(i,true);
            Clause clause4 = new Clause(v1,v2,v3);
            clauses.add(clause1);
            clauses.add(clause2);
            clauses.add(clause3);
            clauses.add(clause4);
        }
        System.out.println("number of clauses: " + clauses.size());
        return clauses;
    }

    
    
    public static void gsat(int maxTries, int maxFlips, int numVariables)
    {
        boolean variable[] = new boolean[numVariables + 1]; //variables that will change
        List<Clause> clauses = createClauses(numVariables);
        // start with random truth assignment
        for (int i = 1; i < numVariables + 1; i++)
        {
            variable[i] = true;
            System.out.println("variable: " + i + " " + variable[i]);
        }
        
        for (int i = 1; i < clauses.size(); i++)
        {
            // TODO check truth assignments for all clauses
            Variable varToChange = clauses.get(i).sameIndex(i);
            if (varToChange !=  null) {
                
            }         
        }
 
        /*
        // TODO flip the variable that would lead to the greatest reduction in unsatisfied clauses
        for (int j=0; j < maxFlips; j++)
        {
            for (int i = 1; i < numVariables; i++)
            {
                //set var[1] to false
                variables[i] = false;
                int satisfiedNum = 0; //num of satisfied clauses
                //check each clause and set counter for sat
                for (int k=0; k < clauses.size();k++) {
                    clauses.get(k);
                }
            }
        }
        */
        // TODO repeat this process for some number of flips: 50

        // TODO and then repeat the whole process for a number of max tries and return the variable truth assignments
        // that had the MOST/ MAX satisfied clauses in total
    }
    
    /*
     * DPLL algorithm for 3SAT
     */
    
 

    public static void main(String[] args)
    {
        // running algorithm for GSAT
       // gsat(25, 25, 50);
        
        // running algorithm for DPLL

    }
}

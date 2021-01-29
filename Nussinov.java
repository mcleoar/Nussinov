package nussinov;

import java.util.*;


/**
 * This class will take a given RNA sequence and calculate a Nussinov table as well as output an optimal structure
 * in vienna format.
 * @author Camden Leonard
 */
public class Nussinov {

    static String seq;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
       /* Scanner in = new Scanner(System.in);                                    //Initalization
        Slot[][] table;

        do{                                                                     //Get input until it is valid
            System.out.print("Enter sequence (A, U, C, or G Bases): ");

            seq = in.nextLine();
            seq = seq.toUpperCase();

            table = calcTable(seq);
        }while(table == null);
        
        System.out.println(printTable(table));                                                      //Output Nussinov Table
        
        Stack<Slot> stack = new Stack<>();
        stack.push(table[0][seq.length()-1]);
        backtrack(table, stack);                                                //Backtrack and mark pairs

        
        System.out.println(vienna(seq, table));                                 //convert pairs to vienna format and output
        System.out.println(seq);*/


        
       
       //The commented code above can be ignored. This is just an alternative way of running the program with no UI implemented.
        

    }
    
    /**
     * This method will use the Nussinov table to represent an optimal folded RNA strand in vienna format and return it as a StringBuilder.
     * @param seq RNA sequence given by user.
     * @param table Generated Nussinov table.
     * @return vienna format of folded RNA
     */
    public static StringBuilder vienna(String seq, Slot[][] table){
        StringBuilder vienna = new StringBuilder("");
        for(int i = 0; i < seq.length(); i++)                                   //Initalize StringBuilder to only contains dots.
            vienna.append(".");

        for(int i = 0; i < seq.length(); i++){                                  //Check all table values for marked base pairs and represent them with parenthises in the proper base location. 
            for(int j = 0; j < seq.length(); j++){
                if(table[i][j].pair){
                    vienna.setCharAt(i, '(');
                    vienna.setCharAt(j, ')');
                }
            }
        }
        
        return vienna;
    }
    
    /**
     * This method will use the RNA sequence and the Nussinov algorithm to fill in the Nussinov table with optimal values. 
     * @param seq RNA sequence given by user.
     * @return 
     */
    public static Slot[][] calcTable(String seq){

        for(int i = 0; i < seq.length(); i++){                                                                              //Check that the sequence only contains 'A', 'U', 'C', or 'G'
            if(seq.charAt(i) == 'A' || seq.charAt(i) == 'U' || seq.charAt(i) == 'C' || seq.charAt(i) == 'G'){}
            else{
                System.out.println("That sequence contained an invalid Base, Try again.");
                return null;
            }

                
        }

        int tableSize = seq.length();                                           //Initalize the table 
        Slot[][] table = new Slot[tableSize][tableSize];
        Slot case1, case2, case3, case4;
       
       for(int i = 0; i < tableSize; i++){
           for(int j = 0; j < tableSize; j++)
               table[i][j] = new Slot(0, i, j);
       } 

        
       for(int g = 1; g < tableSize; g++){                                      //Loop for each diagonal checked through
            
            for(int i = 0, j = g; i < tableSize && j < tableSize; i++, j++){    //Loop to move diagonally through the table one slot at a time/
                case1 = new Slot(-1, i, j); 
                case2 = new Slot(-1, i, j);
                case3 = new Slot(-1, i, j);
                case4 = new Slot(-1, i, j);

                if(isBasePair(seq.charAt(i), seq.charAt(j)))                    //Case 1, Base pair condition checked then value of [i+1, j-1] + 1 is stored if condition is true
                    case1.value = table[i+1][j-1].value + 1;
                
                case2.value = table[i+1][j].value;                              //Case 2, [i+1][j] value stored
                                
                case3.value = table[i][j-1].value;                              //Case 3, [i][j-1] value stored
                
                for(int k = i+1; k < j; k++){                                   //Case 4 (Birfurcation), checks all values such that i < k < j, then stores the maximum of those choices
                    if(case4.value < table[i][k].value + table[k+1][j].value)
                        case4.value = table[i][k].value + table[k+1][j].value;
                    
                }
                
                table[i][j].value = Slot.max(case1, Slot.max(case2, Slot.max(case3, case4))).value;  //Maximum value from 4 cases is taken and is placed into the table at [i,j]
   
            }
        }
    
       return table;                                                            
    }
    
    /**
     * This method contains a basic switch logic structure that will determine if 2 bases are base pairs.
     * @param x First base.
     * @param y Second base.
     * @return True if it is a base pair and false otherwise.
     */
    public static boolean isBasePair(char x, char y){
        
        boolean check = false;
        
        switch(x){
         case 'A' :                                                             //A to U base pair
            if(y == 'U')
                check = true;
            break;
         case 'U' :                                                             //U to A Base pair
             if(y == 'A')
                 check = true;
             break;
         case 'C' :                                                             //C to G Base pair
            if(y == 'G')
                check = true;
            break;
         case 'G' :                                                             //G to C Base pair
            if(y == 'C')
                check = true;
            break;
         default :                                                              //Any other letters are caught here as invalid bases
            System.out.println("Invalid base");
      }

        return check;
        
    }
    
    /**
     * This method will print a formatted Nussinov table to a string and return it.
     * @param table Generated Nussinov Table.
     * @return a formatted version of the Nussinov table as a string.
     */
    public static String printTable(Slot[][] table){
        String tableText = "";
        
        for(int i = 0; i < table.length; i++){
            for(int j = 0; j < table.length; j++)
                tableText += (String.format("%4d", table[i][j].value));                    //format output so spacings of table is maintained.
            tableText += ('\n');
        }
        
        return tableText;

    }
    
    
    /**
     * This method will use a stack to backtrack through the Nussinov table and find an optimal solution.
     * @param table Generated Nussinov Table.
     * @param stack Stack used to backtrack the table.
     */
    public static void backtrack(Slot[][] table, Stack<Slot> stack){
        Slot check = stack.pop();                                               //take the top value of the stack (on first call this will be the optimal value you want to backtrack from).
        
        if(check.i >= check.j){                                                                         //Make sure we are in the proper portion of the table
        }   
                                                                                                         //If value matches:    
        else if(table[check.i+1][check.j].value == table[check.i][check.j].value)                       //Case 2,
            stack.push(table[check.i+1][check.j]);
        
        else if(table[check.i][check.j-1].value == table[check.i][check.j].value)                       //Case 3,
            stack.push(table[check.i][check.j-1]);
        
        else if(table[check.i+1][check.j-1].value + 1 == table[check.i][check.j].value){                //Case 1,
            table[check.i][check.j].pair = true;
            stack.push(table[check.i+1][check.j-1]);
        }
        
        else{                                                                                           //Case 4,
            for(int k = check.i+1; k < check.j; k++){                                                   // then that slot is pushed onto the stack to be backtracked in the next recursive call.
                if(table[check.i][k].value + table[k+1][check.j].value == table[check.i][check.j].value){
                    stack.push(table[check.i][k]);                                                      //Two values pushed onto stack because Bifurcation occured.
                    stack.push(table[k+1][check.j]);
                    break;
                }
                    
            }
               
        }                                                                                               
        
        if(!stack.empty())                                                      //When the stack is empty then the method has backtracked to a base case and is finsihed so this method will exit.
            backtrack(table, stack);
            
    }
    
}

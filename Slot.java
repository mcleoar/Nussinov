/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nussinov;

/**
 *  This class is used to represent a position in the 2d Nussinov matrix.
 * @author Camden Leonard
 */
public class Slot implements Comparable{
    int value, i, j;                        //i and j are the y and x positions in the matrix  
                                            //Note: [0,0] is in the top left of the matrix. I is numbered 1 to n in a downward direction and J is numbered 1 to n in a righthand dirtection.
    boolean pair = false;                   //This variable will be true if i and j correspond to a base pair in the i and j positions of the RNA sequence.
    
    /**
     * Basic constructor
     * @param value Number assigned by Nussinov algorithm
     * @param i Position variable
     * @param j Position Variable
     */
    public Slot(int value, int i, int j){
        this.value = value;
        this.i = i;
        this.j = j;
    }
    
    /**
     * Simple max method that returns the max of the 2 Slot objects given.
     * @param x Slot 1
     * @param y Slot 2
     * @return Slot with maximum value
     */
    public static Slot max(Slot x, Slot y){
        if(x.value < y.value)
            return y;
        else return x;
    }
    
    /**
     * This method will change the assign i and j value to the given values
     * @param i Position variable
     * @param j Position variable
     */
    public void assignPos(int i, int j){
        this.i = i;
        this.j = j;
    }
    
    /**
     * This method will copy the contents of the Slot object sent in, to the Slot object calling the method.
     * @param x Slot to be copied from
     */
    public void copy(Slot x){
        this.value = x.value;
        this.i = x.i;
        this.j = x.j;
        
    }
    
    
    public void print(){
        System.out.println("[Value: " + this.value + "; i: " + this.i + "; j: " + this.j + "]");
    }
    
    /**
     * Method used to compare 2 Slot objects using their value variables.
     * @param o Object compared  
     * @return 1 if greater than o, 0 if equal to o, -1 if less than o
     */
    @Override
    public int compareTo(Object o) {
        Slot sec = (Slot)o;
        if(this.value < sec.value)
            return -1;
        if(this.value == sec.value)
            return 0;
        else
            return 1;
    }
    
    
}

package com.scu.coen383.team2.pagingsimulator;

import java.util.Random;

/**
 * COEN383 GROUP 2
 * 
 * An object representing a Process which has a runtime, memory size,
 * name, and arrival time
 */

public class Process implements Cloneable
{
    public int duration;
    //private int current; //the most recently accessed page
    public int current; //the most recently accessed page
    public int size;
    public double start;
		public double arrival;
    //public int arrival;
    public String name;
    public boolean inMemory;
    
    public Process(int duration, int size, String name, double arrival, double start)
		//public Process(int duration, int size, char name, int arrival, double start)
    {
        this.duration = duration;
        this.size = size;
        this.name = name;
        this.arrival = arrival;
        this.start = start;
        this.current = -1; //init to -1
				//this.current = 0;
        inMemory = false;
    }
        
    @Override 
    public Object clone() throws CloneNotSupportedException 
    {
        return new Process(this.duration, this.size, this.name, this.arrival, this.start);
    }
    
    public String toString() {
    	return ("Arrival Time: " + arrival + "\tName: " + name +  "\tSize: " + size + "\tDuration: " + duration );
    }
    
    public int NextPage(){
			if(current == -1) {
				return 0;
			}
			//curr != -1
			Random rand = new Random();
			int j;
			// Generates a random r between 0 and process size (inclusive)
			int r = rand.nextInt(size);
			// Takes 70% of process size
			double r70percent = size*0.7;
			if (0 <= r && r < r70percent){
				// Generates a random Î”i to be -1, 0 or 1
				int delta = rand.nextInt(3)-1; //-1, 0 or 1
				if (delta<0 && current==0){
					j = size-1;
				}
				else {
					j = current+ delta;
				}
				return j;
			}
			else{
				int delta = rand.nextInt(size-2)+2;
				j = delta + current;
				if (j > size-1){
					j = j-size;
				}
				return j;
			}
		}
    
    public boolean equals(Object other) {
    	if(this.name == ((Process) other).name)
    		return true;
    	else
    		return false;
		}
    
}

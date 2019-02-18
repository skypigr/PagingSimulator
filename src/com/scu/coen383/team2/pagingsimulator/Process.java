package com.scu.coen383.team2.pagingsimulator;

import java.util.Random;

/**
 * COEN383 GROUP 2
 * 
 * Customize a class named Process which implements the Cloneable interface and override the clone()
 *
 */

public class Process implements Cloneable {
	//Include the info of name, duration time, size, start time, arrival time, the most recently accessed page and whether is it in memory
    public int duration;
    public int size;
	public String name;
    public double start;
    public double arrival;
	//Record the most recently accessed page within the page
	public int current;
	public boolean inMemory;

	//Constructor of this class
	public Process(int duration, int size, String name, double arrival, double start) {
        this.duration = duration;
        this.size = size;
        this.name = name;
        this.arrival = arrival;
        this.start = start;
        //initialize the current to -1 means all of the pages are not been accessed yet
        this.current = -1;
        inMemory = false;
    }

    //Use 70% probability rule to decide which page within this process will be the next page
    public int NextPage() {
		//Make sure each process begins with its page0
		if (current == -1) {
			return 0;
		}
		Random rand = new Random();
		int nextPageNumber;
		//Generates a random number r within [0, size)
		int r = rand.nextInt(size);
		//Check r is within [0, 0.7 * size) or not, if it is, then satisfy the 70% probability
		if (0 <= r && r < size * 0.7) {
			//Generates random delta value from -1, 0, or 1;
			int del = rand.nextInt(3) - 1;
			//Use the last page as the next if delta plus current is -1, like a cycle
			if (del < 0 && current == 0){
				nextPageNumber = size - 1;
			} else {
				nextPageNumber = current + del;
			}
			return nextPageNumber;
		} else {
			//0 <= nextPageNumber <= current - 2 or current + 2 <= nextPageNumber <= size - 1
			//if r are not within [0, 0.7*size), randomly generate delta within [2, size - 2]
			int delta = rand.nextInt(size - 3) + 2;
			nextPageNumber = delta + current;
			//Take mode operation is nextPageNumber is bigger than size - 1
			if (nextPageNumber > size - 1){
				nextPageNumber = nextPageNumber % size;
			}
			return nextPageNumber;
		}
	}
    
    public boolean equals(Object other) {
		if (this.name == ((Process) other).name) {
			return true;
		} else {
			return false;
		}
	}

	public String toString() {
		return "Arrival Time: " + arrival + "\tName: " + name +  "\tSize: " + size + "\tDuration: " + duration;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return new Process(this.duration, this.size, this.name, this.arrival, this.start);
	}
}

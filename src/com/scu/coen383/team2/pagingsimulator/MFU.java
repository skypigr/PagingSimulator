package com.scu.coen383.team2.pagingsimulator;

import java.util.LinkedList;

/**
 * COEN383 GROUP 2
 *
 * MFU pager
 */

public class MFU extends Pager
{
	public MFU(LinkedList<MemoryPage> memory, LinkedList<Process> process) {
		super(memory, process);
	}

	@Override
	public int run(){
            //Index in main memory to remove MemoryPage. -1 means an error has occured.
            int remove = -1;
            double highest = Integer.MIN_VALUE;
            //Loops through all entry in main memory
            for (int x = 0; x < memory.size(); x++){
                //Takes MemoryPage in memory[x]
                MemoryPage temp = memory.get(x);
                //Checks if MemoryPage is not empty
                if (temp.name != "."){
                    //If frequency in MemoryPage[x] is higher than highest, set highest to frequency. 
                    if(temp.frequency > highest){
                        highest = temp.frequency;
                        //Index to remove is x
                        remove = x;
                    }
                }
            }
            return remove;
	}

	@Override
	public String getName() {
		return "MFU";
	};
}

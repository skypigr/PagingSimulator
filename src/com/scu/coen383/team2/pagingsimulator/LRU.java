package com.scu.coen383.team2.pagingsimulator;

import java.util.LinkedList;

/**
 * COEN383 GROUP 2
 *
 * LRU Pager
 */

public class LRU extends Pager
{
    
	public LRU(LinkedList<MemoryPage> memory, LinkedList<Process> process) {
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
                    //If lastAccessed in MemoryPage[x] is greater than highest, set highest to lastAccessed. 
                    if(temp.lastAccessed > highest){
                        highest = temp.lastAccessed;
                        //Index to remove is x
                        remove = x;
                    }
                }
            }
            return remove;
        }

	@Override
	public String getName() {
		return "LRU";
	};
}

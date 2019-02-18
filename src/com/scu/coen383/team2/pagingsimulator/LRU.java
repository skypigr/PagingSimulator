package com.scu.coen383.team2.pagingsimulator;

import java.util.LinkedList;

/**
 * COEN383 GROUP 2
 *
 * LRU(Least recently used) Pager
 */

public class LRU extends Pager
{
    
	public LRU(LinkedList<MemoryPage> memory, LinkedList<Process> process) {
		super(memory, process);
	}

	@Override
        public int run(){
            int remove = -1;//initialize as -1, if it couldnt find, will remain -1
            double highest = Integer.MIN_VALUE;
            //go through memory to find the largest lastAccessed
            for (int i = 0; i < memory.size(); i++){
                MemoryPage memoryAtI = memory.get(i);
                if (memoryAtI.name != "." && memoryAtI.lastAccessed > highest){
                    highest = memoryAtI.lastAccessed;
                    remove = i;
                }
            }
            return remove;
        }

	@Override
	public String getName() {
		return "LRU";
	};
}

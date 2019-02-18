package com.scu.coen383.team2.pagingsimulator;

import java.util.LinkedList;

/**
 * COEN383 GROUP 2
 *
 * MFU(Most Frequent Used) pager
 */

public class MFU extends Pager
{
	public MFU(LinkedList<MemoryPage> memory, LinkedList<Process> process) {
		super(memory, process);
	}

	@Override
	public int run(){

            int remove = -1;//initialize as -1, if it couldnt find, will remain -1
            double highest = Integer.MIN_VALUE;
            //go through memory to find the highest frequency
            for (int i = 0; i < memory.size(); i++){
                MemoryPage memoryAtI = memory.get(i);
                if (memoryAtI.name != "." && memoryAtI.frequency > highest){
                    highest = memoryAtI.frequency;
                    remove = i;
                }
            }
            return remove;
	}

	@Override
	public String getName() {
		return "MFU";
	};
}

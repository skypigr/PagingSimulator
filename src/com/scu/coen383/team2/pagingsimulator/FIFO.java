package com.scu.coen383.team2.pagingsimulator;

import java.util.LinkedList;

/**
 * COEN383 GROUP 2
 *
 * FIFO(First In First Out) Pager
 */

public class FIFO extends Pager
{
	
	public FIFO(LinkedList<MemoryPage> memory, LinkedList<Process> process) {
		super(memory, process);
	}

	@Override
	public int run() {
		int remove = -1;//initialize as -1, if it couldnt find, will remain -1
		double highest = -1;
		//go through memory, find the highest runtime
		for (int i = 0; i < memory.size(); i++){
				MemoryPage memoryAtI = memory.get(i);
				if (memoryAtI.name != "." && memoryAtI.runTime > highest){
					highest = memoryAtI.runTime;
					remove = i;
				}
		}
		if(remove == -1) {
			System.out.println("FIFO error: can not find largest runtime");
		}
		return remove;
	}

	@Override
	public String getName() {
		return "FIFO";
	};
}


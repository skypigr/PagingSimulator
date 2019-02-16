package com.scu.coen383.team2.pagingsimulator;

import java.util.LinkedList;

/**
 * COEN383 GROUP 2
 *
 * FIFO Pager
 */

public class FIFO extends Pager
{
	
	public FIFO(LinkedList<MemoryPage> memory, LinkedList<Process> process) {
		super(memory, process);
	}

	@Override
        //Page with the highest runTime should be removed because it probably is the process that came first
	public int run()
	{
		//Index in main memory to remove MemoryPage. -1 means an error has occured.
		int remove = -1;
		double highest = -1; //0;
		//Loops through all entry in main memory
		for (int x = 0; x < memory.size(); x++){
				//Takes MemoryPage in memory[x]
				MemoryPage temp = memory.get(x);
				//Checks if MemoryPage is not empty
				if (temp.name != "."){
						//If runTime in MemoryPage[x] is greater than highest, set highest to runTime. 
						if(temp.runTime > highest){
								highest = temp.runTime;
								//Index to remove is x
								remove = x;
						}
				}
		}
		if(remove == -1) {
			System.out.println();
		}
		return remove;
	}

	@Override
	public String getName() {
		return "FIFO";
	};
}


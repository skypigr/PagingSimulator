package com.scu.coen383.team2.pagingsimulator;
import java.util.LinkedList;

/**
 * COEN383 GROUP 2
 *
 * LFU(Least-frequently used) Pager
 */

public class LFU extends Pager
{   
	public LFU(LinkedList<MemoryPage> memory, LinkedList<Process> process) {
		super(memory, process);
	}

	@Override
	public int run(){
            int remove = -1;//initialize as -1, if it couldnt find the largest runtime, will remain -1
            double lowest = Integer.MAX_VALUE;
            //go through memory, find lowest frequency
            for (int i = 0; i < memory.size(); i++){
                MemoryPage memoryAtI = memory.get(i);
                if (memoryAtI.name != "." && memoryAtI.frequency < lowest){
                    lowest = memoryAtI.frequency;
                    remove = i;
                }
            }
            return remove;
	}

	@Override
	public String getName() {
		return "LFU";
	};
}

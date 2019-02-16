package com.scu.coen383.team2.pagingsimulator;

import java.util.LinkedList;
import java.util.Random;

/**
 * COEN383 GROUP 2
 *
 * Random Pick Pager
 */

public class RP extends Pager
{
	public RP(LinkedList<MemoryPage> memory, LinkedList<Process> process) {
		super(memory, process);
	}

	@Override
	public int run(){
		Random rand = new Random();
		//int remove = rand.nextInt(99);
		int remove = rand.nextInt(MEMORY_SIZE_MB);
		return remove;
	}

	@Override
	public String getName() {
		return "Random Pick";
	};
}

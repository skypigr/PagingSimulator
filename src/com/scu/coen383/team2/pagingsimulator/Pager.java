package com.scu.coen383.team2.pagingsimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * COEN383 GROUP 2
 * 
 * Simulates Paging Algorithm
 */

public abstract class Pager 
{
	public LinkedList<Process> queue,  arrivedQueue;
	public LinkedList<MemoryPage> memory;
	public static final int MEMORY_SIZE_MB = 100;
	public int swapped = 0;
	public int hit = 0;
	public int miss = 0;
	public double ratio = 0;
	public int numReferencedPages = 0;
	/**
	 * Use a particular swapping algorithm to get the index of the next allocation
	 * @param memory LinkedList<Process> representing the memory space
	 * @param size int : size of the process to be allocated
	 * @param start int: index to start searching (ignored by FirstFit, BestFit)
	 * @return int : the index that the next process will be allocated to
	 */
	public Pager(LinkedList<MemoryPage> memory, LinkedList<Process> process) {
		this.memory = new LinkedList<MemoryPage>( memory);
		this.queue = new LinkedList<Process>(process);
		this.arrivedQueue = new LinkedList<Process>();
	}

	public abstract int run();
	public abstract String getName();

	/**
	 * Simulate swapping using the given Swapping Algorithm and collect statistics
	 * teminate when 1 min reached
	 * @param processQueue Queue<Process> in FCFS order 
	 * @return int : number of processes that were successfully swapped in 
	 */
	public void simulate() throws CloneNotSupportedException {
		int free = memory.size();
		double time = 0.0;
		boolean firstTime = true;
		while (time <= 60) {

			if(!firstTime) {
				updateAll();
				free += deallocateProcess(time);
			}

			while (!queue.isEmpty() && queue.element().arrival <= time && free >= 4) { //add arrived processes to wait queue
				//while (queue.element().arrival <= time && free >= 4) {
				arrivedQueue.add(queue.removeFirst());
			}

			if(arrivedQueue.size() > 0) {
				firstTime=false;
			}
			
			for (Process p : arrivedQueue) {
				int frame = p.NextPage();
				//int frame = p.current == -1 ? 0 : p.NextPage();

				if (free >= 4) {//add existing or new process from arrivedQueue queue
					if (checkProcess(p, frame)) { //mem hit
						updateMemPage(p, frame);
						hit++;
					} else { //miss
						miss++;
						if(p.current == -1) { //p not start yet
							//if(frame == 0) {
							p.start=time; //set start time
						}
						addProcPage(p, frame);
						free--;
						System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Enter,\t\t Size:" + p.size + ",\tService Duration:" + p.duration);
					}
					p.current = frame; //set curr frame
					numReferencedPages++;
				} else if(p.start < time) { //0 < free < 4 -> only work for started processes
					if(free > 0){
						if (checkProcess(p, frame)) {
							//memory.indexOf(p.name);
							updateMemPage(p, frame);
							hit++;
						} else { //!checkProc -> p.frame not in mem
							/*if(frame == 0) {
								p.start=time;
							}*/
							addProcPage(p, frame);
							free--;
							miss++;
						}
					} else { //free == 0
						//else if( free == 0 ){
						int index = run(); //call the abstract method run()
						replace(index, p, frame);
						swapped++;
						System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Exit,\t\t Size:" + p.size + ",\t Service Duration:" + p.duration);
					}
					p.current = frame; //set curr frame
					numReferencedPages++;
				}
			}
			time = time + 0.10;
			//time = (double) (time + 0.10);
			BigDecimal bd = new BigDecimal(time);
			bd=bd.round(new MathContext(3));
			printMemoryMap();
			time = bd.doubleValue();
		}
		
		ratio = (double) hit/miss;
	}

	/**
	 * Simulate swapping using the given Swapping Algorithm and collect statistics
	 * terminate when 100 pages referenced
	 * @param processQueue Queue<Process> in FCFS order
	 * @return int : number of processes that were successfully swapped in
	 */
	public void simulate2() throws CloneNotSupportedException {
		int free = memory.size();
		double time = 0.0;
		boolean firstTime = true;
		while (numReferencedPages < 100) {
			
			if(!firstTime) {
				updateAll();
				free += deallocateProcess(time);
			}
			
			while (queue.element().arrival <= time) { //add arrived processes to wait queue
																								//while (queue.element().arrival <= time && free >= 4) {
				arrivedQueue.add(queue.removeFirst());
			}
			
			if(arrivedQueue.size() > 0) {
				firstTime=false;
			}
			
			for (Process p : arrivedQueue) {
				int frame = p.NextPage();
				//int frame = p.current == -1 ? 0 : p.NextPage();
				
				if (free >= 4) {//add existing or new process from arrivedQueue queue
					if (checkProcess(p, frame)) { //mem hit
						updateMemPage(p, frame);
						hit++;
						System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Page referenced:" + frame + ",\tPage in memory");
					} else { //miss
						miss++;
						if(p.current == -1) { //p not start yet
																	//if(frame == 0) {
							p.start=time; //set start time
						}
						addProcPage(p, frame);
						free--;
						System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Page referenced:" + frame + ",\tPage not in memory,\tno page evicted");
					}
					p.current = frame; //set curr frame
					numReferencedPages++;
				} else if(p.current != -1) { //if(p.start < time) { //0 < free < 4 -> only work for started processes
					if(free > 0){
						if (checkProcess(p, frame)) {
							//memory.indexOf(p.name);
							updateMemPage(p, frame);
							hit++;
						} else { //!checkProc -> p.frame not in mem
							/*if(frame == 0) {
								p.start=time;
							 }*/
							addProcPage(p, frame);
							free--;
							miss++;
						}
					} else { //free == 0
									 //else if( free == 0 ){
						int index = run(); //call the abstract method run()
						//int procName = memory.get(index).processName;
						//int procPage = memory.get(index).processPage;
                        String procName = memory.get(index).processName;
                        int procPage = memory.get(index).processPage;

						replace(index, p, frame);
						swapped++;
						System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Page referenced:" + frame + ",\tPage not in memory" + ",\tPage " + procPage + " of process " + procName + " will be evicted");
					}
					p.current = frame; //set curr frame
					numReferencedPages++;
				}
			}
			time = time + 0.10;
			//time = (double) (time + 0.10);
			BigDecimal bd = new BigDecimal(time);
			bd=bd.round(new MathContext(3));
			printMemoryMap();
			time = bd.doubleValue();
		}
		
		ratio = (double) hit/miss;
	}
	
	private void updateMemPage(Process p, int frame) {//LFU MFU
		for (MemoryPage m : memory){
			if ((m.name == p.name) && (m.processPage == frame)) {
				
				m.lastAccessed = 0;
				m.frequency+= 1;
			}
		}

	}

	private void updateAll() {//LRU FIFO
		// TODO Auto-generated method stub
		for (MemoryPage m : memory){
			if (m.name != ".") {
				m.runTime += 0.1;
				m.lastAccessed += 0.1;
			}
		}
	}

	/*
	 *Check if the frame of p is in memory
	 */
	private boolean checkProcess(Process p, int frame) {
		boolean ans = false;
		for (MemoryPage m : memory)
		{ 
			if((m.name ==p.name) && (m.processPage ==frame)){
				ans =  true;
				break;
			}
		}
		return ans;
	}

	private void replace(int index, Process p, int page) {
		memory.set(index, new MemoryPage(p, page, 0, 0, 0));
	}

	/**
	 * Find the memory for the process that finished and deallocate it
	 * @param memory LinkedList<Process> : the memory to search for deallocation
	 * @param time int : the current time for use in printing the memory map
	 */
	public int deallocateProcess(double time)
	{
		int i = 0; //index of memory
		int count = 0; //number of mem pages freed
		HashSet<Process> procSet = new HashSet<Process>();
		ArrayList<Integer> remove = new ArrayList<Integer>();
		boolean removed = false;
		for (MemoryPage m : memory){
			//Remove the finished process and replace with available space ('.' process)
			if (m.name != ".") {
				if (time == (m.process.start + m.process.duration)) //m.process is done
				{
					remove.add(i);
					procSet.add(m.process);
					count++;
					removed =true;
				}
			}
			i++;
		}
		
		for(Process p : procSet) {
			//arrivedQueue.remove(p);
			if(arrivedQueue.remove(p)) {
				System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Exit,\t\t Size:" + p.size + ",\t Service Duration:" + p.duration);
			}
		}
		for(int idx : remove) {
			memory.set(idx, new MemoryPage("."));
		}
		remove.clear();
		return count;
	}    



	public void printMemoryMap()
	{
		System.out.print("<");
		for (MemoryPage m : memory)
			System.out.print(m.name);
		System.out.println(">");
	}



	public void addProcPage(Process p, int page)
	{
		MemoryPage blank =  new MemoryPage(".");
		int i = memory.indexOf(blank);
		memory.set(i, new MemoryPage(p, page, 0, 0, 0));

	}

}

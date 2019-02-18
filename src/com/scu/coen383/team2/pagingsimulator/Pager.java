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


	public Pager(LinkedList<MemoryPage> memory, LinkedList<Process> process) {
		this.memory = new LinkedList<MemoryPage>(memory);
		this.queue = new LinkedList<Process>(process);
		this.arrivedQueue = new LinkedList<Process>();
	}

	public abstract int run();
	public abstract String getName();


	public void simulate(){ // simulate the situation of 60 seconds
		int free = memory.size();
		double time = 0.0;
		boolean firstTime = true;
		while (time <= 60) {

			if(!firstTime) {
				updateAll(); //update the run time and last access index of the memory page
				free += deallocateProcess(time); // free the memory space of the finished process
			}

			while (!queue.isEmpty() && queue.getFirst().arrival <= time && free >= 4) {
				arrivedQueue.add(queue.removeFirst());
			}

			if(arrivedQueue.size() > 0) {
				firstTime = false;
			}
			
			for (Process p : arrivedQueue) {
				int frame = p.NextPage();
				//get next page number of the page

				if (free >= 4) {
					if (checkProcess(p, frame)) { //mem hit, update the last access index of the page
						updateMemPage(p, frame);
						hit++;
					} else { //mem miss, bring the page to the memory
						miss++;
						if(p.current == -1) { // if p hasn't started yet, start p and set its start time to current time, set its page to current frame
							p.start=time;
						}
						addProcPage(p, frame);
						free--;
						System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Enter,\t\t Size:" + p.size + ",\tService Duration:" + p.duration);
					}
					p.current = frame;
					numReferencedPages++;

				} else if(p.start < time) { // when free < 4, only work for the already started process
					if(free > 0){
						if (checkProcess(p, frame)) {
							updateMemPage(p, frame);
							hit++;
						} else {
							addProcPage(p, frame);
							free--;
							miss++;
						}
					} else { //free = 0 replace the existing page with the newest one
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
			BigDecimal bd = new BigDecimal(time);
			bd = bd.round(new MathContext(3));
			printMemoryMap();
			time = bd.doubleValue();
		}
		
		ratio = (double) hit/miss;
	}


	public void simulate2() {// simulate the situation of 100 reference page
		int free = memory.size();
		double time = 0.0;
		boolean firstTime = true;
		while (numReferencedPages < 100) {
			
			if(!firstTime) {
				updateAll();
				free += deallocateProcess(time);
			}
			
			while (queue.element().arrival <= time) {
				arrivedQueue.add(queue.removeFirst());
			}
			
			if(arrivedQueue.size() > 0) {
				firstTime=false;
			}
			
			for (Process p : arrivedQueue) {
				int frame = p.NextPage();

				if (free >= 4) {
					if (checkProcess(p, frame)) {
						updateMemPage(p, frame);
						hit++;
						System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Page referenced:" + frame + ",\tPage in memory");
					} else {
						miss++;
						if(p.current == -1) {
							p.start=time;
						}
						addProcPage(p, frame);
						free--;
						System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Page referenced:" + frame + ",\tPage not in memory,\tno page evicted");
					}
					p.current = frame;
					numReferencedPages++;
				} else if(p.current != -1) { // when free < 4, only work for the already started process
					if(free > 0){
						if (checkProcess(p, frame)) {
							updateMemPage(p, frame);
							hit++;
						} else {
							addProcPage(p, frame);
							free--;
							miss++;
						}
					} else {
						int index = run(); //call the abstract method run()
                        String procName = memory.get(index).processName;
                        int procPage = memory.get(index).processPage;
						replace(index, p, frame);
						swapped++;
						System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Page referenced:" + frame + ",\tPage not in memory" + ",\tPage " + procPage + " of process " + procName + " will be evicted");
					}
					p.current = frame;
					numReferencedPages++;
				}
			}
			time = time + 0.10;
			BigDecimal bd = new BigDecimal(time);
			bd=bd.round(new MathContext(3));
			printMemoryMap();
			time = bd.doubleValue();
		}
		
		ratio = (double) hit/miss;
	}
	
	private void updateMemPage(Process p, int frame) {// when the page on the memory is revisited, update its last accessed index and frequency index
		for (MemoryPage m : memory){
			if ((m.name == p.name) && (m.processPage == frame)) {
				m.lastAccessed = 0;
				m.frequency+= 1;
			}
		}

	}

	private void updateAll() {// update the memory page's existing time and last access index
		// TODO Auto-generated method stub
		for (MemoryPage m : memory){
			if (m.name != ".") {
				m.runTime += 0.1;
				m.lastAccessed += 0.1;
			}
		}
	}


	private boolean checkProcess(Process p, int frame) { // check if the page of a process is in the memory
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

	private void replace(int index, Process p, int page) { //replace the existing page with the newest one
		memory.set(index, new MemoryPage(p, page, 0, 0, 0));
	}

	public int deallocateProcess(double time)
	{
		int i = 0; //index of memory
		int count = 0; //number of mem pages freed
		HashSet<Process> procSet = new HashSet<>();
		ArrayList<Integer> remove = new ArrayList<>();

		for (MemoryPage m : memory){ //Remove the finished process and replace with available space ('.' process)
			if (m.name != ".") {
				if (time == (m.process.start + m.process.duration)) //m.process is done
				{
					remove.add(i);
					procSet.add(m.process);
					count++;
				}
			}
			i++;
		}
		
		for(Process p : procSet){ // evict the finished process from arrivedQueue
			if(arrivedQueue.remove(p)) {
				System.out.println("Time:" + time + ",\t Name:" + p.name + ",\t Exit,\t\t Size:" + p.size + ",\t Service Duration:" + p.duration);
			}
		}
		for(int idx : remove) {// set memory space as available
			memory.set(idx, new MemoryPage("."));
		}
		remove.clear();
		return count;
	}    



	public void printMemoryMap(){
		System.out.print("<");
		for (MemoryPage m : memory)
			System.out.print(m.name);
		System.out.println(">");
	}



	public void addProcPage(Process p, int page){ //bring the page to the memory
		MemoryPage blank =  new MemoryPage(".");
		int i = memory.indexOf(blank);
		memory.set(i, new MemoryPage(p, page, 0, 0, 0));

	}

}

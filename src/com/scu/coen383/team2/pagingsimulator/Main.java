package com.scu.coen383.team2.pagingsimulator;

import java.util.ArrayList;
import java.io.*;
import java.util.LinkedList;

/**
 * Homework 4 - Swap
 * COEN383 GROUP 2
 *
 * This program simulates memory swapping with first fit, next fit, and best fit
 * memory allocation algorithms based on various parameters:
 *
 *     -SwappingSimulator memory has 100MB swap space
 *     -Processes have randomly, evenly distributed sizes of  5, 7, 11, 31 MB
 *     -Processes last 1, 2, 3, 4, or 5 seconds
 *     -Process Scheduler uses FCFS
 *     -Simulation runs for "60 seconds"
 *     -Simulation runs for "100 page references"
 */

public class Main
{
    public static final int SIM_RUNS = 5;
    public static final int SIM_TIME_MAX = 60;
    public static final int MEMORY_SIZE = 100;
    public static final int PROCESS_COUNT= 150;
    public static LinkedList<MemoryPage> memory = new LinkedList<MemoryPage>();
    public static double FIFOsw, FIFOhm, LFUsw, LFUhm, LRUsw, LRUhm, MFUsw, MFUhm, RPsw, RPhm;

    /** Simulate each algorithm SIM_RUNS times. Print out memory maps
     * each time memory is changed, and also print overall statistics
     */
    public static void main(String[] args) throws CloneNotSupportedException
    {
        PrintStream o = null;
        try {
            o = new PrintStream(new File("output.txt"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // redirect output to file output.txt
//        System.setOut(o);

        simuOneMin();
//        simuOneHundredReferences();

    }

    public static void simuOneMin() throws CloneNotSupportedException
    {
        System.out.println("Simulation for 1 minute");
        System.out.println("------------------------------------------------");
        System.out.println("------------------------------------------------");
        int sim=1;
        while(sim <= SIM_RUNS) {

            generateMemory(100);
            System.out.println("Simulation Run: " + sim);
            System.out.println("------------------------------------------------");
            LinkedList<Process> q = ProcessFactory.generateProcesses(PROCESS_COUNT);

            System.out.println("Name\t Arrival\t Duration\t Size");
            for (Process p : q) {
                System.out.format("%s\t %.1f\t\t %d\t\t %d", p.name, p.arrival, p.duration, p.size);
                //System.out.format("%c\t %d\t\t %d\t\t %d", p.name, p.arrival, p.duration, p.size);
                System.out.println();
            }
            System.out.println("------------------------------------------------");
            ArrayList<Pager> pager = new ArrayList<Pager>() {{
                add(new FIFO(memory, q));
                add(new LFU(memory, q));
                add(new LRU(memory, q));
                add(new LRU(memory, q));
                add(new MFU(memory, q));
                add(new RP(memory, q));
            }};

            for (Pager p : pager){
                System.out.println("Paging: " + p.getName());

                p.simulate();
                if (p.getName() == "FIFO"){
                    FIFOsw += p.swapped;
                    FIFOhm += p.ratio;
                }
                if (p.getName() == "LFU"){
                    LFUsw += p.swapped;
                    LFUhm += p.ratio;
                }
                if (p.getName() == "LRU"){
                    LRUsw += p.swapped;
                    LRUhm += p.ratio;
                }
                if (p.getName() == "MFU"){
                    MFUsw += p.swapped;
                    MFUhm += p.ratio;
                }
                if (p.getName() == "Random Pick"){
                    RPsw += p.swapped;
                    RPhm += p.ratio;
                }
                System.out.println("------------------------------------------------");

                System.out.println();

            }


            System.out.println("\n\n");

            sim++;
            memory.clear();
        }
        System.out.println("Average Number of Processes Successfully Switched in over " + SIM_RUNS + " runs:");
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("FIFO: " +  FIFOsw/SIM_RUNS);
        System.out.println("LFU: " +  LFUsw/SIM_RUNS);
        System.out.println("LRU: " +  LRUsw/SIM_RUNS);
        System.out.println("MFU: " +  MFUsw/SIM_RUNS);
        System.out.println("Random Pick: " +  RPsw/SIM_RUNS);
        System.out.println();
        System.out.println("Average Hit/Miss Ration over " + SIM_RUNS + " runs:");
        System.out.println("---------------------------------------------------------------------------------");
		/*System.out.println("FIFO: " +  FIFOhm/SIM_RUNS);
		System.out.println("LFU: " +  LFUhm/SIM_RUNS);
		System.out.println("LRU: " +  LRUhm/SIM_RUNS);
		System.out.println("MFU: " +  MFUhm/SIM_RUNS);
		System.out.println("Random Pick: " +  RPhm/SIM_RUNS);*/

        System.out.format("FIFO: %.2f%n", FIFOhm/SIM_RUNS);
        System.out.format("LFU: %.2f%n", LFUhm/SIM_RUNS);
        System.out.format("LRU: %.2f%n", LRUhm/SIM_RUNS);
        System.out.format("MFU: %.2f%n", MFUhm/SIM_RUNS);
        System.out.format("Random Pick: %.2f%n", RPhm/SIM_RUNS);
    }

    public static void simuOneHundredReferences() throws CloneNotSupportedException
    {
        System.out.println("************************************************");
        System.out.println("************************************************");
        System.out.println();
        System.out.println("Simulation for 100 page references");
        System.out.println("------------------------------------------------");
        System.out.println("------------------------------------------------");

        //reinit
        FIFOsw = 0;
        FIFOhm = 0;
        LFUsw = 0;
        LFUhm = 0;
        LRUsw = 0;
        LRUhm = 0;
        MFUsw = 0;
        MFUhm = 0;
        RPsw = 0;
        RPhm = 0;

        int sim=1;
        while(sim <= SIM_RUNS) {

            generateMemory(100);
            System.out.println("Simulation Run: " + sim);
            System.out.println("------------------------------------------------");
            LinkedList<Process> q = ProcessFactory.generateProcesses(PROCESS_COUNT);

            System.out.println("Name\t Arrival\t Duration\t Size");
            for (Process p : q) {
                System.out.format("%s\t %.1f\t\t %d\t\t %d", p.name, p.arrival, p.duration, p.size);
                //System.out.format("%c\t %d\t\t %d\t\t %d", p.name, p.arrival, p.duration, p.size);
                System.out.println();
            }
            System.out.println("------------------------------------------------");
            ArrayList<Pager> pager = new ArrayList<Pager>() {{
                add(new FIFO(memory, q));
                add(new LFU(memory, q));
                add(new LRU(memory, q));
                add(new LRU(memory, q));
                add(new MFU(memory, q));
                add(new RP(memory, q));
            }};

            for (Pager p : pager){
                System.out.println("Paging: " + p.getName());

                p.simulate2();
                if (p.getName() == "FIFO"){
                    FIFOsw += p.swapped;
                    FIFOhm += p.ratio;
                }
                if (p.getName() == "LFU"){
                    LFUsw += p.swapped;
                    LFUhm += p.ratio;
                }
                if (p.getName() == "LRU"){
                    LRUsw += p.swapped;
                    LRUhm += p.ratio;
                }
                if (p.getName() == "MFU"){
                    MFUsw += p.swapped;
                    MFUhm += p.ratio;
                }
                if (p.getName() == "Random Pick"){
                    RPsw += p.swapped;
                    RPhm += p.ratio;
                }
                System.out.println("------------------------------------------------");

                System.out.println();

            }


            System.out.println("\n\n");

            sim++;
            memory.clear();
        }/*
		System.out.println("Average Number of Processes Successfully Switched in over " + SIM_RUNS + " runs:");
		System.out.println("---------------------------------------------------------------------------------");
		System.out.println("FIFO: " +  FIFOsw/SIM_RUNS);
		System.out.println("LFU: " +  LFUsw/SIM_RUNS);
		System.out.println("LRU: " +  LRUsw/SIM_RUNS);
		System.out.println("MFU: " +  MFUsw/SIM_RUNS);
		System.out.println("Random Pick: " +  RPsw/SIM_RUNS);
		System.out.println();*/
        System.out.println("Average Hit/Miss Ration over " + SIM_RUNS + " runs:");
        System.out.println("---------------------------------------------------------------------------------");
		/*System.out.println("FIFO: " +  FIFOhm/SIM_RUNS);
		 System.out.println("LFU: " +  LFUhm/SIM_RUNS);
		 System.out.println("LRU: " +  LRUhm/SIM_RUNS);
		 System.out.println("MFU: " +  MFUhm/SIM_RUNS);
		 System.out.println("Random Pick: " +  RPhm/SIM_RUNS);*/

        System.out.format("FIFO: %.2f%n", FIFOhm/SIM_RUNS);
        System.out.format("LFU: %.2f%n", LFUhm/SIM_RUNS);
        System.out.format("LRU: %.2f%n", LRUhm/SIM_RUNS);
        System.out.format("MFU: %.2f%n", MFUhm/SIM_RUNS);
        System.out.format("Random Pick: %.2f%n", RPhm/SIM_RUNS);
    }

    public static void generateMemory(int n)
    {
        for(int i = 0 ; i < n; i++) {
            memory.add(new MemoryPage("."));
        }
    }
}

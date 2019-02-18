package com.scu.coen383.team2.pagingsimulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
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

    // we have 150 total processes in the simulation
    public static final int PROCESS_COUNT= 150;

    // data structure of memory, as we need to swap out some MemoryPage randomly, so
    // a LinkedList is needed
    public static LinkedList<MemoryPage> memory = new LinkedList<MemoryPage>();
    public static ArrayList<Integer> FIFOsw = new ArrayList<>(),
        LFUsw = new ArrayList<>(),
        LRUsw = new ArrayList<>(),
        MFUsw = new ArrayList<>(),
        RPsw = new ArrayList<>();
    public static ArrayList<Double>  FIFOhm = new ArrayList<>(),
            LFUhm = new ArrayList<>(),
            LRUhm = new ArrayList<>(),
            MFUhm = new ArrayList<>(),
            RPhm = new ArrayList<>();


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
        System.setOut(o);

        // run the simulator for 1 minutes
        simuOneMin();

        // run the simulator for 100 memory references.
        simuOneHundredReferences();

    }

    public static void simuOneMin() throws CloneNotSupportedException
    {
        System.out.println("Simulation for 1 minute");
        System.out.println("------------------------------------------------");
        System.out.println("------------------------------------------------");

        // we run 'SIM_RUNS' rounds to get an average statistics.
        int sim=1;
        while(sim <= SIM_RUNS) {

            // initialize the memory data structure with 100 free MemoryPages.
            generateMemory(MEMORY_SIZE);
            System.out.println("Simulation Run: " + sim);
            System.out.println("------------------------------------------------");

            /*
              generate 'PROCESS_COUNT' processes and sort them by arrival time in ascending order
              the return value is stored as a LinkedList.
             */
            LinkedList<Process> q = ProcessFactory.generateProcesses(PROCESS_COUNT);

            // print out process info in process queue.
            System.out.format("%-8s %-10s %-10s %s\n", "Name", "Arrival", "Duration", "Size");
            for (Process p : q) {
                System.out.format("%-8s %-10.1f %-10d %-2d", p.name, p.arrival, p.duration, p.size);
                System.out.println();
            }
            System.out.println("------------------------------------------------");

            /*
              class Pager is a abstract class, class FIFO is extended from Pager, the only thing
              FIFO do is to overwrite the abstract function RUN and getName, so, we can use he base
              class to represent these 5 algorithms implementation class
             */
            ArrayList<Pager> pager = new ArrayList<Pager>() {{
                add(new FIFO(memory, q));
                add(new LFU(memory, q));
                add(new LRU(memory, q));
                add(new MFU(memory, q));
                add(new RP(memory,  q));
            }};

            for (Pager p : pager){
                System.out.println("Paging: " + p.getName());

                p.simulate();
                if (p.getName() == "FIFO"){
                    FIFOsw.add(p.swapped);
                    FIFOhm.add(p.ratio);
                }
                if (p.getName() == "LFU"){
                    LFUsw.add(p.swapped);
                    LFUhm.add(p.ratio);
                }
                if (p.getName() == "LRU"){
                    LRUsw.add(p.swapped);
                    LRUhm.add(p.ratio);
                }
                if (p.getName() == "MFU"){
                    MFUsw.add(p.swapped);
                    MFUhm.add(p.ratio);
                }
                if (p.getName() == "Random Pick"){
                    RPsw.add(p.swapped);
                    RPhm.add(p.ratio);
                }
                System.out.println("------------------------------------------------");
                System.out.println();
            }

            System.out.println("\n\n");

            sim++;
            memory.clear();
        }
        System.out.println("Average Number of Processes Successfully Switched in over " + SIM_RUNS + " runs:");
        printOutStatisticSwap();
        System.out.println("Average Hit/Miss Ration over " + SIM_RUNS + " runs:");
        printOutStatisticAvg();
    }

    public static void simuOneHundredReferences() throws CloneNotSupportedException
    {
        System.out.println("************************************************");
        System.out.println("************************************************");
        System.out.println();
        System.out.println("Simulation for 100 page references");
        System.out.println("------------------------------------------------");
        System.out.println("------------------------------------------------");

        //reinitialize
        FIFOsw.clear();
        FIFOhm.clear();
        LFUsw.clear();
        LFUhm.clear();
        LRUsw.clear();
        LRUhm.clear();
        MFUsw.clear();
        MFUhm.clear();
        RPsw.clear();
        RPhm.clear();

        int sim=1;
        while(sim <= SIM_RUNS) {

            generateMemory(100);
            System.out.println("Simulation Run: " + sim);
            System.out.println("------------------------------------------------");
            LinkedList<Process> q = ProcessFactory.generateProcesses(PROCESS_COUNT);

            // print out process info in process queue.
            System.out.format("%-8s %-10s %-10s %s\n", "Name", "Arrival", "Duration", "Size");
            for (Process p : q) {
                System.out.format("%-8s %-10.1f %-10d %-2d", p.name, p.arrival, p.duration, p.size);
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
                    FIFOsw.add(p.swapped);
                    FIFOhm.add(p.ratio);
                }
                if (p.getName() == "LFU"){
                    LFUsw.add(p.swapped);
                    LFUhm.add(p.ratio);
                }
                if (p.getName() == "LRU"){
                    LRUsw.add(p.swapped);
                    LRUhm.add(p.ratio);
                }
                if (p.getName() == "MFU"){
                    MFUsw.add(p.swapped);
                    MFUhm.add(p.ratio);
                }
                if (p.getName() == "Random Pick"){
                    RPsw.add(p.swapped);
                    RPhm.add(p.ratio);
                }
                System.out.println("------------------------------------------------");
                System.out.println();

            }

            System.out.println("\n\n");

            sim++;
            memory.clear();
        }
        System.out.println("Average Hit/Miss Ration over " + SIM_RUNS + " runs:");
        printOutStatisticAvg();
    }

    public static void generateMemory(int n)
    {
        for(int i = 0 ; i < n; i++) {
            memory.add(new MemoryPage("."));
        }
    }


    public static void printOutStatisticAvg() {
        System.out.println("---------------------------------------------------------------------------------");
        System.out.format("%-8s %-10s %-10s %-10s %s\n", "FIFO", "LFU", "LRU", "MFU", "Random Pick");
        for (int i = 0; i < SIM_RUNS; i++) {
            System.out.format("%-8.2f %-10.2f %-10.2f %-10.2f %.2f\n",
                    FIFOhm.get(i), LFUhm.get(i), LRUhm.get(i), MFUhm.get(i), RPhm.get(i));
        }
        // print out the average data
        System.out.println("---------------------------------------------------------------------------------");
        System.out.format("%-8.2f %-10.2f %-10.2f %-10.2f %.2f\n",
                FIFOhm.stream().mapToDouble(val -> val).average().orElse(0.0),
                LFUhm.stream().mapToDouble(val -> val).average().orElse(0.0),
                LRUhm.stream().mapToDouble(val -> val).average().orElse(0.0),
                MFUhm.stream().mapToDouble(val -> val).average().orElse(0.0),
                RPhm.stream().mapToDouble(val -> val).average().orElse(0.0)
        );
    }

    public static void printOutStatisticSwap() {
        System.out.println("---------------------------------------------------------------------------------");
        System.out.format("%-8s %-10s %-10s %-10s %s\n", "FIFO", "LFU", "LRU", "MFU", "Random Pick");
        for (int i = 0; i < SIM_RUNS; i++) {
            System.out.format("%-8d %-10d %-10d %-10d %d\n",
                    FIFOsw.get(i), LFUsw.get(i), LRUsw.get(i), MFUsw.get(i), RPsw.get(i));
        }
        System.out.println("---------------------------------------------------------------------------------");
        System.out.format("%-8.1f %-10.1f %-10.1f %-10.1f %.1f\n",
                FIFOsw.stream().mapToDouble(val -> val).average().orElse(0.0),
                LFUsw.stream().mapToDouble(val -> val).average().orElse(0.0),
                LRUsw.stream().mapToDouble(val -> val).average().orElse(0.0),
                MFUsw.stream().mapToDouble(val -> val).average().orElse(0.0),
                RPsw.stream().mapToDouble(val -> val).average().orElse(0.0)
        );
        System.out.println();
    }

}

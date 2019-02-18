/**
 * COEN383 GROUP 2
 * 
 * Customize a class named MemoryPage, use it to create memory page objects.
 */
package com.scu.coen383.team2.pagingsimulator;

public class MemoryPage {
    //Include the info of belonging to which process, page number, lastAccessed time, runtime and frequency
    Process process;
    String name;
    String processName;
    int processPage;
    double lastAccessed;
    double runTime;
    int frequency;

    //Constructor of this class
    public MemoryPage(Process process, int processPage, double lastAccessed, double runTime, int frequency) {
        this.process = process;
        this.processPage = processPage;
        this.lastAccessed = lastAccessed;
        this.runTime = runTime;
        this.frequency = frequency;
        this.name = process.name;
    }

    //Overload: another constructor of this class
    public MemoryPage(String name) {
        this.name = name;
    }

    public boolean equals(Object other) {
        if (this.name.equals(((MemoryPage) other).name)) {
            return true;
        } else {
            return false;
        }
    }
}

/**
 * COEN383 GROUP 2
 * 
 * An object representing a Memory Page, containing process pages
 */
package com.scu.coen383.team2.pagingsimulator;

public class MemoryPage {
           
               Process process;
               String name;
               String processName;
               int processPage;
               double lastAccessed;
               double runTime;
               int frequency;

               public MemoryPage(Process process, int processPage, double lastAccessed, double runTime, int frequency)
               {
                 this.process = process; 
                 this.processPage = processPage; 
                 this.lastAccessed = lastAccessed; 
                 this.runTime = runTime; 
                 this.frequency = frequency;
                 this.name=process.name;
               }
               
               public MemoryPage(String c)
               {
                 this.name = c;
               }
               
               public boolean equals(Object other) {
                    if(this.name == ((MemoryPage) other).name)
                            return true;
                    else
                            return false;
                    }
}

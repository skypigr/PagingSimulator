package com.scu.coen383.team2.pagingsimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * COEN383 GROUP 2
 * A ProcessFactory that generates processes
 */

public class ProcessFactory 
{    
	public static final int PROCESS_TIME_MAX = 5;
	public static final int[] PROCESS_SIZES_MB = {5, 11, 17, 31};
	public static final double MAX_SIMU_TIME = 60.0; //1 min = 60 seconds
    public static final String[] NAMES = new String[150];
    //"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789" + //62
        //"ÀÁÂÃÄÅÆÇÈÉÊËÌÍÎÏ00D0ÐÑÒÓÔÕÖØÙÚÛÜÝÞßàáâãäåæçèéêëìíîïðñòóôõö"+ //58
			//"ĀāĂăĄąĆćĈĉĊċČčĎďĐđĒēĔĕĖėĘęĚěĜĝĞğĠġĢģĤĥĦħ";//40 = 160 char/unique processes


	/** Generate processes randomly until the total task duration is greater
	 * than SIM_TIME_MAX by a bit so that there is at least one process that 
	 * doesn't finish executing
	 * @param n int : maximum number of processes to generate
	 */
	public static LinkedList<Process> generateProcesses(int n)
	{
		LinkedList<Process> processQueue = new LinkedList<>();

		// used for evenly distributing memory sizes
		Random rand = new Random();
		int index = 0;
		//int arrivalTime=0;
		double arrivalTime = 0.0;
		int duration = 0;
		int size = 0;
		List<Integer> procDuration = new ArrayList<>();
		List<Integer> procSize = new ArrayList<>();

        for (int i = 0; i < NAMES.length; i++) {
            NAMES[i] = "P" + Integer.toString(i);
        }
        
		for (int i = 0; i < 32; i++) {
			for (int j = 1; j <= PROCESS_TIME_MAX ; j++) {
				procDuration.add(j);
			}
		}
		Collections.shuffle(procDuration, new Random());
		
		
		for (int i = 0; i < 40; i++) {
			for (int j = 0; j < PROCESS_SIZES_MB.length ; j++) {
				procSize.add(PROCESS_SIZES_MB[j]);
			}
		}
		Collections.shuffle(procSize, new Random());
		
		
		while (processQueue.size() < NAMES.length)
		{
			size = procSize.get(index);
			duration = procDuration.get(index);
			
			processQueue.addLast(new Process(duration, size, 
					NAMES[index], arrivalTime, arrivalTime));
			//arrivalTime = arrivalTime + rand.nextInt(2);
			arrivalTime = arrivalTime + rand.nextDouble() * (MAX_SIMU_TIME - arrivalTime) / (NAMES.length - processQueue.size());

			index ++;
		}

		return processQueue;
	}
}

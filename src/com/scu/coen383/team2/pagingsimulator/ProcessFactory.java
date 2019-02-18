package com.scu.coen383.team2.pagingsimulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * COEN383 GROUP 2
 *
 * Customize a class named ProcessFactory which is used to generate Processes
 *
 */

public class ProcessFactory {
	//Set MAX_SIMULATION_TIME to 1 minute (60 seconds)
	public static final double MAX_SIMULATION_TIME = 60.0;
	public static final int PROCESS_SERVICE_DURATION_MAX = 5;
	public static final int[] PROCESS_SIZES_IN_MB = new int[]{5, 11, 17, 31};

	public static LinkedList<Process> generateProcesses(int processCount) {
		LinkedList<Process> processList = new LinkedList<>();

		String[] processNames = new String[processCount];
		List<Integer> processDurationList = new ArrayList<>();
		List<Integer> processSizeList = new ArrayList<>();

		//Process is named as P0, P2, P3 ... P149
        for (int i = 0; i < processCount; i++) {
			processNames[i] = "P" + String.valueOf(i);
        }

		//Used rand to evenly distribute process size and process service duration time
		Random rand = new Random();
		//Generate 150 service process size evenly and distractedly within 5, 11, 17, 31 MB
		for (int i = 0; i < 38; i++) {
			for (int j = 0; j < PROCESS_SIZES_IN_MB.length; j++) {
				processSizeList.add(PROCESS_SIZES_IN_MB[j]);
			}
		}
		//Disorganize the process size
		Collections.shuffle(processSizeList, new Random());

        //Generate 150 service duration time evenly and distractedly within 1, 2, 3, 4, 5 seconds
		for (int i = 0; i < 30; i++) {
			for (int j = 1; j <= PROCESS_SERVICE_DURATION_MAX; j++) {
				processDurationList.add(j);
			}
		}
		//Disorganize the service duration time
		Collections.shuffle(processDurationList, new Random());

		//Generate 150 random Processes based on processSizeList and processDurationList
		int count = 0;
		double arrivalTime = 0.0;
		while (processList.size() < processCount) {
			processList.add(new Process(processDurationList.get(count), processSizeList.get(count),
					processNames[count], arrivalTime, arrivalTime));
			arrivalTime = arrivalTime + rand.nextDouble() * (MAX_SIMULATION_TIME - arrivalTime) / (processCount - processList.size());
			count++;
		}
		return processList;
	}
}

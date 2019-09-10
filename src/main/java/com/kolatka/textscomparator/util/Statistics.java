package com.kolatka.textscomparator.util;

import java.util.ArrayList;

public class Statistics {
	private final ArrayList<SingleStat> times;
	private SingleStat currentStat;
	private Long startTime;
	private Long endTime;
	private int comparison;

	public Statistics() {
		times = new ArrayList<>();
		comparison = 0;
	}

	public void startTimer(String taskName) {
		currentStat = new SingleStat();
		currentStat.setTask(taskName);
		currentStat.setStartTime(System.nanoTime());
	}

	public void stopTimer() {
		currentStat.setEndTime(System.nanoTime());
		currentStat.setTime(currentStat.getEndTime() - currentStat.getStartTime());
		times.add(currentStat);
	}

	public Double getLastStat() {
		if (times.get(times.size() - 1).getTime() < 1000) return 0.0;
		else return times.get(times.size() - 1).getTime() / 1000000.0;
	}

	public Long getLastStatLong() {
		if (times.get(times.size() - 1).getTime() < 1000) return 0L;
		else return times.get(times.size() - 1).getTime();
	}

	public String getSpecificStats(String taskName) {
		StringBuilder message = new StringBuilder();
		for (int i = 0; i < times.size(); i++) {
			if (times.get(i).getTask().equals(taskName))
				message.append("\n").append(i).append(": \t").append(times.get(i).getTime() / 1000000).append(" ms");
		}
		return message.toString();
	}

	public String getSpecificStat(String taskName) {
		for (int i = 0; i < times.size(); i++) {
			if (times.get(i).getTask().equals(taskName)) return times.get(i).getTime() / 1000000 + " ms";
		}
		return "";
	}

	public String getAllStats() {
		StringBuilder message = new StringBuilder();
		for (int i = 0; i < times.size(); i++) {
			message.append("\n").append(i).append(". ").append(times.get(i).getTask()).append(": \t").append(times.get(i).getTime() / 1000000).append(" ms");
		}

		return message.toString();
	}

	public void nextComparison() {
		comparison++;
	}

	public int getComparison() {
		return comparison;
	}

	public void addTime(String task, Long time) {
		SingleStat newStat = new SingleStat();
		newStat.setTask(task);
		newStat.setTime(time);
		times.add(newStat);
	}

}

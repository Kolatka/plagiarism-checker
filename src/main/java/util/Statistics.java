/**
 * 
 */
package util;

import java.util.ArrayList;

public class Statistics {
	private ArrayList<SingleStat> times;
	private SingleStat currentStat;
	private Long startTime;
	private Long endTime;
	private int comparison;
	
	public Statistics(){
		times = new ArrayList<SingleStat>();
		comparison = 0;
	}
	
	
	public void startTimer(String taskName) {
		currentStat = new SingleStat();
		currentStat.setTask(taskName);
		currentStat.setStartTime(System.nanoTime());	
	}
	
	
	public void stopTimer() {
		currentStat.setEndTime(System.nanoTime());	
		currentStat.setTime(currentStat.getEndTime()-currentStat.getStartTime());
		times.add(currentStat);
	}
	
	
	public Double getLastStat() {
		if (times.get(times.size()-1).getTime()<1000) return 0.0;
		else return times.get(times.size()-1).getTime()/1000000.0;
	}
	
	public Long getLastStatLong() {
		if (times.get(times.size()-1).getTime()<1000) return 0L;
		else return times.get(times.size()-1).getTime();
	}
	
	public String getSpecificStats(String taskName) {
		String message = "";
		for(int i = 0; i < times.size(); i++) {
			if(times.get(i).getTask().equals(taskName)) message += "\n" + i  + ": \t" + times.get(i).getTime()/1000000 + " ms";
		}
		return message;
	}
	public String getSpecificStat(String taskName) {
		for(int i = 0; i < times.size(); i++) {
			if(times.get(i).getTask().equals(taskName)) return times.get(i).getTime()/1000000 + " ms";
		}
		return "";
	}
	public String getAllStats() {
		String message = "";
		for(int i = 0; i < times.size(); i++) {
			 message += "\n" + i + ". " + times.get(i).getTask()  + ": \t" + times.get(i).getTime()/1000000 + " ms";
		}
		
		return message;
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

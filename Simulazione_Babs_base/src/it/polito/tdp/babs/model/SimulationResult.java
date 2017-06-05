package it.polito.tdp.babs.model;

public class SimulationResult {
	
	private int numberOfPickMiss;
	private int numberOfDropMiss;
	
	
	public SimulationResult() {
		numberOfPickMiss=0;
		numberOfDropMiss=0;
	}
	public int getNumberOfPickMiss() {
		return numberOfPickMiss;
	}
	public int getNumberOfDropMiss() {
		return numberOfDropMiss;
	}
	public void increaseNumberOfPickMiss() {
		numberOfPickMiss++;
	}
	public void increaseNumberOfDropMiss() {
		numberOfDropMiss++;
	}
}

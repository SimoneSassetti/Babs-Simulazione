package it.polito.tdp.babs.model;

import java.time.LocalDateTime;
import java.util.*;
import java.util.PriorityQueue;

public class Simulazione {
	
	SimulationResult simulationResult;
	
	private enum EventType{
		PICK, DROP;
	}
	PriorityQueue<Event> pt;
	
	public Simulazione(){
		pt=new PriorityQueue<Event>();
		simulationResult=new SimulationResult();
	}
	
	private class Event implements Comparable<Event>{
		
		EventType type;
		LocalDateTime ldt;
		Trip trip;

		public Event(EventType type, LocalDateTime ldt, Trip trip) {
			super();
			this.type = type;
			this.ldt = ldt;
			this.trip = trip;
		}

		@Override
		public int compareTo(Event o) {
			// TODO Auto-generated method stub
			return 0;
		}
	}
	
	public void run(){
		
	}
	
	public void loadPick(List<Trip> trips){
		for(Trip t: trips){
			pt.add(new Event(EventType.PICK, t.getStartDate(),t));
		}
	}
	public void loadDrop(List<Trip> trips){
		for(Trip t: trips){
			pt.add(new Event(EventType.DROP, t.getEndDate(),t));
		}
	}
	
	public SimulationResult collectResult(){
		return null;
	}

	public void loadStation(double k) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}

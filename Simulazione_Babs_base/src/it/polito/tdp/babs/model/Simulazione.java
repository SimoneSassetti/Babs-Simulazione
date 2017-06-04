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
	HashMap<Station,Integer> mappaStazioni;
	private int preseMancate;
	private int ritorniMancati;
	
	public Simulazione(){
		pt=new PriorityQueue<Event>();
		simulationResult=new SimulationResult();
		mappaStazioni=new HashMap<>();
		preseMancate=0;
		ritorniMancati=0;
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
			if(this.trip.getStartDate().isAfter(o.trip.getStartDate()))
				return 1;
			else
				return -1;
		}
	}
	
	public void run(List<Station> stazioni){
		while(!pt.isEmpty()){
			
			Event e=pt.poll();
			
			switch(e.type){
			
			case PICK:
				//COME FACCIO A RECUPERARE LA STAZIONE VELOCEMENTE?
				for(Station s: stazioni){
					if(s.getStationID()==e.trip.getStartStationID()){
						if(mappaStazioni.get(s)==0){
							preseMancate++;
						}else{
							int temp=mappaStazioni.get(s);
							mappaStazioni.put(s, temp-1);
						}
					}
				}
				break;
				
			case DROP:
				for(Station s: stazioni){
					if(s.getStationID()==e.trip.getEndStationID()){
						if(mappaStazioni.get(s)>=s.getDockCount()){
							ritorniMancati++;
						}else{
							int temp=mappaStazioni.get(s);
							mappaStazioni.put(s, temp+1);
						}
					}
				}
				break;
			}
		}
		simulationResult.setNumberOfPickMiss(preseMancate);
		simulationResult.setNumberOfDropMiss(ritorniMancati);
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
		return simulationResult;
	}

	public void loadStation(double k, List<Station> stazioni) {
		//RIEMPIAMO LA MAPPA CON TUTTE LE STAZIONI DISPONIBILI E METTIAMO POI ASSOCIAMO I POSTI LIBERI O OCCUPATI
		for(Station s: stazioni){
			int occ=(int) (s.getDockCount()*k/100);//salvo i posti occupati
			mappaStazioni.put(s, occ);
		}
	}
}

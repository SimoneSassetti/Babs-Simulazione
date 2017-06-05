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
	HashMap<Integer,Integer> mappaStazioni;
	private Model model;
	
	public Simulazione(Model model){
		pt=new PriorityQueue<Event>();
		simulationResult=new SimulationResult();
		mappaStazioni=new HashMap<>();
		this.model=model;
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
//			if(this.trip.getStartDate().isAfter(o.trip.getStartDate()))
//				return 1;
//			else
//				return -1;
			return this.ldt.compareTo(o.ldt);
		}
	}
	
	public void run(){
		while(!pt.isEmpty()){
			
			Event e=pt.poll();
			
			switch(e.type){
			
			case PICK:
				int occupacy=mappaStazioni.get(e.trip.getStartStationID());
				if(occupacy>0){
					occupacy--;
					mappaStazioni.put(e.trip.getStartStationID(), occupacy);
					pt.add(new Event(EventType.DROP,e.trip.getEndDate(),e.trip));
				}else{
					simulationResult.increaseNumberOfPickMiss();
				}
				break;
				
			case DROP:
				occupacy=mappaStazioni.get(e.trip.getEndStationID());
				int disponibilita=model.getStationById(e.trip.getEndStationID()).getDockCount();
				if(occupacy>=disponibilita){
					simulationResult.increaseNumberOfDropMiss();
				}else{
					occupacy++;
					mappaStazioni.put(e.trip.getEndStationID(), occupacy);
				}
				break;
			}
		}
	}
	
	public void loadPick(List<Trip> trips){
		for(Trip t: trips){
			pt.add(new Event(EventType.PICK, t.getStartDate(),t));
		}
	}
	/**
	 * INIZIALMENTE IL PROF AVEVA PENSATO DI PRENDERE GLI EVENTI SEPARATAMENTE IN MODO CHE SE UN UTENTE AVESSE PRESO UNA BICI IN UN GIORNO
	 * E L'AVESSE LASCIATA IL GIORNO SEGUENTE DEVO PRENDERE GLI EVENTI DI DROP IN MODO SEPARATO DAL PICK DEL GIORNO PRECEDENTE.
	 * QUESTA SOLUZIONE HA UN PROBLEMA: SE UN UTENTE VA PER PRENDERE LA BICI MA NON RIESCE A PRENDERLA NON HA SENSO CONSIDERARE IL DROP DI QUELLA BICI XK NON L'HA PRESA
	 * QUINDI FLASEREI I DATI DELLA SIMULAZIONE.
	 * 
	 * CONSIDERIAMO DUNQUE SOLO GLI EVENTI DI PICK (INIZIALMENTE) E POI X OGNI EVENTO DI PICK PROCESSARE L'EVENTO DI DROP SOLO SE L'UTENTE E' RIUSCITO A PRELEVARE LA BICICLETTA
	 */
//	public void loadDrop(List<Trip> trips){
//		for(Trip t: trips){
//			pt.add(new Event(EventType.DROP, t.getEndDate(),t));
//		}
//	}
	
	public SimulationResult collectResult(){
		return simulationResult;
	}

	public void loadStation(double k, List<Station> stazioni) {
		//RIEMPIAMO LA MAPPA CON TUTTE LE STAZIONI DISPONIBILI E METTIAMO POI ASSOCIAMO I POSTI LIBERI O OCCUPATI
		for(Station s: stazioni){
			int occ=(int) (s.getDockCount()*k);//salvo i posti occupati
			mappaStazioni.put(s.getStationID(), occ);
		}
	}
}

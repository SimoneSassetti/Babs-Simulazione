package it.polito.tdp.babs.model;

import java.time.LocalDate;
import java.util.*;
import it.polito.tdp.babs.db.BabsDAO;

public class Model {
	
	BabsDAO dao;
	List<Station> stazioni;
	
	
	public Model() {
		dao=new BabsDAO();
	}
	
	public List<Station> getStazioni(){
		if(stazioni==null){
			stazioni=dao.getAllStations();
		}
		return stazioni;
	}

	public List<Statistics> getStats(LocalDate ld){
		List<Statistics> stats=new ArrayList<Statistics>();
		
		for(Station s: this.getStazioni()){
			int pick=dao.getPickNumber(s,ld);
			int drop=dao.getDropNumber(s,ld);
			Statistics stat=new Statistics(s,pick,drop);
			stats.add(stat);
		}
		return stats;
	}

	public List<Trip> getTripsWithPickForDate(LocalDate ld) {
		return dao.getTripsForDayPick(ld);
	}
	public List<Trip> getTripsWithDropForDate(LocalDate ld) {
		return dao.getTripsForDayPick(ld);
	}
}

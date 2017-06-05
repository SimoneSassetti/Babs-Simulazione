package it.polito.tdp.babs.model;

import java.time.LocalDate;
import java.util.*;
import it.polito.tdp.babs.db.BabsDAO;

public class Model {
	
	private BabsDAO dao;
	private List<Station> stazioni;
	private Map<Integer, Station> mappaStaz;
	
	public Model() {
		dao=new BabsDAO();
		mappaStaz=new HashMap<>();
	}
	
	public List<Station> getStazioni(){
		if(stazioni==null){
			stazioni=dao.getAllStations();
			for(Station s: stazioni){
				mappaStaz.put(s.getStationID(), s);
			}
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
	
	//FUNZIONA MA NON TIENE CONTO DI QUELLE CHE HANNO 0 COME VALORI DI PICK O DROP SICCOME IL GROUP BY LE SALTA
	public List<Statistics> getStatisticheOttimizzato(LocalDate ld){
		List<Statistics> stats=new ArrayList<Statistics>();
		Map<Integer,Integer> stazioniPick=dao.getStationWithPick(ld);
		Map<Integer,Integer> stazioniDrop=dao.getStationWithDrop(ld);
		for(Station s: this.getStazioni()){
			for(Integer stazP: stazioniPick.keySet()){
				if(stazP==s.getStationID()){
					for(Integer stazD: stazioniDrop.keySet()){
						if(stazD==s.getStationID()){
							Statistics stat=new Statistics(s,stazioniPick.get(stazP),stazioniDrop.get(stazD));
							stats.add(stat);
						}
					}
				}
			}
		}
		return stats;
	}

	public Station getStationById(int id){
		return mappaStaz.get(id);
	}
	
	public List<Trip> getTripsWithPickForDate(LocalDate ld) {
		return dao.getTripsForDayPick(ld);
	}
	public List<Trip> getTripsWithDropForDate(LocalDate ld) {
		return dao.getTripsForDayPick(ld);
	}
}

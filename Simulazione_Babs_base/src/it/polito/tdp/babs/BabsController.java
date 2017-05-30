package it.polito.tdp.babs;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.babs.model.Model;
import it.polito.tdp.babs.model.Simulazione;
import it.polito.tdp.babs.model.Statistics;
import it.polito.tdp.babs.model.Trip;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class BabsController {

	private Model model;

	public void setModel(Model model) {
		this.model = model;
	}

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private DatePicker pickData;

	@FXML
	private Slider sliderK;

	@FXML
	private TextArea txtResult;

	@FXML
	void doContaTrip(ActionEvent event) {
		txtResult.clear();
		
		LocalDate ld=pickData.getValue();
		if(ld==null){
			txtResult.setText("Selezionare una data.");
			return;
		}
		List<Statistics> stats=model.getStats(ld);
		Collections.sort(stats);
		
		for(Statistics s: stats){
			if(s.getPick()<=0)//dovrei controllare anche i drop
				txtResult.appendText(String.format("WARNING: Stazione %s con 0 pick\n", s.getStazione().getName()));
			else
				txtResult.appendText(String.format("%s %d %d\n",s.getStazione().getName(),s.getPick(),s.getDrop()));
		}
	}

	@FXML
	void doSimula(ActionEvent event) {
		txtResult.clear();
		
		LocalDate ld=pickData.getValue();
		if(ld==null || ld.getDayOfWeek()==DayOfWeek.SATURDAY || ld.getDayOfWeek()==DayOfWeek.SUNDAY){
			txtResult.setText("Selezionare un giorno feriale.");
			return;
		}
		double k=(double)sliderK.getValue()/100.0;
		
		
		List<Trip> tripsPick=model.getTripsWithPickForDate(ld);
		List<Trip> tripsDrop=model.getTripsWithDropForDate(ld);
		
		Simulazione simulazione=new Simulazione();
		simulazione.loadPick(tripsPick);
		simulazione.loadDrop(tripsDrop);
		simulazione.loadStation(k);
		simulazione.run();
		simulazione.collectResult();
		
		
	}

	@FXML
	void initialize() {
		assert pickData != null : "fx:id=\"pickData\" was not injected: check your FXML file 'Babs.fxml'.";
		assert sliderK != null : "fx:id=\"sliderK\" was not injected: check your FXML file 'Babs.fxml'.";
		assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Babs.fxml'.";

		pickData.setValue(LocalDate.of(2013, 9, 1));
	}
}

package fr.battlechronicles.question;

import java.util.List;

public class Reponse {

	private String nomBataille;
	private String nomConflit;
	private String result;
	private List<String> belligerants;
	private String date;
	private Double latitude;
	private Double longitude;
	private List<String> commandants;
	
	public Reponse(String nomBataille, String nomConflit, String result, List<String> belligerants, String date, 
			Double latitude, Double longitude, List<String> commandants) {
		this.nomBataille = nomBataille;
		this.nomConflit = nomConflit;
		this.result = result;
		this.belligerants = belligerants;
		this.date = date;
		this.latitude = latitude;
		this.longitude = longitude;
		this.commandants = commandants;
	}

	public String getNomBataille() {
		return nomBataille;
	}

	public void setNomBataille(String nomBataille) {
		this.nomBataille = nomBataille;
	}

	public String getNomConflit() {
		return nomConflit;
	}

	public void setNomConflit(String nomConflit) {
		this.nomConflit = nomConflit;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<String> getBelligerants() {
		return belligerants;
	}

	public void setBelligerants(List<String> belligerants) {
		this.belligerants = belligerants;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public List<String> getCommandants() {
		return commandants;
	}

	public void setCommandants(List<String> commandants) {
		this.commandants = commandants;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	

}

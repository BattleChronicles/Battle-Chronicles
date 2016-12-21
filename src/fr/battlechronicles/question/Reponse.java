package fr.battlechronicles.question;

import java.util.List;

public class Reponse {

	private String nomBataille;
	private String nomConflit;
	private String result;
	private String belligerants;
	private Double latitude;
	private Double longitude;
	private String resultatBataille;
	private List<String> commandants;
	
	public Reponse(String nomBataille, String nomConflit, String result, String belligerants, 
			Double latitude, Double longitude, String resultatBataille, List<String> commandants) {
		this.nomBataille = nomBataille;
		this.nomConflit = nomConflit;
		this.result = result;
		this.belligerants = belligerants;
		this.latitude = latitude;
		this.longitude = longitude;
		this.resultatBataille = resultatBataille;
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

	public String getBelligerants() {
		return belligerants;
	}

	public void setBelligerants(String belligerants) {
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

	public String getResultatBataille() {
		return resultatBataille;
	}

	public void setResultatBataille(String resultatBataille) {
		this.resultatBataille = resultatBataille;
	}

	public List<String> getCommandants() {
		return commandants;
	}

	public void setCommandants(List<String> commandants) {
		this.commandants = commandants;
	}
	

}

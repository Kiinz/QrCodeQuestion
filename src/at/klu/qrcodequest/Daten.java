package at.klu.qrcodequest;

public class Daten {
	
	private String vorname;
	private String nachname;
	private String benutzername;
	private int punkte;
	private int platz;
	
	
	public Daten(String vorname, String nachname, String benutzername,
			int punkte) {
		super();
		this.vorname = vorname;
		this.nachname = nachname;
		this.benutzername = benutzername;
		this.punkte = punkte;
	
		
	}
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getBenutzername() {
		return benutzername;
	}
	public void setBenutzername(String benutzername) {
		this.benutzername = benutzername;
	}
	public int getPunkte() {
		return punkte;
	}
	public void setPunkte(int punkte) {
		this.punkte = punkte;
	}
	public int getPlatz() {
		return platz;
	}
	public void setPlatz(int platz) {
		this.platz = platz;
	}
	
	

}

package fp2014;

public class Ansatt {

	private String brukernavn;
	private String passord;
	private String fornavn;
	private String etternavn;
	private String email;
	
	public Ansatt() {
	}
	
	/*
	 * Husk listeners ved endring av egenskaper til ansatt, viktig for å holde
	 * databasen oppdatert og synkronisert med kalenderklienten.
	 */
	
	public String getBrukernavn() {
		return brukernavn;
	}

	public String getPassord() {
		return passord;
	}


	public String getFornavn() {
		return fornavn;
	}

	public String getEtternavn() {
		return etternavn;
	}

	public String getEmail() {
		return email;
	}

	public void visKalender(){
		/*
		 * Kalles som standardmetode for fremvisning av logget inn ansatt sine avtaler.
		 * 
		 */
		
		/*
		 * 
		 */
	}
	
}

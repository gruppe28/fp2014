package fp2014;

public class Rom {
	/*
	 * Rom legges til i databasen direkte, dette er evt noe vi kan legge til hvis vi blir ferdige før tiden.
	 */
	
	private int romNr;
	private String sted;
	private int antPlasser;
	private String beskrivelse;
	
	public Rom(int romNr, String sted, int antPlasser, String beskrivelse){
		this.romNr = romNr;
		this.sted = sted;
		this.antPlasser = antPlasser;
		this.beskrivelse = beskrivelse;
	}
	
	public int getRomNr() {
		return romNr;
	}
	public String getSted() {
		return sted;
	}
	public int getAntPlasser() {
		return antPlasser;
	}
	public String getBeskrivelse() {
		return beskrivelse;
	}
	
	public void reserverRom(Rom rom, String start, String slutt, String dato){
		/*
		 * Oppretter en reservasjon mellom et gitt rom og en gitt avtale i et gitt tidsrom.
		 */
	}
	
	public void slettReservasjon(Rom rom, String start, String slutt, String dato){

	}
	
	public boolean sjekkReservasjon(Rom rom, String start, String slutt, String dato){
		
		/*
		 * Sjekker kun om rommet har en RomAvtale-relasjon i gitt tidsrom.
		 */
		
		return true;
		
	}
}

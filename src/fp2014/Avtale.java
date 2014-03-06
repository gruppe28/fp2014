package fp2014;

public class Avtale {
	
	/*
	 * muligens best å ha en metode som ser på alle endringer som er gjort i avtale, og deretter endrer dette i databasen???? Koble dette til lagre knappen elns.
	 */

	private int avtaleNr;
	private String navn;
	private String startTidspunkt;
	private String sluttTidspunkt;
	private String beskrivelse;
	private String sted;
	private String dato;
	private String opprettetAv;
	private Rom rom;
	
	public String getOpprettetAv() {
		return opprettetAv;
	}
	public void setOpprettetAv(String brukernavn){
		this.opprettetAv = brukernavn;
		/*
		 * Lager opprettetAv relasjonen i databasen mellom brukernavn og avtale.
		 */
	}
	public int getAvtaleNr() {
		return avtaleNr;
	}
	public String getNavn() {
		return navn;
	}
	public void setNavn(String navn) {
		this.navn = navn;
	}
	public String getStartTidspunkt() {
		return startTidspunkt;
	}
	public void setStartTidspunkt(String startTidspunkt) {
		this.startTidspunkt = startTidspunkt;
	}
	public String getSluttTidspunkt() {
		return sluttTidspunkt;
	}
	public void setSluttTidspunkt(String sluttTidspunkt) {
		this.sluttTidspunkt = sluttTidspunkt;
	}
	public String getBeskrivelse() {
		return beskrivelse;
	}
	public void setBeskrivelse(String beskrivelse) {
		this.beskrivelse = beskrivelse;
	}
	public String getSted() {
		return sted;
	}
	public void setSted(String sted) {
		this.sted = sted;
	}
	public String getDato() {
		return dato;
	}
	public void setDato(String dato) {
		this.dato = dato;
	}
	
	public void endreAvtale(Avtale avtale){
		/*
		 * Kun tilgjengelig for den som har en opprettetAv-relasjon, og gir tilgang til alle setterne. 
		 */
	}
	
	public void nyAvtale(){
		/*
		 * Oppretter en ny avtale, hvorpå den bruker som er innlogget blir satt som "eier" til denne avtalen.
		 */
	}
	
	public void leggTilDeltager(Ansatt ansatt){
		/*
		 * Legger til ansatte som deltagere, opprettet avtaler hos disse med gitt tidspunkt etc.
		 */
	}
	
	public void endreStatus(Ansatt ansatt, boolean status){
		/*
		 * setter deltagelsesstatus til hver enkelt deltager.
		 * Hver deltager kan kanskje ha tilgang til denne metoden for seg selv, 
		 * vet ikke hvordan vi skal implementere det.
		 */
	}
	
	public void slettDeltager(Ansatt ansatt){
		/*
		 * fjerner alle relasjoner ansatt har med denne avtalen, kaller opp databasen, kan løses fint vha cascade spørringer.
		 */
	}
	
	public void endreTidspunkt(String start, String slutt){
		/*
		 * Endrer tidspunktet til denne avtalen, må finne en måte å skille mellom nyopprettet avtale, og endring på tidspunkt fra en eksisterende avatle.
		 */
		this.setStartTidspunkt(start);
		this.setSluttTidspunkt(slutt);
	}
	
	public void endreRom(Rom rom, String start, String slutt, String dato){
		/*
		 * Oppretter en romreservasjon-relasjon mellom rommet og avtalen, må kalle sjekkReservasjon etc.
		 */
	}
}

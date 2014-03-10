package fp2014;

public class Appointment {
	
	/*
	 * muligens best å ha en metode som ser på alle endringer som er gjort i avtale, og deretter endrer dette i databasen???? Koble dette til lagre knappen elns.
	 */

	private int appointmentNr;
	private String name;
	private String startTime;
	private String endTime;
	private String description;
	private String place;
	private String startDate;
	private String endDate;
	private Ansatt madeBy;
	private Rom rom;
	
	public Appointment(String name, String startTime, String endTime, String description, String place, String startDate, String endDate, Ansatt madeBy) {
		this.setName(name);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setDescription(description);
		this.setPlace(place);
		this.setStartDate(startDate);
		this.setEndDate(endDate);
		this.setMadeBy(madeBy);
	}
	
	public Ansatt getMadeBy() {
		return madeBy;
	}
	public void setMadeBy(Ansatt username){
		this.madeBy = username;
		/*
		 * Lager opprettetAv relasjonen i databasen mellom brukernavn og avtale.
		 */
	}
	public int getAppointmentNr() {
		return appointmentNr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
		/*
		 * Kun tilgjengelig for den som har en opprettetAv-relasjon, og gir tilgang til alle setterne. 
		 */
	
	
	public void newAppointment(){
		/*
		 * Oppretter en ny avtale, hvorpå den bruker som er innlogget blir satt som "eier" til denne avtalen.
		 * 
		 * møteleder må settes som deltager, svare at han/hun kommer, avtalen må bli lagret til gitt bruker
		 */
		
		
	}
	
	public void addParticipant(Ansatt ansatt){
		/*
		 * Legger til ansatte som deltagere, opprettet avtaler hos disse med gitt tidspunkt etc.
		 */
	}
	
	public void changeStatus(Ansatt ansatt, boolean status){
		/*
		 * setter deltagelsesstatus til hver enkelt deltager.
		 * Hver deltager kan kanskje ha tilgang til denne metoden for seg selv, 
		 * vet ikke hvordan vi skal implementere det.
		 */
	}
	
	public void removeParticipant(Ansatt ansatt){
		/*
		 * fjerner alle relasjoner ansatt har med denne avtalen, kaller opp databasen, kan løses fint vha cascade spørringer.
		 */
	}
	
	public void changeTime(String start, String end){
		/*
		 * Endrer tidspunktet til denne avtalen, må finne en måte å skille mellom nyopprettet avtale, og endring på tidspunkt fra en eksisterende avatle.
		 */
		this.setStartTime(start);
		this.setEndTime(end);
	}
	
	public void changeRoom(Rom rom, String start, String end, String date){
		/*
		 * Oppretter en romreservasjon-relasjon mellom rommet og avtalen, må kalle sjekkReservasjon etc.
		 */
	}
}

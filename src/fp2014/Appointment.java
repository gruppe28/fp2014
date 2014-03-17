package fp2014;

import java.awt.Color;
import java.util.HashMap;

import database.Database;

public class Appointment {
	
	/*
	 * muligens best � ha en metode som ser p� alle endringer som er gjort i avtale, og deretter endrer dette i databasen???? Koble dette til lagre knappen elns.
	 */

	private int appointmentNr;
	private String name;
	private String startTime;
	private String endTime;
	private String description;
	private String place;
	private String Date;
	private Ansatt madeBy;
	private Rom rom;
	private HashMap<Ansatt, Integer> participants = new HashMap<Ansatt, Integer>();
//	private ArrayList<Ansatt> participants = new ArrayList<Ansatt>();
	
	public Appointment (){
	}
	
	public Appointment(int appointmentNr, String name, String startTime, String endTime, String description, String place, Rom room, String startDate, Ansatt madeBy) {
		this.setAppointmentNr(appointmentNr);
		this.setName(name);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setDescription(description);
		this.setPlace(place);
		this.setRom(room);
		this.setDate(startDate);
		this.setMadeBy(madeBy);
	}
	
	public void edit(String name, String startTime, String endTime, String description, String startDate, Ansatt madeBy, HashMap<Ansatt, Integer> participants) {
//		this.setAppointmentNr(appointmentNr);
		this.setName(name);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setDescription(description);
		this.setDate(startDate);
		this.setMadeBy(madeBy);
		this.setParticipants(participants);
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
	public String getDate() {
		return Date;
	}

	public void setDate(String Date) {
		this.Date = Date;
	}
	
	public Rom getRom() {
		return rom;
	}
	
	public void setRom(Rom rom) {
		this.rom = rom;
	}

	
		/*
		 * Kun tilgjengelig for den som har en opprettetAv-relasjon, og gir tilgang til alle setterne. 
		 */
	
	
	public void sendAppoinmentToDatabase(){
		
		/*
		 * Oppretter en ny avtale, som sendes til databasen, f�r et avtalenummer.
		 * 		db.update("Insert Into Avtale(navn, starttidspunkt, sluttidspunkt, beskrivelse, sted, dato, romNr, opprettetAv) Values('" + appointment.getName() 
				+ "', '" + appointment.getStartTime()
				+ "', '" + appointment.getEndTime() 
				+ "', '" + appointment.getDescription() 
				+ "', '" + appointment.getPlace() 
				+ "', '" + appointment.getDate() 
				+ "', '" + appointment.getRom().getRomNr() 
				+ "', '" + appointment.getMadeBy().getBrukernavn() + "')");
		 */
		
		Database db = new Database();
		if (this.getRom() == null) {
			db.update("Insert Into Avtale(navn, starttidspunkt, sluttidspunkt, beskrivelse, sted, dato, opprettetAv) Values('" + this.getName() 
					+ "', '" + this.getStartTime()
					+ "', '" + this.getEndTime() 
					+ "', '" + this.getDescription() 
					+ "', '" + this.getPlace() 
					+ "', '" + this.getDate() 
					+ "', '" + this.getMadeBy().getBrukernavn() + "')");

		} else {
			db.update("Insert Into Avtale(navn, starttidspunkt, sluttidspunkt, beskrivelse, dato, romNr, opprettetAv) Values('" 
					+ this.getName() 
					+ "', '" + this.getStartTime()
					+ "', '" + this.getEndTime() 
					+ "', '" + this.getDescription() 
					+ "', '" + this.getDate() 
					+ "', '" + this.getRom().getRomNr() 
					+ "', '" + this.getMadeBy().getBrukernavn() 
					+ "')");

		}
		db.close();
				
	}
	
	public void addParticipant(Ansatt ansatt, Appointment appointment){
		/*
		 * Legger til ansatte som deltagere, opprettet avtaler hos disse med gitt tidspunkt etc.
		 */
		// TODO: Legg til at ansatt blir varslet om sin invitasjon, en ny instans av denne avtalen blir lagt til der ogs�.
		
		Database db = new Database();
		
		//Adding a AnsattAvtale-relation
		db.update("Insert into AnsattAvtale values("+appointment.getAppointmentNr()+", "+"'"+ansatt.getBrukernavn()+"'"+", null)");
		db.close();
		
		//
	}
	
	public void changeStatus(Ansatt ansatt, boolean status, Appointment appointment){
		/*
		 * setter deltagelsesstatus til hver enkelt deltager.
		 * Hver deltager kan kanskje ha tilgang til denne metoden for seg selv, 
		 * vet ikke hvordan vi skal implementere det.
		 */
		
		int intstatus;
		if (status) {
			intstatus = 1;
		}else {
			intstatus = 0;
		}
		
		Database db = new Database();
		db.update("Update AnsattAvtale Set deltar='"+intstatus+"' Where avtaleNr = " + "'" + appointment.getAppointmentNr() + "' And brukernavn = '"+ ansatt.getBrukernavn() +"'");
		db.close();
	}
	
	public void removeParticipantDB(Ansatt ansatt, Appointment appointment){
		/*
		 * fjerner alle relasjoner ansatt har med denne avtalen, kaller opp databasen, kan l�ses fint vha cascade sp�rringer.
		 */
		
		//TODO: Fjerne relasjoner mellom ansatt og avtalen, samt evt varsler som er opprettet
		Database db = new Database();
		db.update("Delete from AnsattAvtale Where avtaleNr = '" + appointment.getAppointmentNr() + "' And brukernavn = '" + ansatt.getBrukernavn() + "'");
		db.close();
	}
	
	public void changeTime(String start, String end){
		/*
		 * Endrer tidspunktet til denne avtalen, m� finne en m�te � skille mellom nyopprettet avtale, og endring p� tidspunkt fra en eksisterende avatle.
		 */
		this.setStartTime(start);
		this.setEndTime(end);
	}
	
	public void changeRoom(Rom rom, String start, String end, String date){
		/*
		 * Oppretter en romreservasjon-relasjon mellom rommet og avtalen, m� kalle sjekkReservasjon etc.
		 */
	}

	public void setAppointmentNr(int appointmentNr) {
		this.appointmentNr = appointmentNr;
	}
	
	public String toString(){
		if (this.getRom() == null) {
			return ("Avtalenavn: " + this.getName() +
					"\nAvtalebeskrivelse: " + this.getDescription() +
					"\nStarttid: " + this.getStartTime() +
					"\nSluttid: " + this.getEndTime() +
					"\nDato: " + this.getDate().toString() +
					"\nSted: " + this.getPlace() +
					"\nRom: " +
					"\nOpprettet av: " + this.getMadeBy().getBrukernavn() +
					"");
		} else {
			return ("Avtalenavn: " + this.getName() +
					"\nAvtalebeskrivelse: " + this.getDescription() +
					"\nStarttid: " + this.getStartTime() +
					"\nSluttid: " + this.getEndTime() +
					"\nDato: " + this.getDate().toString() +
					"\nSted: " + this.getPlace() +
					"\nRom: " + this.getRom().getSted() +
					"\nOpprettet av: " + this.getMadeBy().getBrukernavn() +
					"");
		}
	}

	public HashMap<Ansatt, Integer> getParticipants() {
		return participants;
	}

	public void setParticipants(HashMap<Ansatt, Integer> participants) {
		this.participants = participants;
	}
	
	public void editParticipant(Ansatt username, Integer status){
		this.participants.put(username, status);
	}
	
	public void removeParticipant(Ansatt user){
		this.participants.remove(user);
	}
	
	public int getParticipantStatus(Ansatt user){
		if (this.participants.containsKey(user)) {
			return this.participants.get(user);			
		} else {
			return (Integer) null;
		}
	}
	
	public Color getStatusColor(){
		boolean allAttending = true;//false
		boolean someAttending = false;//true
		boolean awaitingAnswer = false;//false
		
		for (int p: participants.values()){
			if (p == 2){
				allAttending = false;
				awaitingAnswer = true;
			}else if(p == 0){
				allAttending = false;
			}else if(p == 1){
				someAttending = true;
			}
		}
		
		if (allAttending){
			return Color.GREEN;
		}else if(someAttending && awaitingAnswer){
			return Color.YELLOW;
		}else if(someAttending && !awaitingAnswer){
			return Color.ORANGE;
		}else if (!someAttending && !awaitingAnswer){
			return Color.RED;
		}else if(!someAttending && awaitingAnswer){
			return Color.YELLOW;
		}
		return Color.MAGENTA;
	}
	
	public String getLocation(){
		if (place == null){
			return rom.getSted();
		}else{
			return place;
		}
	}
	
	
}
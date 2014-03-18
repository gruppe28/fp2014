package fp2014;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;

import database.SQL;

public class Appointment implements Serializable {
	
	/*
	 * muligens best � ha en metode som ser p� alle endringer som er gjort i avtale, og deretter endrer dette i databasen???? Koble dette til lagre knappen elns.
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 4109998445878823995L;
	private int appointmentNr;
	private String name;
	private String startTime;
	private String endTime;
	private String description;
	private String place;
	private String Date;
	private User madeBy;
	private Room rom;
	private HashMap<User, Integer> participants = new HashMap<User, Integer>();
//	private ArrayList<Ansatt> participants = new ArrayList<Ansatt>();
	
	public Appointment (){
	}
	
	public Appointment(int appointmentNr, String name, String startTime, String endTime, String description, String place, Room room, String startDate, User madeBy) {
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
	
	public void edit(String name, String startTime, String endTime, String description, String startDate, User madeBy, HashMap<User, Integer> participants) {
//		this.setAppointmentNr(appointmentNr);
		this.setName(name);
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.setDescription(description);
		this.setDate(startDate);
		this.setMadeBy(madeBy);
		this.setParticipants(participants);
	}
	
	public User getMadeBy() {
		return madeBy;
	}
	public void setMadeBy(User username){
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
	
	public Room getRom() {
		return rom;
	}
	
	public void setRom(Room rom) {
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
		
		SQL db = new SQL();
		if (this.getRom() == null) {
			db.update("Insert Into Avtale(navn, starttidspunkt, sluttidspunkt, beskrivelse, sted, dato, opprettetAv) Values('" + this.getName() 
					+ "', '" + this.getStartTime()
					+ "', '" + this.getEndTime() 
					+ "', '" + this.getDescription() 
					+ "', '" + this.getPlace() 
					+ "', '" + this.getDate() 
					+ "', '" + this.getMadeBy().getUsername() + "')");
		} else {
			db.update("Insert Into Avtale(navn, starttidspunkt, sluttidspunkt, beskrivelse, dato, romNr, opprettetAv) Values('" 
					+ this.getName() 
					+ "', '" + this.getStartTime()
					+ "', '" + this.getEndTime() 
					+ "', '" + this.getDescription() 
					+ "', '" + this.getDate() 
					+ "', '" + this.getRom().getRoomNumber() 
					+ "', '" + this.getMadeBy().getUsername() 
					+ "')");
		}
		db.close();		
	}
	
	public void addParticipant(User ansatt, Appointment appointment){
		/*
		 * Legger til ansatte som deltagere, opprettet avtaler hos disse med gitt tidspunkt etc.
		 */
		
		SQL db = new SQL();
		
		//Adding a AnsattAvtale-relation
		db.update("Insert into AnsattAvtale values("+appointment.getAppointmentNr()+", "+"'"+ansatt.getUsername()+"'"+", null)");
		db.close();
	}
	
	public void changeStatus(User ansatt, boolean status, Appointment appointment){
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
		
		SQL db = new SQL();
		db.update("Update AnsattAvtale Set deltar='"+intstatus+"' Where avtaleNr = " + "'" + appointment.getAppointmentNr() + "' And brukernavn = '"+ ansatt.getUsername() +"'");
		db.close();
	}
	
	public void removeParticipantDB(User ansatt, Appointment appointment){
		/*
		 * fjerner alle relasjoner ansatt har med denne avtalen, kaller opp databasen, kan l�ses fint vha cascade sp�rringer.
		 */
		
		SQL db = new SQL();
		db.update("Delete from AnsattAvtale Where avtaleNr = '" + appointment.getAppointmentNr() + "' And brukernavn = '" + ansatt.getUsername() + "'");
		db.close();
	}
	
	public void changeTime(String start, String end){
		/*
		 * Endrer tidspunktet til denne avtalen, m� finne en m�te � skille mellom nyopprettet avtale, og endring p� tidspunkt fra en eksisterende avatle.
		 */
		
		this.setStartTime(start);
		this.setEndTime(end);
	}
	
	public void changeRoom(Room rom, String start, String end, String date){
		/*
		 * Oppretter en romreservasjon-relasjon mellom rommet og avtalen, m� kalle sjekkReservasjon etc.
		 */
	}

	public void setAppointmentNr(int appointmentNr) {
		this.appointmentNr = appointmentNr;
	}
	
	public String toString(){
		if (this.getRom() == null) {
			return ("Appointment name: " + this.getName() +
					"\nDescription: " + this.getDescription() +
					"\nStarttime: " + this.getStartTime() +
					"\nEndtime: " + this.getEndTime() +
					"\nDate: " + this.getDate().toString() +
					"\nPlace: " + this.getPlace() +
					"\nRoom: " +
					"\nCreated by: " + this.getMadeBy().getUsername() +
					"");
		} else {
			return ("Appointment name: " + this.getName() +
					"\nDescription: " + this.getDescription() +
					"\nStarttime: " + this.getStartTime() +
					"\nEndtime: " + this.getEndTime() +
					"\nDate: " + this.getDate().toString() +
					"\nPlace: " + this.getPlace() +
					"\nRoom: " + this.getRom().getPlace() +
					"\nCreated by: " + this.getMadeBy().getUsername() +
					"");
		}
	}

	public HashMap<User, Integer> getParticipants() {
		return participants;
	}

	public void setParticipants(HashMap<User, Integer> participants) {
		this.participants = participants;
	}
	
	public void editParticipant(User username, Integer status){
		this.participants.put(username, status);
	}
	
	public void removeParticipant(User user){
		this.participants.remove(user);
	}
	
	public int getParticipantStatus(User user){
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
			return new Color(0xCCFFCC); // green
		}else if(someAttending && awaitingAnswer){
			return new Color(0xFFFFCC); // yellow
		}else if(someAttending && !awaitingAnswer){
			return new Color(0xFFE5CC); // orange
		}else if (!someAttending && !awaitingAnswer){
			return new Color(0xFFCCCC); // red
		}else if(!someAttending && awaitingAnswer){
			return new Color(0xFFFFCC); // yellow
		}
		return Color.MAGENTA;
	}
	
	public String getLocation(){
		if (place == null){
			return rom.getPlace();
		}else{
			return place;
		}
	}
	
	public boolean hasReservedRoom(){
		if (this.rom.isReservedToAppointment()){
			return true;
		}
		return false;
		
	}
	
	
}
package fp2014;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.Database;

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
	private ArrayList<Ansatt> participants;
	
	public Appointment(int appointmentNr, String name, String startTime, String endTime, String description, String place, String startDate, String endDate, Ansatt madeBy) {
		this.setAppointmentNr(appointmentNr);
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
	
	public void addParticipant(Ansatt ansatt, Appointment appointment){
		/*
		 * Legger til ansatte som deltagere, opprettet avtaler hos disse med gitt tidspunkt etc.
		 */
		// TODO: Legg til at ansatt blir varslet om sin invitasjon, en ny instans av denne avtalen blir lagt til der også.
		
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
	
	public void removeParticipant(Ansatt ansatt, Appointment appointment){
		/*
		 * fjerner alle relasjoner ansatt har med denne avtalen, kaller opp databasen, kan løses fint vha cascade spørringer.
		 */
		
		//TODO: Fjerne relasjoner mellom ansatt og avtalen, samt evt varsler som er opprettet
		
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
	
	public static void main(String[] args) {
		Appointment appointment = new Appointment(6, "Viktig avtale", "10:30", "13:00", "viktig!", null, "18.04.2014", "18.04.2014", new Ansatt("audunlib", null, null, null, null));
		Ansatt ansatt = appointment.getAnsatt("admin");

		appointment.changeStatus(ansatt, true, appointment);
	}

	public void setAppointmentNr(int appointmentNr) {
		this.appointmentNr = appointmentNr;
	}
	
	
}
package fp2014;

import java.io.Serializable;

public class Room implements Serializable {
	/*
	 * Rom legges til i databasen direkte, dette er evt noe vi kan legge til hvis vi blir ferdige f�r tiden.
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2101758399092291226L;
	private int roomNumber;
	private String place;
	private int capacity;
	private String description;
	
	public Room(int romNr, String sted, int antPlasser, String beskrivelse){
		this.roomNumber = romNr;
		this.place = sted;
		this.capacity = antPlasser;
		this.description = beskrivelse;
	}
	
	public int getRoomNumber() {
		return roomNumber;
	}
	public String getPlace() {
		return place;
	}
	public int getCapacity() {
		return capacity;
	}
	public String getDescription() {
		return description;
	}
	
	public boolean isReservedToAppointment(){
		if (this.place != null){
			return true;
		}
		return false;
	}
}

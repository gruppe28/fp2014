package fp2014;

public class Gruppe {
	
	/*
	 * Grupper håndteres utenfor kalenderklienten og fungerer som statiske objekter.
	 * Ved invitasjon av grupper, må alle brukere som er medlemmer få en møteinnkalling etc.
	 */

	private int id;
	private String navn;
	public int getId() {
		return id;
	}
	
	public String getNavn() {
		return navn;
	}
}

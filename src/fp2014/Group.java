package fp2014;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6966846658780701887L;
	String name;
	ArrayList<String> members;

	public Group(String name){
		this.name = name;
		members = new ArrayList<String>();
	}
	
	public void addMember(String member){
		members.add(member);
	}
	
	public ArrayList<String> getMembers() {
		return members;
	}
	
	public String getName(){
		return name;
	}
	
	public String toString(){
		return name;
	}

}

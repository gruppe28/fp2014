package fp2014;

import java.util.ArrayList;

public class Group {

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

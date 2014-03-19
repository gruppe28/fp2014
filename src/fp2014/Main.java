package fp2014;

import client.UserDB;

public class Main {

	private UserDB usr;

	public Main() throws Exception {
		//usr = new UserDB();
	}

	public static void main(String[] args) throws Exception {

		Main main = new Main();
		
		// Tester:
		System.out.println(main.usr.userExists("admin"));
		System.out.println(main.usr.checkLogin("admin", "adminpw"));
	}

}

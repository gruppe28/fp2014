package fp2014;

import fp2014.database.User;

public class Main {

	private User usr;

	public Main() throws Exception {
		usr = new User();
	}

	public static void main(String[] args) throws Exception {

		Main main = new Main();
		System.out.println(main.usr.userExists("JanMann"));
		System.out.println(main.usr.checkLogin("JanMann", "janjanjanjanjan"));
	}

}

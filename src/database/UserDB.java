package database;

public class UserDB{
	
	public String feedback;
	private Client client;
	
	public UserDB(Client client){
		this.client = client;
	}
	
	// Checks the existence of a username with the database
	public boolean userExists(String inputUsername){
		client.sendMessage("userExists");
		client.sendMessage(inputUsername);
		boolean exists = (boolean)client.recieveObject();
		return exists;
	}
	
	// Check if a username/password combination is valid. Proper feedback is written to the feedback field
	public boolean checkLogin(String inputUsername, String inputPassword){
		
		boolean exists = userExists(inputUsername);

		client.sendMessage("findPassword");
		client.sendMessage(inputUsername);
		String password = (String)client.recieveObject();
		
		if (exists && (inputPassword.equals(password))) { return true; }
		else if (exists) { feedback = "Incorrect password."; }
		else { feedback = "Username does not exist."; }
		return false;
	}

}

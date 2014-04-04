package client;

@SuppressWarnings("unused")
public class UserDB{
	
	private String feedback;
	private Client client;
	private String username, password;
	
	public UserDB(Client client, String username, String password){
		this.client = client;
		this.username = username;
		this.password = password;
	}
	
	// Checks the existence of a username with the database
	public boolean userExists(){
		client.sendMessage("userExists");
		client.sendMessage(username);
		boolean exists = (boolean)client.recieveObject();
		return exists;
	}
	
	// Check if a username/password combination is valid. Proper feedback is written to the feedback field
	public boolean checkLogin(){
		
		boolean exists = userExists();

		client.sendMessage("findPassword");
		client.sendMessage(username);
		String password = (String)client.recieveObject();
		
		if (exists && (password.equals(password))) {
			return true;
			
		}else if (exists) {
			feedback = "Incorrect password.";
			
		}else {
			feedback = "User does not exist.";
			
		}
		return false;
	}
	
	public String getFeedback(){
		if (feedback == null){
			return "Something went wrong, please contact the system administrator";
		}
		return feedback;
	}

}

package database;

import java.io.*;
import java.net.*;
public class Client{
	
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
 	Boolean open;
 	
	public Client(){}
	
	public void run()
	{
		try{
			requestSocket = new Socket("localhost", 2828);
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			open = true;
			do{
				try{
					message = (String)in.readObject();
					sendMessage("Hi my server");
					message = "bye";
					//sendMessage(message);
				}
				catch(ClassNotFoundException classNot){
					System.err.println("Bad data!");
				}
			}while(!message.equals("bye"));
		}
		catch(UnknownHostException unknownHost){
			System.err.println("Bad host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	public void close(){
		System.out.println("Klient stenger.");
		try {
			open = false;
			out.writeObject("bye");
			in.close();
			out.close();
			requestSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	void sendObject(Object o)
	{
		try{
			out.writeObject(o);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	public Object recieveObject()
	{
		try{
			 return in.readObject();
		}
		catch(IOException | ClassNotFoundException ioException){
			ioException.printStackTrace();
			return null;
		}
	}
	
	public boolean isOpen(){
		return open;
	}
}
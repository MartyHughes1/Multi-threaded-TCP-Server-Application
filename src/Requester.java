

import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
 	Scanner input;
	Requester(){
		
		input = new Scanner(System.in);
	}
	void run()
	{
		try{
			//1. creating a socket to connect to the server
			
			requestSocket = new Socket("127.0.0.1", 2004);
			System.out.println("Connected to localhost in port 2004");
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			
			///Client Conversation......
			
			
			String choice = "";

			while (true) {
			    System.out.println("\n--- Library System ---");
			    System.out.println("1. Register");
			    System.out.println("2. Login");
			    System.out.println("3. Exit");
			    System.out.print("Choose: ");
			    choice = input.nextLine();

			    if (choice.equals("1")) {
			        registerUser();
			    } else if (choice.equals("2")) {
			        loginUser();     
			    } else if (choice.equals("3")) {
			        break;           
			    }
			}

		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
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
	
	private void registerUser() {
	    System.out.print("Enter Name: ");
	    String name = input.nextLine();

	    System.out.print("Enter Student ID: ");
	    String studentId = input.nextLine();

	    System.out.print("Enter Email: ");
	    String email = input.nextLine();

	    System.out.print("Enter Password: ");
	    String password = input.nextLine();

	    System.out.print("Enter Department: ");
	    String dept = input.nextLine();

	    System.out.print("Enter Role (Student/Librarian): ");
	    String role = input.nextLine();

	    String msg = "REGISTER|" + name + "|" + studentId + "|" + email + "|" + password + "|" + dept + "|" + role;

	    sendMessage(msg);

	    try {
	        String response = (String) in.readObject();
	        System.out.println("server> " + response);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	private void loginUser() {
	    System.out.print("Email: ");
	    String email = input.nextLine();

	    System.out.print("Password: ");
	    String password = input.nextLine();

	    // Send login command as one formatted string
	    sendMessage("LOGIN|" + email + "|" + password);

	    // Read response from server
	    try {
	        String response = (String) in.readObject();
	        System.out.println("server> " + response);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}



	public static void main(String args[])
	{
		Requester client = new Requester();
		client.run();
	}
}
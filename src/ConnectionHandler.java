import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread {
	
	private Socket connection;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private String message;
	public ConnectionHandler(Socket s)
	{
		connection = s;
	}
	
	public void run()
	{
	
		System.out.println("Connection received from " + connection.getInetAddress().getHostName());
		//3. get Input and Output streams
		
		try
		{
			out = new ObjectOutputStream(connection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(connection.getInputStream());
		}
		catch(IOException classnot)
		{
			
		}
		
		///Insert the Server Conversation......
		
		try {
		    while (true) {
		        message = (String) in.readObject();
		        
		        //splits message, and stores first command, as this is the user choice
		        String[] parts = message.split("\\|");  
		        String command = parts[0];              


		        if (message == null) break;

		        System.out.println("client> " + message);

		        if (command.equalsIgnoreCase("REGISTER")) {
		            handleRegister(message);         // keep as is
		        } else if (command.equalsIgnoreCase("LOGIN")) {
		            handleLogin(parts);             // pass the parts array
		        } else {
		            sendMessage("ERROR: Unknown command");
		        }

		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}

	
		//4: Closing connection
		try
		{
			in.close();
			out.close();
			connection.close();
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
	}

	void sendMessage(String msg)
	{
	try{
		out.writeObject(msg);
		out.flush();
		System.out.println("server>" + msg);
	}
	catch(IOException ioException){
		ioException.printStackTrace();
	}
	}
	
	//Method to deal with user inputs
	private void handleRegister(String msg) {
	    // Format:
	    // REGISTER|name|studentId|email|password|department|role

	    String[] parts = msg.split("\\|");

	    if (parts.length != 7) {
	        sendMessage("ERROR: Invalid REGISTER format");
	        return;
	    }

	    String name = parts[1];
	    String studentId = parts[2];
	    String email = parts[3];
	    String password = parts[4];
	    String dept = parts[5];
	    String role = parts[6];

	    // Check uniqueness
	    synchronized (Provider.users) {
	        for (User u : Provider.users) {
	            if (u.studentId.equals(studentId)) {
	                sendMessage("ERROR: Student ID already registered");
	                return;
	            }
	            if (u.email.equals(email)) {
	                sendMessage("ERROR: Email already registered");
	                return;
	            }
	        }

	        // Add new user
	        User newUser = new User(name, studentId, email, password, dept, role);
	        Provider.users.add(newUser);
	    }

	    sendMessage("SUCCESS: Registration completed");
	}

	private void handleLogin(String[] parts) {
	    // parts[0] = "LOGIN", parts[1] = email, parts[2] = password
	    if (parts.length != 3) {
	        sendMessage("ERROR: Invalid LOGIN format");
	        return;
	    }

	    String email = parts[1];
	    String password = parts[2];

	    synchronized (Provider.users) {
	        for (User u : Provider.users) {
	            if (u.email.equals(email) && u.password.equals(password)) {
	                sendMessage("SUCCESS: Logged in as " + u.name + " (" + u.role + ")");
	                return;
	            }
	        }
	    }

	    sendMessage("ERROR: Invalid email or password");
	}



}

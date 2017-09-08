package boxConnex.boxConnex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class App implements Runnable {

	private Scanner scan;
	private DatabaseConnector database;
	private Box box;
	private ServerSocket serverSocket;
	private Socket client;
	private InputStreamReader isr ;
	private BufferedReader in;
	private PrintWriter w;
	
	private BufferedReader r;
   
	
	
	/* timers to check if client has timed out */
	private long startTime;
	
	public App(Socket client) {
		this.client = client;
		  System.out.println("Client Connected!: " + client.getInetAddress());
		scan = new Scanner(System.in);
		database = new DatabaseConnector();
		

	//	 box = new Box();
		
	}
	
	public boolean tokenConfirm(String token) {
		
		if (token.equals("abc")) {
			System.out.println("Token OK");
			return true;
		}
		
		System.out.println("Token BAD");
		return false;
		
	}
	
	public void performAction(String inputLine) {
		System.out.println("Performing action on input:" + inputLine);
		String[] input = inputLine.split(":");
		
		
		// check to see if client wants to login --> receive token
		if (input[0].equals("LOGIN")) {
			String username = input[1]; 
			String password = input[2];
			System.out.println("Username:"+username+"password:"+password);
			/* authorize (just email for now) */
			if (database.checkEmail(username)) {
				Random random = new SecureRandom();
				String token = new BigInteger(130, random).toString(32);
				w.println("TOKEN:"+token);
			}
			/* create token and store in DB */
			
			
			/* return token to user (user will use token on all subsequent sends until it receives a bad */
			return;
		}
		
		
		
	// message received from client, check token
		if (tokenConfirm(input[0])) {
			w.println("OK");
			return;
		}
		
		if (!tokenConfirm(input[0])) {
			w.println("BAD");
			return;
		} 
		
		
/*		String[] input = inputLine.split(" ");
	
		System.out.println("Performing action on input:" + inputLine);
	
		
		if (input[0].equals("list")) {
			ArrayList<String[]> dirs;
		
			if ( ( dirs = database.getDirs()) != null) {
				for (int i = 0; i < dirs.size(); i++) {
					System.out.println(dirs.get(i)[0] + " : " + dirs.get(i)[1] + "<->" + dirs.get(i)[2] );
				}
				return;
			}
			System.out.println("No directories setup for syncing");
		}
		
		if (input[0].equals("link")) {
			if (input.length < 3) {
				System.out.println("Syntax: link <local_directory> <remote_directory>");
				return;
			}
			database.linkDirectory(input[1], input[2]);
		}
		if (input[0].equals("unlink")) {
			if (input.length < 2) {
				System.out.println("Syntax: unlink <local_directory>");
				return;
			}
		database.unlinkDirectory(input[1]);
		}
		
		*/
	}

	public void run() {
		// TODO Auto-generated method stub
		 try {

	                r = new BufferedReader(new InputStreamReader(client.getInputStream()));
	                w = new PrintWriter(client.getOutputStream(), true);
	                w.println("Welcome!");
	                String line = null;
	                
	              do {
	                
		                    line = r.readLine();
		                    
		                    if ( line != null ) {
		                        System.out.println("Received:" + line);
		                
		                        // if we receive a PING, reset timer, and continue next iteration
		                        // else, perform action on input received from client
		                       
		                     
		                        performAction(line); 
		                       
		                    }
		                    
		                    System.out.println("looping");
	            	  
	                    // loop as long as client doesn't initiate bye or timer doesn't exceed 10 seconds since ping
	                }  while ( !line.trim().equals("bye")  ) ;
	              
	                client.close();
	                System.out.println("Client disconnected");
	            
	        }
	        catch (Exception err)
	        {
	            System.err.println(err);
	        }
		
	}
	

	
	


}

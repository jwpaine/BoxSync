package boxConnex.boxConnex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
	
	
	
	/* timers to check if client has timed out */
	private long startTime;
	
	public App(Socket client) {
		this.client = client;
		  System.out.println("Client Connected!: " + client.getInetAddress());
		scan = new Scanner(System.in);
		database = new DatabaseConnector();
		

		 box = new Box();
		
	}
	
	public void performAction(String inputLine) {
		
		
		String[] input = inputLine.split(" ");
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
	}

	public void run() {
		// TODO Auto-generated method stub
		 try {

	                BufferedReader r = new BufferedReader(new InputStreamReader(client.getInputStream()));
	                PrintWriter w = new PrintWriter(client.getOutputStream(), true);
	                w.println("Welcome!");
	                String line = null;
	                
	              do {
	                
		                    line = r.readLine();
		                    
		                    if ( line != null ) {
		                        w.println("Received:"+ line);
		                        // if we receive a PING, reset timer, and continue next iteration
		                        // else, perform action on input received from client
		                        System.out.println("Attempting to perform action on data:" + line);
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

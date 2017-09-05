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
	private Socket clientSocket;
	private InputStreamReader isr ;
	private BufferedReader in;
	private PrintWriter w;
	public App() {
		scan = new Scanner(System.in);
		database = new DatabaseConnector();
		box = new Box();
		
		 try
	        {
	            serverSocket = new ServerSocket(10001);
	        }
	        catch (Exception err)
	        {
	            System.out.println(err);
	        }
		
		
	}
	
	public void performAction(String inputLine) {
		
		String[] input = scan.nextLine().split(" ");
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
			 Socket client = serverSocket.accept();
			  System.out.println("Client Connected!: " + client.getInetAddress());
	            
	                BufferedReader r = new BufferedReader(new InputStreamReader(client.getInputStream()));
	                PrintWriter w = new PrintWriter(client.getOutputStream(), true);
	                w.println("Welcome!");
	                String line;
	                do
	                {
	                    line = r.readLine();
	                    
	                    if ( line != null ) {
	                        w.println("Received:"+ line);
	                        System.out.println("Attempting to perform action on data:" + line);
	                    	
	                    }
	                    performAction(line);
	                    
	                } while ( !line.trim().equals("bye") );
	                
	                client.close();
	                System.out.println("Client disconnected");
	            
	        }
	        catch (Exception err)
	        {
	            System.err.println(err);
	        }
		
	}
	


}

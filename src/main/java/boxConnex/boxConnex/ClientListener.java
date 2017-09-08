package boxConnex.boxConnex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientListener implements Runnable {

	private ServerSocket serverSocket;
	private Socket clientSocket;
	
	public ClientListener() {
		 try
	        {
	           serverSocket = new ServerSocket(10005);
	           System.out.println("Server listening on port 10001");
	        }
	        catch (Exception err)
	        {
	            System.out.println(err);
	        }
	}
	
	public void run() {
		
		while(true) {
				 try {
					 
					System.out.println("Server waiting to accept new client");
					 
					Socket client = serverSocket.accept();
					 (new Thread(new App(client))).start();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
				 
	}	  
		    
}

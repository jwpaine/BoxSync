package boxConnexClient;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;



public class Interface implements Runnable {
	
	private Scanner scan;
	
	private InputStreamReader isr;
	private BufferedReader in;	
	
	
	
	private String client_id; 
	private String security_token;
	
	private String url = "https://account.box.com/api/oauth2/authorize?response_type=code&client_id=" + client_id + "&state=security_token%25" + security_token;
	
	private String token;
	
	  private Socket s;
      private BufferedReader r;
      private PrintWriter w;
      private BufferedReader con;
	
	public Interface() {
		scan = new Scanner(System.in);
		
		
		client_id = "secret"; 
		security_token = "secret";
		
		
		url = "https://account.box.com/api/oauth2/authorize?response_type=code&client_id=" + client_id + "&state=security_token%25" + security_token;
		
		token = "xyz";
		
		
	}
	
	public void run() {
		try
        {
            s = new Socket("127.0.0.1", 10005);
            r = new BufferedReader(new InputStreamReader(s.getInputStream()));
            w = new PrintWriter(s.getOutputStream(), true);
            con = new BufferedReader(new InputStreamReader(System.in));
        
            String line;
            do {
 
               line = r.readLine();
                
               if ( line != null ) {
                    System.out.println(line);
               } 
               
               System.out.print(">>");
               /* perform action on input */
               String input = con.readLine();
               performAction(input);
                
               
               } // while ( !line.trim().equals("bye") );
            while(true);
            

        } 
        catch (Exception err)
        {
            System.err.println(err);
        }
			
	}
public void performAction(String inputLine) {
		
		
	/*	String[] input = inputLine.split(" ");
		System.out.println("Performing action on input:" + inputLine);
	
		
		if (input[0].equals("authorize")) {
			this.authorize();
			return;
		}
		
		*/
		
		/* if method doesn't return early, per above, send inputLine to server */
      
    //    String out = token + ":" + inputLine + "\n";
		System.out.println("sending to server:"  + inputLine);
        w.println(inputLine);
		
				
}
	public String authorize() {
		
		 try {
			 	/* call default browser with GET request
			 	 * redirect url set to localhost:7888
			 	 * capture response, parse for code */
				Desktop.getDesktop().browse(java.net.URI.create(url));
				ServerSocket serverSocket = new ServerSocket(7888); 
				Socket clientSocket = serverSocket.accept();
				InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
				BufferedReader in = new BufferedReader(isr);			
				String inputLine = in.readLine();
				
				System.out.println(inputLine);
				// return code if code exists in inputLine (else, access was denied)
				if (inputLine.split("code=").length > 1) {
					PrintWriter w = new PrintWriter(clientSocket.getOutputStream(), true);
					w.println("HTTP/1.0 200 OK \r\n Content-Length: 20\r\n BINGO \r\n");
					w.close();
					serverSocket.close();
					return inputLine.split("code=")[1].split(" ")[0];
					
				}
				clientSocket.close();
				serverSocket.close();
				return null;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				return null;
			}
	        
	}
}

package boxConnexClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;



public class Interface implements Runnable {
	
	private Scanner scan;
	
	private InputStreamReader isr;
	private BufferedReader in;	
	
	
	private PrintWriter w;
	
	public Interface() {
		scan = new Scanner(System.in);
	
		
		
	}
	
	public void run() {
		try
        {
            Socket s = new Socket("127.0.0.1", 10001);
            BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter w = new PrintWriter(s.getOutputStream(), true);
            BufferedReader con = new BufferedReader(new InputStreamReader(System.in));
        
            String line;
            do {
 
                line = r.readLine();
                
               if ( line != null ) {
                    System.out.println(line);
               }
               
               System.out.print(">>");
                line = con.readLine();
                line = line + "\n";
                w.println(line);
               } while ( !line.trim().equals("bye") );

        } 
        catch (Exception err)
        {
            System.err.println(err);
        }
			
	}
}

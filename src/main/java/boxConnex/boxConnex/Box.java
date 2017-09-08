package boxConnex.boxConnex;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.box.sdk.BoxAPIConnection;
import com.box.sdk.BoxFolder;
import com.box.sdk.BoxItem;

public class Box {
	  
       private String client_id;
       private String security_token;
       private String url;
       private String auth_code = null;
       private BoxAPIConnection api;
       
	public Box() {
		
		/*
		client_id = "secret"; 
		security_token = "secret";
		*/
		
		client_id = "g2bawbzo7yvg4qj61747jpe5v1eqzmvn"; 
		security_token = "bwEUHi61lTTx3V2OkVGy8Pi7fs3DV2iR";
		
		url = "https://account.box.com/api/oauth2/authorize?response_type=code&client_id=" + client_id + "&state=security_token%25" + security_token;
		
			api = new BoxAPIConnection(client_id,security_token, authorize());
		
		
	    BoxFolder rootFolder = BoxFolder.getRootFolder(api);
	    for (BoxItem.Info itemInfo : rootFolder) {
	    	System.out.format("[%s] %s\n", itemInfo.getID(), itemInfo.getName());
	    }
		
	}
	
	public String getAuthCode() {
		return this.auth_code;
	}
		/* return authorization code from box.com after user authorizes app */
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

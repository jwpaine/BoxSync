package boxConnex.boxConnex;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		client_id = ""; 
		security_token = "";
		
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
					return inputLine.split("code=")[1].split(" ")[0];
				}
				
				return null;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	        
	}

}

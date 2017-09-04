package boxConnex.boxConnex;

import java.util.Scanner;

public class Interface implements Runnable {
	
	private Scanner scan;
	private DatabaseConnector database;
	public Interface() {
		scan = new Scanner(System.in);
		database = new DatabaseConnector();
		connectDB();
		
		
		
	}
	
	public void connectDB() {
		if (!database.doesExist()) {
			System.out.println("Database does not exist, creating...");
			/* create */
			database.create();
		}
		
		database.connect();
	}
	

	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			System.out.print(">>>");
			String input = scan.nextLine();
			System.out.println(input);
		}
		
	}

}

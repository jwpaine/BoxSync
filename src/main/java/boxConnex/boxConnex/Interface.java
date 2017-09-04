package boxConnex.boxConnex;

import java.util.Scanner;

public class Interface implements Runnable {
	
	private Scanner scan;
	private DatabaseConnector database;
	private Box box;
	public Interface() {
		scan = new Scanner(System.in);
		database = new DatabaseConnector();
		
		
		box = new Box();
		database.addDirectory("/local", "/remote");
		database.getLocalDirs();
		
		
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

package boxConnex.boxConnex;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {
	
	private String dbFile;
	private Connection conn;
	
	public DatabaseConnector() {
		dbFile = "test.db";
		conn = null;
		/* check if exist, create if not, connect in the end */
		if (!doesExist()) {
			System.out.println("Database does not exist, creating...");
			/* create */
			create();
		}
		
		connect();
		
	}
	/* check if database exists */
	public boolean doesExist() {
		File f = new File(dbFile);
		if(f.exists()) {
			return true;
		}
		return false;
	}
	public void create() {
		
			 /* create DB */
		        String url = "jdbc:sqlite:" + dbFile;
		 
		        try {
		        	conn = DriverManager.getConnection(url);
		            if (conn != null) {
		                DatabaseMetaData meta = conn.getMetaData();
		                System.out.println("The driver name is " + meta.getDriverName());
		                System.out.println("A new database has been created.");
		                
		                /* create table with columns 'local' (local directory) 
		                 * and 'remote' (corresponding remote directory for file sync 
		                 */
		                System.out.println("Creating table...");
		                
		                String sql = "CREATE TABLE IF NOT EXISTS directories (\n"
			                    + "	id integer PRIMARY KEY,\n"
			                    + "	local text NOT NULL,\n"
			                    + "	remote text NOT NULL\n"
			                    + ");";
			           
		                Statement stmt = conn.createStatement();
			                // create a new table
		                stmt.execute(sql);
		                System.out.println("Table created.");

		            }
		 
		        } catch (SQLException e) {
		            System.out.println(e.getMessage());
		        }
	}
	public void connect() {
       
        try {
            // db parameters
            String url = "jdbc:sqlite:" + dbFile;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }
	/* Add if not exist */
	public void addDirectory(String localDir, String remoteDir) {
        String sql = "INSERT INTO directories(local,remote) VALUES(?,?)";
 
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, localDir);
            pstmt.setString(2, remoteDir);
            pstmt.executeUpdate();
        } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
       
    }
 
	/* return a list of local directories setup for syncing */
	public String[] getLocalDirs() {
		String sql = "SELECT local FROM directories";
        
       try {
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql);
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getString("local") + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       /* return list of local directories. for now return null */
		return null;
	}

}

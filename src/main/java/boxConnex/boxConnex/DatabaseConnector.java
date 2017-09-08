package boxConnex.boxConnex;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
		                System.out.println("Creating table directories...");
		                
		                String sql = "CREATE TABLE IF NOT EXISTS directories (\n"
			                    + "	id integer PRIMARY KEY,\n"
			                    + "	local text NOT NULL,\n"
			                    + "	remote text NOT NULL\n"
			                    + ");";
		               
		                Statement stmt = conn.createStatement();
			                // create a new table
		                stmt.execute(sql);
		                System.out.println("Table created.");
		                
		                System.out.println("Creating table accounts...");
		                
		                sql = "CREATE TABLE IF NOT EXISTS accounts (\n"
			                    + "	id integer PRIMARY KEY,\n"
			                    + "	email text NOT NULL,\n"
			                    + "	token text NOT NULL\n"
			                    + ");";
		               
		                stmt = conn.createStatement();
			                // create a new table
		                stmt.execute(sql);
		                System.out.println("Table created.");
		                
		                System.out.println("Adding dummy account and token to begin");
		                
		                this.addAccount("john.paine", "abc");
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
	public boolean checkEmail(String email) {
		
		String query = "SELECT (count(*) > 0) as found FROM accounts WHERE email LIKE ?";
        
   
        try {
        	PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            Statement stmt  = conn.createStatement();
            ResultSet rs    = pst.executeQuery();
           
           // loop through the result set
            if (rs.next()) {
                boolean found = rs.getBoolean(1); // "found" column
                if (found) {
                    return true;
                } else {
                    
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
}
 
	public void addAccount(String email, String token) {
        String sql = "INSERT INTO accounts (email,token) VALUES(?,?)";
 
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, token);
            pstmt.executeUpdate();
            System.out.println("New email and token added for: " + email);
        } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }
	
	/* Add if not exist */
	public void linkDirectory(String localDir, String remoteDir) {
        String sql = "INSERT INTO directories(local,remote) VALUES(?,?)";
 
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, localDir);
            pstmt.setString(2, remoteDir);
            pstmt.executeUpdate();
            System.out.println("Sync enabled for (local -> remote): " + localDir + "->" + remoteDir);
        } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
    }
	/* remove if exist */
	public void unlinkDirectory(String localDir) {
        String sql = "DELETE from directories where local = ?";
 
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, localDir);
            pstmt.executeUpdate();
            System.out.println("Directory: " + localDir + " will not be synced");
        } catch (SQLException e) {
                System.out.println(e.getMessage());
        }
        
    }
 
	/* return a list of local,remote directory pairs enabled for syncing */
	
	public ArrayList<String[]> getDirs() {
		String sql = "SELECT id, local, remote FROM directories";
        ArrayList dirs = new ArrayList();
       try {
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql);
            
            // loop through the result set
            while (rs.next()) {
                String[] triplet = new String[3];
            	triplet[0] = String.valueOf(rs.getInt("id"));
            	triplet[1] = rs.getString("local");
            	triplet[2] = rs.getString("remote");
            	dirs.add(triplet);
            	return dirs;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
       /* return list of local directories. for now return null */
		return null;
	}

}

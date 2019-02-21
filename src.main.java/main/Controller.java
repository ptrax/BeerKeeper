package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import gui.MainFrame;

/**
 * Entry for BeerKeeper application. The SQL connection is set up and maintained here.
 * @author Paul Traxler
 *
 */
public class Controller {
	
	private static Controller instance = null;
	private Connection conn = null;
	private MainFrame mainFrame = null;
	
	/**
	 * Singleton constructor for Controller. If an instance is available, that instance
	 * is returned. If not, a new object is created and that's returned. 
	 * 
	 * @return Controller instance
	 */
	public static Controller getInstance() {
		if(instance == null) {
			new Controller();
		}
		
		return instance;
	}
	
	/**
	 * Private constructor for singleton Controller.
	 */
	private Controller() {
		Controller.instance = this;
		mainFrame = MainFrame.getInstance();
	}
	
	/**
	 * Sets up the connection to the database. The mySQL database must already be running, or this
	 * will obviously fail to connect. 
	 * @param url - the host:port of the connection, such as localhost:3306
	 * @param user - the username for the connection
	 * @param pass - the users password for the connection
	 * @return - True if a connection is established, false otherwise.
	 */
	public boolean connectionSetUp(String url, String user, String pass) {
		try {
			// Build the URL
			String conURL = "jdbc:mysql://" + url + "/Beerkeeper?autoReconnect=true&&useSSL=false" +
							"&&useLegacyDatetimeCode=false&&serverTimezone=America/New_York"; 
			
			// Set up the connection
			conn = DriverManager.getConnection(conURL, user, pass);
			conn.setAutoCommit(false);
			
			// If we get to this point without throwing an error, the connection was successful
			System.out.println("Connection Success");
			mainFrame = MainFrame.getInstance();
			mainFrame.showPanel("Home");
			return true;
			
		// If an error was thrown, tell us why
		} catch (SQLException e) {
			System.out.println("ERROR: " + e.getMessage());
		}
		
		// This happens if the connection failed.
		return false;
	}
	
	public Connection getConnection() {
		return this.conn;
	}
	
	/**
	 * Entry point for Beerkeeper application
	 * @param args
	 */
	public static void main(String args[]) {
		MainFrame.getInstance();
	}
}

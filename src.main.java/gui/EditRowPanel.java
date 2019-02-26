package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Controller;

/**
 * The Edit Row panel has the necessary UI and SQL to edit stock information to
 * the database. 
 * 
 * @author ptraxler
 *
 */
public class EditRowPanel extends JPanel {
	// Set up connection objects
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	PreparedStatement pStmt = null;
	
	// Get reference to this panel
	JPanel thisPanel = this;
	
	// Set up JLabels
	JLabel weeksServed = new JLabel("Weeks Served:", SwingConstants.CENTER);
	JLabel currentUnits = new JLabel("Current Units: ", SwingConstants.CENTER);
	JLabel desUnits = new JLabel("Desired Units: ", SwingConstants.CENTER);
	JLabel soldOverall = new JLabel("Sold Overall: ", SwingConstants.CENTER);
	JLabel price = new JLabel("Price: ", SwingConstants.CENTER);
	
	// Set up JTextFields
	JTextField weeksField = new JTextField();
	JTextField curUnitsField = new JTextField();
	JTextField desUnitsField = new JTextField();
	JTextField soldOverallField = new JTextField();
	JTextField priceField = new JTextField();

	// Set up JButtons
	JButton addButton = new JButton("Update");
	JButton cancelButton = new JButton("Cancel");
	
	// Variable declarations
	int beerID = 0;
	int pkgID = 0;
	int stockID = 0;
	int totalSold = 0;
	
	/** 
	 * Constructor for EditRowPanel class. Takes an open connection to the SQL
	 * database, as well as beerName and pkgName so we know what we're updating.
	 * @param conn - Open SQL connection 
	 * @param beerName - Name of beer to update
	 * @param pkgName - Packaging type of beer
	 */
	public EditRowPanel(String beerName, String pkgName) {
		this.conn = Controller.getInstance().getConnection();
		
		// Set up layout
		this.setLayout(new GridLayout(6,2));
		this.add(weeksServed);
		this.add(weeksField);
		this.add(desUnits);
		this.add(desUnitsField);
		this.add(currentUnits);
		this.add(curUnitsField);
		this.add(soldOverall);
		this.add(soldOverallField);
		this.add(price);
		this.add(priceField);
		this.add(cancelButton);
		this.add(addButton);
		
		try {
			// Get the total number of beers sold
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT SUM(SoldOverall) FROM STOCK");
			if(rs.next()) {
				totalSold = rs.getInt(1);
			}
			
			// Get the Beer ID from the name
			pStmt = conn.prepareStatement("SELECT BeerID FROM BEER WHERE Name = ?");
			pStmt.setString(1, beerName);
			rs = pStmt.executeQuery();
			
			if(rs.next()) {
				beerID = rs.getInt(1);
			}
			
			// Get the PkgID from the name 
			pStmt = conn.prepareStatement("SELECT PkgID FROM PACKAGING WHERE PkgName = ?");
			pStmt.setString(1, pkgName);
			rs = pStmt.executeQuery();
			
			if(rs.next()) {
				pkgID = rs.getInt(1);
			}
			
			// Get the stock information based on beerID and pkgID
			pStmt = conn.prepareStatement("SELECT * FROM STOCK WHERE BeerID = ? AND PkgID = ?");
			pStmt.setInt(1, beerID);
			pStmt.setInt(2, pkgID);
			rs = pStmt.executeQuery();
			
			// Set up the textfields with the current information
			if(rs.next()) {
				stockID = rs.getInt(1);
				weeksField.setText(rs.getString(3));
				curUnitsField.setText(rs.getString(4));
				desUnitsField.setText(rs.getString(5));
				soldOverallField.setText(rs.getString(6));
				priceField.setText(rs.getString(7));
			}
			
		} catch (SQLException e) {
			System.out.println(e);
		} finally {
			try {
				// Close Resources
				if (pStmt != null)
					pStmt.close();
				if (rs != null)
					rs.close();
				if (conn != null)
					conn.close();
				if (stmt != null) 
					stmt.close();
			} catch (SQLException f) {
				System.out.println(f.getMessage());
			}
		}
		
		/**
		 * Action listener for the Add button
		 */
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					conn = Controller.getInstance().getConnection();
					// Set up the UPDATE statement
					pStmt = conn.prepareStatement("UPDATE STOCK SET WeeksServed = ?, CurrentUnits = ?, DesiredUnits = ?, SoldOverall = ?, Price = ? WHERE BeerID = ? and PkgID = ?;");
					pStmt.setString(1, weeksField.getText());
					pStmt.setString(2, curUnitsField.getText());
					pStmt.setString(3, desUnitsField.getText());
					pStmt.setString(4, soldOverallField.getText());
					pStmt.setString(5, priceField.getText());
					pStmt.setInt(6, beerID);
					pStmt.setInt(7, pkgID);
					
					// Execute and commit
					pStmt.executeUpdate();
					conn.commit();
					
					// Update popularity
					pStmt = conn.prepareStatement("UPDATE POPULARITY SET TotalPplr = ?, TimePplr = ? WHERE StockID = ?");
					pStmt.setInt(1, (totalSold/(Integer.valueOf(soldOverallField.getText()))));
					pStmt.setInt(2, (Integer.valueOf(soldOverallField.getText())/Integer.valueOf(weeksField.getText())));
					pStmt.setInt(3, stockID);
					
					pStmt.executeUpdate();
					conn.commit();
					
				} catch (SQLException f) {
					System.out.println(f.getMessage());
				} finally {
					
					try {
						// Close Resources
						if (pStmt != null)
							pStmt.close();
						if (conn != null)
							conn.close();
					} catch (SQLException f) {
						System.out.println(f.getMessage());
					}
					
					// No matter if it passes or fails, let the parent know update has occured
					thisPanel.firePropertyChange("update", true, false);
				}
				
			}
		});
		
		/**
		 * If cancel is pressed, inform parent
		 */
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thisPanel.firePropertyChange("update", true, false);
			}
		});
	}

}
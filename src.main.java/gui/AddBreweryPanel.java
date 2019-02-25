package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Controller;

/**
 * The Add Brewery panel has the necessary UI and SQL to add Brewery information to
 * the database. 
 * 
 * @author ptraxler
 *
 */
public class AddBreweryPanel extends JPanel {
	// Set up connection variables
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	PreparedStatement pStmt = null;
	
	// Get reference to this panel
	JPanel thisPanel = this;
	
	// Label setup
	JLabel breweryName = new JLabel("Name:", SwingConstants.CENTER);
	JLabel breweryLocation = new JLabel("Location:", SwingConstants.CENTER);
	JLabel breweryPhone = new JLabel("Phone:", SwingConstants.CENTER);
	JLabel breweryEmail = new JLabel("Email:", SwingConstants.CENTER);
	
	// Textfield setup
	JTextField nameField = new JTextField();
	JTextField locationField = new JTextField();
	JTextField phoneField = new JTextField();
	JTextField emailField = new JTextField();

	// Button setup
	JButton addButton = new JButton("Add");
	JButton cancelButton = new JButton("Cancel");
	
	/**
	 * Constructor for AddBrewery Panel. Takes an open SQL connection object and uses it to 
	 * make queries. 
	 */
	public AddBreweryPanel() {
		this.setLayout(new GridLayout(5,2));
		
		// Set up layout
		this.add(breweryName);
		this.add(nameField);
		this.add(breweryLocation);
		this.add(locationField);
		this.add(breweryPhone);
		this.add(phoneField);
		this.add(breweryEmail);
		this.add(emailField);
		this.add(cancelButton);
		this.add(addButton);
		
		/**
		 * Action listener for the Add button. Gets the brewery ID and name, checks that we aren't trying to add 
		 * a duplicate, and then adds our new values into the database. 
		 */
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// Set up the connection
					conn = Controller.getInstance().getConnection();
					stmt = conn.createStatement();
					rs = stmt.executeQuery("SELECT BrewerID, Name FROM BREWERY ORDER BY BrewerID;");
					
					// Variables
					int brewerID = 0;
					boolean nameExists = false;
					
					// Loop through result set, checking that this won't be a duplicate entry and 
					// also finding the last brewery ID and incrementing off that for our new ID.
					while (rs.next()) {
						// Case sensitive check for duplicate
						String nameString = rs.getString(2).toLowerCase();
						if(nameField.getText().toLowerCase().equals(nameString)) {
							nameExists = true;
							break;
						}
						
						// Since this is an ordered list, we know if we increment the last result 
						// we will have a safe new ID. 
						if (rs.isLast()) {
							brewerID = rs.getInt(1) + 1;
						}
					}
					
					// If we didn't find that this was a duplicate in the last step, Insert the
					// new values into the BREWERY table
					if(!nameExists) {
						// Set up the statement
						pStmt = conn.prepareStatement("INSERT INTO BREWERY VALUES (?,?,?,?,?)");
						pStmt.setInt(1, brewerID);
						pStmt.setString(2, nameField.getText());
						pStmt.setString(3, locationField.getText());
						pStmt.setString(4, phoneField.getText());
						pStmt.setString(5, emailField.getText());
						
						// Execute and commit
						pStmt.executeUpdate();
						conn.commit();
						
						// Let the parent panel know that we have performed an add. 
						thisPanel.firePropertyChange("add", false, true);
					} else {
						// If the while loop DID find that this was a duplicate,
						// cancel this.
						cancelButton.doClick();
					}
				} catch (SQLException f) {
					System.out.println(f.getMessage());
				} finally {
					try {
						// Close Resources
						if (pStmt != null)
							pStmt.close();
						if (rs != null)
							rs.close();
						if (conn != null) {
							conn.close();
						} if (stmt != null) 
							stmt.close();
					} catch (SQLException f) {
						System.out.println(f.getMessage());
					}
				}
			}
		});
		
		/**
		 * This button will fire a property change listener that the parent can use to 
		 * close the screen.
		 */
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thisPanel.firePropertyChange("add", true, false);
			}
		});
	}

}
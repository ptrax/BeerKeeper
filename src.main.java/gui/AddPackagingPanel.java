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
 * The Add Packaging panel has the necessary UI and SQL to add Packaging information to
 * the database. 
 * 
 * @author ptraxler
 *
 */
public class AddPackagingPanel extends JPanel {
	// Set up connection variables
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	PreparedStatement pStmt = null;
	
	// Get reference to this panel
	JPanel thisPanel = this;
	
	// Set up label and textfield
	JLabel pkgName = new JLabel("Packaging Name:", SwingConstants.CENTER);
	JTextField pkgNameField = new JTextField();
	
	// Set up buttons
	JButton addButton = new JButton("Add");
	JButton cancelButton = new JButton("Cancel");
	
	/**
	 * Constructor for AddPackaging Panel. Takes an open SQL connection object and uses it to 
	 * make queries. 
	 * @param conn Open SQL connection
	 */
	public AddPackagingPanel() {
		this.setLayout(new GridLayout(2,2));
		
		// Set up layout
		this.add(pkgName);
		this.add(pkgNameField);
		this.add(cancelButton);
		this.add(addButton);
		
		/**
		 * Action listener for the Add button. Gets the packaging ID and name, checks that we aren't trying to add 
		 * a duplicate, and then adds our new values into the database. 
		 */
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					// Set up the connection	
					stmt = conn.createStatement();
					rs = stmt.executeQuery("SELECT * FROM PACKAGING ORDER BY PkgID;");
					
					// Variables
					int typeID = 0;
					boolean nameExists = false;
					
					// Loop through result set, checking that this won't be a duplicate entry and 
					// also finding the last packaging ID and incrementing off that for our new ID.
					while (rs.next()) {
						// Case Sensitive check for duplicate
						String nameString = rs.getString(2).toLowerCase();
						if(pkgNameField.getText().toLowerCase().equals(nameString)) {
							nameExists = true;
							break;
						}
						
						// Since this is an ordered list, we know if we increment the last result 
						// we will have a safe new ID. 
						if (rs.isLast()) {
							typeID = rs.getInt(1) + 1;
						}
					}
					
					// If we didn't find that this was a duplicate in the last step, Insert the
					// new values into the PACKAGING table
					if(!nameExists) {
						// Set up the statement
						pStmt = conn.prepareStatement("INSERT INTO PACKAGING VALUES (?,?)");
						pStmt.setInt(1, typeID);
						pStmt.setString(2, pkgNameField.getText());
						
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
						if (conn != null)
							conn.close();
						if (stmt != null) 
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
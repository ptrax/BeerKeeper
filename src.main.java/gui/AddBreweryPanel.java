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

/**
 * The Add Brewery panel has the necessary UI and SQL to add Brewery information to
 * the database. 
 * 
 * @author ptraxler
 *
 */
public class AddBreweryPanel extends JPanel {
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	PreparedStatement pStmt = null;
	
	JPanel thisPanel = this;
	JLabel breweryName = new JLabel("Name:", SwingConstants.CENTER);
	JLabel breweryLocation = new JLabel("Location:", SwingConstants.CENTER);
	JLabel breweryPhone = new JLabel("Phone:", SwingConstants.CENTER);
	JLabel breweryEmail = new JLabel("Email:", SwingConstants.CENTER);
	
	JTextField nameField = new JTextField();
	JTextField locationField = new JTextField();
	JTextField phoneField = new JTextField();
	JTextField emailField = new JTextField();

	
	JButton addButton = new JButton("Add");
	JButton cancelButton = new JButton("Cancel");
	
	public AddBreweryPanel(Connection conn) {
		this.conn = conn;
		this.setLayout(new GridLayout(5,2));
		
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
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT BrewerID, Name FROM BREWERY ORDER BY BrewerID;");
				int brewerID = 0;
				boolean nameExists = false;
				
				while (rs.next()) {
					
					String nameString = rs.getString(2).toLowerCase();
					if(nameField.getText().toLowerCase().equals(nameString)) {
						nameExists = true;
						break;
					}
					
					if (rs.isLast()) {
						brewerID = rs.getInt(1) + 1;
					}
				}
				
				if(!nameExists) {
					pStmt = conn.prepareStatement("INSERT INTO BREWERY VALUES (?,?,?,?,?)");
					pStmt.setInt(1, brewerID);
					pStmt.setString(2, nameField.getText());
					pStmt.setString(3, locationField.getText());
					pStmt.setString(4, phoneField.getText());
					pStmt.setString(5, emailField.getText());
					pStmt.executeUpdate();
					conn.commit();
					
					thisPanel.firePropertyChange("add", false, true);
				} else {
					cancelButton.doClick();
				}
				} catch (SQLException f) {
					System.out.println(f.getMessage());
				}
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thisPanel.firePropertyChange("add", true, false);
			}
		});
	}

}
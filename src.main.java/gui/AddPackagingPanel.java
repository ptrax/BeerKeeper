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
 * The Add Packaging panel has the necessary UI and SQL to add Packaging information to
 * the database. 
 * 
 * @author ptraxler
 *
 */
public class AddPackagingPanel extends JPanel {
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	PreparedStatement pStmt = null;
	
	JPanel thisPanel = this;
	JLabel pkgName = new JLabel("Packaging Name:", SwingConstants.CENTER);
	JTextField pkgNameField = new JTextField();
	
	JButton addButton = new JButton("Add");
	JButton cancelButton = new JButton("Cancel");
	
	public AddPackagingPanel(Connection conn) {
		this.conn = conn;
		this.setLayout(new GridLayout(2,2));
		
		this.add(pkgName);
		this.add(pkgNameField);
		this.add(cancelButton);
		this.add(addButton);
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT * FROM PACKAGING ORDER BY PkgID;");
				int typeID = 0;
				boolean nameExists = false;
				
				while (rs.next()) {
					
					String nameString = rs.getString(2).toLowerCase();
					if(pkgNameField.getText().toLowerCase().equals(nameString)) {
						nameExists = true;
						break;
					}
					
					if (rs.isLast()) {
						typeID = rs.getInt(1) + 1;
					}
				}
				
				if(!nameExists) {
					pStmt = conn.prepareStatement("INSERT INTO PACKAGING VALUES (?,?)");
					pStmt.setInt(1, typeID);
					pStmt.setString(2, pkgNameField.getText());
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
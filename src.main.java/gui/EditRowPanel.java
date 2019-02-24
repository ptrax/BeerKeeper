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

/**
 * The Edit Row panel has the necessary UI and SQL to edit stock information to
 * the database. 
 * 
 * @author ptraxler
 *
 */
public class EditRowPanel extends JPanel {
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	PreparedStatement pStmt = null;
	
	JPanel thisPanel = this;
	
	JLabel weeksServed = new JLabel("Weeks Served:", SwingConstants.CENTER);
	JLabel currentUnits = new JLabel("Current Units: ", SwingConstants.CENTER);
	JLabel desUnits = new JLabel("Desired Units: ", SwingConstants.CENTER);
	JLabel soldOverall = new JLabel("Sold Overall: ", SwingConstants.CENTER);
	JLabel price = new JLabel("Price: ", SwingConstants.CENTER);
	
	JTextField weeksField = new JTextField();
	JTextField curUnitsField = new JTextField();
	JTextField desUnitsField = new JTextField();
	JTextField soldOverallField = new JTextField();
	JTextField priceField = new JTextField();

	JButton addButton = new JButton("Update");
	JButton cancelButton = new JButton("Cancel");
	
	int beerID = 0;
	int pkgID = 0;
	
	public EditRowPanel(Connection conn, String beerName, String pkgName) {
		this.conn = conn;
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
			pStmt = conn.prepareStatement("SELECT BeerID FROM BEER WHERE Name = ?");
			pStmt.setString(1, beerName);
			rs = pStmt.executeQuery();
			
			if(rs.next()) {
				beerID = rs.getInt(1);
			}
			
			pStmt = conn.prepareStatement("SELECT PkgID FROM PACKAGING WHERE PkgName = ?");
			pStmt.setString(1, pkgName);
			rs = pStmt.executeQuery();
			
			if(rs.next()) {
				pkgID = rs.getInt(1);
			}
			
			pStmt = conn.prepareStatement("SELECT * FROM STOCK WHERE BeerID = ? AND PkgID = ?");
			pStmt.setInt(1, beerID);
			pStmt.setInt(2, pkgID);
			rs = pStmt.executeQuery();
			
			if(rs.next()) {
				weeksField.setText(rs.getString(3));
				curUnitsField.setText(rs.getString(4));
				desUnitsField.setText(rs.getString(5));
				soldOverallField.setText(rs.getString(6));
				priceField.setText(rs.getString(7));
			}
			
		} catch (SQLException e) {
			System.out.println(e);
		}
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					pStmt = conn.prepareStatement("UPDATE STOCK SET WeeksServed = ?, CurrentUnits = ?, DesiredUnits = ?, SoldOverall = ?, Price = ? WHERE BeerID = ? and PkgID = ?;");
					pStmt.setString(1, weeksField.getText());
					pStmt.setString(2, curUnitsField.getText());
					pStmt.setString(3, desUnitsField.getText());
					pStmt.setString(4, soldOverallField.getText());
					pStmt.setString(5, priceField.getText());
					pStmt.setInt(6, beerID);
					pStmt.setInt(7, pkgID);
					
					pStmt.executeUpdate();
					conn.commit();
				} catch (SQLException f) {
					System.out.println(f.getMessage());
				} finally {
					thisPanel.firePropertyChange("update", true, false);
				}
				
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				thisPanel.firePropertyChange("update", true, false);
			}
		});
	}

}
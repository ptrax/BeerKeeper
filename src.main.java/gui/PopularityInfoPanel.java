package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import main.Controller;

/**
 * This panel is shown in the data area when the Popularity menu button is pressed
 * @author ptraxler
 *
 */
public class PopularityInfoPanel extends JPanel{
	// Set up componenets
	JLabel header = new JLabel("Beer Popularity", SwingConstants.CENTER);
	
	JLabel beer = new JLabel("Beer:", SwingConstants.CENTER);
	JLabel minWeeks = new JLabel("Min. Weeks Sold:", SwingConstants.CENTER);
	JLabel packaging = new JLabel("Packaging:", SwingConstants.CENTER);
	
	JComboBox<String> beerPicker = new JComboBox<String>();
	JComboBox<String> packagePicker = new JComboBox<String>();
	
	JTextField weeksSold = new JTextField();
	
	JButton execute = new JButton("Execute Query");
	
	JScrollPane scroll = new JScrollPane();
	JTable table = new JTable();
	
	// Add some connection/query stuff
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pStmt = null;
	Statement stmt = null;
	
	/**
	 * Constructor for Popularity panel. 
	 */
	public PopularityInfoPanel() {
		// Makes the background transparent
		this.setBackground(new Color(255,255,255,0));
		
		// Gridbag layout
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridbag);
		
		// Set up the header
		header.setFont(new Font("Calibri", Font.PLAIN, 24));
		c.insets = new Insets(25, 0, 25, 0);
		c.anchor = GridBagConstraints.NORTH;
		c.weightx = 0;
		c.weighty = 1;
		c.gridy = 0;
		c.gridwidth =  GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(header, c);
		 
		// Set up "Select Beer" label
		c.insets = new Insets(0, 0, 20, 0);
		c.gridy = 1;
		c.gridwidth =  GridBagConstraints.BOTH;
		this.add(beer, c);
		
		
		// Set up the Beer picker
		c.insets = new Insets(0, 0, 0, 0);
		c.gridx = 1;
		this.add(beerPicker, c);
		
		// Set up the "Select Packaging" label
		c.gridx = 2;
		this.add(packaging, c);
		
		// Set up the packaging picker
		c.gridx = 4;
		this.add(packagePicker, c);
		
		// Set up the "Minimum weeks sold" label
		c.gridx = 0;
		c.gridy = 2;
		this.add(minWeeks, c);
		
		// Set up the Weeks sold textbox
		c.gridx = 1;
		this.add(weeksSold, c);
		
		// Set up the execute button
		c.insets = new Insets(0, 0, 20, 0);
		c.gridx = 2;
		c.gridwidth =  GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(execute, c);
		
		// Set up the table and place it in the scroll pane
		DefaultTableModel model = new DefaultTableModel(0,1);
		model.setColumnIdentifiers(new Object[] {"Execute a Query"});
		table.setModel(model);
		scroll.setViewportView(table);
		scroll.setPreferredSize(new Dimension(100,100));

		// Set up the scrollpane in the gridbag 
		c.gridwidth = 1;
		c.weightx = 1;
		c.weighty = 5;
		c.gridy = 4;
		c.gridx = 0;
		c.gridwidth =  GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = (int)scroll.getPreferredSize().getHeight();
		this.add(scroll,c);
		
		/** 
		 * Action listener for the button click. When a click is detected the query is executed. 
		 */
		execute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Run the prepared statement to get our beer info. The values from the comboboxes and textfield
				// are used here
				// For the beer name, packaging, and weeks served from the different fields, an
				// AND(condition OR condition) is used so all records are returned when Any is 
				// selected
				try {
					pStmt = conn.prepareStatement(
							"SELECT b.Name,s.WeeksServed,pk.PkgName,p.TotalPplr,p.TimePplr,s.CurrentUnits \n" + 
							"FROM BEER b, STOCK s, POPULARITY p, PACKAGING pk\n" + 
							"WHERE b.BeerID = s.BeerID AND s.StockID = p.StockID AND pk.PkgID = s.PkgID AND " +
							"(b.Name = ? OR b.Name LIKE ?) AND (pk.pkgName = ? OR pk.pkgName LIKE ?)" +
							"AND (s.WeeksServed >= ? OR s.WeeksServed LIKE ?);"
							);
					
					// Set the first strings for the beer name
					if(beerPicker.getSelectedItem().equals("Any")) {
						pStmt.setString(1, "%");
						pStmt.setString(2, "%");

					} else {
						pStmt.setString(1, (String)beerPicker.getSelectedItem());
						pStmt.setString(2, (String)beerPicker.getSelectedItem());

					}
					
					// Set the second strings for the packaging
					if(packagePicker.getSelectedItem().equals("Any")) {
						pStmt.setString(3, "%");
						pStmt.setString(4, "%");

					} else {
						pStmt.setString(3, (String)packagePicker.getSelectedItem());
						pStmt.setString(4, (String)packagePicker.getSelectedItem());

					}
					
					// Set the third strings for the weeks sold
					if(weeksSold.getText().equals("")) {
						pStmt.setString(5, "%");
						pStmt.setString(6, "%");
					} else {
						pStmt.setString(5, (String)weeksSold.getText());
						pStmt.setString(6, (String)weeksSold.getText());

					}
					
					// Execute the statement
					rs = pStmt.executeQuery();
					
					// Had to do this to get all the table rows to reliably remove.
					if(table.getRowCount() > 0) {
						table.removeRowSelectionInterval(0, table.getRowCount()-1);
					}
					
					// Redo the table model, adding column names
					DefaultTableModel model = new DefaultTableModel();
					model.setColumnCount(rs.getMetaData().getColumnCount());
					Object[] columnNames = new Object[model.getColumnCount()];
					for(int i = 1; i <= model.getColumnCount() ; i++) {
						columnNames[i-1] = rs.getMetaData().getColumnName(i);
					}
					model.setColumnIdentifiers(columnNames);
					
					// Iterate through result set, placing data in table rows.
					while(rs.next()) {
						System.out.println(rs);
						Object rowData[] = {rs.getString(1),rs.getInt(2),rs.getString(3),
											rs.getDouble(4),rs.getDouble(5), rs.getInt(6)};
						model.addRow(rowData);
					}
					
					// Set the model once all data is in. 
					table.setModel(model);
					
					// If i put these here, we can't run another query.. 
					//conn.close();
					//rs.close();
					//pStmt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

			}
			
		});
	}
	
	/**
	 * Sets up the combo boxes with the beer names and packaging names. This was needed so they
	 * could be updated after a connection was established
	 */
	public void setupCombos() {
		// If we don't have a connection, get one.
		if(conn == null) {
			conn = Controller.getInstance().getConnection();
		}
		
		// Query for the beer names
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT Name FROM BEER");
			beerPicker.addItem("Any");
			while(rs.next()) {
				beerPicker.addItem(rs.getString(1));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		// Query for the package names
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT PkgName FROM PACKAGING");
			packagePicker.addItem("Any");
			while(rs.next()) {
				packagePicker.addItem(rs.getString(1));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Overrides the paintComponenet method for asthetics. Otherwise it won't display right. 
	 */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Color is RBGA, where A is alpha (transparency)
        Color color = new Color(255,255,255,135);
        g.setColor(color);
        g.fillRoundRect(0, 0, this.getParent().getWidth(), this.getParent().getWidth(), 20, 20);

    }
}

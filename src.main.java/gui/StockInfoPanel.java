package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import main.Controller;

/**
 * This panel is shown when the Beer button is pressed in the menu.
 * @author ptraxler
 * @author gfpierce
 */
public class StockInfoPanel extends JPanel{
	JPanel thisPanel = this;
	
	// Set up components
	JLabel header = new JLabel("Stock Info Panel", SwingConstants.CENTER);
	JLabel beer = new JLabel("Beer:", SwingConstants.CENTER);
	JLabel packaging = new JLabel("Packaging:", SwingConstants.CENTER);
	
	JComboBox<String> beerPicker = new JComboBox<String>();
	JComboBox<String> packagePicker = new JComboBox<String>();
	
	JButton execute = new JButton("Execute Query");
	
	JScrollPane scroll = new JScrollPane();
	JTable table = new JTable();
	
	//Set up connection/query variables
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pStmt = null;
	Statement stmt = null;
	
	public StockInfoPanel() {
		this.setBackground(new Color(255,255,255,0));
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridbag);
		
		// Set up the header
        header.setFont(new Font("Calibri", Font.PLAIN, 24));
		c.insets = new Insets(25, 0, 25, 0);
		c.anchor = GridBagConstraints.NORTH;
        //c.ipady=0;
		
		c.weightx = 0;
		c.weighty = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(header, c);
		
		// Set up "Select Beer" label
		c.insets = new Insets(0,0,20,0);
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.BOTH;
		this.add(beer, c);
		
		// Set up Beer picker
		//c.insets = new Insets(0,0,0,0);
		c.gridx = 1;
		c.gridy = 1;
		this.add(beerPicker);
		
		// Set up "Select Packaging" label
		c.gridx = 2;
		c.gridy = 2;
		this.add(packaging, c);
		
		// Set up the package picker
		c.gridx = 4;
		c.gridy = 2;
		this.add(packagePicker, c);
		
		// Set up the execute button
		c.insets = new Insets(0,0,20,0);
		c.gridx = 2;
		c.gridy = 3;
		c.gridwidth = GridBagConstraints.REMAINDER;
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
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = (int)scroll.getPreferredSize().getHeight();
		this.add(scroll, c);
		
		// Just gonna copy Paul's cleanup algorithm here for now, maybe forever
		Component comp[] = this.getComponents();
		for (Component cmp : comp) {
			String s = cmp.getClass().getSimpleName();
			
			if (s.equals(("JComboBox"))) {
				JComboBox<String> jcb = (JComboBox<String>) cmp;
				jcb.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						thisPanel.revalidate();
					}
				});
			}
			
		}
		
		// Change Listener for the button so it doesn't wreck the background (it's important)
		execute.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				thisPanel.revalidate();
			}
		});
		
		// Action listener for query button click. Upon click, query is executed
		execute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Run the prepared statement to get stock info
				try {
					// Need some allowance for the packaging in the query here
					pStmt = conn.prepareStatement("SELECT Name,WeeksServed,CurrentUnits,Price,PkgID \n" + 
							"FROM STOCK JOIN BEER ON STOCK.BeerID = BEER.BeerID AND (BEER.Name = ?)");
					
					// Set the first string for the beer name
					if (beerPicker.getSelectedItem().equals("Any")) {
						pStmt.setString(1,  "%");
					} else {
						pStmt.setString(1, (String)beerPicker.getSelectedItem());
					}
					
					// Un-comment this to set the string in the query for the packaging
					// Set the second string for the packaging
					/*if (packagePicker.getSelectedItem().equals("Any")) {
						pStmt.setString(2, "%");
					} else {
						pStmt.setString(2, (String)packagePicker.getSelectedItem());
					}*/
					
					rs = pStmt.executeQuery();
					
					// Trying Paul's table row removal
					if (table.getRowCount() > 0) {
						table.removeRowSelectionInterval(0, table.getRowCount()-1);
					}
					
					// Redo the table model, adding column names
					DefaultTableModel model = new DefaultTableModel();
					model.setColumnCount(rs.getMetaData().getColumnCount());
					Object[] columnNames = new Object[model.getColumnCount()];
					for (int i = 1; i <= model.getColumnCount(); i++) {
						columnNames[i-1] = rs.getMetaData().getColumnName(i);
					}
					
					model.setColumnIdentifiers(columnNames);
					
					// Iterate through result set, placing data in table rows
					while (rs.next()) {
						System.out.println(rs);
						System.out.println(rs.getString(1));
						Object rowData[] = {rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getInt(5)};
						model.addRow(rowData);
					}
					
					table.setModel(model);
					
					// Should have these but it takes away the ability to run another query because the world is a tough place
					//conn.close();
					//rs.close();
					//pStmt.close();
				} catch(SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	public void setupCombos() {
		// If we don't have a connection, get one
		if (conn == null) {
			conn = Controller.getInstance().getConnection();
		}
		
		// Query for the beer names
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT Name FROM BEER");
			beerPicker.removeAllItems();
			beerPicker.addItem("Any");
			while (rs.next()) {
				beerPicker.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// Query for the package names
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT PkgName FROM PACKAGING");
			packagePicker.removeAllItems();
			packagePicker.addItem("Any");
			while (rs.next()) {
				packagePicker.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Color is RGBA, where A is alpha (transparency)
		Color color = new Color(255,255,255,135);
		g.setColor(color);
		g.fillRoundRect(0, 0, this.getParent().getWidth(),  this.getParent().getWidth(), 20, 20);
	}
}

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
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
	JLabel header = new JLabel("Stock Info", SwingConstants.CENTER);
	JLabel beer = new JLabel("Beer:", SwingConstants.CENTER);
	JLabel packaging = new JLabel("Packaging:", SwingConstants.CENTER);
	
	JComboBox<String> beerPicker = new JComboBox<String>();
	JComboBox<String> packagePicker = new JComboBox<String>();
	
	JButton execute = new JButton("Execute Query");
	JButton addRow = new JButton("Add Row");
	JButton deleteRow = new JButton("Delete Row");
	JButton editRow = new JButton("Edit Row");
	
	JScrollPane scroll = new JScrollPane();
	JTable table = new JTable();
	
	//Set up connection/query variables
	Connection conn = null;
	ResultSet rs = null;
	PreparedStatement pStmt = null;
	Statement stmt = null;
	
	GridBagLayout gridbag;
	GridBagConstraints c;
	public StockInfoPanel() {
		this.setBackground(new Color(255,255,255,0));
		
		gridbag = new GridBagLayout();
		c = new GridBagConstraints();
		this.setLayout(gridbag);
		
		// Set up the header
        header.setFont(new Font("Calibri", Font.PLAIN, 24));
		c.insets = new Insets(25, 0, 25, 0);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.weightx = 1.25;
		c.weighty = 1;
		c.gridy = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(header, c);
		
		// Set up "Select Beer" label
		c.insets = new Insets(0, 0, 10, 0);
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = GridBagConstraints.BOTH;
		this.add(beer, c);
		
		// Set up Beer picker
		c.insets = new Insets(0,0,0,0);
		c.gridx = 1;
		this.add(beerPicker);
		
		// Set up "Select Packaging" label
		c.gridx = 2;
		this.add(packaging, c);
		
		// Set up the package picker
		c.gridx = 4;
		c.gridwidth = GridBagConstraints.REMAINDER;
		this.add(packagePicker, c);
		
		// Set up the execute button
		c.insets = new Insets(0,0,10,0);
		c.gridx = 4;
		c.gridy = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
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
		
		// Set up the scrollpane in the gridbag
		c.insets = new Insets(0,0,0,0);
		c.gridwidth = 1;
		c.ipady = 0;
		c.weightx = 1;
		c.weighty = 1;
		c.gridy = 5;
		c.gridx = 1;
		c.fill = GridBagConstraints.BOTH;
		this.add(addRow, c);
		
		// Set up edit button
		c.gridx = 2;
		this.add(editRow,c);
		
		// Set up delete button
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridx = 3;
		this.add(deleteRow,c);	
		
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
					pStmt = conn.prepareStatement("SELECT Name,WeeksServed,CurrentUnits,Price,PACKAGING.pkgName \n" + 
							"FROM STOCK JOIN BEER ON STOCK.BeerID = BEER.BeerID AND (BEER.Name = ? OR BEER.Name LIKE ?)\n" +
							"JOIN PACKAGING ON STOCK.pkgID = PACKAGING.pkgID WHERE (PACKAGING.pkgName = ? OR PACKAGING.pkgName LIKE ?);");
					
					// Set the first string for the beer name
					if (beerPicker.getSelectedItem().equals("Any")) {
						pStmt.setString(1,  "%");
						pStmt.setString(2,  "%");
					} else {
						pStmt.setString(1, (String)beerPicker.getSelectedItem());
						pStmt.setString(2, (String)beerPicker.getSelectedItem());

					}
					
					if (packagePicker.getSelectedItem().equals("Any")) {
						pStmt.setString(3, "%");
						pStmt.setString(4, "%");
					} else {
						pStmt.setString(3, (String)packagePicker.getSelectedItem());
						pStmt.setString(4, (String)packagePicker.getSelectedItem());
					}
					
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
						Object rowData[] = {rs.getString(1), rs.getInt(2), rs.getInt(3), rs.getDouble(4), rs.getString(5)};
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
		
		/** 
		 * Action listener for the Delete Row button. Upon clicking the button, a dialog is shown
		 * that asks the user what they actually want to delete
		 */
		deleteRow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Figure out what is selected
				int row = table.getSelectedRow();
				String beerName = (String)table.getModel().getValueAt(row, 0);
				String packName = (String)table.getModel().getValueAt(row,4);
				
				// Set up the buttons with the information from the row
				JButton deleteAll = new JButton("Delete all " + beerName);
				JButton deletePack = new JButton("Delete only " + beerName + " in " + packName + "s");
				
				// Set up the panel for the dialog 
				JPanel deletePanel = new JPanel(new BorderLayout());
				deletePanel.add(deleteAll, BorderLayout.NORTH);
				deletePanel.add(deletePack, BorderLayout.SOUTH);
				
				// Set up the dialog itself
				JDialog deletePrompt = new JDialog();
				deletePrompt.add(deletePanel);
				
				//(gfpierce) The below line didn't render very well for me, so I adjusted the height dimension
				deletePrompt.setSize(new Dimension((int)deletePack.getPreferredSize().getWidth(),150));
				//deletePrompt.setSize(400,300);
				deletePrompt.setLocationRelativeTo(thisPanel);
				deletePrompt.setVisible(true);
				
				/**
				 * Action listener for the "Delete all" button within the delete dialog.
				 * This will delete the beer with the given name from the beer table 
				 */
				deleteAll.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							// Craft the statement to delete what we want
							pStmt = conn.prepareStatement("DELETE FROM BEER WHERE BEER.Name = ?");
							pStmt.setString(1, beerName);
							
							// Execute the update and commit the changes
							pStmt.executeUpdate();
							conn.commit();

							// Populate the table again
							execute.doClick();
						} catch (SQLException f) {
							System.out.println(f.getMessage());
						} finally {
							// Get rid of the screen when a button is clicked
							deletePrompt.dispose();
						}
					}
				});
				
				/**
 				 * Action listener for the "Delete all of this beer with this packaging" button within the delete dialog.
				 * This will delete the stock listing with the given beer name and given packaging from the Stock table
				 */
				deletePack.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							// First we make a statement to get the beer ID and package ID from the names in the row
							stmt = conn.createStatement();
							pStmt = conn.prepareStatement("SELECT pkgID, beerID FROM PACKAGING p, BEER b WHERE p.pkgName = ? AND b.Name = ?");
							pStmt.setString(1, packName);
							pStmt.setString(2, beerName);
							rs = pStmt.executeQuery();
							
							// Now we craft another statement with the IDs we got in the last query, deleting what we want. 
							rs.next();
							String sql = "DELETE FROM STOCK WHERE pkgID = " + rs.getInt(1) + " AND BeerID = " + rs.getInt(2);
							stmt.executeUpdate(sql);
							conn.commit();
							
							// Populate the table again
							execute.doClick();
						} catch (SQLException f) {
							System.out.println(f.getMessage());
						} finally {
							// Get rid of the dialog box
							deletePrompt.dispose();
						}
					}
					
				});
			}
			
		});
		
		addRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// Set up text fields
				JTextField brewerNameField = new JTextField("Brewer Name");
				JTextField styleNameField = new JTextField("Style Name");
				JTextField typeNameField = new JTextField("Type Name");
				JTextField pkgNameField = new JTextField("Package Name");
				JTextField beerNameField = new JTextField("Beer Name");
				JTextField contentField = new JTextField("Alcohol Content");
				JTextField descriptionField = new JTextField("Description");
				
				// Set up execute button
				
				JButton executeAdd = new JButton("Add to beers and stock");
				
				// Set up panel for the dialog
				JPanel addPanel = new JPanel(new GridLayout(7,1));
				addPanel.add(brewerNameField);
				addPanel.add(styleNameField);
				addPanel.add(typeNameField);
				addPanel.add(pkgNameField);
				addPanel.add(beerNameField);
				addPanel.add(contentField);
				addPanel.add(descriptionField);
				addPanel.add(executeAdd);
				
				// Set up the dialog box
				JDialog addPrompt = new JDialog();
				addPrompt.add(addPanel);
				addPrompt.setSize(600, 450);
				addPrompt.setLocationRelativeTo(thisPanel);
				addPrompt.setVisible(true);
				
				executeAdd.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							//Find last beerId and add 1 to it
							rs = stmt.executeQuery("SELECT BeerId FROM BEER ORDER BY BeerID;");
							int beerId = 0;
							while (rs.next()) {
								if (rs.isLast()) {
									beerId = rs.getInt(1) + 1;
								}
							}
							String beerIdString = Integer.toString(beerId);
							
							// Set up statement and variables to find brewerId
							String brewerName = brewerNameField.getText();
							pStmt = conn.prepareStatement("SELECT BrewerID FROM BREWERY WHERE Name = ?;");
							pStmt.setString(1, brewerName);
							rs = pStmt.executeQuery();
							int brewerId = 0;
							if (rs.next() == false) {
								rs = stmt.executeQuery("SELECT BrewerID FROM BREWERY ORDER BY BrewerID");
								while (rs.next()) {
									if (rs.isLast()) {
										brewerId = rs.getInt(1) + 1;
										//Values: BrewerID | Name | Location | Phone | Email
										pStmt = conn.prepareStatement("INSERT INTO BREWERY VALUES (?,?,?,?,?)");
										pStmt.setString(1, Integer.toString(brewerId));
										pStmt.setString(2, brewerName);
										
										
										//Probably need to update this
										pStmt.setString(3, "USA");
										pStmt.setString(4, "123-456-7890");
										pStmt.setString(5, "brewery@newbrewer.com");
										pStmt.executeUpdate();
										conn.commit();
									}
								}
							} else {
								brewerId = rs.getInt(1);
							}
							
							String brewerIdString = Integer.toString(brewerId);
							
							// Get styleId
							String styleName = styleNameField.getText();
							pStmt = conn.prepareStatement("SELECT StyleID FROM STYLE WHERE StyleName = ?;");
							pStmt.setString(1, styleName);
							rs = pStmt.executeQuery();
							int styleId = 0;
							if (rs.next()) {
								styleId = rs.getInt(1);
							}
							String styleIdString = Integer.toString(styleId);
							
							// Get typeId
							String typeName = typeNameField.getText();
							pStmt = conn.prepareStatement("SELECT TypeID FROM TYPE WHERE TypeName = ?;");
							pStmt.setString(1, typeName);
							rs = pStmt.executeQuery();
							int typeId = 0;
							if (rs.next()) {
								typeId = rs.getInt(1);
							}
							String typeIdString = Integer.toString(typeId);
							
							// Add beer to table
							String beerName = beerNameField.getText();
							String beerContent = contentField.getText();
							String description = descriptionField.getText();
							
							//Values: BeerId | Name | BrewerID | AlcContent | Description | StyleID | TypeID
							pStmt = conn.prepareStatement("INSERT INTO BEER VALUES (?,?,?,?,?,?,?);");
							pStmt.setString(1, beerIdString);
							pStmt.setString(2, beerName);
							pStmt.setString(3, brewerIdString);
							pStmt.setString(4, beerContent);
							pStmt.setString(5, description);
							pStmt.setString(6, styleIdString);
							pStmt.setString(7, typeIdString);
							pStmt.executeUpdate();
							conn.commit();
							
							// Get pkgID
							String pkgName = pkgNameField.getText();
							pStmt = conn.prepareStatement("SELECT PkgID FROM PACKAGING WHERE PkgName = ?;");
							pStmt.setString(1, pkgName);
							rs = pStmt.executeQuery();
							int pkgId = 0;
							if (rs.next()) {
								pkgId = rs.getInt(1);
							}
							
							String pkgIdString = Integer.toString(pkgId);
							
							// Get stockID
							rs = stmt.executeQuery("SELECT StockID FROM STOCK;");
							int stockId = 0;
							while (rs.next()) {
								if (rs.isLast()) {
									stockId = rs.getInt(1) + 1;
								}
							}
							String stockIdString = Integer.toString(stockId);
							
							// Insert into stock
							//Values: 1: StockID | 2: BeerID | 3: WeeksServed | 4: CurrentUnits | 5: DesiredUnits | 6: SoldOverall | 7: Price | 8: PkgID
							pStmt = conn.prepareStatement("INSERT INTO STOCK VALUES (?,?,?,?,?,?,?,?);");
							pStmt.setString(1, stockIdString);
							pStmt.setString(2, beerIdString);
							pStmt.setString(3, "0");
							pStmt.setString(4, "0");
							pStmt.setString(5, "0");
							pStmt.setString(6, "0");
							pStmt.setString(7, "0");
							pStmt.setString(8, pkgIdString);
							
							pStmt.executeUpdate();
							conn.commit();
							execute.doClick();
						} catch (SQLException exc) {
							exc.printStackTrace();
						} finally {
							addPrompt.dispose();
						}
					}
				});
				
			}
		});
	}
	
	/**
	 * Set up the combo boxes with beer names and package names. 
	 */
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

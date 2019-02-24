package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import main.Controller;

/**
 * The Add Stock panel has the necessary UI and SQL to add Stock information to
 * the database. 
 * 
 * @author gfpierce
 * @author ptraxler
 *
 */
public class AddStockPanel extends JPanel{
	JPanel thisPanel = this;
	
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	PreparedStatement pStmt = null;
			
	JComboBox<String> breweryName = new JComboBox<String>();
	JComboBox<String> styleName = new JComboBox<String>();
	JComboBox<String> typeName = new JComboBox<String>();
	JComboBox<String> pkgName = new JComboBox<String>();
	
	// Set up text fields
	JTextField beerNameField = new JTextField("Beer Name");
	JTextField contentField = new JTextField("Alcohol Content");
	JTextField descriptionField = new JTextField("Description");
	
	public AddStockPanel(Connection conn) {
		
		// Query for the beer names
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT Name FROM BREWERY");
			breweryName.removeAllItems();
			breweryName.addItem("Select Brewery...");
			while (rs.next()) {
				breweryName.addItem(rs.getString(1));
			}
			breweryName.addItem("Add...");
		} catch (SQLException f) {
			f.printStackTrace();
		}
		
		// Query for the style names
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT StyleName FROM STYLE");
			styleName.removeAllItems();
			styleName.addItem("Select Style...");
			while (rs.next()) {
				styleName.addItem(rs.getString(1));
			}
			styleName.addItem("Add...");
		} catch (SQLException f) {
			f.printStackTrace();
		}
		
		// Query for the type names
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT TypeName FROM TYPE");
			typeName.removeAllItems();
			typeName.addItem("Select Type...");
			while (rs.next()) {
				typeName.addItem(rs.getString(1));
			}
			typeName.addItem("Add...");
		} catch (SQLException f) {
			f.printStackTrace();
		}
		
		// Query for the package names
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT PkgName FROM PACKAGING");
			pkgName.removeAllItems();
			pkgName.addItem("Select Packaging...");
			while (rs.next()) {
				pkgName.addItem(rs.getString(1));
			}
			pkgName.addItem("Add...");
		} catch (SQLException f) {
			f.printStackTrace();
		}
		
		breweryName.addItemListener(new ItemListener() {
	
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(breweryName.getSelectedIndex() >= 0) {
					if(breweryName.getSelectedItem().equals("Add...") && e.getStateChange()== 1) {
						JPanel addBreweryPanel = new AddBreweryPanel(conn);
						JDialog breweryDialog = new JDialog();
						
						addBreweryPanel.addPropertyChangeListener("add", new PropertyChangeListener() {
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								System.out.println((boolean)evt.getNewValue());
								if((boolean)evt.getNewValue() == true) {
									// Query for the style names
									try {
										stmt = conn.createStatement();
										rs = stmt.executeQuery("SELECT Name FROM BREWERY");
										breweryName.removeAllItems();
										breweryName.addItem("Select Brewerye...");
										while (rs.next()) {
											breweryName.addItem(rs.getString(1));
										}
										breweryName.addItem("Add...");
									} catch (SQLException f) {
										f.printStackTrace();
									}
									breweryName.setSelectedIndex(breweryName.getItemCount()-2);
								} else {
									breweryName.setSelectedIndex(0);
								}
								
								breweryDialog.dispose();
							}
						});
						
						breweryDialog.add(addBreweryPanel);
						breweryDialog.setLocationRelativeTo(thisPanel.getParent());
						breweryDialog.setSize(new Dimension(250,160));
						breweryDialog.setResizable(false);
						breweryDialog.setVisible(true);
					}
				}
			} 
		});
		
		styleName.addItemListener(new ItemListener() {
	
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(styleName.getSelectedIndex() >= 0) {
					if(styleName.getSelectedItem().equals("Add...") && e.getStateChange()== 1) {
						JPanel addStylePanel = new AddStylePanel(conn);
						JDialog styleDialog = new JDialog();
						
						addStylePanel.addPropertyChangeListener("add", new PropertyChangeListener() {
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								System.out.println((boolean)evt.getNewValue());
								if((boolean)evt.getNewValue() == true) {
									// Query for the style names
									try {
										stmt = conn.createStatement();
										rs = stmt.executeQuery("SELECT StyleName FROM STYLE");
										styleName.removeAllItems();
										styleName.addItem("Select Style...");
										while (rs.next()) {
											styleName.addItem(rs.getString(1));
										}
										styleName.addItem("Add...");
									} catch (SQLException f) {
										f.printStackTrace();
									}
									styleName.setSelectedIndex(styleName.getItemCount()-2);
								} else {
									styleName.setSelectedIndex(0);
								}
								
								styleDialog.dispose();
							}
						});
						
						styleDialog.add(addStylePanel);
						styleDialog.setLocationRelativeTo(thisPanel.getParent());
						styleDialog.setSize(new Dimension(250,70));
						styleDialog.setResizable(false);
						styleDialog.setVisible(true);
					}
				}
			} 
		
		});
		
		typeName.addItemListener(new ItemListener() {
	
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(typeName.getSelectedIndex() >= 0) {
					if(typeName.getSelectedItem().equals("Add...") && e.getStateChange() == 1) {
						JPanel addTypePanel = new AddTypePanel(conn);
						JDialog typeDialog = new JDialog();
						
						addTypePanel.addPropertyChangeListener("add", new PropertyChangeListener() {
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								System.out.println((boolean)evt.getNewValue());
								if((boolean)evt.getNewValue() == true) {
									// Query for the type names
									try {
										stmt = conn.createStatement();
										rs = stmt.executeQuery("SELECT TypeName FROM TYPE");
										typeName.removeAllItems();
										typeName.addItem("Select Type...");
										while (rs.next()) {
											typeName.addItem(rs.getString(1));
										}
										typeName.addItem("Add...");
									} catch (SQLException f) {
										f.printStackTrace();
									}
									typeName.setSelectedIndex(typeName.getItemCount()-2);
								} else {
									typeName.setSelectedIndex(0);
								}
								
								typeDialog.dispose();
							}
						});
						
						typeDialog.add(addTypePanel);
						typeDialog.setLocationRelativeTo(thisPanel.getParent());
						typeDialog.setSize(new Dimension(250,70));
						typeDialog.setResizable(false);
						typeDialog.setVisible(true);
					}
				} 
			}
		});
		
		pkgName.addItemListener(new ItemListener() {
	
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(pkgName.getSelectedIndex() >= 0) {
					if(pkgName.getSelectedItem().equals("Add...") && e.getStateChange()== 1) {
						JPanel addPackagingPanel = new AddPackagingPanel(conn);
						JDialog pkgDialog = new JDialog();
						
						addPackagingPanel.addPropertyChangeListener("add", new PropertyChangeListener() {
							@Override
							public void propertyChange(PropertyChangeEvent evt) {
								System.out.println((boolean)evt.getNewValue());
								if((boolean)evt.getNewValue() == true) {
									// Query for the style names
									try {
										stmt = conn.createStatement();
										rs = stmt.executeQuery("SELECT PkgName FROM PACKAGING");
										pkgName.removeAllItems();
										pkgName.addItem("Select Packaging...");
										while (rs.next()) {
											pkgName.addItem(rs.getString(1));
										}
										pkgName.addItem("Add...");
									} catch (SQLException f) {
										f.printStackTrace();
									}
									pkgName.setSelectedIndex(pkgName.getItemCount()-2);
								} else {
									pkgName.setSelectedIndex(0);
								}
								
								pkgDialog.dispose();
							}
						});
						
						pkgDialog.add(addPackagingPanel);
						pkgDialog.setLocationRelativeTo(thisPanel.getParent());
						pkgDialog.setSize(new Dimension(250,70));
						pkgDialog.setResizable(false);
						pkgDialog.setVisible(true);
					}
				} 
			}
		});
		
		// Set up execute button
		
		JButton executeAdd = new JButton("Add to beers and stock");
		
		// Set up panel for the dialog
		this.setLayout(new GridLayout(8,2));
		this.add(new JLabel("Brewery: ", SwingConstants.CENTER));
		this.add(breweryName);
		this.add(new JLabel("Style: ", SwingConstants.CENTER));
		this.add(styleName);
		this.add(new JLabel("Type: ", SwingConstants.CENTER));
		this.add(typeName);
		this.add(new JLabel("Packaging: ", SwingConstants.CENTER));
		this.add(pkgName);
		this.add(new JLabel("Beer: ", SwingConstants.CENTER));
		this.add(beerNameField);
		this.add(new JLabel("Alcohol Content: ", SwingConstants.CENTER));
		this.add(contentField);
		this.add(new JLabel("Description: ", SwingConstants.CENTER));
		this.add(descriptionField);
		this.add(executeAdd);
		
		/**
		 * Action listener for the "Add" button
		 */
		executeAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					//Find last beerId and add 1 to it to create new beerId
					rs = stmt.executeQuery("SELECT BeerId FROM BEER ORDER BY BeerID;");
					int beerId = 0;
					while (rs.next()) {
						if (rs.isLast()) {
							beerId = rs.getInt(1) + 1;
						}
					}
					String beerIdString = Integer.toString(beerId);
					
					// Set up statement and variables to find brewerId
					String brewerName = (String)breweryName.getSelectedItem();
					pStmt = conn.prepareStatement("SELECT BrewerID FROM BREWERY WHERE Name = ?;");
					pStmt.setString(1, brewerName);
					rs = pStmt.executeQuery();
					rs.next();
					int brewerId = rs.getInt(1);
					
					String brewerIdString = Integer.toString(brewerId);
					
					// Get styleId
					String styleNameString = (String)styleName.getSelectedItem();
					pStmt = conn.prepareStatement("SELECT StyleID FROM STYLE WHERE StyleName = ?;");
					pStmt.setString(1, styleNameString);
					rs = pStmt.executeQuery();
					int styleId = 0;
					if (rs.next()) {
						styleId = rs.getInt(1);
					}
					String styleIdString = Integer.toString(styleId);
					
					// Get typeId
					String typeNameString = (String)typeName.getSelectedItem();
					pStmt = conn.prepareStatement("SELECT TypeID FROM TYPE WHERE TypeName = ?;");
					pStmt.setString(1, typeNameString);
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
					
					System.out.println(styleIdString);
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
					String pkgNameString = (String)pkgName.getSelectedItem();
					pStmt = conn.prepareStatement("SELECT PkgID FROM PACKAGING WHERE PkgName = ?;");
					pStmt.setString(1, pkgNameString);
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
					pStmt.setString(3, "1");
					pStmt.setString(4, "0");
					pStmt.setString(5, "0");
					pStmt.setString(6, "0");
					pStmt.setString(7, "0");
					pStmt.setString(8, pkgIdString);
					
					pStmt.executeUpdate();
					conn.commit();
					
				} catch (SQLException exc) {
					exc.printStackTrace();
				} finally {
					thisPanel.firePropertyChange("clicked", false, true);
				}
			}
		});	
	}
}

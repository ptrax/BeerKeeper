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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import main.Controller;

/**
 * This panel is shown when the Factory button is pressed on the menu.
 * Information here should be the brewery/distributer info.
 *
 * @author kawilki3
 *
 */
public class FactoryInfoPanel extends JPanel {

    JPanel thisPanel = this;

    // Set up components
    JLabel header = new JLabel("Breweries and Distributors", SwingConstants.CENTER);
    JLabel brewery = new JLabel("Brewery:", SwingConstants.CENTER);
    JLabel dist = new JLabel("Distributer:", SwingConstants.CENTER);

    JComboBox<String> breweryPicker = new JComboBox<String>();
    JComboBox<String> distPicker = new JComboBox<String>();
    
    JButton execute = new JButton("Execute");

    JScrollPane scroll = new JScrollPane();
    JTable table = new JTable();

    //Set up connection/query variables
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pStmt = null;
    Statement stmt = null;

    GridBagLayout gridbag;
    GridBagConstraints c;

    public FactoryInfoPanel() {
        this.setBackground(new Color(255, 255, 255, 0));

        gridbag = new GridBagLayout();
        c = new GridBagConstraints();
        this.setLayout(gridbag);

        // Set up the header
        header.setFont(new Font("Calibri", Font.PLAIN, 24));
        c.insets = new Insets(25, 0, 0, 0);
        c.ipady = 0;
        c.weightx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(header, c);

        // Set up "Brewery:" label
        c.insets = new Insets(0, 0, 10, 0);
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.BOTH;
        this.add(brewery, c);

        // Set up Beer picker
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(breweryPicker, c);

        // Set up "Distributor:" label
        c.gridwidth = GridBagConstraints.BOTH;
        c.gridy = 2;
        c.gridx = 0;
        this.add(dist, c);
        
        // Set up distributer combo box
        c.gridx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(distPicker, c);
        
        // Set up the execute button
        c.insets = new Insets(0, 0, 10, 0);
        c.gridx = 4;
        c.gridy = 3;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(execute, c);

        // Set up the table and place it in the scroll pane
        DefaultTableModel model = new DefaultTableModel(0, 1);
        model.setColumnIdentifiers(new Object[]{"Execute a Query"});
        table.setModel(model);
        scroll.setViewportView(table);
        scroll.setPreferredSize(new Dimension(100, 100));

        // Set up the scrollpane in the gridbag
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 5;
        c.gridy = 4;
        c.gridx = 0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = (int) scroll.getPreferredSize().getHeight();
        this.add(scroll, c);

        // Set up the scrollpane in the gridbag
        c.insets = new Insets(0, 0, 0, 0);
        c.gridwidth = 1;
        c.ipady = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridy = 5;

        // Paul's uber-duber cleanup algorithm
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
            	conn = Controller.getInstance().getConnection();
            	
                // Prepare alternate prepared statements
                boolean picked = true;
                String pStmt1 = "SELECT bw.Name AS 'Brewery', dist.Name AS 'Distributor'\n" + 
                				"FROM BREWERY bw JOIN DISTRIBUTES ON bw.BrewerID = DISTRIBUTES.BrewerID\n" + 
        						"JOIN DISTRIBUTOR dist ON DISTRIBUTES.DistID = dist.DistID\n"+
                				"WHERE (bw.Name = ? OR dist.Name LIKE ?) AND (dist.Name = ? OR dist.Name LIKE ?)";
               
                try {
                	pStmt = conn.prepareStatement(pStmt1);
                	
                    // Set the first string for the brewery name
                    if (breweryPicker.getSelectedItem().equals("Any")) {
                        pStmt.setString(1, "%");
                        pStmt.setString(2, "%");
                    } else {
                        pStmt.setString(1, (String) breweryPicker.getSelectedItem());
                        pStmt.setString(2, (String) breweryPicker.getSelectedItem());
                    }

                    if (distPicker.getSelectedItem().equals("Any")) {
                        pStmt.setString(3, "%");
                        pStmt.setString(4, "%");
                    } else {
                        pStmt.setString(3, (String) distPicker.getSelectedItem());
                        pStmt.setString(4, (String) distPicker.getSelectedItem());
                    }
                    

                    rs = pStmt.executeQuery();

                    // Paul's table row removal
                    if (table.getRowCount() > 0) {
                        table.removeRowSelectionInterval(0, table.getRowCount() - 1);
                    }

                    // Redo the table model, adding column names
                    DefaultTableModel model = new DefaultTableModel();
                    model.setColumnCount(rs.getMetaData().getColumnCount());
                    Object[] columnNames = new Object[model.getColumnCount()];
                    for (int i = 1; i <= model.getColumnCount(); i++) {
                        columnNames[i - 1] = rs.getMetaData().getColumnLabel(i);
                    }

                    model.setColumnIdentifiers(columnNames);

                    // Iterate through result set, placing data in table rows
                    while (rs.next()) {
                        System.out.println(rs);
                        System.out.println(rs.getString(1));
                        Object rowData[] = {rs.getString(1), rs.getString(2)};
                        model.addRow(rowData);
                    }

                    table.setModel(model);

                } catch (SQLException e1) {
                    e1.printStackTrace();
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
    }

    /**
     * Set up the combo boxes with beer names and package names.
     */
    public void setupCombos() {
        conn = Controller.getInstance().getConnection();

        try {
            // Query for the brewery names
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT Name FROM BREWERY");
            breweryPicker.removeAllItems();
            breweryPicker.addItem("Any");
            while (rs.next()) {
                breweryPicker.addItem(rs.getString(1));
            }

            // Query for distributer names
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT Name FROM DISTRIBUTOR");
            distPicker.removeAllItems();
            distPicker.addItem("Any");
            while (rs.next()) {
                distPicker.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
			try {
				// Close Resources
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Color is RGBA, where A is alpha (transparency)
        Color color = new Color(255, 255, 255, 135);
        g.setColor(color);
        g.fillRoundRect(0, 0, this.getParent().getWidth(), this.getParent().getWidth(), 20, 20);
    }
}

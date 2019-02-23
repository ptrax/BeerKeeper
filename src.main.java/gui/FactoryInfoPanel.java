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
    JLabel header = new JLabel("Brewery/Distributer info", SwingConstants.CENTER);
    JLabel brewery = new JLabel("Brewery    :", SwingConstants.CENTER);
    JLabel type = new JLabel("Type:", SwingConstants.CENTER);

    JComboBox<String> breweryPicker = new JComboBox<>();
    JComboBox<String> typePicker = new JComboBox<>();

    JButton execute = new JButton("Execute Query");

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

        // Set up "Select Beer" label
        c.insets = new Insets(0, 0, 10, 0);
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = GridBagConstraints.BOTH;
        this.add(brewery, c);

        // Set up Beer picker
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 1;
        this.add(breweryPicker);

        // Set up "Select Packaging" label
        c.gridx = 2;
        this.add(type, c);

        // Set up the package picker
        c.gridx = 4;
        c.gridwidth = GridBagConstraints.REMAINDER;
        this.add(typePicker, c);

        // Set up the execute button
        c.insets = new Insets(0, 0, 10, 0);
        c.gridx = 4;
        c.gridy = 2;
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
                // Prepare alternate prepared statements
                boolean picked = true;
                String pStmt1 = "SELECT bw.Name,bw.Location,b.Name,s.StyleName \n"
                        + "FROM BEER b, BREWERY bw, STYLE s \n"
                        + "WHERE b.BrewerID = ? AND s.StyleID = b.StyleID;";
                String pStmt2 = "SELECT bw.Name,bw.Location,b.Name,s.StyleName,t.TypeName\n"
                        + "FROM BEER b, BREWERY bw, STYLE s, TYPE t\n"
                        + "WHERE b.BrewerID = ? AND s.StyleID = b.StyleID AND t.TypeID = ?;";
                // Run the prepared statement to get stock info
                try {
                    if (typePicker.getSelectedItem().equals("Any")) {
                        pStmt = conn.prepareStatement(pStmt1);
                        picked = false;
                    } else {
                        pStmt = conn.prepareStatement(pStmt2);
                    }
                    // Set the first string for the brewery name
                    pStmt.setString(1, (String) breweryPicker.getSelectedItem());
                    if (picked) {
                        pStmt.setString(2, (String) typePicker.getSelectedItem());
                    }
                    if (breweryPicker.getSelectedItem().equals("Any")) {
                        pStmt.setString(1, "%");
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
                        columnNames[i - 1] = rs.getMetaData().getColumnName(i);
                    }

                    model.setColumnIdentifiers(columnNames);

                    // Iterate through result set, placing data in table rows
                    while (rs.next()) {
                        System.out.println(rs);
                        System.out.println(rs.getString(1));
                        if (picked) {
                            Object rowData[] = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)};
                            model.addRow(rowData);
                        } else {
                            Object rowData[] = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)};
                            model.addRow(rowData);
                        }
                    }

                    table.setModel(model);

                    // Should have these but it takes away the ability to run another query because the world is a tough place
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
     * Set up the combo boxes with beer names and package names.
     */
    public void setupCombos() {
        // If we don't have a connection, get one
        if (conn == null) {
            conn = Controller.getInstance().getConnection();
        }

        // Query for the brewery names
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT Name FROM BREWERY");
            breweryPicker.removeAllItems();
            breweryPicker.addItem("Any");
            while (rs.next()) {
                breweryPicker.addItem(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Query for the package names
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT TypeName FROM TYPE");
            typePicker.removeAllItems();
            typePicker.addItem("Any");
            while (rs.next()) {
                typePicker.addItem(rs.getString(1));
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

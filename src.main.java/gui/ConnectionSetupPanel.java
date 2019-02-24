package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.Controller;

/**
 * The Connection Setup Panel is the first to display when opening the program. Fields are provided
 * for entering database url, username, and password. Once these are entered and the user
 * clicks the "Connect" button, the information is sent to the respective class for setting up the 
 * connection. If the connection is not successful, the UI notifies the user. 
 * 
 * @author ptraxler
 * @date February 2019
 */
public class ConnectionSetupPanel extends JPanel{
	JLabel header = new JLabel("Beerkeeper Connection Setup", SwingConstants.CENTER);
	JLabel instruct = new JLabel("Please enter the url of the database, as well as " +
							     "the username and password for database access", SwingConstants.CENTER);					
	JLabel url = new JLabel("Host:Port");
	JLabel user = new JLabel("Username:");
	JLabel pass = new JLabel("Password:");
	JLabel conFail = new JLabel("Connection Failed! Try again.");
	
	JTextField urlTF = new JTextField(300);
	JTextField userTF = new JTextField(300);
	//JTextField passTF = new JTextField(300);
	JPasswordField passTF = new JPasswordField(300);
	
	JButton connect = new JButton("Connect");
	
	JPanel thisPanel = this;
	
	public ConnectionSetupPanel( ) {
		// Make it seethrough
		this.setBackground(new Color(255,255,255,0));
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridbag);

		// Set up the header
        header.setFont(new Font("Calibri", Font.PLAIN, 24));
		c.insets = new Insets(25, 0, 0, 0);
        c.ipady=100;
		c.weightx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(header, c);
		
		// Set up the URL label
		c.insets = new Insets(0, 0, 0, 0);
		c.ipady = 0;
		c.gridwidth =1;
		c.fill = 0;
		c.weightx = .1;
		c.gridy = 1;
		gridbag.setConstraints(url, c);
		this.add(url);
		
		// Set up the URL Textfield
		c.insets = new Insets(0, 0, 0, 25);
		c.weightx = .9;
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		gridbag.setConstraints(urlTF, c);
		this.add(urlTF);
		
		// Set up the Username label
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = .1;
		gridbag.setConstraints(user, c);
		this.add(user);
		
		// Set up the Username textfield
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 25);
		c.gridx = 1;
		c.gridy = 2;
		c.weightx = .9;
		gridbag.setConstraints(userTF, c);
		this.add(userTF);
		
		// Set up the Password label
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = 0;
		c.gridx = 0;
		c.gridy = 3;
		c.weightx = .1;
		gridbag.setConstraints(pass, c);
		this.add(pass);

		// Set up the Password Textfield
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 25);
		c.gridx = 1;
		c.gridy = 3;
		c.weightx = .9;
		gridbag.setConstraints(passTF, c);
		this.add(passTF);
		
		// St up Connect button
		c.gridx = 0;
		c.gridy = 4;
		c.gridheight = 100;
		c.insets = new Insets(50, 0, 0, 0);
		c.weightx = 0;
		c.gridwidth = 0;
		c.fill = GridBagConstraints.CENTER;
		this.add(connect, c);
		
		// Set up Connection Failed text
		conFail.setForeground(Color.RED);
		c.gridx = 0;
		c.gridy = 3;
		c.gridheight = 0;
		c.insets = new Insets(0, 0, 0, 0);
		c.fill = GridBagConstraints.NONE;
		this.add(conFail, c);
		conFail.setVisible(false);
		
		// Get a list of the components 
		Component comp[] = thisPanel.getComponents();
		
		// Loop through components, picking out the JTextFields and 
		// adding a listener to them so they don't screw up the background.
		for(Component cmp : comp) {
			String s = cmp.getClass().getSimpleName();

			if(s.equals("JTextField") || s.contentEquals("JPasswordField")) {
				cmp.addFocusListener(new FocusListener() {

					// Revalidate for the background and remove the connection failed text
					@Override
					public void focusGained(FocusEvent e) {
						thisPanel.revalidate();
						conFail.setVisible(false);
					}

					@Override
					public void focusLost(FocusEvent e) {
						thisPanel.revalidate();
					}

				});
			}
		}
		
		// Change Listener for the button so it doesn't wreck the background.
		connect.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				thisPanel.revalidate();
			}
		});
		
		// Action listener for handling button clicks. Here we send the connection
		// info to the Controller class. If the connection is successful, this panel
		// will go away. Otherwise we tell the user it wasn't good. 
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Controller c = Controller.getInstance();
				boolean conStatus = c.connectionSetUp(urlTF.getText(), userTF.getText(), String.copyValueOf(passTF.getPassword()));
				if(!conStatus) {
					conFail.setVisible(true);
				}
			}
		});
	}
}

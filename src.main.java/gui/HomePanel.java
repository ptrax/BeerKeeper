package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This panel comes up after the connection has been established.
 * @author ptraxler
 *
 */
public class HomePanel extends JPanel{
	JLabel header = new JLabel("Welcome to Beerkeeper", SwingConstants.CENTER);
	JLabel prompt = new JLabel("Begin by using the menu on the left.");
	public HomePanel() {
		this.setBackground(new Color(255,255,255,0));
		
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridbag);
		
		// Set up the header
        header.setFont(new Font("Calibri", Font.PLAIN, 24));
		c.insets = new Insets(25, 0, 0, 0);
        c.ipady=0;
		c.weightx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(header, c);
		
		// Set up the prompt
        header.setFont(new Font("Calibri", Font.PLAIN, 18));
		c.insets = new Insets(25, 0, 0, 0);
        c.ipady=0;
        c.gridy = 1;
		c.weightx = 0;
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(prompt, c);
	}
}

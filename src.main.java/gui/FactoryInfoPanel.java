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
 * This panel is shown when the Factory button is pressed on the menu. Information
 * here should be the brewery/distributer info.
 * @author ptraxler
 *
 */
public class FactoryInfoPanel extends JPanel{
	JLabel header = new JLabel("Brewery/Distributer info", SwingConstants.CENTER);
	public FactoryInfoPanel() {
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
	}
}
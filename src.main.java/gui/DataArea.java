package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * This creates a Data Area panel where the main content of the app will show. 
 * @author ptraxler
 *
 */
public class DataArea extends JComponent{
	
	/**
	 * Set up date area with default size and layout. 
	 */
	public DataArea() {
		this.setLayout(new FlowLayout());
		this.setPreferredSize(new Dimension(300,360));
	}
	
	/**
	 * Overriden paintComponent method to give it the resize functionality and
	 * the appearence we want. 
	 */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // For resize:
        this.setSize(this.getWidth(), (int) (this.getParent().getHeight()*.85));
        
        // Color is RBGA, where A is alpha (transparency)
        Color color = new Color(255,255,255,135);
        g.setColor(color);
        g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 20, 20);
    }
}

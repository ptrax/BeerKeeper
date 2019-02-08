package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JComponent;

/**
 * This is the panel on the left that the menu buttons sit on. The prefferred size is 
 * set here and a layout manager is given, and the paintComponenet method is overriden
 * for asthetics. 
 * 
 * @author Paul Traxler
 */
public class MenuPanel extends JComponent{
	
	/**
	 * Creates a new Menu Panel with a default size of 150 x 400.
 	 */
    public MenuPanel() {
    	this.setPreferredSize(new Dimension(150,400));
        this.setLayout(new GridLayout(3,1,0,-70));
    }
    
    /**
     * Overriden paintComponenet method setting the size to follow the parent and
     * giving it the transparency/rounded rectangle. 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // For resize:
        this.setSize(this.getWidth(), (int) (this.getParent().getHeight()*.85));
        
        // Color is RGBA, where A is alpha (transparency)
        Color color = new Color(255,255,255,175);
        g.setColor(color);
        g.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 20, 20);
    }
}

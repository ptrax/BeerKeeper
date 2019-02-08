package gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Background component for displaying a background image. 
 * @author Paul Traxler
 *
 */
public class Background extends JComponent{
    private Image image;
    
    /** 
     * Creates a background image component with a specified image.
     * @param backgroundImage - image to use
     */
    public Background(Image backgroundImage) {
    	// Set the background image
    	this.image = backgroundImage;
		
		// Set the layout so we can place things on this component
        this.setLayout(new BorderLayout());
    }
    
    /**
     * Overriden paintComponenet method so we can draw our image. 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this);
    }
}

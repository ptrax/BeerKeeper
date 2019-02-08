package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Menu button class for BeerKeeper menu buttons. This button is made to support 
 * a .png icon with a transparent background. Affects added here for button clicks. 
 * @author ptraxler
 *
 */
public class MenuButton extends JButton implements MouseListener{
	/**
	 * Creates a new menu button with the given icon. Icon should
	 * have transparent background. 
	 * @param icon - .png with transparent background
	 */
	public MenuButton(ImageIcon icon) {
		super();
		this.setIcon(icon);
		
		// These three things make the actual jButton invisible,
		// only the icon will show.
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		
		// Without this the click effeect won't show.
		addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		this.setSize(this.getWidth()-10, this.getHeight()-10); 
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.setSize(this.getWidth()+10, this.getHeight()+10); 
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {}
}

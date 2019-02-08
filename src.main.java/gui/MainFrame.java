package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is the main frame for the BeerKeeper application.
 * 
 * @author Paul Traxler
 */
public class MainFrame extends JFrame{
	// Set up the three menu buttons
	MenuButton beerBtn = new MenuButton(new ImageIcon(MainFrame.class.getResource("/beer2.png")));
	//MenuButton beerBtn = new MenuButton(new ImageIcon("resources/beer2.png"));
	//MenuButton approvalBtn = new MenuButton(new ImageIcon("resources/approval.png"));
	//MenuButton factoryBtn = new MenuButton(new ImageIcon("resources/factory.png"));

	MenuButton approvalBtn = new MenuButton(new ImageIcon(MainFrame.class.getResource("/approval.png")));
	MenuButton factoryBtn = new MenuButton(new ImageIcon(MainFrame.class.getResource("/factory.png")));
	
	// Set up the background
	Image i = Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/BeerStockPhoto3.jpg"));
	JComponent back = new Background(i);

	// Set up the panels for the menu and data portions
	JPanel dataPanel = new JPanel();
	MenuPanel menu = new MenuPanel();
	DataArea da = new DataArea();

	// Create a couple spacers for layout purposes
	JPanel spacer = new JPanel();
	JPanel spacer2 = new JPanel();
	
	public MainFrame() {
		// Reference to this frame for future use
		JFrame frame = this;
		
		// Set the spacer values
		spacer.setPreferredSize(new Dimension(10,40));
		spacer.setMaximumSize(new Dimension(10,40));
		spacer.setOpaque(false);
		spacer2.setOpaque(false);

		// Set up the data panel, where the main content will be
		dataPanel.setLayout(new BorderLayout());
		dataPanel.setBackground(new Color(255,255,255,0));
		
		// Set the content pane to the background componenet
		this.setContentPane(back);
		back.setLayout(new BorderLayout(20,0));
		
		// Add elements to the background compoenent
		back.add(spacer, BorderLayout.NORTH);
		back.add(menu,BorderLayout.WEST);
		back.add(dataPanel, BorderLayout.CENTER);
		back.add(spacer2, BorderLayout.EAST);

		// Add the buttons to the menu
		menu.add(approvalBtn);
		menu.add(beerBtn);
		menu.add(factoryBtn);
		
		// Add the Data Area to the data panel
		dataPanel.add(da);
		
		// Temporary label just for demonstrating button clicks
		JLabel label = new JLabel();
		da.add(label, BorderLayout.CENTER);

		//--------------- ACTION LISTENERS ------------------------
		
		// Beer button - for showing beer stock info
		beerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				label.setText("BeerXplorer");
				label.setFont(new Font("Serif", Font.PLAIN, 21));
				frame.repaint();
				frame.revalidate();
			}
		});
		
		// Approval button - For showing poularity info
		approvalBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				label.setText("Customer Ratings");
				label.setFont(new Font("Serif", Font.PLAIN, 21));
				frame.repaint();
				frame.revalidate();
			}
		});
		
		// Factory button - For showing Brewery/Distributer info
		factoryBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				label.setText("Distrubuters/Breweries");
				label.setFont(new Font("Serif", Font.PLAIN, 21));
				frame.repaint();
				frame.revalidate();
			}
		});
		
		// Frame setup stuff
		this.setSize(613, 518);
		this.setMaximumSize(new Dimension(613,518));
		this.setMinimumSize(new Dimension(400,400));
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setTitle("BeerKeeper");
	}
}

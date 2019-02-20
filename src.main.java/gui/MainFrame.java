package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This is the main frame for the BeerKeeper application. This has the background, and all other
 * panels/components have this as their ultimate parent. 
 * 
 * @author Paul Traxler
 */
public class MainFrame extends JFrame{
	// Set up the three menu buttons
	MenuButton beerBtn = new MenuButton(new ImageIcon(MainFrame.class.getResource("/beer2.png")));
	MenuButton approvalBtn = new MenuButton(new ImageIcon(MainFrame.class.getResource("/approval.png")));
	MenuButton factoryBtn = new MenuButton(new ImageIcon(MainFrame.class.getResource("/factory.png")));
	
	// Set up the background
	Image i = Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/BeerStockPhoto3.jpg"));
	JComponent back = new Background(i);

	// Set up the panels for the menu and data portions
	JPanel dataPanel = new JPanel();
	MenuPanel menu = new MenuPanel();
	DataArea dataArea = new DataArea();
	ConnectionSetupPanel connSetPanel = new ConnectionSetupPanel();
	HomePanel home = new HomePanel();

	// Create a couple spacers for layout purposes
	JPanel spacer = new JPanel();
	JPanel spacer2 = new JPanel();
	JPanel spacer3 = new JPanel();
	
	static MainFrame instance = null;
	
	/**
	 * Singleton MainFrame getter
	 * @return
	 */
	public static MainFrame getInstance() {
		if(instance == null) {
			instance = new MainFrame(); 
		}
		return instance; 
	}
	
	/**
	 * Private constructor for singleton implementation
	 */
	private MainFrame() {
		// Reference to this frame for future use
		JFrame frame = this;
		
		// Set the spacer values
		spacer.setPreferredSize(new Dimension(10,40));
		spacer.setMaximumSize(new Dimension(10,40));
		spacer3.setPreferredSize(new Dimension(10,60));

		spacer.setOpaque(false);
		spacer2.setOpaque(false);
		spacer3.setOpaque(false);
		
		// Set up the data panel, where the main content will be
		dataPanel.setLayout(new BorderLayout());
		dataPanel.setBackground(new Color(255,255,255,0));
		
		// Set the content pane to the background component
		this.setContentPane(back);
		back.setLayout(new BorderLayout(20,0));
		
		// Add elements to the background component
		back.add(spacer, BorderLayout.NORTH);
		back.add(menu,BorderLayout.WEST);
		back.add(dataPanel, BorderLayout.CENTER);
		back.add(spacer2, BorderLayout.EAST);

		// Add the buttons to the menu
		menu.add(approvalBtn);
		menu.add(beerBtn);
		menu.add(factoryBtn);
		
		// Temporary label just for demonstrating button clicks
		JLabel label = new JLabel(" ", SwingConstants.CENTER);
		dataArea.add(label, BorderLayout.NORTH);
		dataArea.add(connSetPanel, BorderLayout.NORTH);
		dataArea.add(spacer3, BorderLayout.SOUTH);
		
		// Add the Data Area to the data panel
		dataPanel.add(dataArea);
		
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
		
		// Approval button - For showing popularity info
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
	
	/**
	 * This method is provided to show a panel when requested. 
	 * @param panel
	 */
	public void showPanel(String panel) {
		// Show the Home panel after database connection
		if(panel.equals("Home")) {
			
			// Get rid of connection setup panel
			connSetPanel.setVisible(false);
			dataArea.remove(connSetPanel);
			
			// Add home panel
			dataArea.add(home, BorderLayout.NORTH);
			home.setVisible(true);
			dataArea.revalidate();
			this.revalidate();
		};
	}
	
}

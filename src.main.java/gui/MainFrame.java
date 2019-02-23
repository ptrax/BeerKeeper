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

import main.Controller;

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
	StockInfoPanel stockPanel = new StockInfoPanel();
	PopularityInfoPanel popPanel = new PopularityInfoPanel();
	FactoryInfoPanel factPanel = new FactoryInfoPanel();

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
                
                // Adding window icon
                ImageIcon icon = new ImageIcon(MainFrame.class.getResource("/beercon.png"));
                frame.setIconImage(icon.getImage());
		
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
				showPanel("stock");
			}
		});
		
		// Approval button - For showing popularity info
		approvalBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPanel("popularity");
			}
		});
		
		// Factory button - For showing Brewery/Distributer info
		factoryBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showPanel("factory");
			}
		});
		
		// Frame setup stuff
		this.setSize(613, 518);
		this.setMaximumSize(new Dimension(613,518));
		this.setMinimumSize(new Dimension(400,400));
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setTitle("BeerKeeper");
	}
	
	/**
	 * This method is provided to show a panel when requested. Panels will only show if 
	 * a connection has been established to the DB.
	 * @param panel
	 */
	public void showPanel(String panel) {
		
		// Check that we have a connection to the DB
		if(Controller.getInstance().isConnected()) {
			// Clean screen
			dataArea.removeAll();
			// Show the Home panel after database connection
			if(panel.equals("Home")) {
				dataArea.add(home, BorderLayout.NORTH);
				home.setVisible(true);
				dataArea.revalidate();
				this.revalidate();
			};
			
			// Change to Stock screen
			if(panel.equals("stock")) {
				stockPanel.setupCombos();
				dataArea.add(stockPanel, BorderLayout.NORTH);
				dataArea.revalidate();
				this.revalidate();
			}
			
			// Change to Brewery/Distributer screen
			if(panel.equals("factory")) {
                                factPanel.setupCombos();
				dataArea.add(factPanel, BorderLayout.NORTH);
				dataArea.revalidate();
				this.revalidate();
			}
			
			// Change to Popularity screen
			if(panel.equals("popularity")) {
				popPanel.setupCombos();
				dataArea.add(popPanel, BorderLayout.NORTH);
				dataArea.revalidate();
				this.revalidate();
			}
		}
	}
}

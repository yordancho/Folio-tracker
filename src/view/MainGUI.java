package view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.UIManager;
import controller.Controller;
import controller.IController;
import model.FolioTracker;
import model.IFolio;
import model.IFolioTracker;
import javax.swing.JSeparator;

/**
 * 
 * @author t07
 *
 */
public class MainGUI implements Observer {

	private JFrame frame;
	private IFolioTracker folioTracker;
	private JTabbedPane tabbedPane;
	private FolioViewer folioViewer;

	private IController controller;
	private JSeparator separator;
	private JSeparator separator_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IFolioTracker ft = new FolioTracker();
					ft.addFolio("Colin's Folio");
					ft.addFolio("Another Folio");
					ft.getFolioByName("Colin's Folio").addShare("GOOG", 100);
					ft.getFolioByName("Colin's Folio").editShare("GOOG", new Double(100), new Double(200));
					//folio.addShare("GOOG", 100);
					//folio.addShare("APPL", 50);
					MainGUI window = new MainGUI(ft);
					window.initialize();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI(IFolioTracker ft) {
		controller = new Controller(this, null, null,null);
		this.folioTracker = ft;
		ft.addObserver(this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Folio Tracker");
		frame.setResizable(false);
		frame.setBounds(100, 100, 1000, 701);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmNewPortfolio = new JMenuItem("New Portfolio");
		mntmNewPortfolio.addActionListener(controller);
		mntmNewPortfolio.setActionCommand("create new folio");
		mnFile.add(mntmNewPortfolio);
		
		JMenuItem mntmEditPortfolio = new JMenuItem("Edit Portfolio");
		mntmEditPortfolio.addActionListener(controller);
		mntmEditPortfolio.setActionCommand("edit folio");
		mnFile.add(mntmEditPortfolio);
		
		JMenuItem mntmClosePortfolio = new JMenuItem("Close Portfolio");
		mntmClosePortfolio.addActionListener(controller);
		mntmClosePortfolio.setActionCommand("close folio");
		mnFile.add(mntmClosePortfolio);
		
		separator_1 = new JSeparator();
		mnFile.add(separator_1);
		
		JMenuItem mntmLoadPortfolio = new JMenuItem("Load Portfolio");
		mntmLoadPortfolio.addActionListener(controller);
		mntmLoadPortfolio.setActionCommand("load folio");
		mnFile.add(mntmLoadPortfolio);
		
		JMenuItem mntmSavePortfolio = new JMenuItem("Save Portfolio");
		mntmSavePortfolio.addActionListener(controller);
		mntmSavePortfolio.setActionCommand("save folio");
		mnFile.add(mntmSavePortfolio);
		
		JMenuItem mntmExitPortfolio = new JMenuItem("Exit");
		mntmExitPortfolio.addActionListener(controller);
		
		separator = new JSeparator();
		mnFile.add(separator);
		mntmExitPortfolio.setActionCommand("exit");
		mnFile.add(mntmExitPortfolio);
		frame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		setFolios();
	}
	
	/**
	 * 
	 */
	public void setFolios(){
		List<IFolio> folios = folioTracker.getFolios();
		for(IFolio folio: folios){
			
			JPanel tab = new JPanel();
			folioViewer = new FolioViewer(folio);
	
			folioViewer.update((Observable) folio, null);
			folioViewer.setBorder(null);
			tab.add(folioViewer);
			
			tabbedPane.addTab(folio.getFolioName(), null, tab, null);
		}
		
	}
	
	/**
	 * 
	 * @return
	 */
	public IFolioTracker getFolioTracker() {
		return folioTracker;
	}

	/**
	 * 
	 */
	@Override
	public void update(Observable o, Object arg) {
		folioTracker = (IFolioTracker) o;
		String newFolioName = (String) arg;
		List<IFolio> folios = folioTracker.getFolios();
		//System.out.println("tab count: " + tabbedPane.getTabCount());
		//System.out.println("folio count: " + folios.size());
		
		if(tabbedPane.getTabCount() < folios.size()){
			
			JPanel tab = new JPanel();
			IFolio folio = folioTracker.getFolioByName(newFolioName);
			folioViewer = new FolioViewer(folio);
			folio.notifyObservers();
			folioViewer.setBorder(null);
			tab.add(folioViewer);

			tabbedPane.addTab(newFolioName, null, tab, null);
		}
		
		else if(tabbedPane.getTabCount() == folios.size()){
			int i = 0;
			for(IFolio folio: folios){
				if(!folio.getFolioName().equals(tabbedPane.getTitleAt(i))){
					tabbedPane.setTitleAt(i, folio.getFolioName());
				}
				i++;
			}
		}
	}
	
	/**
	 * 
	 * @return JTabbedPane
	 */
	public JTabbedPane getTabbedPane() {
		return tabbedPane;
	}

}

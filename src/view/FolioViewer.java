package view;

import javax.swing.JPanel;
import java.awt.GridBagLayout;
import controller.Controller;
import controller.IController;
import model.Folio;
import model.IFolio;
import model.IShare;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import java.awt.Font;

/**
 * 
 * @author t07
 *
 */
public class FolioViewer extends JPanel implements Observer {
	
	private IController controller;
	private IFolio folio;
	private DefaultTableModel tableModel;
	private JTextField txtTickerSymbol;
	private JTextField txtNoShares;
	private DecimalFormat df;
	
	private JButton btnRefresh;
	private JButton btnAdd;
	private JLabel lblFolioValue;
	private JLabel lblFolioGain;

	/**
	 * Class constructor. Create the panel.
	 * @param folioTracker 
	 * @param quote 
	 */
	public FolioViewer(IFolio folio) {
		folio.addObserver(this);
		controller = new Controller(null, this, null,null);
		this.folio = folio;
		df = new DecimalFormat( "#,###,###,##0.00" );
		
		initialiseTable();
	}
	
	/**
	 * 
	 */
	public void initialiseTable(){

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{60, 100, 100, 0, 100, 100, 100, 100, 100, 100, 0};
		gridBagLayout.rowHeights = new int[]{20, 27, 30, 300, 35, 35, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblTickerSymbol = new JLabel("Ticker Symbol");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		add(lblTickerSymbol, gbc_lblNewLabel);
		
		txtTickerSymbol = new JTextField();
		
		txtTickerSymbol.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					btnAdd.doClick();
			}
		});
		GridBagConstraints gbc_txtpnTickerSymbol = new GridBagConstraints();
		gbc_txtpnTickerSymbol.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnTickerSymbol.fill = GridBagConstraints.BOTH;
		gbc_txtpnTickerSymbol.gridx = 2;
		gbc_txtpnTickerSymbol.gridy = 1;
		add(txtTickerSymbol, gbc_txtpnTickerSymbol);
		txtTickerSymbol.setMaximumSize(new Dimension(100, 27));
		
		JLabel lblNoShares = new JLabel("No. Shares");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 4;
		gbc_lblNewLabel_1.gridy = 1;
		add(lblNoShares, gbc_lblNewLabel_1);
		
		txtNoShares = new JTextField();
		txtNoShares.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					btnAdd.doClick();
			}
		});
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.insets = new Insets(0, 0, 5, 5);
		gbc_textPane.fill = GridBagConstraints.BOTH;
		gbc_textPane.gridx = 5;
		gbc_textPane.gridy = 1;
		add(txtNoShares, gbc_textPane);
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(controller);
		btnAdd.setActionCommand("add share");
		GridBagConstraints gbc_btnAdd = new GridBagConstraints();
		gbc_btnAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnAdd.gridx = 6;
		gbc_btnAdd.gridy = 1;
		add(btnAdd, gbc_btnAdd);
		
		//--Table
		String[] columnNames = {"Ticker Symbol",
                "Buy Price",
                "Current Price",
                "No. Shares",
                "Value of Holding",
                "Gain"};
		Object[][] empty = {
				
		};
		tableModel = new DefaultTableModel(empty, columnNames);
		JTable table = new JTable(tableModel);
		table.setEnabled(false);
		table.addMouseListener(controller);
		
		JScrollPane scrollPane = new JScrollPane(table); 
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 9;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 3;
		add(scrollPane, gbc_scrollPane);
		table.setFillsViewportHeight(true);
		
		//--Table
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(controller);
		btnRefresh.setActionCommand("refresh");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 7;
		gbc_btnNewButton.gridy = 1;
		add(btnRefresh, gbc_btnNewButton);
		
		lblFolioValue = new JLabel("Total holding value of folio: " + df.format(folio.getTotalValue()));
		lblFolioValue.setFont(new Font("Calibri", Font.PLAIN, 14));
		lblFolioValue.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.gridwidth = 8;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 4;
		add(lblFolioValue, gbc_lblNewLabel_2);
		
		lblFolioGain = new JLabel("Gain of folio: " + df.format(folio.getRunningGain()));
		lblFolioGain.setFont(new Font("Calibri", Font.PLAIN, 14));
		GridBagConstraints gbc_lblTotalGainOf = new GridBagConstraints();
		gbc_lblTotalGainOf.anchor = GridBagConstraints.WEST;
		gbc_lblTotalGainOf.gridwidth = 8;
		gbc_lblTotalGainOf.insets = new Insets(0, 0, 0, 5);
		gbc_lblTotalGainOf.gridx = 1;
		gbc_lblTotalGainOf.gridy = 5;
		add(lblFolioGain, gbc_lblTotalGainOf);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see model.IObserver#update(model.Folio)
	 * 
	 * This update function clears the the JTable and re-populates it
	 */
	@Override
	public void update(Observable o, Object arg) {
		IFolio folio = (IFolio) o;
		tableModel.setRowCount(0); 
		for(IShare share: folio.getFolioShares()){
			String ticker = share.getTicker();
			String initialPrice = df.format(share.getInitialPrice());
			String currentPrice = df.format(share.getCurrentPrice());
			double noShares = share.getShareNo();
			String holdingValue = df.format(share.getHoldingValue());
			String profit = df.format(share.getGain());
			Object[] row = {ticker, initialPrice, currentPrice, noShares, holdingValue, profit};
			
			tableModel.addRow(row);
		}
		
		lblFolioValue.setText("Total holding value of folio: " + df.format(folio.getTotalValue()));
		lblFolioGain.setText("Gain of folio: " + df.format(folio.getRunningGain()));
		
	}

	/**
	 * 
	 * @return String
	 */
	public String getTickerSymbol() {
		return txtTickerSymbol.getText().toUpperCase();
	}
	
	/**
	 * @return String
	 */
	public String getNoShares() {
		return txtNoShares.getText();
	}

	/**
	 * 
	 * @return IFolio
	 */
	public IFolio getFolio() {
		return folio;
	}

	/**
	 * 
	 * @return JButton
	 */
	public JButton getBtnRefresh() {
		return btnRefresh;
	}

	/**
	 * 
	 * @return JButton
	 */
	public JButton getBtnAdd() {
		return btnAdd;
	}

	public void resetAddValues() {
		txtTickerSymbol.setText(null);
		txtNoShares.setText(null);
	}
}

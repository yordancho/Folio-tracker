package view;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import controller.Controller;
import controller.IController;
import model.IFolio;
import model.IShare;

import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import java.awt.Component;
import javax.swing.Box;
import java.text.DecimalFormat;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 * @author t07
 *
 */
public class EditShare extends JDialog {
	private JTextField txtNoShares;
	private JTextField txtInitialPrice;
	private IController controller;
	private IShare share;
	private JButton okButton;
	private DecimalFormat df;
	private IFolio folio;
	private EditShare editShare;

	/**
	 * Class constructor. Create the dialog.
	 */
	public EditShare(IFolio folio, IShare share) {
		editShare = this;
		controller = new Controller(null, null, this,null);
		this.folio = folio;
		this.share = share;
		df = new DecimalFormat( "#,###,###,##0.00" );
		
		String ticker = share.getTicker();
		double initialPrice = share.getInitialPrice();
		double currentPrice = share.getCurrentPrice();
		double noShares =  share.getShareNo();
    	double totalValue =  share.getHoldingValue();
    	String gain = df.format(share.getGain());
		
		
		
		setBounds(100, 100, 440, 390);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 100, 67, 106, 109, 0};
		gridBagLayout.rowHeights = new int[]{0, 40, 40, 40, 40, 40, 40, 40, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		getContentPane().setLayout(gridBagLayout);
		{
			Component horizontalStrut = Box.createHorizontalStrut(20);
			GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
			gbc_horizontalStrut.gridheight = 6;
			gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
			gbc_horizontalStrut.gridx = 0;
			gbc_horizontalStrut.gridy = 1;
			getContentPane().add(horizontalStrut, gbc_horizontalStrut);
		}
		{
			JLabel lblNewLabel = new JLabel("Editing portfolio: " + folio.getFolioName());
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
			gbc_lblNewLabel.gridwidth = 4;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
			gbc_lblNewLabel.gridx = 1;
			gbc_lblNewLabel.gridy = 1;
			getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			JLabel lblTicker = new JLabel("Ticker: " + ticker);
			GridBagConstraints gbc_lblTicker = new GridBagConstraints();
			gbc_lblTicker.anchor = GridBagConstraints.WEST;
			gbc_lblTicker.insets = new Insets(0, 0, 5, 5);
			gbc_lblTicker.gridx = 1;
			gbc_lblTicker.gridy = 2;
			getContentPane().add(lblTicker, gbc_lblTicker);
		}
		{
			JLabel lblNewLabel_1 = new JLabel("Current Value: " + currentPrice);
			GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
			gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel_1.gridx = 1;
			gbc_lblNewLabel_1.gridy = 3;
			getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		}
		{
			JLabel lblNoOfShares = new JLabel("No. of Shares: " + noShares);
			GridBagConstraints gbc_lblNoOfShares = new GridBagConstraints();
			gbc_lblNoOfShares.anchor = GridBagConstraints.WEST;
			gbc_lblNoOfShares.insets = new Insets(0, 0, 5, 5);
			gbc_lblNoOfShares.gridx = 1;
			gbc_lblNoOfShares.gridy = 4;
			getContentPane().add(lblNoOfShares, gbc_lblNoOfShares);
		}
		{
			txtNoShares = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.gridwidth = 2;
			gbc_textField.insets = new Insets(0, 0, 5, 5);
			gbc_textField.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField.gridx = 2;
			gbc_textField.gridy = 4;
			getContentPane().add(txtNoShares, gbc_textField);
			txtNoShares.setColumns(10);
		}
		{
			
			JLabel lblInitialValue = new JLabel("Initial Value: " + initialPrice);
			GridBagConstraints gbc_lblInitialValue = new GridBagConstraints();
			gbc_lblInitialValue.anchor = GridBagConstraints.WEST;
			gbc_lblInitialValue.insets = new Insets(0, 0, 5, 5);
			gbc_lblInitialValue.gridx = 1;
			gbc_lblInitialValue.gridy = 5;
			getContentPane().add(lblInitialValue, gbc_lblInitialValue);
		}
		{
			txtInitialPrice = new JTextField();
			GridBagConstraints gbc_textField_1 = new GridBagConstraints();
			gbc_textField_1.gridwidth = 2;
			gbc_textField_1.insets = new Insets(0, 0, 5, 5);
			gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
			gbc_textField_1.gridx = 2;
			gbc_textField_1.gridy = 5;
			getContentPane().add(txtInitialPrice, gbc_textField_1);
			txtInitialPrice.setColumns(10);
		}
		{
			JLabel lblTotalGain = new JLabel("Total Gain: " + gain);
			GridBagConstraints gbc_lblTotalGain = new GridBagConstraints();
			gbc_lblTotalGain.anchor = GridBagConstraints.WEST;
			gbc_lblTotalGain.insets = new Insets(0, 0, 5, 5);
			gbc_lblTotalGain.gridx = 1;
			gbc_lblTotalGain.gridy = 6;
			getContentPane().add(lblTotalGain, gbc_lblTotalGain);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.insets = new Insets(0, 0, 0, 5);
			gbc_buttonPane.fill = GridBagConstraints.BOTH;
			gbc_buttonPane.gridx = 3;
			gbc_buttonPane.gridy = 8;
			getContentPane().add(buttonPane, gbc_buttonPane);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(controller);
				buttonPane.add(okButton);
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						editShare.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * 
	 * @return JTextField
	 */
	public JTextField getTxtNoShares() {
		return txtNoShares;
	}

	/**
	 * 
	 * @return JTextField
	 */
	public JTextField getTxtInitialPrice() {
		return txtInitialPrice;
	}

	/**
	 * 
	 * @return IShare
	 */
	public IShare getShare() {
		return share;
	}

	/**
	 * 
	 * @return JButton
	 */
	public JButton getOkButton() {
		return okButton;
	}

	public IFolio getFolio() {
		return folio;
	}
}

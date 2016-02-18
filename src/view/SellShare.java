package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import controller.Controller;
import controller.IController;
import model.IFolio;
import model.IShare;

public class SellShare extends JDialog  {

	private IController controller;
	private IShare share;
	private JFrame frmSellShare;
	private JTextField txtSellShare;
	private IFolio folio;
	SellShare sellShare;
	
	public SellShare(IFolio folio, IShare share) {
		setTitle("Share Selling");
		controller = new Controller(null,null,null,this);
		
		sellShare = this;
		this.folio = folio;
		this.share = share;
		setBounds(100, 100, 300, 146);
		getContentPane().setLayout(null);
		{
			//Display current shares here
			JLabel lblCurrentShares = new JLabel("Current shares:    " + share.getShareNo());
			lblCurrentShares.setBounds(34, 11, 200, 14);
			getContentPane().add(lblCurrentShares);
		}
		{
			JLabel lblNewLabel = new JLabel("Shares to sell: " );
			lblNewLabel.setBounds(34, 36, 100, 14);
			getContentPane().add(lblNewLabel);
		}
		{
			txtSellShare = new JTextField();
			txtSellShare.setBounds(145, 36, 86, 20);
			getContentPane().add(txtSellShare );
			txtSellShare.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(34, 61, 166, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(controller);
				buttonPane.add(okButton);
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent e) {
						sellShare.dispose();
					}
					
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

	
	}
	
	public String getSellCount() {
		return txtSellShare.getText();
	}
	
	public IShare getShare() {
		return share;
	}
	public IFolio getFolio() {
		return folio;
	}
}

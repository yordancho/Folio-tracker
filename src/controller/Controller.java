package controller;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import model.IFolio;
import model.IShare;
import view.SellShare;
import view.EditShare;
import view.FolioViewer;
import view.MainGUI;

/**
 * 
 * @author t07
 *
 */
public class Controller implements IController{

	private MainGUI mainGUI;
	private FolioViewer fv;
	private EditShare editShare;
	private SellShare sellShare;

	/**
	 * Class constructor.
	 * 
	 * @param mainGUI
	 * @param fv
	 * @param editShare
	 */
	public Controller(MainGUI mainGUI, FolioViewer fv, EditShare editShare, SellShare sellShare) {
		this.mainGUI = mainGUI;
		this.fv = fv;
		this.editShare = editShare;
		this.sellShare = sellShare;
	}

	/**
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		// handle close

		switch (command) {
			case "save folio":
			saveFolio();
			break;
		case "load folio":
			loadFolio();
			break;
		case "exit":
			System.exit(0);
		case "close folio":
			closeFolio();
			break;
		case "edit folio":
			editFolioName();
			break;
		case "create new folio":
			createNewFolio();
			break;
		case "add share":
			addShare();
			break;
		case "refresh":
			refreshShares();
			break;
		}
		if (editShare != null) { // Editing shares
			editShare();
		} else if (sellShare != null) { // Selling shares
			sellShares();
		}
	}
	

	/**
	 * 
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2 && !e.isConsumed()) {
			refreshShares();
			Object[] choices = { "Sell", "Edit" };
			String shareOption = (String) JOptionPane.showInputDialog(null, "Please select a share option",
					"The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null, choices,																					// choices
					choices[0]);
			IFolio folio = fv.getFolio();
			e.consume();
			JTable table = (JTable) e.getSource();
			Point p = e.getPoint();
			int row = table.rowAtPoint(p);

			if (row >= 0) {
				String ticker = (String) table.getValueAt(row, 0);
				IShare share = fv.getFolio().getShareByTicker(ticker);
				double shareCount = share.getShareNo();
				
				if (shareOption != null && shareOption.equals("Sell")) {
					SellShare shareSelling = new SellShare(folio, share);
					shareSelling.setLocationRelativeTo(null);
					shareSelling.setVisible(true);
					
					shareSelling.addWindowListener(new WindowAdapter() {
						public void windowClosed(WindowEvent e) {
							IFolio f = fv.getFolio();

							String txtSellAmount = shareSelling.getSellCount();
							if(txtSellAmount != null && !txtSellAmount.isEmpty()){
								double sellAmount = Double.parseDouble(txtSellAmount);
								if (sellAmount == shareCount) {
									f.removeShare(share);
								}
								refreshShares();
							}
						}
					});
				}

				else if (shareOption != null && shareOption.equals("Edit")) {
					EditShare shareEditing = new EditShare(folio, share);
					shareEditing.addWindowListener(new WindowAdapter() {
						public void windowClosed(WindowEvent e) {
							refreshShares();
						}
					});
					shareEditing.setLocationRelativeTo(null);
					shareEditing.setVisible(true);
				}
			}
		}
	}
	
	/**
	 * 
	 */
	private void editFolioName() {
		Object[] choices = getFolioNames();
		boolean valid = false;
		
		if(getFolioNames()==null){
			createWarningMessage("No folio's to edit. Create a folio first.", "Invalid input");
		}
		else{
			String rename = (String) JOptionPane.showInputDialog(null, "Choose the folio you wish to rename",
					"The Choice of a Lifetime", JOptionPane.QUESTION_MESSAGE, null, 
					choices, 
					choices[0]);
	
			if (rename != null) {
				String folioName = JOptionPane.showInputDialog("Enter a new name", null);
	
				if (folioName != null) {
					while (!valid && folioName != null) {
						if (folioName.isEmpty()) {
							createWarningMessage("Please enter a folio name", "Empty input");
							folioName = JOptionPane.showInputDialog("Enter a new name", null);
						} 
						else if ((mainGUI.getFolioTracker().getFolioByName(folioName) != null)) {
							createWarningMessage("An existing folio already has that name, please enter a different name", "Name taken");
							folioName = JOptionPane.showInputDialog("Enter a new name", null);
						} 
						else {
							valid = true;
						}
					}
					if (folioName != null) {
	
						mainGUI.getFolioTracker().getFolioByName(rename).setFolioName(folioName);
						mainGUI.getFolioTracker().notifyObservers();
					}
				}
			}
		}
	}


	private void loadFolio() {
		IFolio f = null;
		JFileChooser jfc = new JFileChooser();
		File currentDIR = mainGUI.getFolioTracker().getDir();

		if (!currentDIR.exists()) {
			currentDIR.mkdirs();
		}
		jfc.setCurrentDirectory(currentDIR);

		int response = jfc.showOpenDialog(fv);
		if (response == jfc.APPROVE_OPTION) {

			try {
				File path = jfc.getSelectedFile();
				FileInputStream fiStream = new FileInputStream(path);
				ObjectInputStream oiStream = new ObjectInputStream(fiStream);
				f = (IFolio) oiStream.readObject();
				
				if (checkDuplicateFolios(f)) {
					createWarningMessage("Folio is already being shown.", "Duplicate folio");
				} else{
					mainGUI.getFolioTracker().addSavedFolio(f);
					mainGUI.getTabbedPane().setSelectedIndex(mainGUI.getTabbedPane().getTabCount() - 1);
					
				}
				oiStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean saveFolio() {
		int tabsOpen = mainGUI.getTabbedPane().getTabCount();
		if(tabsOpen == 0) {
			createWarningMessage("No open folio to be saved.", "Empty Folio Tracker");
			return false;
		} else {
			int currentTab = mainGUI.getTabbedPane().getSelectedIndex();
			String currentTabName = mainGUI.getTabbedPane().getTitleAt(currentTab);
			JFileChooser jfc = new JFileChooser();
			File currentDIR = mainGUI.getFolioTracker().getDir();
			if (!currentDIR.exists()) {
				currentDIR.mkdirs();
			}
			File initialFile = new File(currentDIR + File.separator + currentTabName);
	
			jfc.setCurrentDirectory(currentDIR);
			jfc.setSelectedFile(initialFile);
	
			int response = jfc.showSaveDialog(fv);
	
			File dir = jfc.getSelectedFile();
			File path = new File(dir + ".folio");
	
			if (response == jfc.APPROVE_OPTION) {
				try {
					FileOutputStream foStream = new FileOutputStream(path);
					ObjectOutputStream ooStream = new ObjectOutputStream(foStream);
	
					IFolio test = mainGUI.getFolioTracker().getFolioByName(currentTabName);
					ooStream.writeObject(test);
					JOptionPane.showMessageDialog(fv, "Folio saved");
					ooStream.close();
					return true;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return false;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			} 
		}
		return false;
	}


	private void closeFolio() {
		int tabsOpen = mainGUI.getTabbedPane().getTabCount();
		if(tabsOpen == 0) {
			createWarningMessage("No open folio to close.", "Empty Folio Tracker");
		} else {
			int currentTab = mainGUI.getTabbedPane().getSelectedIndex();
			String folioName = mainGUI.getTabbedPane().getTitleAt(currentTab);
			int response = JOptionPane.showConfirmDialog(null, "Would you like to save"
			 		+ " your folio?",
				        "Warning", JOptionPane.YES_NO_OPTION);
			
			boolean choice = false;
			if(response == JOptionPane.YES_OPTION) {
				 choice = saveFolio();				
			} 
			if((choice)||(response != JOptionPane.YES_OPTION)){
				mainGUI.getTabbedPane().remove(currentTab);
				mainGUI.getFolioTracker().deleteObserver(this.fv);
				mainGUI.getFolioTracker().deleteFolio(folioName);
			}
			
		}
	}

	/**
	 * 
	 * @param IFolio
	 * @return boolean
	 */
	private boolean checkDuplicateFolios(IFolio f) {
		for (int i = 0; i < mainGUI.getTabbedPane().getTabCount(); i++) {
			if (f.getFolioName().equals(mainGUI.getTabbedPane().getTitleAt(i))) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 
	 * @return
	 */
	private Object[] getFolioNames() {
		List<IFolio> folios = mainGUI.getFolioTracker().getFolios();
		List<String> folioNames = new ArrayList<String>();

		for (IFolio folio : folios) {
			folioNames.add(folio.getFolioName());
		}
		
		if(folioNames.size()>0)
			return folioNames.toArray();
		else
			return null;
	}

	private void addShare() {
		String ticker = fv.getTickerSymbol().replaceAll("\\s+", "");
		String noShares = fv.getNoShares().replaceAll("\\s+", "");
		
		if (!containsOnlyChars(ticker) && !isValidDouble(noShares)) {
			createWarningMessage("Ticker contains numbers and No. Shares is not a valid double", "Invalid inputs");
		} 
		else if (!containsOnlyChars(ticker) && isValidDouble(noShares)) {
			createWarningMessage("Ticker should only contain letters.", "Invalid input");
		} 
		else if (containsOnlyChars(ticker) && !isValidDouble(noShares)) {
			createWarningMessage("No. Shares input is not a correct Double.", "Invalid input");
		} 
		else if (ticker.isEmpty() || noShares.isEmpty()) {
			createWarningMessage("Ticker or No. Shares is empty", "Invalid input");
		}
		else if (Double.parseDouble(noShares) <= 0) {
			createWarningMessage("No. Shares cannot be less than or equal to zero.", "Invalid input");
		}
		else if (fv.getFolio().getSharePrice(ticker) == -1) {
			createWarningMessage("No share found with that ticker.", "Share not found");
		} 
		else {
			if(getInputSize(noShares) >= 10) {
				createWarningMessage("Share size to large, please enter in a smaller amount","Warning");
			} else {
				double amount = Double.parseDouble(noShares);
				fv.getFolio().addShare(ticker, amount);
			}
			// clears text from textboxes after add
			fv.resetAddValues();
		}
	}
	
	private void editShare() {
		String txtNoShares = editShare.getTxtNoShares().getText().replaceAll("\\s+", "");
		String txtInitialPrice = editShare.getTxtInitialPrice().getText().replaceAll("\\s+", "");
		IShare share = editShare.getShare();
		String ticker = share.getTicker();
		IFolio folio = editShare.getFolio();
		
		if (txtNoShares.isEmpty() && txtInitialPrice.isEmpty()) {
			createWarningMessage("Empty No. Shares and Initial price.", "Empty input");
		} 
		else if (!isValidDouble(txtNoShares) || !isValidDouble(txtInitialPrice)) {
			createWarningMessage("No. Shares or Initial Price is not a valid double.", "Invalid input");
		} else if(getInputSize(txtNoShares) >= 10) {
			createWarningMessage("Share size too large, please enter a smaller amount","Warning");
		}
		else if (!txtNoShares.isEmpty() && txtInitialPrice.isEmpty()) {
			double shareNo = Double.parseDouble(txtNoShares);
			folio.editShare(ticker, shareNo, null);
			share.setShareNo(shareNo);
			editShare.dispose();
		} 
		else if (txtNoShares.isEmpty() && !txtInitialPrice.isEmpty()) {
			double initialPrice = Double.parseDouble(txtInitialPrice);
			folio.editShare(ticker, null, initialPrice);
			editShare.dispose();
		} 
		else {
			double shareNo = Double.parseDouble(txtNoShares);
			double initialPrice = Double.parseDouble(txtInitialPrice);
			folio.editShare(ticker, shareNo, initialPrice);
			editShare.dispose();
		}
	}

	private void sellShares() {
		String txtSellAmount = sellShare.getSellCount().replaceAll("\\s+", "");
		IShare share = sellShare.getShare();

		double currentShareNo = share.getShareNo();
		IFolio folio = sellShare.getFolio();
		
		if (!isValidDouble(txtSellAmount)) {
			createWarningMessage("Input type is not a valid number.", "Invalid input");
		}
		else if (txtSellAmount.isEmpty()) {
			createWarningMessage("Please enter a selling value!", "Empty input");
		} 
		else {
			double sellShareNo = Double.parseDouble(txtSellAmount);
			if (sellShareNo == 0) {
				createWarningMessage("You cannot sell 0 shares", "How to sell zero?");
			} 
			else if (sellShareNo > currentShareNo) {
				createWarningMessage("Cannot sell more shares than you own.", "Amount too large");
			} 
			else {
				DecimalFormat df = new DecimalFormat("#,###,###,##0.00");
				int input = JOptionPane.showConfirmDialog(null,
						"Are you sure you wish to sell " + txtSellAmount + " of your stock" + " for "
								+ df.format((share.getCurrentPrice() * sellShareNo)) + "?",
						"WARNING", JOptionPane.YES_NO_OPTION);

				if (input == JOptionPane.YES_OPTION) {
					folio.sellShare(share.getTicker(), sellShareNo);
					sellShare.dispose();
				}
			}
		}
	}
	
	/**
	 * 
	 */
	private void refreshShares() {
		fv.getFolio().updateShares();
	}

	/**
	 * 
	 */
	private void createNewFolio() {
		String folioName = JOptionPane.showInputDialog("Please enter the folio name", null);

		if (folioName != null) {
			while (folioName.isEmpty() && folioName != null) {
				createWarningMessage("Please enter a folio name.", "Empty input");
				folioName = JOptionPane.showInputDialog("Please enter the folio name", null);
			}

			if(mainGUI.getFolioTracker().getFolioByName(folioName) == null) {
				mainGUI.getFolioTracker().addFolio(folioName);
				mainGUI.getTabbedPane().setSelectedIndex(mainGUI.getTabbedPane().getTabCount() - 1);
			} else{
				createWarningMessage("That folio already exists!", "Folio Exists!");
			}
				
				
				
		}
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	
	private int getInputSize(String input) {
		int count = 0;
		for(int i = 0; i < input.length(); i++) {
			if(Character.isDigit(input.charAt(i)) || Character.isLetter(input.charAt(i))) {
				count++;
			}
		}
		return count;
	}
	private boolean isValidDouble(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isDigit(str.charAt(i))){
				if(str.charAt(i) == '.'){
					for(int j=i+1; j<str.length(); j++){
						if(!Character.isDigit(str.charAt(j)))
							return false;
					}
				}
				else 
					return false;
			}
		}
		return true;
	}

	private boolean containsOnlyChars(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isLetter(str.charAt(i)))
				return false;
		}
		return true;
	}
	
	private void createWarningMessage(String message, String title){
		JOptionPane.showMessageDialog(null, "Error: "+ message, title,
				JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

}

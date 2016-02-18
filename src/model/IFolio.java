package model;

import java.util.List;
import java.util.Observer;

/**
 * Interface to access and modify information in Folio objects
 * @author t07
 *
 */
public interface IFolio {
	
	/**
	 * The getFolioShares methods returns the list of shares held by the folio
	 * 
	 * @return folio: List of current shares
	 * 
	 * @effects Returns the current shares within the folio
	 */
	public List<IShare> getFolioShares();
	
	/**
	 * The addShare method uses the ticker and amount parameter to add a new 
	 * share to the folio. If the share already exists within the folio then 
	 * the values are added to existing holding.
	 * 
	 * @param String - ticker
	 * @param double - amount
	 * @return boolean
	 * 
	 * @requires String ticker != null && is valid ticker symbol? && amount != null
	 * @effects adds a share to the current folio
	 * @modifies this
	 */
	public boolean addShare(String ticker, double amount);
	
	/**
	 * The removeShare method removes the share parameter from the folio's list 
	 * of shares.
	 * 
	 * @param IShare - share
	 * 
	 * @requires folio.contains(share)
	 * @effects removes a share from the current folio
	 * @modifies this
	 */
	public boolean removeShare(IShare share);
	
	/**
	 * The getSharePrice method returns the price of a share within the folio's 
	 * list of shares and is identified using the ticker parameter.
	 *  
	 * @param String - ticker
	 * @return double
	 * 
	 * @requires: quote != null && ticker.length > 0
	 */
	public double getSharePrice(String ticker);
	
	/**
	 * The editShare method replaces values in the share object with values 
	 * provided as parameters.
	 * 
	 * @param: String - ticker
	 * @param: Double - noShares
	 * @param: Double - buyPrice
	 * 
	 * @modifies: folio
	 */
	public void editShare(String ticker, Double noShares, Double buyPrice);
	
	/**
	 * The sellShare method sells a certain amount of shares from the folio. 
	 * 
	 * @param: String - ticker
	 * @param: double - amount
	 * 
	 * @modifies: folio
	 */
	public void sellShare(String ticker, double amount);
	
	/**
	 * The getFolioName method returns the name of the folio
	 * 
	 * @return String
	 */
	public String getFolioName();
	
	/**
	 * The setFolioName method renames the folio to the name provided by the
	 * folioName parameter.
	 * 
	 * @param folioName
	 * 
	 * @requires: name.length > 0
	 * @modifies: this
	 */
	public void setFolioName(String folioName);
	
	/**
	 * The getGain method returns the total gain for the folio. Total gain is
	 * the sum of each individual share's gain value.
	 * 
	 * @return double
	 */
	public double getGain();
	
	/**
	 * The getTotalValue method returns the total value of the folio. The total
	 * is calculated by summing the total value of each share in the folio's 
	 * list of shares.
	 * 
	 * @return double
	 */
	public double getTotalValue();
	
	/**
	 * The updateShares method cycles through each share currently held by the 
	 * folio. Each share receives a new value for latestPrice via a request to
	 * the Quote server. Once the new values are in place the observers of the 
	 * folio class will be notified.
	 * 
	 * @requires: folio.size > 0
	 * @modifies: this
	 * @effects: updates all shares to current price
	 */
	public void updateShares();
	
	/**
	 * The getShareByTickerPrice returns a particular share from the folio's 
	 * list of shares providing it's values match the ticker and initialPrices
	 * parameters. If no shares match the parameters then null is returned.
	 * 
	 * @param String - ticker
	 * @param double - initialPrice
	 * @return IFolio
	 */
	public IShare getShareByTicker(String ticker);
	
	/**
	 * @effects method to notify all observers of changes
	 * @see java.util.Observable#notifyObservers()
	 */
	public void notifyObservers();
	
	/**
	 * Adds an observer 
	 * @param Observer: Object we want to notify of changes
	 * 
	 * @requires: newObserver != null
	 * @effects: add observer to observer List
	 * @modifies: observers
	 */
	public void addObserver(Observer newObserver);
	
	public double getRunningGain();

}


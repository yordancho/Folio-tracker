package model;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import model.quoteServer.IQuote;
import model.quoteServer.MethodException;
import model.quoteServer.NoSuchTickerException;
import model.quoteServer.Quote;
import model.quoteServer.WebsiteDataException;


/**
 * Folio object which contains a collection of shares.
 * 
 * @author t07
 *
 */

public class Folio extends Observable implements IFolio, Serializable {
	
	private List<IShare> folio;
	private transient List<Observer> observers;
	private transient IQuote quote;
	private String name;
	private double runningGain;
	

	/**
	 * Class constructor
	 * 
	 * @param q
	 * @requires q != null
	 */
	public Folio(IQuote q){
		quote = q ;
		observers = new ArrayList<Observer>();
		folio = new ArrayList<IShare>();
	}
	
	/**
	 * The getFolioShares methods returns the list of shares held by the folio
	 * 
	 * @return folio: List of current shares
	 * 
	 * @effects Returns the current shares within the folio
	 */
	public List<IShare> getFolioShares() {
		return folio;
	}
	

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
	 * @modifies this.folio
	 */
	@Override
	public boolean addShare(String ticker, double amount){
		double latestPrice = getSharePrice(ticker);
		
		if(checkDuplicates(ticker)){
			for(int i=0; i<folio.size(); i++){
				if(folio.get(i).getTicker().equals(ticker) &&
						folio.get(i).getInitialPrice() == latestPrice){
					folio.get(i).setShareNo(folio.get(i).getShareNo() + amount);
					folio.get(i).setHoldingValue(folio.get(i).getShareNo() * latestPrice);
				}

				else if(folio.get(i).getTicker().equals(ticker) &&
						folio.get(i).getInitialPrice() != latestPrice){

					double oldGain = folio.get(i).getGain();
					double newNoShares = folio.get(i).getShareNo() + amount;
					double newInitialPrice = latestPrice - (oldGain/newNoShares);
					
					folio.get(i).setInitialPrice(newInitialPrice);
					folio.get(i).setShareNo(newNoShares);
					folio.get(i).setHoldingValue(newNoShares * latestPrice);
				}
			}
		}
		else {
			double holdingValue = amount * latestPrice;
			Share share = new Share(ticker, latestPrice, amount, holdingValue);
			folio.add(share);
		}
		
		notifyObservers();
		assert(!checkDuplicates(ticker)):"Share with ticker "+ticker+" failed to be added to folio!";
		return true;
	}
	

	/**
	 * The removeShare method removes the share parameter from the folio's list 
	 * of shares.
	 * 
	 * @param IShare - share
	 * 
	 * @requires folio.contains(share)
	 * @effects removes a share from the current folio
	 * @modifies this.folio
	 */
	@Override
	public boolean removeShare(IShare share){
		return folio.remove(share);
	}
	

	/**
	 * The updateShares method cycles through each share currently held by the 
	 * folio. Each share receives a new value for latestPrice via a request to
	 * the Quote server. Once the new values are in place the observers of the 
	 * folio class will be notified.
	 * 
	 * @requires: folio.size > 0
	 * @modifies: this.folio
	 * @effects: updates all shares to current price
	 */
	public void updateShares(){
		// needed for loading saved folios
		if(quote == null) {
			quote = new Quote(false);
		}
		double gainBefore = getGain();
		for(IShare share: folio){
			String ticker = share.getTicker();
			double noShares = share.getShareNo();
			try {
				quote.setValues(ticker);
				double latestPrice = quote.getLatest();
				double holdingValue = latestPrice * noShares;
				share.setCurrentPrice(latestPrice);
				share.setHoldingValue(holdingValue);
			} catch (IOException | WebsiteDataException | NoSuchTickerException | MethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		runningGain += getGain() - gainBefore;
		notifyObservers();
	}
	
	/**
	 * The getSharePrice method returns the price of a share within the folio's 
	 * list of shares and is identified using the ticker parameter.
	 *  
	 * @param String - ticker
	 * @return double
	 * 
	 * @requires: quote != null && ticker.length > 0
	 */
	public double getSharePrice(String ticker){
		// needed for loading saved folios
		if(quote == null) {
			quote = new Quote(false);
		}
		
		try {
			quote.setValues(ticker);
			return quote.getLatest();
		} 
		catch(NoSuchTickerException e){
			return -1;
		}
		catch (IOException | WebsiteDataException | 
				MethodException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
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
	public void editShare(String ticker, Double noShares, Double buyPrice){
		
		for(IShare share: folio){
			if(share.getTicker().equals(ticker)){
				double oldGain = share.getGain();
				if(noShares != null && buyPrice !=null){
					share.setShareNo(noShares);
					share.setInitialPrice(buyPrice);
				}
				else if(noShares != null){
					share.setShareNo(noShares);
				}
				else if(buyPrice != null){
					share.setInitialPrice(buyPrice);
				}
				runningGain += share.getGain() - oldGain;
				break;
			}
		}
		notifyObservers();
	}
	
	/**
	 * The sellShare method sells a certain amount of shares from the folio. 
	 * 
	 * @param: String - ticker
	 * @param: double - amount
	 * 
	 * @modifies: folio
	 */
	public void sellShare(String ticker, double amount){
		for(IShare share: folio){
			if(share.getTicker().equals(ticker)){
				
				double currentShareNo = share.getShareNo();
				double remainingShareNo = currentShareNo - amount;
				double oldGain = share.getGain();
				share.setShareNo(remainingShareNo);
			}
		}	
	}
	
	/**
	 * The checkDuplicates method checks if the folio contains shares which has
	 * the same ticker symbol as the the ticker parameter.
	 * 
	 * @param String - ticker
	 * @return boolean
	 * 
	 * @requires folio.size > 0 && ticker.length > 0
	 */
	public boolean checkDuplicates(String ticker){
		for(int i=0; i<folio.size(); i++){
			if(folio.get(i).getTicker().equals(ticker))
				return true;
		}
		return false;
	}


	/**
	 * The getFolioName method returns the name of the folio
	 * 
	 * @return String
	 */
	public String getFolioName() {
		assert(name!=null):"Folio name is null";
		return name;
	}

	/**
	 * The setFolioName method renames the folio to the name provided by the
	 * folioName parameter.
	 * 
	 * @param folioName
	 * 
	 * @requires: name.length > 0
	 * @modifies: this
	 */
	public void setFolioName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 */
	/*
	public double getFolioGain() {
		return folioGain;
	}
	*/
	
	/**
	 * The getGain method returns the total gain for the folio. Total gain is
	 * the sum of each individual share's gain value.
	 * 
	 * @return double
	 */
	public double getGain() {
		double folioGain = 0;
		for(IShare share: folio){
			folioGain += share.getGain();
		}
		return folioGain;
	}
	
	/**
	 * The getFolioValue method returns the total value of the folio. The total
	 * is calculated by summing the total holding value of each share in the 
	 * folio's list of shares.
	 * 
	 * @return double
	 */
	public double getTotalValue(){
		double total = 0;
		for(IShare share: folio){
			total += share.getHoldingValue();
		}
		return total;
	}
	
	/** 
	 * The getShareByTicker returns a particular share from the folio's 
	 * list of shares providing it's ticker matches the ticker parameter
	 * If no shares match the parameter then null is returned.
	 * 
	 * @param String - ticker
	 * @return IFolio
	 */
	public IShare getShareByTicker(String ticker){
		for(IShare share: folio){
			if(share.getTicker().equals(ticker)) 
				return share;
		}
		return null;
	}

	/**
	 * Adds an observer 
	 * 
	 * @param Observer: Object we want to notify of changes
	 * 
	 * @requires: newObserver != null
	 * @effects: add observer to observer List
	 * @modifies: observers
	 */
	@Override
	public void addObserver(Observer newObserver) {
		// TODO Auto-generated method stub
		if(observers==null)
			observers = new ArrayList<Observer>();
		
		observers.add(newObserver);
		assert(observers.contains(newObserver)):"Failed to add observer:"+newObserver.toString();
	}
	
	/**
	 * Removes an observer
	 * 
	 * @param Observer: Observer to be removed
	 * 
	 * @requires: deleteObserver != null
	 * @effects: removes deleteObserver instance from observer List
	 * @modifies: observers
	 */
	@Override
	public void deleteObserver(Observer deleteObserver) {
		// TODO Auto-generated method stub
		observers.remove(deleteObserver);
		assert(!observers.contains(deleteObserver)):"Failed to remove observer:"+deleteObserver.toString();
	}
	
	/**
	 * @effects method to notify all observers of changes
	 * @see java.util.Observable#notifyObservers()
	 */
	@Override
	public void notifyObservers(){
		if(observers != null){
			for(Observer observer: observers){
				observer.update(this, folio);
			}
		}
	}

	public double getRunningGain() {
		return runningGain;
	}
}

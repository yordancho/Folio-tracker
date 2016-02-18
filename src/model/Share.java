package model;

import java.io.Serializable;

/**
 * Share object which represents a stock held in folios.
 * 
 * @author t07
 *
 */
public class Share implements IShare, Serializable {

	private String ticker;
	private double noShares;
	private double initialPrice;
	private double currentPrice;
	private double holdingValue;
	private double gain;

	/**
	 * Class constructor.
	 * 
	 * @param String - ticker
	 * @param double - initialPrice
	 * @param double - noShares
	 * @param double - totalValue
	 */
	public Share(String ticker, double initialPrice ,
			double noShares, double totalValue){
		
		this.ticker = ticker;
		this.initialPrice = initialPrice;
		this.noShares = noShares;
		this.holdingValue = totalValue;
		
		currentPrice = initialPrice;
		gain = noShares * (currentPrice - initialPrice);
	}
	
	/**
	 * The getTicker method returns the share's ticker symbol
	 * 
	 * @return String
	 */
	public String getTicker() {
		assert(ticker!=null):"Ticker is null!";
		return ticker;
	}
	
	/**
	 * The getShareNo method returns the quantity of shares
	 * 
	 * @return double
	 */
	public double getShareNo() {
		assert(noShares>=0):"Negative number of shares:"+noShares;
		return noShares;
	}
	
	/**
	 * The setShareNo method modifies the number of shares held.
	 * 
	 * @param double - shareNo
	 * 
	 * modifies: this
	 */
	public void setShareNo(double shareNo) {
		assert(shareNo>=0):"Negative number of shares:"+shareNo;
		this.noShares = shareNo;
	}
	
	/**
	 * The getHoldingValue method returns the total value of share
	 * 
	 * @return double
	 */
	public double getHoldingValue() {
		return holdingValue;
	}
	
	/**
	 * The setHoldingValue method modifies the total value of share.
	 * 
	 * @param double
	 * 
	 * modifies: this
	 */
	public void setHoldingValue(double totalValue) {
		this.holdingValue = totalValue;
	}
	
	/**
	 * The getInitialPrice method returns the initial price of share
	 * 
	 * @return double
	 */
	public double getInitialPrice() {
		assert(initialPrice>=0):"Negative initial price:"+initialPrice;
		return initialPrice;
	}

	/**
	 * The setInitialPrice method modifies the initial price of share.
	 * 
	 * @param double - price
	 * 
	 * modifies: this
	 */
	public void setInitialPrice(double initialPrice) {
		assert(initialPrice>=0):"Negative initial price:"+initialPrice;
		this.initialPrice = initialPrice;
	}

	/**
	 * The getCurrentPrice method returns the current price of share
	 * 
	 * @return double
	 */
	public double getCurrentPrice() {
		assert(currentPrice>=0):"Negative current price:"+currentPrice;
		return currentPrice;
	}

	/**
	 * The setCurrentPrice method modifies the current price of share.
	 * 
	 * @param double
	 * 
	 * @modifies: this
	 */
	public void setCurrentPrice(double currentPrice) {
		assert(currentPrice>=0):"Negative current price:"+currentPrice;
		this.currentPrice = currentPrice;
	}
	
	/**
	 * Getter method to return the profit. Profit is calculated by subtracting 
	 * the initial share value from the current share value and multiplying the 
	 * result with the number of shares held.
	 * 
	 * @return double - gain
	 */
	public double getGain() {
		gain = noShares * (currentPrice - initialPrice);
		return gain;
	}
}

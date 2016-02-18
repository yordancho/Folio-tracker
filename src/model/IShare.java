package model;

/**
 * Interface to access and modify information in Share objects
 * 
 * @author t07
 *
 */
public interface IShare {
	
	/**
	 * The getTicker method returns the share's ticker symbol
	 * 
	 * @return String
	 */
	public String getTicker();
	
	/**
	 * The getShareNo method returns the quantity of shares
	 * 
	 * @return double
	 */
	public double getShareNo();
	
	/**
	 * The getInitialPrice method returns the initial price of share
	 * 
	 * @return double
	 */
	public double getInitialPrice();
	
	/**
	 * The getHoldingValue method returns the total value of share
	 * 
	 * @return double
	 */
	public double getHoldingValue();
	
	/**
	 * The getCurrentPrice method returns the current price of share
	 * 
	 * @return double
	 */
	public double getCurrentPrice();
	
	/**
	 * The setCurrentPrice method modifies the current price of share.
	 * 
	 * @param double
	 * 
	 * @modifies: this
	 */
	public void setCurrentPrice(double currentPrice);
	
	/**
	 * Getter method to return the gain. Gain is calculated by subtracting 
	 * the initial share value from the current share value and multiplying the 
	 * result with the number of shares held.
	 * 
	 * @return double - gain
	 */
	public double getGain();
	
	/**
	 * The setShareNo method modifies the number of shares held.
	 * 
	 * @param double - shareNo
	 * 
	 * modifies: this
	 */
	public void setShareNo(double shareNo);
	
	/**
	 * The setInitialPrice method modifies the initial price of share.
	 * 
	 * @param double - price
	 * 
	 * modifies: this
	 */
	public void setInitialPrice(double price);
	
	/**
	 * The setHoldingValue method modifies the total value of share.
	 * 
	 * @param double
	 * 
	 * modifies: this
	 */
	public void setHoldingValue(double totalValue);
	


}

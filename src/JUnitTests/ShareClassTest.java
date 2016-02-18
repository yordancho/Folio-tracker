package JUnitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.IShare;
import model.Share;

public class ShareClassTest {

	IShare s1;
	IShare s2;
	
	@Before
	public void setUp(){
		s1 = new Share("HAR",7.8,3,23.4);
		s2 = new Share("SKUL",5.1,50,255);
	}
	
	//ticker and name test
	@Test
	public void nameTest() {
		//getTicker
		assertTrue(s1.getTicker().equals("HAR"));
		assertTrue(s2.getTicker().equals("SKUL"));
	}

	//share test
	@Test
	public void shareTest() {
		//get number of shares
		assertTrue(s1.getShareNo()==3);
		assertTrue(s2.getShareNo()==50);
		
		//set number of shares
		s1.setShareNo(7);
		assertTrue(s1.getShareNo()==7);
		s2.setShareNo(30);
		assertTrue(s2.getShareNo()==30);
	}
	
	//initial price test
	@Test
	public void priceTest() {
		//get price
		assertTrue(s1.getInitialPrice()==7.8);
		assertTrue(s2.getInitialPrice()==5.1);
		
		//set price
		s1.setInitialPrice(3.2);
		assertTrue(s1.getInitialPrice()==3.2);
		s2.setInitialPrice(12.7);
		assertTrue(s2.getInitialPrice()==12.7);
	}
	
	//holding value test
	@Test
	public void holdingValueTest() {
		//get holdingVlaue
		assertTrue(s1.getHoldingValue()==23.4);
		assertTrue(s2.getHoldingValue()==255);
		
		//set holdingValue
		s1.setHoldingValue(32.5);
		assertTrue(s1.getHoldingValue()==32.5);
		s2.setHoldingValue(1300.5);
		assertTrue(s2.getHoldingValue()==1300.5);
	}
	
	//current price and gain test
	@Test
	public void priceProfitTest(){
		//set holdingValue
		s1.setCurrentPrice(32.5);
		assertTrue(s1.getCurrentPrice()==32.5);
		s2.setCurrentPrice(1300.5);
		assertTrue(s2.getCurrentPrice()==1300.5);
		
		//profit test
		double profit;
		s1.setInitialPrice(100);
		s1.setCurrentPrice(150);
		profit=s1.getShareNo()*50;
		assertTrue(s1.getGain()==profit);
		
		s2.setInitialPrice(50);
		s2.setCurrentPrice(32.5);
		profit=s2.getShareNo()*(-17.5);
		assertTrue(s2.getGain()==profit);
	}
}

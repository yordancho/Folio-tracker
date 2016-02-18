package JUnitTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Folio;
import model.FolioTracker;
import model.quoteServer.Quote;
import view.MainGUI;

public class FolioClassTest {

	Folio f1;
	Folio f2;
	
	@Before
	public void setUp(){
		f1 = new Folio(new Quote(false));
		f2 = new Folio(null);
	}
	
	//share test
	@Test 
	public void shareTest() {
		//no added shares
		assertTrue(f1.getFolioShares().size()==0);
		
		//add some shares
		assertTrue(f1.addShare("SKUL", 1.1));
		f1.editShare("SKUL", 1.1, new Double(33));
		assertTrue(f1.addShare("SKUL", 22.2));
		assertTrue(f1.addShare("HAR", 0));
		
		
		//test number of shares if valid
		assertTrue(f1.getFolioShares().size()==2);
		
		f1.getShareByTicker("HAR").setInitialPrice(0);
		assertTrue(f1.addShare("HAR", 10));
		
		//remove share HAR
		assertTrue(f1.removeShare(f1.getFolioShares().get(1)));
		assertTrue(f1.getFolioShares().size()==1);
		assertTrue(f1.getFolioShares().get(0).getTicker().equals("SKUL"));
		
		//get share by ticker
		assertTrue(f1.getShareByTicker("")==null);
		assertTrue(f1.getShareByTicker("SKUL").hashCode()==f1.getFolioShares().get(0).hashCode());
	}
	
	//update share test
	@Test
	public void updateTest(){
		f2.updateShares();
		//add a share
		assertTrue(f2.addShare("HAR", 0));
		f2.updateShares();
	}
	
	//edit share test
	@Test
	public void editTest(){
		//add some shares
		assertTrue(f1.addShare("SKUL", 50));
		assertTrue(f1.addShare("HAR", 10));
		
		//change share number and price
		f1.editShare("HAR", new Double(15), new Double(50));
		assertTrue(f1.getShareByTicker("HAR").getShareNo()==15);
		assertTrue(f1.getShareByTicker("HAR").getInitialPrice()==50);
		
		//change price
		f1.editShare("HAR", null, new Double(80));
		assertTrue(f1.getShareByTicker("HAR").getShareNo()==15);
		assertTrue(f1.getShareByTicker("HAR").getInitialPrice()==80);
		
		//change share number
		f1.editShare("HAR", new Double(25), null);
		assertTrue(f1.getShareByTicker("HAR").getShareNo()==25);
		assertTrue(f1.getShareByTicker("HAR").getInitialPrice()==80);
	}
	
	//sell share test
	@Test
	public void sellTest(){
		//add some shares
		assertTrue(f1.addShare("SKUL", 50));
		assertTrue(f1.addShare("HAR", 10));
		
		//invalid tracker
		f1.sellShare("",5);
		assertTrue(f1.getShareByTicker("SKUL").getShareNo()==50);
		assertTrue(f1.getShareByTicker("HAR").getShareNo()==10);
		
		//sell shares from one
		f1.sellShare("HAR",5);
		assertTrue(f1.getShareByTicker("SKUL").getShareNo()==50);
		assertTrue(f1.getShareByTicker("HAR").getShareNo()==5);
	}
	
	//Folio setter/getter tests
	@Test
	public void test() {	
		//test setName
		f1.setFolioName("testFolio1");
		assertTrue(f1.getFolioName().equals("testFolio1"));
	}
	
	//Observer tests
	@Test
	public void observerTest() {
		MainGUI observer = new MainGUI(new FolioTracker());
		
		f1.addObserver(observer);
		f1.deleteObserver(observer);
	}
	
	//test total value and gain
	@Test
	public void folioValue(){
		//add some shares
		f1.addShare("SKUL", 1);
		f1.addShare("SKUL", 0);
		f1.addShare("HAR", 3);
		
		assertTrue(f2.getSharePrice("")==-1);
		double expectedValue = f1.getSharePrice("SKUL")+f1.getSharePrice("HAR")*3;
		assertTrue(f1.getTotalValue()==expectedValue);
		assertTrue(f1.getGain()==0);
		assertTrue(f1.getRunningGain()==0);
	}
}

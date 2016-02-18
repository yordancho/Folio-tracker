package JUnitTests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import model.Folio;
import model.FolioTracker;
import model.quoteServer.Quote;
import view.MainGUI;

public class FolioTrackerClassTest {

	FolioTracker ft;
	
	@Before
	public void setUp(){
		ft = new FolioTracker();
	}
	
	//folio test
	@Test
	public void folioTest() {
		//no added folios
		assertEquals(ft.getFolios().size(),0);
		
		//add some folios
		ft.addFolio("f1");
		assertEquals(ft.getFolios().size(),1);
		ft.addFolio("f2");
		assertEquals(ft.getFolios().size(),2);
		ft.addFolio("f3");
		assertEquals(ft.getFolios().size(),3);
		
		//add saved folio
		ft.addSavedFolio(new Folio(new Quote(false)));
		assertEquals(ft.getFolios().size(),4);
		
		//get folio test
		assertEquals(ft.getFolioByName(""),null);
		assertEquals(ft.getFolioByName("f2").getFolioName(),"f2");
		
		//delete saved folio
		int number = ft.getFolios().size()-1;
		ft.deleteFolio(ft.getFolios().get(0).getFolioName());
		assertTrue(ft.getFolios().size()==number);
		
		assertFalse(ft.getQ()==null);
	}
	
	//rename test
	@Test
	public void renameTest(){
		//add some folios
		ft.addFolio("f1");
		ft.addFolio("f2");
		ft.addFolio("f3");
		
		//rename test
		ft.renameFolio("f1","folio1");
		ft.renameFolio("f3","folio3");
		ft.renameFolio("f2","folio2");
		assertEquals(ft.getFolios().get(0).getFolioName(),"folio1");
		assertEquals(ft.getFolios().get(1).getFolioName(),"folio2");
		assertEquals(ft.getFolios().get(2).getFolioName(),"folio3");
	}
	
	//file tests
	@Test
	public void fileTest() {	
		String path = System.getProperty("user.home") + File.separator+ "FolioTracker";
		System.out.println(path);
		assertEquals(ft.getDir().getPath(),path);
	}
	
	//Observer tests
	@Test
	public void observerTest() {
		MainGUI observer = new MainGUI(new FolioTracker());
		
		ft.addObserver(observer);
		ft.deleteObserver(observer);
	}
	
}

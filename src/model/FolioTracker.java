package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Observable;
import model.quoteServer.IQuote;
import model.quoteServer.Quote;

/**
 * The FolioTracker object is the back end for the FolioTracker system. It 
 * consists of a collection of folio objects and can access the Quote server 
 * to update the share prices and totals of the folios held. 
 * 
 * @author t07
 *
 */
public class FolioTracker extends Observable implements IFolioTracker {
	
	private List<Observer> observers;
	private List<IFolio> folios;
	private IQuote q;
	private String newFolioName;
	private File dir = new File(System.getProperty("user.home") +File.separator+ "FolioTracker");
	
	/**
	 * Class constructor
	 */
	public FolioTracker(){
		observers = new ArrayList<Observer>();
		folios  = new ArrayList<IFolio>();
		q = new Quote(false);
	}

	/**
	 * The getFolios method returns a List of current Folios
	 * 
	 * @return List of Folios
	 * 
	 */
	public List<IFolio> getFolios() {
		assert(folios!=null):"ArrayList folios is null";
		return folios;
	}
	
	/**
	 * The addFolio method creates a new Folio and add it to the Folio list
	 * 
	 * @param String folioName: name of folio to be added to Folio list
	 * 
	 * @requires folioName.length > 0
	 * @effects  creates a new Folio
	 * @modifies this.folios
	 */
	public void addFolio(String folioName){
		assert(folioName.length()>0):"Folio name doesn't meet length requirements (it is "+folioName.length()+")";
		IFolio newFolio = new Folio(q);
		newFolio.setFolioName(folioName);
		folios.add(newFolio);
		
		newFolioName = folioName;
		notifyObservers();
		assert(getFolioByName(folioName)!=null):"Failed to add folio "+folioName;
	}
	
	/**
	 * The deleteFolio method finds an existing folio and deletes it from the list
	 * 
	 * @param String folioName: name of folio to be removed from Folio list
	 * 
	 * @requires folioName.length > 0
	 * @effects  deletes a specified Folio
	 * @modifies this.folios
	 */
	public void deleteFolio(String folioName){
		IFolio folio = getFolioByName(folioName);
		folios.remove(folio);
		notifyObservers();
		assert(!folios.contains(folio)):"Failed to delete folio "+folio;
	}
	
	/**
	 * The addSavedFolio method adds a new Folio object to the current list of 
	 * folios
	 * 
	 * @param IFolio
	 * 
	 * @requires: folio != null
	 * @modifies: this.folios
	 */
	public void addSavedFolio(IFolio folio){
		folios.add(folio);
		newFolioName = folio.getFolioName();
		notifyObservers();
		assert(folios.contains(folio)):"Failed to load folio "+folio;
	}
	
	/**
	 * The renameFolio method changes the folio whose name matches the oldName 
	 * parameter and renames it to the newName parameter
	 * 
	 * @param String - oldName
	 * @param String - newName
	 * 
	 * @requires newName.length > 0 && folios.contains(getFolioByName(oldname))
	 * @effects renames a current folio in the Folios list
	 * @modifies this.folios
	 */
	public void renameFolio(String oldName, String newName){
		IFolio renameFolio = getFolioByName(oldName);
		renameFolio.setFolioName(newName);
		assert((getFolioByName(oldName)==null)&&(getFolioByName(newName)!=null)):
			"Failed to rename folio "+oldName+" to "+newName;
	}
	
	/**
	 * The getFolioByName method searches the list of folios for a folio which 
	 * matches the folioName parameter. If a match is found then that folio is 
	 * returned otherwise null is returned.
	 * 
	 * @param String - folioName: name of folio to search for
	 * 
	 * @requires: folioName != null 
	 * @effects: checks the folio list for a matching folioName
	 */
	public IFolio getFolioByName(String folioName){
		
		for(IFolio folio: folios){
			if(folioName.equals(folio.getFolioName()))
				return folio;
		}
		return null;
	}

	/**
	 * The getQ method returns the Quote server object
	 * 
	 * @return IQoute
	 */
	public IQuote getQ() {
		assert(q!=null):"Quote server is null";
		return q;
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
				observer.update(this, newFolioName);
			}
		}
	}
	
	/**
	 * The getDir method returns the File object which is the path to the 
	 * folder where folios will be stored.
	 * 
	 * @return File
	 */
	public File getDir() {
		return dir;
	}
}
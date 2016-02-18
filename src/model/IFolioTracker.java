package model;

import java.io.File;
import java.util.List;
import java.util.Observer;
import model.quoteServer.IQuote;

/**
 * Interface for accessing and modifying the FolioTracker object
 * 
 * @author t07
 *
 */
public interface IFolioTracker {

	/**
	 * The getFolios method returns a List of current Folios
	 * 
	 * @return List of Folios
	 */
	public List<IFolio> getFolios();
	
	/**
	 * The addFolio method creates a new Folio and add it to the Folio list
	 * 
	 * @param String folioName: name of folio to be added to Folio list
	 * 
	 * @requires folioName.length > 0
	 * @effects  creates a new Folio
	 * @modifies this.folios
	 */
	public void addFolio(String folioName);
	
	/**
	 * The deleteFolio method finds an existing folio and deletes it from the list
	 * 
	 * @param String folioName: name of folio to be removed from Folio list
	 * 
	 * @requires folioName.length > 0
	 * @effects  deletes a specified Folio
	 * @modifies this.folios
	 */
	public void deleteFolio(String folioName);
	
	/**
	 * The addSavedFolio method adds a new Folio object to the current list of 
	 * folios
	 * 
	 * @param IFolio
	 * 
	 * @requires: folio != null
	 * @modifies: this.folios
	 */
	public void addSavedFolio(IFolio folio);
	
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
	public void renameFolio(String oldName, String newName);
	
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
	public IFolio getFolioByName(String folioName);
	
	/**
	 * The getQ method returns the Quote server object
	 * 
	 * @return IQoute
	 */
	public IQuote getQ();
	
	/**
	 * The getDir method returns the File object which is the path to the 
	 * folder where folios will be stored.
	 * 
	 * @return File
	 */
	public File getDir();
	
	/**
	 * Adds an observer
	 *  
	 * @param Observer: Object we want to notify of changes
	 * 
	 * @requires: newObserver != null
	 * @effects: add observer to observer List
	 * @modifies: observers
	 */
	public void addObserver(Observer newObserver);
	
	/**
	 * Removes an observer
	 * 
	 * @param Observer: Observer to be removed
	 * 
	 * @requires: deleteObserver != null
	 * @effects: removes deleteObserver instance from observer List
	 * @modifies: observers
	 */
	public void deleteObserver(Observer deleteObserver);
	
	/**
	 * @effects method to notify all observers of changes
	 * @see java.util.Observable#notifyObservers()
	 */
	public void notifyObservers();
	
}

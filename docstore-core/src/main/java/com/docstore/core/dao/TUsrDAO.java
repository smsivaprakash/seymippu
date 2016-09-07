package com.docstore.core.dao;

import com.docstore.core.entity.TUsr;

public interface TUsrDAO {
	
	/**
	 * Stores a new TUsr entity object in to the persistent store
	 * 
	 * @param tUsr TUsr Entity object to be persisted
	 * @return tUsr Persisted TUsr object
	 */
	TUsr createTUsr(TUsr tUsr);

	/**
	 * Deletes a TUsr entity object from the persistent store
	 * 
	 * @param tUsr TUsr Entity object to be deleted
	 */
	void deleteTUsr(Integer tUsrId);

	/**
	 * Updates a TUsr entity object in to the persistent store
	 * 
	 * @param tUsr TUsr Entity object to be updated
	 * @return tUsr Persisted TUsr object
	 */
	TUsr updateTUsr(TUsr tUsr);

	/**
	 * Retrieve an TUsr object based on given custId.
	 * 
	 * @param tUsrId the primary key value of the TUsr Entity.
	 * @return an Object if it exists against given primary key. Returns null of not found
	 */
	TUsr findTUsrById(Integer tUsrId);

}

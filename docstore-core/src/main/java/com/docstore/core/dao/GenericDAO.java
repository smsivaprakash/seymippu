package com.docstore.core.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;

// Auto-generated Javadoc
/**
 * Interface represents implementation of generic DAO. The implementation for
 * GenericDAO could support any JPA Implementation like OpenJPA or Hibernate or
 * Toplink providers
 * 
 * @param <T>
 *            Object type
 * @param <I>
 *            Primary Key type
 * @author JCoE team
 * @version 1.0
 * 
 */
public interface GenericDAO {

	/**
	 * Remove an entity object from the persistent unit.
	 *
	 * @param <T> the generic type
	 * @param entity object to be removed.
	 */
	<T extends Serializable> void remove(final T entity);

	/**
	 * Stores a new entity object in to the persistent store.
	 *
	 * @param <T> the generic type
	 * @param entity entity object to be persisted
	 * @return persisted entity object
	 */
	<T extends Serializable> T store(final T entity);

	/**
	 * Save all changes made to an existing entity object.
	 *
	 * @param <T> the generic type
	 * @param entity entity object to be updated
	 * @return updated entity object
	 */
	<T extends Serializable> T update(final T entity);

	/**
	 * Save all changes made to an existing entities objects.
	 *
	 * @param <T> the generic type
	 * @param entity list of objects to be updated
	 * @return list of updated entities object
	 */
	<T extends Serializable> List<T> update(final List<T> entity);

	/**
	 * Retrieve an object based on given primary key value.
	 *
	 * @param <T> the generic type
	 * @param <I> the generic type
	 * @param clazz name of the class
	 * @param identification the primary key value, it could be a primitive value or a
	 * composite class object.
	 * @return an Object if it exists against given primary key. Returns null if
	 * not found
	 */
	<T, I extends Serializable> T get(Class<T> clazz, I identification);

	/**
	 * Fetch count of records based on given search criteria using JPA named
	 * query.
	 *
	 * @param <T> the generic type
	 * @param queryName The named query name to be searched
	 * @param queryParam The named query parameter
	 * @return an Object which contains count of an entity objects if it exists
	 * against given search criteria.
	 */
	<T extends Object> Object countEntitiesNamedQuery(final String queryName,
			final Object queryParam);

	/**
	 * Retrieve entities based on given search criteria.
	 *
	 * @param <T> the generic type
	 * @param queryName The named query name to be searched
	 * @param queryParams conditional parameters to be used in query
	 * @param index specifies start of the result
	 * @param maxresult specifies end of the result
	 * @return a list of an Objects if it exists against given conditional
	 * parameters. Returns null if not found
	 */

	<T extends Object> List<T> findEntitiesByNamedQueryMultiCond(
			final String queryName, final List<Object> queryParams,
			final int index, final int maxresult);

	/**
	 * 
	 * @param query
	 * @return
	 */
	int executeUpdateOnNativeQuery(final String query);
	

	/**
	 * Method to execute a Native SQL query.
	 *
	 * @param <T> the generic type
	 * @param query The native query to be executed
	 * @return a list of an Object if it exists against given search criteria.
	 * Returns null if not found
	 */
	<T extends Object> List<T> findByNativeQuery(final String query);
	
	
	/**
	 * Method to execute a Native SQL query.
	 *
	 * @param 
	 * @param query The native query to be executed
	 * @return BigInteger count if it exists against given search criteria.
	 * Returns null if not found
	 */
	BigInteger countByNativeQuery(final String query);

	/**
	 * loads all given entity objects.
	 *
	 * @param <T> the generic type
	 * @param clazz the name of the entity class
	 * @return a list of a given entity objects
	 */
	<T extends Object> List<T> loadAll(final Class<T> clazz);

	/**
	 * Retrieve entities based on given search criteria.
	 *
	 * @param <T> the generic type
	 * @param queryName The named query name to be searched
	 * @return a list of an Objects if it exists against a given conditional
	 * parameter specified in a named query. Returns null if not found
	 */
	<T extends Object> List<T> findEntitiesByNamedQuery(final String queryName);

	/**
	 * Updates the entity objects present in the given query.
	 *
	 * @param queryName The named query name to be searched
	 * @param queryParams conditional parameters to be used in query
	 * @return the number of updated entity objects
	 */
	int updateEntitiesNamedQuery(final String queryName,
			final List<Object> queryParams);

	/**
	 * Updates the entity objects present in the given query within the
	 * specified limits.
	 *
	 * @param queryName The named query name to be searched
	 * @param queryParams conditional parameters to be used in query
	 * @param index specifies start of the result
	 * @param maxresult specifies end of the result
	 * @return the number of updated entity objects
	 */
	int updateEntitiesByNamedQueryMultiCond(final String queryName,
			final List<Object> queryParams, final int index, final int maxresult);

	/**
	 * Retrieve entities based on given search criteria.
	 *
	 * @param <T> the generic type
	 * @param queryName The named query name to be searched
	 * @param queryParams conditional parameters to be used in query using IN clause
	 * @param index specifies start of the result
	 * @param maxresult specifies end of the result
	 * @return a list of an Objects if it exists against given conditional
	 * parameters. Returns null if not found
	 */
	<T extends Object> List<T> findEntitiesByNamedInQuery(
			final String queryName, final List<Object> queryParams,
			final int index, final int maxresult);

	/**
	 * Retrieve entities based on given search criteria.
	 *
	 * @param <T> the generic type
	 * @param queryName The named query name to be searched
	 * @param queryParams conditional parameters to be used in query
	 * @return a list of an Objects if it exists against a given conditional
	 * parameters. Returns null if not found
	 */
	<T extends Object> List<T> findEntitiesByNamedQuery(final String queryName,
			final List<Object> queryParams);

	/**
	 * persist entities using batch process.
	 *
	 * @param <T> the generic type
	 * @param entityList list of an entity
	 * @return a list of persisted entities objects
	 */
	<T extends Object> List<T> storeBatch(final List<T> entityList);

	/**
	 * Save all changes made to an existing entities objects using batch process.
	 *
	 * @param <T> the generic type
	 * @param entityList list of entities to be updated
	 * @return list of updated entities object
	 */
	<T extends Object> List<T> updateBatch(final List<T> entityList);

	/**
	 * Updates the entity objects present in the given named query using batch
	 * process.
	 *
	 * @param query The named query name to be searched
	 * @param queryParams conditional parameters to be used in query
	 * @return the number of updated entity objects
	 */
	int updateBatchByNamedQuery(final String query,
			final List<List<Object>> queryParams);

	/**
	 * get JPA session.
	 *
	 * @return a JPA Session object
	 */
	Session jpaSession();

	/*
	 * TCommNoteType findEntitiesById(Long tCommID);
	 * 
	 * TCommNoteType updateTCommNoteTypeById(TCommNoteType tCommNoteType);
	 */

	/**
	 * Method to execute a Native SQL query for INSERT, CREATE, UPDATE or DELETE
	 * operation.
	 * 
	 * @param query
	 *            The native query to be executed
	 * 
	 * @return the number of updated entity objects
	 */
	int executeNativeQuery(final String query);

	/**
	 * Retrieve entities based on given search criteria using native sql query.
	 *
	 * @param queryName The native queryName to be searched
	 * @param transformer The name of the class on which results needs be converted
	 * @return a list of an Objects of specified class type if it exists against
	 * given criteria. Returns null if not found
	 */
	List<?> findEntitiesByQuery(final String queryName, Class<?> transformer);

	/**
	 * Retrieve entities based on search criteria present in query.
	 *
	 * @param <T> the generic type
	 * @param query The query to be executed
	 * @param index specifies start of the result
	 * @param maxresult specifies end of the result
	 * @return a list of an Objects if it exists against conditional parameters
	 * present in query. Returns null if not found
	 */
	<T extends Object> List<T> findEntitiesByBuildQuery(final String query,
			final int index, final int maxresult);

	/**
	 * get entity manager.
	 *
	 * @return underlying JPA EntityManager Object
	 */
	EntityManager getEntityManagerFromJPA();

	/**
	 * Retrieve entities based on given search criteria.
	 *
	 * @param <T> the generic type
	 * @param query The query to be executed
	 * @param queryParams conditional parameters to be used in query
	 * @param index specifies start of the result
	 * @param maxresult specifies end of the result
	 * @return a list of an Objects if it exists against given conditional
	 * parameters. Returns null if not found
	 */
	<T extends Object> List<T> findEntitiesByBuildQueries(final String query,
			final List<Object> queryParams, final int index, final int maxresult);

	/**
	 * Fetch count of records based on given search criteria using JPA named
	 * query.
	 *
	 * @param <T> the generic type
	 * @param queryName The named query name to be searched
	 * @param queryParams The named query parameters
	 * @param index specifies start of the result
	 * @param maxresult specifies end of the result
	 * @return an Object which contains count of an entity objects if it exists
	 * against given search criteria.
	 */
	<T extends Object> Object countEntitiesByNamedQueryMultiCond(
			final String queryName, final List<Object> queryParams,
			final int index, final int maxresult);

	/**
	 * Method to execute a Native SQL query with a multi condition positional
	 * parameter.
	 * 
	 * @param query
	 *            The native query to be executed
	 * @param queryParams
	 *            The query parameters
	 * @param index
	 *            specifies start of the result
	 * @param maxresult
	 *            specifies end of the result
	 * @return a list of an Object if it exists against given search criteria.
	 *         Returns null if not found.
	 */
	List findByNativeQueryMultiCond(final String query,
			final List<Object> queryParams, final int index, final int maxresult);

	/**
	 * code to generate unique number from t_unique_key.
	 *
	 * @param group group name for which unique id needs be generated
	 * @param table a unique key generator table name
	 * @param valueCol the value col
	 * @param pKeyColumn the key column
	 * @return a unique id for the passed group name
	 */
	Long generateID(final String group, final String table,
			final String valueCol, final String pKeyColumn);

	/**
	 * Generate id.
	 *
	 * @param group the group
	 * @param table the table
	 * @param valueCol the value col
	 * @param pKeyColumn the key column
	 * @param maxlo the maxlo
	 * @return the long
	 */
	Long generateID(String group, String table, String valueCol,String pKeyColumn, String maxlo);
   
	/**
	 * Switch database context.
	 */
	public void switchDatabaseContext();
	
	/**
	 * Flush entity.
	 *
	 * @param <T> the generic type
	 */
	public <T extends Serializable> void flushEntity();
	
	/**
	 * Execute native query multi condition.
	 *
	 * @param query the query
	 * @param queryParams the query params
	 * @return the int
	 */
	public int executeNativeQueryMultiCondition(final String query,final List<Object> queryParams);
	
}

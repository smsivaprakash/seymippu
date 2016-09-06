package com.docstore.core.dao;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.cfg.ObjectNameNormalizer;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.MultipleHiLoPerTableGenerator;
import org.hibernate.internal.SessionImpl;
import org.hibernate.type.BigIntegerType;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.util.StringUtils;
import com.docstore.core.dao.GenericDAO;

/**
 * Class provides API implementation for GenericDAO.
 * 
 * @param <T>
 *            Object type
 * @param <I>
 *            Primary Key type
 * @author JCoE team
 * @version 1.0
 * 
 */

@SuppressWarnings("unchecked")
public class GenericDAOImpl extends JpaDaoSupport implements GenericDAO {

	private static final String DEFAULT_MAX_LO = "50";


	private static final Predicate[] VOID_PREDICATE_ARRAY = {};
	private boolean isChildConditionAvailable = false;

	/**
	 * Stores a new entity object in to the persistent store
	 * 
	 * @param entity
	 *            entity object to be persisted
	 * @return persisted entity object
	 */
	public <T extends Serializable> T store(final T entity) {
		/*
		 * System.out.println("=========== Store ===========");
		 * System.out.println("Store Entity : " + entity);
		 */

		getJpaTemplate().persist(entity);
		return entity;
	}

	/**
	 * Save all changes made to an existing entity object
	 * 
	 * @param entity
	 *            entity object to be updated
	 * @return updated entity object
	 */
	public <T extends Serializable> T update(final T entity) {
		/*
		 * System.out.println("=========== Update ===========");
		 * System.out.println("Udate Entity : " + entity);
		 */
		List<T> entityLS = new ArrayList<T>();
		entityLS.add(entity);

		return update(entityLS).get(0);
	}

	/**
	 * Save all changes made to an existing entities objects
	 * 
	 * @param entity
	 *            list of objects to be updated
	 * 
	 * @return list of updated entities object
	 */
	public <T extends Serializable> List<T> update(final List<T> entity) {
		/*
		 * System.out.println("=========== Update ===========");
		 * System.out.println("Udate Entity : " + entity);
		 */

		if (entity != null && entity.size() > 0) {
			List<T> list = new ArrayList<T>();
			for (T t : entity) {
				list.add((T) getJpaTemplate().merge(t));

			}
			getJpaTemplate().flush();
			return list;
		} else {
			//throw new OpservDataAccessException("entity cannot be empty");
			return null;
		}
	}

	/**
	 * Updates the entity objects present in the given query
	 * 
	 * @param queryName
	 *            The named query name to be searched
	 * @param queryParams
	 *            conditional parameters to be used in query
	 * 
	 * @return the number of updated entity objects
	 * 
	 */
	public int updateEntitiesNamedQuery(final String queryName,
			final List<Object> queryParams) {
		/*
		 * System.out.println("=========== countEntitiesNamedQuery ===========");
		 * System.out.println("queryName : " + queryName + "queryParam : " +
		 * queryParam);
		 */

		return updateEntitiesByNamedQueryMultiCond(queryName, queryParams, -1,
				-1);
	}

	/**
	 * Updates the entity objects present in the given query within the
	 * specified limits
	 * 
	 * @param queryName
	 *            The named query name to be searched
	 * @param queryParams
	 *            conditional parameters to be used in query
	 * @param index
	 *            specifies start of the result
	 * @param maxresult
	 *            specifies end of the result
	 * 
	 * 
	 * @return the number of updated entity objects
	 * 
	 */

	public int updateEntitiesByNamedQueryMultiCond(final String queryName,
			final List<Object> queryParams, final int index, final int maxresult) {
		return (Integer) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				// System.out.println("=========== findEntities ===========");
				// System.out.println("queryName : " + queryName + "index : " + index +
				// "maxresult : " + maxresult);

				if (queryParams != null && queryParams.size() > 0) {

					final Query queryNamed = entityMgr
							.createNamedQuery(queryName);

					Iterator itr = queryParams.iterator();

					int paramCounter = 1;
					while (itr.hasNext()) {
						queryNamed.setParameter(paramCounter, itr.next());
						paramCounter++;
					}

					if (index != -1) {
						queryNamed.setFirstResult(index);
					}
					if (maxresult != -1) {
						queryNamed.setMaxResults(maxresult);
					}
					return (Integer) queryNamed.executeUpdate();
				} else {
					//throw new OpservDataAccessException("queryParam list cannot be empty");
					return null;
				}
			}
		});

	}

	/**
	 * Save all changes made to an existing entities objects using batch process
	 * 
	 * @param entityList
	 *            list of objects to be updated
	 * 
	 * @return list of updated entities object
	 */
	public <T extends Object> List<T> updateBatch(final List<T> entityList) {
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				/* System.out.println("=========== Store List ==========="); */

				int batchSize = 1000;
				for (int i = 0; i < entityList.size(); i++) {
					Object entity = entityList.get(i);
					System.out.println("Store Entity : " + entity);
					entityMgr.merge(entity);
					if ((i + 1) % batchSize == 0) {
						entityMgr.flush();
						entityMgr.clear();
					}
				}
				entityMgr.flush();
				entityMgr.clear();
				return entityList;
			}
		});

	}

	/**
	 * Updates the entity objects present in the given named query using batch
	 * process
	 * 
	 * @param queryName
	 *            The named query name to be searched
	 * @param queryParams
	 *            conditional parameters to be used in query
	 * 
	 * @return the number of updated entity objects
	 * 
	 */
	public int updateBatchByNamedQuery(final String queryName,
			final List<List<Object>> queryParams) {
		return (Integer) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				System.out.println("=========== Store List ===========");
				int batchSize = 100;
				int i = 0;
				for (List<Object> argObj : queryParams) {
					final Query queryNamed = entityMgr
							.createNamedQuery(queryName);
					if (argObj != null) {
						Iterator itr = argObj.iterator();
						int paramCounter = 1;
						while (itr.hasNext()) {
							queryNamed.setParameter(paramCounter, itr.next());
							paramCounter++;
						}
					}
					queryNamed.executeUpdate();
					i++;
					if ((i + 1) % batchSize == 0) {
						entityMgr.flush();
						entityMgr.clear();
					}
				}

				entityMgr.flush();
				entityMgr.clear();
				return i;
			}
		});

	}

	/**
	 * Method to execute a Native SQL query for INSERT, CREATE, UPDATE or DELETE
	 * operation.
	 * 
	 * @param query
	 *            The native query to be executed
	 * 
	 * @return the number of updated entity objects
	 */
//	@Override
//	public int executeNativeQuery(final String query) {
//		return (int) getJpaTemplate().execute(new JpaCallback() {
//			public Object doInJpa(final EntityManager entityMgr)
//					throws PersistenceException {
//				// System.out.println("=========== findEntities ===========");
//				final Query queryJPQL = entityMgr.createNativeQuery(query);
//				return queryJPQL.executeUpdate();
//			}
//		});
//	}

	/**
	 * Remove an entity object from the persistent unit
	 * 
	 * @param entity
	 *            object to be removed.
	 * 
	 */
	public <T extends Serializable> void remove(final T entity) {
		/*
		 * System.out.println("=========== Remove Entity ===========");
		 * System.out.println("Enity : " + entity);
		 */

		getJpaTemplate().remove(entity);

	}

	/**
	 * Retrieve an object based on given primary key value.
	 * 
	 * @param clazz
	 *            name of the class
	 * @param identification
	 *            the primary key value, it could be a primitive value or a
	 *            composite class object.
	 * @return an Object if it exists against given primary key. Returns null if
	 *         not found
	 */
	public <T, I extends Serializable> T get(final Class<T> clazz,
			final I identification) {
		/*
		 * System.out.println("=========== Find By Id ===========");
		 * System.out.println("Id : " + identification);
		 */
		return getJpaTemplate().find(clazz, identification);
	}

	/**
	 * Retrieve entities based on given search criteria.
	 * 
	 * @param queryName
	 *            The named query name to be searched
	 * @return a list of an Objects if it exists against a given conditional
	 *         parameter specified in a named query. Returns null if not found
	 */

	public <T extends Object> List<T> findEntitiesByNamedQuery(
			final String queryName) {
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				/* System.out.println("=========== findEntities ==========="); */
				final Query namedQuery = entityMgr.createNamedQuery(queryName);
				return namedQuery.getResultList();
			}
		});
	}

	/**
	 * Retrieve entities based on given search criteria.
	 * 
	 * @param queryName
	 *            The named query name to be searched
	 * @param queryParams
	 *            conditional parameters to be used in query
	 * 
	 * @return a list of an Objects if it exists against a given conditional
	 *         parameters. Returns null if not found
	 */
	public <T extends Object> List<T> findEntitiesByNamedQuery(
			final String queryName, final List<Object> queryParams) {
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				/*
				 * System.out.println("=========== findEntities ===========");
				 * System.out.println("queryName : " + queryName);
				 */
				return findEntitiesByNamedQueryMultiCond(queryName,
						queryParams, -1, -1);

			}
		});
	}

	/**
	 * Retrieve entities based on given search criteria.
	 * 
	 * @param queryName
	 *            The named query name to be searched
	 * @param queryParams
	 *            conditional parameters to be used in query
	 * @param index
	 *            specifies start of the result
	 * @param maxresult
	 *            specifies end of the result
	 * 
	 * @return a list of an Objects if it exists against given conditional
	 *         parameters. Returns null if not found
	 */

	public <T extends Object> List<T> findEntitiesByNamedQueryMultiCond(
			final String queryName, final List<Object> queryParams,
			final int index, final int maxresult) {
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				// System.out.println("=========== findEntities ===========");
				// System.out.println("queryName : " + queryName + "index : " + index +
				// "maxresult : " + maxresult);

				// Setting the Query Cache for the query
				// queryNamed.setHint("org.hibernate.cacheable", true);
				// queryNamed.setHint("org.hibernate.cacheRegion",
				// "query.entityQueryCache");

				if (queryParams != null && queryParams.size() > 0) {
					final Query queryNamed = entityMgr
							.createNamedQuery(queryName);
					Iterator itr = queryParams.iterator();
					int paramCounter = 1;
					while (itr.hasNext()) {
						queryNamed.setParameter(paramCounter, itr.next());
						paramCounter++;
					}

					if (index != -1) {
						queryNamed.setFirstResult(index);
					}
					if (maxresult != -1) {
						queryNamed.setMaxResults(maxresult);
					}
					return queryNamed.getResultList();
				} else {
					//throw new OpservDataAccessException("queryParam cannot be empty");
					return null;
				}
			}
		});
	}

	/**
	 * Retrieve entities based on given search criteria.
	 * 
	 * @param queryName
	 *            The named query name to be searched
	 * @param queryParams
	 *            conditional parameters to be used in query using IN clause
	 * @param index
	 *            specifies start of the result
	 * @param maxresult
	 *            specifies end of the result
	 * 
	 * @return a list of an Objects if it exists against given conditional
	 *         parameters. Returns null if not found
	 */
	public <T extends Object> List<T> findEntitiesByNamedInQuery(
			final String queryName, final List<Object> queryParams,
			final int index, final int maxresult) {
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				// System.out.println("=========== findEntities ===========");
				// System.out.println("queryName : " + queryName + "index : " + index +
				// "maxresult : " + maxresult);

				final Query queryNamed = entityMgr.createNamedQuery(queryName);
				if (queryParams != null) {
					queryNamed.setParameter("paramList", queryParams);
				}
				if (index != -1) {
					queryNamed.setFirstResult(index);
				}
				if (maxresult != -1) {
					queryNamed.setMaxResults(maxresult);
				}
				return queryNamed.getResultList();
			}
		});
	}

	/**
	 * Retrieve entities based on given search criteria using native sql query
	 * 
	 * @param queryName
	 *            The native queryName to be searched
	 * @param transformer
	 *            The name of the class on which results needs be converted
	 * @return an list of an Objects of specified class type if it exists
	 *         against given criteria. Returns null if not found
	 */
	public List<?> findEntitiesByQuery(final String queryName,
			final Class<?> transformer) {
		return (List<?>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				// System.out.println("=========== findEntities ===========");
				final Query namedQuery = entityMgr.createNativeQuery(queryName,
						transformer);
				// namedQuery.unwrap(DataSetDescription.class);
				return namedQuery.getResultList();
			}
		});
	}

	/**
	 * Fetch count of records based on given search criteria using JPA named
	 * query.
	 * 
	 * @param queryName
	 *            The named query name to be searched
	 * @param queryParam
	 *            The named query parameter
	 * @return an Object which contains count of an entity objects if it exists
	 *         against given search criteria.
	 */

	public <T extends Object> Object countEntitiesNamedQuery(
			final String queryName, final Object queryParam) {
		/*
		 * System.out.println("=========== countEntitiesNamedQuery ===========");
		 * System.out.println("queryName : " + queryName + "queryParam : " +
		 * queryParam);
		 */
		List<Object> queryParamLS = new ArrayList<Object>();
		queryParamLS.add(queryParam);
		return countEntitiesByNamedQueryMultiCond(queryName, queryParamLS, -1,
				-1);
	}

	/**
	 * Fetch count of records based on given search criteria using JPA named
	 * query.
	 * 
	 * @param queryName
	 *            The named query name to be searched
	 * @param queryParams
	 *            The named query parameters
	 * @param index
	 *            The index of the search
	 * @param maxresult
	 *            The page size of the search
	 * 
	 * @return an Object which contains count of an entity objects if it exists
	 *         against given search criteria.
	 */
	public <T extends Object> Object countEntitiesByNamedQueryMultiCond(
			final String queryName, final List<Object> queryParams,
			final int index, final int maxresult) {
		return (Object) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				/*
				 * System.out.println("=========== findEntities ===========");
				 * System.out.println("queryName : " + queryName + "index : " + index +
				 * "maxresult : " + maxresult);
				 */

				if (queryParams != null && queryParams.size() > 0) {
					final Query queryNamed = entityMgr
							.createNamedQuery(queryName);

					// Setting the Query Cache for the query
					/*
					 * queryNamed.setHint("org.hibernate.cacheable", true);
					 * queryNamed.setHint("org.hibernate.cacheRegion",
					 * "query.entityQueryCache");
					 */

					if (queryParams != null) {
						Iterator itr = queryParams.iterator();
						int paramCounter = 1;
						while (itr.hasNext()) {
							queryNamed.setParameter(paramCounter, itr.next());
							paramCounter++;
						}
					}
					if (index != -1) {
						queryNamed.setFirstResult(index);
					}
					if (maxresult != -1) {
						queryNamed.setMaxResults(maxresult);
					}
					return queryNamed.getSingleResult();
				} else {
					//throw new OpservDataAccessException("queryParam cannot be empty.");
					return null;
				}
			}
		});
	}

	public int executeUpdateOnNativeQuery(final String query) {
		return (Integer) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				/* System.out.println("=========== executeUpdateOnNativeQuery ==========="); */
				final Query queryJPQL = entityMgr.createNativeQuery(query);
				return queryJPQL.executeUpdate();
			}
		});

	}


	/**
	 * Method to execute a Native SQL query.
	 * 
	 * @param query
	 *            The native query to be executed
	 * @return a list of an Object if it exists against given search criteria.
	 *         Returns null if not found
	 */
	public <T extends Object> List<T> findByNativeQuery(final String query) {
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				/* System.out.println("=========== findEntities ==========="); */
				final Query queryJPQL = entityMgr.createNativeQuery(query);
				return queryJPQL.getResultList();
			}
		});
	}


	/**
	 * Method to execute a Native SQL query.
	 * 
	 * @param query
	 *            The native query to be executed
	 * @return a list of an Object if it exists against given search criteria.
	 *         Returns null if not found
	 */
	public BigInteger countByNativeQuery(final String query) {
		return (BigInteger) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				/* System.out.println("=========== findEntities ==========="); */
				final Query queryJPQL = entityMgr.createNativeQuery(query);
				return queryJPQL.getSingleResult();
			}
		});
	}

	/**
	 * Method to execute a Native SQL query with a multi condition positional
	 * parameter.
	 * 
	 * @param query
	 *            The native query to be executed
	 * @return a list of an Object if it exists against given search criteria.
	 *         Returns null if not found.
	 */
	public List findByNativeQueryMultiCond(final String query,
			final List<Object> queryParams, final int index, final int maxresult) {
		return (List) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				// System.out.println("=========== findEntities ===========");

				final Query queryJPQL = entityMgr.createNativeQuery(query);

				if (queryParams != null) {
					Iterator itr = queryParams.iterator();
					int paramCounter = 1;
					while (itr.hasNext()) {
						queryJPQL.setParameter(paramCounter, itr.next());
						paramCounter++;
					}
				}
				if (index != -1) {
					queryJPQL.setFirstResult(index);
				}
				if (maxresult != -1) {
					queryJPQL.setMaxResults(maxresult);
				}

				return queryJPQL.getResultList();
			}
		});
	}

	/**
	 * loads all given entity objects
	 * 
	 * @param clazz
	 *            the name of the entity class
	 * @return a list of a given entity objects
	 */
	public <T extends Object> List<T> loadAll(final Class<T> clazz) {
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				final Query query = entityMgr.createQuery("from "
						+ clazz.getName());
				// Session session = (Session)entityMgr.getDelegate();
				// session.enableFilter ("createdBy").setParameter ("createdBy",
				// 1);
				return query.getResultList();
			}
		});
	}

	/**
	 * persist entities using batch process
	 * 
	 * @param entityList
	 *            list of an entity
	 * @return a list of persisted entities objects
	 */
	public <T extends Object> List<T> storeBatch(final List<T> entityList) {
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				System.out.println("=========== Store List ===========");

				int batchSize = 100;
				for (int i = 0; i < entityList.size(); i++) {
					Object entity = entityList.get(i);
					System.out.println("Store Entity : " + entity);
					entityMgr.persist(entity);
					if (i + 1 % batchSize == 0) {
						entityMgr.flush();
						entityMgr.clear();
					}
				}
				entityMgr.flush();
				entityMgr.clear();
				return entityList;
			}
		});

	}

	/**
	 * get JPA session
	 * 
	 * @return a JPA Session object
	 */

	@Override
	public Session jpaSession() {

		final Session session = getJpaTemplate().execute(
				new JpaCallback<Session>() {
					@Override
					public Session doInJpa(EntityManager em)
							throws PersistenceException {
						return (Session) em.unwrap(Session.class);
					}
				});

		return session;
	}

	/**
	 * Retrieve entities based on search criteria present in query.
	 * 
	 * @param query
	 *            The query to be executed
	 * @param index
	 *            specifies start of the result
	 * @param maxresult
	 *            specifies end of the result
	 * 
	 * @return a list of an Objects if it exists against conditional parameters
	 *         present in query. Returns null if not found
	 */

	public <T extends Object> List<T> findEntitiesByBuildQuery(
			final String query, final int index, final int maxresult) {
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				// System.out.println("=========== findEntities ===========");
				// System.out.println("queryName : " + query + "index : " + index +
				// "maxresult : " + maxresult);

				final Query queryNamed = entityMgr.createQuery(query);

				if (index != -1) {
					queryNamed.setFirstResult(index);
				}
				if (maxresult != -1) {
					queryNamed.setMaxResults(maxresult);
				}
				return queryNamed.getResultList();
			}
		});

	}

	/**
	 * Retrieve entities based on given search criteria.
	 * 
	 * @param query
	 *            The query to be executed
	 * @param queryParams
	 *            conditional parameters to be used in query
	 * @param index
	 *            specifies start of the result
	 * @param maxresult
	 *            specifies end of the result
	 * 
	 * @return a list of an Objects if it exists against given conditional
	 *         parameters. Returns null if not found
	 */

	public <T extends Object> List<T> findEntitiesByBuildQueries(
			final String query, final List<Object> queryParams,
			final int index, final int maxresult) {
		return (List<T>) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				// System.out.println("=========== findEntities ===========");
				// System.out.println("queryName : " + query + "index : " + index +
				// "maxresult : " + maxresult);

				final Query queryNamed = entityMgr.createQuery(query);

				if (queryParams != null) {
					final Iterator itr = queryParams.iterator();
					int paramCounter = 1;
					while (itr.hasNext()) {
						queryNamed.setParameter(paramCounter, itr.next());
						paramCounter++;
					}
				}
				if (index != -1) {
					queryNamed.setFirstResult(index);
				}
				if (maxresult != -1) {
					queryNamed.setMaxResults(maxresult);
				}
				return queryNamed.getResultList();
			}
		});

	}

	/**
	 * get entity manager
	 * 
	 * @return underlying JPA EntityManager Object
	 */
	public EntityManager getEntityManagerFromJPA() {
		return getJpaTemplate().getEntityManagerFactory().createEntityManager();
	}

	/**
	 * Method to call Stored procedure to validate isShape Polygon is Continuous
	 * 
	 * @param destSalesSPId
	 *            Dest Sales Position Id
	 * @param destHierId
	 *            Dest Sales Hier Id
	 * @param zipList
	 *            ZipCode List
	 * @param optUORD
	 *            operation Union or Difference
	 * @param lowestTableName
	 *            lowest table name of the default country
	 * @param userId
	 *            logged in userId
	 * 
	 * @return Object return from Stored procedure
	 */

	public <T extends Object> Object isShapePolygonContinousProc(
			final Long destSalesSPId, final Long destHierId,
			final String zipList, final String optUORD,
			final String lowestTableName, Integer userId) {
		return (Object) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				System.out.println("=========== isShapePolygonContinousProc ========START=== ");
				String zipFinal = "" + zipList.toString() + "";
				System.out.println("=========== destSalesSPId = " + destSalesSPId);
				System.out.println("=========== destHierId = " + destHierId);
				System.out.println("=========== zipStr = " + zipFinal);
				System.out.println("=========== operationFlag = " + optUORD);
				System.out.println("=========== lowestTableName = " + lowestTableName);
				System.out.println("=========== START TIME = "
						+ System.currentTimeMillis());
				final Query queryJPQL = entityMgr
						.createNativeQuery("{ CALL P2_isShape_Continuous_M(?,?,?,?,?) }");
				queryJPQL.setParameter(1, destSalesSPId);
				queryJPQL.setParameter(2, destHierId);
				queryJPQL.setParameter(3, zipFinal);
				queryJPQL.setParameter(4, optUORD);
				queryJPQL.setParameter(5, lowestTableName);

				String result = (String) queryJPQL.getSingleResult();
				System.out.println("=========== END TIME = "
						+ System.currentTimeMillis());
				System.out.println("=========== isShapePolygonContinousProc ========END=== "
						+ result);

				return new String(result);
			}
		});
	}

	/**
	 * Method to call Stored procedure to Updated Shape to destination SP and
	 * remove shape from Source SP
	 * 
	 * @param destSalesSPId
	 *            Dest Sales Position Id
	 * @param destHierId
	 *            Dest Sales Hier Id
	 * @param zipList
	 *            ZipCode List
	 * @param zipAssignedList
	 *            ZipCode List assigned to Source SP
	 * @param flagAssignOrUnassign
	 *            operation Assign or UnAssign
	 * @param lowestTableName
	 *            lowest table name of the default country
	 * @param userId
	 *            logged in userId
	 * 
	 * @return Object return from Stored procedure
	 */
	public <T extends Object> Object updateChildParentShapePolygonProc(
			final Long destSalesSPId, final Long destHierId,
			final String zipList, final String zipAssignedList,
			final String flagAssignOrUnassign, final String lowestTableName,
			final Integer userId) {
		return (Object) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)
					throws PersistenceException {
				System.out.println("=========== updateChildParentShapePolygonProc ========START=== ");
				System.out.println("=========== destSalesSPId = " + destSalesSPId);
				System.out.println("=========== destHierId = " + destHierId);
				System.out.println("=========== zipStr = " + zipList);
				System.out.println("=========== zipAssignedList = " + zipAssignedList);
				System.out.println("=========== flagAssignOrUnassign = "
						+ flagAssignOrUnassign);
				System.out.println("=========== lowestTableName = " + lowestTableName);
				System.out.println("=========== userId = " + userId);
				System.out.println("=========== START TIME = "
						+ System.currentTimeMillis());
				final Query queryJPQL = entityMgr
						.createNativeQuery("{ CALL P7_Update_Shape_AssignUnAssignGeo_M(?,?,?,?,?,?,?) }");
				queryJPQL.setParameter(1, destSalesSPId);
				queryJPQL.setParameter(2, destHierId);
				queryJPQL.setParameter(3, zipList);
				queryJPQL.setParameter(4, zipAssignedList);
				queryJPQL.setParameter(5, flagAssignOrUnassign);
				queryJPQL.setParameter(6, lowestTableName);
				queryJPQL.setParameter(7, userId);

				String result = (String) queryJPQL.getSingleResult();
				System.out.println("=========== END TIME = "
						+ System.currentTimeMillis());
				System.out.println("=========== updateChildParentShapePolygonProc ========END=== "
						+ result);

				return new String(result);
			}
		});
	}

	/**
	 * code to generate unique number from t_unique_key
	 * 
	 * @param group
	 *            group name for which unique id needs be generated
	 * @param table
	 *            a unique key generator table name
	 * 
	 * @param valueCol
	 * @param pKeyColumn
	 * 
	 * 
	 * @return a unique id for the passed group name
	 */
	@Override
	public Long generateID(final String group, final String table,
			final String valueCol, final String pKeyColumn) {
		return generateID(group, table, valueCol, pKeyColumn, DEFAULT_MAX_LO);
	}

	private Path getSelectionColumn(Root from,String selectedCol){
		if(!StringUtils.isEmpty(selectedCol)){
			if(selectedCol.contains(":")){
				String[] nestedAttribute = selectedCol.replace(':', ' ').split(" ");
				String[] newAttribute = nestedAttribute[1].replace('.', ' ').split(" ");


				Path path =from.get(newAttribute[0]);

				for (int i = 1; i < newAttribute.length; i++) {
					path = path.get(newAttribute[i]);

				}
				return path;

			}else if(selectedCol.contains(".")){
				String[] newAttribute = selectedCol.replace('.', ' ').split(" ");

				//				return from.join(newAttribute[0]).get(newAttribute[1]);
				Path path =from.join(newAttribute[0],JoinType.LEFT);
				//				Path path =from.get(newAttribute[0]);

				for (int i = 1; i < newAttribute.length; i++) {
					path = path.get(newAttribute[i]);
				}
				return path;
			}else{
				return from.get(selectedCol);
			}
		}
		return null;

	}



	/**
	 * code to generate unique number from t_unique_key
	 * 
	 * @param group
	 *            group name for which unique id needs be generated
	 * @param table
	 *            a unique key generator table name
	 * 
	 * @param valueCol
	 * @param pKeyColumn
	 * 
	 * 
	 * @return a unique id for the passed group name
	 */
	@Override
	public Long generateID(final String group, final String table,final String valueCol, final String pKeyColumn,final String maxlo) {
		return (Long) getJpaTemplate().execute(new JpaCallback() {
			public Object doInJpa(final EntityManager entityMgr)throws PersistenceException {

				ObjectNameNormalizer normalizer = new ObjectNameNormalizer() {
					@Override
					protected boolean isUseQuotedIdentifiersGlobally() {
						return false;
					}
					@Override
					protected NamingStrategy getNamingStrategy() {
						return new Configuration().getNamingStrategy();
					}
				};

				MultipleHiLoPerTableGenerator gen = new MultipleHiLoPerTableGenerator();
				Properties properties = new Properties();
				properties.put("table", table);
				properties.put("value_column", valueCol);
				properties.put("primary_key_column", pKeyColumn);
				properties.put("primary_key_value", group);
				properties.put("max_lo",maxlo);
				properties.put("hibernate.dialect",entityMgr.getEntityManagerFactory().getProperties().get("hibernate.dialect"));
				// "org.hibernate.dialect.MySQL5Dialect"
				properties.put("identifier_normalizer", normalizer);
				gen.configure(new BigIntegerType(), properties,Dialect.getDialect(properties));
				Serializable ss = gen.generate((SessionImpl) entityMgr.getDelegate(), new Object());
				return Long.parseLong(ss.toString());
			}
		});
	}

	@Override
	public void switchDatabaseContext() {
		//This method is to switch context using DBRouterAspect.

	}

	/**
	 * Remove an entity object from the persistent unit
	 * 
	 * @param entity
	 *            object to be removed.
	 * 
	 */
	public <T extends Serializable> void flushEntity() {
		/*
		 * System.out.println("=========== Remove Entity ===========");
		 * System.out.println("Enity : " + entity);
		 */

		getJpaTemplate().flush();
	}

//	public int executeNativeQueryMultiCondition(final String query,final List<Object> queryParams) {
//		return (int) getJpaTemplate().execute(new JpaCallback() {
//			public Object doInJpa(final EntityManager entityMgr)
//					throws PersistenceException {
//				// System.out.println("=========== findEntities ===========");
//				final Query queryJPQL = entityMgr.createNativeQuery(query);
//				if (queryParams != null && queryParams.size() > 0) {
//					Iterator itr = queryParams.iterator();
//					int paramCounter = 1;
//					while (itr.hasNext()) {
//						queryJPQL.setParameter(paramCounter, itr.next());
//						paramCounter++;
//					}
//				}
//				return queryJPQL.executeUpdate();
//			}
//		});
//	}


}

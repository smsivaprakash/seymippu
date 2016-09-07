package com.docstore.core.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.docstore.core.entity.TUsr;

@Repository("tUsrDAO")
public class TUsrDAOImpl implements TUsrDAO{
	
	@Autowired
	private GenericDAO genericDAO;
	
	private final Class<TUsr> clazz;
	
	public TUsrDAOImpl(){
		super();
		this.clazz = TUsr.class;
	}

	@Override
	public TUsr createTUsr(TUsr tUsr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteTUsr(Integer tUsrId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TUsr updateTUsr(TUsr tUsr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TUsr findTUsrById(Integer tUsrId) {
		System.out.println("find TUsr instance with usrId: " + tUsrId);
		return genericDAO.get(clazz, tUsrId);
	}

}

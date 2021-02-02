// Author: Foster Hangdaan
package com.fluffypony.data;

import ca.senecacollege.prg556.crba.dao.ClientDAO;
import ca.hazystate.data.DataSourceFactory;

public class ClientDAOFactory {
	
	public static ClientDAO getClientDAO() {
		return new ClientData(DataSourceFactory.getDataSource());
	}
}

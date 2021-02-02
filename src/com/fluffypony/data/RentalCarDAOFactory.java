// Author: Foster Hangdaan
package com.fluffypony.data;
import ca.senecacollege.prg556.crba.dao.RentalCarDAO;
import ca.hazystate.data.DataSourceFactory;

public class RentalCarDAOFactory
{
	public static RentalCarDAO getRentalCarDAO() 
	{
		return new RentalCarData(DataSourceFactory.getDataSource());
	}
}

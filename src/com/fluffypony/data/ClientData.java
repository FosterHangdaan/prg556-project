// Author: Foster hangdaan
package com.fluffypony.data;

import ca.senecacollege.prg556.crba.dao.ClientDAO;
import ca.senecacollege.prg556.crba.bean.Client;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import javax.sql.DataSource;

public class ClientData implements ClientDAO {
	
	private DataSource ds;
	
	ClientData (DataSource ds)
	{
		setDs(ds);
	}
	
	@Override
	public Client authenticateClient(String userName, String password) throws SQLException
	{
		String sqlStatement = "SELECT id, username, password FROM client WHERE username = ? AND password = ?";
		
		try(Connection conn = getDs().getConnection())
		{
			try(PreparedStatement pStatement = conn.prepareStatement(sqlStatement))
			{
				pStatement.setString(1, userName);
				pStatement.setString(2, password);
				try(ResultSet rslt = pStatement.executeQuery())
				{
					if(rslt.next())
						return getClient(rslt.getInt("id"));
					else
						return null;
				}
			}
		}
	}
	
	@Override
	public Client getClient(int id) throws SQLException
	{
		String sqlStatement = "SELECT id, first_name, last_name FROM client WHERE id = ?";
		
		try(Connection conn = getDs().getConnection())
		{
			try(PreparedStatement pStatement = conn.prepareStatement(sqlStatement))
			{
				pStatement.setInt(1, id);
				try(ResultSet rslt = pStatement.executeQuery())
				{
					if (rslt.next())
						return new Client(rslt.getInt("id"), rslt.getString("first_name"), rslt.getString("last_name"));
					else
						return null;
				}
			}
		}
	}
	
	private DataSource getDs()
	{
		return ds;
	}
	
	private void setDs(DataSource ds)
	{
		this.ds = ds;
	}
}

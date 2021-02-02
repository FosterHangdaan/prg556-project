// Author: Foster Hangdaan
package com.fluffypony.data;

import ca.senecacollege.prg556.crba.bean.RentalCar;
import ca.senecacollege.prg556.crba.dao.RentalCarDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.spamassassins.common.DateHelper;

public class RentalCarData implements RentalCarDAO
{
	private DataSource ds;
	
	RentalCarData(DataSource ds)
	{
		setDs(ds);
	}
	
	@Override
	public List<RentalCar> getAvailableCars(Date pickup, Date dropoff, String size) throws SQLException 
	{
		List<RentalCar> cars = new ArrayList<>();
		
		String sqlStatement = "SELECT carmake, MIN(plate) AS plate, carsize FROM rentalcar WHERE plate NOT IN (SELECT license_plate FROM rentalbooking WHERE ? < dropoff_date AND ? > pickup_date) AND carsize LIKE ? GROUP BY carmake, carsize ORDER BY carsize, carmake";
		
		try(Connection conn = getDs().getConnection())
		{
			try(PreparedStatement psmt = conn.prepareStatement(sqlStatement))
			{
				psmt.setTimestamp(1, DateHelper.dateToTimestamp(pickup));
				psmt.setTimestamp(2, DateHelper.dateToTimestamp(dropoff));
				if (size == null) psmt.setString(3, "%");
				else psmt.setString(3, size);

				try(ResultSet rslt = psmt.executeQuery())
				{
					while(rslt.next())
					{
						cars.add(new RentalCar(rslt.getString("carmake"), rslt.getString("plate"), rslt.getString("carsize")));
					}
				}
			}
		}
		return cars;
	}
	
	@Override
	public RentalCar getRentalCar(String plate) throws SQLException 
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement psmt = conn.prepareStatement("SELECT plate, carmake, carsize FROM rentalcar WHERE plate = ?"))
			{
				psmt.setString(1, plate);
				try (ResultSet rslt = psmt.executeQuery())
				{
					if (rslt.next())
						return new RentalCar(rslt.getString("carmake"), plate, rslt.getString("carsize"));
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

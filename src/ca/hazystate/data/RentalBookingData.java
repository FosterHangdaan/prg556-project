// Author: Mark Brierley
package ca.hazystate.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import javax.sql.DataSource;

import com.fluffypony.data.*;
import com.spamassassins.common.*;

import ca.senecacollege.prg556.crba.bean.*;
import ca.senecacollege.prg556.crba.dao.*;

public class RentalBookingData implements RentalBookingDAO
{
	
	// RentalCarDAO property
	private RentalCarDAO carData;
	
	private DataSource ds;
	
	public RentalBookingData(DataSource ds)
	{
		setDs(ds);
		carData = RentalCarDAOFactory.getRentalCarDAO();
	}
	
	public RentalBookingData() {
		carData = RentalCarDAOFactory.getRentalCarDAO();
	}

	@Override
	public RentalBooking bookRentalCar(int clientId, String plate, Date pickup, Date dropoff) throws SQLException
	{
		// Get data of car which matches the given plate.
		RentalCar rentalCar = carData.getRentalCar(plate);
		
		if (rentalCar != null) // rentalCar will be null if plate was not found (no match) 
		{
			// Extract make and size information from rental car object
			String make = rentalCar.getMake();
			String size = rentalCar.getSize();
			
			// Add new booking using the extracted information and push it onto the stack.
			
			String sqlStatement = "SELECT client_id, license_plate, pickup_date, dropoff_date FROM rentalbooking";
			
			try(Connection conn = getDs().getConnection())
			{
				try(Statement statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE))
				{
					try(ResultSet rslt = statement.executeQuery(sqlStatement))
					{
						rslt.moveToInsertRow();
						rslt.updateInt("client_id", clientId);
						rslt.updateString("license_plate", plate);
						rslt.updateTimestamp("pickup_date", DateHelper.dateToTimestamp(pickup));
						rslt.updateTimestamp("dropoff_date", DateHelper.dateToTimestamp(dropoff));
						rslt.insertRow();
					}
					
					try (ResultSet rsIdent = statement.executeQuery("SELECT @@IDENTITY"))
					{
						rsIdent.next();
						int bookingNumber = rsIdent.getInt(1);
						return new RentalBooking(bookingNumber, make, plate, size, pickup, dropoff);
					}
				}
			}			
		}
		return null;
	}

	@Override
	public void cancelRentalBooking(int bookingNo) throws SQLException 
	{
		try (Connection conn = getDs().getConnection())
		{
			try(PreparedStatement statement = conn.prepareStatement("SELECT book_num FROM rentalbooking WHERE book_num = ?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE))
			{
				statement.setInt(1, bookingNo);
				try (ResultSet rslt = statement.executeQuery())
				{
					if (rslt.next())
						rslt.deleteRow();
				}
			}
		}
	}

	@Override
	public RentalBooking getRentalBooking(int bookingNo) throws SQLException 
	{	
		String sqlQry = "SELECT book_num, carmake, license_plate, carsize, pickup_date, dropoff_date FROM rentalbooking INNER JOIN rentalcar ON rentalcar.plate = rentalbooking.license_plate WHERE book_num = ?";
		
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement(sqlQry))
			{
				statement.setInt(1, bookingNo);
				try (ResultSet rslt = statement.executeQuery())
				{
					if (rslt.next())
						return new RentalBooking(bookingNo, rslt.getString("carmake"), rslt.getString("license_plate"),rslt.getString("carsize"), rslt.getDate("pickup_date"), rslt.getDate("dropoff_date"));
					else
						return null;
				}
			}
		}
	}

	@Override
	public List<RentalBooking> getRentalBookings(int clientId) throws SQLException 
	{
		List<RentalBooking> bookings  = new ArrayList<>();
		String sql = "SELECT book_num, carmake, license_plate, carsize, pickup_date, dropoff_date FROM rentalbooking INNER JOIN rentalcar ON rentalcar.plate = rentalbooking.license_plate WHERE client_id = ?";
		
		try(Connection conn = getDs().getConnection())
		{
			try(PreparedStatement psmt = conn.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY))
			{
				psmt.setInt(1, clientId);
				try (ResultSet rslt = psmt.executeQuery())
				{
					while(rslt.next())
					{
						bookings.add(new RentalBooking(rslt.getInt("book_num"), rslt.getString("carmake"), rslt.getString("license_plate"), rslt.getString("carsize"), rslt.getDate("pickup_date"), rslt.getDate("dropoff_date")));
					}
				}
			}
		}
		return bookings;
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

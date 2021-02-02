// Author: Mark Brierley
package ca.hazystate;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ca.hazystate.data.RentalBookingDAOFactory;

import ca.senecacollege.prg556.crba.bean.Client;
import ca.senecacollege.prg556.crba.bean.RentalBooking;
import ca.senecacollege.prg556.crba.dao.ClientDAO;
import ca.senecacollege.prg556.crba.dao.RentalBookingDAO;

import java.util.List;

import com.fluffypony.data.ClientDAOFactory;


/**
 * Servlet Filter implementation class CRBAMenuFilter
 */

public class CRBAMenuFilter implements Filter 
{
	//private List<RentalBooking> bookingData;
	//private Client clientData;
	/**
     * Default constructor. 
     */
    public CRBAMenuFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		int length;
		RentalBookingDAO bookings = RentalBookingDAOFactory.getRentalBookingDAO();
		Client client = (Client) session.getAttribute("client");
		int id = client.getId();
		String firstName = client.getFirstName();
		String lastName = client.getLastName();
		
		if ( (firstName + lastName).length() > 16 )
		{
			firstName = firstName.split(" ")[0];
			lastName = lastName.charAt(0) + ".";
		}
		
		try
		{
			List<RentalBooking> bookingData = bookings.getRentalBookings(id);
			length = bookingData.size();
		}
		catch(Exception e)
		{
			length = 0;
		}
		
		request.setAttribute("firstName", firstName);
		request.setAttribute("lastName", lastName);
		request.setAttribute("numBookings", length);
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

// Author: Foster Hangdaan
package com.fluffypony;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.SQLException;

import ca.on.senecac.prg556.common.StringHelper;
import ca.senecacollege.prg556.crba.bean.Client;
import ca.senecacollege.prg556.crba.bean.RentalBooking;
import ca.senecacollege.prg556.crba.dao.RentalBookingDAO;
import ca.hazystate.data.*;

/**
 * Servlet Filter implementation class ShowBookingsFilter
 */
@WebFilter("/ShowBookingsFilter")
public class ShowBookingsFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ShowBookingsFilter() {
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpSession session = req.getSession();
		
		try
		{
			RentalBookingDAO rentalBookingDAO = RentalBookingDAOFactory.getRentalBookingDAO();
			
			if ("POST".equals(req.getMethod()) && StringHelper.isNotNullOrEmpty(req.getParameter("cancelBooking")))
			{
				Integer bookingNum = Integer.parseInt(request.getParameter("bookingNumber"));
				
				if (rentalBookingDAO.getRentalBooking(bookingNum) == null) throw new BadRequestException();
				else rentalBookingDAO.cancelRentalBooking(bookingNum);
			}
			
			Client client = (Client)session.getAttribute("client");
			req.setAttribute("bookingsList", rentalBookingDAO.getRentalBookings(client.getId()));
		}
		catch(NumberFormatException nfe)
		{
			throw new BadRequestException(nfe);
		}
		catch(SQLException sql)
		{
			throw new ServletException(sql);
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

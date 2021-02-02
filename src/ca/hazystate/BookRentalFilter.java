// Author: Mark Brierley
package ca.hazystate;
import ca.hazystate.data.RentalBookingDAOFactory;
import ca.on.senecac.prg556.common.StringHelper;
import ca.senecacollege.prg556.crba.bean.Client;
import ca.senecacollege.prg556.crba.bean.RentalCar;
import ca.senecacollege.prg556.crba.dao.RentalBookingDAO;
import ca.senecacollege.prg556.crba.dao.RentalCarDAO;

import com.fluffypony.BadRequestException;
import com.fluffypony.data.RentalCarDAOFactory;
import com.spamassassins.common.*;

import java.util.Date;
import java.util.List;
import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class BookRentalFilter
 */
public class BookRentalFilter implements Filter {

	//private List<RentalCar> carData;
	//private RentalCar desiredCar;
	
    /**
     * Default constructor. 
     */
    public BookRentalFilter() {
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
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		
		Client client = (Client) session.getAttribute("client");
		int id = client.getId();
		
		boolean pickupValid, dropoffValid, pickupBefore = true;
		Date pickup = null, dropoff = null;
		List<RentalCar> carData;
		//RentalCar desiredCar;
	
		try
		{
			if ("POST".equals(req.getMethod()))
			{
				RentalCarDAO car = RentalCarDAOFactory.getRentalCarDAO();
				RentalBookingDAO book = RentalBookingDAOFactory.getRentalBookingDAO();
				String button = request.getParameter("checkAvail");
				String strPick = request.getParameter("pickupDate");
				String strDrop = request.getParameter("dropoffDate");
				Date today = DateHelper.getCurrentDate();
				
				if (StringHelper.isNotNullOrEmpty(strPick))
				{
					pickup = DateHelper.parseDate(strPick);
					
					if (pickup != null && pickup.compareTo(today) >= 0)
					{						
						pickupValid = true;
						req.setAttribute("pickupDate", strPick);
					}
					else
						pickupValid = false;
				}
				else
					pickupValid = false;
				
				req.setAttribute("pickupValid", pickupValid);
				
				if (StringHelper.isNotNullOrEmpty(strDrop))
				{
					dropoff = DateHelper.parseDate(strDrop);
					
					if (dropoff.compareTo(today) >= 0)
					{						
						dropoffValid = true;
						req.setAttribute("dropoffDate", strDrop);
					}
					else
						dropoffValid = false;
				}
				else
					dropoffValid = false;
				req.setAttribute("dropoffValid", dropoffValid);
				
				if ("Check Availability".equals(button))
				{
					String size = req.getParameter("size");
					req.setAttribute("size", size);
					
					if (dropoffValid == true && pickupValid == true)
					{
						if (dropoff.compareTo(pickup) > 0)
						{
							pickupBefore = true;
							
							if ("A".equals(size))
								size = null;
							
							carData = car.getAvailableCars(pickup, dropoff, size);
							req.setAttribute("availableList", carData);
							req.setAttribute("numAvail", carData.size());
						}
						else
							pickupBefore = false;
					}	
					req.setAttribute("pickupBefore",pickupBefore);
				}
				else
				{
					try
					{
						if (pickupValid && dropoffValid)
						{
							String plate = req.getParameter("license");
							if (StringHelper.isNullOrEmpty(plate)) /* StringHelper is from the ca.on.senecac.prg556.common package */
								throw new BadRequestException("Not a valid license plate.");
							RentalCar desiredCar = car.getRentalCar(plate);
							if (null == desiredCar)
								throw new BadRequestException("Not a valid license plate.");
							book.bookRentalCar(id, plate, pickup, dropoff);
							res.sendRedirect(req.getContextPath() + "/");
							return;
						}
						else
						{
							throw new BadRequestException("Pickup or Drop off date is invalid.");
						}
					}
					catch(Exception e)
					{
						throw new BadRequestException();
					}
				}
			}
		}
		catch(Exception e)
		{
		throw new BadRequestException();
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

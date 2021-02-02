// Author: Mark Brierley
package ca.hazystate;

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

import ca.senecacollege.prg556.crba.bean.Client;

/**
 * Servlet Filter implementation class ApplicationAccessFilter
 */

public class ApplicationAccessFilter implements Filter {
	
    /**
     * Default constructor. 
     */
    public ApplicationAccessFilter() {
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
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		
		Client client = (Client) session.getAttribute("client");
		String requestPath = req.getRequestURI();
		String clientloginPath = req.getContextPath() + "/clientlogin.jspx";
		if(client == null && !requestPath.equals(clientloginPath))
			res.sendRedirect(clientloginPath);
		else
			chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}

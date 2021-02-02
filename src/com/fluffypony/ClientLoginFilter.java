// Author: Foster Hangdaan
package com.fluffypony;

import java.io.IOException;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebFilter;

import java.sql.SQLException;

import com.fluffypony.data.ClientData;
import com.fluffypony.data.ClientDAOFactory;

import ca.senecacollege.prg556.crba.bean.Client;
import ca.senecacollege.prg556.crba.dao.ClientDAO;
import ca.on.senecac.prg556.common.StringHelper;

/**
 * Servlet Filter implementation class ClientLoginFilter
 */
public class ClientLoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ClientLoginFilter() {
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
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession();

		try
		{
			Client client = (Client) session.getAttribute("client");
			if(client != null)
			{
				response.sendRedirect(request.getContextPath() + "/");
				return;
			}
			else if ("POST".equals(request.getMethod()))
			{
				String username = request.getParameter("username");
				String password = request.getParameter("password");
				if (StringHelper.isNotNullOrEmpty(username) && StringHelper.isNotNullOrEmpty(password))
				{
					ClientDAO dao = ClientDAOFactory.getClientDAO();
					client = dao.authenticateClient(username, password);
					if ( client != null )
					{
						session.setAttribute("client", client);
						response.sendRedirect(request.getContextPath() + "/");
						return;
					}
				}
				request.setAttribute("authorizationFail", Boolean.TRUE);
				request.setAttribute("username", StringHelper.xmlEscape(username));
			}
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

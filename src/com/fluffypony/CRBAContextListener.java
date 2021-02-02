// Author: Foster Hangdaan
package com.fluffypony;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.annotation.Resource;
import javax.sql.DataSource;

import ca.hazystate.data.DataSourceFactory;

/**
 * Application Lifecycle Listener implementation class CRBAContextListener
 *
 */
@WebListener
public class CRBAContextListener implements ServletContextListener
{
	@Resource(name = "CarRentalDS")
	private DataSource _ds;
	
    /**
     * Default constructor. 
     */
    public CRBAContextListener() { }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent context) {
        DataSourceFactory.setDataSource(_ds);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent context) {
        DataSourceFactory.setDataSource(null);
    }
	
}

package com.increff.pos.spring;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This class is a hook for <b>Servlet 3.0</b> specification, to initialize
 * Spring configuration without any <code>web.xml</code> configuration. Note
 * that {@link #getServletConfigClasses} method returns {@link com.increff.pos.spring.SpringConfig},
 * which is the starting point for Spring configuration <br>
 * <b>Note:</b> You can also implement the {@link WebApplicationInitializer }
 * interface for more control
 */

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { com.increff.pos.spring.SpringConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}
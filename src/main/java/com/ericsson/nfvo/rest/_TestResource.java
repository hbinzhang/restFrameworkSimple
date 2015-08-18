package com.ericsson.nfvo.rest;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ericsson.nfvo.rest.auth.Anonymous;

@ Singleton
@ Path( "test")
public class _TestResource {

	private static final Logger LOGGER = LoggerFactory.getLogger( _TestResource.class);

	public _TestResource() {
		LOGGER.trace( "creating a new {}", this.getClass());
	}

	@ GET
	@ Produces( MediaType.TEXT_HTML)
	@ Anonymous
	public String index() {
		System.out.println( "in resource request = " + this.request);
		StringBuilder html = new StringBuilder();
		html.append( "<form method=\"POST\" action=\"auth/login\">");
		html.append( "<input name=\"username\" />");
		html.append( "<input name=\"password\" />");
		html.append( "<input type=\"submit\" />");
		html.append( "</form>");
		return html.toString();
	}

	@ Context
	private HttpServletRequest request;
}

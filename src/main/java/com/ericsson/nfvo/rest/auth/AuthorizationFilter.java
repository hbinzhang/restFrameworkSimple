package com.ericsson.nfvo.rest.auth;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;

import java.io.IOException;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.nfvo.model.User;

@ Component
@ Singleton
@ Provider
public class AuthorizationFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = LoggerFactory.getLogger( AuthorizationFilter.class);

	public AuthorizationFilter() {
		LOGGER.trace( "created a {}: {}", this.getClass(), this);
	}

	/*
	 * A workaround for a Jersey-Spring3 bug.
	 * 
	 * When Spring decoration (for example, @Transactional) is used, Spring uses CGLib to enhance these classes. As a result, Jersey can no
	 * more find @Context fields and methods. We are using final methods to enforce Spring not to override it, but Spring will then refuse
	 * to set field value of the original class. As we are using singleton instances of them, it's safe to store the injected resources in
	 * a static field.
	 * 
	 * Note that if @Transaction (or anything alike) is not used, this is not needed. Just annotate plain old fields with @Context.
	 */
	private static final ResourceInfo[] requestInfo = new ResourceInfo[ 1];
	private static final HttpServletRequest[] httpServletRequest = new HttpServletRequest[ 1];

	@ Context
	public final void setResourceInfo( final ResourceInfo info) {
		AuthorizationFilter.requestInfo[ 0] = info;
	}

	@ Context
	public final void setHttpServletRequest( final HttpServletRequest request) {
		AuthorizationFilter.httpServletRequest[ 0] = request;
	}

	@ PersistenceContext
	private EntityManager entityManager;

	@ Override
	@ Transactional( propagation = Propagation.REQUIRED)
	public void filter( final ContainerRequestContext request) throws IOException {
		if( requestInfo[ 0].getResourceMethod().isAnnotationPresent( Anonymous.class)) return;
		HttpSession session = httpServletRequest[ 0].getSession( false);
		String username = session == null? null: ( String) session.getAttribute( "username");
		User user = username == null? null: this.entityManager.find( User.class, username);
		if( user != null && !user.isDisabled()) return;
		request.abortWith( Response.status( FORBIDDEN).build());
	}
}

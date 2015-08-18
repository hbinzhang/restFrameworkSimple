package com.ericsson.nfvo.rest.auth;

import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.nfvo.model.User;
import com.ericsson.nfvo.rest.util.Message;

/*
 * By default, Jersey instantiates resource classes in a per-request basis (the default and the only way standardized in JAX-RS).
 * However, when @Component is used, that is, when these resource classes are managed by Spring context, it becomes singleton. You cannot
 * use @Scope("request") to make it request-scoped, because the request life-cycle is not managed by Spring (i.e., the front-dispatcher
 * is not Spring). @Singleton annotation (which signals Jersey to use a singleton instance) is added here merely to remind this fact.
 */
@ Singleton
@ Path( "auth")
@ Component
@ Transactional( propagation = Propagation.REQUIRED)
public class AuthResource {

	private static final Logger LOGGER = LoggerFactory.getLogger( AuthResource.class);

	private static final int LOGIN_INTERVAL = 1000;

	/*
	 * The resource class is singleton. It is absolutely wrong to use a single EntityManager across different requests. However, since we
	 * are using Spring, it injects a proxy that routes to different Hibernate EntityManagers in different transactions.
	 */
	@ PersistenceContext
	private EntityManager entityManager;

	public AuthResource() {
		LOGGER.trace( "creating a new {}", this.getClass());
	}

	@ POST
	@ Path( "login")
	@ Consumes( MediaType.APPLICATION_FORM_URLENCODED)
	@ Produces( MediaType.APPLICATION_JSON)
	@ Anonymous
	public Response login( @ NotNull @ FormParam( "username") final String username,
			@ NotNull @ FormParam( "password") final String password, @ Context final HttpServletRequest request) {
		final HttpSession existingSession = request.getSession( false);
		if( existingSession != null) existingSession.invalidate();

		final long timestamp = System.currentTimeMillis();
		final User user = this.entityManager.find( User.class, username);
		if( user != null && user.getLastFailedLoginAttempt() < timestamp - LOGIN_INTERVAL && user.testPassword( password)) {
			final HttpSession newSession = request.getSession( true);
			newSession.setAttribute( "username", user.getUsername());
			return Response.ok().build();
		} else {
			if( user != null && user.getLastFailedLoginAttempt() < timestamp - LOGIN_INTERVAL) user.setLastFailedLoginAttempt();
			return Response.status( UNAUTHORIZED).entity( new Message( "wrongUsernamePasswordOrFrequentAttempt")).build();
		}
	}

	@ POST
	@ Path( "logout")
	@ Produces( MediaType.APPLICATION_JSON)
	public Message logout( @ Context final HttpServletRequest request) {
		HttpSession session = request.getSession( false);
		if( session != null) session.invalidate();
		return new Message( "logged out");
	}

	@ GET
	@ Path( "hello")
	public String hello() {
		return "hello";
	}

	@ GET
	@ Path( "hello2")
	@ Anonymous
	public String hello2() {
		return "hello2";
	}
}

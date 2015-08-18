package com.ericsson.nfvo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.nfvo.model.User;
import com.ericsson.nfvo.rest.auth.AuthResource;

public class ApplicationContextInitializer implements ApplicationListener< ContextRefreshedEvent> {

	private static final Logger LOGGER = LoggerFactory.getLogger( AuthResource.class);

	private static final String DEFAULT_ADMIN_USERNAME = "admin";
	private static final String DEFAULT_ADMIN_PASSWORD = "admin";

	@ PersistenceContext
	private EntityManager entityManager;

	@ Override
	@ Transactional( propagation = Propagation.REQUIRED)
	public void onApplicationEvent( final ContextRefreshedEvent event) {
		if( this.entityManager.find( User.class, DEFAULT_ADMIN_USERNAME) == null) {
			LOGGER.info( "provisioning default admin user: " + DEFAULT_ADMIN_USERNAME);
			this.entityManager.persist( new User( DEFAULT_ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD));
		}
	}
}

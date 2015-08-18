package com.ericsson.nfvo.rest;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.ericsson.nfvo.Nfvo;

public class Application extends ResourceConfig {

	public Application() {
		if( Nfvo.isDevelopmentProfile()) this.property( ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		this.register( MultiPartFeature.class);
		this.packages( _RestPackage.class.getPackage().getName());
	}
}

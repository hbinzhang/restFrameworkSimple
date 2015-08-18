package com.ericsson.nfvo;

import java.util.prefs.Preferences;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Nfvo {

	private static final Logger LOGGER = LoggerFactory.getLogger( Nfvo.class);

	private Nfvo() {
	}

	private static final Preferences USER_PREFS = Preferences.userNodeForPackage( Nfvo.class);
	private static final Preferences SYSTEM_PREFS = Preferences.systemNodeForPackage( Nfvo.class);
	private static final String PROFILE = System.getProperty( Nfvo.class.getPackage().getName() + ".profile", "").trim();
	private static final Preferences PROFILE_USER_PREFS = PROFILE.equals( "")? USER_PREFS: USER_PREFS.node( PROFILE);
	private static final Preferences PROFILE_SYSTEM_PREFS = PROFILE.equals( "")? SYSTEM_PREFS: SYSTEM_PREFS.node( PROFILE);
	static {
		LOGGER.info( "profile = " + PROFILE);
	}

	public static String getProfile() {
		return PROFILE;
	}

	public static boolean isDevelopmentProfile() {
		return Pattern.matches( "development(\\..*)?", PROFILE);
	}

	public static String getDatabaseUrl() {
		return Nfvo.getConfiguration( "database-url", isDevelopmentProfile()? "jdbc:hsqldb:mem:nfvo"
				: "jdbc:mysql://nfvo:nfvo@localhost/nfvo");
	}

	public static String getConfiguration( final String key, final String defaultValue) {
		return PROFILE_USER_PREFS.get( key, USER_PREFS.get( key, PROFILE_SYSTEM_PREFS.get( key, SYSTEM_PREFS.get( key, defaultValue))));
	}
}

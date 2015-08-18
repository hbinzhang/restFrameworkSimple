package com.ericsson.nfvo.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.DatatypeConverter;

@ Entity
public class User {

	private static final String PASSWORD_DIGEST_ALGORITHM = "SHA-1"; // once went online, never change it
	private static final Charset PASSWORD_CHARSET = StandardCharsets.UTF_8; // once went online, never change it
	private static final byte[] PASSWORD_SALT = "NFVO#ERICSSON".getBytes( PASSWORD_CHARSET); // once went online, never change it

	private static final String PASSWORD_DIGEST( String password) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance( PASSWORD_DIGEST_ALGORITHM);
			algorithm.update( PASSWORD_SALT);
			return DatatypeConverter.printHexBinary( algorithm.digest( password.getBytes( PASSWORD_CHARSET))).toUpperCase();
		} catch( NoSuchAlgorithmException exception) {
			// MD5, SHA-1, and SHA-256 are required to be supported in every JRE, as long as we use one of them, this won't happen.
			throw new AssertionError( "message digest algorithm " + PASSWORD_DIGEST_ALGORITHM + " is not available");
		}
	}

	@ Id
	private String username;
	@ Size( min = 40, max = 40)
	private String password;
	private boolean disabled = false;
	private long lastFailedLoginAttempt = 0;

	@ NotNull
	private String nickname;

	protected User() {
	}

	public User( String username, String password) {
		this( username, password, null);
	}

	public User( String username, String password, String nickname) {
		this.username = username;
		this.setPassword( password);
		this.nickname = nickname != null? nickname: username;
	}

	public String getUsername() {
		return this.username;
	}

	/**
	 * Sets password. Only a digest of the password is stored. So there is no <code>getPassword()</code> method. If the password is set to
	 * <code>null</code>, {@link #testPassword(String)} never returns <code>true</code>, even with <code>null</code> parameter.
	 * 
	 * @param password
	 */
	public void setPassword( String password) {
		this.password = password == null? null: PASSWORD_DIGEST( password);
	}

	/**
	 * Tests if the stored password is the given string. They are compared by their digests, so false positive is possible, although
	 * negligible in practice. Note that if {@link #setPassword(String)} was given <code>null</code>, this method always returns
	 * <code>false</code>.
	 * 
	 * @param password
	 * @return
	 */
	public boolean testPassword( String password) {
		return this.password != null && this.password.equals( PASSWORD_DIGEST( password));
	}

	public void enable() {
		this.disabled = false;
	}

	public void disable() {
		this.disabled = true;
	}

	public boolean isDisabled() {
		return this.disabled;
	}

	public long getLastFailedLoginAttempt() {
		return this.lastFailedLoginAttempt;
	}

	public void setLastFailedLoginAttempt() {
		this.lastFailedLoginAttempt = System.currentTimeMillis();
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname( String nickname) {
		this.nickname = nickname;
	}

	@ Override
	public String toString() {
		return "{user " + this.username + "}";
	}
}

package com.ericsson.nfvo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@ Entity
public class Vim {

	@ Id
	@ GeneratedValue
	private long id;

	private String name;

	public void setName( String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}

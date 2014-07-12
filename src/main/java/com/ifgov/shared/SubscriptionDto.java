package com.ifgov.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SubscriptionDto implements IsSerializable {
	String name;
	String when;
	Double lat;
	Double lon;
	String then;
	String at;

	public String getName() {
		return name;
	}

	public void setName(String username) {
		this.name = username;
	}

	public String getWhen() {
		return when;
	}

	public void setWhen(String when) {
		this.when = when;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public String getThen() {
		return then;
	}

	public void setThen(String then) {
		this.then = then;
	}

	public String getAt() {
		return at;
	}

	public void setAt(String at) {
		this.at = at;
	}

}

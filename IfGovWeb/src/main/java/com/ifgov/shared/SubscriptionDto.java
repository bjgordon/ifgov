package com.ifgov.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class SubscriptionDto implements IsSerializable {
	Integer id;
	String name;
	Double lat;
	Double lon;
	Integer sourceid;
	Integer notification;
	String notificationsettings;
	Integer condition;
	String conditionvalue;
	Date lastupdate;
	String lastvalue;
	Date currentupdate;
	String currentvalue;
	Date lastnotified;
	Boolean welcomed;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getSourceid() {
		return sourceid;
	}

	public void setSourceid(Integer sourceid) {
		this.sourceid = sourceid;
	}

	public Integer getNotification() {
		return notification;
	}

	public void setNotification(Integer notification) {
		this.notification = notification;
	}

	public String getNotificationsettings() {
		return notificationsettings;
	}

	public void setNotificationsettings(String notificationsettings) {
		this.notificationsettings = notificationsettings;
	}

	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	public String getConditionvalue() {
		return conditionvalue;
	}

	public void setConditionvalue(String conditionvalue) {
		this.conditionvalue = conditionvalue;
	}

	public Date getLastupdate() {
		return lastupdate;
	}

	public void setLastupdate(Date lastupdate) {
		this.lastupdate = lastupdate;
	}

	public String getLastvalue() {
		return lastvalue;
	}

	public void setLastvalue(String lastvalue) {
		this.lastvalue = lastvalue;
	}

	public Date getCurrentupdate() {
		return currentupdate;
	}

	public void setCurrentupdate(Date currentupdate) {
		this.currentupdate = currentupdate;
	}

	public String getCurrentvalue() {
		return currentvalue;
	}

	public void setCurrentvalue(String currentvalue) {
		this.currentvalue = currentvalue;
	}

	public Date getLastnotified() {
		return lastnotified;
	}

	public void setLastnotified(Date lastnotified) {
		this.lastnotified = lastnotified;
	}

	public Boolean getWelcomed() {
		return welcomed;
	}

	public void setWelcomed(Boolean welcomed) {
		this.welcomed = welcomed;
	}

	@Override
	public String toString() {
		return "SubscriptionDto [id=" + id + ", name=" + name + ", lat=" + lat
				+ ", lon=" + lon + ", sourceid=" + sourceid + ", notification="
				+ notification + ", notificationsettings="
				+ notificationsettings + ", condition=" + condition
				+ ", conditionvalue=" + conditionvalue + ", lastupdate="
				+ lastupdate + ", lastvalue=" + lastvalue + ", currentupdate="
				+ currentupdate + ", currentvalue=" + currentvalue
				+ ", lastnotified=" + lastnotified + ", welcomed=" + welcomed
				+ "]";
	}

}

package com.ifgov.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DataDto implements IsSerializable {

	String broadband;

	public String getBroadband() {
		return broadband;
	}

	public void setBroadband(String broadband) {
		this.broadband = broadband;
	}

	@Override
	public String toString() {
		return "DataDto [broadband=" + broadband + "]";
	}

}

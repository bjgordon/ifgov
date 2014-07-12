package com.ifgov.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ifgov.client.IfGovService;
import com.ifgov.shared.SubscriptionDto;

@SuppressWarnings("serial")
public class IfGovServiceImpl extends RemoteServiceServlet implements
		IfGovService {

	protected final static Logger logger = LoggerFactory
			.getLogger(IfGovServiceImpl.class);

	@Override
	public void subscribe(SubscriptionDto subscriptionDto) {
		logger.warn("subscribe todo " + subscriptionDto);
	}

	@Override
	public String getData(Double lat, Double lon) {
		logger.warn("getData todo " + lat + "," + lon);
		return null;
	}

}

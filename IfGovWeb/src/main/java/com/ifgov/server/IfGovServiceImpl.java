package com.ifgov.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ifgov.client.IfGovService;
import com.ifgov.server.dao.TheDao;
import com.ifgov.shared.DataDto;
import com.ifgov.shared.SubscriptionDto;

@SuppressWarnings("serial")
public class IfGovServiceImpl extends RemoteServiceServlet implements
		IfGovService {

	protected final static Logger logger = LoggerFactory
			.getLogger(IfGovServiceImpl.class);

	@Override
	public void subscribe(SubscriptionDto subscriptionDto) {
		logger.info("subscribing: " + subscriptionDto);

		TheDao dao = new TheDao();

		long id = dao.createSubscription(subscriptionDto);
		logger.debug("subscribed id = " + id);

	}

	@Override
	public DataDto getData(Double lat, Double lon) {

		DataDto dataDto = new DataDto();

		try {
			dataDto.setBroadband(DataRequester.getBroadband(lat, lon));
		} catch (Exception e) {
			logger.error("Error getting broadband data: " + e.getMessage(), e);
			// todo
			dataDto.setBroadband("{\"error\",\"" + e.getMessage() + "\"}");
		}

		logger.info("Data: " + dataDto);
		// logger.warn("getData returning test data " + lat + "," + lon);
		//
		// TheDao dao = new TheDao();
		// SubscriptionDto subscriptionDto = dao.readSubscriptionById(7);
		// dataDto.setBroadband(subscriptionDto.getLastvalue());
		// logger.debug("data: " + dataDto);
		return dataDto;
	}
}

package com.ifgov.server.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.ifgov.shared.SubscriptionDto;

public class TheDao {

	static SqlSessionFactory sf = SqlSessionFactorySingleton.instance();

	public TheDao() {

	}

	public List<SubscriptionDto> readAllSubscriptions() {
		try (SqlSession session = sf.openSession()) {
			return session.selectList("mappers.readAllSubscriptions");
		}
	}

	public SubscriptionDto readSubscriptionById(int id) {
		try (SqlSession session = sf.openSession()) {
			return session.selectOne("mappers.readSubscriptionById", id);
		}
	}

	public long createSubscription(SubscriptionDto subscriptionDto) {
		try (SqlSession session = sf.openSession()) {
			session.insert("mappers.createSubscription", subscriptionDto);
			session.commit();
			return subscriptionDto.getId();
		}
	}

}

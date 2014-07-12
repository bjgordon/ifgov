package com.ifgov.server.dao;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlSessionFactorySingleton {

	protected final static Logger logger = LoggerFactory
			.getLogger(SqlSessionFactorySingleton.class);

	protected static SqlSessionFactory sqlSessionFactory = null;

	static {
		try {
			String resource = "mybatis-config.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(inputStream);
			logger.info("Initialised SqlSessionFactorySingleton from "
					+ resource);
		} catch (IOException e) {
			sqlSessionFactory = null;
			logger.error("Problem during init: " + e.getMessage(), e);
		}
	}

	public static SqlSessionFactory instance() {
		if (sqlSessionFactory == null) {
			throw new RuntimeException(
					"SqlSessionFactory singleton not initialised!");
		}
		return sqlSessionFactory;
	}

}

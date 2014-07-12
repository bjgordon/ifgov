package com.ifgov.server.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.ifgov.shared.UsersDto;

public class UsersDao {

	static SqlSessionFactory sf = SqlSessionFactorySingleton.instance();

	public UsersDao() {

	}

	public List<UsersDto> readAllUsersDtos() {
		try (SqlSession session = sf.openSession()) {
			return session.selectList("mappers.readAllUsersDto");
		}
	}

	public UsersDto readUsersDto(int id) {
		try (SqlSession session = sf.openSession()) {
			return session.selectOne("mappers.readUsersDto");
		}
	}

	public UsersDto readUsersDtoByName(String name) {
		try (SqlSession session = sf.openSession()) {
			return session.selectOne("mappers.readUsersDtoByName", name);
		}
	}

	public long createUsersDto(UsersDto usersDto) {
		try (SqlSession session = sf.openSession()) {
			int status = session.insert("mappers.createUsersDto", usersDto);
			session.commit();
			return usersDto.getId();
		}
	}

}

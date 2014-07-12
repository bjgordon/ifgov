import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ifgov.server.dao.UsersDao;
import com.ifgov.shared.UsersDto;

public class DatabaseTest {
	@Test
	public void testdatabase() throws Exception {
		// make sure the db works

		UsersDao dao = new UsersDao();

		// List<UsersDto> dtos = dao.readAllUsersDtos();
		// assertTrue(dtos.size() > 0);

		UsersDto dto = dao.readUsersDtoByName("testname");
		assertEquals("testname", dto.getName());

	}

	@Test
	public void createUser() throws Exception {
		UsersDao dao = new UsersDao();
		UsersDto usersDto = new UsersDto();
		usersDto.setName("unittest");
		usersDto.setLat(1.0);
		usersDto.setLon(2.0);
		long result = dao.createUsersDto(usersDto);
		assertTrue(result > 0);
		System.out.println("res =" + result);
	}
}

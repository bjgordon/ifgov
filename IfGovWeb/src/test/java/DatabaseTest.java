import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.ifgov.server.dao.TheDao;
import com.ifgov.shared.SubscriptionDto;

public class DatabaseTest {
	@Test
	public void testdatabase() throws Exception {
		// make sure the db works

		TheDao dao = new TheDao();

		List<SubscriptionDto> dtos = dao.readAllSubscriptions();
		assertTrue(dtos.size() > 0);

		SubscriptionDto dto = dao.readSubscriptionById(7);

		assertEquals("test", dto.getName());
	}

	@Test
	public void createUser() throws Exception {
		TheDao dao = new TheDao();
		SubscriptionDto subscriptionDto = new SubscriptionDto();
		subscriptionDto.setName("unittest");
		subscriptionDto.setLat(1.0);
		subscriptionDto.setLon(2.0);
		subscriptionDto.setSourceid(1);
		subscriptionDto.setCurrentvalue("{testcurrentvalue}");
		subscriptionDto.setNotification(1);
		subscriptionDto.setNotificationsettings("unittest@test.com");
		long result = dao.createSubscription(subscriptionDto);
		assertTrue(result > 0);
		System.out.println("res =" + result);
	}
}

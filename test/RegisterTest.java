import models.Register;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.fest.assertions.Assertions.assertThat;

@ContextConfiguration(classes={TestAppConfig.class, TestDatabaseConfig.class})
public class RegisterTest {

	@Test
	public void setRegisterPhoneNumber() {
		Register user = new Register();
		user.setPhoneNumber("+4065550123");
		assertThat(user.getPhoneNumber()).isEqualTo("+4065550123");
	}
}

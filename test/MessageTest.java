import models.Message;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.fest.assertions.Assertions.assertThat;

@ContextConfiguration(classes={TestAppConfig.class, TestDatabaseConfig.class})
public class MessageTest {

	@Test
	public void setMessageContents() {
		Message message = new Message();
		message.setContents("This is a test message");
		assertThat(message.getContents()).isEqualTo("This is a test message");
	}
}

//import static org.fest.assertions.Assertions.assertThat;
//import static play.test.Helpers.fakeApplication;
//import static play.test.Helpers.running;
//
//import models.Message;
//
//import org.junit.Test;
//
//public class TaskTest {
//
//	@Test
//	public void create() {
//		running(fakeApplication(), new Runnable() {
//			public void run() {
//				Message message = new Message();
//				message.contents = "Write a test";
//				message.save();
//				assertThat(message.id).isNotNull();
//			}
//		});
//	}
//
//}

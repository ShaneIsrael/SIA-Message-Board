import models.Message;
import models.Register;

import services.MessageService;

import org.junit.runner.Computer;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;
import static play.test.Helpers.*;

import static org.fest.assertions.Assertions.assertThat;

@ContextConfiguration(classes={TestAppConfig.class, TestDatabaseConfig.class})
public class BaseMessageTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private MessageService msgService;

    @Test
    public void testMessage() {
    	running(fakeApplication(), new Runnable() {
    	      public void run() {
    	          Message message = new Message();
    	          message.setContents("This is a test message");
    	          msgService.addMessage(message);
    	          assertThat(message.getId()).isNotNull();
    	          List<Message> messages = msgService.getMessages();
    	          assertThat(messages.size()).isEqualTo(1);
    	       }
    	   });
    }

    @Test
    public void testRegister() {
    	running(fakeApplication(), new Runnable() {
  	      public void run() {
  	    	Register register = new Register();
  	    	register.setPhoneNumber("+4065550123");
  	    	msgService.registerNumber(register);
  	    	assertThat(register.getId()).isNotNull();
  	    	List<Register> users = msgService.getRegisteredNumbers();
  	    	assertThat(users.size()).isEqualTo(1);
  	       }
  	   });
    }

}
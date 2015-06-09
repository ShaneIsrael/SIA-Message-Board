package services;
import models.Message;
import models.Register;

import services.MessageService;

import configs.AppConfigTest;
import configs.DataConfigTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.assertThat;

@ContextConfiguration(classes={AppConfigTest.class, DataConfigTest.class})
public class MessageServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private MessageService msgService;


    @Test
    public void testMessage() {
		Message message = new Message();
		message.setContents("This is a test message");
		msgService.addMessage(message);
		assertThat(message.getId()).isNotNull();
		List<Message> messages = msgService.getMessages();
		assertThat(messages.size()).isEqualTo(1);
    }

    @Test
    public void testRegister() {
		Register register = new Register();
		register.setPhoneNumber("4065550123");
		msgService.registerNumber(register);
		assertThat(register.getId()).isNotNull();
		List<Register> users = msgService.getRegisteredNumbers();
		assertThat(users.size()).isEqualTo(1);
    }

    @Test
    public void testGetMessage() {
		Message message = new Message();
		message.setContents("This is a test message");
		msgService.addMessage(message);
		List<Message> messages = msgService.getMessages();
		assertThat(messages.size()).isGreaterThan(0);
		assertThat(messages.get(0).getContents()).isEqualTo("This is a test message");
    }

    @Test
    public void testGetRegisteredNumbers() {
    	Register register = new Register();
        register.setPhoneNumber("4065550123");
        msgService.registerNumber(register);
        List<Register> users = msgService.getRegisteredNumbers();
        assertThat(users.size()).isGreaterThan(0);
        assertThat(users.get(0).getPhoneNumber()).isEqualTo("4065550123");
    }

    @Test
    public void testGetLastMessage() {
		Message message = new Message();
		message.setContents("This is a test message");
		msgService.addMessage(message);
        Message lastMessage = msgService.getLastMessage();
        assertThat(lastMessage).isNotNull();
        assertThat(lastMessage.getContents()).isEqualTo("This is a test message");
    }
}

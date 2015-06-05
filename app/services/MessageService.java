package services;

import models.Message;
import models.Register;

import java.util.List;

public interface MessageService {
	void addMessage(Message msg);
	boolean registerNumber(Register rgstr);
	boolean unregisterNumber(Register rgstr);
	List<Message> getMessages();
	List<Register> getRegisteredNumbers();
	Message getLastMessage();
}

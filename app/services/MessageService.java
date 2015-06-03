package services;

import models.Message;
import models.Register;

import java.util.List;

public interface MessageService {

		public void addMessage(Message msg);
		public boolean registerNumber(Register rgstr);
		public boolean unregisterNumber(Register rgstr);
		public List<Message> getMessages();
		public List<Register> getRegisteredNumbers();
		public Message getLastMessage();
}

package services;

import models.Message;
import java.util.List;

public interface MessageService {

		public void addMessage(Message msg);
		public List<Message> getMessages();
}

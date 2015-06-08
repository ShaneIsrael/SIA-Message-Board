import models.Message;
import models.Register;

import java.util.List;

public interface MessageServiceTest {
	void test();
	void addMessage(Message msg);
	boolean registerNumber(Register rgstr);
	boolean unregisterNumber(Register rgstr);
	List<Message> getMessages();
	List<Register> getRegisteredNumbers();
	Message getLastMessage();
}

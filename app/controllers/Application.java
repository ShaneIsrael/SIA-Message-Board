package controllers;

import models.Message;
import models.Register;
import services.MessageService;
import views.html.index;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Controller
public class Application extends play.mvc.Controller {

	private static final String ACCOUNT_SID = Play.application()
			.configuration().getString("account.sid");
	private static final String AUTH_TOKEN = Play.application().configuration()
			.getString("auth.token");

	private TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID,
			AUTH_TOKEN);

	private static final Logger logger = LoggerFactory
			.getLogger(Application.class);

	private final String SMS_DEFAULT_NUMBER = Play.application().configuration().getString("sms.default.number");

	@Autowired
	private MessageService msgService;

	public Result index() {
		String smsNumber = SMS_DEFAULT_NUMBER;
		return ok(index.render("Text " + smsNumber + " to add a message!",
				play.data.Form.form(Message.class)));
	}

	public Result addMessage() {
		Form<Message> form = Form.form(Message.class).bindFromRequest();
		if (form.hasErrors()) {
			logger.debug("Bad Request: Failed to add message to the board.");
			return badRequest(index.render("Failed to add message", form));
		}
		Message message = form.get();
		message.setContents("["
				+ new SimpleDateFormat("HH:mm").format(new Date()) + "] "
				+ message.getContents());

		if (message.getContents().length() > 255) {
			message.setContents(message.getContents().substring(0, 255));
		}
		msgService.addMessage(message);
		notifyRegistered(null);
		return redirect(controllers.routes.Application.index());
	}

	public Result getMessages() {
		List<Message> messages = msgService.getMessages();
		return ok(Json.toJson(messages));
	}

	public Result getRegisteredNumbers() {
		List<Register> registered = msgService.getRegisteredNumbers();
		return ok(Json.toJson(registered));
	}

	public String getLastMessage() {
		Message message = msgService.getLastMessage();
		return message.getContents();
	}

	public Result twilioMessages() {
		DynamicForm form = Form.form().bindFromRequest();
		if (form.data().size() != 0) {
			// If not a command, add the message to the board.
			if (!parseCommands(form)) {
				String body = form.get("Body");
				String contents = "["
						+ new SimpleDateFormat("HH:mm").format(new Date())
						+ "] " + body;
				Message message = new Message();
				message.setContents(contents);
				msgService.addMessage(message);
			}
			notifyRegistered(form.get("From"));
		}
		return ok();
	}

	private void notifyRegistered(String ignore) {
		logger.info("Notifying Registered Users");
		Account account = client.getAccount();
		String messageBody = getLastMessage();
		List<Register> numbers = msgService.getRegisteredNumbers();
		for (Register rgstr : numbers) {
			// don't send the senders reply back to himself
			if (ignore == null || !rgstr.getPhoneNumber().equals(ignore)) {
				try {
					MessageFactory messageFactory = account.getMessageFactory();
					List<NameValuePair> params = new ArrayList<>();
					params.add(new BasicNameValuePair("To", rgstr
							.getPhoneNumber()));
					params.add(new BasicNameValuePair("From",SMS_DEFAULT_NUMBER));
					params.add(new BasicNameValuePair("Body", messageBody));
					messageFactory.create(params);
				} catch (TwilioRestException e) {
					logger.debug(
							"Failed to send message to registered user!", e);
				}
			}
		}

	}

	private boolean parseCommands(DynamicForm in) {
		String body = in.get("Body");
		String number = in.get("From");
		//if ("!register".equalsIgnoreCase(body)) {
		if (body.toLowerCase().equals("!register")) {
			logger.info("Command Received: !register");
			Register register = new Register();
			register.setPhoneNumber(number);
			String reply;
			if (msgService.registerNumber(register)) {
				reply = "Registered Successfully! New messages will be sent to your phone :)";
			} else {
				logger.warn("User {} has already registered!", register.getPhoneNumber());
				reply = "You are already registered!";
			}

			sendReply(number, reply);

			return true;
		} else if (body.toLowerCase().equals("!unregister")) {
			Register user = new Register();
			user.setPhoneNumber(number);
			if (msgService.unregisterNumber(user)) {
				logger.info("Command Received: !unregister");
				sendReply(number,
						"You have been unregistered, you will no longer receive messages.");
			} else {
				logger.warn("User {} could not be unregistered, are they in the database?", user.getPhoneNumber());
			}
			return true;
		}
		return false;
	}

	private void sendReply(String number, String msg) {
		String[] colors = { "red", "green", "blue", "cyan", "pink", "purple",
				"yellow", "gray", "magenta" };

		int begin = msg.indexOf("#");
		int end = msg.indexOf(":", msg.indexOf(" "));
		for (String color : colors) {
			if (msg.contains(color + ":")) {
				msg.replace(color + ":", "");
			}
		}
		if(begin > -1 && end > -1) {
			String hexColor = msg.substring(begin, end);
			msg.replace(hexColor, "");
		}


		try {
			Account account = client.getAccount();
			MessageFactory messageFactory = account.getMessageFactory();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("To", number));
			params.add(new BasicNameValuePair("From", SMS_DEFAULT_NUMBER));
			params.add(new BasicNameValuePair("Body", msg));
			messageFactory.create(params);
		} catch (TwilioRestException e) {
			logger.debug(
					"TwilioRestException: Failed to send message to user!", e);
		}
	}

}

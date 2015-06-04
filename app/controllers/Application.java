package controllers;

import models.Register;

import services.MessageService;

import play.Play;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import play.libs.Json;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.List;

import views.html.index;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import com.twilio.sdk.verbs.*;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@org.springframework.stereotype.Controller
public class Application {

	public static final String ACCOUNT_SID = Play.application().configuration()
			.getString("account.sid");
	public static final String AUTH_TOKEN = Play.application().configuration()
			.getString("auth.token");

	static {
		System.setProperty("logback.configurationFile", "/app/conf/logger.xml");
	}

	static Logger Logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private MessageService msgService;

	public Result index() {
		String smsNumber = Play.application().configuration()
				.getString("sms.default");
		return play.mvc.Controller.ok(index.render("Text " + smsNumber
				+ " to add a message!",
				play.data.Form.form(models.Message.class)));
	}

	public Result addMessage() {
		Logger.info("Message added to board [Web-Interface]");
		Form<models.Message> form = Form.form(models.Message.class)
				.bindFromRequest();
		if (form.hasErrors()) {
			Logger.info("Form has an error");
			return play.mvc.Controller.badRequest(index.render(
					"You Failed! Noob!", form));
		} else {
			models.Message message = form.get();
			message.contents = "["
					+ new SimpleDateFormat("HH:mm").format(new Date()) + "] "
					+ message.contents;
			msgService.addMessage(message);
			notifyRegistered(null);
			return play.mvc.Controller.redirect(controllers.routes.Application
					.index());
		}
	}

	public Result getMessages() {

		List<models.Message> messages = msgService.getMessages();

		return play.mvc.Controller.ok(Json.toJson(messages));
	}

	public Result getRegisteredNumbers() {
		List<models.Register> registered = msgService.getRegisteredNumbers();
		return play.mvc.Controller.ok(Json.toJson(registered));
	}

	public String getLastMessage() {
		models.Message message = msgService.getLastMessage();
		return message.contents;
	}

	public Result twilioMessages() {
		Logger.info("Message added to board [SMS-Interface]");
		DynamicForm form = Form.form().bindFromRequest();
		if (form.data().size() != 0) {
			String body = form.get("Body");
			// If not a command, add the message ot the board.
			if (!parseCommands(form)) {
				String contents = "["
						+ new SimpleDateFormat("HH:mm").format(new Date())
						+ "] " + body;
				models.Message message = new models.Message();
				message.contents = contents;
				msgService.addMessage(message);
			}
		}

		notifyRegistered(form.get("From"));

		return play.mvc.Controller.ok();
	}

	private void notifyRegistered(String ignore) {
		Logger.info("Notifying Registered Users");
		TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);

		Account account = client.getAccount();

		String messageBody = getLastMessage();
		List<models.Register> numbers = msgService.getRegisteredNumbers();
		for (models.Register rgstr : numbers) {
			// don't send the senders reply back to himself
			if (null == ignore || !rgstr.phoneNumber.equals(ignore)) {
				try {
					MessageFactory messageFactory = account.getMessageFactory();
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("To", rgstr.phoneNumber));
					params.add(new BasicNameValuePair("From", Play
							.application().configuration()
							.getString("sms.default")));
					params.add(new BasicNameValuePair("Body", messageBody));
					Message sms = messageFactory.create(params);
				} catch (TwilioRestException tre) {
					Logger.debug("TwilioRestException: Failed to send message to registered user!");
				}
			}
		}

	}

	private boolean parseCommands(DynamicForm in) {
		String body = in.get("Body");
		String number = in.get("From");
		if (body.toLowerCase().equals("!register")) {
			Logger.info("Received !register Command");
			models.Register register = new models.Register();
			register.phoneNumber = number;
			String reply;
			if (msgService.registerNumber(register)) {
				reply = "Registered Successfully! New messages will be sent to your phone :)";
			} else {
				reply = "You are already registered!";
			}

			Logger.info("Sending register success reply!");
			sendReply(number, reply);

			return true;
		} else if (body.toLowerCase().equals("!unregister")) {
			models.Register user = new models.Register();
			user.phoneNumber = number;
			if (msgService.unregisterNumber(user)) {
				Logger.info("A user has unregistered.");
				sendReply(number,
						"You have been unregistered, you will no longer receive messages.");
			} else {
				Logger.debug("A user could not be unregistered, are they in the database?");
			}
			return true;
		}
		return false;
	}

	private void sendReply(String number, String msg) {
		try {
			TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID,
					AUTH_TOKEN);

			Account account = client.getAccount();
			MessageFactory messageFactory = account.getMessageFactory();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("To", number));
			params.add(new BasicNameValuePair("From", Play.application()
					.configuration().getString("sms.default")));
			params.add(new BasicNameValuePair("Body", msg));
			Message sms = messageFactory.create(params);
		} catch (TwilioRestException tre) {
			tre.printStackTrace();
			Logger.debug("Failed to send message to user!");
		}
	}

}

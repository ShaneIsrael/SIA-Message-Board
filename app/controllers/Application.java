package controllers;

import models.Message;
import services.MessageService;
import views.html.index;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
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

    @Autowired
    private MessageService msgService;

    public Result index() {
        String smsNumber = Play.application().configuration()
                .getString("sms.default.number");
        return ok(index.render("Text " + smsNumber + " to add a message!",
                play.data.Form.form(models.Message.class)));
    }

    public Result addMessage() {
        Form<models.Message> form = Form.form(models.Message.class).bindFromRequest();
        if (form.hasErrors()) {
        	logger.info("Bad Request: Failed to add message to the board.");
        	return badRequest(index.render(
                    "Failed to add message", form));
        }
        models.Message message = form.get();
        message.setContents("["
        + new SimpleDateFormat("HH:mm").format(new Date()) + "] "
                + message.getContents());

        if (message.getContents().length() > 255) {
            message.setContents(message.getContents().substring(0, 255));
        }
        msgService.addMessage(message);
        notifyRegistered(null);
        return redirect(controllers.routes.Application
                .index());
    }

    public Result getMessages() {
        List<models.Message> messages = msgService.getMessages();
        return ok(Json.toJson(messages));
    }

    public Result getRegisteredNumbers() {
        List<models.Register> registered = msgService.getRegisteredNumbers();
        return ok(Json.toJson(registered));
    }

    public String getLastMessage() {
        models.Message message = msgService.getLastMessage();
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
                models.Message message = new models.Message();
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
        List<models.Register> numbers = msgService.getRegisteredNumbers();
        for (models.Register rgstr : numbers) {
            // don't send the senders reply back to himself
            if (ignore == null || !rgstr.getPhoneNumber().equals(ignore)) {
                try {
                    MessageFactory messageFactory = account.getMessageFactory();
                    List<NameValuePair> params = new ArrayList<>();
                    params.add(new BasicNameValuePair("To", rgstr
                            .getPhoneNumber()));
                    params.add(new BasicNameValuePair("From", Play
                            .application().configuration()
                            .getString("sms.default")));
                    params.add(new BasicNameValuePair("Body", messageBody));
                    messageFactory.create(params);
                } catch (TwilioRestException e) {
                    logger.debug(
                            "TwilioRestException: Failed to send message to registered user!",
                            e);
                }
            }
        }

    }

    private boolean parseCommands(DynamicForm in) {
        String body = in.get("Body");
        String number = in.get("From");
        if (body.toLowerCase().equals("!register")) {
            logger.info("Command Received: !register");
            models.Register register = new models.Register();
            register.setPhoneNumber(number);
            String reply;
            if (msgService.registerNumber(register)) {
                reply = "Registered Successfully! New messages will be sent to your phone :)";
            } else {
                logger.info("User has already registered!");
                reply = "You are already registered!";
            }

            sendReply(number, reply);

            return true;
        } else if (body.toLowerCase().equals("!unregister")) {
            models.Register user = new models.Register();
            user.setPhoneNumber(number);
            if (msgService.unregisterNumber(user)) {
                logger.info("Command Received: !unregister");
                sendReply(number,
                        "You have been unregistered, you will no longer receive messages.");
            } else {
                logger.debug("A user could not be unregistered, are they in the database?");
            }
            return true;
        }
        return false;
    }

    private void sendReply(String number, String msg) {

    	int begin = msg.indexOf("#");
    	int end = msg.indexOf(":", msg.indexOf(" "));
    	String hexColor = msg.substring(begin, end);

    	String[] colors = {"red", "green", "blue", "cyan", "pink", "purple", "yellow", "gray", "magenta"};
    	for (String color : colors) {
    		if (msg.contains(color+":")) {
    			msg.replace(color+":", "");
    		}
    	}
    	msg.replace(hexColor, "");

        try {
            Account account = client.getAccount();
            MessageFactory messageFactory = account.getMessageFactory();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("To", number));
            params.add(new BasicNameValuePair("From", Play.application()
                    .configuration().getString("sms.default")));
            params.add(new BasicNameValuePair("Body", msg));
            messageFactory.create(params);
        } catch (TwilioRestException e) {
            logger.debug("TwilioRestException: Failed to send message to user!", e);
        }
    }

}

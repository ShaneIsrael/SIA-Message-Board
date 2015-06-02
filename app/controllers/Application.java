package controllers;

import services.MessageService;

import play.Play;
import play.data.DynamicForm;
import play.mvc.Controller;
import play.mvc.Result;
import play.data.Form;
import play.libs.Json;

import java.util.Date;
import java.util.UUID;
import java.text.SimpleDateFormat;
import java.util.List;

import views.html.index;

import org.springframework.beans.factory.annotation.Autowired;
import com.twilio.sdk.verbs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@org.springframework.stereotype.Controller
public class Application {

	static { System.setProperty("logback.configurationFile", "/app/conf/logger.xml");}
	static Logger Logger = LoggerFactory.getLogger(Application.class);

	@Autowired
	private MessageService msgService;

    public Result index() {
    	String smsNumber = Play.application().configuration().getString("sms.default");
        return play.mvc.Controller.ok(index.render("Text "+smsNumber+" to add a message!", play.data.Form.form(models.Message.class)));
    }

    public Result addMessage() {
    	Logger.info("Message added to board [Web-Interface]");
    	Form<models.Message> form = Form.form(models.Message.class).bindFromRequest();
    	if (form.hasErrors()) {
    		return play.mvc.Controller.badRequest(index.render("You Failed! Noob!", form));
    	}
    	else {
    		models.Message message = form.get();
    		message.contents = "["+ new SimpleDateFormat("HH:mm").format(new Date()) + "] " + message.contents;
    		msgService.addMessage(message);
    		return play.mvc.Controller.redirect(controllers.routes.Application.index());
    	}
    }

    public Result getMessages() {
    	List<models.Message> messages = msgService.getMessages();
//    	if (messages.size() > 15) {
//    		while(messages.size() > 15)
//    			messages.remove(0);
//    	}

    	return play.mvc.Controller.ok(Json.toJson(messages));
    }

    public Result twilioMessages()
    {
    	Logger.info("Message added to board [SMS-Interface]");
    	DynamicForm form = Form.form().bindFromRequest();
    	if(form.data().size() != 0) {
    		String body = form.get("Body");
    		String contents = "[" + new SimpleDateFormat("HH:mm").format(new Date()) + "] " + body;
    		models.Message message = new models.Message();
    		message.contents = contents;
    		msgService.addMessage(message);
    	}
//    	TwiMLResponse twiml = new TwiMLResponse();
//    	Message message = new Message("Thanks for sending me an SMS!");
//
//    	try {
//    		twiml.append(message);
//    	}
//    	catch (Exception e) {
//    		//Logger.error("An error occurred trying to append the message verb");
//    	}
    	return play.mvc.Controller.ok();
    }
}

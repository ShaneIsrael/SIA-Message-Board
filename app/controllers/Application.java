package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("hello, world", play.data.Form.form(models.Task.class)));
    }

    public static Result addTask() {
    	play.data.Form<models.Task> form = play.data.Form.form(models.Task.class).bindFromRequest();
    	if (form.hasErrors()) {
    		return badRequest(index.render("You Failed! Noob!", form));
    	}
    	else {
    		models.Task task = form.get();
    		task.save();
    		return redirect(routes.Application.index());
    	}
    }

    public static Result getTasks() {
    	List<models.Task> tasks = new play.db.ebean.Model.Finder<>(String.class,  models.Task.class).all();
    	return ok(play.libs.Json.toJson(tasks));
    }
}

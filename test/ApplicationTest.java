//import org.junit.*;
//
//import play.Play;
//import play.mvc.*;
//import play.twirl.api.Content;
//
//import static play.test.Helpers.*;
//import static org.fest.assertions.Assertions.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//import controllers.Application;
//
///**
//*
//* Simple (JUnit) tests that can call all parts of a play app.
//* If you are interested in mocking a whole application, see the wiki for more details.
//*
//*/
//@Transactional
//public class ApplicationTest extends BaseTestCase {
//
//	@Autowired
//	private Application appController;
//
//    @Test
//    public void simpleCheck() {
//        int a = 1 + 1;
//        assertThat(a).isEqualTo(2);
//    }
//
//    @Test
//    public void renderTemplate() {
//    	Content html = views.html.index.render("Text "+Play.application().configuration().getString("sms.default")+" to add a message!", play.data.Form.form(models.Message.class));
//        assertThat(contentType(html)).isEqualTo("text/html");
//        assertThat(contentAsString(html)).contains("Text "+Play.application().configuration().getString("sms.default")+" to add a message!");
//    }
//
//    @Test
//    public void testIndex() {
//
//    	Result result = appController.index();
//    	assertThat(status(result)).isEqualTo(OK);
//    	assertThat(contentType(result)).isEqualTo("text/html");
//    	assertThat(charset(result)).isEqualTo("utf-8");
//    	assertThat(contentAsString(result)).contains("Text "+Play.application().configuration().getString("sms.default")+" to add a message!");
//    }
//
//
//}

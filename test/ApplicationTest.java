//import org.junit.*;
//
//import play.mvc.*;
//import play.twirl.api.Content;
//
//import static play.test.Helpers.*;
//import static org.fest.assertions.Assertions.*;
//
//
///**
//*
//* Simple (JUnit) tests that can call all parts of a play app.
//* If you are interested in mocking a whole application, see the wiki for more details.
//*
//*/
//public class ApplicationTest {
//
//    @Test
//    public void simpleCheck() {
//        int a = 1 + 1;
//        assertThat(a).isEqualTo(2);
//    }
//
//    @Test
//    public void renderTemplate() {
//    	Content html = views.html.index.render("HELLO, WORLD", play.data.Form.form(models.Message.class));
//        assertThat(contentType(html)).isEqualTo("text/html");
//        assertThat(contentAsString(html)).contains("HELLO, WORLD");
//    }
//
//    @Test
//    public void testIndex() {
//    	Result result = controllers.Application.index();
//    	assertThat(status(result)).isEqualTo(OK);
//    	assertThat(contentType(result)).isEqualTo("text/html");
//    	assertThat(charset(result)).isEqualTo("utf-8");
//    	assertThat(contentAsString(result)).contains("HELLO, WORLD");
//    }
//
//
//}

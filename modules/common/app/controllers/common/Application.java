package controllers.common;


import play.mvc.Controller;
import play.mvc.Result;



public class Application extends Controller {

    public static Result index() {
        return ok(views.html.index.render("Your new application is ready from Common"));
    }

}

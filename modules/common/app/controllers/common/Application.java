package controllers.common;


import play.mvc.*;
import views.html.*;

import play.libs.F;
import play.libs.F.Function;
import play.libs.F.Promise;
import com.typesafe.plugin.*;



@With(CatchAction.class)
public class Application extends Controller {

    public static Result preflight(String all) {
        response().setHeader("Access-Control-Allow-Origin", "*");
        response().setHeader("Allow", "*");
        response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        response().setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Referer, User-Agent, X-AUTH-TOKEN");
        return ok();
    }


    public static Result index() {
        return ok(index.render("New Manger Server is ready."));
    }

    public static Promise<Result> emailSender() {
        Promise<Boolean> emailResult = Promise.promise(
                new F.Function0<Boolean>() {
                    @Override
                    public Boolean apply() throws Throwable {
                        try {
                            MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
                            mail.setSubject("mailer");
                            //mail.setRecipient("sunbei914914@msn.com","minjun@augbase.com","yanminjun99@126.com","min.lin@augbase.com","zhuang@augbase.com");
                            mail.setRecipient("sunbei914914@msn.com");

                            mail.setFrom("Augbase <noreply@augbase.com>");

                            mail.sendHtml("<html><body><h1>Hello, <h1><p>Welcome to our system.</p><p>Thank you</p></body></html>");

                            return true;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }
        );

        return emailResult.map(
                new Function<Boolean, Result>() {
                    @Override
                    public Result apply(Boolean sent) throws Throwable {
                        if (sent) {
                            return ok("Email sent!");
                        } else {
                            return ok("Email wast not sent!");
                        }
                    }
                }
        );
    }

}




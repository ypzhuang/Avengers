package controllers.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.common.SysUser;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.With;
import utils.common.Constants;
import views.html.login;

/**
 * Created by ypzhuang on 15/9/21.
 */
@With(CatchAction.class)
public class SecurityController extends Controller {

    public static class Login {
        @Constraints.Required
        @Constraints.Email
        public String email;

        @Constraints.Required
        public String password;
    }


    public static Result login() {
        return ok(login.render(Form.form(Login.class)));
    }



    public static Result authenticate() {
        Form<Login> loginForm = Form.form(Login.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            return badRequest(loginForm.errorsAsJson());
        }

        Login login = loginForm.get();

        SysUser user = SysUser.findByEmailAndPassword(login.email, login.password);

        ObjectNode authTokenJson = Json.newObject();
        if (user == null) {
            authTokenJson.put(Constants.AUTH_ERROR_KEY,Constants.WRONG_USER_OR_PASSWORD_ERROR);
            return unauthorized(authTokenJson);
        } else {
            String authToken = user.createToken();
            authTokenJson.put(Constants.AUTH_TOKEN, authToken);
            //authTokenJson.put(Constants.ROLE_KEY,user.role.toString());
            return ok(authTokenJson);
        }
    }



    @SecuredAnnotation({"Guest","Editor","Reviewer","Super"})
    public static Result role() {
        final SysUser user = getUser();
        ObjectNode authTokenJson = Json.newObject();
        if (user == null) {
            authTokenJson.put(Constants.AUTH_ERROR_KEY,Constants.WRONG_EMAIL);
            return unauthorized(authTokenJson);
        } else {
            authTokenJson.put(Constants.ROLE_KEY,user.role.toString());
            return ok(authTokenJson);
        }
    }

    private static  SysUser getUser() {
        String email = Http.Context.current().request().username();
        return SysUser.findByEmail(email);
    }

}

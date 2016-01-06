package controllers.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.common.Role;
import models.common.SysUser;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.common.Constants;

/**
 * Created by ypzhuang on 15/12/28.
 */
@With(CatchAction.class)
public class SysUsers extends Controller {

    @SecuredAnnotation({"Super"})
    public static Result listAll() {
       return ok(Json.toJson(SysUser.find.all()));
    }

    @SecuredAnnotation({"Super"})
    public static Result open(Long id) {
        if (check(id,"open")){
            return ok(Json.toJson(SysUser.find.all()));
        } else {
            return badRequest();
        }

    }

    @SecuredAnnotation({"Super"})
    public static Result close(Long id) {
        if (check(id,"close")){
            return ok(Json.toJson(SysUser.find.all()));
        } else {
            return badRequest();
        }
    }

    private static final Form<SysUserForm> sysUserFormForm = Form.form(SysUserForm.class);

    @SecuredAnnotation({"Super"})
    public static Result addSysUser() {
        ObjectNode jsonResult = Json.newObject();
        Form<SysUserForm> boundForm = sysUserFormForm.bindFromRequest();
        SysUserForm sysUserForm = boundForm.get();
        if(boundForm.hasErrors()) {
            return badRequest(boundForm.errorsAsJson());
        }

        SysUser existedUser = SysUser.findByEmail(sysUserForm.email);
        if (existedUser != null ) {
            jsonResult.put("email","email has existed.");
            return badRequest(Json.toJson(jsonResult));
        }

        existedUser = SysUser.findByPhone(sysUserForm.phone);
        if (existedUser != null ) {
            jsonResult.put("phone","phone has existed.");
            return badRequest(Json.toJson(jsonResult));
        }

        SysUser sysUser = new SysUser(sysUserForm.email, sysUserForm.password);
        sysUser.phone = sysUserForm.phone;
        Role role = Role.valueOf(sysUserForm.role);
        sysUser.role = role;

        sysUser.save();

        return ok();
    }

    @SecuredAnnotation({"Super"})
    @BodyParser.Of(BodyParser.Json.class)
    public static Result changePassword(Long id) {
        ObjectNode jsonResult = Json.newObject();

        JsonNode jsonData = request().body().asJson();

        SysUser sysUser = SysUser.find.byId(id);
        if (sysUser == null) {
            jsonResult.put("id","illegal id");
            return badRequest(Json.toJson(jsonResult));
        }
        String password = jsonData.get("password").textValue();
        Logger.debug("password:{}",password);

        sysUser.setPassword(password);
        sysUser.authToken = null;
        sysUser.save();
        return ok();
    }

    private static boolean check(Long id,String operation){
        SysUser sysUser = SysUser.find.byId(id);
        if (sysUser == null) {
            return false;
        }
        if ("open".equals(operation) && !sysUser.isDeleted || "close".equals(operation) && sysUser.isDeleted) {
            return false;
        }
        sysUser.isDeleted = !sysUser.isDeleted;
        sysUser.authToken = null;
        sysUser.update();
        return true;
    }


    public static class SysUserForm {

        @Constraints.Required
        @Constraints.Email
        public String email;

        @Constraints.Required
        public String password;

        @Constraints.Required
        public String phone;

        @Constraints.Required
        public String role;

    }

}

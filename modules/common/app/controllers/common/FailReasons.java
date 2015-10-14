package controllers.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.common.FailReason;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * Created by ypzhuang on 15/9/24.
 */
@With(CatchAction.class)
public class FailReasons extends Controller {


    @SecuredAnnotation({"Reviewer","Super"})
    public static Result list() {
        List<FailReason> failReasons = FailReason.findAll();
        return ok(Json.toJson(failReasons));
    }


    @SecuredAnnotation({"Super"})
    public static Result delete(Long id) {
        ObjectNode json = Json.newObject();

        FailReason failReason = FailReason.findById(id);
        if (failReason == null) {
            //illegal argument<id> value
            json.put("error","no fail reason with value of id: " + id);
            return notFound(json);
        } else  if (failReason.isDeleted) {
            //already deleted
            json.put("error","already deleted the fail reason : " + id);
            return forbidden(json);
        } else {
            failReason.isDeleted = true;
            failReason.deleteFailReason();
            json.put("success", "successfully deleted fail reason with id: " + id);
            return ok(Json.toJson(json));
        }
    }

    @SecuredAnnotation({"Super"})
    public static Result update(Long id) {
        ObjectNode json = Json.newObject();

        FailReason failReason = FailReason.findById(id);

        Form<FailReasonForm> failReasonForm = Form.form(FailReasonForm.class).bindFromRequest();

        if (failReasonForm.hasErrors()) {
            return badRequest(failReasonForm.errorsAsJson());
        }

        FailReasonForm form = failReasonForm.get();
        String content = form.content;
        String description = form.description;
        String suggestion = form.suggestion;

        boolean flag = false;
        if (content != null) {
            failReason.content = content;
            flag = true;
        }

        if (description != null) {
            failReason.description = description;
            flag = true;
        }

        if (suggestion != null) {
            failReason.suggestion = suggestion;
            flag = true;
        }

        if (failReason.isDeleted) {
            json.put("error", "the fail reason with id " + id + " has been deleted.You can not update it.") ;
            return forbidden(json);
        } else if (flag){
            failReason.updateFailReason();
            return ok(Json.toJson(failReason));
        } else {
            json.put("error","you must modify one of the properties(content,description,suggestion)");
            return badRequest(Json.toJson(json));
        }
    }


    @SecuredAnnotation({"Super"})
    public static Result create() {
        ObjectNode json = Json.newObject();

        Form<FailReasonForm> failReasonForm = Form.form(FailReasonForm.class).bindFromRequest();

        if (failReasonForm.hasErrors()) {
            return badRequest(failReasonForm.errorsAsJson());
        }

        FailReasonForm form = failReasonForm.get();
        String content = form.content;
        String description = form.description;
        String suggestion = form.suggestion;


        if (content == null) { //must
            json.put("error","property of content is required.");
            return badRequest(Json.toJson(json));
        }


        FailReason failReason = new FailReason();
        failReason.content = content;
        failReason.description = description;
        failReason.suggestion = suggestion;
        failReason.isDeleted = false;
        failReason.createTime = new Date();
        failReason.save();

        return ok(Json.toJson(failReason));


    }

    public static class FailReasonForm {
        @Constraints.MaxLength(500)
        public String description;

        @Constraints.MaxLength(2000)
        public String content;

        @Constraints.MaxLength(1000)
        public String suggestion;
    }
}

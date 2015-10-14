package controllers.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.common.Hospital;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.mvc.BodyParser;
import play.mvc.With;
import utils.common.Constants;
import models.common.Ltr;
import models.common.LtrStatus;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

@With(CatchAction.class)
public class Ltrs extends Controller {



    @SecuredAnnotation({"Editor","Reviewer","Super"}) // TODO add some constraints
    public static Result findById(Long id){
        final Ltr ltr  = Ltr.find.byId(id);
        if (ltr == null) {
            return notFound(String.format("Ltr(%d) does not exist.", id));
        }
        return ok(Json.toJson(ltr));
    }

    @SecuredAnnotation({"Super"})
    @BodyParser.Of(BodyParser.Json.class)
    public static Result list(String filter,int page,int pageSize){
        JsonNode json = request().body().asJson();
        Logger.debug("isArray:{}", json.findPath("status").getClass());

        if (json.get("status") != null ) {

            if (json.get("status").isArray()) {
                ArrayNode data=(ArrayNode)json.get("status");
                List<LtrStatus> statuses=new ArrayList<LtrStatus>();
                for (JsonNode dataNode : data) {
                    statuses.add(LtrStatus.valueOf(dataNode.asText()));
                }
                Logger.debug("status is :{}",statuses);
            }

        }



        List<JsonNode> statusList = json.findValues("status");

        for (JsonNode tmp : statusList) {
            String text = tmp.asText();
            Logger.debug("text:{}",text);
            //LtrStatus status = LtrStatus.valueOf(tmp.asText());
            //Logger.debug("parsed LTR Status is :{}",status);
        }

        List<Ltr> ltrs = Ltr.search(filter, page, pageSize);
        return ok(Json.toJson(ltrs));
    }

    @SecuredAnnotation({"Super"})
    public static Result count(String filter,int page,int pageSize){
        int count = Ltr.count(filter, page, pageSize);
        Map<String,Integer> map = new HashMap();
        map.put(Constants.JSON_KEY_FOR_COUNT,count);
        return ok(Json.toJson(map));
    }


    @SecuredAnnotation({"Editor"})
    public static Result list4edit(String filter,int page,int pageSize){
        List<Ltr> ltrs = Ltr.search(filter, page, pageSize, LtrStatus.ToEdit, LtrStatus.UserRejected, LtrStatus.Rejected);
        return ok(Json.toJson(ltrs));
    }

    @SecuredAnnotation({"Editor"})
    public static Result count4edit(String filter,int page,int pageSize){
        int count = Ltr.count(filter,page,pageSize,LtrStatus.ToEdit, LtrStatus.UserRejected, LtrStatus.Rejected);
        Map<String,Integer> map = new HashMap();
        map.put(Constants.JSON_KEY_FOR_COUNT,count);
        return ok(Json.toJson(map));
    }


    @SecuredAnnotation({"Reviewer"})
    public static Result list4review(String filter,int page,int pageSize){
        List<Ltr> ltrs = Ltr.search(filter,page,pageSize,LtrStatus.ToReview);
        return ok(Json.toJson(ltrs));
    }

    @SecuredAnnotation({"Reviewer"})
    public static Result count4review(String filter,int page,int pageSize){
        int count = Ltr.count(filter,page,pageSize,LtrStatus.ToReview);
        Map<String,Integer> map = new HashMap();
        map.put(Constants.JSON_KEY_FOR_COUNT,count);
        return ok(Json.toJson(map));
    }




    @SecuredAnnotation({"Editor","Reviewer","Super"})
    public static Result update(Long id){
        ObjectNode json = Json.newObject();
        final Ltr ltr  = Ltr.find.byId(id);
        if (ltr == null) {
            return notFound(String.format("Ltr(%d) does not exist.", id));
        }

        Form<LtrForm> ltrFormForm = Form.form(LtrForm.class).bindFromRequest();

        if (ltrFormForm.hasErrors()) {
            return badRequest(ltrFormForm.errorsAsJson());
        }

        LtrForm form = ltrFormForm.get();
        Long hospitalId = form.hospitalId;
        String hospitalName = form.hospitalName;
        Date testTime = form.testtime;

        Hospital hospital = Hospital.find.byId(hospitalId);


        if (hospital == null ) {
            json.put("error","invalid hospital id");
            return  badRequest(Json.toJson(json));
        } else if (!hospital.name.equals(hospitalName)){
            json.put("error","invalid hospital name");
            return  badRequest(Json.toJson(json));
        } else {
            ltr.testtime = testTime;
            ltr.hospital = hospital.name;
            ltr.hid = hospitalId;
            ltr.update();
        }


        return ok(Json.toJson(ltr));
    }


    public static class LtrForm {
        @Constraints.Required
        public Long hospitalId;

        @Constraints.Required
        public String hospitalName;

        @Constraints.Required
        public Date testtime;
    }

}

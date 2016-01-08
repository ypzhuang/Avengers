package controllers.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.common.Doctor;
import models.common.FailReason;
import models.common.Ltr;
import models.common.LtrStatus;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.common.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ypzhuang on 16/1/6.
 */

@With(CatchAction.class)
public class Doctors extends Controller {

    @SecuredAnnotation({"Super"})
    public static Result list(String filter,int page,int pageSize){
        List<Doctor> doctors = Doctor.search(filter, page, pageSize);
        return ok(Json.toJson(doctors));
    }

    @SecuredAnnotation({"Super"})
    public static Result count(String filter,int page,int pageSize){
        int count = Doctor.count(filter, page, pageSize);
        Map<String,Integer> map = new HashMap();
        map.put(Constants.JSON_KEY_FOR_COUNT,count);
        return ok(Json.toJson(map));
    }

}

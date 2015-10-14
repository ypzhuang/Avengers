package controllers.common;


import models.common.Hospital;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.common.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ypzhuang on 15/9/17.
 */

@SecuredAnnotation({"Editor","Super"})
public class Hospitals extends Controller {

    @SecuredAnnotation({"Editor","Reviewer","Super"})
    public static Result list(String filter,int page,int pageSize){
        List<Hospital> hospitals = Hospital.search(filter, page, pageSize);
        return ok(Json.toJson(hospitals));
    }


    @SecuredAnnotation({"Editor","Reviewer","Super"})
    public static Result count(String filter,int page,int pageSize){
        int count = Hospital.count(filter,page,pageSize);
        Map<String,Integer> map = new HashMap();
        map.put(Constants.JSON_KEY_FOR_COUNT,count);
        return ok(Json.toJson(map));
    }
}

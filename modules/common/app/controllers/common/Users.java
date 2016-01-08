package controllers.common;

import models.common.User;
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
public class Users extends Controller {

    @SecuredAnnotation({"Super"})
    public static Result list(String filter,int page,int pageSize){
        List<User> users = User.search(filter, page, pageSize);
        return ok(Json.toJson(users));
    }

    @SecuredAnnotation({"Super"})
    public static Result count(String filter,int page,int pageSize){
        int count = User.count(filter, page, pageSize);
        Map<String,Integer> map = new HashMap();
        map.put(Constants.JSON_KEY_FOR_COUNT,count);
        return ok(Json.toJson(map));
    }
}

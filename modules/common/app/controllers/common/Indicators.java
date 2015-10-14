package controllers.common;


import models.common.Ltr;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import models.common.Indicator;
import play.mvc.With;
import utils.common.Constants;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ypzhuang on 15/9/16.
 */
@With(CatchAction.class)
public class Indicators extends Controller {
    @SecuredAnnotation({"Editor","Reviewer","Super"})
    public static Result filterByVal(String name){
        List<Indicator> indicators = Indicator.filterBy(name);
        return ok(Json.toJson(indicators));
    }

    @SecuredAnnotation({"Editor","Reviewer","Super"})
    public static Result findById(Long id){
        Indicator indicator= Indicator.findById(id);
        if (indicator == null) {
            return notFound(String.format("Indicator(%d) does not exist.", id));
        }
        return ok(Json.toJson(indicator));
    }


    @SecuredAnnotation({"Editor","Reviewer","Super"})
    public static Result list(String filter,int category, int page,int pageSize){
        List<Indicator> indicators = Indicator.search(filter, category, page, pageSize);
        return ok(Json.toJson(indicators));
    }

    @SecuredAnnotation({"Editor","Reviewer","Super"})
    public static Result count(String filter,int category, int page,int pageSize){
        int count = Indicator.count(filter,category, page, pageSize);
        Map<String,Integer> map = new HashMap();
        map.put(Constants.JSON_KEY_FOR_COUNT,count);
        return ok(Json.toJson(map));
    }



}

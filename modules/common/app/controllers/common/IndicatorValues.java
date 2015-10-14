package controllers.common;

import models.common.IndicatorValue;
import models.common.Ltr;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by ypzhuang on 15/9/16.
 */
public class IndicatorValues extends Controller {

    @SecuredAnnotation({"Editor","Reviewer","Super"}) // TODO add some constraints
    public static Result listByLtrId(Long ltrId){
        List<IndicatorValue> indicatorValues = IndicatorValue.findAllByLtrId(ltrId);
        return ok(Json.toJson(indicatorValues));
    }

}

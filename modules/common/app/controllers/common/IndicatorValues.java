package controllers.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import models.common.*;
import org.springframework.beans.BeanUtils;
import play.Logger;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.*;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by ypzhuang on 15/9/16.
 */
@With(CatchAction.class)
public class IndicatorValues extends Controller {

    @SecuredAnnotation({"Editor","Reviewer","Super"}) // TODO add some constraints
    public static Result listByLtrId(Long ltrId){
        List<IndicatorValue> indicatorValues = IndicatorValue.findAllByLtrId(ltrId);
        return ok(Json.toJson(indicatorValues));
    }


    @SecuredAnnotation({"Editor","Super"})
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public static Result saveIndicatorValues(Long ltrId){
        ObjectNode jsonResult = Json.newObject();
        JsonNode jsonData = request().body().asJson();
        Logger.debug("jsonData:{}",jsonData);
        if (jsonData == null) {
            jsonResult.put("error","json body error");
            return badRequest(Json.toJson(jsonResult));
        }

        Ltr ltr = Ltr.find.byId(ltrId);
        boolean isEdited = ltr.status.equals(LtrStatus.ToEdit) || ltr.status.equals(LtrStatus.UserRejected)  ||
        ltr.status.equals(LtrStatus.Rejected);
        if (ltr == null || !isEdited) {
            jsonResult.put("error","illegal status of ltr");
            return badRequest(Json.toJson(jsonResult));
        }

        String json = jsonData.toString();
        Logger.debug("ltrId:{}, with indicator value data {}",ltrId,json);

        try{
            //List<IndicatorValue> lists = new ObjectMapper().readValue(json, new TypeReference<List<IndicatorValue>>(){});
            List<IndicatorValue> lists = new ObjectMapper().readValue(json,
                    TypeFactory.defaultInstance().constructCollectionType(List.class,IndicatorValue.class));
            Logger.debug("values:{}",lists);
            if (lists == null) {
                jsonResult.put("error","illegal json body");
                return badRequest(Json.toJson(jsonResult));
            }

            IndicatorValue.deleteAllByLtrId(ltrId);
            for(IndicatorValue iv : lists) {
                Indicator indicator = Indicator.find.byId(iv.indicator.id);
                iv.category = indicator.category;
                iv.uid = ltr.uid;
                iv.isDelete = 0;
                iv.testtime = ltr.testtime;
                iv.ltr = ltr;
                iv.save();
                Logger.debug("saved {}",iv);
            }


        }catch(Exception e){
            e.printStackTrace();
            Logger.error("error"+e.getMessage());
            return badRequest();
        }
        return ok();
    }

    @SecuredAnnotation({"Editor","Super"})
    @BodyParser.Of(BodyParser.Json.class)
    @Transactional
    public static Result commitIndicatorValues(Long ltrId){
        Result result = saveIndicatorValues(ltrId);
        if (result instanceof SimpleResult) {
            SimpleResult status = (SimpleResult)result;
            int actual =  status.getWrappedSimpleResult().header().status();
            int expected = Results.ok().getWrappedSimpleResult().header().status();
            if (actual == expected) {
                Ltr ltr = Ltr.find.byId(ltrId);
                ltr.status = LtrStatus.ToReview;
                ltr.save();
                return ok();

            } else {
                return result;
            }

        } else {
            return internalServerError();
        }
    }



}

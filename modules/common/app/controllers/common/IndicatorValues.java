package controllers.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import models.common.Indicator;
import models.common.IndicatorValue;
import models.common.Ltr;
import play.Logger;
import play.db.ebean.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.BodyParser;

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
        JsonNode jsonData = request().body().asJson();
        Logger.debug("jsonData:{}",jsonData);

        String json = jsonData.toString();
        Logger.debug("ltrId:{}, with indicator value data {}",ltrId,json);

        try{
            //List<IndicatorValue> lists = new ObjectMapper().readValue(json, new TypeReference<List<IndicatorValue>>(){});
            List<IndicatorValue> lists = new ObjectMapper().readValue(json,
                    TypeFactory.defaultInstance().constructCollectionType(List.class,IndicatorValue.class));
            Logger.debug("values:{}",lists);

            IndicatorValue.deleteAllByLtrId(ltrId);
            for(IndicatorValue iv : lists) {
                Indicator indicator = Indicator.find.byId(iv.indicator.id);
                Ltr ltr = Ltr.find.byId(ltrId);
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




}

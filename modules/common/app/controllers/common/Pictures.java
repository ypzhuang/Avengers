package controllers.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.common.Ltr;
import models.common.Picture;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

/**
 * Created by ypzhuang on 15/12/2.
 */
@With(CatchAction.class)
public class Pictures extends Controller {

    @SecuredAnnotation({"Editor","Reviewer","Super"})
    public static Result findPictureNameByLtrId(Long ltrId){
        ObjectNode json = Json.newObject();
        final Ltr ltr  = Ltr.find.byId(ltrId);
        if (ltr == null) {
            json.put("error",String.format("Ltr(%d) does not exist.", ltrId));
            return badRequest(Json.toJson(json));
        }

        Picture picture = Picture.findPictureByLtrId(ltrId);
        if (picture == null) {
            json.put("error",  String.format("Cannot find the picture of ltr:(%d)", ltrId));
            return internalServerError(Json.toJson(json));
        }

        String fileName = picture.storagePath;
        json.put("fileName",  fileName);
        return ok(Json.toJson(json));
    }
}

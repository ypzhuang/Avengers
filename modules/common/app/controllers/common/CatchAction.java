package controllers.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.SimpleResult;
import utils.common.ExceptionMailer;

/**
 * Created by ypzhuang on 15/10/14.
 */
public class CatchAction extends Action.Simple {
    public  F.Promise<SimpleResult> call(Http.Context ctx) {
        try {
            Logger.debug("running in CatchAction......");
            return delegate.call(ctx);
        } catch (Throwable e) {
            Logger.error(e.getMessage());
            ExceptionMailer mailer = new ExceptionMailer();
            mailer.send(e, ctx);
            Logger.debug("emailed to Administrator...");
            ObjectNode json = Json.newObject();
            json.put(utils.common.Constants.ERROR_KEY, "Internal server error, please contact the administrator");
            return F.Promise.pure((SimpleResult) internalServerError(json));
        }
    }
}

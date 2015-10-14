package controllers.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.common.Role;
import models.common.SysUser;
import play.Logger;
import play.libs.F;
import play.libs.Json;
import play.mvc.*;
import utils.common.Constants;


/**
 * Created by ypzhuang on 15/9/21.
 */
public class SecuredAnnotationAction extends Action<SecuredAnnotation> {
    @Override
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
        String [] roles = configuration.value();

        //Here is a good point to log user request
        Logger.debug("Calling action for {} in SecuredAnnotationAction " ,ctx);

        String token = getTokenFromHeader(ctx);

        ObjectNode node = Json.newObject();
        if (token != null && !"".equals(token.trim())) {
            SysUser user = SysUser.findByAuthToken(token);
            if (user != null) {
                Role role = user.role;
                Logger.debug(" with the role {} " ,role.toString());
                boolean in = false;
                if (role != null) {
                    for (String paramRole : roles) {
                        if (paramRole.equals(role.toString())) {
                            in = true;
                        }
                    }
                } else {
                    node.put(Constants.AUTH_ERROR_KEY,Constants.WITH_OUT_ROLE_ERROR);
                }

                if (in) {
                    ctx.request().setUsername(user.email);
                    return delegate.call(ctx);
                } else {
                    node.put(Constants.AUTH_ERROR_KEY,Constants.NOT_AUTHORIZED_ERROR);
                }
            } else {
                node.put(Constants.AUTH_ERROR_KEY,Constants.INVALID_AUTH_TOKEN_ERROR);
            }
        } else {
            node.put(Constants.AUTH_ERROR_KEY, Constants.REQUIRED_AUTH_TOKEN_ERROR);
        }
        Logger.debug("...but denied with token:{}!",token);
        return F.Promise.pure((SimpleResult) unauthorized(node));
    }

    private String getTokenFromHeader(Http.Context ctx) {
        String[] authTokenHeaderValues = ctx.request().headers().get(Constants.REQUEST_HEAD_TOKEN);
        if ((authTokenHeaderValues != null) && (authTokenHeaderValues.length == 1) && (authTokenHeaderValues[0] != null)) {
            return authTokenHeaderValues[0];
        }
        return null;
    }
}

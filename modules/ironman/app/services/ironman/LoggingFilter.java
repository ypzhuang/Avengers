package services.ironman;

import play.Logger;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import java.io.IOException;

/**
 * Created by ypzhuang on 15/12/18.
 */
public class LoggingFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext context) throws IOException {
        Logger.debug(context.getEntity().toString());
    }
}

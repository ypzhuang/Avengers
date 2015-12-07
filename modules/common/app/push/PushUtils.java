package push;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import play.Logger;
import play.libs.Json;
import push.android.PushMessage;
import utils.push.iOS.ApnsServiceUtil;
import utils.push.iOS.SuperApns;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by ypzhuang on 15/12/7.
 */
public class PushUtils {

    public static class MessageBody{

        public MessageBody(){}

        public String clientType;
        public String type;
        public String message;
        public String title;

        @Override
        public String toString() {
            return "MessageBody{" +
                    "clientType='" + clientType + '\'' +
                    ", type='" + type + '\'' +
                    ", message='" + message + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    public static void mockpush(String deviceToken,MessageBody body) throws PushException{
        Logger.debug("{},{}", deviceToken, body);
        Logger.debug("sent");

    }
    public static void push(String deviceToken,MessageBody body) throws PushException{
        Logger.debug("{},{}", deviceToken, body);

        try {
            if ("iOS".equalsIgnoreCase(body.clientType)) {

                ApnsService service = ApnsServiceUtil.getApnsService();

                String payload = null;
                SuperApns superApns = new SuperApns();

                Map<String, Object> map = new HashMap<String, Object>();
                map.put("type", body.type);
                superApns.setAlert(body.message);

                superApns.setObj(map);
                payload = ApnsServiceUtil.createAPNSJson(superApns);
                ApnsNotification notification = service.push(deviceToken, payload);

                Logger.debug("pushed");
            } else if ("Android".equalsIgnoreCase(body.clientType)) {

                String appKey = play.Play.application().configuration().getString("umeng.appkey");
                String secret = play.Play.application().configuration().getString("umeng.secret");
                Logger.debug("appKey:{}", appKey);
                Logger.debug("secret:{}", secret);


                UPush push = new UPush(appKey, secret);

                PushMessage message = new PushMessage();
                message.ticker = body.title;
                message.title = body.title;
                message.text = body.message;
                Map<String, String> map = new HashMap<String, String>();
                map.put("type", body.type);
                message.extra = map;


                push.sendAndroidUnicast(deviceToken, message);
                Logger.debug("pushed");

            }

        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new PushException(deviceToken+ " "+e.getMessage());
        }
    }


}

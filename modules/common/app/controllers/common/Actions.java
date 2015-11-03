package controllers.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ning.http.client.FluentCaseInsensitiveStringsMap;
import com.ning.http.util.AsyncHttpProviderUtils;
import models.common.Action;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import play.Logger;
import play.Play;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import utils.common.Constants;
import java.util.*;

/**
 * Created by ypzhuang on 15/10/15.
 */
public class Actions  extends Controller {

    private static final Form<Action> actionForm = Form.form(Action.class);

    public static Result findAllModules() {
        List<String> modules = Action.findAllModules();
        return ok(Json.toJson(modules));
    }

    public static Result findAllUrisByModule(String module) {
        List<String> uris = Action.findAllUrisByModule(module);
        return ok(Json.toJson(uris));
    }


    public static Result findAllMethod() {
        Form<Action> boundForm = actionForm.bindFromRequest();
        if(boundForm.hasErrors()) {
            return badRequest();
        }

        Action action = actionForm.get();
        List<String> methods = Action.findAllMethodByModuleAndUri(action.module, action.uri);
        return ok(Json.toJson(methods));
    }

    public static Result findAction() {
        Form<Action> boundForm = actionForm.bindFromRequest();
        if(boundForm.hasErrors()) {
            return badRequest();
        }

        Action action = actionForm.get();
        List<Action> actions = Action.findAllByModuleAndUriAndMethod(action.module, action.uri, action.method);
        return ok(Json.toJson(actions.get(0)));
    }

    public static Result mapOfModule() {
        Map<String,Map<String,Map<String,Action>>> map = new HashMap<String,Map<String,Map<String,Action>>>();
          //module     uri         method, list
        List<String> modules = Action.findAllModules();
        for (String module : modules) {
            List<String> uris = Action.findAllUrisByModule(module);

            Map<String,Map<String,Action>> urisMap= new HashMap<String,Map<String,Action>>();
            for (String uri: uris) {
                List<String> methods = Action.findAllMethodByModuleAndUri(module, uri);

                Map<String,Action> methodsMap = new HashMap<String,Action>();
                for (String method: methods) {
                    Action action = Action.findAllByModuleAndUriAndMethod(module, uri, method).get(0);
                    methodsMap.put(method,action);
                }

                urisMap.put(uri,methodsMap);
            }

            map.put(module,urisMap);
        }


        return ok(Json.toJson(map));
    }


    private static final Form<ActionForm> actionFormForm = Form.form(ActionForm.class);

    public static Result go() {

        String host =  Play.application().configuration().getString("host.url");

        Logger.debug("host:{}",host);

        Form<ActionForm> boundForm = actionFormForm.bindFromRequest();

        ObjectNode authTokenJson = Json.newObject();




        if(boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            //return badRequest(views.html.products.details.render(boundForm));
            authTokenJson.put(Constants.AUTH_TOKEN, "Unknown");
            return badRequest(views.html.admin.home.render(authTokenJson,null));
        }

        ActionForm form = boundForm.get();
        String uri = form.uri;
        String method = form.method;
        String body = form.body;
        String token = form.token;

        Logger.debug("uri:{}", uri);
        Logger.debug("method:{}", method);
        Logger.debug("body:{}", body);
        Logger.debug("token:{}", token);


        String url = String.format("%s%s", host, uri);
        Logger.debug("url:{}", url);
        if (body == null || body.trim().equals("")) {
            body = "{}";
        }
        Promise<WS.Response> p;
        if (method.equals("POST")) {
            Logger.debug("{}","POST");
            p = WS.url(url)
                    .setContentType("application/json")
                    .setHeader(Constants.REQUEST_HEAD_TOKEN, token)
                    .post(body);

        } else if(method.equals("GET")) {
            Logger.debug("{}","GET");
//            p = WS.url(url)
//                    .setContentType("application/json")
//                    .setHeader(Constants.REQUEST_HEAD_TOKEN, token)
//                    .get();
            p = executeString(url, "GET", body, token);
            Logger.debug("{}",p);
        } else if(method.equals("DELETE")) {
            Logger.debug("{}","DELETE");
//            p = WS.url(url)
//                    .setContentType("application/json")
//                    .setHeader(Constants.REQUEST_HEAD_TOKEN, token)
//                    .delete(); //cannot set body for delete method,so I created one.
           p = executeString(url,"DELETE", body, token);
        } else if (method.equals("PUT")) {
            Logger.debug("{}","PUT");
            p = WS.url(url)
                    .setContentType("application/json")
                    .setHeader(Constants.REQUEST_HEAD_TOKEN, token)
                    .put(body);
            Logger.debug("{}",p);
        } else {
            p = WS.url(url)
                    .setContentType("application/json")
                    .setHeader(Constants.REQUEST_HEAD_TOKEN, token)
                    .execute(method);
            Logger.debug("{}",p);
        }




        WS.Response response = p.get(10 * 1000);

        Logger.debug("{}, {},status:{}, {}",method,response.getUri().toString(),response.getStatus(),response.getStatusText());
        Logger.debug("Body:{}",response.getBody());

        ObjectNode json = Json.newObject();
        json.put("status",response.getStatus());
        json.put("statusText",response.getStatusText());
        json.put("method", method);


        try {
            json.put("content", response.asJson());
        }catch(Throwable e) {
            Logger.debug(e.getMessage());
            json.put("content", response.getBody());
        }
        json.put("uri", response.getUri().toString());
        json.put("head", utils.common.Constants.REQUEST_HEAD_TOKEN);
        json.put(utils.common.Constants.REQUEST_HEAD_TOKEN, token);
        json.put("requestBody", body);

        
        flash("success", String.format("Successfully executed at %s", response.getUri()));

        authTokenJson.put(Constants.AUTH_TOKEN, token);

        return ok(views.html.admin.home.render(authTokenJson,json));


    }


    private static Promise<WS.Response> executeString(String url,String method, String body,String token) {
        Map<String, Collection<String>> headersx = new HashMap<String, Collection<String>>();
        if (headersx.containsKey("X-AUTH-TOKEN")) {
            Collection<String> values = headersx.get("X-AUTH-TOKEN");
            values.add(token);
        } else {
            List<String> values = new ArrayList<String>();
            values.add(token);
            headersx.put("X-AUTH-TOKEN", values);
        }

        if (headersx.containsKey("Content-Type")) {
            Collection<String> values = headersx.get("Content-Type");
            values.add("application/json");
        } else {
            List<String> values = new ArrayList<String>();
            values.add("application/json");
            headersx.put("Content-Type", values);
        }


        FluentCaseInsensitiveStringsMap headers = new FluentCaseInsensitiveStringsMap(headersx);

        // Detect and maybe add charset
        String contentType = headers.getFirstValue(HttpHeaders.Names.CONTENT_TYPE);
        if (contentType == null) {
            contentType = "text/plain";
        }
        String charset = AsyncHttpProviderUtils.parseCharset(contentType);
        if (charset == null) {
            charset = "utf-8";
            headers.replace(HttpHeaders.Names.CONTENT_TYPE, contentType + "; charset=utf-8");
        }

        WS.WSRequest req = new WS.WSRequest(method).setBody(body)
                .setUrl(url)
                .setHeaders(headers)
              //  .setQueryParameters(new FluentStringsMap(queryParameters))
                .setBodyEncoding(charset);
        return req.execute();

    }

    public static class ActionForm {

        @Constraints.Required
        public String uri;

        @Constraints.Required
        public String method;


        public String body;

        @Constraints.Required
        public String token;

    }
}

package utils.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.StringUtils;
import play.Play;
import play.libs.Json;
import play.mvc.Http;

/**
 * Created by ypzhuang on 15/10/14.
 */
public class ExceptionMailer extends DefaultMailer {

    public ExceptionMailer(){
        super();
        this.setSubject("Pay attention to Unknown Exception");
        this.setRecipient(Play.application().configuration().getString("administrator.mail"));
    }


    public void send(Throwable e,Http.Context ctx){
        ObjectNode json = Json.newObject();
        StringBuilder builder = new StringBuilder();

        java.util.Map<String,String[]> headers = ctx.request().headers();
        for (String headerKey:headers.keySet()){
            builder.append("<h4>&nbsp;&nbsp;").append(headerKey).append(":").append(stringFromArray(headers.get(headerKey))).append("</h4>");
        }

        String path = ctx.request().method() + " " + ctx.request().path();
        String requestBody = ctx.request().body().asJson().toString();

        String body = String.format(
                "<html><body>" +
                "<h2>Hi 主人<h2>" +
                "<h3>有个问题我处理不来，需要您协助处理。</h3>" +
                "<h3>Path:</h3>" +
                "<h4>&nbsp;&nbsp;%s</h4>" +
                "<h3>Heads:</h3>" +
                "%s" +
                "<h3>Body:</h3>" +
                "<h4>&nbsp;&nbsp;%s</h4>" +
                "<h3>Error:</h3>" +
                "<h4>&nbsp;&nbsp;%s</h4>" +
                "<p>Thank you.</p>" +
                "</body></html>",path,builder.toString(),requestBody,e.getMessage());
        this.sendHtml(body);
    }

    public String stringFromArray(String array[]) {
        return StringUtils.arrayToCommaDelimitedString(array);
    }


}

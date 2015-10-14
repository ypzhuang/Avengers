package controllers.common;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import models.common.Product;
import play.Logger;
import play.api.Mode;
import play.api.Play;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.*;
import push.UPush;
import utils.push.iOS.ApnsServiceUtil;
import utils.push.iOS.SuperApns;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Products  extends Controller {



    @SecuredAnnotation({"Editor","Super"})
    public static Result search(String filter,int page,int pageSize){
        Logger.debug("product search action");
        List<Product> products = Product.search(filter, page, pageSize);
        return ok(Json.toJson(products));
    }

    public static Result currentMode(){
        ObjectNode json = Json.newObject();
        if(Play.current().mode() == Mode.Dev()) {
            json.put("mode","Dev");
        } else if (Play.current().mode() == Mode.Prod()) {
            json.put("mode","Prod");
        } else if (Play.current().mode() == Mode.Test()) {
            json.put("mode","Test");
        } else {
            json.put("mode","Unknown");
        }

        return ok(Json.toJson(json));
    }

    public static Result push(String deviceToken) {
        Logger.debug("{}",deviceToken);
        ApnsService service = ApnsServiceUtil.getApnsService();

        String payload = null;
        SuperApns superApns = new SuperApns();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", 1);
        superApns.setAlert("今天不早点下班吗？？");
        superApns.setObj(map);
        payload = ApnsServiceUtil.createAPNSJson(superApns);
        ApnsNotification notification = service.push(deviceToken, payload);

        return ok(Json.toJson(notification));
    }

    public static Result androidPush(String deviceToken) {

        ObjectNode json = Json.newObject();

        String appKey =  play.Play.application().configuration().getString("umeng.appkey");
        String secret =  play.Play.application().configuration().getString("umeng.secret");
        Logger.debug("appKey:{}",appKey);
        Logger.debug("secret:{}",secret);

        UPush push = new UPush(appKey,secret);
        Map<String,Object> map = new HashMap<String,Object>();

        Random random = new Random();
        int i = random.nextInt(1);

        try {
            String reason = "化验单照片不清楚";
            if ("0".equals(i + "")) {
                map.put("type", 0);
                //push.sendAndroidUnicast(deviceToken, "化验单识别失败", "识别失败", reason, "go_app", "notification", map);
            } else {//成功
                map.put("type", 1);
                //push.sendAndroidUnicast(deviceToken, "你有一张化验单识别好了", "识别成功", "你有一张化验单识别好了", "go_app", "notification", map);
            }
        }catch(Exception e) {
            Logger.debug("push to android error:",e.getMessage());
            json.put("result", "failed");
            json.put("error", e.getMessage());
        }


        json.put("result","ok");

        return ok(Json.toJson(json));

    }

    private static final Form<Product> productForm = Form.form(Product.class);

    public static Result newProduct() {
        return ok(views.html.products.details.render(productForm));
    }


    public static Result list() {
        Product product = new Product();

        Random generator = new Random();
        long i = generator.nextLong();

        product.ean = i;
        product.name = "World";
        product.description = "Test Code";

        product.save();


        List<Product> products = Product.findAll();

        return ok(Json.toJson(products));
    }


    public static Result home() {
        //Result result = authenticate();
        //Future<SimpleResult> future =  result.getWrappedResult();
        //return redirect(routes.Products.list());
        List<Product> products = Product.findAll();
        //return ok(products.list.render(products));



        return ok(views.html.products.list.render(products));
    }

    public static Result details(Long ean) {
        final Product product = Product.findByEan(ean);
        if (product == null) {
            return notFound(String.format("Product %s does not exist.", ean));
        }

//        if (request().accepts("text/plain")) {
//            return ok("goood");
//        }
        Form<Product> filledForm = productForm.fill(product);

        return ok(views.html.products.details.render(filledForm));
    }

    public static Result save() {
        Form<Product> boundForm = productForm.bindFromRequest();
        if(boundForm.hasErrors()) {
            flash("error", "Please correct the form below.");
            return badRequest(views.html.products.details.render(boundForm));
        }

        Product product = boundForm.get();
        product.save();
        flash("success", String.format("Successfully added product %s", product));

        return redirect(controllers.common.routes.Products.list());
    }

    public static Result delete(Long ean) {
        final Product product = Product.findByEan(ean);
        if(product == null) {
            return notFound(String.format("Product %s does not exists.", ean));
        }
        Product.remove(product);
        return redirect(controllers.common.routes.Products.list());
    }

}

package controllers.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.io.Files;
import models.common.*;
import org.springframework.beans.BeanUtils;
import play.Logger;
import play.core.j.JavaResultExtractor;
import play.data.Form;
import play.data.validation.Constraints;
import play.db.ebean.Transactional;
import play.mvc.*;
import push.PushException;
import push.PushUtils;
import utils.common.Constants;
import play.libs.Json;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

@With(CatchAction.class)
public class Ltrs extends Controller {
    public static class LtrHospitalAndTesttimeForm {
        @Constraints.Required
        public Long hospitalId;

        @Constraints.Required
        public String hospitalName;

        @Constraints.Required
        public Date testtime;
    }


    @SecuredAnnotation({"Editor","Reviewer","Super"}) // TODO add some constraints
    public static Result findById(Long id){
        final Ltr ltr  = Ltr.find.byId(id);
        if (ltr == null) {
            return notFound(String.format("Ltr(%d) does not exist.", id));
        }
        return ok(Json.toJson(ltr));
    }

    @SecuredAnnotation({"Super"})
    public static Result list(String filter,String status,int page,int pageSize){
        List<LtrStatus> statusList = new ArrayList<LtrStatus>();
        try {
            if (status != null && !"".equals(status.trim())) {
                String statusArray[] = status.split(",");
                for (String tmpStatus : statusArray) {
                    LtrStatus ltrStatus = LtrStatus.valueOf(tmpStatus);
                    statusList.add(ltrStatus);
                }
            }
        }catch(IllegalArgumentException e){
            ObjectNode json = Json.newObject();
            json.put("error",e.getMessage());
            return badRequest(Json.toJson(json));
        }

        List<Ltr> ltrs;
        if (statusList.size() > 0){
            ltrs = Ltr.search(filter, page, pageSize,statusList.toArray());
        } else {
            ltrs = Ltr.search(filter, page, pageSize);
        }

        return ok(Json.toJson(ltrs));
    }

    @SecuredAnnotation({"Super"})
    public static Result count(String filter,String status,int page,int pageSize){
        List<LtrStatus> statusList = new ArrayList<LtrStatus>();
        try {
            if (status != null && !"".equals(status.trim())) {
                String statusArray[] = status.split(",");
                for (String tmpStatus : statusArray) {
                    LtrStatus ltrStatus = LtrStatus.valueOf(tmpStatus);
                    statusList.add(ltrStatus);
                }
            }
        }catch(IllegalArgumentException e){
            ObjectNode json = Json.newObject();
            json.put("error",e.getMessage());
            return badRequest(Json.toJson(json));
        }

        int count;
        if (statusList.size() > 0){
            count = Ltr.count(filter, page, pageSize, statusList.toArray());
        } else {
            count = Ltr.count(filter, page, pageSize);
        }


        Map<String,Integer> map = new HashMap();
        map.put(Constants.JSON_KEY_FOR_COUNT,count);
        return ok(Json.toJson(map));
    }


    @SecuredAnnotation({"Editor"})
    public static Result list4edit(String filter,int page,int pageSize){
        List<Ltr> ltrs = Ltr.search(filter, page, pageSize, LtrStatus.ToEdit, LtrStatus.UserRejected, LtrStatus.Rejected);
        return ok(Json.toJson(ltrs));
    }

    @SecuredAnnotation({"Editor"})
    public static Result count4edit(String filter,int page,int pageSize){
        int count = Ltr.count(filter,page,pageSize,LtrStatus.ToEdit, LtrStatus.UserRejected, LtrStatus.Rejected);
        Map<String,Integer> map = new HashMap();
        map.put(Constants.JSON_KEY_FOR_COUNT,count);
        return ok(Json.toJson(map));
    }


    @SecuredAnnotation({"Reviewer"})
    public static Result list4review(String filter,Boolean isException,int page,int pageSize){
        List<Ltr> ltrs = Ltr.search(filter, isException, page, pageSize, LtrStatus.ToReview);
        return ok(Json.toJson(ltrs));
    }

    @SecuredAnnotation({"Reviewer"})
    public static Result count4review(String filter,Boolean isException,int page,int pageSize){
        int count = Ltr.count(filter, isException, page, pageSize, LtrStatus.ToReview);
        Map<String,Integer> map = new HashMap();
        map.put(Constants.JSON_KEY_FOR_COUNT, count);
        return ok(Json.toJson(map));
    }


    /**
     * updateHospital test time and hospitail id
     * @param id
     * @return
     */
    @SecuredAnnotation({"Editor","Reviewer","Super"})
    public static Result updateHospital(Long id){

        ObjectNode json = Json.newObject();
        final Ltr ltr  = Ltr.find.byId(id);
        if (ltr == null) {
            return notFound(String.format("Ltr(%d) does not exist.", id));
        }

        Form<LtrHospitalAndTesttimeForm> ltrFormForm = Form.form(LtrHospitalAndTesttimeForm.class).bindFromRequest();

        if (ltrFormForm.hasErrors()) {
            return badRequest(ltrFormForm.errorsAsJson());
        }

        LtrHospitalAndTesttimeForm form = ltrFormForm.get();
        Long hospitalId = form.hospitalId;
        String hospitalName = form.hospitalName;
        Date testTime = form.testtime;

        Hospital hospital = Hospital.find.byId(hospitalId);


        if (hospital == null ) {
            json.put("error","invalid hospital id");
            return  badRequest(Json.toJson(json));
        } else if (!hospital.name.equals(hospitalName)){
            json.put("error","invalid hospital name");
            return  badRequest(Json.toJson(json));
        } else {
            ltr.testtime = testTime;
            ltr.hospital = hospital.name;
            ltr.hid = hospitalId;
            ltr.update();
        }


        return ok(Json.toJson(ltr));
    }


    @SecuredAnnotation({"Editor","Super"})
    @BodyParser.Of(BodyParser.Json.class)
    public static Result updateLtrFailId(Long id){

        ObjectNode json = Json.newObject();
        final Ltr ltr  = Ltr.find.byId(id);
        if (ltr == null) {
            json.put("error", String.format("Ltr(%d) does not exist.", id));
            return notFound(Json.toJson(json));
        } else  if (!ltr.status.equals(LtrStatus.ToEdit) &&
                !ltr.status.equals(LtrStatus.UserRejected) &&
                !ltr.status.equals(LtrStatus.Rejected)) {
            json.put("error", String.format("illegal Ltr(%d) status.", id));
            return badRequest(Json.toJson(json));
        }


        JsonNode bodyJSON = request().body().asJson();
        Long failId = bodyJSON.findPath("failId").asLong();

        FailReason failReason = null;
        if(failId == null || failId <= 0) {
            json.put("error","Missing parameter [failId]");
            return badRequest(Json.toJson(json));
        } else {
            failReason = FailReason.findById(failId);
            if (failReason == null) {
                json.put("error","invalid failId");
                return  badRequest(Json.toJson(json));
            }
        }
        ltr.failId = failReason.id;
        ltr.status = LtrStatus.ToReview;
        ltr.update();
        return ok(Json.toJson(ltr));
    }

    @SecuredAnnotation({"Editor"})
    @Transactional
    public static Result uploadLtrPicture(Long id){
        ObjectNode json = Json.newObject();
        final Ltr ltr  = Ltr.find.byId(id);
        if (ltr == null) {
            json.put("error", String.format("Ltr(%d) does not exist.", id));
            return notFound(Json.toJson(json));
        }

        Result result = savePicture();


        if (result instanceof  SimpleResult) {
            SimpleResult status = (SimpleResult)result;
            int actual =  status.getWrappedSimpleResult().header().status();
            int expected = Results.ok().getWrappedSimpleResult().header().status();
            if (actual == expected) {
                String fileName = getFileName(result);
                Picture picture = Picture.findPictureByLtrId(id);
                if (picture != null) {
                    HistoryPicture historyPicture = new HistoryPicture();
                    BeanUtils.copyProperties(picture,historyPicture);
                    historyPicture.id = null;
                    historyPicture.save();

                    picture.storagePath = fileName;
                    picture.save();
                } else {
                    json.put("error",  String.format("Cannot find the picture of ltr:(%d)", id));
                    return internalServerError(Json.toJson(json));
                }
            } else {
                return result;
            }

        }


        return ok();
    }


    private static String getFileName(Result result){
        SimpleResult status = (SimpleResult)result;

        Logger.debug("status.getWrappedSimpleResult():{}", status.getWrappedSimpleResult());

        String header = JavaResultExtractor.getHeaders(status).get("Content-Type");
        String charset = "utf-8";
        if(header != null && header.contains("; charset=")){
            charset = header.substring(header.indexOf("; charset=") + 10, header.length()).trim();
        }
        byte[] body = JavaResultExtractor.getBody(status);
        String bodyStr = null;
        try {
            bodyStr = new String(body, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonNode bodyJson = Json.parse(bodyStr);
        String fileName = bodyJson.get("file").asText();
        Logger.debug("Got filename:" + fileName);
        return fileName;
    }

    @SecuredAnnotation({"Editor","Super"})
    public static Result savePicture(){

        Http.MultipartFormData body = request().body().asMultipartFormData();
        List<Http.MultipartFormData.FilePart> filePartList = body.getFiles();
        Logger.debug("fileList Size:{}",filePartList.size());

        ObjectNode json = Json.newObject();
        if (filePartList.size() == 0) {
            json.put("error","please select a file to upload!");
            return badRequest(Json.toJson(json));
        } else if (filePartList.size() > 1) {
            json.put("error","Now we only support one file to upload!");
            return badRequest(Json.toJson(json));
        } else {

            Http.MultipartFormData.FilePart part = filePartList.get(0);
            File file = part.getFile();
            String contentType = part.getContentType();

            if (!"image/jpeg".equals(contentType) && !"image/png".equals(contentType)) {
                json.put("error","Not support " + contentType + ".We only support jpeg and png!");
                return badRequest(Json.toJson(json));
            }

            Logger.debug("contentType:{}", part.getContentType());
            Logger.debug("fileName:{}", part.getFilename());
            Logger.debug("key:{}", part.getKey());
            try {

                String dir = play.Play.application().configuration().getString("FileServer.dir");

                String oldFileName = part.getFilename();

                String newFileName =
                        oldFileName.substring(0, oldFileName.lastIndexOf(".")) + "_" +
                        System.currentTimeMillis() + oldFileName.substring(oldFileName.lastIndexOf("."),oldFileName.length());
                File toFile = new File(dir + File.separator + newFileName);
                Files.copy(file,toFile);

                json.put("file", newFileName);
                return ok(Json.toJson(json));

            } catch (IOException e) {
                return internalServerError("Error reading file upload");
            }

        }
    }


    @SecuredAnnotation({"Reviewer","Super"})
    @Transactional
    public static Result reject(Long id) {
        ObjectNode json = Json.newObject();
        final Ltr ltr = Ltr.find.byId(id);
        if (ltr == null) {
            json.put("error", String.format("Ltr(%d) does not exist.", id));
            return notFound(Json.toJson(json));
        } else if (!ltr.status.equals(LtrStatus.ToReview)){
            json.put("error", String.format("illegal Ltr(%d) status.", id));
            return badRequest(Json.toJson(json));
        }

        ltr.status = LtrStatus.Rejected;
        ltr.save();

        return ok();

    }

    @SecuredAnnotation({"Reviewer","Super"})
    @Transactional
    public static Result push(Long id) {
        ObjectNode json = Json.newObject();
        final Ltr ltr = Ltr.find.byId(id);
        if (ltr == null) {
            json.put("error", String.format("Ltr(%d) does not exist.", id));
            return notFound(Json.toJson(json));
        }

        if (!ltr.status.equals(LtrStatus.ToReview)) {
            json.put("error", String.format("illegal Ltr(%d) status.", id));
            return badRequest(Json.toJson(json));
        }

        try {

            String type;
            String reason = "";
            if (ltr.failId != null) { //失败推送
                ltr.finish = 2; //兼容old manager
                ltr.state = 3;
                ltr.status = LtrStatus.Pushed;
                ltr.save();
                type = "0";
                FailReason failReason = FailReason.findById(ltr.failId);
                reason = failReason.content;
            } else {  //成功推送
                ltr.status = LtrStatus.Pushed;
                ltr.save();
                type = "1";
            }

            List<Device> devices = Device.findAllByUserId(ltr.uid);


            for (Device device : devices) {
                PushUtils.MessageBody body = new PushUtils.MessageBody();

                body.clientType = device.clientType;
                body.type = type;
                if (type.equals("1")) {
                    body.title = "化验单成功识别";

                    body.message = reason;
                } else {
                    body.title = "化验单失败失败";
                    body.message = "你有一张化验单识别好了";
                }

                PushUtils.mockpush(device.machineid, body);

            }

        } catch(PushException e){
            json.put("error", e.getMessage());
            return badRequest(Json.toJson(json));
        } catch(Exception e) {
            json.put("error", e.getMessage());
            return internalServerError(Json.toJson(json));
        }


        return ok();

    }



    @SecuredAnnotation({"Super"})
    public static Result ocringToEditing(Long id) {
        ObjectNode json = Json.newObject();
        final Ltr ltr = Ltr.find.byId(id);
        if (ltr == null) {
            json.put("error", String.format("Ltr(%d) does not exist.", id));
            return notFound(Json.toJson(json));
        }

        if (!ltr.status.equals(LtrStatus.OCRING) && !ltr.status.equals(LtrStatus.REOCR)) {
            json.put("error", String.format("illegal Ltr(%d) status.", id));
            return badRequest(Json.toJson(json));
        }

        ltr.status = LtrStatus.ToEdit;
        ltr.save();

        return ok();
    }



}

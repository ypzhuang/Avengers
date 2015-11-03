import com.avaje.ebean.Ebean;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import java.util.List;
import java.util.Date;
import java.util.Random;

import models.common.*;
import play.api.Mode;
import play.api.Play;
import play.libs.F;
import play.mvc.Http;
import play.mvc.Result;
import scala.Function1;
import scala.collection.JavaConversions;


public class Global extends GlobalSettings {
    @Override
    public void onStart(Application app) {
       
        Logger.info("Application has started");




        InitialData.insertAction(app);

        InitialData.insert(app);
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Application shutdown...");
    }
    static class InitialData {
        public static void insertAction(Application app) {
            if(Play.current().mode() == Mode.Dev() || Play.current().mode() == Mode.Test()) {
                List<Object> actionIds = Ebean.find(Action.class).findIds();
                Ebean.delete(Action.class,actionIds);
            }

            scala.collection.immutable.List<scala.Tuple3<String,String,String>> commonRoutes = common.Routes.documentation();


            List<scala.Tuple3<String,String,String>> commonRoutesList = JavaConversions.asJavaList(commonRoutes);

            Ebean.beginTransaction();
            try {
                List<Action> actions = new java.util.ArrayList<Action>();
                for (scala.Tuple3<String, String, String> route : commonRoutesList) {
                    String method = route._1();
                    String uri = route._2();
                    String mappingAction = route._3();

                    String module = null;
                    if (uri.length() == 1 || uri.contains("/assets") || uri.contains("/login") || uri.contains("/authenticate") || uri.contains("/actions")) {
                        continue;
                    }
                    int firstSlash = uri.indexOf("/");
                    int secondSlash = uri.indexOf("/", firstSlash + 1);
                    if (secondSlash > 0) {
                        module = uri.substring(firstSlash + 1 , secondSlash);
                    } else {
                        module = uri.substring(firstSlash + 1);
                    }

                    Action action = new Action();
                    action.method = method;
                    action.uri = uri;
                    action.mappingAction = mappingAction;
                    action.module = module;

                    actions.add(action);
                }
                Ebean.save(actions);
                Ebean.commitTransaction();
            }catch(Throwable e) {
                Logger.error(e.getMessage());
                Ebean.rollbackTransaction();
            }





        }
        public static void insert(Application app) {


            if (Ebean.find(Indicator.class).findRowCount() == 0) {
                Logger.info("insert indicator test data");

                Indicator indicator = new Indicator();
                indicator.code = "GLU";
                indicator.category = 1;
                indicator.description ="<p>糖的主要功能是提供热能。每克葡萄糖在人体内氧化产生4千卡能量，人体所需要的70%左右的能量由糖提供。此外，糖还是构成组织和保护肝脏功能的重要物质。</p>\n" +
                        "<p>长期高糖饮食，会使人体内环境失调，进而给人体健康造成种种危害。由于糖属酸性物质，吃糖过量会改变人体血液的酸碱度，呈酸性体质，减弱人体白血球对外界病毒的抵御能力，使人易患各种疾病。</p>";
                indicator.showname = "糖";
                indicator.unit = "mmol/L";
                Ebean.save(indicator);

                indicator = new Indicator();
                indicator.code = "PA";
                indicator.category = 1;
                indicator.description ="<p>前白蛋白，又称转甲状腺素蛋白，由肝细胞合成。因此，测定其在血浆中的浓度对于了解蛋白质的营养不良、肝功能不全、比之白蛋白和转铁蛋白具有更高的敏感性。</p> <p>对于不同的人群，前白蛋白的正常值是不同的，具体如下：1岁左右的儿童前白蛋白正常值是 100mg/L；1-3岁的孩子前白蛋白的正常范围是168-281mg/L；成年人则在280-360mg/L。一般来说病越重，前白蛋白值越低。</p>";
                indicator.showname = "前白蛋白";
                indicator.unit = "mg/L";
                Ebean.save(indicator);

                indicator = new Indicator();
                indicator.code = "AST";
                indicator.category = 1;
                indicator.description ="<p>谷丙转氨酶（缩写 ALT）是一种转氨酶，存在于血浆及多种身体组织中，但最常见与肝脏关联。</p> <p>ALT的含量升高往往意味着酒精肝或病毒性肝炎，充血性心力衰竭、肝损伤、胆管损伤，传染性单核细胞增多症或肌病等疾病的存在。因此，谷丙转氨酶常常被用来确认肝脏的问题。但是，谷丙转氨酶含量升高并不一定意味着病变的发生。一天内谷丙转氨酶的含量也会有正常的起伏。谷丙转氨酶含量也会由于剧烈运动而升高。</p>";
                indicator.showname = "谷草转氨酶";
                indicator.unit = "mg/L";
                Ebean.save(indicator);

                indicator = new Indicator();
                indicator.code = "";
                indicator.category = 4;
                indicator.description ="<p>果糖胺是血浆中的蛋白质与葡萄糖非酶糖化过程中形成的高分子酮胺结构类似果糖胺的物质，它的浓度与血糖水平成正相关，并相对保持稳定。它的测定却不受血糖的影响。由于血浆蛋白的半衰期为17～20天，故果糖胺可以反映糖尿病患者检测前1～3周内的平均血糖水平。从一定程度上弥补了糖化血红蛋白不能反映较短时期内血糖浓度变化的不足。果糖胺的测定快速而价廉（化学法），是评价糖尿病控制情况的一个良好指标，尤其是对血糖波动较大的脆性糖尿病及妊娠糖尿病，了解其平均血糖水平有实际意义。但果糖胺不受每次进食的影响，所以不能用来直接指导每日胰岛素及口服降糖药的用量。血清果糖胺正常值为1.64～2.64mmol/L，血浆中果糖胺较血清低0.3mmol/L。</p>";
                indicator.showname = "果糖胺";
                indicator.unit = "mmol/L";
                Ebean.save(indicator);

            } else {
                Logger.info("test data exists");
            }


            if (Ebean.find(Ltr.class).findRowCount() == 0) {
                Logger.info("insert test ltr data");

                List<Indicator> indicators = Indicator.findAll();
                for(int i = 0; i <  100; i++){
                    Ltr ltr = mockLtr();
                    Ebean.save(ltr);

                    Random random = new Random();
                    int sizeOfIndicatorValue = random.nextInt(10);

                    for (int k = 0;k < sizeOfIndicatorValue; k++) {
                        IndicatorValue value = mockIndicatorValue();
                        value.uid = ltr.uid;
                        value.ltr = ltr;


                        int randomIndex = random.nextInt(indicators.size());
                        value.indicator = indicators.get(randomIndex);

                        Ebean.save(value);
                    }

                }
            } else {
                Logger.info("test data exists");
            }


            insertHospitalTestData();


            insertUserTestDate();

            insertFailReasonData();

        }

        public static void insertFailReasonData(){
            if (Ebean.find(FailReason.class).findRowCount() == 0) {
                FailReason reason = new FailReason();
                reason.description = "您有一张化验单因信息不完整无法识别。";
                reason.content = "化验单不完整";
                reason.createTime = new Date();
                reason.isDeleted = false;
                reason.suggestion = "上传同时包含项目名称、检验结果、参考范围（含单位）的化验单。如仍有疑问，请联系：ocr.yizhenapp@augbase.com";
                reason.save();


                reason = new FailReason();
                reason.description = "您有一张化验单因超出识别范畴无法识别。";
                reason.content = "超出易诊识别范畴";
                reason.createTime = new Date();
                reason.isDeleted = false;
                reason.suggestion = "暂未开通对非化验单类型（包括B超、CT等）检验结果的识别服务。建议关注“易诊”（绿色图标）直接发图咨询医师。";
                reason.save();

                reason = new FailReason();
                reason.description = "您有一张化验单因非纸质报告无法有效识别。";
                reason.content = "不是纸质的化验单等报告";
                reason.createTime = new Date();
                reason.isDeleted = false;
                reason.suggestion = "在清晰的光线下对准您的纸质报告进行拍摄。如仍有疑问，请联系：ocr.yizhenapp@augbase.com";
                reason.save();

                reason = new FailReason();
                reason.description = "您有一张化验单因缺少时间信息无法有效识别。";
                reason.content = "缺少时间信息，且和之前已识别的化验单种类冲突";
                reason.createTime = new Date();
                reason.isDeleted = false;
                reason.suggestion = "可以讲化验单底下的时间信息拍摄进去，从而不导致冲突。如仍有疑问，请联系：ocr.yizhenapp@augbase.com";
                reason.save();

            }
        }
        public static void insertUserTestDate(){
            if (Ebean.find(SysUser.class).findRowCount() == 0) {
                SysUser guest = new SysUser("guest@augbase.com","12345678");
                guest.role = Role.Guest;
                guest.save();

                SysUser editor = new SysUser("editor@augbase.com","12345678");
                editor.role = Role.Editor;
                editor.save();

                SysUser reviewer = new SysUser("reviewer@augbase.com","12345678");
                reviewer.role = Role.Reviewer;
                reviewer.save();

                SysUser superUser = new SysUser("super@augbase.com","12345678");
                superUser.role = Role.Super;
                superUser.save();
            }
        }

        public static void insertHospitalTestData() {
            String hospitals[] = {"延安大学附属医院","北京市隆福医院","北京市海淀区妇幼保健院"};
            String addresses[] = {"陕西省延安市中心街250号","北京市东城区美术馆东街18号","北京市海淀区海淀南路33号"};
            if (Ebean.find(Hospital.class).findRowCount() == 0) {
                Logger.info("insert hosptial ltr data");
                for(int i = 0; i < hospitals.length; i++) {
                    Hospital hosptial = new Hospital();
                    hosptial.name = hospitals[i];
                    hosptial.address = addresses[i];
                    Ebean.save(hosptial);
                }
            } else {
                Logger.info("test data exists");
            }
        }

        public static Ltr mockLtr() {
            Ltr ltr = new Ltr();
            Random random = new Random();
            Long uid  =  random.nextLong();
            ltr.uid = (uid > 0 ? uid : -uid);

            Long hid  =  random.nextLong();
            ltr.hid = (hid > 0 ? hid : -hid);

            ltr.hospital = generateHosptial();
            ltr.createtime = new Date();
            ltr.testtime =  new Date();
            ltr.category = 1;
            ltr.finish = 0;
            ltr.delete = false;
            ltr.type = 0;
            ltr.status = generateStatus();

            return ltr;
        }

        public static String generateHosptial(){
            String hosptials[] = {"上海瑞金医院","华山医院分院","上海中山医院","中国医院分院"};
            Random random = new Random();
            int i = random.nextInt(4);
            return hosptials[i];
        }

        static int index;
        public static LtrStatus generateStatus(){
            int i = index % LtrStatus.values().length ;
            index++;
            return LtrStatus.values()[i];
        }


        public static IndicatorValue mockIndicatorValue(){
            IndicatorValue value = new IndicatorValue();
            value.category = 1;
            value.value = 20.0d;
            value.state = 0;
            value.type = 0;
            value.upperlimit = 100d;
            value.lowerlimit = 40d;
            value.direction = 0;
            value.calmethod = 0;
            value.testtime = new Date();
            value.isDelete = 0;
            value.standardValue = value.value;
            return value;
        }



    }
}

package push;

import org.json.JSONArray;
import org.json.JSONObject;

import push.android.*;

import java.util.HashMap;
import java.util.Map;


public class UPush {
	private String appkey = null;
	private String appMasterSecret = null;
	private String timestamp = null;
	
	public UPush(String key, String secret) {
		try {
			appkey = key;
			appMasterSecret = secret;
			timestamp = Integer.toString((int)(System.currentTimeMillis() / 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendAndroidBroadcast() throws Exception {
		AndroidBroadcast broadcast = new AndroidBroadcast();
		broadcast.setAppMasterSecret(appMasterSecret);
		broadcast.setPredefinedKeyValue("appkey", this.appkey);
		broadcast.setPredefinedKeyValue("timestamp", this.timestamp);
		broadcast.setPredefinedKeyValue("ticker", "Android broadcast ticker");
		broadcast.setPredefinedKeyValue("title", "中文的title");
		broadcast.setPredefinedKeyValue("text",   "Android broadcast text");
		broadcast.setPredefinedKeyValue("after_open", "go_app");
		broadcast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		broadcast.setPredefinedKeyValue("production_mode", "true");
		// Set customized fields
		broadcast.setExtraField("test", "helloworld");
		broadcast.send();
	}


	public void sendAndroidUnicast(String deviceToken,PushMessage message) throws Exception {
		AndroidUnicast unicast = new AndroidUnicast();
		unicast.setAppMasterSecret(appMasterSecret);
		unicast.setPredefinedKeyValue("appkey", this.appkey);
		unicast.setPredefinedKeyValue("timestamp", this.timestamp);
		unicast.setPredefinedKeyValue("device_tokens",deviceToken);
		unicast.setPredefinedKeyValue("ticker", message.ticker);
		unicast.setPredefinedKeyValue("title", message.title);
		unicast.setPredefinedKeyValue("text",   message.text);
		unicast.setPredefinedKeyValue("after_open", "go_custom");
		unicast.setPredefinedKeyValue("display_type", "notification");
        unicast.setPredefinedKeyValue("custom",message.extra);
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		unicast.setPredefinedKeyValue("production_mode", "true");
		// Set customized fields
//        final Map<String,String> extra = message.extra;
//        for(String key : extra.keySet()) {
//            unicast.setExtraField(key, extra.get(key));
//        }



        unicast.send();
	}



	
	public void sendAndroidGroupcast() throws Exception {
		AndroidGroupcast groupcast = new AndroidGroupcast();
		groupcast.setAppMasterSecret(appMasterSecret);
		groupcast.setPredefinedKeyValue("appkey", this.appkey);
		groupcast.setPredefinedKeyValue("timestamp", this.timestamp);
		/*  TODO
		 *  Construct the filter condition:
		 *  "where": 
		 *	{
    	 *		"and": 
    	 *		[
      	 *			{"tag":"test"},
      	 *			{"tag":"Test"}
    	 *		]
		 *	}
		 */
		JSONObject filterJson = new JSONObject();
		JSONObject whereJson = new JSONObject();
		JSONArray tagArray = new JSONArray();
		JSONObject testTag = new JSONObject();
		JSONObject TestTag = new JSONObject();
		testTag.put("tag", "test");
		TestTag.put("tag", "Test");
		tagArray.put(testTag);
		tagArray.put(TestTag);
		whereJson.put("and", tagArray);
		filterJson.put("where", whereJson);
		System.out.println(filterJson.toString());
		
		groupcast.setPredefinedKeyValue("filter", filterJson);
		groupcast.setPredefinedKeyValue("ticker", "Android groupcast ticker");
		groupcast.setPredefinedKeyValue("title", "中文的title");
		groupcast.setPredefinedKeyValue("text",   "Android groupcast text");
		groupcast.setPredefinedKeyValue("after_open", "go_app");
		groupcast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		groupcast.setPredefinedKeyValue("production_mode", "true");
		groupcast.send();
	}
	
	public void sendAndroidCustomizedcast() throws Exception {
		AndroidCustomizedcast customizedcast = new AndroidCustomizedcast();
		customizedcast.setAppMasterSecret(appMasterSecret);
		customizedcast.setPredefinedKeyValue("appkey", this.appkey);
		customizedcast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO Set your alias here, and use comma to split them if there are multiple alias.
		// And if you have many alias, you can also upload a file containing these alias, then 
		// use file_id to send customized notification.
		customizedcast.setPredefinedKeyValue("alias", "xx");
		// TODO Set your alias_type here
		customizedcast.setPredefinedKeyValue("alias_type", "xx");
		customizedcast.setPredefinedKeyValue("ticker", "Android customizedcast ticker");
		customizedcast.setPredefinedKeyValue("title", "中文的title");
		customizedcast.setPredefinedKeyValue("text",   "Android customizedcast text");
		customizedcast.setPredefinedKeyValue("after_open", "go_app");
		customizedcast.setPredefinedKeyValue("display_type", "notification");
		// TODO Set 'production_mode' to 'false' if it's a test device. 
		// For how to register a test device, please see the developer doc.
		customizedcast.setPredefinedKeyValue("production_mode", "true");
		customizedcast.send();
	}
	
	public void sendAndroidFilecast() throws Exception {
		AndroidFilecast filecast = new AndroidFilecast();
		filecast.setAppMasterSecret(appMasterSecret);
		filecast.setPredefinedKeyValue("appkey", this.appkey);
		filecast.setPredefinedKeyValue("timestamp", this.timestamp);
		// TODO upload your device tokens, and use '\n' to split them if there are multiple tokens 
		filecast.uploadContents("aa"+"\n"+"bb");
		filecast.setPredefinedKeyValue("ticker", "Android filecast ticker");
		filecast.setPredefinedKeyValue("title",  "中文的title");
		filecast.setPredefinedKeyValue("text",   "Android filecast text");
		filecast.setPredefinedKeyValue("after_open", "go_app");
		filecast.setPredefinedKeyValue("display_type", "notification");
		filecast.send();
	}
	

	public static void main(String[] args) {
		// TODO set your appkey and master secret here
		UPush demo = new UPush("560b40b567e58e61ff000195", "ee9ttmzifrrtopao4lqm4eftccmafpc9");
		try {
            PushMessage message = new PushMessage();
            message.ticker = "化验单识别失败";
            message.title = "识别失败";
            message.text = "照片太模糊了，请换iPhone 6 Plus拍摄";
            Map<String,String> map = new HashMap<String,String>();
            map.put("type", "1");
            message.extra = map;


			demo.sendAndroidUnicast("AtJJ6-q5ovoo9ecm2z8wWaicg9aNlU-8J-yDdC2nqICU",message);
			/* TODO these methods are all available, just fill in some fields and do the test
			 * demo.sendAndroidBroadcast();
			 * demo.sendAndroidGroupcast();
			 * demo.sendAndroidCustomizedcast();
			 * demo.sendAndroidFilecast();
			 * 
			 * demo.sendIOSBroadcast();
			 * demo.sendIOSUnicast();
			 * demo.sendIOSGroupcast();
			 * demo.sendIOSCustomizedcast();
			 * demo.sendIOSFilecast();
			 */
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

}

package utils.push.iOS;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.json.JSONException;
import org.json.JSONObject;
import play.Logger;
import play.Play;
import play.api.Mode;

/**
 * 单例 -- 返回APNS
 * @author Liu Congcong
 *
 */
public class ApnsServiceUtil {
	
	private static ApnsService apnsService;
	private static ApnsService doctorApnsService;  //医生端推送
	
	public static ApnsService getDoctorApnsService() {
		if(doctorApnsService == null){
			return createDoctorApnsService();
		}else{
			return doctorApnsService;
		}
	}
	
	
	public static ApnsService getApnsService() {
		if(apnsService == null){
			return createApnsService();
		}else{
			return apnsService;
		}
	}
	
	
	private static ApnsService createApnsService() {
		String certificatePathOfPatient =  Play.application().configuration().getString("push.patient.filePath");
		String passwordPathOfPatient =  Play.application().configuration().getString("push.patient.password");

		Logger.debug("certificatePathOfPatient:{}", certificatePathOfPatient);
		Logger.debug("passwordPathOfPatient:{}", passwordPathOfPatient);
		if(play.api.Play.current().mode() == Mode.Dev()) {
			apnsService = APNS.newService()
					.withCert(certificatePathOfPatient, passwordPathOfPatient).withSandboxDestination().build();
		} else if (play.api.Play.current().mode() == Mode.Prod()) {
			apnsService = APNS.newService()
					.withCert(certificatePathOfPatient, passwordPathOfPatient).withProductionDestination().build();
		}

		return apnsService;
	}
	
	
	private static ApnsService createDoctorApnsService() {
		String certificatePathOfDoctor =  Play.application().configuration().getString("push.doctor.filePath");
		String passwordPathOfDoctor =  Play.application().configuration().getString("push.doctor.password");


		if(play.api.Play.current().mode() == Mode.Dev()) {
			doctorApnsService = APNS.newService()
					.withCert(certificatePathOfDoctor, passwordPathOfDoctor).withSandboxDestination().build();
		} else if (play.api.Play.current().mode() == Mode.Prod()) {
			doctorApnsService = APNS.newService()
					.withCert(certificatePathOfDoctor, passwordPathOfDoctor).withProductionDestination().build();
		}

		return doctorApnsService;
	}
	
	
	
	//创建json 
	public static String createAPNSJson(Object newType){
		JSONObject obj = new JSONObject();
		try {
			obj.put("aps",new JSONObject(newType));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
}

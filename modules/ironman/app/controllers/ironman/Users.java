package controllers.ironman;

import java.util.List;

import org.igniterealtime.restclient.entity.UserEntities;
import org.igniterealtime.restclient.entity.UserEntity;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ironman.ApiClient;

public class Users extends Controller{
	 public static Result findAllUsers() {
	     UserEntities userEntities = ApiClient.getInstance().getUsers();
	     List<UserEntity> users= userEntities.getUsers();
		 for (UserEntity userEntity:users) {
			 Logger.debug("name:" + userEntity.getName() + " email:" + userEntity.getEmail() + " name:" + userEntity.getName() + " password:" + userEntity.getPassword());

		 }
		 return ok(Json.toJson(users));
	 }
}

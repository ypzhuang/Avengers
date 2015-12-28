package controllers.ironman;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import org.igniterealtime.restclient.entity.MUCRoomEntities;
import org.igniterealtime.restclient.entity.MUCRoomEntity;
import org.igniterealtime.restclient.entity.UserEntities;
import org.igniterealtime.restclient.entity.UserEntity;

import com.fasterxml.jackson.databind.node.ObjectNode;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import services.ironman.ApiClient;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class Rooms extends Controller{
	 public static Result findAllRooms() {
		 MUCRoomEntities roomEntities = ApiClient.getInstance().getChatRooms();
	      List<MUCRoomEntity> roomEntity= roomEntities.getMucRooms();


		 return ok(Json.toJson(roomEntity));
	 }

	 public static Result createRoom() {
		 ObjectNode json = Json.newObject();
		 MUCRoomEntity chatRoom = new MUCRoomEntity("chatroom9", "Four Chat Room", "Some descriptionxxxxx");
		 chatRoom.setPersistent(true);
		 chatRoom.setMaxUsers(50);
		 List<String> broadcastPresenceRoles = Arrays.asList(new String[]{"moderator", "participant", "visitor"});
		 chatRoom.setBroadcastPresenceRoles(broadcastPresenceRoles);
//		 chatRoom.setPublicRoom(true);
//		 chatRoom.setRegistrationEnabled(true);
//		 chatRoom.setCanOccupantsInvite(true);
//		 chatRoom.setLogEnabled(true);
//		 chatRoom.setMembersOnly(false);
//
		 chatRoom.setOwners(Arrays.asList(new String[]{"admin@121.43.151.177"}));

//		 List<String> admins = Arrays.asList(new String[]{"admin@121.43.151.177"});
//		 chatRoom.setAdmins(admins);
//
//		 chatRoom.setCreationDate(new Date());
//		 chatRoom.setModificationDate(new Date());

		 Entity entity = Entity.entity(chatRoom, MediaType.APPLICATION_XML);

		 Response response = ApiClient.getInstance().createChatRoom(chatRoom);
		 Logger.debug("{}",response);
		 if (response.getStatus() == 201 ) {
			 json.put("res",new Integer(0));
		 } else {
			 json.put("res",new Integer(101));
			 json.put("error",response.getEntity().toString());
		 }
		 return ok(Json.toJson(json));
	 }
}

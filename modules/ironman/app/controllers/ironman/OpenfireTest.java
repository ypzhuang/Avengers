package controllers.ironman;

import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.AuthenticationToken;
import org.igniterealtime.restclient.entity.UserEntities;
import org.igniterealtime.restclient.entity.UserEntity;
import play.Logger;

import java.util.List;

/**
 * Created by ypzhuang on 15/12/18.
 */
public class OpenfireTest {
    public static void main(String args[]){
      
        System.out.println("azzzz");
        //AuthenticationToken authenticationToken = new AuthenticationToken("admin", "fuckgfw1");
        AuthenticationToken authenticationToken = new AuthenticationToken("l6Mn7Q3nthY3aHF4");
        
        // Set Openfire settings (9090 is the port of Openfire Admin Console)
        RestApiClient restApiClient = new RestApiClient("http://121.43.151.177", 9090, authenticationToken);

        // Request all available users
        UserEntities userEntities = restApiClient.getUsers();
        List<UserEntity> users= userEntities.getUsers();
        for (UserEntity userEntity:users) {
        	
            System.out.println("name:"+userEntity.getName() + " email:" + userEntity.getEmail() +" name:"+userEntity.getName() + " password:"+ userEntity.getPassword());
           
        }
        System.out.println("Done");
    }
}

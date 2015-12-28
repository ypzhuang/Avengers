package controllers.ironman;

import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.AuthenticationToken;
import org.igniterealtime.restclient.entity.UserEntities;
import org.igniterealtime.restclient.entity.UserEntity;
import play.*;
import play.mvc.*;
import services.ironman.ApiClient;
import views.html.*;

import java.util.List;

public class Application extends Controller {

    public static Result index() {
        // Shared secret key
        //AuthenticationToken authenticationToken = new AuthenticationToken("l6Mn7Q3nthY3aHF4");
        // Basic HTTP Authentication
        //AuthenticationToken authenticationToken = new AuthenticationToken("admin", "fuckgfw1");
        // Set Openfire settings (9090 is the port of Openfire Admin Console)
        //RestApiClient restApiClient = new RestApiClient("http://121.43.151.177", 9090, authenticationToken);

        // Request all available users
        UserEntities userEntities = ApiClient.getInstance().getUsers();
        List<UserEntity> users= userEntities.getUsers();
        for (UserEntity userEntity:users) {
            Logger.debug("username:" + userEntity.getUsername() + " email:" + userEntity.getEmail() + " name:" + userEntity.getName() + " password:" + userEntity.getPassword());

        }

        return ok(index.render("Your new application is ready from Ironman."));
    }

}

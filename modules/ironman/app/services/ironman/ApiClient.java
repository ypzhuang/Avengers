package services.ironman;


import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.AuthenticationToken;
import play.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class ApiClient {
    private static RestApiClient apiClient;
    
    private ApiClient(){}
    
    public static RestApiClient getInstance(){
    	
    	if (apiClient == null) {
    		synchronized (ApiClient.class) {
				if (apiClient == null) {
					Logger.debug("Initial RestApiClient....");

//					System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
//					System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
//					System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
//					System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

//					Client client = ClientBuilder.newClient();
//					client.register(new LoggingFilter());

//
//					Client client = Client.create();
//
//
//					client.addFilter(new LoggingFilter(System.out));


					ClientConfig config = new ClientConfig();

					Client client = ClientBuilder.newClient(config);
					client.register(new LoggingFilter());


//					Client client = ClientBuilder.newClient();
//					client.register(new LoggingFilter());

					AuthenticationToken authenticationToken = new AuthenticationToken("l6Mn7Q3nthY3aHF4");
					apiClient =  new RestApiClient("121.43.151.177", 9090, authenticationToken);
				}
			}
    	}
    	return apiClient;
    }
    
}

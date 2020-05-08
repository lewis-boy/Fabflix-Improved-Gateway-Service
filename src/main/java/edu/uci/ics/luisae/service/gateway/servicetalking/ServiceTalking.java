package edu.uci.ics.luisae.service.gateway.servicetalking;

import edu.uci.ics.luisae.service.gateway.GatewayService;
import edu.uci.ics.luisae.service.gateway.core.ProcessRequest;
import edu.uci.ics.luisae.service.gateway.logger.ServiceLogger;
import edu.uci.ics.luisae.service.gateway.models.LoginAndSessionResponse;
import edu.uci.ics.luisae.service.gateway.models.SessionRequest;
import edu.uci.ics.luisae.service.gateway.util.Util;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.JSONObject;

import javax.ws.rs.client.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ServiceTalking {

    public static LoginAndSessionResponse sessionCheck(HttpHeaders headers){
        String sessionEndpoint = GatewayService.getIdmConfigs().getSessionPath();
        SessionRequest request = new SessionRequest(headers.getHeaderString("email"),
                                                    headers.getHeaderString("session_id"));

        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        WebTarget webTarget = client.target(getIdmPath()).path(sessionEndpoint);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        //need headers to identify
        ProcessRequest.attachHeaders(invocationBuilder, headers);
        Response jsonResponse = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
        LoginAndSessionResponse response = Util.modelMapper(jsonResponse.readEntity(String.class),LoginAndSessionResponse.class);

        if(response.getResultCode() != 130)
            return response;
        return null;

    }

    public static String getIdmPath(){
        return GatewayService.getIdmConfigs().getScheme() + GatewayService.getIdmConfigs().getHostName() + ":"
                + GatewayService.getIdmConfigs().getPort() + GatewayService.getIdmConfigs().getPath();
    }
    public static String getMoviesPath(){
        return GatewayService.getMoviesConfigs().getScheme() + GatewayService.getMoviesConfigs().getHostName() + ":"
                + GatewayService.getMoviesConfigs().getPort() + GatewayService.getMoviesConfigs().getPath();
    }
    public static String getBillingPath(){
        return GatewayService.getBillingConfigs().getScheme() + GatewayService.getBillingConfigs().getHostName() + ":"
                + GatewayService.getBillingConfigs().getPort() + GatewayService.getBillingConfigs().getPath();
    }

}

package edu.uci.ics.luisae.service.gateway.core;

import edu.uci.ics.luisae.service.gateway.GatewayService;
import edu.uci.ics.luisae.service.gateway.logger.ServiceLogger;
import edu.uci.ics.luisae.service.gateway.models.LoginAndSessionResponse;
import edu.uci.ics.luisae.service.gateway.servicetalking.ServiceTalking;
import edu.uci.ics.luisae.service.gateway.threadpool.ClientRequest;
import edu.uci.ics.luisae.service.gateway.threadpool.HTTPMethod;
import edu.uci.ics.luisae.service.gateway.transaction.TransactionGenerator;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

public class ProcessRequest {
    public static void attachHeaders(Invocation.Builder invocationBuilder, ClientRequest request){
        if(request.getEmail() != null)
            invocationBuilder.header("email", request.getEmail());
        if(request.getSession_id() != null)
            invocationBuilder.header("session_id",request.getSession_id());
        invocationBuilder.header("transaction_id", request.getTransaction_id());
    }

    public static void attachHeaders(Invocation.Builder invocationBuilder, HttpHeaders headers){
        if(headers.getHeaderString("email") != null)
            invocationBuilder.header("email", headers.getHeaderString("email"));
        if(headers.getHeaderString("session_id") != null)
            invocationBuilder.header("session_id",headers.getHeaderString("session_id"));
        invocationBuilder.header("transaction_id", headers.getHeaderString("transaction_id"));
    }

    public static Response get(WebTarget webTarget, ClientRequest request){
        Invocation.Builder invocationBuilder;
//        if(request.getPathParams()!= null) {
////            for (Map.Entry<String, List<String>> entry : request.getPathParams().entrySet()) {
////                ServiceLogger.LOGGER.info("Key: " + entry.getKey() + "\tValue: " + entry.getValue().get(0));
////                webTarget = webTarget.resolveTemplate(entry.getKey(), entry.getValue().get(0));
////            }
////        }
        if(request.getQueryParams()!= null){
            for(Map.Entry<String, List<String>> entry : request.getQueryParams().entrySet()) {
                ServiceLogger.LOGGER.info("Key: " + entry.getKey() + "\tValue: " + entry.getValue().get(0));
                webTarget = webTarget.queryParam(entry.getKey(), entry.getValue().get(0));
            }
        }
        invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        attachHeaders(invocationBuilder, request);
        ServiceLogger.LOGGER.info(webTarget.toString());

        try{
            return invocationBuilder.get();
        }catch(ProcessingException e){
            ServiceLogger.LOGGER.warning(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Connection Error.").build();
        }
    }

    public static Response post(WebTarget webTarget, ClientRequest request){
        Invocation.Builder invocationBuilder;
        invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        attachHeaders(invocationBuilder, request);
        return invocationBuilder.post(Entity.entity(request.getJsonBytes(),MediaType.APPLICATION_JSON));
    }

    public static Response createAndPutRequestIntoQueue(HttpHeaders headers,
                                                        String URI,
                                                        String endpoint,
                                                        HTTPMethod method,
                                                        MultivaluedMap<String, String> queryParams,
                                                        byte[] jsonBytes){
        String transactionId = TransactionGenerator.generate();
        ClientRequest cr = new ClientRequest(headers.getHeaderString("email"),
                headers.getHeaderString("session_id"),
                transactionId,
                URI,
                endpoint,
                method,
                queryParams,
                jsonBytes);
        GatewayService.getThreadPool().putRequest(cr);
        return Response.status(Response.Status.NO_CONTENT)
                .header("transaction_id",transactionId)
                .header("request_delay", GatewayService.getThreadConfigs().getRequestDelay())
                .header("Please wait...", "String").build();
    }

    public static Response simpleIdmCall(byte[] jsonBytes,String endpoint){
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        WebTarget webTarget = client.target(ServiceTalking.getIdmPath()).path(endpoint);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        return invocationBuilder.post(Entity.entity(jsonBytes, MediaType.APPLICATION_JSON));
    }

    public static Response sendBackIdmResponse(LoginAndSessionResponse response, HttpHeaders headers){
        return Response.status(response.getResult().getStatus()).entity(response)
                .header("email",headers.getHeaderString("email"))
                .header("session_id",headers.getHeaderString("session_id"))
                .header("transaction_id",headers.getHeaderString("transaction_id"))
                .build();
    }


}

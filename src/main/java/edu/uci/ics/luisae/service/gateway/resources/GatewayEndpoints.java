package edu.uci.ics.luisae.service.gateway.resources;


import edu.uci.ics.luisae.service.gateway.GatewayService;
import edu.uci.ics.luisae.service.gateway.core.ProcessRequest;
import edu.uci.ics.luisae.service.gateway.database.Database;
import edu.uci.ics.luisae.service.gateway.logger.ServiceLogger;
import edu.uci.ics.luisae.service.gateway.models.GatewayResponse;
import edu.uci.ics.luisae.service.gateway.models.LoginAndSessionResponse;
import edu.uci.ics.luisae.service.gateway.servicetalking.ServiceTalking;
import edu.uci.ics.luisae.service.gateway.threadpool.HTTPMethod;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.Connection;

@Path("g")
public class GatewayEndpoints {

    @Path("idm/register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiRegister(byte[] jsonBytes, @Context HttpHeaders headers){
        ServiceLogger.LOGGER.info("Entering gateway idm register Endpoint");
        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getIdmPath(),
                GatewayService.getIdmConfigs().getRegisterPath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("idm/login")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiLogin(byte[] jsonBytes, @Context HttpHeaders headers) {
        ServiceLogger.LOGGER.info("Entering gateway idm login Endpoint");
        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getIdmPath(),
                GatewayService.getIdmConfigs().getLoginPath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("idm/session")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiSession(byte[] jsonBytes, @Context HttpHeaders headers){
        ServiceLogger.LOGGER.info("Entering gateway idm session Endpoint");
        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getIdmPath(),
                GatewayService.getIdmConfigs().getSessionPath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("idm/privilege")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiPrivilege(byte[] jsonBytes, @Context HttpHeaders headers){
        ServiceLogger.LOGGER.info("Entering gateway idm privilege Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null)
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getIdmPath(),
                GatewayService.getIdmConfigs().getPrivilegePath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("movies/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiMovieSearch(@Context HttpHeaders headers, @Context UriInfo uriInfo){
        ServiceLogger.LOGGER.info("Entering gateway movies search Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null) {
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getMoviesPath(),
                GatewayService.getMoviesConfigs().getSearchPath(),
                HTTPMethod.GET,
                uriInfo.getQueryParameters(),
                null);
    }

    @Path("movies/get/{movie_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiMoviesGet(@Context HttpHeaders headers, @PathParam("movie_id")String movie_id){
        ServiceLogger.LOGGER.info("Entering gateway movies getMovieId Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null) {
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getMoviesPath(),
                GatewayService.getMoviesConfigs().getGetPath()+movie_id,
                HTTPMethod.GET,
                null,
                null);
    }

    @Path("movies/browse/{phrase}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiMovieGetPhrase(@Context HttpHeaders headers, @Context UriInfo uriInfo, @PathParam("phrase")String phrase){
        ServiceLogger.LOGGER.info("Entering gateway movies browsePhrase Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getMoviesPath(),
                GatewayService.getMoviesConfigs().getBrowsePath()+phrase,
                HTTPMethod.GET,
                uriInfo.getQueryParameters(),
                null);
    }

    @Path("movies/thumbnail")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiMovieGetThumbnail(@Context HttpHeaders headers, byte[] jsonBytes){
        ServiceLogger.LOGGER.info("Entering gateway movies thumbnail Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getMoviesPath(),
                GatewayService.getMoviesConfigs().getThumbnailPath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("movies/people")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiMovieGetPeople(@Context HttpHeaders headers, @Context UriInfo uriInfo){
        ServiceLogger.LOGGER.info("Entering gateway movies MoviesByPeople Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getMoviesPath(),
                GatewayService.getMoviesConfigs().getPeoplePath(),
                HTTPMethod.GET,
                uriInfo.getQueryParameters(),
                null);
    }

    @Path("movies/people/search")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiMoviePeopleSearch(@Context HttpHeaders headers, @Context UriInfo uriInfo){
        ServiceLogger.LOGGER.info("Entering gateway movies peopleSearch Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getMoviesPath(),
                GatewayService.getMoviesConfigs().getPeopleSearchPath(),
                HTTPMethod.GET,
                uriInfo.getQueryParameters(),
                null);
    }

    @Path("movies/people/get/{person_id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiMovieGetPersonId(@Context HttpHeaders headers, @Context UriInfo uriInfo, @PathParam("person_id") String person_id){
        ServiceLogger.LOGGER.info("Entering gateway movies PersonId Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getMoviesPath(),
                GatewayService.getMoviesConfigs().getPeopleGetPath()+person_id,
                HTTPMethod.GET,
                uriInfo.getQueryParameters(),
                null);
    }


    @Path("movies/random")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiMovieGetRandom(@Context HttpHeaders headers, @Context UriInfo uriInfo){
        ServiceLogger.LOGGER.info("Entering gateway movies Random Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getMoviesPath(),
                GatewayService.getMoviesConfigs().getRandomPath(),
                HTTPMethod.GET,
                uriInfo.getQueryParameters(),
                null);
    }

    @Path("billing/cart/insert")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiBillingCartInsert(@Context HttpHeaders headers,
                                         byte[] jsonBytes){
        ServiceLogger.LOGGER.info("Entering gateway billing insert Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getBillingPath(),
                GatewayService.getBillingConfigs().getCartInsertPath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("billing/cart/update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiBillingCartUpdate(@Context HttpHeaders headers,
                                         byte[] jsonBytes){
        ServiceLogger.LOGGER.info("Entering gateway billing update Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getBillingPath(),
                GatewayService.getBillingConfigs().getCartUpdatePath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("billing/cart/delete")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiBillingCartDelete(@Context HttpHeaders headers,
                                         byte[] jsonBytes){
        ServiceLogger.LOGGER.info("Entering gateway billing delete Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getBillingPath(),
                GatewayService.getBillingConfigs().getCartDeletePath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("billing/cart/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiBillingCartRetrieve(@Context HttpHeaders headers,
                                           byte[] jsonBytes){
        ServiceLogger.LOGGER.info("Entering gateway billing retrieve Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getBillingPath(),
                GatewayService.getBillingConfigs().getCartRetrievePath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("billing/cart/clear")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiBillingCartClear(@Context HttpHeaders headers,
                                        byte[] jsonBytes){
        ServiceLogger.LOGGER.info("Entering gateway billing clear Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getBillingPath(),
                GatewayService.getBillingConfigs().getCartClearPath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("billing/order/place")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiBillingOrderPlace(@Context HttpHeaders headers,
                                         byte[] jsonBytes){
        ServiceLogger.LOGGER.info("Entering gateway billing order place Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getBillingPath(),
                GatewayService.getBillingConfigs().getOrderPlacePath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("billing/order/retrieve")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiBillingOrderRetrieve(@Context HttpHeaders headers,
                                            byte[] jsonBytes){
        ServiceLogger.LOGGER.info("Entering gateway billing order retrieve Endpoint");
        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
        if(sessionInfo != null){
            ServiceLogger.LOGGER.warning("Session Error: " + sessionInfo.getResultCode());
            return ProcessRequest.sendBackIdmResponse(sessionInfo, headers);
        }

        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getBillingPath(),
                GatewayService.getBillingConfigs().getOrderRetrievePath(),
                HTTPMethod.POST,
                null,
                jsonBytes);
    }

    @Path("billing/order/complete")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response apiBillingOrderComplete(@Context UriInfo uriInfo,
                                            @Context HttpHeaders headers){
        ServiceLogger.LOGGER.info("Entering gateway billing complete Endpoint");
        return ProcessRequest.createAndPutRequestIntoQueue(headers,
                ServiceTalking.getBillingPath(),
                GatewayService.getBillingConfigs().getOrderCompletePath(),
                HTTPMethod.GET,
                uriInfo.getQueryParameters(),
                null);
    }


    @Path("report")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response report(@Context HttpHeaders headers) {
//        LoginAndSessionResponse sessionInfo = ServiceTalking.sessionCheck(headers);
//        if (sessionInfo != null) {
//            ServiceLogger.LOGGER.info("status: " + sessionInfo.getResult().getStatus().toString());
//            ServiceLogger.LOGGER.info("status: " + sessionInfo.getResult());
//            return Response.status(sessionInfo.getResult().getStatus()).entity(sessionInfo)
//                    .header("email", headers.getHeaderString("email"))
//                    .header("session_id", headers.getHeaderString("session_id"))
//                    .header("transaction_id", headers.getHeaderString("transaction_id"))
//                    .build();
//        }
        Connection con = GatewayService.getConnectionPoolManager().requestCon();
        GatewayResponse response = Database.findResponse(headers.getHeaderString("transaction_id")
                        .replace("\"", ""),
                con);
        GatewayService.getConnectionPoolManager().releaseCon(con);
        if (response == null) {
            return Response.status(Response.Status.NO_CONTENT)
                    .header("transaction_id", headers.getHeaderString("transaction_id"))
                    .header("request_delay", GatewayService.getThreadConfigs().getRequestDelay())
                    .header("message", "Please wait...").build();
        }
        ServiceLogger.LOGGER.info("Finished Entire Process");
        return Response.status(response.getHttp_status()).entity(response.getResponse())
                .header("email", headers.getHeaderString("email"))
                .header("session_id", headers.getHeaderString("session_id"))
                .header("transaction_id", headers.getHeaderString("transaction_id"))
                .build();
    }


}

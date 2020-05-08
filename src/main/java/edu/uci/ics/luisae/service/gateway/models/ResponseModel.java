package edu.uci.ics.luisae.service.gateway.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.luisae.service.gateway.logger.ServiceLogger;

import javax.ws.rs.core.Response;

public abstract class ResponseModel {
    @JsonIgnore
    private Result result;

    public ResponseModel() { }

    public ResponseModel(Result result)
    {
        this.result = result;
    }

    @JsonProperty("resultCode")
    public int getResultCode()
    {
        return result.getResultCode();
    }

    @JsonProperty("message")
    public String getMessage()
    {
        return result.getMessage();
    }



    @JsonIgnore
    public Result getResult()
    {
        return result;
    }

    @JsonIgnore
    public void setResult(Result result)
    {
        this.result = result;
    }

    @JsonIgnore
    public Response buildResponse()
    {
        ServiceLogger.LOGGER.info("Response being built with Result: " + result);

        if (result == null || result.getStatus() == Response.Status.INTERNAL_SERVER_ERROR)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        return Response.status(result.getStatus()).entity(this).build();
    }
    @JsonIgnore
    public Response buildResponseWithHeaders(String email, String session_id, String transaction_id)
    {
        ServiceLogger.LOGGER.info("Response being built with Result: " + result);
        if(result == null)
            ServiceLogger.LOGGER.warning("result is NULL");

        if (result == null || result.getStatus() == Response.Status.INTERNAL_SERVER_ERROR) {
            ServiceLogger.LOGGER.warning("ENTERED WARNING");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        return Response.status(result.getStatus()).entity(this)
                .header("email",email).
                header("session_id", session_id)
                .header("transaction_id",transaction_id)
                .build();
    }
}

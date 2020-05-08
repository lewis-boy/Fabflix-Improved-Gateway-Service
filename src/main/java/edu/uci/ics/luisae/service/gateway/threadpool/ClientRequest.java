package edu.uci.ics.luisae.service.gateway.threadpool;

import edu.uci.ics.luisae.service.gateway.logger.ServiceLogger;

import javax.ws.rs.core.MultivaluedMap;

public class ClientRequest
{
    /* User Information */
    private String email;
    private String session_id;
    private String transaction_id;

    /* Target Service and Endpoint */
    private String URI;
    private String endpoint;
    private HTTPMethod method;

    private MultivaluedMap<String, String> queryParams;
    private byte[] jsonBytes;

    /*
     * So before when we wanted to get the request body
     * we would grab it as a String (String jsonText).
     *
     * The Gateway however does not need to see the body
     * but simply needs to pass it. So we save ourselves some
     * time and overhead by grabbing the request as a byte array
     * (byte[] jsonBytes).
     *
     * This way we can just act as a
     * messenger and just pass along the bytes to the target
     * service and it will do the rest.
     *
     * for example:
     *
     * where we used to do this:
     *
     *     @Path("hello")
     *     ...ect
     *     public Response hello(String jsonString) {
     *         ...ect
     *     }
     *
     * do:
     *
     *     @Path("hello")
     *     ...ect
     *     public Response hello(byte[] jsonBytes) {
     *         ...ect
     *     }
     *
     */
    private byte[] requestBytes;

    public ClientRequest(String email, String session_id, String transaction_id, String URI,
                         String endpoint, HTTPMethod method,
                         MultivaluedMap<String, String> queryParams, byte[] jsonBytes)
    {
        this.email = email;
        this.session_id = session_id;
        this.transaction_id = transaction_id;
        this.URI = URI;
        this.endpoint = endpoint;
        this.method = method;
        this.queryParams = queryParams;
        this.jsonBytes = jsonBytes;
    }


    public MultivaluedMap<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(MultivaluedMap<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public byte[] getJsonBytes() {
        return jsonBytes;
    }

    public void setJsonBytes(byte[] jsonBytes) {
        this.jsonBytes = jsonBytes;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getURI() {
        return URI;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public void setMethod(HTTPMethod method) {
        this.method = method;
    }

    public byte[] getRequestBytes() {
        return requestBytes;
    }

    public void setRequestBytes(byte[] requestBytes) {
        this.requestBytes = requestBytes;
    }

    public void print(){
        ServiceLogger.LOGGER.info("email: " + this.email);
        ServiceLogger.LOGGER.info("session_id: " + this.session_id);
        ServiceLogger.LOGGER.info("transaction_id: " + this.transaction_id);
        ServiceLogger.LOGGER.info("URI: " + this.URI);
        ServiceLogger.LOGGER.info("endpoint: " + this.endpoint);
        ServiceLogger.LOGGER.info("type of method: " + this.method.toString());
    }

    public void printPath(){
        ServiceLogger.LOGGER.info(this.URI + this.endpoint);
    }
}

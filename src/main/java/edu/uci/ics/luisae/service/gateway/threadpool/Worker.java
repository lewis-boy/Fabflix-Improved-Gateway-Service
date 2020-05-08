package edu.uci.ics.luisae.service.gateway.threadpool;

import edu.uci.ics.luisae.service.gateway.GatewayService;
import edu.uci.ics.luisae.service.gateway.core.ProcessRequest;
import edu.uci.ics.luisae.service.gateway.database.Database;
import edu.uci.ics.luisae.service.gateway.logger.ServiceLogger;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.sql.Connection;

public class Worker extends Thread {
    int id;
    ThreadPool threadPool;

    private Worker(int id, ThreadPool threadPool) {
        this.id = id;
        this.threadPool = threadPool;
    }

    public static Worker CreateWorker(int id, ThreadPool threadPool) {
        return new Worker(id, threadPool);
    }

    public void process(ClientRequest request, Connection con) {
        ServiceLogger.LOGGER.info("Worker "+ this.id + " Processing");
        request.print();

        Response response;
        Invocation.Builder invocationBuilder;
        Client client = ClientBuilder.newClient();
        client.register(JacksonFeature.class);
        WebTarget webTarget = client.target(request.getURI()).path(request.getEndpoint());
//        ServiceLogger.LOGGER.info("Worker #" + this.id + " URI destination: ");
        if(request.getMethod().toString() == "GET") {
            response = ProcessRequest.get(webTarget, request);
        }
        else if(request.getMethod().toString() == "POST")
            response = ProcessRequest.post(webTarget, request);
        else
            response = null;
        if(response.getStatus() == 500) {
            ServiceLogger.LOGGER.severe("Grizzly Connection Error");
            Database.insertResponse(request,con);
        }
        else {
            ServiceLogger.LOGGER.info("Response code: " + response.getStatus());
            Database.insertResponse(response, request, con);
        }
    }

    @Override
    public void run() {
        while (true) {
            ClientRequest request = this.threadPool.takeRequest();
            Connection con = GatewayService.getConnectionPoolManager().requestCon();
            process(request, con);
            ServiceLogger.LOGGER.info("Worker " + this.id + " is done processing");
            GatewayService.getConnectionPoolManager().releaseCon(con);
        }
    }
}

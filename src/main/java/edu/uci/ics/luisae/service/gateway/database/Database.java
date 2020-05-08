package edu.uci.ics.luisae.service.gateway.database;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import edu.uci.ics.luisae.service.gateway.GatewayService;
import edu.uci.ics.luisae.service.gateway.logger.ServiceLogger;
import edu.uci.ics.luisae.service.gateway.models.GatewayResponse;
import edu.uci.ics.luisae.service.gateway.threadpool.ClientRequest;
import edu.uci.ics.luisae.service.gateway.util.Param;
import edu.uci.ics.luisae.service.gateway.util.Util;

import java.sql.*;

public class Database {
    public static void insertResponse(Response response, ClientRequest request, Connection con){
        int updated = 0;
        String query = "INSERT INTO responses (transaction_id,email,session_id,response,http_status)" +
                " VALUES(?,?,?,?,?);";
        Param[] params = new Param[]{
                Param.create(Types.VARCHAR, request.getTransaction_id()),
                Param.create(Types.VARCHAR, request.getEmail()),
                Param.create(Types.VARCHAR, request.getSession_id()),
                Param.create(Types.VARCHAR, response.readEntity(String.class)),
                Param.create(Types.INTEGER, response.getStatus())
        };
        try{
            PreparedStatement ps = Util.prepareStatement(query,params,con);
            updated = ps.executeUpdate();
        }
        catch(SQLException e ){
            ServiceLogger.LOGGER.warning("SQLException in Database insertResponse with ClientRequest\n" + e.getMessage());
        }
    }

    public static void insertResponse(Response response, HttpHeaders headers, Connection con){
        int updated = 0;
        Param[] params;
        String query = "INSERT INTO responses (transaction_id,email,session_id,response,http_status)" +
                " VALUES(?,?,?,?,?);";
        ServiceLogger.LOGGER.info("entity: " + response.getEntity().toString());
        if(response.getStatus() == 500){
            params = new Param[]{
                    Param.create(Types.VARCHAR, headers.getHeaderString("transaction_id")),
                    Param.create(Types.VARCHAR, headers.getHeaderString("email")),
                    Param.create(Types.VARCHAR, headers.getHeaderString("session_id")),
                    Param.create(Types.VARCHAR, response.getEntity()),
                    Param.create(Types.INTEGER, response.getStatus())
            };
        }
        else {
            params = new Param[]{
                    Param.create(Types.VARCHAR, headers.getHeaderString("transaction_id")),
                    Param.create(Types.VARCHAR, headers.getHeaderString("email")),
                    Param.create(Types.VARCHAR, headers.getHeaderString("session_id")),
                    Param.create(Types.VARCHAR, response.readEntity(String.class)),
                    Param.create(Types.INTEGER, response.getStatus())
            };
        }
        //Param.create(Types.VARCHAR, response.readEntity(String.class)),
        try{
            PreparedStatement ps = Util.prepareStatement(query,params,con);
            updated = ps.executeUpdate();
        }
        catch(SQLException e ){
            ServiceLogger.LOGGER.warning("SQLException in Database insertResponse with Headers\n" + e.getMessage());
        }
    }

    public static void insertResponse(ClientRequest request, Connection con){
        int updated = 0;
        Param[] params;
        String query = "INSERT INTO responses (transaction_id,email,session_id,response,http_status)" +
                " VALUES(?,?,?,?,?);";
            params = new Param[]{
                    Param.create(Types.VARCHAR, request.getTransaction_id()),
                    Param.create(Types.VARCHAR, request.getEmail()),
                    Param.create(Types.VARCHAR, request.getSession_id()),
                    Param.create(Types.VARCHAR, "Connection error with SQL. Connection refused"),
                    Param.create(Types.INTEGER, 500)
            };
        //Param.create(Types.VARCHAR, response.readEntity(String.class)),
        try{
            PreparedStatement ps = Util.prepareStatement(query,params,con);
            updated = ps.executeUpdate();
        }
        catch(SQLException e ){
            ServiceLogger.LOGGER.warning("SQLException in Database insertResponse with no Response \n" + e.getMessage());
        }
    }


    public static GatewayResponse findResponse(String tid, Connection connection){
        Connection con = connection;
        GatewayResponse returnResponse;
        String query = "SELECT JSON_OBJECT('transaction_id',transaction_id,'response',response,'http_status',http_status)\n" +
                "    as responseFound FROM responses WHERE transaction_id =?;";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,tid);
            ServiceLogger.LOGGER.info(ps.toString());
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                //ran into an error, we cannot rs.get whatever after we close the connection
                returnResponse = Util.modelMapper(rs.getString("responseFound"), GatewayResponse.class);
                deleteResponse(tid, con);
                GatewayService.getConnectionPoolManager().releaseCon(con);
                return returnResponse;
            }else{
                GatewayService.getConnectionPoolManager().releaseCon(con);
                return null;}

        }catch (SQLException e){
            ServiceLogger.LOGGER.warning("SQLException in Database findResponse\n" + e.getMessage());
            GatewayService.getConnectionPoolManager().releaseCon(con);
            return null;
        }

    }

    private static void deleteResponse(String tid, Connection con){
        String query = "DELETE FROM responses WHERE transaction_id = ?;";
        try{
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1,tid);
            ps.executeUpdate();
        }catch(SQLException e){
            ServiceLogger.LOGGER.warning("SQLException in Database deleteResponse\n" + e.getMessage());
        }
    }









}

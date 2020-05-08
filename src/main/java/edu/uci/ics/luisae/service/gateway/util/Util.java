package edu.uci.ics.luisae.service.gateway.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.uci.ics.luisae.service.gateway.logger.ServiceLogger;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Util {
    public static PreparedStatement prepareStatement(String query, Param[] paramList, Connection con)
            throws SQLException
    {
        int count = 1;
        PreparedStatement ps = con.prepareStatement(query);
        for (Param param : paramList)
            ps.setObject(count++, param.getParam(), param.getType());
        ServiceLogger.LOGGER.info("Query prepared from prepareStatement: " + ps.toString());
        return ps;
    }

    public static <T> T modelMapper(String jsonString, Class<T> className)
    {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, className);
        } catch (IOException e) {
            ServiceLogger.LOGGER.severe("Mapping Object Failed IO Exception: " + e.getMessage());
            ServiceLogger.LOGGER.severe("Tried to map this jsonString: " + jsonString);
            return null;
        }
    }
}

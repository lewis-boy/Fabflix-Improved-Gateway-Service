package edu.uci.ics.luisae.service.gateway.connectionpool;

import com.zaxxer.hikari.HikariConfig;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;
import edu.uci.ics.luisae.service.gateway.logger.ServiceLogger;

public class ConnectionPoolManager
{
    private HikariDataSource hikariConPool;

    private ConnectionPoolManager(HikariConfig hikariConfigs)
    {
        this.hikariConPool = new HikariDataSource(hikariConfigs);
    }

    /*
     * Hikari Connection Pools are built by creating a HikariDataSource
     * supplied with a HikariConfig. There are plenty of config options
     * available to us but for now we will just use:
     *
     *      setJdbcUrl()
     *      setUsername()
     *      setPassword()
     *      setMaximumPoolSize()
     *
     * Hikari allows you to use either a JdbcUrl or a driver to create
     * the pool. To match what we have been doing in the past we will
     * be creating the pool using the JdbcUrl
     *
     * Because HikariDataSource is the actual pool we call this class
     * ConnectionPoolManager that allows us to create a wrapper around
     * it to handle some instantiation and management of the pool.
     */
    public static ConnectionPoolManager createConPool(String url, String username, String password, int numCons)
    {
        HikariConfig hikariConfigs = new HikariConfig();

        hikariConfigs.setJdbcUrl(url);
        hikariConfigs.setUsername(username);
        hikariConfigs.setPassword(password);
        hikariConfigs.setMaximumPoolSize(numCons);

        return new ConnectionPoolManager(hikariConfigs);
    }

    /*
     * Note that our connection pool (Hikari) will limit the amount of
     * connections allowed and needs the caller to "release" the
     * connection and give it back to the connection pool. Make sure
     * to properly release the connections or there will be
     * resource leaks.
     *
     * Make sure to release connections in case of errors!!!
     * Once you request a connection it is up to you to give it back
     * to the connection pool
     */
    public Connection requestCon()
    {
        // TODO request connections from hikariConPool
        try{
            Connection connection = hikariConPool.getConnection();
            return connection;
        }
        catch(SQLException e){
            ServiceLogger.LOGGER.severe(e.getMessage());
            return null;
        }
    }

    public void releaseCon(Connection con)
    {
        // TODO release connections back to hikariConPool
        hikariConPool.evictConnection(con);
    }
}

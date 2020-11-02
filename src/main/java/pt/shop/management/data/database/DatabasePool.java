package pt.shop.management.data.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

/**
 * Database Pool Class
 *
 * @author Hugo Silva
 * @version 2020-10-28
 */

public class DatabasePool {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(DatabasePool.class.getName());
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url =
            "jdbc:mysql://projecthub.hopto.org:3306/management?useTimezone=true&serverTimezone=UTC";
    private static final String user = "admin";
    private static final String pass = "dbpw";
    // Data source object
    private static ComboPooledDataSource dataSource;

    static {
        try {
            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass(driver);
            dataSource.setJdbcUrl(url);
            dataSource.setUser(user);
            dataSource.setPassword(pass);
            dataSource.setMinPoolSize(5);
            dataSource.setMaxStatements(10);
            dataSource.setMaxStatementsPerConnection(1);
        } catch (PropertyVetoException ex) {
            LOGGER.log(Level.ERROR, "PropertyVetoException occurred {}", ex);
        }
    }

    /**
     * Get data source instance
     *
     * @return data source instance
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
}
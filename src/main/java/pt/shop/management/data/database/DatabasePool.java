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
 * @version 2020-10-25
 */

public class DatabasePool {

    // Logger
    private static final Logger LOGGER = LogManager.getLogger(DatabasePool.class.getName());
    // Data source object
    private static ComboPooledDataSource dataSource;

    static {
        try {

            dataSource = new ComboPooledDataSource();
            dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://192.168.0.199:3306/management?useTimezone=true&serverTimezone=UTC");
            dataSource.setUser("admin");
            dataSource.setPassword("dbpw");
            dataSource.setMinPoolSize(100);
            dataSource.setMaxPoolSize(1000);
            dataSource.setMaxIdleTime(10000);
            dataSource.setAcquireRetryAttempts(0);
            dataSource.setAcquireRetryDelay(500);
            dataSource.setAcquireIncrement(5);
        } catch (PropertyVetoException ex) {
            LOGGER.log(Level.ERROR, "Exception occurred {}", ex);
        }
    }

    /**
     * Get data source instance
     *
     * @return - data source instance
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
}
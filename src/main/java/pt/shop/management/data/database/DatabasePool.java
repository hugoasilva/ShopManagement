package pt.shop.management.data.database;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

/**
 * Database Pool Class
 *
 * @author Hugo Silva
 * @version 2020-10-25
 */

public class DatabasePool {
    private static final BasicDataSource dataSource;

    static {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://projecthub.hopto.org:3306/management?useTimezone=true&serverTimezone=UTC");
        dataSource.setUsername("admin");
        dataSource.setPassword("dbpw");
        dataSource.setInitialSize(50);
        dataSource.setMaxIdle(20);
        dataSource.setMaxTotal(100);
        dataSource.setMaxWaitMillis(10000);
        dataSource.setMaxOpenPreparedStatements(100);
    }

    private DatabasePool() {
    }

    public static DataSource getConnection() {
        return dataSource;
    }
}
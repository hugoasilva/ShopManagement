package pt.shop.management.data.database;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

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
        dataSource.setInitialSize(10);
        dataSource.setMaxTotal(10);

    }

    private DatabasePool() {
    }

    public static DataSource getConnection() throws SQLException {
        return dataSource;
    }
}
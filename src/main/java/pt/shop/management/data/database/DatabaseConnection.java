package pt.shop.management.data.database;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {
    public void main(String[] args) {
        DatabaseConnection dsCon = new DatabaseConnection();
        try {
            dsCon.displayEmployee(37);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void displayEmployee(int id) throws SQLException {
        Connection connection = null;
        String selectSQL = "Select * from employees";
        PreparedStatement prepStmt = null;
        try {
            DataSource ds = DatabasePool.getConnection();
            // getting connection
            connection = ds.getConnection();
            prepStmt = connection.prepareStatement(selectSQL);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id") + " Name: "
                        + rs.getString("name") + " address: " + rs.getInt("address"));
            }
        } finally {
            if (prepStmt != null) {
                prepStmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }
}
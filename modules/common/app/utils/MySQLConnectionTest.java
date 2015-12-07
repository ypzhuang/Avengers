package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ypzhuang on 15/12/7.
 */
public class MySQLConnectionTest {

    public static void main(String args[]) {

        String url = "jdbc:mysql://127.0.0.1:3306/playdb";
        String username = "root";
        String password = "root";

        System.out.println("Connecting database...");

        try {
           Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }

    }

}

package org.example.eurovision_manager.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String DBURL = "jdbc:postgresql://localhost:5432/eurovision";
    private static final String USER = "postgres";
    private static final String PASS = "budinqtz21";

    private static final ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection() {
        try {
            return DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Fail connecting to database!");
            e.printStackTrace();
            return null;
        }
    }

    public static Connection getConnection() {
        return singleInstance.createConnection();
    }
}
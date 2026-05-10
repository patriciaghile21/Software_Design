package org.example.eurovision_manager.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String DRIVER = "org.postgresql.Driver";
    private static final String DBURL = "jdbc:postgresql://localhost:5432/eurovision_catalog";
    private static final String USER = "postgres";
    private static final String PASS = "budinqtz21";

    private static final ConnectionFactory singleInstance = new ConnectionFactory();

    private Connection connection;

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
            this.connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Fail connecting to database!");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (singleInstance.connection == null || singleInstance.connection.isClosed()) {
                singleInstance.connection = DriverManager.getConnection(DBURL, USER, PASS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return singleInstance.connection;
    }
}
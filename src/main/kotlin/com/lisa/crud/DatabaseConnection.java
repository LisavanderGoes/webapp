package com.lisa.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private Connection conn = null;

    public Connection getDatabaseConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e);
        }

        final String DATBASE_NAME = "webapp";
        final String DB_URL = "jdbc:mysql://localhost:3306/"+ DATBASE_NAME;
        final String USER = "root";
        final String PASS = "";

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Could not make connection: " + e);
        }
        return conn;
    }
}


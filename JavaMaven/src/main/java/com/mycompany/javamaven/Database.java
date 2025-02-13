/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.javamaven;

/**
 *
 * @author asus
 */
//


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;


    public static void connectToDatabase() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/studentdb";
        String user = "root";
        String password = "29102001"; 
        connection = DriverManager.getConnection(url, user, password);
        System.out.println("Connected to the database.");
    }

 
    public static void disconnectDatabase() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.err.println("Error disconnecting from the database: " + e.getMessage());
        }
    }
    public static Connection getConnection() {
        return connection;
    }
}

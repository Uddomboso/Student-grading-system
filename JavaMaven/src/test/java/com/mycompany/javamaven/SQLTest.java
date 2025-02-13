///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
// */
//package com.mycompany.javamaven;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.*;
//import java.sql.*;
//
///**
// *
// * @author asus
// */
//
//public class Test {
//
//    @BeforeAll
//    public static void setUpClass() throws SQLException {
//  
//        SQL.connectToDatabase();
//        System.out.println("Connected to test database.");
//    }
//
//    @AfterAll
//    public static void tearDownClass() {
//      
//        SQL.disconnectDatabase();
//        System.out.println("Disconnected from test database.");
//    }
//
//    @BeforeEach
//    public void setUp() {
//    
//        try {
//            SQL.addStudent(1, "T12345", "John", "Doe", "M", "USA", "2000-01-01");
//        } catch (SQLException e) {
//            fail("Failed to set up test data: " + e.getMessage());
//        }
//    }
//
//    @AfterEach
//    public void tearDown() {
//    
//        try {
//            SQL.deleteStudent(1); 
//        } catch (SQLException e) {
//            System.err.println("Failed to clean up test data: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testConnectToDatabase() {
//   
//        assertDoesNotThrow(() -> SQL.connectToDatabase());
//    }
//
//    @Test
//    public void testDisconnectDatabase() {
//        assertDoesNotThrow(() -> SQL.disconnectDatabase());
//    }
//
//    @Test
//    public void testAddStudent() {
//        System.out.println("Testing addStudent...");
//        try {
//            SQL.addStudent(2, "T67890", "Jane", "Smith", "F", "UK", "2001-02-02");
//
//          
//            String query = "SELECT * FROM students WHERE id = 2";
//            try (Statement stmt = SQL.getConnection().createStatement();
//                 ResultSet rs = stmt.executeQuery(query)) {
//
//                assertTrue(rs.next(), "Student with ID 2 should exist");
//                assertEquals("Jane", rs.getString("name"), "Name should be Jane");
//
//            } catch (SQLException e) {
//                fail("SQL exception occurred: " + e.getMessage());
//            } finally {
//                SQL.deleteStudent(2); 
//            }
//
//        } catch (SQLException e) {
//            fail("Error adding student: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testEditStudent() {
//        System.out.println("Testing editStudent...");
//        try {
//            SQL.editStudent(1, "T12345", "John", "Doe", "M", "Canada", "2000-01-01");
//
//
//            String query = "SELECT * FROM students WHERE id = 1";
//            try (Statement stmt = SQL.getConnection().createStatement();
//                 ResultSet rs = stmt.executeQuery(query)) {
//
//                assertTrue(rs.next(), "Student with ID 1 should exist");
//                assertEquals("Canada", rs.getString("nationality"), "Nationality should be updated to Canada");
//
//            } catch (SQLException e) {
//                fail("SQL exception occurred: " + e.getMessage());
//            }
//
//        } catch (SQLException e) {
//            fail("Error editing student: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testDeleteStudent() {
//        System.out.println("Testing deleteStudent...");
//        try {
//            SQL.addStudent(3, "T54321", "Mark", "Taylor", "M", "Australia", "1999-12-12");
//
//            SQL.deleteStudent(3);
//
//            String query = "SELECT * FROM students WHERE id = 3";
//            try (Statement stmt = SQL.getConnection().createStatement();
//                 ResultSet rs = stmt.executeQuery(query)) {
//
//                assertFalse(rs.next(), "Student with ID 3 should not exist");
//
//            } catch (SQLException e) {
//                fail("SQL exception occurred: " + e.getMessage());
//            }
//
//        } catch (SQLException e) {
//            fail("Error deleting student: " + e.getMessage());
//        }
//    }
//
//    @Test
//    public void testListStudents() {
//        System.out.println("Testing listStudents...");
//        try {
//            SQL.listStudents();
//
//        } catch (SQLException e) {
//            fail("Error listing students: " + e.getMessage());
//        }
//    }
//}

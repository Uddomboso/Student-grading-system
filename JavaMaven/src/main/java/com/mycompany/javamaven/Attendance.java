/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author asus
 */
package com.mycompany.javamaven;
import java.io.BufferedReader;
import java.sql.*;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Attendance {


    int att_id;
    int id;  
    int crs_id;  
    Date att_date;  


    public Attendance(int att_id, int id, int crs_id, Date att_date) {
        this.att_id = att_id;
        this.id = id;
        this.crs_id = crs_id;
        this.att_date = att_date;
    }

       public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "29102001");
    }

 
    public void setAttId(int att_id) { this.att_id = att_id; }
    public void setId(int id) { this.id = id; }
    public void setCrsId(int crs_id) { this.crs_id = crs_id; }
    public void setAttDate(Date att_date) { this.att_date = att_date; }

    public int getAttId() { return att_id; }
    public int getId() { return id; }
    public int getCrsId() { return crs_id; }
    public Date getAttDate() { return att_date; }

 
    public static Attendance readAttendance() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Attendance ID: ");
        int attId = sc.nextInt();
        System.out.print("Enter Student ID: ");
        int studentId = sc.nextInt();
        System.out.print("Enter Course ID: ");
        int courseId = sc.nextInt();
        sc.nextLine();  
        System.out.print("Enter Attendance Date (yyyy-mm-dd): ");
        String dateString = sc.nextLine();
        Date attDate = Date.valueOf(dateString);  

        return new Attendance(attId, studentId, courseId, attDate);
    }

    
    public static void displayAttendance(Connection conn) {
        String query = "SELECT * FROM attendance";  

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Attendance ID | Student ID | Course ID | Date");
            System.out.println("---------------------------------------------");

            while (rs.next()) {
                int attId = rs.getInt("att_id");
                int studentId = rs.getInt("id");
                int courseId = rs.getInt("crs_id");
                Date attDate = rs.getDate("att_date");


                Attendance at = new Attendance(attId, studentId, courseId, attDate);

                System.out.println(at.getAttId() + " | " + at.getId() + " | " + at.getCrsId() + " | " + at.getAttDate());
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving attendance records: " + e.getMessage());
        }
    }

 
    public static void addAttendanceToDatabase(Connection conn) {
  
        Attendance at = readAttendance(); 
        String query = "INSERT INTO attendance (att_id, id, crs_id, att_date) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, at.getAttId());  
            stmt.setInt(2, at.getId());  
            stmt.setInt(3, at.getCrsId());  
            stmt.setDate(4, at.getAttDate());  

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Attendance record added successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting attendance: " + e.getMessage());
        }
    }


    public static void updateAttendance(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Attendance ID to update: ");
        int attId = sc.nextInt();
        System.out.print("Enter new Student ID: ");
        int studentId = sc.nextInt();
        System.out.print("Enter new Course ID: ");
        int courseId = sc.nextInt();
        sc.nextLine(); 
        System.out.print("Enter new Attendance Date (yyyy-mm-dd): ");
        String dateString = sc.nextLine();
        Date attDate = Date.valueOf(dateString);  

        String query = "UPDATE attendance SET id = ?, crs_id = ?, att_date = ? WHERE att_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, studentId);  
            stmt.setInt(2, courseId);  
            stmt.setDate(3, attDate);  
            stmt.setInt(4, attId); 

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Attendance record updated successfully.");
            } else {
                System.out.println("No attendance record found with the given ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating attendance: " + e.getMessage());
        }
    }

    
    public static void deleteAttendance(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Attendance ID to delete: ");
        int attId = sc.nextInt();

        String query = "DELETE FROM attendance WHERE att_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, attId);  

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Attendance record with ID " + attId + " has been deleted successfully.");
            } else {
                System.out.println("No attendance record found with the given ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting attendance record: " + e.getMessage());
        }
    }
      
    public static void backupAttendance(Connection conn) {
        String query = "SELECT * FROM attendance";
        String backupFile = "C:\\Users\\asus\\OneDrive\\Desktop\\copied\\src\\main\\java\\com\\mycompany\\javamaven\\attendance_backup.csv";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();
             BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {

            writer.write("att_id,id,crs_id,att_date");  
            writer.newLine();

            while (rs.next()) {
                int attId = rs.getInt("att_id");
                int studentId = rs.getInt("id");
                int courseId = rs.getInt("crs_id");
                Date attDate = rs.getDate("att_date");
                writer.write(attId + "," + studentId + "," + courseId + "," + attDate);  
                writer.newLine();
            }

            System.out.println("Backup successful! Data saved to " + backupFile);

        } catch (SQLException | IOException e) {
            System.err.println("Error backing up attendance: " + e.getMessage());
        }
    }
    
    public static void restoreAttendance(Connection conn) {
    String restoreFile = "C:\\Users\\asus\\OneDrive\\Desktop\\copied\\src\\main\\java\\com\\mycompany\\javamaven\\attendance_backup.csv";
    String query = "INSERT INTO attendance (att_id, id, crs_id, att_date) VALUES (?, ?, ?, ?)";

    try (BufferedReader reader = new BufferedReader(new FileReader(restoreFile));
         PreparedStatement stmt = conn.prepareStatement(query)) {

        String line;
        reader.readLine();

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue; 
            }

            String[] data = line.split(",");
            if (data.length != 4) {
                System.err.println("Skipping malformed line: " + line);
                continue; 
            }

            try {
                int attId = Integer.parseInt(data[0].trim());
                int studentId = Integer.parseInt(data[1].trim());
                int courseId = Integer.parseInt(data[2].trim());
                Date attDate = Date.valueOf(data[3].trim());  

                stmt.setInt(1, attId);
                stmt.setInt(2, studentId);
                stmt.setInt(3, courseId);
                stmt.setDate(4, attDate);

                stmt.executeUpdate();
            } catch (NumberFormatException | SQLException e) {
                System.err.println("Error parsing line: " + line + " -> " + e.getMessage());
            }
        }

        System.out.println("Restore successful! Attendance records have been added to the database.");

    } catch (SQLException | IOException e) {
        System.err.println("Error restoring attendance: " + e.getMessage());
    }
}

}

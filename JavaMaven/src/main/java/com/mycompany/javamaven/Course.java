package com.mycompany.javamaven;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Course {

    private int crs_id;
    private int dept_id;
    private String crs_code;
    private String crs_name;

    public Course(int crs_id, int dept_id, String crs_code, String crs_name) {
        this.crs_id = crs_id;
        this.dept_id = dept_id;
        this.crs_code = crs_code;
        this.crs_name = crs_name;
    }

    public int getCrsId() { return crs_id; }
    public int getDeptId() { return dept_id; }
    public String getCrsCode() { return crs_code; }
    public String getCrsName() { return crs_name; }

    public void setCrsId(int crs_id) { this.crs_id = crs_id; }
    public void setDeptId(int dept_id) { this.dept_id = dept_id; }
    public void setCrsCode(String crs_code) { this.crs_code = crs_code; }
    public void setCrsName(String crs_name) { this.crs_name = crs_name; }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "29102001");
    }
public static void readCourseAndInsert(Scanner sc) {
    try (Connection conn = getConnection()) {
        System.out.print("Input Course ID: ");
        int crs_id = sc.nextInt();

        System.out.print("Input Department ID: ");
        int dept_id = sc.nextInt();
        sc.nextLine(); 

        System.out.print("Input Course Code: ");
        String crs_code = sc.nextLine();

        System.out.print("Input Course Name: ");
        String crs_name = sc.nextLine();

        Course course = new Course(crs_id, dept_id, crs_code, crs_name);

        insertCourseIntoDatabase(course, conn);
    } catch (SQLException e) {
        System.err.println("Error establishing connection: " + e.getMessage());
    }
}

public static void insertCourseIntoDatabase(Course course, Connection conn) {
    String query = "INSERT INTO courses (crs_id, dept_id, crs_code, crs_name) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, course.getCrsId());
        stmt.setInt(2, course.getDeptId());
        stmt.setString(3, course.getCrsCode());
        stmt.setString(4, course.getCrsName());
        stmt.executeUpdate();
        System.out.println("Course added successfully to the database.");
    } catch (SQLException e) {
        System.err.println("Error inserting course: " + e.getMessage());
    }
}


    public static void displayCourses() {
        String query = "SELECT * FROM courses";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            System.out.println("Course ID | Department ID | Course Code | Course Name");
            System.out.println("------------------------------------------------------");
            while (rs.next()) {
                int crs_id = rs.getInt("crs_id");
                int dept_id = rs.getInt("dept_id");
                String crs_code = rs.getString("crs_code");
                String crs_name = rs.getString("crs_name");

                Course course = new Course(crs_id, dept_id, crs_code, crs_name);
                System.out.println(course.getCrsId() + " | " + course.getDeptId() + " | " + course.getCrsCode() + " | " + course.getCrsName());
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving courses: " + e.getMessage());
        }
    }

    public static void userUpdateCourse(Scanner sc) {
        System.out.print("Enter Course ID to update: ");
        int crs_id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new Department ID: ");
        int dept_id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new Course Code: ");
        String crs_code = sc.nextLine();

        System.out.print("Enter new Course Name: ");
        String crs_name = sc.nextLine();

        Course course = new Course(crs_id, dept_id, crs_code, crs_name);
        try (Connection conn = getConnection()) {
            updateCourseInDatabase(course, conn);
        } catch (SQLException e) {
            System.err.println("Error establishing connection: " + e.getMessage());
        }
    }

    public static void updateCourseInDatabase(Course course, Connection conn) {
        String query = "UPDATE courses SET dept_id = ?, crs_code = ?, crs_name = ? WHERE crs_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, course.getDeptId());
            stmt.setString(2, course.getCrsCode());
            stmt.setString(3, course.getCrsName());
            stmt.setInt(4, course.getCrsId());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Course with ID " + course.getCrsId() + " has been updated successfully.");
            } else {
                System.out.println("No course found with ID " + course.getCrsId());
            }
        } catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
        }
    }


    public static void userDeleteCourse(Scanner sc) {
        System.out.print("Enter Course ID to delete: ");
        int crs_id = sc.nextInt();
        try (Connection conn = getConnection()) {
            deleteCourse(conn, crs_id); 
        } catch (SQLException e) {
            System.err.println("Error establishing connection: " + e.getMessage());
        }
    }

    public static void deleteCourse(Connection conn, int crs_id) {
        String query = "DELETE FROM courses WHERE crs_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, crs_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Course with ID " + crs_id + " has been deleted successfully.");
            } else {
                System.out.println("No course found with ID " + crs_id);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
        }
    }

    public static void backupCourses(Connection conn) throws IOException {
     String query = "SELECT * FROM courses"; 
     String backupFile = "course_backup.sql"; 

     try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery();
          FileWriter fw = new FileWriter(backupFile);
          BufferedWriter bw = new BufferedWriter(fw)) {

         while (rs.next()) {
             int crs_id = rs.getInt("crs_id");
             int dept_id = rs.getInt("dept_id");
             String crs_code = rs.getString("crs_code");
             String crs_name = rs.getString("crs_name");

             bw.write("INSERT INTO courses (crs_id, dept_id, crs_code, crs_name) VALUES (" 
                     + crs_id + ", "
                     + dept_id + ", '"
                     + crs_code + "', '"
                     + crs_name + "');\n");
         }
         System.out.println("Backup completed successfully to " + backupFile);

     } catch (SQLException e) {
         System.err.println("Error during backup: " + e.getMessage());
     }
 }

 public static void restoreCourses(Connection conn) {
     String restoreFile = "course_backup.sql"; 

     try (BufferedReader br = new BufferedReader(new FileReader(restoreFile))) {
         String line;
         while ((line = br.readLine()) != null) {
             if (line.trim().startsWith("INSERT INTO")) {
                 try (Statement stmt = conn.createStatement()) {
                     stmt.executeUpdate(line); 
                 }
             }
         }
         System.out.println("Restore completed successfully from " + restoreFile);
     } catch (IOException | SQLException e) {
         System.err.println("Error during restore: " + e.getMessage());
     }
 }

}

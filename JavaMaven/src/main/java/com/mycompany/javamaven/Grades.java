package com.mycompany.javamaven;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Grades {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentdb";
    private static final String USER = "root";
    private static final String PASSWORD = "29102001";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    int grd_id;
    int id;
    int crs_id;
    float grd_mt;
    float grd_hw;
    float grd_final;
    String grd_lgrade;

    public Grades(int grd_id, int id, int crs_id, float grd_mt, float grd_hw, float grd_final) {
        this.grd_id = grd_id;
        this.id = id;
        this.crs_id = crs_id;
        this.grd_mt = grd_mt;
        this.grd_hw = grd_hw;
        this.grd_final = grd_final;
        this.grd_lgrade = null;
    }

    public Grades(int grd_id, int crs_id) {
        this.grd_id = grd_id;
        this.crs_id = crs_id;
        this.grd_mt = 0.0f;
        this.grd_hw = 0.0f;
        this.grd_final = 0.0f;
        this.grd_lgrade = null;
    }

    public void setGrdId(int grd_id) { this.grd_id = grd_id; }
    public void setId(int id) { this.id = id; }
    public void setCrsId(int crs_id) { this.crs_id = crs_id; }
    public void setGrdMt(float grd_mt) { this.grd_mt = grd_mt; }
    public void setGrdHw(float grd_hw) { this.grd_hw = grd_hw; }
    public void setGrdFinal(float grd_final) { this.grd_final = grd_final; }
    public void setGrdLgrade(String grd_lgrade) { this.grd_lgrade = grd_lgrade; }

    public int getGrdId() { return grd_id; }
    public int getid() { return id; }
    public int getCrsId() { return crs_id; }
    public float getGrdMt() { return grd_mt; }
    public float getGrdHw() { return grd_hw; }
    public float getGrdFinal() { return grd_final; }
    public String getGrdLgrade() { return grd_lgrade; }

    public void readAndStoreGrade(Connection conn) {
        Scanner sc = new Scanner(System.in);
        Grades gr = new Grades(grd_id, crs_id);

        try {
            System.out.print("Enter Student ID (not number): ");
            gr.setId(sc.nextInt());
            System.out.print("Enter Course ID: ");
            gr.setCrsId(sc.nextInt());
            System.out.print("Enter Midterm Grade: ");
            gr.setGrdMt(sc.nextFloat());
            System.out.print("Enter Homework Grade: ");
            gr.setGrdHw(sc.nextFloat());
            System.out.print("Enter Final Grade: ");
            gr.setGrdFinal(sc.nextFloat());

            gr.calculateLetterGrade(gr);
            String query = "INSERT INTO grades (std_id, crs_id, grd_mt, grd_hw, grd_final, grd_lgrade) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, gr.getid());
                stmt.setInt(2, gr.getCrsId());
                stmt.setFloat(3, gr.getGrdMt());
                stmt.setFloat(4, gr.getGrdHw());
                stmt.setFloat(5, gr.getGrdFinal());
                stmt.setString(6, gr.getGrdLgrade());

                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Grade added successfully with Letter Grade: " + gr.getGrdLgrade());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting grade into database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: Invalid input. " + e.getMessage());
        }
    }

    public void updateGrades(Connection conn) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter Course ID to update grades: ");
            int courseId = sc.nextInt();

            Grades gr = new Grades(grd_id, crs_id);
            gr.setCrsId(courseId);

            System.out.print("Enter new Midterm Grade: ");
            float newMt = sc.nextFloat();
            System.out.print("Enter new Homework Grade: ");
            float newHw = sc.nextFloat();
            System.out.print("Enter new Final Grade: ");
            float newFinal = sc.nextFloat();

            gr.setGrdMt(newMt);
            gr.setGrdHw(newHw);
            gr.setGrdFinal(newFinal);

            gr.calculateLetterGrade(gr);

            String query = "UPDATE grades SET grd_mt = ?, grd_hw = ?, grd_final = ?, grd_lgrade = ? WHERE grd_id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setFloat(1, gr.getGrdMt());
                stmt.setFloat(2, gr.getGrdHw());
                stmt.setFloat(3, gr.getGrdFinal());
                stmt.setString(4, gr.getGrdLgrade());
                stmt.setInt(5, gr.getGrdId());

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Grades for course ID " + gr.getCrsId() + " have been updated successfully.");
                } else {
                    System.out.println("No grades found for course ID " + gr.getCrsId() + ". Please check the course ID.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error updating grades in database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: Invalid input. " + e.getMessage());
        }
    }

    public void deleteGrade(Connection conn) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter the Course ID to delete the grade: ");
            int courseId = sc.nextInt();

            Grades gr = new Grades(grd_id, crs_id);
            gr.setCrsId(courseId);

            String query = "DELETE FROM grades WHERE crs_id = ?";

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, gr.getCrsId());

                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Grade for course ID " + gr.getCrsId() + " has been deleted successfully.");
                } else {
                    System.out.println("No grades found for course ID " + gr.getCrsId() + ". Please check the course ID.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error deleting grade from database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: Invalid input. " + e.getMessage());
        }
    }

    public void displayGradeFromDB(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Course ID to view grades: ");
        int courseId = sc.nextInt();

        String query = "SELECT * FROM grades WHERE crs_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, courseId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Grades gr = new Grades(rs.getInt("grd_id"), rs.getInt("std_id"), rs.getInt("crs_id"),
                        rs.getFloat("grd_mt"), rs.getFloat("grd_hw"), rs.getFloat("grd_final"));
                gr.setGrdLgrade(rs.getString("grd_lgrade"));
                gr.displayGrade(conn);
            } else {
                System.out.println("No grades found for the given course ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error fetching grades from database: " + e.getMessage());
        }
    }

    public void calculateLetterGrade(Grades gr) {
        float totalScore = 0.3f * gr.getGrdMt() + 0.3f * gr.getGrdHw() + 0.4f * gr.getGrdFinal();
        if (totalScore > 90) {
            gr.setGrdLgrade("A");
        } else if (totalScore >= 85) {
            gr.setGrdLgrade("A-");
        } else if (totalScore >= 80) {
            gr.setGrdLgrade("B");
        } else if (totalScore >= 75) {
            gr.setGrdLgrade("B-");
        } else if (totalScore >= 70) {
            gr.setGrdLgrade("C");
        } else if (totalScore >= 65) {
            gr.setGrdLgrade("C-");
        } else if (totalScore >= 60) {
            gr.setGrdLgrade("D");
        } else if (totalScore >= 55) {
            gr.setGrdLgrade("D-");
        } else {
            gr.setGrdLgrade("F");
        }
    }

    public void displayGrade(Connection conn) {
        System.out.println("Grade ID: " + grd_id);
        System.out.println("Student ID: " + id);
        System.out.println("Course ID: " + crs_id);
        System.out.println("Midterm: " + grd_mt + ", Homework: " + grd_hw + ", Final: " + grd_final);
        System.out.println("Letter Grade: " + grd_lgrade);
    }

    public void insertGradeIntoDatabase(Connection conn) {
        String query = "INSERT INTO grades (grd_id, std_id, crs_id, grd_mt, grd_hw, grd_final, grd_lgrade) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, grd_id);
            stmt.setInt(2, id);
            stmt.setInt(3, crs_id);
            stmt.setFloat(4, grd_mt);
            stmt.setFloat(5, grd_hw);
            stmt.setFloat(6, grd_final);
            stmt.setString(7, grd_lgrade);

            stmt.executeUpdate();
            System.out.println("Grade added successfully to the database.");
        } catch (SQLException e) {
            System.err.println("Error inserting grade: " + e.getMessage());
        }
    }

    public void updateGradeInDatabase(Connection conn) {
        String query = "UPDATE grades SET grd_mt = ?, grd_hw = ?, grd_final = ?, grd_lgrade = ? WHERE grd_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setFloat(1, grd_mt);
            stmt.setFloat(2, grd_hw);
            stmt.setFloat(3, grd_final);
            stmt.setString(4, grd_lgrade);
            stmt.setInt(5, grd_id);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Grade with ID " + grd_id + " has been updated successfully.");
            } else {
                System.out.println("No grade found with ID " + grd_id);
            }
        } catch (SQLException e) {
            System.err.println("Error updating grade: " + e.getMessage());
        }
    }

    public static void displayGrades(Connection conn) {
        String query = "SELECT * FROM grades";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Grade ID | Student ID | Course ID | Midterm | Homework | Final | Letter Grade");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------");

            while (rs.next()) {
                int grdId = rs.getInt("grd_id");
                int stdId = rs.getInt("std_id");
                int crsId = rs.getInt("crs_id");
                float grdMt = rs.getFloat("grd_mt");
                float grdHw = rs.getFloat("grd_hw");
                float grdFinal = rs.getFloat("grd_final");
                String grdLgrade = rs.getString("grd_lgrade");

                System.out.println(grdId + " | " + stdId + " | " + crsId + " | " + grdMt + " | " + grdHw + " | " + grdFinal + " | " + grdLgrade);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching grades from database: " + e.getMessage());
        }
    }

    public static void backupGrades(Connection conn) {
         String query = "SELECT * FROM grades";
         String backupFile = "C:\\Users\\asus\\OneDrive\\Desktop\\JavaMaven\\src\\main\\java\\com\\mycompany\\grades_backup.csv"; 

         try (PreparedStatement stmt = conn.prepareStatement(query);
              ResultSet rs = stmt.executeQuery();
              BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {

             writer.write("grd_id,std_id,crs_id,grd_mt,grd_hw,grd_final,grd_lgrade");
             writer.newLine();

             while (rs.next()) {
                 int grdId = rs.getInt("grd_id");
                 int stdId = rs.getInt("std_id");
                 int crsId = rs.getInt("crs_id");
                 float grdMt = rs.getFloat("grd_mt");
                 float grdHw = rs.getFloat("grd_hw");
                 float grdFinal = rs.getFloat("grd_final");
                 String grdLgrade = rs.getString("grd_lgrade");

                 writer.write(grdId + "," + stdId + "," + crsId + "," + grdMt + "," + grdHw + "," + grdFinal + "," + grdLgrade);
                 writer.newLine();
             }

             System.out.println("Backup successful! Data saved to " + backupFile);

         } catch (SQLException | IOException e) {
             System.err.println("Error backing up grades: " + e.getMessage());
         }
     }
   

    public static void restoreGrades(Connection conn) {
         String restoreFile = "C:\\Users\\asus\\OneDrive\\Desktop\\JavaMaven\\src\\main\\java\\com\\mycompany\\grades_backup.csv"; 
         String query = "INSERT INTO grades (grd_id, std_id, crs_id, grd_mt, grd_hw, grd_final, grd_lgrade) VALUES (?, ?, ?, ?, ?, ?, ?)";

         try (BufferedReader reader = new BufferedReader(new FileReader(restoreFile));
              PreparedStatement stmt = conn.prepareStatement(query)) {

             String line;
             reader.readLine(); 

             while ((line = reader.readLine()) != null) {
                 if (line.trim().isEmpty()) {
                     continue;
                 }

                 String[] data = line.split(",");
                 if (data.length != 7) {
                     System.err.println("Skipping malformed line: " + line);
                     continue;
                 }

                 try {
                     int grdId = Integer.parseInt(data[0].trim());
                     int stdId = Integer.parseInt(data[1].trim());
                     int crsId = Integer.parseInt(data[2].trim());
                     float grdMt = Float.parseFloat(data[3].trim());
                     float grdHw = Float.parseFloat(data[4].trim());
                     float grdFinal = Float.parseFloat(data[5].trim());
                     String grdLgrade = data[6].trim();

                     stmt.setInt(1, grdId);
                     stmt.setInt(2, stdId);
                     stmt.setInt(3, crsId);
                     stmt.setFloat(4, grdMt);
                     stmt.setFloat(5, grdHw);
                     stmt.setFloat(6, grdFinal);
                     stmt.setString(7, grdLgrade);

                     stmt.executeUpdate();
                 } catch (NumberFormatException | SQLException e) {
                     System.err.println("Error parsing line: " + line + " -> " + e.getMessage());
                 }
             }

             System.out.println("Restore successful! Grades records have been added to the database.");

         } catch (SQLException | IOException e) {
             System.err.println("Error restoring grades: " + e.getMessage());
         }
     }

}
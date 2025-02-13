package com.mycompany.javamaven;
import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Department {



    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "29102001");
    }
    int dept_id;
    String dept_name;

    public Department(int dept_id, String dept_name) {
        this.dept_id = dept_id;
        this.dept_name = dept_name;
    }

    public int getDeptId() { return dept_id; }
    public String getDeptName() { return dept_name; }

    public void setDeptId(int dept_id) { this.dept_id = dept_id; }
    public void setDeptName(String dept_name) { this.dept_name = dept_name; }

    public static Department readDepartment() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Department Name: ");
        String name = sc.nextLine();
        return new Department(0, name);  
    }

    public static void displayDepartments(Connection conn) {
        String query = "SELECT * FROM departments";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Department ID | Department Name");
            System.out.println("-------------------------------");

            while (rs.next()) {
                int deptId = rs.getInt("dept_id");
                String deptName = rs.getString("dept_name");
                System.out.println(deptId + " | " + deptName);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving departments: " + e.getMessage());
        }
    }

    public static void addDepartmentToDatabase(Connection conn) {
        Department dp = readDepartment();  
        String query = "INSERT INTO departments (dept_name) VALUES (?)";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dp.getDeptName());
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Department added successfully to the database.");
            } else {
                System.out.println("Failed to add the department.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting department: " + e.getMessage());
        }
    }

    public static void updateDepartment(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Department ID to update: ");
        int deptId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new Department Name: ");
        String newDeptName = sc.nextLine();

        if (newDeptName.isEmpty()) {
            System.out.println("Update canceled. No new name provided.");
            return;
        }

        String query = "UPDATE departments SET dept_name = ? WHERE dept_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newDeptName);
            stmt.setInt(2, deptId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Department updated successfully.");
            } else {
                System.out.println("No department found with the given ID.");
            }
        } catch (SQLException e) {
            System.err.println("Error updating department: " + e.getMessage());
        }
    }

    public static void deleteDepartment(Connection conn) {
        String query = "DELETE FROM departments WHERE dept_name = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            String deptName = null;
            stmt.setString(1, deptName);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Department " + deptName + " has been deleted successfully.");
            } else {
                System.out.println("No department found with the name " + deptName);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting department: " + e.getMessage());
        }
    }

    public static void backupDepartments(Connection conn) {
        String query = "SELECT * FROM departments";
        String backupFile =  "C:\\Users\\asus\\OneDrive\\Desktop\\JavaMaven\\department_backup.csv";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();
             BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {

            writer.write("dept_id,dept_name");
            writer.newLine();

            while (rs.next()) {
                int deptId = rs.getInt("dept_id");
                String deptName = rs.getString("dept_name");
                writer.write(deptId + "," + deptName);
                writer.newLine();
            }

            System.out.println("Backup successful! Data saved to " + backupFile);

        } catch (SQLException | IOException e) {
            System.err.println("Error backing up departments: " + e.getMessage());
        }
    }

    public static void restoreDepartments(Connection conn) {
        String restoreFile = "C:\\Users\\asus\\OneDrive\\Desktop\\JavaMaven\\department_backup.csv";
        String query = "INSERT INTO departments (dept_id, dept_name) VALUES (?, ?)";

        try (BufferedReader reader = new BufferedReader(new FileReader(restoreFile));
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String line;
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",");
                if (data.length != 2) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;
                }

                try {
                    int deptId = Integer.parseInt(data[0].trim());
                    String deptName = data[1].trim();

                    stmt.setInt(1, deptId);
                    stmt.setString(2, deptName);

                    stmt.executeUpdate();
                } catch (NumberFormatException | SQLException e) {
                    System.err.println("Error parsing line: " + line + " -> " + e.getMessage());
                }
            }

            System.out.println("Restore successful! Department records have been added to the database.");

        } catch (SQLException | IOException e) {
            System.err.println("Error restoring departments: " + e.getMessage());
        }
    }
}

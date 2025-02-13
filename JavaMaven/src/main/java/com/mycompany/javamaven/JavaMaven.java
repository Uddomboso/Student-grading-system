package com.mycompany.javamaven;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class JavaMaven {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        String url = "jdbc:mysql://localhost:3306/studentdb?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "29102001";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to the database!");

            while (running) {
                System.out.println("\nSelect a table to interact with:");
                System.out.println("1. Student");
                System.out.println("2. Attendance");
                System.out.println("3. Course");
                System.out.println("4. Department");
                System.out.println("5. Grades");
                System.out.println("6. Exit");

                int choice = readInt(sc, "Enter your choice: ");
                switch (choice) {
                    case 1 -> studentMenu(conn, sc);
                    case 2 -> attendanceMenu(conn, sc);
                    case 3 -> courseMenu(conn, sc);
                    case 4 -> departmentMenu(conn, sc);
                    case 5 -> gradesMenu(conn, sc);
                    case 6 -> {
                        System.out.println("Exiting...");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error establishing database connection: " + e.getMessage());
        } finally {
            sc.close(); 
        }
    }

   public static int readInt(Scanner sc, String prompt) {
    while (true) {
        try {
            System.out.print(prompt);
            
           
            if (!sc.hasNextLine()) {
                System.out.println("No input found! Please try again.");
                continue;
            }

            String input = sc.nextLine().trim(); 
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty! Please try again.");
                continue;
            }
            

            int value = Integer.parseInt(input);
            return value;

        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        } catch (NoSuchElementException e) {
            System.out.println("Unexpected error: No line found. Please try again.");
        } catch (IllegalStateException e) {
            System.out.println("Scanner has been closed unexpectedly.");
            break;
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
        return 0;
   }

public static void studentMenu(Connection conn, Scanner sc) throws IOException {
    boolean backToMain = false;

    while (!backToMain) {
        System.out.println("\nStudent Menu:");
        System.out.println("1. Add a new student");
        System.out.println("2. Update student information");
        System.out.println("3. Delete a student");
        System.out.println("4. Display all students");
        System.out.println("5. Backup student tables");
        System.out.println("6. Restore student tables");
        System.out.println("7. Go back to main menu");


        int choice = readInt(sc, "Enter your choice: ");
        
        switch (choice) {
            case 1 -> Student.readStudentAndInsert(conn);
            case 2 -> Student.user_update_student(conn);
            case 3 -> Student.user_delete_student(conn);
            case 4 -> Student.displayStudents(conn);
            case 5 -> Student.backupStudents(conn);
            case 6 -> Student.restoreStudents(conn);
            case 7 -> backToMain = true;
            default -> System.out.println("Invalid choice! Please try again.");
        }
    }
}


    public static void attendanceMenu(Connection conn, Scanner sc) {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\nAttendance Menu:");
            System.out.println("1. Add attendance record");
            System.out.println("2. Update attendance record");
            System.out.println("3. Delete attendance record");
            System.out.println("4. Display all attendance records");
            System.out.println("5. Backup attendance records");
            System.out.println("6. Restore attendance records");
            System.out.println("7. Go back to main menu");

            int choice = readInt(sc, "Enter your choice: ");
            switch (choice) {
                case 1 -> Attendance.addAttendanceToDatabase(conn);
                case 2 -> Attendance.updateAttendance(conn);
                case 3 -> Attendance.deleteAttendance(conn);
                case 4 -> Attendance.displayAttendance(conn);
                case 5 -> Attendance.backupAttendance(conn);
                case 6 -> Attendance.restoreAttendance(conn);
                case 7 -> backToMain = true;
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }


    public static void courseMenu(Connection conn, Scanner sc) throws IOException {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\nCourse Menu:");
            System.out.println("1. Insert a Course");
            System.out.println("2. Display all Courses");
            System.out.println("3. Update a Course");
            System.out.println("4. Delete a Course");
            System.out.println("5. Backup course records");
            System.out.println("6. Restore course records");
            System.out.println("7. Go back to main menu");

            int choice = readInt(sc, "Enter your choice: ");
            switch (choice) {
                case 1 -> Course.readCourseAndInsert(sc);
                case 2 -> Course.displayCourses();
                case 3 -> Course.userUpdateCourse(sc);
                case 4 -> Course.userDeleteCourse(sc);
                case 5 -> Course.backupCourses(conn);
                case 6 -> Course.restoreCourses(conn);
                case 7 -> backToMain = true;
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    public static void departmentMenu(Connection conn, Scanner sc) {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\nDepartment Menu:");
            System.out.println("1. Add a new department");
            System.out.println("2. Update department information");
            System.out.println("3. Delete a department");
            System.out.println("4. Display all departments");
            System.out.println("5. Backup department records");
            System.out.println("6. Restore department records");
            System.out.println("7. Go back to main menu");

            int choice = readInt(sc, "Enter your choice: ");
            switch (choice) {
                case 1 -> Department.addDepartmentToDatabase(conn);
                case 2 -> Department.updateDepartment(conn);
                case 3 -> Department.deleteDepartment(conn);
                case 4 -> Department.displayDepartments(conn);
                case 5 -> Department.backupDepartments(conn);
                case 6 -> Department.restoreDepartments(conn);
                case 7 -> backToMain = true;
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    public static void gradesMenu(Connection conn, Scanner sc) {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\nGrades Menu:");
            System.out.println("1. Add grades");
            System.out.println("2. Update grades");
            System.out.println("3. Delete grades");
            System.out.println("4. Display grades for a course");
            System.out.println("5. Display all grades");
            System.out.println("6. Backup grades records");
            System.out.println("7. Restore grades records");
            System.out.println("8. Go back to main menu");

            int choice = readInt(sc, "Enter your choice: ");
            Grades grades = new Grades(0, 0, 0, 0.0f, 0.0f, 0.0f);

            switch (choice) {
                case 1 -> grades.readAndStoreGrade(conn);
                case 2 -> grades.updateGrades(conn);
                case 3 -> grades.deleteGrade(conn);
                case 4 -> grades.displayGrade(conn);
                case 5 -> Grades.displayGrades(conn);
                case 6 -> Grades.backupGrades(conn);
                case 7 -> Grades.restoreGrades(conn);
                case 8 -> backToMain = true;
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}

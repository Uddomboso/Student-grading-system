package com.mycompany.javamaven;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class Student {
    private String student_number;
    private String name;
    private String surname;
    private char gender;
    private String nationality;
    private String birthday;

    public Student(String student_number, String name, String surname, char gender, String nationality, String birthday) {
        this.student_number = student_number;
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.nationality = nationality;
        this.birthday = birthday;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "29102001");
    }


    public void setStudentNumber(String student_number) { this.student_number = student_number; }
    public void setName(String name) { this.name = name; }
    public void setSurname(String surname) { this.surname = surname; }
    public void setGender(char gender) { this.gender = gender; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getStudentNumber() { return student_number; }
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public char getGender() { return gender; }
    public String getNationality() { return nationality; }
    public String getBirthday() { return birthday; }

    public static void readStudentAndInsert(Connection conn) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Input student number: ");
            String student_number = sc.nextLine().trim();
            if (student_number.isEmpty()) {
                System.err.println("Student number cannot be empty.");
                return;
            }

            System.out.print("Input student name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.err.println("Name cannot be empty.");
                return;
            }

            System.out.print("Input student surname: ");
            String surname = sc.nextLine().trim();
            if (surname.isEmpty()) {
                System.err.println("Surname cannot be empty.");
                return;
            }

            System.out.print("Input gender (M/F): ");
            String genderInput = sc.nextLine().toUpperCase().trim();
            if (!genderInput.equals("M") && !genderInput.equals("F")) {
                System.err.println("Invalid gender input. Please enter 'M' or 'F'.");
                return;
            }
            char gender = genderInput.charAt(0);

            System.out.print("Input nationality: ");
            String nationality = sc.nextLine().trim();
            if (nationality.isEmpty()) {
                System.err.println("Nationality cannot be empty.");
                return;
            }

            System.out.print("Input birthday (yyyy-MM-dd): ");
            String birthday = sc.nextLine().trim();
            if (!isValidDate(birthday)) {
                System.err.println("Invalid date format. Please use yyyy-MM-dd.");
                return;
            }

            Student st = new Student(student_number, name, surname, gender, nationality, birthday);
            st.insertStudentIntoDatabase();
        }
    }


    public void insertStudentIntoDatabase() {
        String query = "INSERT INTO students (student_number, name, surname, gender, nationality, birthday) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, getStudentNumber());
            stmt.setString(2, getName());
            stmt.setString(3, getSurname());
            stmt.setString(4, String.valueOf(getGender()));
            stmt.setString(5, getNationality());
            stmt.setString(6, getBirthday());

            stmt.executeUpdate();
            System.out.println("Student added successfully to the database.");
        } catch (SQLException e) {
            System.err.println("Error inserting student: " + e.getMessage());
        }
    }

    public static void user_update_student(Connection conn) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter the student number to update: ");
            String student_number = sc.nextLine().trim();
            if (student_number.isEmpty()) {
                System.err.println("Student number cannot be empty.");
                return;
            }

            System.out.print("Input new student name: ");
            String name = sc.nextLine().trim();
            if (name.isEmpty()) {
                System.err.println("Name cannot be empty.");
                return;
            }

            System.out.print("Input new student surname: ");
            String surname = sc.nextLine().trim();
            if (surname.isEmpty()) {
                System.err.println("Surname cannot be empty.");
                return;
            }

            System.out.print("Input new gender (M/F): ");
            String genderInput = sc.nextLine().toUpperCase().trim();
            if (!genderInput.equals("M") && !genderInput.equals("F")) {
                System.err.println("Invalid gender input. Please enter 'M' or 'F'.");
                return;
            }
            char gender = genderInput.charAt(0);

            System.out.print("Input new nationality: ");
            String nationality = sc.nextLine().trim();
            if (nationality.isEmpty()) {
                System.err.println("Nationality cannot be empty.");
                return;
            }

            System.out.print("Input new birthday (yyyy-MM-dd): ");
            String birthday = sc.nextLine().trim();
            if (!isValidDate(birthday)) {
                System.err.println("Invalid date format. Please use yyyy-MM-dd.");
                return;
            }

            Student st = new Student(student_number, name, surname, gender, nationality, birthday);
            updateStudentInDatabase(st);
        }
    }


    public static void updateStudentInDatabase(Student st) {
        String query = "UPDATE students SET name = ?, surname = ?, gender = ?, nationality = ?, birthday = ? WHERE student_number = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, st.getName());
            stmt.setString(2, st.getSurname());
            stmt.setString(3, String.valueOf(st.getGender()));
            stmt.setString(4, st.getNationality());
            stmt.setString(5, st.getBirthday());
            stmt.setString(6, st.getStudentNumber());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student with Student Number " + st.getStudentNumber() + " has been updated successfully.");
            } else {
                System.out.println("No student found with Student Number " + st.getStudentNumber());
            }
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
        }
    }

    public static void displayStudents(Connection conn) {
        String query = "SELECT * FROM students";
        try (PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            System.out.println("Student Number | Name | Surname | Gender | Nationality | Birthday");
            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {
                String studentNumber = rs.getString("student_number");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String gender = rs.getString("gender");
                String nationality = rs.getString("nationality");
                String birthday = rs.getString("birthday");

                Student st = new Student(studentNumber, name, surname, gender.charAt(0), nationality, birthday);

                System.out.println(st.getStudentNumber() + " | " + st.getName() + " | " + st.getSurname() + " | " + st.getGender() + " | " + st.getNationality() + " | " + st.getBirthday());
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving students: " + e.getMessage());
        }
    }

    public static void user_delete_student(Connection conn) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Enter the student number to delete: ");
            String student_number = sc.nextLine().trim();
            if (student_number.isEmpty()) {
                System.err.println("Student number cannot be empty.");
                return;
            }

            Student st = new Student(student_number, "", "", ' ', "", "");
            deleteStudent(st);
        }
    }


    public static void deleteStudent(Student st) {
        String student_number = st.getStudentNumber();
        String query = "DELETE FROM students WHERE student_number = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, student_number);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student with Student Number " + student_number + " has been deleted successfully.");
            } else {
                System.out.println("No student found with Student Number " + student_number);
            }
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
    }
    
    public static void backupStudents(Connection conn) throws IOException {
    String query = "SELECT * FROM students";
    String backupFile = "C:\\Users\\asus\\OneDrive\\Desktop\\JavaMaven\\src\\main\\java\\com\\mycompany\\students_backup.csv";

    try (PreparedStatement stmt = conn.prepareStatement(query);
         ResultSet rs = stmt.executeQuery();
         BufferedWriter writer = new BufferedWriter(new FileWriter(backupFile))) {

        writer.write("student_number,name,surname,gender,nationality,birthday");
        writer.newLine();

        while (rs.next()) {
            String studentNumber = rs.getString("student_number");
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String gender = rs.getString("gender");
            String nationality = rs.getString("nationality");
            String birthday = rs.getString("birthday");

            writer.write(studentNumber + "," + name + "," + surname + "," + gender + "," + nationality + "," + birthday);
            writer.newLine();
        }

        System.out.println("Backup successful! Data saved to " + backupFile);

    } catch (SQLException | IOException e) {
        System.err.println("Error backing up students: " + e.getMessage());
    }
}
    
   public static void restoreStudents(Connection conn) {
    String restoreFile = "C:\\Users\\asus\\OneDrive\\Desktop\\JavaMaven\\src\\main\\java\\com\\mycompany\\students_backup.csv";
    String query = "INSERT INTO students (student_number, name, surname, gender, nationality, birthday) VALUES (?, ?, ?, ?, ?, ?)";

    try (BufferedReader reader = new BufferedReader(new FileReader(restoreFile));
         PreparedStatement stmt = conn.prepareStatement(query)) {

        String line;
        reader.readLine();

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] data = line.split(",");
            if (data.length != 6) {
                System.err.println("Skipping malformed line: " + line);
                continue;
            }

            try {
                String studentNumber = data[0].trim();
                String name = data[1].trim();
                String surname = data[2].trim();
                String gender = data[3].trim();
                String nationality = data[4].trim();
                String birthday = data[5].trim();

                stmt.setString(1, studentNumber);
                stmt.setString(2, name);
                stmt.setString(3, surname);
                stmt.setString(4, gender);
                stmt.setString(5, nationality);
                stmt.setString(6, birthday);

                stmt.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error restoring line: " + line + " -> " + e.getMessage());
            }
        }

        System.out.println("Restore successful! Students records have been added to the database.");

    } catch (SQLException | IOException e) {
        System.err.println("Error restoring students: " + e.getMessage());
    }
}

    private static boolean isValidDate(String date) {
        try {
            java.time.LocalDate.parse(date); 
        } catch (java.time.format.DateTimeParseException e) {
            return false; 
        }
        return false;
    }


    }

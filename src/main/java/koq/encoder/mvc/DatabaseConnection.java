package koq.encoder.mvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Database objects for reference:
 * 
 * students: 
 *      student_id (PRIMARY), 
 *      first_name (VARCHAR), 
 *      last_name (VARCHAR)
 * 
 * classes: 
 *      class_id (PRIMARY),
 *      section (VARCHAR),
 *      subject (VARCHAR)
 * 
 * activities:
 *      activity_id (PRIMARY),
 *      activity_name (VARCHAR),
 *      max_grade (DECIMAL 4,2),
 *      activity_type_id (FOREIGN)
 * 
 * activity_types:
 *      activity_type_id (PRIMARY)
 *      activity_type_name (VARCHAR)
 * 
 * grades:
 *      grade_id (PRIMARY),
 *      student_id (FOREIGN), 
 *      class_id (FOREIGN),
 *      activity_id (FOREIGN),
 *      grade (DECIMAL 4,2),
 *      term (VARCHAR)
 * 
 * student_classes: (Junction table)
 *      student_id (FOREIGN), 
 *      class_id (FOREIGN)
 */

/** 
 * Class for all database-related functionalities
 */
public class DatabaseConnection {
    public static Connection connection;
    
    private Connection serverConn;
    private ResultSet rs;
    private String url = "jdbc:mysql://localhost:3306/";
    private String user = "root";
    private String password = "root";
    
    // Connect to a schema upon constructor call
    public DatabaseConnection(String schema) throws SQLException {
        try {
            serverConn = DriverManager.getConnection(url, user, password);
            if (dbExists(schema)) {
                connection = DriverManager.getConnection(url + schema, user, password);
            } else {
                System.out.println("Error: Schema '" + schema + "' does not exist. Creating...");
                createSchema(schema);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        
        try {
            Statement s = connection.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM students;");
            
            while (rs.next()) {
                studentList.add(new Student(
                    rs.getInt("student_id"), rs.getString("first_name"), rs.getString("last_name"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return studentList;
    }
    
    public static List<String> getActivitiesInClassRecord(String className, String subjectName, String term) {
        List<String> activityList = new ArrayList<>();
        
        try {
            PreparedStatement ps = connection.prepareStatement("""
                SELECT DISTINCT a.activity_type_id, a.activity_id, a.activity_name
                FROM grades g
                JOIN activities a ON g.activity_id = a.activity_id
                JOIN classes c ON g.class_id = c.class_id
                WHERE c.section = ? AND c.subject = ? AND a.term = ?
                ORDER BY a.activity_type_id, a.activity_name;                                               
            """);
            ps.setString(1, className);
            ps.setString(2, subjectName);
            ps.setString(3, term);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                activityList.add(rs.getString("activity_name"));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return activityList;
    }
    
    public static List<Row> getClassRecord(String className, String subjectName, String term) {
        List<Row> rows = new ArrayList<>();
        
        try {
            PreparedStatement ps = connection.prepareStatement("""
                SELECT *
                FROM grades g
                JOIN students s ON g.student_id = s.student_id
                JOIN activities a ON g.activity_id = a.activity_id
                JOIN classes c ON g.class_id = c.class_id
                WHERE c.section = ? AND c.subject = ? AND a.term = ?
                ORDER BY s.student_id, a.activity_type_id, a.activity_name;
            """);
            ps.setString(1, className);
            ps.setString(2, subjectName);
            ps.setString(3, term);
            
            ResultSet rs = ps.executeQuery();
            
            List<Student> studentList = new ArrayList<>();
            List<Grade> gradesList = new ArrayList<>();
            
            while (rs.next()) {
                Student s = new Student(rs.getInt("student_id"), rs.getString("first_name"), rs.getString("last_name"));
                
                boolean studentInList = false;
                for (Student st: studentList) {
                    if (st.getStudentId() == s.getStudentId()) {
                        studentInList = true;
                        break;
                    }
                }
                if (!(studentInList)) {
                    studentList.add(s);
                }

                Grade g = new Grade(
                        rs.getInt("grade_id"), 
                        rs.getInt("student_id"), 
                        rs.getInt("class_id"), 
                        rs.getInt("activity_id"), 
                        Double.parseDouble(rs.getString("max_grade"))
                );
                
                gradesList.add(g);
            }
            
            for (Student s: studentList) {
                List<Grade> studentGrades = new ArrayList<>();
                for (Grade g: gradesList) {
                    if (g.getStudentId() == s.getStudentId()) {
                        studentGrades.add(g);
                    }
                }
                rows.add(new Row(s, studentGrades));
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rows;
    }
    
    private boolean dbExists(String name) {
        boolean dbExists = false;
        
        try {
            rs = serverConn.getMetaData().getCatalogs();
            
            while (rs.next()) {
                String dbName = rs.getString(1);
                if (dbName.equalsIgnoreCase(name)) {
                    dbExists = true;
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if (dbExists) {
                return true;
            } else {
                return false;
        }
    }
    
    private void createSchema(String name) {
        try {
            Statement s = serverConn.createStatement();
            s.executeUpdate("CREATE SCHEMA " + name + ";");
            
            // After creating schema, connect to database and create tables
            connection = DriverManager.getConnection(url + name, user, password);
            s = connection.createStatement();
            
            s.executeUpdate("""
                CREATE TABLE `activity_types` (
                  `activity_type_id` int NOT NULL AUTO_INCREMENT,
                  `activity_type_name` varchar(20) DEFAULT NULL,
                  PRIMARY KEY (`activity_type_id`)
                );
            """);
            
            s.executeUpdate("INSERT INTO `activity_types` (`activity_type_id`, `activity_type_name`) VALUES ('1', 'Seatwork');");
            s.executeUpdate("INSERT INTO `activity_types` (`activity_type_id`, `activity_type_name`) VALUES ('2', 'Assignment');");
            s.executeUpdate("INSERT INTO `activity_types` (`activity_type_id`, `activity_type_name`) VALUES ('3', 'Performance Task');");
            s.executeUpdate("INSERT INTO `activity_types` (`activity_type_id`, `activity_type_name`) VALUES ('4', 'Quiz');");
            s.executeUpdate("INSERT INTO `activity_types` (`activity_type_id`, `activity_type_name`) VALUES ('5', 'Exam');");

            s.executeUpdate("""
                CREATE TABLE `classes` (
                  `class_id` int NOT NULL AUTO_INCREMENT,
                  `section` varchar(45) NOT NULL,
                  `subject` varchar(45) NOT NULL,
                  `academic_year` varchar(45) NOT NULL,
                  PRIMARY KEY (`class_id`)
                );
            """);

            s.executeUpdate("""
                CREATE TABLE `activities` (
                  `activity_id` int NOT NULL AUTO_INCREMENT,
                  `activity_name` varchar(45) DEFAULT NULL,
                  `max_grade` decimal(4,2) DEFAULT NULL,
                  `activity_type_id` int DEFAULT NULL,
                  `term` varchar(45) DEFAULT NULL,
                  PRIMARY KEY (`activity_id`),
                  KEY `activity_type_id` (`activity_type_id`),
                  CONSTRAINT `activities_ibfk_1` FOREIGN KEY (`activity_type_id`) REFERENCES `activity_types` (`activity_type_id`) ON DELETE CASCADE ON UPDATE CASCADE
                );
            """);

            s.executeUpdate("""
                CREATE TABLE `students` (
                  `student_id` INT NOT NULL,
                  `first_name` VARCHAR(45) NULL,
                  `last_name` VARCHAR(45) NULL,
                  PRIMARY KEY (`student_id`)
                );
            """);
            
            s.executeUpdate("""
                CREATE TABLE `grades` (
                  `grade_id` int NOT NULL AUTO_INCREMENT,
                  `student_id` int NOT NULL,
                  `class_id` int NOT NULL,
                  `activity_id` int NOT NULL,
                  `grade` int DEFAULT NULL,
                  PRIMARY KEY (`grade_id`),
                  KEY `grades_ibfk_2_idx` (`student_id`),
                  KEY `grades_ibfk_1` (`activity_id`),
                  CONSTRAINT `grades_ibfk_1` FOREIGN KEY (`activity_id`) REFERENCES `activities` (`activity_id`) ON DELETE CASCADE,
                  CONSTRAINT `grades_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
                );
            """);
            
            s.executeUpdate("""                      
                CREATE TABLE `student_classes` (
                  `student_class_id` int NOT NULL AUTO_INCREMENT,
                  `student_id` int NOT NULL,
                  `class_id` int NOT NULL,
                  PRIMARY KEY (`student_class_id`),
                  KEY `student_id_idx` (`student_id`),
                  KEY `class_id_idx` (`class_id`),
                  CONSTRAINT `class_id` FOREIGN KEY (`class_id`) REFERENCES `classes` (`class_id`),
                  CONSTRAINT `student_id` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`)
                );
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Schema " + name + " created.");
    }
    
    /** 
     * NOTE: OUTDATED CODE
     * 
     */
    public void getStudentsInClass() throws SQLException {
        String checkQuery = "SELECT * FROM student_classes";
        try (PreparedStatement pstmt = connection.prepareStatement(checkQuery)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("student_id\tclass_id");
                
                while (rs.next()) {
                    int studentId = rs.getInt("student_id");
                    int classId = rs.getInt("class_id");
                    System.out.println(studentId + "\t\t" + classId);
                }
            }
        }
    }
    
    public boolean studentExists(String firstName, String lastName) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM students WHERE first_name = ? AND last_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(checkQuery)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // If count > 0, student exists
                }
            }
        }
        return false; // Student does not exist
    }

    public void addStudent(String firstName, String lastName) throws SQLException {
        if (!studentExists(firstName, lastName)) {
            String insertQuery = "INSERT INTO students (first_name, last_name, class_id) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.executeUpdate();
                System.out.println("Student added successfully.");
            }
        } else {
            System.out.println("Student already exists.");
        }
    }
}
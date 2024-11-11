package koq.encoder.mvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import classes.Activity;
import classes.Grade;
import classes.Student;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.SwingWorker;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

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

public class Model {

    private String[] columnNames;
    private int selectedRow;
    private int selectedColumn;
    private int selectedActivity;
    private List<Student> studentList;
    private List<Activity> activityList;
    private ClassRecord record;
    private ClassRecord currentClassRecord;
    
    private String addStudentFirstName;
    private String addStudentLastName;
    private String addActivitySection;
    private String addActivitySubject;
    private int addActivityTerm;
    
    private Connection db;
    private Connection serverConn;
    private String url = "jdbc:mysql://localhost:3306/";
    private String user = "root";
    private String password = "root";
    
    public static enum Fields {
        EDIT_GRADE,
        SELECT_ACTIVITY,
        SELECT_ACTIVITY_TYPE,
        MAX_GRADE
    };
    
    public static enum Actions {
        NEWTABLE,
        OPENFILE,
        EXPORTFILE,
        EXIT,
        ADDTOTABLE,
        REMOVEFROMTABLE,
        ADDSTUDENT,
        ADDMENUCLICKED,
        ADDACTIVITY,
        ADDASSIGNMENT,
        ADDPT,
        ADDQUIZ,
        ADDEXAM,
        PREVIOUSSTUDENT,
        NEXTSTUDENT,
        MOVEROWDOWN,
        MOVEROWUP,
        VIEWABOUT
    }
    
    public Model() {
        // NOTE: Placeholder values, change these later
        selectedRow = 0;
        selectedColumn = 0;
        selectedActivity = 0;
        
        addStudentFirstName = null;
        addStudentLastName = null;
        
        try {
            db = connectToDB("encoder_data");
        } catch (SQLException e) {}
        
        studentList = getAllStudents();
        
        List<Row> rows = getClassRecordInDB("A", "Mathematics 10", 1);
        List<String> cols = getActivityNamesInClassRecord("A", "Mathematics 10", 1);
        cols.add(0, "#");
        cols.add(1, "Student name");
        
        record = new ClassRecord("A", "Mathematics 10", 1, "2024-2025", rows, cols);
        
        currentClassRecord = record;
        
        activityList = getActivitiesInClassRecord("A", "Mathematics 10", 1);
        
    }
    
    private Connection getConnection() {
        return db;
    }
    
    public ClassRecord getClassRecord() {
        return record;
    }
    
    public ClassRecord getCurrentClassRecord() {
        return currentClassRecord;
    }
    
    public List<Student> getStudentList() {
        return studentList;
    }
    
    
    /**
     * TODO: Rework this part to conform to the new AbstractTableModel ClassRecord class
     * 
     */
    public Object getTableObjectAtCurrentSelection() {
        return getClassRecord().getValueAt(getSelectedRow(), getSelectedColumn());
    }
    
    public void addActivityToTable() {
        /**
         * 1. Get table headers
         * 2. Get the index
         * 3. Get the activity number
         * 4. Insert new column
        
        for (String s: getTableHeaders()) {
            if (s)
        }
        */
    }

    /*
    public void setTableModel(AbstractTableModel data, List<String> columnNames) {
        tableModel = data;
        this.columnNames = columnNames;
    }
    */
    
    private String[] getTableHeaders() {
        return columnNames;
    }
    
    public int getSelectedRow() {
        return selectedRow;
    }
    public void setSelectedRow(int value) {
        selectedRow = value;
    }
    public int getSelectedColumn() {
        return selectedColumn;
    }
    public void setSelectedColumn(int value) {
        selectedColumn = value;
    }
    public int getSelectedActivity() {
        return selectedActivity;
    }
    public void setSelectedActivity(int index) {
        selectedActivity = index;
    }
    
    public void setStudentFirstName(String name) {
        addStudentFirstName = name;
    }
    public void setStudentLastName(String name) {
        addStudentLastName = name;
    }
    public String getStudentFirstName() {
        return addStudentFirstName;
    }
    public String getStudentLastName() {
        return addStudentLastName;
    }
    
    public void setActivitySection(String section) {
        addActivitySection = section;
    }
    public void setActivitySubject(String name) {
        addActivitySubject = name;
    }
    public void setActivityTerm(int term) {
        addActivityTerm = term;
    }
    public String getActivitySection() {
        return addActivitySection;
    }
    public String getActivitySubject() {
        return addActivitySubject;
    }
    public int getActivityTerm() {
        return addActivityTerm;
    }
    
    public void addStudentToClassRecord(String firstName, String lastName) {
        try {
            addStudent(firstName, lastName);
            
            int id = getStudentIdInDB(firstName, lastName);
            
            Student s = new Student(id, firstName, lastName);
            
            Row row = new Row(s, new ArrayList<Grade>());

            for (Grade g: getCurrentClassRecord().getClassList().getFirst().getGrades()) {
                addEmptyGradeToDB(s.getStudentId(), g.getClassId(), g.getActivityId());
            }

            row.setGrades(getGrades(s.getStudentId(), getCurrentClassRecord().getClassId()));
            
            System.out.println("Size: " + row.getGrades().size());
            for (Grade g: row.getGrades()) {
                System.out.println(g.getActivityId());
            }

            getClassRecord().getClassList().add(row);
            getClassRecord().fireTableDataChanged();
            System.out.println("Row added");
        } catch (SQLException err) {}
    }
    
    public void addNewActivity(int index, String name, Double totalScore, int activityTypeId) {
        int activityId;
        int gradeId;
        
        try {
            addActivityToDB(getClassRecord().getClassId(), name, totalScore, activityTypeId, getClassRecord().getTerm());
            activityId = getActivityIdInDB(
                getClassRecord().getClassId(),
                name,
                getClassRecord().getTerm(),
                getClassRecord().getSY()
            );
            System.out.println("New activity added to DB");
            
            getClassRecord().insertColumn(index+1, name);
            
            for (Row r: getClassRecord().getClassList()) {
                addEmptyGradeToDB(
                    r.getStudent().getStudentId(),
                    r.getGrades().get(0).getClassId(),
                    activityId
                );
                
                gradeId = getGradeIdInDB(
                    r.getStudent().getStudentId(),
                    r.getGrades().get(0).getClassId(),
                    activityId
                );
                
                Grade grade = new Grade(
                    r.getStudent().getStudentId(),      // student_id
                    r.getGrades().get(0).getClassId(),  // class_id
                    activityId,                         // activity_id
                    null,                               // grade
                    totalScore                          // max_grade
                );
                // Fill all cells inside column to be empty
                r.getGrades().add(index, grade);
            }
        } catch (SQLException e) {}            
    }
    
    public int getActivityIdInDB(int classId, String name, int term, String schoolYear) throws SQLException {
        String insertQuery = 
            "SELECT activity_id " +
            "FROM activities a " +
            "JOIN classes c ON c.class_id = a.class_id " +
            "WHERE a.class_id = ? AND a.activity_name = ? AND a.term = ? AND c.academic_year = ?;";
            
        System.out.println("classId: " + classId + ", " + "name: " + name + ", " + "term: " + term + ", " + "schoolYear: " + schoolYear);
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, classId);
            pstmt.setString(2, name);
            pstmt.setInt(3, term);
            pstmt.setString(4, schoolYear);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("activity_id");
            } else {
                throw new SQLException("Error: Activity does not exist.");
            }
        }
    }
    
    public int getGradeIdInDB(int studentId, int classId, int activityId) throws SQLException {
        String insertQuery = 
            "SELECT grade_id " +
            "FROM grades g " +
            "WHERE g.student_id = ? AND g.class_id = ? AND g.activity_id = ?;";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, classId);
            pstmt.setInt(3, activityId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("grade_id");
            } else {
                throw new SQLException("Error: Grade does not exist.");
            }
        }
    }
    
    public void addSeatworkToTable(double totalScore) {
        List<String> columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Seatwork")).collect(Collectors.toList());
        String newActivityName;
        int index;

        if (!columns.isEmpty()) {
            String[] s = columns.getLast().split(" ");
            newActivityName = s[0] + " " + (Integer.parseInt(s[s.length-1])+1);
        } else {
            newActivityName = "Seatwork 1";
            columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Student name")).collect(Collectors.toList());
        }

        // Get index
        index = getClassRecord().findColumn(columns.getLast());
        
        addNewActivity(index-1, newActivityName, totalScore, 1);
    }
    
    public void addHWToTable(double totalScore) {
        List<String> columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Assignment")).collect(Collectors.toList());
        String newActivityName;
        int index;

        if (!columns.isEmpty()) {
            String[] s = columns.getLast().split(" ");
            newActivityName = s[0] + " " + (Integer.parseInt(s[s.length-1])+1);
        } else {
            newActivityName = "Assignment 1";
            columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Seatwork")).collect(Collectors.toList());
        }

        // Get index
        index = getClassRecord().findColumn(columns.getLast());
        
        addNewActivity(index-1, newActivityName, totalScore, 2);
    }
    
    public void addPTToTable(double totalScore) {
        List<String> columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Performance Task")).collect(Collectors.toList());
        String newActivityName;
        int index;

        if (!columns.isEmpty()) {
            String[] s = columns.getLast().split(" ");
            newActivityName = s[0] + " " + s[1] + " " + (Integer.parseInt(s[s.length-1])+1);
        } else {
            newActivityName = "Performance Task 1";
            columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Assignment")).collect(Collectors.toList());
        }

        // Get index
        index = getClassRecord().findColumn(columns.getLast());
        
        addNewActivity(index-1, newActivityName, totalScore, 3);
    }
    
    public void addQuizToTable(double totalScore) {
        List<String> columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Quiz")).collect(Collectors.toList());
        String newActivityName;
        int index;

        if (!columns.isEmpty()) {
            String[] s = columns.getLast().split(" ");
            newActivityName = s[0] + " " + (Integer.parseInt(s[s.length-1])+1);
        } else {
            newActivityName = "Quiz 1";
            columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Performance Task")).collect(Collectors.toList());
        }

        // Get index
        index = getClassRecord().findColumn(columns.getLast());
        
        addNewActivity(index-1, newActivityName, totalScore, 4);
    }
    
    public void addExamToTable(double totalScore) {
        List<String> columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Exam")).collect(Collectors.toList());
        String newActivityName;
        int index;

        if (!columns.isEmpty()) {
            String[] s = columns.getLast().split(" ");
            newActivityName = s[0] + " " + (Integer.parseInt(s[s.length-1])+1);
        } else {
            newActivityName = "Exam 1";
        }

        // Get index
        index = getClassRecord().findColumn(columns.getLast());

        addNewActivity(index-1, newActivityName, totalScore, 5);
    }
    
    // Connect to a database
    private Connection connectToDB(String schema) throws SQLException {
        try {
            serverConn = DriverManager.getConnection(url, user, password);
            if (!dbExists(schema)) {
                System.out.println("Error: Schema '" + schema + "' does not exist. Creating...");
                createSchema(schema);
            }
        } catch (SQLException e) {}
        
        return DriverManager.getConnection(url + schema, user, password);
    }
    
    private List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        
        try {
            Statement s = getConnection().createStatement();
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
    
    private List<Activity> getActivitiesInClassRecord(String className, String subjectName, int term) {
        List<Activity> activityList = new ArrayList<>();
        
        try {
            PreparedStatement ps = getConnection().prepareStatement("""
                SELECT DISTINCT a.activity_type_id, a.activity_id, a.activity_name, max_grade, term
                FROM grades g
                JOIN activities a ON g.activity_id = a.activity_id
                JOIN classes c ON g.class_id = c.class_id
                WHERE c.section = ? AND c.subject = ? AND a.term = ?;                                            
            """);
            ps.setString(1, className);
            ps.setString(2, subjectName);
            ps.setInt(3, term);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                activityList.add(new Activity(
                    rs.getInt("activity_id"),
                    rs.getString("activity_name"),
                    rs.getInt("activity_type_id"),
                    rs.getDouble("max_grade"),
                    rs.getInt("term")
                ));
            }
            
        } catch (SQLException e) {}
        
        return activityList;
    }
    
    private List<String> getActivityNamesInClassRecord(String className, String subjectName, int term) {
        List<String> activityNames = new ArrayList<>();
        
        try {
            PreparedStatement ps = getConnection().prepareStatement("""
                SELECT DISTINCT a.activity_type_id, a.activity_name
                FROM grades g
                JOIN activities a ON g.activity_id = a.activity_id
                JOIN classes c ON g.class_id = c.class_id
                WHERE c.section = ? AND c.subject = ? AND a.term = ?
                ORDER BY a.activity_type_id, a.activity_name;                                               
            """);
            ps.setString(1, className);
            ps.setString(2, subjectName);
            ps.setInt(3, term);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                activityNames.add(rs.getString("activity_name"));
            }
            
        } catch (SQLException e) {}
        
        return activityNames;
    }
    
    private List<Row> getClassRecordInDB(String className, String subjectName, int term) {
        List<Row> rows = new ArrayList<>();
        
        try {
            PreparedStatement ps = getConnection().prepareStatement("""
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
            ps.setInt(3, term);
            
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
                
                Double grade;
                
                if (rs.getString("grade") == null) {
                    grade = null;
                } else {
                    grade = Double.parseDouble(rs.getString("grade"));
                }

                Grade g = new Grade(
                        rs.getInt("grade_id"), 
                        rs.getInt("student_id"), 
                        rs.getInt("class_id"), 
                        rs.getInt("activity_id"), 
                        grade,
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
            ResultSet rs = serverConn.getMetaData().getCatalogs();
            
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
            Connection connection = DriverManager.getConnection(url + name, user, password);
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
    
    private boolean studentExists(String firstName, String lastName) throws SQLException {
        String checkQuery = "SELECT COUNT(*) FROM students WHERE first_name = ? AND last_name = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(checkQuery)) {
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
            String insertQuery = "INSERT INTO students (first_name, last_name) VALUES (?, ?)";
            try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                pstmt.executeUpdate();
                System.out.println("Student added successfully.");
            }
        } else {
            System.out.println("Student already exists.");
        }
    }
    
    public void deleteStudent(Student student) throws SQLException {
        String insertQuery = "DELETE FROM students WHERE student_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, student.getStudentId());
            pstmt.executeUpdate();
            System.out.println("Student deleted successfully.");
        }
    }
    
    public void addActivityToDB(int classId, String activity_name, double maxGrade, int activityTypeId, int term) {
        String insertQuery = "INSERT INTO activities (class_id, activity_name, max_grade, activity_type_id, term) " +
                             "VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
                pstmt.setInt(1, classId);
                pstmt.setString(2, activity_name);
                pstmt.setDouble(3, maxGrade);
                pstmt.setInt(4, activityTypeId);
                pstmt.setInt(5, term);
                pstmt.executeUpdate();
        } catch (SQLException e) {}
    }
    
    public Grade getGradeById(int id) {
        String query = 
                "SELECT grade_id, student_id, class_id, g.activity_id, grade, max_grade" + 
                "FROM encoder_data.grades g" +
                "JOIN activities a ON g.activity_id = a.activity_id" +
                "WHERE grades.grade_id = ?";
        
        ResultSet rs;
        Grade grade = null;
        
        try (PreparedStatement ps = getConnection().prepareStatement(query)) {
            ps.setInt(1, id);
            rs = ps.executeQuery();
            grade = new Grade(
                rs.getInt("grade_id"), 
                rs.getInt("student_id"), 
                rs.getInt("class_id"),
                rs.getInt("activity_id"),
                rs.getDouble("grade"),
                rs.getDouble("max_grade")
            );
        } catch (SQLException e) {}
        
        
        return grade;
    }
    
    public int getClassId(String section, String subject) {
        int id = -1;
        
        String query = "SELECT class_id FROM classes c WHERE c.section = ? AND s.subject = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setString(1, section);
            pstmt.setString(2, subject);
            ResultSet rs = pstmt.executeQuery();
            id = rs.getInt("class_id");
        } catch (SQLException e) {}
        
        return id;
    }
    
    public int getStudentIdInDB(String firstName, String lastName) throws SQLException {
        if (studentExists(firstName, lastName)) {
            String insertQuery = "SELECT student_id FROM students s WHERE s.first_name = ? AND s.last_name = ?";
            try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
                pstmt.setString(1, firstName);
                pstmt.setString(2, lastName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("student_id");
                } else {
                    throw new SQLException("Error: Student does not exist.");
                }
            }  
        } else {
            throw new SQLException("Error: Student does not exist.");
        }
    }
    
    private void addGradeToDB(int studentId, int classId, int activity_id, Double grade) {
        String insertQuery = "INSERT INTO grades (student_id, class_id, activity_id, grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, classId);
            pstmt.setInt(3, activity_id);
            pstmt.setDouble(4, grade);
            
            pstmt.executeUpdate();
            System.out.println("Grade added successfully.");
        } catch (SQLException e) {}
    }
    
    public void addEmptyGradeToDB(int studentId, int classId, int activity_id) {
        String insertQuery = "INSERT INTO grades (student_id, class_id, activity_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, classId);
            pstmt.setInt(3, activity_id);
            pstmt.executeUpdate();
            System.out.println("Empty grade added successfully.");
        } catch (SQLException e) {}
    }
    
    public List<Grade> getGrades(int studentId, int classId) {
        String query = 
                "SELECT grade_id, student_id, class_id, g.activity_id, grade, max_grade " +
                "FROM grades g " + 
                "JOIN activities a ON g.activity_id = a.activity_id " +
                "WHERE student_id = ? AND class_id = ?;";
        
        List<Grade> gradeList = new ArrayList();
        Double grade;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, classId);
            ResultSet rs = pstmt.executeQuery();
         
            System.out.println("studentId: " + studentId + "\t" + "classId: " + classId);
            
            while (rs.next()) {
                if (rs.getString("grade") == null) {
                     grade = null;
                } else {
                    grade = Double.parseDouble(rs.getString("grade"));
                }
                
                gradeList.add(new Grade(
                    rs.getInt("grade_id"), 
                    rs.getInt("student_id"), 
                    rs.getInt("class_id"), 
                    rs.getInt("activity_id"), 
                    grade,
                    Double.parseDouble(rs.getString("max_grade"))
                ));
            }
        } catch (SQLException e) {}
        
        System.out.println("GradeList size: " + gradeList.size());
        return gradeList;
    }
}

/**
 * Class representing ClassRecord row data
 * Contains Student object and a list of Activity objects
 */
class Row {
    private Student student;
    private List<Grade> gradesList;

    public Row(Student student, List<Grade> gradesList) {
        this.student = student;
        this.gradesList = gradesList;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public List<Grade> getGrades() {
        return gradesList;
    }
    
    public void setGrades(List<Grade> gradesList) {
        this.gradesList = gradesList;
    }
    
    public void setGradesAt(int index, Double value) {
        if (!(gradesList.get(index) == null)) {
            gradesList.get(index).setGrade(value);
        }
    }

    public int getActivityGrade(int index) {

        /**
         * TODO
         * 
        try {
            Statement s = db.createStatement();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        

        return activities.get(index).getFormattedGrade();
        */
        
        return 0;
    }
}

/**
 * Custom table for representing a class record in the GUI
 */
class ClassRecord extends AbstractTableModel {
    private List<Row> classList;
    private List<String> columnNames;
    // Class Record Metadata
    private String section;
    private String subject;
    private Integer term;
    private String schoolYear;

    public ClassRecord(String section, String subject, int term, String schoolYear, List<Row> classList, List<String> columnNames) {
        this.section = section;
        this.subject = subject;
        this.term = term;
        this.schoolYear = schoolYear;
        this.classList = classList;
        this.columnNames = columnNames;
    }
    
    /* UNUSED CODE
    public ClassRecord() {
        this.section = "";
        this.subject = "";
        this.term = null;
        this.schoolYear = "";
        classList = new ArrayList<>();
        columnNames = new ArrayList<>();
    }
    */
    
    public void insertColumn(int index, String value) {
        // Insert new column
        getColumns().add(index+1, value);

        fireTableStructureChanged();
    }
    
    public int getClassId() {
        return classList.getFirst().getGrades().getFirst().getClassId();
    }
    
    public String getSection() {
        return section;
    }
    public String getSubject() {
        return subject;
    }
    public int getTerm() {
        return term;
    }
    public String getSY() {
        return schoolYear;
    }
    public String getFormattedTerm() {
        switch(term) {
            case 1:
                return "1st Quarter";
            case 2:
                return "2nd Quarter";
            case 3:
                return "3rd Quarter";
            case 4:
                return "4th Quarter";
            default:
                return "Invalid";
        }
    }

    public Row getRowAt(int index) {
        return classList.get(index);
    }
    
    public List<Row> getClassList() {
        return classList;
    }
    
    @Override
    public int getRowCount() {
        return classList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int index) {
        return columnNames.get(index);
    }
    
    public List<String> getColumns() {
        return columnNames;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Row r = classList.get(row);
        
        int colModulo = col % (columnNames.size());
        
        if (colModulo == 0) {
            return classList.indexOf(r)+1;
        } else if (colModulo == 1) {
            return r.getStudent().getStudentNameFormatted();
        } else {
            if (r.getGrades().get(colModulo-2) == null) {
                return null;
            }
            return r.getGrades().get(colModulo-2).getGrade();
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col > 0; // Make only certain columnNames editable
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Row obj = classList.get(row);
        
        int colModulo = col % (columnNames.size());
                
        if (colModulo > 1) {
            if (value == null) {
                obj.setGradesAt(colModulo-1, null);
            } else {
                obj.setGradesAt(colModulo-1, Double.parseDouble( (String) value ));
            }
        }
        
        fireTableCellUpdated(row, col);
    }
    
    public void moveRow(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= classList.size() || toIndex >= classList.size()) {
            throw new IndexOutOfBoundsException("Invalid row index");
        }

        // Swap the rows
        Row row = classList.remove(fromIndex);
        classList.add(toIndex, row);

        // Notify the table about the data change
        fireTableRowsDeleted(fromIndex, fromIndex);
        fireTableRowsInserted(toIndex, toIndex);
    }
}
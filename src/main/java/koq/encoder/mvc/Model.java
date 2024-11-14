package koq.encoder.mvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import koq.encoder.classes.Activity;
import koq.encoder.classes.Grade;
import koq.encoder.classes.Student;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import koq.encoder.classes.ClassRecord;
import koq.encoder.classes.Row;

public class Model {

    private int selectedRow;
    private int selectedColumn;
    private int selectedActivity;
    private List<Student> studentList;
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
    private String password = "password";
    
    public String DATA_DIRECTORY = System.getProperty("user.dir") + "\\resources\\data\\";
    
    public static enum Fields {
        EDIT_GRADE,
        SELECT_ACTIVITY,
        SELECT_ACTIVITY_TYPE,
        MAX_GRADE
    };
    
    public static enum Actions {
        NEW_RECORD,
        OPEN_RECORD,
        EXPORTFILE,         // UNUSED
        EXIT,
        ADD_TO_TABLE,
        REMOVE_FROM_TABLE,
        EDIT_GRADE_WEIGHTS,
        GENERATE_REPORT,
        ADDSTUDENT,
        ADD_MENU_CLICKED,
        ADDACTIVITY,
        ADDASSIGNMENT,
        ADDPT,
        ADDQUIZ,
        ADDEXAM,
        PREVIOUS_STUDENT,
        NEXT_STUDENT,
        MOVE_ROW_DOWN,
        MOVE_ROW_UP,
        VIEWABOUT,
        VIEWSHORTCUTS
    }
    
    public Model() {
        // Create data directory if it doesn't exist
        File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            boolean created = directory.mkdir(); // Creates the directory
            if (created) {
                System.out.println("Directory created successfully.");
            } else {
                System.out.println("Failed to create directory.");
            }
        }
        
        // NOTE: Placeholder values, change these later
        selectedRow = 0;
        selectedColumn = 0;
        selectedActivity = 0;
        
        addStudentFirstName = null;
        addStudentLastName = null;
        
        try {
            db = connectToDB("encoder_data");
        } catch (SQLException e) { e.printStackTrace(); }
        
        studentList = getAllStudents();         // UNUSED
        
        record = null;
        
        currentClassRecord = record;
    }
    
    private Connection getConnection() {
        return db;
    }
    
    public ClassRecord getClassRecord() {
        return record;
    }
    
    public void setClassRecord(ClassRecord record) {
        this.record = record;
    }
    
    public void initClassRecord(ClassRecord record) {
        try {
            record.setClassList(fetchStudentsInClassRecord(record.getClassId()));
            System.out.println("Student records retrieved. Size: " + record.getClassList().size());
        } catch(SQLException e) { e.printStackTrace(); }
        
        getGradeRecordsInDB(record.getClassList(), record.getClassId());
        System.out.println("Grade records retrieved");
        
        record.setColumnNames(getActivityNamesInClassRecord(record.getClassId()));
        System.out.println("Column names set. No. of columns: " + record.getColumnCount());
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

    /*
    public void setTableModel(AbstractTableModel data, List<String> columnNames) {
        tableModel = data;
        this.columnNames = columnNames;
    }
    */
    
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
    
    public String serializeClassRecord(ClassRecord record) {
        String fileName = String.format("%d_%s_Q%d_%s_%s.ser", 
            record.getGradeLevel(), record.getSection(), 
            record.getTerm(), record.getSY(), record.getSubject()
        );
        
        try (FileOutputStream fileOut = new FileOutputStream(DATA_DIRECTORY + fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(record);
            System.out.println("Object serialized to " + fileName);
            
        } catch (IOException e) { e.printStackTrace(); }
        return fileName;
    }
    
    public ClassRecord deserializeClassRecord(File file) {
        ClassRecord record = null;

        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            record = (ClassRecord) in.readObject();
            System.out.println("Object deserialized: class_id = " + record.getClassId());
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        
        return record;
    }
    
     public ClassRecord deserializeClassRecord(String fileName) {
        ClassRecord record = null;

        try (FileInputStream fileIn = new FileInputStream(DATA_DIRECTORY + fileName);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            record = (ClassRecord) in.readObject();
            System.out.println("Object deserialized: class_id = " + record.getClassId());
        } catch (IOException | ClassNotFoundException i) {
            i.printStackTrace();
        }
        
        return record;
    }
    
    /**
     * Add student to database, retrieve their data, create a Student object and add it
     * to the current ClassRecord
     */
    public void addStudentToClassRecord(String firstName, String lastName) {
        try {
            addStudent(firstName, lastName);
            
            int id = getStudentIdInDB(firstName, lastName);
            
            Student s = new Student(id, firstName, lastName);
            
            addStudentToClass(s.getStudentId(), getClassRecord().getClassId());
            
            Row row = new Row(s, new ArrayList<Grade>(), null);

            // Populate the row with empty grade values if there are student entries in the ClassRecord
            if (getClassRecord().getClassList().size() != 0) {
                for (Grade g: getClassRecord().getClassList().getFirst().getGrades()) {
                    addEmptyGradeToDB(s.getStudentId(), g.getClassId(), g.getActivityId());
                }
            }
            
            row.setGrades(getGrades(s.getStudentId(), getClassRecord().getClassId()));
            
            for (Grade g: row.getGrades()) {
                System.out.println(g.getActivityId());
            }

            getClassRecord().getClassList().add(row);
            getClassRecord().fireTableDataChanged();
        } catch (SQLException err) {}
    }
    
    public void addNewActivity(int index, String name, Double totalScore, int activityTypeId) {
        int activityId;
        int gradeId;
        
        try {
            addActivityToDB(getClassRecord().getClassId(), name, totalScore, activityTypeId);
            activityId = getActivityIdInDB(getClassRecord().getClassId(), name);
            
            getClassRecord().insertColumn(index+1, name);
            
            for (Row r: getClassRecord().getClassList()) {
                addEmptyGradeToDB(
                    r.getStudent().getStudentId(),
                    getClassRecord().getClassId(),
                    activityId
                );
                
                gradeId = getGradeIdInDB(
                    r.getStudent().getStudentId(),
                    getClassRecord().getClassId(),
                    activityId
                );
                
                Grade grade = new Grade(
                    gradeId,                            // grade_id
                    r.getStudent().getStudentId(),      // student_id
                    getClassRecord().getClassId(),      // class_id
                    activityId,                         // activity_id
                    null,                               // grade
                    totalScore                          // max_grade
                );
                // Fill all cells inside column to be empty
                r.getGrades().add(index, grade);
            }
        } catch (SQLException e) { e.printStackTrace(); }            
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
    
    public void updateGrade(int gradeId, Double value) {
        String updateQuery = "UPDATE grades SET grade = ? WHERE grade_id = ?;";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(updateQuery)) {
            pstmt.setDouble(1, value);
            pstmt.setInt(2, gradeId);
            pstmt.executeUpdate();
            System.out.println("Grade updated");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public void addSeatworkToTable(double totalScore) {
        List<String> columns;
        columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Seatwork")).collect(Collectors.toList());
        String newActivityName;
        int index;

        if (!columns.isEmpty()) {
            String[] s = columns.getLast().split(" ");
            newActivityName = s[0] + " " + (Integer.parseInt(s[s.length-1])+1);
        } else {
            newActivityName = "Seatwork 1";
            columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Student Name")).collect(Collectors.toList());
        }

        // Get index
        index = getClassRecord().findColumn(columns.getLast());
        
        addNewActivity(index-1, newActivityName, totalScore, 1);
    }
    
    public void addHWToTable(double totalScore) {
        List<String> columns = getClassRecord().getColumns().stream()
                .filter(e -> e.contains("Homework")).collect(Collectors.toList());
        String newActivityName;
        int index;

        if (!columns.isEmpty()) {
            String[] s = columns.getLast().split(" ");
            newActivityName = s[0] + " " + (Integer.parseInt(s[s.length-1])+1);
        } else {
            newActivityName = "Homework 1";
            
            // Get index to insert
            List<String> keywords = Arrays.asList("Seatwork", "Student Name");
            for (String keyword : keywords) {
                columns = getClassRecord().getColumns().stream()
                    .filter(e -> e.contains(keyword))
                    .collect(Collectors.toList());
                if (!columns.isEmpty()) break;
            }
        }
        
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
            
            // Get index to insert
            List<String> keywords = Arrays.asList("Homework", "Seatwork", "Student Name");
            for (String keyword : keywords) {
                columns = getClassRecord().getColumns().stream()
                    .filter(e -> e.contains(keyword))
                    .collect(Collectors.toList());
                if (!columns.isEmpty()) break;
            }
        }

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
            
            // Get index to insert
            List<String> keywords = Arrays.asList("Performance Task", "Homework", "Seatwork", "Student Name");
            for (String keyword : keywords) {
                columns = getClassRecord().getColumns().stream()
                    .filter(e -> e.contains(keyword))
                    .collect(Collectors.toList());
                if (!columns.isEmpty()) break;
            }
        }

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
            
            // Get index to insert
            List<String> keywords = Arrays.asList("Quiz", "Performance Task", "Homework", "Seatwork", "Student Name");
            for (String keyword : keywords) {
                columns = getClassRecord().getColumns().stream()
                    .filter(e -> e.contains(keyword))
                    .collect(Collectors.toList());
                if (!columns.isEmpty()) break;
            }
        }

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
        } catch (SQLException e) { e.printStackTrace(); }
        
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
                WHERE c.section = ? AND c.subject = ? AND c.term = ?;                                            
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
            
        } catch (SQLException e) { e.printStackTrace(); }
        
        return activityList;
    }
    
    private List<String> getActivityNamesInClassRecord(int classId) {
        List<String> activityNames = new ArrayList<>();
        
        try {
            PreparedStatement ps = getConnection().prepareStatement("""
                SELECT DISTINCT a.activity_name, a.activity_type_id
                FROM activities a
                LEFT JOIN grades g ON g.activity_id = a.activity_id
                JOIN classes c ON a.class_id = c.class_id
                WHERE c.class_id = ?
                ORDER BY a.activity_type_id, a.activity_name;                                        
            """);
            ps.setInt(1, classId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                activityNames.add(rs.getString("activity_name"));
            }
            
        } catch (SQLException e) { e.printStackTrace(); }
        
        return activityNames;
    }
    
    public void initTable(JTable table, ClassRecord clsRec) {
        table.setModel(clsRec);
        
        table.getTableHeader().addMouseListener(new HeaderSelector(table));
        table.addMouseListener(new RowSelector(table));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        System.out.println("Table model set");
    }
    
    /**
     * Retrieve all student records belonging to a class using class_id.
     * Returns a list of rows with all the student objects and empty grades list.
     * Must call getClassRecordInDB() to populate the grades.
     * Must call getComputedGradesInDB() to populate the computed grades
     */
    private List<Row> fetchStudentsInClassRecord(int classId) throws SQLException {
        List<Row> rows = new ArrayList<>();
        
        try {
            PreparedStatement ps = getConnection().prepareStatement("""
                SELECT s.student_id, s.first_name, s.last_name
                FROM students s
                JOIN student_classes sc ON s.student_id = sc.student_id
                JOIN classes c ON sc.class_id = c.class_id
                WHERE c.class_id = ?
                ORDER BY s.student_id;
            """);
            ps.setInt(1, classId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                rows.add(new Row(
                    new Student(
                        rs.getInt("student_id"), 
                        rs.getString("first_name"), 
                        rs.getString("last_name")
                    ), 
                    new ArrayList<Grade>(), 
                    null
                ));
            }
            
        } catch(SQLException e) {}
        
        return rows;
    }
    
    /**
     * Add a class record into the database. Must call getClassId() to get the corresponding class_id
     */
    public void addClassRecordInDB(int gradeLevel, String section, String subject, int term, String schoolYear) {
        try {
            PreparedStatement ps = getConnection().prepareStatement("""
                INSERT INTO classes
                (grade_level, section, subject, term, academic_year)
                VALUES
                (?, ?, ?, ?, ?);
            """);
            ps.setInt(1, gradeLevel);
            ps.setString(2, section);
            ps.setString(3, subject);
            ps.setInt(4, term);
            ps.setString(5, schoolYear);
            ps.executeUpdate();
            System.out.println("Added class record to database");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    /**
     * Retrieves a class record from the database
     */
    public ClassRecord getClassRecordInDB(int classId) throws SQLException {
        ClassRecord record = null;
        
        try {
            PreparedStatement ps = getConnection().prepareStatement("""
                SELECT *
                FROM classes
                WHERE class_id = ?;
            """);
            ps.setInt(1, classId);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                record = new ClassRecord(
                    rs.getInt("class_id"), 
                    rs.getInt("grade_level"),
                    rs.getString("section"), 
                    rs.getString("subject"), 
                    rs.getInt("term"), 
                    rs.getString("academic_year")
                );
                System.out.println("Retrieved class record details. Class record object created");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        if (record != null) {
            return record;
        } else {
            throw new SQLException("Class record with class_id = " + classId + " not found");
        }
    }
    
    /**
     * Retrieve grades from database given the student list
     */
    private void getGradeRecordsInDB(List<Row> rows, int classId) {
        try {
            for (Row r: rows) {                
                PreparedStatement ps = getConnection().prepareStatement("""
                    SELECT g.grade_id, g.class_id, g.student_id, g.activity_id, g.grade, a.max_grade
                    FROM grades g
                    JOIN student_classes sc ON g.student_id = sc.student_id AND g.class_id = sc.class_id
                    JOIN students s ON g.student_id = s.student_id
                    JOIN activities a ON g.activity_id = a.activity_id
                    JOIN classes c ON g.class_id = c.class_id
                    WHERE c.class_id = ? AND s.student_id = ?
                    ORDER BY a.activity_type_id, a.activity_name;
                """);
                ps.setInt(1, classId);
                ps.setInt(2, r.getStudent().getStudentId());

                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {

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

                    r.getGrades().add(g);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    /**
     * Retrieve the computed grades of a student in a class from the database
     * 
     * NOTE: Currently WIP. Add TODO
     */
    private Double getComputedGradesInDB(int classId, int studentId) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("""
            SELECT cg.raw_grade
            FROM computed_grades cg
            JOIN students s ON cg.student_id = s.student_id
            JOIN classes c ON c.class_id = c.class_id
            WHERE c.class_id = ? AND student_id = ?;
        """);
        ps.setInt(1, classId);
        ps.setInt(2, studentId);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getDouble("raw_grade");
        } else {
            throw new SQLException("class_id " + classId + " and student_id " + studentId + " not found.");
        }
    }
    
    /**
     * Check if the database named with the String parameter exists
     */
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
    
    /**
     * Creates a new schema with all the tables if it does not exist
     * NOTE: Update this later
     */
    private void createSchema(String name) {
        try {
            Statement s = serverConn.createStatement();
            s.executeUpdate("CREATE SCHEMA " + name + ";");
            
            // After creating schema, connect to database and create tables
            Connection connection = DriverManager.getConnection(url + name, user, password);
            s = connection.createStatement();
            
            s.executeUpdate("""
                CREATE TABLE activity_types (
                    activity_type_id INT PRIMARY KEY NOT NULL,
                    activity_type_name VARCHAR(50) NOT NULL
                );
            """);
            
            s.executeUpdate("INSERT INTO activity_types (activity_type_id, activity_type_name) VALUES ('1', 'Seatwork');");
            s.executeUpdate("INSERT INTO activity_types (activity_type_id, activity_type_name) VALUES ('2', 'Assignment');");
            s.executeUpdate("INSERT INTO activity_types (activity_type_id, activity_type_name) VALUES ('3', 'Performance Task');");
            s.executeUpdate("INSERT INTO activity_types (activity_type_id, activity_type_name) VALUES ('4', 'Quiz');");
            s.executeUpdate("INSERT INTO activity_types (activity_type_id, activity_type_name) VALUES ('5', 'Exam');");
            System.out.println("activity_types table created");
            
            s.executeUpdate("""
                CREATE TABLE classes (
                    class_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                    grade_level INT NOT NULL,
                    section VARCHAR(50) NOT NULL,
                    subject VARCHAR(100) NOT NULL,
                    term INT NOT NULL,
                    academic_year VARCHAR(10) NOT NULL
                );
            """);
            System.out.println("classes table created");

            s.executeUpdate("""
                CREATE TABLE activities (
                    activity_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                    class_id INT NOT NULL,
                    activity_name VARCHAR(30) NOT NULL,
                    max_grade DECIMAL(5, 2) NOT NULL,
                    activity_type_id INT NOT NULL,
                    FOREIGN KEY (class_id) REFERENCES classes(class_id),
                    FOREIGN KEY (activity_type_id) REFERENCES activity_types(activity_type_id)
                );
            """);
            System.out.println("activities table created");

            s.executeUpdate("""
                CREATE TABLE students (
                    student_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
                    first_name VARCHAR(50) NOT NULL,
                    last_name VARCHAR(50) NOT NULL
                );
            """);
            System.out.println("students table created");
            
            s.executeUpdate("""
                CREATE TABLE grades (
                  grade_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                  student_id INT NOT NULL,
                  class_id INT NOT NULL,
                  activity_id INT NOT NULL,
                  grade DECIMAL(5,2) DEFAULT NULL,
                  FOREIGN KEY (student_id) REFERENCES Students(student_id),
                  FOREIGN KEY (activity_id) REFERENCES Activities(activity_id)
                );
            """);
            System.out.println("grades table created");
            
            s.executeUpdate("""                      
                CREATE TABLE student_classes (
                  student_class_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                  class_id int NOT NULL,
                  student_id int NOT NULL,
                  FOREIGN KEY (class_id) REFERENCES classes (class_id),
                  FOREIGN KEY (student_id) REFERENCES students (student_id)
                );
            """);
            System.out.println("student_classes table created");
            
            s.executeUpdate("""
                CREATE TABLE grade_weights (
                    grade_weight_id INT AUTO_INCREMENT PRIMARY KEY,
                    class_id INT NOT NULL,
                    seatwork_weight DECIMAL(4, 2) NOT NULL,
                    homework_weight DECIMAL(4, 2) NOT NULL,
                    quiz_weight DECIMAL(4, 2) NOT NULL,
                    performance_task_weight DECIMAL(4, 2) NOT NULL,
                    exam_weight DECIMAL(4, 2) NOT NULL,
                    FOREIGN KEY (class_id) REFERENCES classes(class_id)
                );
            """);
            System.out.println("grade_weights table created");
            
            s.executeUpdate("""
                CREATE TABLE final_grades (
                    final_grade_id INT PRIMARY KEY AUTO_INCREMENT,
                    student_class_id INT NOT NULL,
                    final_grade DECIMAL(5, 2) NOT NULL,
                    FOREIGN KEY (student_class_id) REFERENCES student_classes(student_class_id)
                );
            """);
            System.out.println("final_grades table created");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Schema " + name + " created.");
    }
    
    /**
     * Creates a new class-student relationship in the database
     */
    public void addStudentToClass(int studentId, int classId) throws SQLException {
        String insertQuery = "INSERT INTO student_classes (student_id, class_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            System.out.println("Student added to class successfully.");
        }
    }
    
    /**
     * Adds a new student to the database if they do not exist yet. getStudentInDB() must be called
     * after this query to get the assigned student_id
     */
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
    
    /**
     * Check if student already exists in the database
     */
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
    
    /**
     * Check if class record already exists in the database
     */
    public boolean classRecordExists(int gradeLevel, String section, String subject, int term, String schoolYear) {
        String checkQuery = """
           SELECT class_id FROM classes c 
           WHERE 
                c.grade_level = ? AND
                c.section = ? AND 
                c.subject = ? AND 
                c.term = ? AND 
                c.academic_year = ?
        """;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(checkQuery)) {
            pstmt.setInt(1, gradeLevel);
            pstmt.setString(2, section);
            pstmt.setString(3, subject);
            pstmt.setInt(4, term);
            pstmt.setString(5, schoolYear);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // If count > 0, class record exists
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return false; // Class record does not exist
    }
    
    /**
     * Removes a student-class relation in the database (i.e., student no longer in the class).
     * Student and class will not be affected
     */
    public void removeStudentFromClass(int studentId, int classId) throws SQLException {
        String insertQuery = "DELETE FROM student_classes WHERE student_id = ? AND class_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, classId);
            pstmt.executeUpdate();
            System.out.println("Student deleted from class successfully.");
        }
    }
    
    /**
     * Deletes an activity record in the database. This method is called whenever an activity
     * is deleted in a class record
     */
    public void deleteActivity(int activityId) throws SQLException {
        String deleteQuery = "DELETE FROM activities WHERE activity_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(deleteQuery)) {
            pstmt.setInt(1, activityId);
            pstmt.executeUpdate();
            System.out.println("Activity deleted successfully.");
        }
    }
    
    /**
     * Retrieves activity_id from database based on class_id and activity_name
     */
    public int getActivityIdInDB(int classId, String activityName) throws SQLException {
        String insertQuery = """
            SELECT activity_id
            FROM activities a
            JOIN classes c ON c.class_id = a.class_id
            WHERE c.class_id = ? AND a.activity_name = ?;
        """;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, classId);
            pstmt.setString(2, activityName);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("activity_id: " + rs.getInt("activity_id"));
                return rs.getInt("activity_id");
            } else {
                throw new SQLException(
                        "activity_id with class_id = " + classId + 
                        ", activity_name = " + activityName + " not found"
                );
            }
        }
    }
    
    /**
     * Deletes a grade record in the database. This method is called whenever an activity
     * or a user is deleted in a class record
     */
    public void deleteGradeInDB(Grade grade) throws SQLException {
        String insertQuery = "DELETE FROM grades WHERE grade_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, grade.getGradeId());
            pstmt.executeUpdate();
            System.out.println("Grade deleted successfully.");
        }
    }
    
    /**
     * Add a new activity record to the database. This method is called when a new activity is added
     * to the class record by the user
     */
    public void addActivityToDB(int classId, String activity_name, double maxGrade, int activityTypeId) {
        String insertQuery = "INSERT INTO activities (class_id, activity_name, max_grade, activity_type_id) " +
                             "VALUES (?, ?, ?, ?);";

        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
                pstmt.setInt(1, classId);
                pstmt.setString(2, activity_name);
                pstmt.setDouble(3, maxGrade);
                pstmt.setInt(4, activityTypeId);
                pstmt.executeUpdate();
                System.out.println("New activity added to DB");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    /**
     * Note: Currently unused
     */
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
        } catch (SQLException e) { e.printStackTrace(); }
        
        
        return grade;
    }
    
    /**
     * Retrieve a ClassRecord's class_id from the database
     * Note: Currently unused.
     */
    public int getClassIdInDB(int gradeLevel, String section, String subject, int term, String schoolYear) {
        int id = -1;
        
        String query = """
           SELECT class_id FROM classes c 
           WHERE 
                c.grade_level = ? AND
                c.section = ? AND 
                c.subject = ? AND 
                c.term = ? AND 
                c.academic_year = ?
        """;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, gradeLevel);
            pstmt.setString(2, section);
            pstmt.setString(3, subject);
            pstmt.setInt(4, term);
            pstmt.setString(5, schoolYear);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                id = rs.getInt("class_id");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        if (id == -1) {
            throw new java.util.NoSuchElementException("Class not found");
        }
        return id;
    }
    
    /**
     * Retrieve student ID from the database. This function is called after a new student is
     * inserted into the database to get their assigned student_id. See usages.
     */
    public int getStudentIdInDB(String firstName, String lastName) throws SQLException {;
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
    
    // Add a grade record to the database with a grade value
    // UNUSED. Remove later
    private void addGradeToDB(int studentId, int classId, int activity_id, Double grade) {
        String insertQuery = "INSERT INTO grades (student_id, class_id, activity_id, grade) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, classId);
            pstmt.setInt(3, activity_id);
            pstmt.setDouble(4, grade);
            pstmt.executeUpdate();
            System.out.println("Grade added successfully.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    // Add a new grade record to the database with grade value set to null
    public void addEmptyGradeToDB(int studentId, int classId, int activity_id) {
        String insertQuery = "INSERT INTO grades (student_id, class_id, activity_id) VALUES (?, ?, ?);";
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, classId);
            pstmt.setInt(3, activity_id);
            pstmt.executeUpdate();
            System.out.println("Empty grade added successfully.");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    // Retrieve all grades for a specific student in a class from the database
    public List<Grade> getGrades(int studentId, int classId) {
        String query = 
                "SELECT g.grade_id, g.student_id, g.class_id, g.activity_id, grade, a.max_grade " +
                "FROM grades g " + 
                "JOIN activities a ON g.activity_id = a.activity_id " +
                "WHERE g.student_id = ? AND g.class_id = ?;";
        
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
        } catch (SQLException e) { e.printStackTrace(); }
        
        System.out.println("GradeList size: " + gradeList.size());
        return gradeList;
    }
}
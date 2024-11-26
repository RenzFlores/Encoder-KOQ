package koq.encoder.mvc;

import java.awt.Color;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import koq.encoder.classes.Grade;
import koq.encoder.classes.Student;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import koq.encoder.classes.*;
import koq.encoder.components.Constants;

public class Model {

    private int selectedRow;
    private int selectedColumn;
    private int selectedActivity;
    private int selectedTab;
    private List<Student> studentList;
    private ClassRecord record;
    private Faculty currentUser;
    
    private Connection db;
    private Connection serverConn;
    private String url = "jdbc:mysql://localhost:3306/";
    private String user = "user";
    private String password = "password";
    
    
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
        REGISTER_STUDENT_SYSTEM,
        LOGOUT,
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
    };
    
    public Model() {
        // Create data directory if it doesn't exist
        File directory = new File(Constants.DATA_DIRECTORY);
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
        selectedTab = 0;
        
        try {
            db = connectToDB();
        } catch (SQLException e) { e.printStackTrace(); }
        
        studentList = getAllStudents();
        
        record = null;
    }
    
    private Connection getConnection() {
        return db;
    }
    
    public void closeConnection() {
        try {
            db.close();
            serverConn.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public boolean checkForLogin(String name, char[] password) {
        String query = "SELECT teacher_id, password FROM faculty;";
        List<String> idList = new ArrayList<>();
        List<String> passwordList = new ArrayList<>();
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                idList.add(rs.getString("teacher_id"));
                passwordList.add(rs.getString("password"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        for (int i = 0; i < idList.size(); i++) {
            char[] passToCharArray = passwordList.get(i).toCharArray();
            
            // Compare the arrays
            if (name.equals(idList.get(i)) && Arrays.equals(password, passToCharArray)) {
                return true;    // Match
            }
        }
        
        return false;           // No match
    }
    
    public GradePeriod getGradePeriod(int quarter) {
        return record.getGradePeriod(quarter);
    }
    
    public ClassRecord getClassRecord() {
        return record;
    }
    
    /**
     * Creates a faculty object from the database given the teacher ID and password
     * This function is for logging in and called after a successful login attempt
     */
    public Faculty getFacultyFromDatabase(String teacher_id, char[] password) {
        String selectQuery = "SELECT * FROM faculty WHERE teacher_id = ? AND password = ?;";
        Faculty f = null;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, Integer.parseInt(teacher_id));
            pstmt.setString(2, String.valueOf(password));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                f = new Faculty(
                    rs.getInt("faculty_id"), 
                    rs.getInt("teacher_id"), 
                    rs.getString("name"),
                    rs.getString("role"),
                    rs.getString("password").toCharArray()
                );
            } else {
                throw new SQLException("Faculty not found");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        if (f != null) {
            return f;
        } else {
            throw new NullPointerException("Faculty object is null");
        }
    }
    
    public void setClassRecord(ClassRecord record) {
        this.record = record;
    }
    
    /**
     * Initializes a class record, fetching all records
     */
    public void initClassRecord(ClassRecord record) {
        try {
            record.setClassList(fetchStudentsInClassRecord(record.getClassId()));
            System.out.println("Student records retrieved. Size: " + record.getClassList().size());
        } catch(SQLException e) { e.printStackTrace(); }
        
        record.getGradePeriod(1).setColumnNames(getActivitiesInDB(record.getClassId(), 1));
        record.setGradePeriod(1, getGradePeriodInDB(record.getClassList(), record.getClassId(), 1));
        record.getGradePeriod(2).setColumnNames(getActivitiesInDB(record.getClassId(), 2));
        record.setGradePeriod(2, getGradePeriodInDB(record.getClassList(), record.getClassId(), 2));
        System.out.println("Grade records retrieved. Q1 columns=" + record.getGradePeriod(1).getColumns().size() + 
                ", Q2 columns=" + record.getGradePeriod(2).getColumns().size());
    }
    
    public List<Student> getStudentList() {
        return studentList;
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
    public void setSelectedTab(int index) {
        selectedTab = index;
    }
    public int getSelectedTab() {
        return selectedTab;
    }
    public void setCurrentUser(Faculty user) {
        currentUser = user;
    }
    public Faculty getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Get a student object from LRN
     */
    public Student getStudentInDB(int lrn) {
        String selectQuery = 
            "SELECT * " +
            "FROM students s " +
            "WHERE s.lrn = ?;";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, lrn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getString("first_name"),
                    rs.getString("middle_name"),
                    rs.getString("last_name"),
                    rs.getInt("lrn"),
                    rs.getString("gender"),
                    rs.getString("date_of_birth"),
                    rs.getString("strand"), 
                    rs.getString("email"), 
                    rs.getString("password") == null ? null : rs.getString("password").toCharArray()
                );
            } else {
                throw new NullPointerException("Error: Student does not exist.");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return null;
    }
    
    /**
     * Add student to database, retrieve their data, create a Student object and add it to the current GradePeriod
     */
    public void addStudentToClassRecord(Student student) {
        /*
        try {
            addStudentToClass(s.getStudentId(), getGradePeriod().getClassId());
            
            Row row = new Row(s, new ArrayList<Grade>(), null);

            // Populate the row with empty grade values if there are student entries in the GradePeriod
            if (getGradePeriod().getClassList().size() != 0) {
                for (Grade g: getGradePeriod().getClassList().getFirst().getGrades()) {
                    addEmptyGradeToDB(s.getStudentId(), g.getClassId(), g.getActivityId());
                }
            }
            
            row.setGrades(getGrades(s.getStudentId(), getGradePeriod().getClassId()));
            
            for (Grade g: row.getGrades()) {
                System.out.println(g.getActivityId());
            }

            getGradePeriod().getClassList().add(row);
            getGradePeriod().fireTableDataChanged();
        } catch (SQLException err) {}
        */
    }
    
    /**
     * TODO: ADD DOCUMENTATION
     */
    public void addNewActivity(int index, String name, int totalScore, int activityTypeId, int quarter) {
        int activityId;
        
        try {
            addActivityToDB(getClassRecord().getClassId(), name, totalScore, activityTypeId, quarter);
            activityId = getActivityIdInDB(getClassRecord().getClassId(), name, quarter);
            List<Integer> studentIdList = new ArrayList<>();
            
            for (Row r: getGradePeriod(quarter).getRows()) {
                studentIdList.add(r.getStudent().getStudentId());
            }
            
            Integer gradeIdList[] = addEmptyGradesToDB(studentIdList.toArray(Integer[]::new),     // Convert List<Integer> to Integer[]
                getClassRecord().getClassId(),
                activityId
            );
                
            for (int i = 0; i < getGradePeriod(quarter).getRows().size(); i++) {
                Row r = getGradePeriod(quarter).getRows().get(i);
                Grade grade = new Grade(
                    gradeIdList[i],                             // grade_id
                    r.getStudent().getStudentId(),              // student_id
                    getClassRecord().getClassId(),              // class_id
                    activityId,                                 // activity_id
                    null,                                       // grade
                    totalScore,                                 // total_score
                    activityTypeId,                             // activity_type_id
                    quarter                                     // quarter
                );
                System.out.println(grade.toString());
                // Fill all cells inside column to be empty
                // Grade columns start at 4th column and 'index' is the selected column index in the table
                // So we subtract 2 to achieve zero-based indexing
                r.getGrades().add(index-2, grade);
            }
            
            // Insert column to table
            getGradePeriod(quarter).insertColumn(index, name);
            
        } catch (SQLException e) { e.printStackTrace(); }            
    }
    
    public int getGradeIdInDB(int studentId, int classId, int activityId) throws SQLException {
        String selectQuery = 
            "SELECT grade_id " +
            "FROM grades g " +
            "WHERE g.student_id = ? AND g.class_id = ? AND g.activity_id = ?;";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(selectQuery)) {
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
    
    public void updateGrade(int gradeId, int value) {
        String updateQuery = "UPDATE grades SET grade = ? WHERE grade_id = ?;";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(updateQuery)) {
            pstmt.setInt(1, value);
            pstmt.setInt(2, gradeId);
            pstmt.executeUpdate();
            System.out.println("Grade updated");
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public double calculatePercentageGrade(int studentId, int activityTypeId, int classId, int quarter) {
        String selectQuery = """
            SELECT *
            FROM grades g
            JOIN activities a ON a.activity_id = g.activity_id
            JOIN students s ON s.student_id = g.student_id
            JOIN classes c ON c.class_id = g.class_id
            WHERE c.class_id = ? AND s.student_id = ? AND a.activity_type_id = ? AND a.quarter = ?;
        """;
        
        int rawScore = 0;
        int totalScore = 0;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, classId);
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, activityTypeId);
            pstmt.setInt(4, quarter);
            ResultSet rs = pstmt.executeQuery();
            
            int value;
            
            while (rs.next()) {
                value = rs.getInt("grade");
                if (! rs.wasNull()) {
                    rawScore += value;
                }
                
                totalScore += rs.getInt("total_score");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        if (totalScore <= 0) {
            return 0.0;
        } else {
            double percentage = (double) rawScore / totalScore * 100.0;
            percentage = Math.round(percentage * 100.0) / 100.0; // Round to 2 decimal places
            System.out.println("rawScore=" + rawScore + ", totalScore=" + totalScore + ", percentage=" + percentage);
            return percentage;
        }
    }
    
    public double[] getGradeWeights(int classId) {
        String selectQuery = """
            SELECT *
            FROM grade_weights gw
            WHERE gw.class_id = ?;
        """;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, classId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new double[]{rs.getDouble("ww_weight"), rs.getDouble("pt_weight"), rs.getDouble("qa_weight")};
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return null;
    }
    
    public double[] getGradeWeights(int studentId, int classId) {
        String selectQuery = """
            SELECT *
            FROM calculated_grades cg
            JOIN grade_weights gw ON gw.class_id = cg.class_id
            WHERE cg.class_id = ? AND cg.student_id = ?;
        """;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, classId);
            pstmt.setInt(2, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new double[]{rs.getDouble("ww_weight"), rs.getDouble("pt_weight"), rs.getDouble("qa_weight")};
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return null;
    }
    
    public double[] calculateWeightedGrade(int studentId, int classId, int quarter) {
        String selectQuery = """
            SELECT *
            FROM calculated_grades cg
            WHERE cg.class_id = ? AND cg.student_id = ?;
        """;
        
        double[] weights = getGradeWeights(classId);
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, classId);
            pstmt.setInt(2, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                if (quarter == 1) {
                    return new double[]{
                        Math.round(rs.getDouble("q1_ww_ps") * weights[0] * 100) / 100.0, 
                        Math.round(rs.getDouble("q1_pt_ps") * weights[1] * 100) / 100.0, 
                        Math.round(rs.getDouble("q1_qa_ps") * weights[2] * 100) / 100.0
                    };
                } else {
                    return new double[]{
                        Math.round(rs.getDouble("q2_ww_ps") * weights[0] * 100) / 100.0,
                        Math.round(rs.getDouble("q2_pt_ps") * weights[1] * 100) / 100.0,
                        Math.round(rs.getDouble("q2_qa_ps") * weights[2] * 100) / 100.0
                    };
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return null;
    }
    
    public void updatePercentageGrade(double value, int studentId, int activityTypeId, int classId, int quarter) {
        String updateQuery = "UPDATE calculated_grades SET ";
        
        switch (quarter) {
            case 1:
                updateQuery = updateQuery.concat("q1_");
                break;
            case 2:
                updateQuery = updateQuery.concat("q2_");
        }
        
        switch (activityTypeId) {
            case 1:
                updateQuery = updateQuery.concat("ww_ps = ? ");
                break;
            case 2:
                updateQuery = updateQuery.concat("pt_ps = ? ");
                break;
            case 3:
                updateQuery = updateQuery.concat("qa_ps = ? ");
        }
        
        updateQuery = updateQuery.concat("WHERE student_id = ? AND class_id = ?;");
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(updateQuery)) {
            pstmt.setDouble(1, value);
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, classId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public void updateInitialGrade(int studentId, int classId, int quarter) {
        String updateQuery = "UPDATE calculated_grades SET ";
        
        if (quarter == 1) {
            updateQuery = updateQuery.concat("q1_raw_grade = ?");
        } else {
            updateQuery = updateQuery.concat("q2_raw_grade = ?");
        }
        
        updateQuery = updateQuery.concat(" WHERE student_id = ? AND class_id = ?;");
        
        double[] grades = calculateWeightedGrade(studentId, classId, quarter);
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(updateQuery)) {
            pstmt.setDouble(1, grades[0] + grades[1] + grades[2]);
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, classId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    public void updateGradeWeights(int classId, double wwWeight, double ptWeight, double qaWeight) {
        String updateQuery = "UPDATE grade_weights SET ww_weight = ?, pt_weight = ?, qa_weight = ? WHERE class_id = ?;";
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(updateQuery)) {
            pstmt.setDouble(1, wwWeight);
            pstmt.setDouble(2, ptWeight);
            pstmt.setDouble(3, qaWeight);
            pstmt.setDouble(4, classId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    /**
     * TODO: ADD DOCUMENTATION
     */
    public void addWWToTable(String name, int totalScore, int quarter) {
        List<String> columns;
        int index;
        
        if (quarter == 1) {
            columns = getClassRecord().getGradePeriod(1).getColumns().stream()
                .filter(e -> e.contains("WW")).collect(Collectors.toList());
        } else {
            columns = getClassRecord().getGradePeriod(2).getColumns().stream()
                .filter(e -> e.contains("WW")).collect(Collectors.toList());
        }
        
        if (columns.isEmpty()) {
            columns = getGradePeriod(getSelectedTab()+1).getColumns().stream()
                .filter(e -> e.contains("Sex")).collect(Collectors.toList());
        }
        
        // Get index
        index = getGradePeriod(quarter).findColumn(columns.getLast());
        
        addNewActivity(index, name, totalScore, 1, quarter);
    }
    
    /**
     * TODO: ADD DOCUMENTATION
     */
    public void addPTToTable(String name, int totalScore, int quarter) {
        List<String> columns;
        int index;
        
        if (quarter == 1) {
            columns = getClassRecord().getGradePeriod(1).getColumns().stream()
                .filter(e -> e.contains("PT")).collect(Collectors.toList());
        } else {
            columns = getClassRecord().getGradePeriod(2).getColumns().stream()
                .filter(e -> e.contains("PT")).collect(Collectors.toList());
        }
        
        if (columns.isEmpty()) {
            columns = getGradePeriod(getSelectedTab()+1).getColumns().stream()
                .filter(e -> e.contains("WW")).collect(Collectors.toList());
        }
        
        // Get index
        index = getGradePeriod(quarter).findColumn(columns.getLast());
        
        addNewActivity(index, name, totalScore, 2, quarter);
    }
    
    // Connect to a database
    private Connection connectToDB() throws SQLException {
        try {
            serverConn = DriverManager.getConnection(url, user, password);
            if (!dbExists("encoder_data")) {
                System.out.println("Error: Schema 'encoder_data' does not exist. Creating...");
                new koq.encoder.components.InitializeDatabase(serverConn, url, user, password);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return DriverManager.getConnection(url + "encoder_data", user, password);
    }
    
    /**
     * Retrieve all student details from database and convert into Student objects.
     */
    private List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        
        try {
            Statement s = getConnection().createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM students;");
            
            while (rs.next()) {
                studentList.add(new Student(
                    rs.getInt("student_id"), 
                    rs.getString("first_name"), 
                    rs.getString("middle_name"),
                    rs.getString("last_name"),
                    rs.getInt("lrn"),
                    rs.getString("gender"),
                    rs.getString("date_of_birth"),
                    rs.getString("strand"), 
                    rs.getString("email"), 
                    rs.getString("password") == null ? null : rs.getString("password").toCharArray()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return studentList;
    }

    /**
     * Retrieves all activity names in a given class record from the database.
     * This function is called to put labels in a JTable header
     */
    public List<Activity> getActivitiesInDB(int classId, int quarter) {
        List<Activity> activities = new ArrayList<>();
        
        try {
            PreparedStatement ps = getConnection().prepareStatement("""
                SELECT DISTINCT a.name, a.activity_type_id, a.activity_id, a.class_id, a.total_score, a.activity_type_id, a.quarter
                FROM activities a
                LEFT JOIN grades g ON g.activity_id = a.activity_id
                JOIN classes c ON a.class_id = c.class_id
                WHERE c.class_id = ? AND a.quarter = ?
                ORDER BY a.activity_type_id, a.activity_id;
            """);
            ps.setInt(1, classId);
            ps.setInt(2, quarter);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                activities.add(new Activity(
                    rs.getInt("activity_id"),
                    rs.getInt("class_id"),
                    rs.getString("name"),
                    rs.getInt("total_score"),
                    rs.getInt("activity_type_id"),
                    rs.getInt("quarter"))
                );
            }
            
        } catch (SQLException e) { e.printStackTrace(); }
        
        return activities;
    }
    
    /**
     * Initializes a JTable that uses the GradePeriod class model with the 
     * appropriate listeners
     */
    public void setTableListeners(JTable table) {
        table.getTableHeader().addMouseListener(new HeaderSelector(table));
        table.addMouseListener(new RowSelector(table));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public void initGradeSheetTable(JTable table, List<Row> rows, int classId, int quarter) {
        Object[][] data = {};
        String[] columnNames = {
            "#", "Student Name", "Sex", "WW|Percentage", 
            "PT|Percentage", "QA|Percentage", "WW|Weighted", 
            "PT|Weighted", "QA|Weighted", "Initial|Grade", "Transmuted|Grade"
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table.setModel(model);
        
        for (int i = 0; i < rows.size(); i++) {
            model.addRow(getGradeSheetRowInDB(
                i+1,
                rows.get(i).getStudent().getStudentId(), 
                classId,
                quarter
                )
            );
        }
    }
    
    public Object[] getGradeSheetRowInDB(int rowCount, int studentId, int classId, int quarter) {
        String selectQuery = """
            SELECT *
            FROM calculated_grades cg
            JOIN students s ON s.student_id = cg.student_id
            WHERE class_id = ? AND s.student_id = ?;
        """;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, classId);
            pstmt.setInt(2, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            double[] weightedGrade = calculateWeightedGrade(studentId, classId, quarter);
            
            if (rs.next()) {
                if (quarter == 1) {
                    return new Object[]{
                        rowCount,
                        rs.getString("last_name") + ", " + rs.getString("first_name") + " " + rs.getString("middle_name"),
                        rs.getString("gender").charAt(0),
                        rs.getDouble("q1_ww_ps") + "%",
                        rs.getDouble("q1_pt_ps") + "%",
                        rs.getDouble("q1_qa_ps") + "%",
                        weightedGrade[0],
                        weightedGrade[1],
                        weightedGrade[2],
                        rs.getDouble("q1_raw_grade"),
                        transmute(rs.getDouble("q1_raw_grade"))
                    };
                } else {
                    return new Object[]{
                        rowCount,
                        rs.getString("last_name") + ", " + rs.getString("first_name") + " " + rs.getString("middle_name"),
                        rs.getString("gender").charAt(0),
                        rs.getDouble("q2_ww_ps") + "%",
                        rs.getDouble("q2_pt_ps") + "%",
                        rs.getDouble("q2_qa_ps") + "%",
                        weightedGrade[0],
                        weightedGrade[1],
                        weightedGrade[2],
                        rs.getDouble("q2_raw_grade"),
                        transmute(rs.getDouble("q2_raw_grade"))
                    };
                }
            } else { throw new NullPointerException("Error: grade sheet row with student_id=" + studentId + ", class_id=" + classId + " does not exist"); }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return null;
    }
    
    public Object[] getFinalGradeRowInDB(int rowCount, int studentId, int classId) {
        String selectQuery = """
            SELECT *
            FROM calculated_grades cg
            JOIN students s ON s.student_id = cg.student_id
            WHERE class_id = ? AND s.student_id = ?;
        """;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, classId);
            pstmt.setInt(2, studentId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int gradeQ1 = transmute(rs.getDouble("q1_raw_grade"));
                int gradeQ2 = transmute(rs.getDouble("q2_raw_grade"));
                int finalGrade = (int) Math.round( (gradeQ1 + gradeQ2)/2.0 );
                String remarks = (finalGrade > 74) ? "Passed" : "Failed";
                       
                return new Object[]{
                    rowCount,
                    rs.getString("last_name") + ", " + rs.getString("first_name") + " " + rs.getString("middle_name"),
                    rs.getString("gender").charAt(0),
                    gradeQ1,
                    gradeQ2,
                    finalGrade,
                    remarks
                };
            } else { throw new NullPointerException("Error: grade sheet row with student_id=" + studentId + ", class_id=" + classId + " does not exist"); }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return null;
    }
    
    public void initFinalGradeTable(JTable table, List<Row> rows, int classId) {
        Object[][] data = {};
        String[] columnNames = {
            "#", "Student Name", "Sex", "First Quarter", 
            "Second Quarter", "Final Grade", "Remarks"
        };
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        table.setModel(model);
        
        for (int i = 0; i < rows.size(); i++) {
            model.addRow(getFinalGradeRowInDB(
                i+1,
                rows.get(i).getStudent().getStudentId(), 
                classId
            ));
        }
    }
    
    public double getPercentageScore(Row row) {
        int raw_score = 0;
        int total_score = 0;
        
        for (Grade g: row.getGrades()) {
            // If grade is null (not encoded yet), assume score is 0
            if (g.getGrade() != null) {
                raw_score += g.getGrade();
            }
            total_score += g.getTotalScore();
        }
                
        return (raw_score / total_score) * 100;
    }
    
    // Weight must be between 0.0 and 1.0
    public double getWeightedScore(double percentageScore, double weight) {
        return percentageScore * weight;
    }
    
    /**
     * Retrieve all student records belonging to a class using class_id.
     * Returns a list of rows with all the student objects and empty grades list.
     * Must call getClassPeriodInDB() to populate the grades.
     * Must call getComputedGradesInDB() to populate the computed grades
     */
    private List<Student> fetchStudentsInClassRecord(int classId) throws SQLException {
        List<Student> students = new ArrayList<>();
        
        try {
            PreparedStatement ps = getConnection().prepareStatement("""
                SELECT *
                FROM students s
                JOIN student_classes sc ON s.student_id = sc.student_id
                JOIN classes c ON sc.class_id = c.class_id
                WHERE c.class_id = ?
                ORDER BY s.gender DESC, s.last_name, s.first_name;
            """);
            ps.setInt(1, classId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                students.add(new Student(
                    rs.getInt("student_id"), 
                    rs.getString("first_name"), 
                    rs.getString("middle_name"),
                    rs.getString("last_name"),
                    rs.getInt("lrn"),
                    rs.getString("gender"),
                    rs.getString("date_of_birth"),
                    rs.getString("strand"),
                    rs.getString("email"),
                    rs.getString("password") == null ? null : rs.getString("password").toCharArray()
                ));
            }
            
        } catch(SQLException e) {}
        
        return students;
    }
    
    /**
     * Add a class record into the database. Must call getClassId() to get the corresponding class_id
     * OUTDATED
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
     * Retrieves class record details from the database based on id
     * NOTE: Grading period and grading sheet is not included. Both must be populated with
     * getGradingPeriodInDB and getGradingSheetInDB, respectively
     */
    public ClassRecord getClassRecordInDB(int classId) {
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
                    rs.getInt("faculty_id"), 
                    rs.getInt("subject_id"), 
                    rs.getInt("grade_level"),
                    rs.getString("section"), 
                    rs.getInt("semester"), 
                    rs.getString("academic_year")
                );
                System.out.println("Retrieved class record details. Class record object created");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        if (record != null) {
            return record;
        } else {
            throw new NullPointerException("Class record with class_id = " + classId + " not found. Object is null");
        }
    }
    
    /**
     * Retrieve grades from database given the student list
     */
    private List<Row> getGradePeriodInDB(List<Student> students, int classId, int quarter) {
        List<Row> rows = new ArrayList<>();
        
        try {
            for (Student s: students) {
                PreparedStatement ps = getConnection().prepareStatement("""
                    SELECT *
                    FROM grades g
                    JOIN student_classes sc ON g.student_id = sc.student_id AND g.class_id = sc.class_id
                    JOIN students s ON g.student_id = s.student_id
                    JOIN activities a ON g.activity_id = a.activity_id
                    JOIN classes c ON g.class_id = c.class_id
                    WHERE c.class_id = ? AND s.student_id = ? AND a.quarter = ?
                    ORDER BY a.activity_type_id, a.activity_id;
                """);
                ps.setInt(1, classId);
                ps.setInt(2, s.getStudentId());
                ps.setInt(3, quarter);

                ResultSet rs = ps.executeQuery();
                
                Row row = new Row(s, new ArrayList<Grade>());
                
                while (rs.next()) {

                    Integer grade;

                    if (rs.getString("grade") == null) {
                        grade = null;
                    } else {
                        grade = Integer.parseInt(rs.getString("grade"));
                    }

                    Grade g = new Grade(
                            rs.getInt("grade_id"), 
                            rs.getInt("student_id"), 
                            rs.getInt("class_id"), 
                            rs.getInt("activity_id"), 
                            grade,
                            Integer.parseInt(rs.getString("total_score")),
                            rs.getInt("activity_type_id"),
                            rs.getInt("quarter")
                    );

                    row.getGrades().add(g);
                }
                rows.add(row);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return rows;
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
     * OUTDATED DOCUMENTATION
     * Adds a new student to the database if they do not exist yet. getStudentInDB() must be called
     * after this query to get the assigned student_id
     */
    public void addStudentToDb(String firstName, String lastName) throws SQLException {
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
     * OUTDATED
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
        String deleteQuery = "DELETE FROM student_classes WHERE student_id = ? AND class_id = ?";
        try (PreparedStatement pstmt = getConnection().prepareStatement(deleteQuery)) {
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
        }
    }
    
    /**
     * Retrieves activity_id from database based on class_id, name, and quarter
     */
    public int getActivityIdInDB(int classId, String name, int quarter) throws SQLException {
        String selectQuery = """
            SELECT activity_id
            FROM activities a
            JOIN classes c ON c.class_id = a.class_id
            WHERE c.class_id = ? AND a.name = ? AND a.quarter = ?;
        """;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(selectQuery)) {
            pstmt.setInt(1, classId);
            pstmt.setString(2, name);
            pstmt.setInt(3, quarter);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                System.out.println("activity_id: " + rs.getInt("activity_id"));
                return rs.getInt("activity_id");
            } else {
                throw new SQLException(
                    "activity_id with class_id = " + classId + 
                    ", name = " + name + ", quarter=" + quarter + " not found"
                );
            }
        }
    }
    
    /**
     * Deletes all grade records in the database based on activity id. This method 
     * is called when an activity is deleted in a class record
     */
    public void deleteGradeByActivity(int activityId) throws SQLException {
        String deleteQuery = "DELETE FROM grades WHERE activity_id = ?;";
        try (PreparedStatement pstmt = getConnection().prepareStatement(deleteQuery)) {
            pstmt.setInt(1, activityId);
            pstmt.executeUpdate();
        }
    }
    
     /**
     * Deletes all grade records in the database based on student id and class id. This method 
     * is called when a student is deleted in a class record
     */
    public void deleteGradeByStudent(int studentId, int classId) throws SQLException {
        String deleteQuery = "DELETE FROM grades WHERE student_id = ? AND class_id = ?;";
        try (PreparedStatement pstmt = getConnection().prepareStatement(deleteQuery)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(1, classId);
            pstmt.executeUpdate();
        }
    }
    
    /**
     * Add a new activity record to the database. This method is called when a new activity is added
     * to the class record by the user
     */
    public void addActivityToDB(int classId, String name, int totalScore, int activityTypeId, int quarter) {
        String insertQuery = "INSERT INTO activities (class_id, name, total_score, activity_type_id, quarter) " +
                             "VALUES (?, ?, ?, ?, ?);";

        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery)) {
                pstmt.setInt(1, classId);
                pstmt.setString(2, name);
                pstmt.setInt(3, totalScore);
                pstmt.setInt(4, activityTypeId);
                pstmt.setInt(5, quarter);
                pstmt.executeUpdate();
                System.out.println("New activity added to DB");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    /**
     * Retrieve a GradePeriod's class_id from the database
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
    
    // Add a new grade record to the database with grade value set to null
    public Integer[] addEmptyGradesToDB(Integer[] studentId, int classId, int activity_id) {
        String insertQuery = "INSERT INTO grades (student_id, class_id, activity_id) VALUES (?, ?, ?);";
        List<Integer> results = new ArrayList<>();
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            for (int id: studentId) {
                pstmt.setInt(1, id);
                pstmt.setInt(2, classId);
                pstmt.setInt(3, activity_id);
                pstmt.addBatch();
            }
            int affectedRows[] = pstmt.executeBatch();
            
            // Calculate total affected rows
            int totalAffectedRows = 0;
            for (int count : affectedRows) {
                totalAffectedRows += count;
            }
            
            if (totalAffectedRows > 0) {
                // Retrieve the generated key
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    while (generatedKeys.next()) {
                        results.add(generatedKeys.getInt(1));
                        System.out.println(generatedKeys.getInt(1));
                    }
                }
            } else {
                System.out.println("Insert failed, no rows affected.");
            }
            
            return results.toArray(Integer[]::new);
        } catch (SQLException e) { e.printStackTrace(); }
        
        return null;
    }
    
    // Retrieve all grades for a specific student in a class from the database
    public List<Grade> getGrades(int studentId, int classId) {
        String query = 
                "SELECT * " +
                "FROM grades g " + 
                "JOIN activities a ON g.activity_id = a.activity_id " +
                "WHERE g.student_id = ? AND g.class_id = ?;";
        
        List<Grade> gradeList = new ArrayList();
        Integer grade;
        
        try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, classId);
            ResultSet rs = pstmt.executeQuery();
         
            System.out.println("studentId: " + studentId + "\t" + "classId: " + classId);
            
            while (rs.next()) {
                if (rs.getString("grade") == null) {
                     grade = null;
                } else {
                    grade = Integer.parseInt(rs.getString("grade"));
                }
                
                gradeList.add(new Grade(
                    rs.getInt("grade_id"), 
                    rs.getInt("student_id"),
                    rs.getInt("class_id"),
                    rs.getInt("activity_id"), 
                    grade,
                    Integer.parseInt(rs.getString("total_score")),
                    rs.getInt("activity_type_id"),
                    rs.getInt("quarter")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        System.out.println("GradeList size: " + gradeList.size());
        return gradeList;
    }
    

    /**
     * Method for transmuting grades
     */
    public int transmute(double score) {
        if (score <= 0) {
            return 0;
        } else if (score < 60) {
            return (int) Math.floor(60 + score/4);
        } else {
            return (int) Math.floor(75 + ((score-60)/1.6));
        }
    }
}
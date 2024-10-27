package koq.encoder.mvc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class Model {

    private String txt;
    private String[] columnNames;
    private int selectedRow;
    private int selectedColumn;
    private DatabaseConnection db;
    private List<Student> studentList;
    private List<Activity> activityList;
    private ClassRecord record;
    
    public Model() {
        selectedRow = 0;
        selectedColumn = 0;
        
        try {
            db = new DatabaseConnection("encoder_data");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        studentList = DatabaseConnection.getAllStudents();
        
        record = new ClassRecord("A", "Mathematics 10", "1st");

    }
    
    public ClassRecord getClassRecord() {
        return record;
    }
    
    public List<Student> getStudentList() {
        return studentList;
    }

    /**
     * TODO: Rework this part to conform to the new AbstractTableModel ClassRecord class
     * 
     */
    public String getTableValueAtCurrentSelection() {
        return (String) getClassRecord().getValueAt(getSelectedRow(), getSelectedColumn());
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
    
    /*
    public void loadTable() {        
        setTableModel(new Object [][] {
            {"Richard Sarmiento", "8", "13", "8", "8", "37"},
            {"Renz Flores", "6", "10", "6", "7", "42"},
            {"Lorenz Chiong", "7", "12", "9", "8", "40"}
        },
        new String [] {
            "Student Name", "Activity 1", "Activity 2", "Assignment 1", "Quiz 1", "Exam 1"
        });
    }
    */
}

/**
 * Class representing ClassRecord row data
 * Contains Student object and a list of Activity objects
 */
class Row {
    private Student student;
    private List<Grade> gradesList;
    private List<String> values;

    public Row(Student student, List<Grade> gradesList) {
        this.student = student;
        this.gradesList = gradesList;
        
        values = new ArrayList<>();
        values.add(student.getStudentName());
        for (Grade g: gradesList) {
            values.add(String.valueOf(g.getGrade()));
        }
    }
    
    public List<String> getValues() {
        return values;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public List<Grade> getGrades() {
        return gradesList;
    }
    
    public void setGradesAt(int index, int value) {
        gradesList.get(index).setGrade(value);
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
    private String className;
    private String subjectName;
    private String term;

    public ClassRecord(String className, String subjectName, String term) {
        this.className = className;
        this.subjectName = subjectName;
        this.term = term;
        
        classList = DatabaseConnection.getClassRecord(className, subjectName, term);
        
        columnNames = DatabaseConnection.getActivitiesInClassRecord(className, subjectName, term);
        columnNames.add(0, "Student name");

    }
    
    public ClassRecord() {
        this.className = className;
        this.subjectName = subjectName;
        this.term = term;
        classList = new ArrayList<>();
        columnNames = new ArrayList<>();
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

    @Override
    public Object getValueAt(int row, int col) {
        Row r = classList.get(row);
        
        switch (col) {
            case 0: return r.getStudent().getStudentName();
            case 1: return r.getGrades().get(col);
            default: return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col > 0; // Make only certain columnNames editable
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Row obj = classList.get(row); // Retrieve the object at this row
        
        obj.setGradesAt(col, (Integer) value);
        
        fireTableCellUpdated(row, col); // Notify JTable of data change
    }
}
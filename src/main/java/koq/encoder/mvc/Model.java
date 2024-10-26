package koq.encoder.mvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author KOQ
 */
public class Model {

    private String txt;
    private Object tableModel[][];
    private String[] columns;
    private int selectedRow;
    private int selectedColumn;
    
    public Model() {
        selectedRow = 0;
        selectedColumn = 0;
        
        try (Connection connection = DatabaseConnection.getConnection()) {
            String insertStudentSQL = "INSERT INTO students (first_name, last_name) VALUES (?, ?)";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertStudentSQL)) {
                preparedStatement.setString(1, "John");
                preparedStatement.setString(2, "Doe");
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getTableValueAtCurrentSelection() {
        return (String) getTableModel()[getSelectedRow()][getSelectedColumn()];
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
            
    public void setTableModel(Object[][] data, String[] columns) {
        tableModel = data;
        this.columns = columns;
    }
    
    private String[] getTableHeaders() {
        return columns;
    }
    
    private Object[][] getTableModel() {
        return tableModel;
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
    
    public void openFile() {
        /**
         * Dummy code
         */
        setTableModel(new Object [][] {
            {"Richard Sarmiento", "8", "13", "8", "8", "37"},
            {"Renz Flores", "6", "10", "6", "7", "42"},
            {"Lorenz Chiong", "7", "12", "9", "8", "40"}
        },
        new String [] {
            "Student Name", "Activity 1", "Activity 2", "Assignment 1", "Quiz 1", "Exam 1"
        });
    }
}

class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/test"; // Change as needed
        String user = "root";
        String password = "root";
        
        return DriverManager.getConnection(url, user, password);
    }
}
package koq.encoder.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Custom table for representing a class record in the GUI
 */
public class GradePeriod extends AbstractTableModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Table model contents
    private transient List<Row> classList;
    private transient List<String> columnNames;
    
    public GradePeriod() {
        this.classList = null;          // Empty rows
        this.columnNames = null;     // Empty columns
    }
   
    // Insert new column
    public void insertColumn(int index, String value) {
        getColumns().add(index+1, value);
        fireTableStructureChanged();
    }
    
     public void deleteColumn(int index) {
        getColumns().remove(index);
        fireTableStructureChanged();
    }
    
    // Setters
    public void setClassList(List<Row> list) {
        classList = list;
    }
    public void setColumnNames(List<String> activityNames) {
        List<String> cols = new ArrayList<>();
        
        cols.add("#");
        cols.add("Student Name");
        cols.addAll(activityNames);
        cols.add("Computed Raw Grades");
        
        columnNames = cols;
    }
    public void initClassList() {
        classList = new ArrayList<>();
        columnNames = new ArrayList<>();
    }

    public Row getRowAt(int index) {
        if (index < classList.size()) {
            return classList.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + classList.size());
        }
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
        
        // Set cell to row number on first column (#)
        if (colModulo == 0) {
            return classList.indexOf(r)+1;
        } 
        // Set cell to student name (Surname, First name) on second column (Student Name)
        else if (colModulo == 1) {
            return r.getStudent().getStudentNameFormatted();
        } 
        // Set cell to computed raw grade on last column (Computed Raw Grade)
        else if (colModulo == columnNames.size()-1) {
            if (r.getComputedGrades()  == null) {
                return null;
            }
            return r.getComputedGrades();
        }
        // Otherwise, set cell to grade value (either null or Double)
        else {
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
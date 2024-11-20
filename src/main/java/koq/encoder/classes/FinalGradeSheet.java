package koq.encoder.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Custom table for representing a class record in the GUI
 */
public class FinalGradeSheet extends AbstractTableModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Table model contents
    private transient List<Row> classList;
    private transient List<String> columnNames;
    
    public FinalGradeSheet() {
        this.classList = null;          // Empty rows
        this.columnNames = null;        // Empty columns
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
        return null;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return col > 0; // Make only certain columnNames editable
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        ;
        
        fireTableCellUpdated(row, col);
    }
}
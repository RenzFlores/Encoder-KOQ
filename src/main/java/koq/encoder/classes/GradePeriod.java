package koq.encoder.classes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Custom table for representing a class record in the GUI
 */
public class GradePeriod extends AbstractTableModel {
    // Table model contents
    private List<Row> rows;
    private List<String> columnNames;
    
    public GradePeriod() {
        rows = new ArrayList<>();           // Empty rows
        columnNames = new ArrayList<>();    // Empty columns
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
    public void setRows(List<Row> list) {
        rows = list;
    }
    public void setColumnNames(List<String> activityNames) {
        List<String> cols = new ArrayList<>();
        
        cols.add("#");
        cols.add("Student Name");
        cols.add("Sex");
        cols.addAll(activityNames);
        
        columnNames = cols;
    }

    public Row getRowAt(int index) {
        if (index < rows.size()) {
            return rows.get(index);
        } else {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for length " + rows.size());
        }
    }
    
    public List<Row> getRows() {
        return rows;
    }
    
    @Override
    public int getRowCount() {
        return rows.size();
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
        Row r = rows.get(row);
        
        int colModulo = col % (columnNames.size());
        
        // Set cell to row number on first column (#)
        if (colModulo == 0) {
            return rows.indexOf(r)+1;
        } 
        // Set cell to student name (Surname, First name M.I.) on second column (Student Name)
        else if (colModulo == 1) {
            return r.getStudent().getStudentFullName();
        } 
        // Set cell to student sex on third column (Sex)
        else if (colModulo == 2) {
            return r.getStudent().getGenderInitial();
        }
        // Otherwise, set cell to grade value (either null or Double)
        else {
            if (r.getGrades().get(colModulo-3) == null) {
                return null;
            }
            return r.getGrades().get(colModulo-3).getGrade();
        }
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        Row obj = rows.get(row);
        
        int colModulo = col % (columnNames.size());
        
        // Only set values if table is not empty
        if (colModulo > 1) {
            if (value == null) {
                obj.setGradesAt(colModulo-1, null);
            } else {
                obj.setGradesAt(colModulo-1, Integer.parseInt( (String) value ));
            }
        }
        
        fireTableCellUpdated(row, col);
    }
    
    public void moveRow(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex < 0 || fromIndex >= rows.size() || toIndex >= rows.size()) {
            throw new IndexOutOfBoundsException("Invalid row index");
        }

        // Swap the rows
        Row row = rows.remove(fromIndex);
        rows.add(toIndex, row);

        // Notify the table about the data change
        fireTableRowsDeleted(fromIndex, fromIndex);
        fireTableRowsInserted(toIndex, toIndex);
    }
}
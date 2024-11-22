package koq.encoder.components;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class TableView extends JTabbedPane {
    
    JTable tableQ1;
    JTable tableQ2;
    JTable tableQ1GradeSheet;
    JTable tableQ2GradeSheet;
    JTable tableFinalGrade;
    
    public JTable getTable(int table) {
        switch(table) {
            case 0:
                return tableQ1;
            case 1:
                return tableQ2;
            case 2:
                return tableQ1GradeSheet;
            case 3:
                return tableQ2GradeSheet;
            default:
                return tableFinalGrade;
        }
    }
    
    /**
     * Wrapping of window components in order:
     * TableView (JTabbedPane) > Viewport (JScrollPane) > Table (JTable)
     */
    public TableView() {
        setPreferredSize(new java.awt.Dimension(860, 650));
        
        tableQ1 = createTable();
        tableQ2 = createTable();
        tableQ1GradeSheet = createTable();
        tableQ2GradeSheet = createTable();
        tableFinalGrade = createTable();
        
        addTab("Grade Period 1", new JScrollPane(tableQ1));
        addTab("Grade Period 2", new JScrollPane(tableQ2));
        addTab("Grade Sheet 1", new JScrollPane(tableQ1GradeSheet));
        addTab("Grade Sheet 2", new JScrollPane(tableQ2GradeSheet));
        addTab("Semester Final Grade", new JScrollPane(tableFinalGrade));
    }
    
    private JTable createTable() {
        JTable table = new JTable();
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }
}
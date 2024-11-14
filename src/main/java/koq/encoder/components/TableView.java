package koq.encoder.components;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class TableView extends JTabbedPane {
    
    JTable tableQ1;
    JTable tableQ2;
    JTable tableQ3;
    JTable tableQ4;
    
    public JTable getTable(int term) {
        switch(term) {
            case 1:
                return tableQ1;
            case 2:
                return tableQ2;
            case 3:
                return tableQ3;
            default:
                return tableQ4;
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
        tableQ3 = createTable();
        tableQ4 = createTable();
        
        addTab("1st Quarter", new JScrollPane(tableQ1));
        addTab("2nd Quarter", new JScrollPane(tableQ2));
        addTab("3rd Quarter", new JScrollPane(tableQ3));
        addTab("4th Quarter", new JScrollPane(tableQ4));
    }
    
    private JTable createTable() {
        JTable table = new JTable();
        table.getTableHeader().setReorderingAllowed(false);
        return table;
    }
}
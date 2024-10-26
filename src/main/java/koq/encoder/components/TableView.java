package koq.encoder.components;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author KOQ
 */
public class TableView extends JTabbedPane {
    
    JPanel tab;
    JTable table;
    JScrollPane viewport;
    DefaultTableModel tableModel;
    
    public JTable getTable() {
        return table;
    }
    
    public JPanel getTab() {
        return tab;
    }
    
    /**
     * Wrapping of window components in order:
     * TablePanel (JTabbedPane) > Tab (JPanel) > Viewport (JScrollPane) > Table (JTable)
     */
    public TableView() {
        setPreferredSize(new java.awt.Dimension(670, 610));

        tab = new JPanel();
        tab.setLayout(new CardLayout());

        Object[][] data = new Object [][] {
                {"Richard Sarmiento", "8", "13", "8", "8", "37"},
                {"Renz Flores", "6", "10", "6", "7", "42"},
                {"Lorenz Chiong", "7", "12", "9", "8", "40"}
        };
        
        String[] cols = new String [] {
            "Student Name", "Activity 1", "Activity 2", "Assignment 1", "Quiz 1", "Exam 1"
        };
        
        tableModel = new DefaultTableModel(data, cols);
        
        table = new JTable(tableModel) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        };
        
        table.setName("table");
        table.setPreferredSize(new java.awt.Dimension(1000, 1000));
        table.getTableHeader().setReorderingAllowed(false);
        table.setColumnSelectionAllowed(true);
        
        viewport = new JScrollPane();
        viewport.setBackground(new Color(150, 0, 0));
        viewport.setViewportView(table);
        
        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(0).setResizable(false);
            table.getColumnModel().getColumn(1).setResizable(false);
            table.getColumnModel().getColumn(2).setResizable(false);
            table.getColumnModel().getColumn(3).setResizable(false);
            table.getColumnModel().getColumn(4).setResizable(false);
            table.getColumnModel().getColumn(5).setResizable(false);
        }

        // Add scrollpane to tabel
        tab.add(viewport);
        addTab("Class 1A", tab);
    }
}

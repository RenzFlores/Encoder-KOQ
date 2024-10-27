package koq.encoder.components;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author KOQ
 */
public class TableView extends JTabbedPane {
    
    JPanel tab;
    JTable table;
    JScrollPane viewport;
    AbstractTableModel tableModel;
    
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
        
        table = new JTable();
        
        table.setName("table");
        table.setPreferredSize(new java.awt.Dimension(1000, 1000));
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        table.setColumnSelectionAllowed(true);
        
        viewport = new JScrollPane();
        viewport.setBackground(new Color(150, 0, 0));
        viewport.setViewportView(table);

        // Add scrollpane to tabel
        tab.add(viewport);
        addTab("Class 1A", tab);
    }
}

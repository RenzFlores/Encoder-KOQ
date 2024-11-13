package koq.encoder.components;

import java.awt.BorderLayout;
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
public class TableView extends JPanel {
    
    JTable table;
    JScrollPane viewport;
    
    public JTable getTable() {
        return table;
    }
    
    /**
     * Wrapping of window components in order:
     * TableView (JPanel) > Viewport (JScrollPane) > Table (JTable)
     */
    public TableView() {
        setPreferredSize(new java.awt.Dimension(860, 650));
        setLayout(new BorderLayout());
        
        table = new JTable();
        
        table.setName("table");
        table.getTableHeader().setReorderingAllowed(false);
        table.setColumnSelectionAllowed(true);
        
        viewport = new JScrollPane(table);
        //viewport.setBackground(new Color(150, 0, 0));         // DEBUG
        table.setFillsViewportHeight(true);                     // Ensures the table fills the vertical space of the viewport

        // Add scrollpane to panel
        add(viewport, BorderLayout.CENTER);
    }
}

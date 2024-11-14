package koq.encoder.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

public class EditorWindow extends JPanel {
    
    private final EditPanel editPanel;
    // private final TableEditorPanel tableEditorPanel;
    private final Toolbar toolbar;
    private final TableView tableView;
    private final FilterPanel filterPanel;
    
    public JPanel getGradeEditor() {
        return editPanel;
    }
    
    public JTable getTable(int term) {
        return tableView.getTable(term);
    }
    
    public JToolBar getToolbar() {
        return toolbar;
    }
    
    public JTabbedPane getTableView() {
        return tableView;
    }
    
    public JPanel getFilter() {
        return filterPanel;
    }
    
    public EditorWindow() {
        setPreferredSize(new Dimension(1300, 720));
        setBackground(new Color(100, 100, 140));
        setLayout(new BorderLayout(5, 5));
        
        editPanel = new EditPanel();
        toolbar = new Toolbar();
        tableView = new TableView();
        filterPanel = new FilterPanel();
        
        add(BorderLayout.NORTH, toolbar);
        add(BorderLayout.CENTER, tableView);
        add(BorderLayout.WEST, editPanel);
        add(BorderLayout.EAST, filterPanel);
    }
}


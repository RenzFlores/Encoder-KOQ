package koq.encoder.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

public class FacultyWindow extends JPanel {
    
    private final EditPanel editPanel;
    // private final TableEditorPanel tableEditorPanel;
    private final Toolbar toolbar;
    private final TableView tableView;
    //private final FilterPanel filterPanel;
    
    public JPanel getGradeEditor() {
        return editPanel;
    }
    
    public JTable getTable(int table) {
        return tableView.getTable(table);
    }
    
    public JToolBar getToolbar() {
        return toolbar;
    }
    
    public JTabbedPane getTableView() {
        return tableView;
    }
    
    /*
    public JPanel getFilter() {
        return filterPanel;
    }
    */
    
    public FacultyWindow() {
        setPreferredSize(new Dimension(1300, 720));
        setBackground(Constants.WINDOW_COLOR_LAYER_1);
        setLayout(new BorderLayout(5, 5));
        
        editPanel = new EditPanel();
        toolbar = new Toolbar();
        tableView = new TableView();
        //filterPanel = new FilterPanel();
        
        add(BorderLayout.NORTH, toolbar);
        add(BorderLayout.CENTER, tableView);
        add(BorderLayout.WEST, editPanel);
        //add(BorderLayout.EAST, filterPanel);
    }
    
    public void enableWindow() {
        editPanel.setEnabled(true);
        toolbar.setEnabled(true);
        tableView.setEnabled(true);
        //filterPanel.setEnabled(true);
    }
    
    public void disableWindow() {
        editPanel.setEnabled(false);
        toolbar.setEnabled(false);
        tableView.setEnabled(false);
        //filterPanel.setEnabled(false);
    }
}


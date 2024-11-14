package koq.encoder.components;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;

public class TableEditorPanel extends JPanel {
    
    JPanel containerPanel;
    JToolBar toolbar;
    TableView tableView;
    
    public JToolBar getToolbar() {
        return toolbar;
    }
    
    public JTable getTable(int term) {
        return ((TableView) tableView).getTable(term);
    }
    
    public JPanel getContainer() {
        return containerPanel;
    }
    
    public TableEditorPanel() {
        setPreferredSize(new java.awt.Dimension(880, 710));
        setLayout(new CardLayout());
        setBorder(new LineBorder(Color.YELLOW, 1));
        
        containerPanel = new JPanel();
        containerPanel.setPreferredSize(new java.awt.Dimension(880, 710));
        containerPanel.setLayout(new FlowLayout());
        containerPanel.setBackground(new Color(0, 0, 100));
        containerPanel.setBorder(new LineBorder(Color.RED, 1));
        
        toolbar = new Toolbar();
        containerPanel.add(toolbar);
        
        tableView = new TableView();
        containerPanel.add(tableView);
        
        add(containerPanel);
    }    
}
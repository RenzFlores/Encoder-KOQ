package koq.encoder.components;

import java.awt.Dimension;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class AddStudentWindow extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;
    private JTextField filterTextField;
    private JButton addButton;
    
    public AddStudentWindow() {
        table = new JTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Disable column resizing
        table.getTableHeader().setResizingAllowed(false);

        // Disable column reordering
        table.getTableHeader().setReorderingAllowed(false);
        
        // Set row height
        table.setRowHeight(30);

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

        addButton = new JButton("Open");

        JPanel panel = new JPanel();
        
        filterTextField = new JTextField();
        filterTextField.setPreferredSize(new Dimension(200, 22));
        
        panel.add(new JLabel("Filter by Name: "));
        panel.add(filterTextField);
        
        add(panel, java.awt.BorderLayout.NORTH);
        add(scrollPane, java.awt.BorderLayout.CENTER);
        add(addButton, java.awt.BorderLayout.PAGE_END);
        
        setTitle("Add student");
        setSize(new java.awt.Dimension(720, 360));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Constants.WINDOW_COLOR_LAYER_0);
    }
    
    public JTable getTable() {
        return table;
    }
    
    public JButton getButton() {
        return addButton;
    }
    
    public void initRowSorter() {
        // TableRowSorter for the JTable
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);

        // Create a text field for filtering by name
        filterTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                // Create a RowFilter based on the text input
                RowFilter<TableModel, Object> rf = null;
                try {
                    // Use case-insensitive regex filter, and trim spaces
                    String filterText = filterTextField.getText().trim();
                    rf = RowFilter.regexFilter("(?i)" + filterText, 0); // Filter by column index 1 (Name)
                } catch (java.util.regex.PatternSyntaxException ex) {
                    return;  // Ignore invalid regex patterns
                }
                sorter.setRowFilter(rf);  // Apply the filter to the sorter
            }
        });
    }
}

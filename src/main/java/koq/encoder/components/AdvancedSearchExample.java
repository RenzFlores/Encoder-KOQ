package koq.encoder.components;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import javax.swing.RowFilter;
import java.awt.*;
import java.util.*;

public class AdvancedSearchExample {
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Create frame
            JFrame frame = new JFrame("Advanced Search Module");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            
            // Sample data
            Object[][] data = {
                {"John", "Doe", 25, 50000.00},
                {"Jane", "Smith", 30, 60000.00},
                {"Alice", "Johnson", 35, 55000.00},
                {"Bob", "Brown", 40, 65000.00}
            };
            
            // Column names
            String[] columnNames = {"First Name", "Last Name", "Age", "Salary"};
            
            // Create table model and JTable
            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            JTable table = new JTable(model);
            
            // Create a TableRowSorter and set it to the table
            TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);
            
            // Panel for search filters
            JPanel filterPanel = new JPanel(new GridLayout(3, 2));
            
            // Text Field for first name filter
            JTextField firstNameField = new JTextField();
            firstNameField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    applyFilter(sorter, firstNameField, null, null, null);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    applyFilter(sorter, firstNameField, null, null, null);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    applyFilter(sorter, firstNameField, null, null, null);
                }
            });
            
            // Text Field for last name filter
            JTextField lastNameField = new JTextField();
            lastNameField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    applyFilter(sorter, null, lastNameField, null, null);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    applyFilter(sorter, null, lastNameField, null, null);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    applyFilter(sorter, null, lastNameField, null, null);
                }
            });
            
            // Slider for Age range filter
            JSlider ageSlider = new JSlider(20, 60);
            ageSlider.addChangeListener(e -> applyFilter(sorter, null, null, ageSlider.getValue(), null));
            
            // Slider for Salary range filter
            JSlider salarySlider = new JSlider(40000, 70000);
            salarySlider.addChangeListener(e -> applyFilter(sorter, null, null, null, (double) salarySlider.getValue()));
            
            // Add components to filter panel
            filterPanel.add(new JLabel("First Name:"));
            filterPanel.add(firstNameField);
            filterPanel.add(new JLabel("Last Name:"));
            filterPanel.add(lastNameField);
            filterPanel.add(new JLabel("Age Range:"));
            filterPanel.add(ageSlider);
            filterPanel.add(new JLabel("Salary Range:"));
            filterPanel.add(salarySlider);
            
            // Add filter panel and table to frame
            frame.add(filterPanel, BorderLayout.NORTH);
            frame.add(new JScrollPane(table), BorderLayout.CENTER);
            
            // Set frame size and visible
            frame.setSize(600, 400);
            frame.setVisible(true);
        });
    }

    /**
     * Apply filter based on multiple criteria (First Name, Last Name, Age, Salary).
     * 
     * @param sorter TableRowSorter for JTable
     * @param firstNameFilter First Name filter text
     * @param lastNameFilter Last Name filter text
     * @param ageFilter Age filter value
     * @param salaryFilter Salary filter value
     */
    private static void applyFilter(TableRowSorter<TableModel> sorter, JTextField firstNameFilter, JTextField lastNameFilter,
                                    Integer ageFilter, Double salaryFilter) {
        // Create a RowFilter based on multiple filters
        RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(".*", 0); // Default no filter for first name column
        
        if (firstNameFilter != null && !firstNameFilter.getText().isEmpty()) {
            rowFilter = RowFilter.andFilter(Collections.singletonList(RowFilter.regexFilter("(?i)" + firstNameFilter.getText(), 0)));
        }

        if (lastNameFilter != null && !lastNameFilter.getText().isEmpty()) {
            rowFilter = RowFilter.andFilter(Collections.singletonList(RowFilter.regexFilter("(?i)" + lastNameFilter.getText(), 1)));
        }

        if (ageFilter != null) {
            rowFilter = RowFilter.andFilter(Collections.singletonList(new RowFilter<TableModel, Object>() {
                @Override
                public boolean include(Entry<? extends TableModel, ? extends Object> entry) {
                    Integer age = (Integer) entry.getValue(2); // Assuming Age is the 3rd column
                    return age >= ageFilter;
                }
            }));
        }

        if (salaryFilter != null) {
            rowFilter = RowFilter.andFilter(Collections.singletonList(new RowFilter<TableModel, Object>() {
                @Override
                public boolean include(Entry<? extends TableModel, ? extends Object> entry) {
                    Double salary = (Double) entry.getValue(3); // Assuming Salary is the 4th column
                    return salary >= salaryFilter;
                }
            }));
        }

        sorter.setRowFilter(rowFilter);
    }
}

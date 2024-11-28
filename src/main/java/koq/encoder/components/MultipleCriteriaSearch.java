package koq.encoder.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class MultipleCriteriaSearch {

    private JTable table;
    private DefaultTableModel model;

    public MultipleCriteriaSearch() {
        // Sample data and model setup
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Grade");

        model.addRow(new Object[] { 1, "John Doe", 85 });
        model.addRow(new Object[] { 2, "Jane Smith", 92 });
        model.addRow(new Object[] { 3, "Alice Johnson", 76 });
        model.addRow(new Object[] { 4, "Bob Brown", 68 });

        table = new JTable(model);
    }

    // Search Criteria Class to store each search condition
    public static class SearchCriteria {
        String column;
        String searchTerm;
        boolean isNumeric; // Whether the search term is a number (for comparisons)

        public SearchCriteria(String column, String searchTerm, boolean isNumeric) {
            this.column = column;
            this.searchTerm = searchTerm;
            this.isNumeric = isNumeric;
        }
    }

    // Method to perform multiple criteria search
    public List<Integer> advancedSearch(List<SearchCriteria> criteriaList) {
        List<Integer> matchingRows = new ArrayList<>();

        // Iterate through the rows of the table model
        for (int rowIndex = 0; rowIndex < model.getRowCount(); rowIndex++) {
            boolean matchesAllCriteria = true;

            // For each search criteria, check if the row matches
            for (SearchCriteria criteria : criteriaList) {
                int columnIndex = model.findColumn(criteria.column); // Find column index by name
                if (columnIndex != -1) {
                    Object cellValue = model.getValueAt(rowIndex, columnIndex);

                    // Check if the value matches the search criteria
                    if (criteria.isNumeric) {
                        try {
                            double cellNumValue = Double.parseDouble(cellValue.toString());
                            double searchNumValue = Double.parseDouble(criteria.searchTerm);
                            if (cellNumValue != searchNumValue) {
                                matchesAllCriteria = false;
                                break;
                            }
                        } catch (NumberFormatException e) {
                            matchesAllCriteria = false;
                            break;
                        }
                    } else {
                        // Case-insensitive search for text
                        if (!cellValue.toString().toLowerCase().contains(criteria.searchTerm.toLowerCase())) {
                            matchesAllCriteria = false;
                            break;
                        }
                    }
                }
            }

            // If row matches all criteria, add its index to the list
            if (matchesAllCriteria) {
                matchingRows.add(rowIndex);
            }
        }

        return matchingRows;
    }

    // Example usage
    public static void main(String[] args) {
        MultipleCriteriaSearch example = new MultipleCriteriaSearch();

        // Define multiple criteria
        List<SearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(new SearchCriteria("Name", "John", false)); // Search for name containing "John"
        criteriaList.add(new SearchCriteria("Grade", "85", true)); // Search for grade equal to 85

        // Perform the search with the criteria
        List<Integer> results = example.advancedSearch(criteriaList);

        // Print the matching row indices
        System.out.println("Matching rows: " + results);
    }
}
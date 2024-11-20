package koq.encoder.components;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JTableRowExample {

    public static void main(String[] args) {
        // Sample data
        Object[][] data = {
            {1, "John", 30, 50000.0},
            {2, "Jane", 28, 60000.0},
            {3, "Bob", 35, 70000.0},
        };

        // Column names
        String[] columnNames = {"ID", "Name", "Age", "Salary"};

        // Create a DefaultTableModel and JTable
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);

        // Create a JScrollPane to add the JTable to the GUI
        JScrollPane scrollPane = new JScrollPane(table);

        // Create a button to trigger the row output
        JButton button = new JButton("Get Row Values");
        button.addActionListener(e -> {
            // Get the selected row index
            int selectedRow = table.getSelectedRow();

            if (selectedRow != -1) {
                // Output the entire row values
                System.out.println("Row " + selectedRow + " values:");

                int columnCount = table.getColumnCount();
                for (int col = 0; col < columnCount; col++) {
                    Object value = table.getValueAt(selectedRow, col);
                    System.out.println(table.getColumnName(col) + ": " + value);
                }
            } else {
                System.out.println("No row selected.");
            }
        });

        // Create a frame to hold the JTable and button
        JFrame frame = new JFrame("JTable Row Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.add(scrollPane);
        frame.add(button);

        // Set the frame size and make it visible
        frame.setSize(400, 300);
        frame.setVisible(true);
    }
}

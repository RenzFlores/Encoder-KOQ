import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.*;

public class SearchableTable extends JFrame {
    private JTable table;
    private MyTableModel tableModel;
    private JTextField searchField;

    public SearchableTable() {
        tableModel = new MyTableModel();
        table = new JTable(tableModel);
        searchField = new JTextField(20);

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText();
                tableModel.filter(query);
            }
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(searchField, BorderLayout.NORTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    class MyTableModel extends AbstractTableModel {
        private String[] columnNames = {"Name", "Age", "City"};
        private Object[][] data = {
            {"John", 25, "New York"},
            {"Anna", 30, "London"},
            {"Mike", 35, "Chicago"}
        };
        private Object[][] filteredData = data;

        public int getRowCount() {
            return filteredData.length;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return filteredData[rowIndex][columnIndex];
        }

        public String getColumnName(int column) {
            return columnNames[column];
        }

        public void filter(String query) {
            // Implement filtering logic here
            // Update filteredData based on the query
            fireTableDataChanged();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SearchableTable());
    }
}
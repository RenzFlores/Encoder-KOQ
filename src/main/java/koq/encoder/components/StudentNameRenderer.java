package koq.encoder.components;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.TableCellRenderer;

public class StudentNameRenderer extends JTextArea implements TableCellRenderer {
    public StudentNameRenderer() {
        setLineWrap(true);      // Enable line wrapping
        setWrapStyleWord(true); // Wrap at word boundaries
        setOpaque(true);        // Ensure background is painted
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
                                                   boolean isSelected, boolean hasFocus, 
                                                   int row, int column) {
        // Set font to match the table's font
        setFont(table.getFont());
        
        // Set text and alignment
        setText(value != null ? value.toString() : "");
        setAlignmentX(LEFT_ALIGNMENT);

        // Adjust colors based on selection
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        // Adjust row height to fit wrapped text
        setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);
        if (table.getRowHeight(row) < getPreferredSize().height) {
            table.setRowHeight(row, getPreferredSize().height);
        }

        return this;
    }
}
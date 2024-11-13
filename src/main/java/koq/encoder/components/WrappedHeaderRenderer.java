package koq.encoder.components;

import java.awt.Color;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class WrappedHeaderRenderer extends DefaultTableCellRenderer {
    public WrappedHeaderRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setOpaque(false);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, 
            boolean isSelected, boolean hasFocus, 
            int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Set background and foreground colors based on selection state
        if (isSelected) {
            setBackground(new Color(184, 207, 229));
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(Color.LIGHT_GRAY);
            setForeground(table.getForeground());
        }
        
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
                
        setText("<html><center>" + value.toString().replace(" ", "<br>") + "</center></html>");
        
        return this;
    }
}

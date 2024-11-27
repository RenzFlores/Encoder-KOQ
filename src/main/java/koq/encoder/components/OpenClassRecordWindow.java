package koq.encoder.components;

import javax.swing.*;

public class OpenClassRecordWindow extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;
    private JButton openButton;
    
    public OpenClassRecordWindow() {
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

        openButton = new JButton("Open");
        
        add(scrollPane, java.awt.BorderLayout.CENTER);
        add(openButton, java.awt.BorderLayout.PAGE_END);

        setTitle("Open Class Record");
        setSize(new java.awt.Dimension(720, 360));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Constants.WINDOW_COLOR_LAYER_0);
    }
    
    public JTable getTable() {
        return table;
    }
    
    public JButton getButton() {
        return openButton;
    }
}

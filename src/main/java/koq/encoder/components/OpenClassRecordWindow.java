package koq.encoder.components;

import javax.swing.*;

public class OpenClassRecordWindow extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;
    private JButton openButton;
    
    public OpenClassRecordWindow() {
        table = new JTable();

        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);

        openButton = new JButton("Open");
        
        add(scrollPane, java.awt.BorderLayout.CENTER);
        add(openButton, java.awt.BorderLayout.PAGE_END);

        setTitle("Open Class Record");
        setSize(new java.awt.Dimension(720, 360));
        setLocationRelativeTo(null);
    }
    
    public JTable getTable() {
        return table;
    }
    
    public JButton getButton() {
        return openButton;
    }
}

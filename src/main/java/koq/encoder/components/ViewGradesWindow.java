package koq.encoder.components;

import javax.swing.*;
import koq.encoder.classes.Student;

public class ViewGradesWindow extends JFrame {
    
    private JLabel firstSemesterLabel;
    private JLabel secondSemesterLabel;
    private JScrollPane scrollPaneQ1;
    private JScrollPane scrollPaneQ2;
    private JTable tableQ1;
    private JTable tableQ2;
    
    public ViewGradesWindow() {
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        
        firstSemesterLabel = new JLabel("First Quarter");
        firstSemesterLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        tableQ1 = new JTable();
        tableQ1.setRowHeight(30);
        //tableQ1.setPreferredSize(new java.awt.Dimension(490, 100));
        
        scrollPaneQ1 = new JScrollPane();
        scrollPaneQ1.setPreferredSize(new java.awt.Dimension(690, 275));
        scrollPaneQ1.setViewportView(tableQ1);
        
        secondSemesterLabel = new JLabel("Second Quarter");
        
        scrollPaneQ2 = new JScrollPane();
        scrollPaneQ2.setPreferredSize(new java.awt.Dimension(690, 275));

        tableQ2 = new JTable();
        tableQ2.setRowHeight(30);
        //tableQ2.setPreferredSize(new java.awt.Dimension(490, 100));
        scrollPaneQ2.setViewportView(tableQ2);

        add(firstSemesterLabel);
        add(scrollPaneQ1);
        add(secondSemesterLabel);
        add(scrollPaneQ2);
        
        setTitle("View Grades");
        setSize(new java.awt.Dimension(720, 640));
        setLocationRelativeTo(null);
    }
    
    public JTable getTableQ1() {
        return tableQ1;
    }
    public JTable getTableQ2() {
        return tableQ2;
    }
    
    
}

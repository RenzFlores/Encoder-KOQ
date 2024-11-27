package koq.encoder.components;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.Box.Filler;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import koq.encoder.classes.Student;

public class StudentWindow extends JFrame {
    private JLabel emailContent;
    private JLabel emailLabel;
    private Filler filler1;
    private Filler filler2;
    private Filler filler3;
    private JPanel dashboardPanel;
    private JPanel containerPanel;
    private JScrollPane scrollPane;
    private JTable table;
    private JLabel lrnContent;
    private JLabel lrnLabel;
    private JButton previewReportCardButton;
    private JSeparator separator;
    private JLabel strandContent;
    private JLabel strandLabel;
    private JLabel studentNameContent;
    private JButton viewGradesButton;
    
    public StudentWindow(Student s) {
        dashboardPanel = new JPanel();
        studentNameContent = new JLabel();
        separator = new JSeparator();
        containerPanel = new JPanel();
        emailLabel = new JLabel();
        emailContent = new JLabel();
        filler1 = new Box.Filler(new Dimension(180, 1), new Dimension(230, 1), new Dimension(180, 1));
        lrnLabel = new JLabel();
        lrnContent = new JLabel();
        filler2 = new Box.Filler(new Dimension(180, 1), new Dimension(230, 1), new Dimension(180, 1));
        strandLabel = new JLabel();
        strandContent = new JLabel();
        filler3 = new Box.Filler(new Dimension(180, 1), new Dimension(230, 1), new Dimension(180, 1));
        previewReportCardButton = new JButton();
        viewGradesButton = new JButton();
        scrollPane = new JScrollPane();
        table = new JTable();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(720, 600));

        dashboardPanel.setBackground(new Color(250, 150, 150));
        dashboardPanel.setPreferredSize(new Dimension(400, 200));
        dashboardPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        studentNameContent.setFont(new Font("Segoe UI", 0, 18)); // NOI18N
        studentNameContent.setText(s.getStudentFullName());
        dashboardPanel.add(studentNameContent);

        separator.setPreferredSize(new Dimension(690, 10));
        dashboardPanel.add(separator);

        containerPanel.setBackground(new Color(250, 150, 150));
        containerPanel.setPreferredSize(new Dimension(250, 150));
        containerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        emailLabel.setText("Email:");
        containerPanel.add(emailLabel);

        emailContent.setText("renztomagan12@gmail.com");
        containerPanel.add(emailContent);
        containerPanel.add(filler1);

        lrnLabel.setText("LRN:");
        containerPanel.add(lrnLabel);

        lrnContent.setText(String.valueOf(s.getLrn()));
        containerPanel.add(lrnContent);
        containerPanel.add(filler2);

        strandLabel.setText("Strand:");
        containerPanel.add(strandLabel);

        strandContent.setText(s.getStrand());
        containerPanel.add(strandContent);
        containerPanel.add(filler3);

        previewReportCardButton.setText("Preview Report Card");
        containerPanel.add(previewReportCardButton);

        viewGradesButton.setText("View Grades");
        viewGradesButton.setPreferredSize(new Dimension(138, 23));
        containerPanel.add(viewGradesButton);
        
        scrollPane.setViewportView(table);
        
        dashboardPanel.add(containerPanel);
        
        add(dashboardPanel, BorderLayout.NORTH);
    }
    
    
    
}

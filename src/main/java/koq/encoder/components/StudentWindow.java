package koq.encoder.components;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.*;
import javax.swing.Box.Filler;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import koq.encoder.classes.Student;

public class StudentWindow extends JPanel {
    private JLabel emailContent;
    private JLabel emailLabel;
    private Filler filler1;
    private Filler filler2;
    private Filler filler3;
    private Filler filler4;
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
    private JTextField schoolYearField;
    
    public StudentWindow() {
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
        filler4 = new Box.Filler(new Dimension(100, 1), new Dimension(100, 1), new Dimension(100, 1));
        previewReportCardButton = new JButton();
        viewGradesButton = new JButton();
        schoolYearField = new JTextField();
        scrollPane = new JScrollPane();
        table = new JTable();
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Disable column resizing
        table.getTableHeader().setResizingAllowed(false);

        // Disable column reordering
        table.getTableHeader().setReorderingAllowed(false);
        
        // Set row height
        table.setRowHeight(30);
        
        setPreferredSize(new Dimension(450, 600));
        setBackground(Constants.WINDOW_COLOR_LAYER_0);

        dashboardPanel.setBackground(new Color(250, 150, 150));
        dashboardPanel.setPreferredSize(new Dimension(450, 200));
        dashboardPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        studentNameContent.setFont(new Font("Segoe UI", 0, 18)); // NOI18N
        dashboardPanel.add(studentNameContent);

        separator.setPreferredSize(new Dimension(440, 10));
        dashboardPanel.add(separator);

        containerPanel.setBackground(new Color(250, 150, 150));
        containerPanel.setPreferredSize(new Dimension(250, 100));
        containerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        emailLabel.setText("Email:");
        containerPanel.add(emailLabel);

        containerPanel.add(emailContent);
        containerPanel.add(filler1);

        lrnLabel.setText("LRN:");
        containerPanel.add(lrnLabel);

        containerPanel.add(lrnContent);
        containerPanel.add(filler2);

        strandLabel.setText("Strand:");
        containerPanel.add(strandLabel);

        containerPanel.add(strandContent);
        containerPanel.add(filler3);

        previewReportCardButton.setText("Preview Report Card");
        previewReportCardButton.setPreferredSize(new Dimension(130, 30));
        
        schoolYearField.setPreferredSize(new Dimension(130, 30));
        schoolYearField.setText("School Year");
        schoolYearField.setForeground(Color.GRAY);
        
        viewGradesButton.setText("View Grades");
        viewGradesButton.setPreferredSize(new Dimension(130, 30));
        
        scrollPane.setViewportView(table);
        
        dashboardPanel.add(containerPanel);
        dashboardPanel.add(filler4);
        dashboardPanel.add(schoolYearField);
        dashboardPanel.add(previewReportCardButton);
        dashboardPanel.add(viewGradesButton);
        
        // Set FocusListener to show text field hint
        schoolYearField.addFocusListener(new FocusAdapter() {
            String hint = "School Year";
            @Override
            public void focusGained(FocusEvent e) {
                if (schoolYearField.getText().equals(hint)) {
                    schoolYearField.setText("");
                    schoolYearField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (schoolYearField.getText().isEmpty()) {
                    schoolYearField.setText(hint);
                    schoolYearField.setForeground(Color.GRAY);
                }
            }
        });
        
        add(dashboardPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void initWindow(Student s) {
        studentNameContent.setText(s.getStudentFullName());
        emailContent.setText(s.getEmail());
        lrnContent.setText(String.valueOf(s.getLrn()));
        strandContent.setText(s.getStrand());
    }
    
    public JButton getViewGradesButton() {
        return viewGradesButton;
    }
    
    public JButton getPreviewReportCardButton() {
        return previewReportCardButton;
    }
    
    public JTable getTable() {
        return table;
    }
    
    public String getSYField() {
        return schoolYearField.getText();
    }
}

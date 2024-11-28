package koq.encoder.components;

import javax.swing.*;
import koq.encoder.classes.Student;

public class ReportCardWindow extends JFrame {
    
    private JLabel studentNameLabel;
    private JLabel studentNameContent;
    private JLabel gradeLabel;
    private JLabel gradeContent;
    private JLabel sectionLabel;
    private JLabel sectionContent;
    private JLabel strandLabel;
    private JLabel strandContent;
    private JLabel birthdayLabel;
    private JLabel birthdayContent;
    private JLabel sexLabel;
    private JLabel sexContent;
    private JLabel lrnLabel;
    private JLabel lrnContent;
    private JLabel schoolYearLabel;
    private JLabel schoolYearContent;
    private JLabel titleLabel;
    private JLabel firstSemesterLabel;
    private JLabel secondSemesterLabel;
    private JScrollPane scrollPaneQ1;
    private JScrollPane scrollPaneQ2;
    private JTable tableQ1;
    private JTable tableQ2;
    private JButton prevButton;
    private JButton nextButton;
    private javax.swing.Box.Filler buttonFiller;
    
    public ReportCardWindow(boolean viewingAsFaculty) {
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        studentNameLabel = new JLabel("Name:");
        studentNameLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        studentNameContent = new JLabel();
        studentNameContent.setPreferredSize(new java.awt.Dimension(260, 16));
        
        gradeLabel = new JLabel("Grade:");
        gradeLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        gradeContent = new JLabel();
        gradeContent.setPreferredSize(new java.awt.Dimension(260, 16));
        
        sectionLabel = new JLabel("Section:");
        sectionLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        sectionContent = new JLabel();
        sectionContent.setPreferredSize(new java.awt.Dimension(260, 16));
        
        strandLabel = new JLabel("Strand:");
        strandLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        strandContent = new JLabel();
        strandContent.setPreferredSize(new java.awt.Dimension(260, 16));
        
        birthdayLabel = new JLabel("Birthday:");
        birthdayLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        birthdayContent = new JLabel();
        birthdayContent.setPreferredSize(new java.awt.Dimension(260, 16));
        
        sexLabel = new JLabel("Sex:");
        sexLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        sexContent = new JLabel();
        sexContent.setPreferredSize(new java.awt.Dimension(260, 16));
        
        lrnLabel = new JLabel("LRN:");
        lrnLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        lrnContent = new JLabel();
        lrnContent.setPreferredSize(new java.awt.Dimension(260, 16));
        
        schoolYearLabel = new JLabel("School Year:");
        schoolYearLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        schoolYearContent = new JLabel();
        schoolYearContent.setPreferredSize(new java.awt.Dimension(260, 16));
        
        titleLabel = new JLabel("REPORT ON LEARNING PROGRESS AND ACHIEVEMENT");
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setPreferredSize(new java.awt.Dimension(710, 16));
        
        firstSemesterLabel = new JLabel("First Semester");
        firstSemesterLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        tableQ1 = new JTable();
        //tableQ1.setPreferredSize(new java.awt.Dimension(490, 100));
        
        scrollPaneQ1 = new JScrollPane();
        scrollPaneQ1.setPreferredSize(new java.awt.Dimension(690, 275));
        scrollPaneQ1.setViewportView(tableQ1);
        
        secondSemesterLabel = new JLabel("Second Semester");
        
        scrollPaneQ2 = new JScrollPane();
        scrollPaneQ2.setPreferredSize(new java.awt.Dimension(690, 275));

        tableQ2 = new JTable();
        //tableQ2.setPreferredSize(new java.awt.Dimension(490, 100));
        scrollPaneQ2.setViewportView(tableQ2);

        prevButton = new JButton("Previous");
        prevButton.setPreferredSize(new java.awt.Dimension(80, 24));
        
        buttonFiller = new Box.Filler(new java.awt.Dimension(0, 30), new java.awt.Dimension(0, 30), new java.awt.Dimension(0, 30));
        
        nextButton = new JButton("Next");
        nextButton.setPreferredSize(new java.awt.Dimension(80, 24));
        
        add(studentNameLabel);
        add(studentNameContent);
        add(gradeLabel);
        add(gradeContent);
        //add(sectionLabel);
        //add(sectionContent);
        add(strandLabel);
        add(strandContent);
        add(birthdayLabel);
        add(birthdayContent);
        add(sexLabel);
        add(sexContent);
        add(lrnLabel);
        add(lrnContent);
        add(schoolYearLabel);
        add(schoolYearContent);
        add(titleLabel);
        add(firstSemesterLabel);
        add(scrollPaneQ1);
        add(secondSemesterLabel);
        add(scrollPaneQ2);
        
        if (viewingAsFaculty) {
            add(prevButton);
            add(buttonFiller);
            add(nextButton);
        }
        
        setTitle("Report Card");
        setSize(new java.awt.Dimension(720, 800));
        setLocationRelativeTo(null);
    }
    
    public void setStudentData(Student s, String schoolYear) {
        studentNameContent.setText(s.getStudentFullName());
        gradeContent.setText(String.valueOf(s.getGradeLevel()));
        lrnContent.setText(String.valueOf(s.getLrn()));
        strandContent.setText(s.getStrand());
        birthdayContent.setText(s.getDateOfBirthFormatted());
        sexContent.setText(s.getGender());
        schoolYearContent.setText(schoolYear);
    }
    
    public JTable getTableSem1() {
        return tableQ1;
    }
    
    public JTable getTableSem2() {
        return tableQ2;
    }
    
    public JButton getPreviousButton() {
        return prevButton;
    }
    
    public JButton getNextButton() {
        return nextButton;
    }
}

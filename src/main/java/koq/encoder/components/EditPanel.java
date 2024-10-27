package koq.encoder.components;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import koq.encoder.mvc.Model.Actions;
import koq.encoder.mvc.Model.Fields;
import koq.encoder.mvc.View;

/**
 *
 * @author KOQ
 */
public class EditPanel extends JPanel {
    
    private final JPanel containerPanel;
    
    private final JLabel windowLabel;
    private final JSeparator windowTitleSeparator;
    
    private final JLabel studentNameLabel;
    private final JLabel studentNameContent;
    private final JLabel classLabel;
    private final JLabel classContent;
    private final JLabel outputTypeLabel;
    private final JComboBox outputTypeCombo;
    private final JLabel outputNumberLabel;
    private final JComboBox outputNumberCombo;
    private final JLabel gradeLabel;
    private final JTextField gradeField;
    private final JLabel maxGradeLabel;
    
    private final JButton prevButton;
    private final Box.Filler buttonFiller;
    private final JButton nextButton;
    
    public JPanel getContainer() {
        return containerPanel;
    }
    
    public JLabel getStudentNameContent() {
        return studentNameContent;
    }
    
    public JLabel getStudentClassContent() {
        return classContent;
    }
    
    public JComboBox getOutputTypeCombo() {
        return outputTypeCombo;
    }
    
    public JComboBox getOutputNumberCombo() {
        return outputNumberCombo;
    }
    
    public JTextField getGradeField() {
        return gradeField;
    }
    
    void setStudentName(View v, int row) {
        JTable selectedName = (JTable)v.getComponent("table");
        studentNameContent.setText((String) selectedName.getValueAt(row , 0)); 
    }
    
    public EditPanel() {
        setPreferredSize(new java.awt.Dimension(300, 710));
        setLayout(new CardLayout());
        
        containerPanel = new JPanel();
        containerPanel.setPreferredSize(new java.awt.Dimension(250, 500));
        containerPanel.setLayout(new FlowLayout());
        containerPanel.setBackground(new Color(200, 200, 240));

        windowLabel = new JLabel("Encode Grades for:");
        windowLabel.setPreferredSize(new java.awt.Dimension(200, 24));
        containerPanel.add(windowLabel);

        windowTitleSeparator = new JSeparator();
        windowTitleSeparator.setPreferredSize(new java.awt.Dimension(200, 10));
        containerPanel.add(windowTitleSeparator);

        containerPanel.setMinimumSize(new java.awt.Dimension(200, 300));
        containerPanel.setPreferredSize(new java.awt.Dimension(250, 170));

        studentNameLabel = new JLabel("Student name:");
        studentNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        studentNameLabel.setPreferredSize(new java.awt.Dimension(100, 24));
        containerPanel.add(studentNameLabel);

        studentNameContent = new JLabel();
        studentNameContent.setPreferredSize(new java.awt.Dimension(100, 24));
        containerPanel.add(studentNameContent);

        classLabel = new JLabel("Class:");
        classLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        classLabel.setPreferredSize(new java.awt.Dimension(100, 24));
        containerPanel.add(classLabel);

        classContent = new JLabel();
        classContent.setPreferredSize(new java.awt.Dimension(100, 24));
        containerPanel.add(classContent);

        outputTypeLabel = new JLabel("Output Type:");
        outputTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        outputTypeLabel.setPreferredSize(new java.awt.Dimension(100, 24));
        // NOTE: OMITTED COMPONENT FOR NOW
        // containerPanel.add(outputTypeLabel);

        outputTypeCombo = new JComboBox(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activity", "Assignment", "Quiz", "Exam" }));
        outputTypeCombo.setMinimumSize(new java.awt.Dimension(100, 22));
        outputTypeCombo.setPreferredSize(new java.awt.Dimension(100, 24));
        // NOTE: OMITTED COMPONENT FOR NOW
        // containerPanel.add(outputTypeCombo);

        outputNumberLabel = new JLabel("Output Number:");
        outputNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        outputNumberLabel.setPreferredSize(new java.awt.Dimension(100, 24));
        containerPanel.add(outputNumberLabel);
        
        outputNumberCombo = new JComboBox(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        outputNumberCombo.setMinimumSize(new java.awt.Dimension(100, 22));
        outputNumberCombo.setPreferredSize(new java.awt.Dimension(100, 24));
        containerPanel.add(outputNumberCombo);

        gradeLabel = new JLabel("Grade:");
        gradeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gradeLabel.setPreferredSize(new java.awt.Dimension(120, 24));
        containerPanel.add(gradeLabel);

        gradeField = new JTextField();
        gradeField.setPreferredSize(new java.awt.Dimension(50, 24));
        containerPanel.add(gradeField);
        
        maxGradeLabel = new JLabel();
        maxGradeLabel.setPreferredSize(new java.awt.Dimension(50, 24));
        containerPanel.add(maxGradeLabel);
        
        prevButton = new JButton("Previous");
        prevButton.setPreferredSize(new java.awt.Dimension(80, 24));
        
        buttonFiller = new Box.Filler(new java.awt.Dimension(20, 20), new java.awt.Dimension(20, 20), new java.awt.Dimension(20, 20));
        
        nextButton = new JButton("Next");
        nextButton.setPreferredSize(new java.awt.Dimension(80, 24));
        
        outputTypeCombo.setName(Fields.SELECT_ACTIVITY_TYPE.name());
        outputNumberCombo.setName(Fields.SELECT_ACTIVITY.name());
        gradeField.setName(Fields.EDIT_GRADE.name());
        maxGradeLabel.setName(Fields.MAX_GRADE.name());
        prevButton.setName(Actions.PREVIOUSSTUDENT.name());
        nextButton.setName(Actions.NEXTSTUDENT.name());
        
        containerPanel.add(prevButton);
        containerPanel.add(buttonFiller);
        containerPanel.add(nextButton);
        
        add(containerPanel);
    }
}
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
    
    private final Box.Filler buttonFiller1;
    private final JButton prevButton;
    private final Box.Filler buttonFiller2;
    private final JButton nextButton;
    
    private final JLabel rearrangeLabel;
    private final JButton moveDownButton;
    private final JButton moveUpButton;
    
    public EditPanel() {
        setPreferredSize(new java.awt.Dimension(200, 710));
        setLayout(new CardLayout());
        
        containerPanel = new JPanel();
        containerPanel.setPreferredSize(new java.awt.Dimension(180, 500));
        containerPanel.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT, 10, 5));
        containerPanel.setBackground(Constants.WINDOW_COLOR_LAYER_0);

        windowLabel = new JLabel("Encode Grades for:");
        windowLabel.setPreferredSize(new java.awt.Dimension(180, 24));
        containerPanel.add(windowLabel);

        windowTitleSeparator = new JSeparator();
        windowTitleSeparator.setPreferredSize(new java.awt.Dimension(180, 10));
        containerPanel.add(windowTitleSeparator);

        containerPanel.setMinimumSize(new java.awt.Dimension(200, 300));
        containerPanel.setPreferredSize(new java.awt.Dimension(250, 170));

        studentNameLabel = new JLabel("Student name:");
        //studentNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        studentNameLabel.setPreferredSize(new java.awt.Dimension(100, 24));
        //studentNameLabel.setBorder(new LineBorder(java.awt.Color.RED, 1));    // DEBUG
        containerPanel.add(studentNameLabel);

        studentNameContent = new JLabel();
        studentNameContent.setPreferredSize(new java.awt.Dimension(180, 24));
        //studentNameContent.setBorder(new LineBorder(java.awt.Color.RED, 1));          // DEBUG
        containerPanel.add(studentNameContent);

        classLabel = new JLabel("Class:");
        //classLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        classLabel.setPreferredSize(new java.awt.Dimension(100, 24));
        //classLabel.setBorder(new LineBorder(java.awt.Color.RED, 1));          // DEBUG
        //containerPanel.add(classLabel);           UNUSED

        classContent = new JLabel();
        classContent.setPreferredSize(new java.awt.Dimension(100, 24));
        //classContent.setBorder(new LineBorder(java.awt.Color.RED, 1));          // DEBUG
        //containerPanel.add(classContent);         UNUSED

        outputTypeLabel = new JLabel("Output Type:");
        //outputTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        outputTypeLabel.setPreferredSize(new java.awt.Dimension(100, 24));
        //outputTypeLabel.setBorder(new LineBorder(java.awt.Color.RED, 1));     // DEBUG
        // NOTE: OMITTED COMPONENT FOR NOW
        // containerPanel.add(outputTypeLabel);

        outputTypeCombo = new JComboBox(new javax.swing.DefaultComboBoxModel<>(new String[] { "Written Work", "Performance Task", "Quarterly Assessment" }));
        outputTypeCombo.setMinimumSize(new java.awt.Dimension(100, 22));
        outputTypeCombo.setPreferredSize(new java.awt.Dimension(100, 24));
        // NOTE: OMITTED COMPONENT FOR NOW
        // containerPanel.add(outputTypeCombo);

        outputNumberLabel = new JLabel("Output Number:");
        //outputNumberLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        outputNumberLabel.setPreferredSize(new java.awt.Dimension(100, 24));
        //outputNumberLabel.setBorder(new LineBorder(java.awt.Color.RED, 1));   // DEBUG
        containerPanel.add(outputNumberLabel);
        
        outputNumberCombo = new JComboBox(new javax.swing.DefaultComboBoxModel<>(new String[] {}));
        outputNumberCombo.setMinimumSize(new java.awt.Dimension(150, 22));
        outputNumberCombo.setPreferredSize(new java.awt.Dimension(150, 24));
        containerPanel.add(outputNumberCombo);

        gradeLabel = new JLabel("Grade:");
        //gradeLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gradeLabel.setPreferredSize(new java.awt.Dimension(150, 24));
        //gradeLabel.setBorder(new LineBorder(java.awt.Color.RED, 1));          // DEBUG
        containerPanel.add(gradeLabel);

        gradeField = new JTextField();
        gradeField.setPreferredSize(new java.awt.Dimension(40, 24));
        containerPanel.add(gradeField);
        
        maxGradeLabel = new JLabel();
        maxGradeLabel.setPreferredSize(new java.awt.Dimension(80, 24));
        //maxGradeLabel.setBorder(new LineBorder(java.awt.Color.RED, 1));       // DEBUG
        containerPanel.add(maxGradeLabel);
        
        prevButton = new JButton("Previous");
        prevButton.setPreferredSize(new java.awt.Dimension(80, 24));
        
        buttonFiller1 = new Box.Filler(new java.awt.Dimension(40, 20), new java.awt.Dimension(40, 20), new java.awt.Dimension(40, 20));
        buttonFiller2 = new Box.Filler(new java.awt.Dimension(0, 30), new java.awt.Dimension(0, 30), new java.awt.Dimension(0, 30));
        
        nextButton = new JButton("Next");
        nextButton.setPreferredSize(new java.awt.Dimension(80, 24));
        
        rearrangeLabel = new JLabel("Rearrange:");
        rearrangeLabel.setHorizontalAlignment(java.awt.FlowLayout.TRAILING);
        rearrangeLabel.setPreferredSize(new java.awt.Dimension(110, 24));
        //rearrangeLabel.setBorder(new LineBorder(java.awt.Color.RED, 1));      // DEBUG
        moveUpButton = new JButton("↑");
        moveDownButton = new JButton("↓");
        
        outputTypeCombo.setName(Fields.SELECT_ACTIVITY_TYPE.name());
        outputNumberCombo.setName(Fields.SELECT_ACTIVITY.name());
        gradeField.setName(Fields.EDIT_GRADE.name());
        maxGradeLabel.setName(Fields.MAX_GRADE.name());
        prevButton.setName(Actions.PREVIOUS_STUDENT.name());
        nextButton.setName(Actions.NEXT_STUDENT.name());
        moveUpButton.setName(Actions.MOVE_ROW_UP.name());
        moveDownButton.setName(Actions.MOVE_ROW_DOWN.name());
        
        //containerPanel.add(buttonFiller1);
        containerPanel.add(prevButton);
        containerPanel.add(buttonFiller2);
        containerPanel.add(nextButton);
        
        /* UNUSED COMPONENTS
        containerPanel.add(rearrangeLabel);
        containerPanel.add(moveUpButton);
        containerPanel.add(moveDownButton);
        */
        
        add(containerPanel);
    }
    
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
    
    // UNUSED
    void setStudentName(View v, int row) {
        JTable selectedName = (JTable)v.getComponent("table");
        studentNameContent.setText((String) selectedName.getValueAt(row , 0)); 
    }
}
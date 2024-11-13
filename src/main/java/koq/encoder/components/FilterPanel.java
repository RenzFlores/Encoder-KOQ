package koq.encoder.components;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class FilterPanel extends JPanel {

    private final JLabel windowLabel;
    private final JLabel classLabel;
    private final JLabel outputTypeLabel;
    private final JLabel gradeMaxLabel;
    private final JLabel gradeMinLabel;
    private final JSeparator editPanelSeparator;
    private final JPanel containerPanel;
    private final JLabel studentNameLabel;
    private final JTextField studentNameField;
    private final JTextField classField;
    private final JTextField gradeMaxField;
    private final JTextField gradeMinField;
    private final JComboBox outputTypeCombo;
    
    private final JButton searchButton;
    
    public JPanel getContainer() {
        return containerPanel;
    }
    
    public FilterPanel() {
        setPreferredSize(new java.awt.Dimension(200, 710));
        setLayout(new CardLayout());
        
        containerPanel = new JPanel();
        containerPanel.setPreferredSize(new java.awt.Dimension(180, 500));
        containerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        containerPanel.setBackground(new Color(200, 200, 240));

        windowLabel = new JLabel("Filter");
        windowLabel.setPreferredSize(new java.awt.Dimension(180, 24));

        editPanelSeparator = new JSeparator();
        editPanelSeparator.setPreferredSize(new Dimension(180, 10));

        studentNameLabel = new JLabel("Student name:");
        studentNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        studentNameLabel.setPreferredSize(new java.awt.Dimension(100, 24));

        studentNameField = new JTextField();
        studentNameField.setPreferredSize(new java.awt.Dimension(100, 24));

        classLabel = new JLabel("Class:");
        classLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        classLabel.setPreferredSize(new java.awt.Dimension(100, 24));

        classField = new JTextField();
        classField.setPreferredSize(new java.awt.Dimension(100, 24));

        outputTypeLabel = new JLabel("Output Type:");
        outputTypeLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        outputTypeLabel.setPreferredSize(new java.awt.Dimension(100, 24));

        outputTypeCombo = new JComboBox(new javax.swing.DefaultComboBoxModel<>(new String[] {"All", "Activity", "Assignment", "Quiz", "Exam" }));
        outputTypeCombo.setMinimumSize(new java.awt.Dimension(100, 24));
        outputTypeCombo.setPreferredSize(new java.awt.Dimension(100, 24));

        gradeMaxLabel = new JLabel("Grade maximum:");
        gradeMaxLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gradeMaxLabel.setPreferredSize(new java.awt.Dimension(100, 24));

        gradeMaxField = new JTextField();
        gradeMaxField.setPreferredSize(new java.awt.Dimension(100, 24));

        gradeMinLabel = new JLabel("Grade minimum:");
        gradeMinLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        gradeMinLabel.setPreferredSize(new java.awt.Dimension(100, 24));

        gradeMinField = new JTextField();
        gradeMinField.setPreferredSize(new java.awt.Dimension(100, 24));
        
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new java.awt.Dimension(100, 24));
                
        containerPanel.add(windowLabel);
        containerPanel.add(editPanelSeparator);
        containerPanel.add(studentNameLabel);
        containerPanel.add(studentNameField);
        //containerPanel.add(classLabel);               // UNUSED
        //containerPanel.add(classField);               // COMPONENTS
        containerPanel.add(outputTypeLabel);          
        containerPanel.add(outputTypeCombo);
        containerPanel.add(gradeMinLabel);
        containerPanel.add(gradeMinField);
        containerPanel.add(gradeMaxLabel);
        containerPanel.add(gradeMaxField);
        containerPanel.add(gradeMaxField);
        containerPanel.add(gradeMaxField);
        containerPanel.add(searchButton);
        
        add(containerPanel);
    }
}
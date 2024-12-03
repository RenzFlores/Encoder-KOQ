package koq.encoder.components;

import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.text.AbstractDocument;
import koq.encoder.mvc.Model.Actions;

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
    
    private final JLabel resultsLabel;
    private final JButton nextButton;
    private final JButton previousButton;
    
    private final JButton searchButton;
    
    public JPanel getContainer() {
        return containerPanel;
    }
    
    public FilterPanel() {
        setPreferredSize(new java.awt.Dimension(200, 710));
        setLayout(new CardLayout());
        
        containerPanel = new JPanel();
        containerPanel.setPreferredSize(new java.awt.Dimension(180, 500));
        containerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        containerPanel.setBackground(Constants.WINDOW_COLOR_LAYER_0);

        windowLabel = new JLabel("Filter");
        windowLabel.setPreferredSize(new java.awt.Dimension(180, 24));

        editPanelSeparator = new JSeparator();
        editPanelSeparator.setPreferredSize(new Dimension(180, 10));

        studentNameLabel = new JLabel("Student name:");
        studentNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        studentNameLabel.setPreferredSize(new java.awt.Dimension(100, 24));

        studentNameField = new JTextField();
        studentNameField.setPreferredSize(new java.awt.Dimension(100, 24));

        classLabel = new JLabel("Class:");
        classLabel.setHorizontalAlignment(SwingConstants.LEFT);
        classLabel.setPreferredSize(new java.awt.Dimension(100, 24));

        classField = new JTextField();
        classField.setPreferredSize(new java.awt.Dimension(100, 24));

        outputTypeLabel = new JLabel("Output Type:");
        outputTypeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        outputTypeLabel.setPreferredSize(new java.awt.Dimension(100, 24));

        outputTypeCombo = new JComboBox(new DefaultComboBoxModel<>(new String[] {"All", "Written Work", "Performance Task", "Quarterly Assessment"} ));
        outputTypeCombo.setMinimumSize(new java.awt.Dimension(100, 24));
        outputTypeCombo.setPreferredSize(new java.awt.Dimension(100, 24));

        gradeMaxLabel = new JLabel("Percentage score maximum:");
        gradeMaxLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gradeMaxLabel.setPreferredSize(new java.awt.Dimension(150, 24));

        gradeMaxField = new JTextField();
        gradeMaxField.setPreferredSize(new java.awt.Dimension(100, 24));

        gradeMinLabel = new JLabel("Percentage score minimum:");
        gradeMinLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gradeMinLabel.setPreferredSize(new java.awt.Dimension(150, 24));

        gradeMinField = new JTextField();
        gradeMinField.setPreferredSize(new java.awt.Dimension(100, 24));
        
        resultsLabel = new JLabel("0 of 0 matches");
        resultsLabel.setHorizontalAlignment(java.awt.FlowLayout.TRAILING);
        resultsLabel.setPreferredSize(new java.awt.Dimension(110, 24));
        //rearrangeLabel.setBorder(new LineBorder(java.awt.Color.RED, 1));      // DEBUG
        previousButton = new JButton("↑");
        nextButton = new JButton("↓");
        
        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new java.awt.Dimension(100, 24));
        
        searchButton.setName(Actions.FILTER.name());
        previousButton.setName(Actions.PREVIOUS_RESULT.name());
        nextButton.setName(Actions.NEXT_RESULT.name());
        
        ( (AbstractDocument) gradeMinField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        ( (AbstractDocument) gradeMaxField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
                
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
        containerPanel.add(new JLabel("%"));
        containerPanel.add(gradeMaxLabel);
        containerPanel.add(gradeMaxField);
        containerPanel.add(new JLabel("%"));
        containerPanel.add(searchButton);
        containerPanel.add(resultsLabel);
        containerPanel.add(previousButton);
        containerPanel.add(nextButton);
        
        add(containerPanel);
    }
    
    public String getStudentName() {
        return studentNameField.getText();
    }
    
    public String getOutputType() {
        return (String) outputTypeCombo.getSelectedItem();
    }
    
    public String getGradeMaximum() {
        return gradeMaxField.getText();
    }
    
    public String getGradeMinimum() {
        return gradeMinField.getText();
    }
    
    public void setResultText(int currentIndex, int range) {
        resultsLabel.setText(currentIndex + " of " + range + " matches");
    }
}
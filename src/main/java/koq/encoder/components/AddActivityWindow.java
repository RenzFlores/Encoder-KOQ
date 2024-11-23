package koq.encoder.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

public class AddActivityWindow extends JFrame {
    
    private JLabel quarterLabel;
    private JLabel activityNameLabel;
    private JLabel activityTypeLabel;
    private JLabel totalScoreLabel;
    
    private JTextField activityNameField;
    private JTextField totalScoreField;
    
    private JComboBox quarterCombo;
    private JComboBox activityTypeCombo;
    
    private JButton confirmButton;
    
    public AddActivityWindow() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        
        quarterLabel = new JLabel("Quarter:");
        quarterLabel.setPreferredSize(new java.awt.Dimension(120, 16));
        
        quarterCombo = new JComboBox();
        quarterCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2" }));
        quarterCombo.setPreferredSize(new java.awt.Dimension(120, 22));
        
        activityNameLabel = new JLabel("Activity name:");
        activityNameLabel.setPreferredSize(new java.awt.Dimension(120, 16));
        
        activityNameField = new JTextField();
        activityNameField.setPreferredSize(new java.awt.Dimension(120, 22));
        
        activityTypeLabel = new JLabel("Activity type:");
        activityTypeLabel.setPreferredSize(new java.awt.Dimension(120, 16));
        
        activityTypeCombo = new JComboBox();
        activityTypeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Written Work", "Performance Task" }));
        activityTypeCombo.setPreferredSize(new java.awt.Dimension(120, 22));
        
        totalScoreLabel = new JLabel("Total score:");
        totalScoreLabel.setPreferredSize(new java.awt.Dimension(120, 16));
        
        totalScoreField = new JTextField();
        totalScoreField.setPreferredSize(new java.awt.Dimension(120, 22));

        ( (AbstractDocument) totalScoreField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        confirmButton = new JButton("OK");
        
        //add(contextLabel);
        add(quarterLabel);
        add(quarterCombo);
        add(activityNameLabel);
        add(activityNameField);
        add(activityTypeLabel);
        add(activityTypeCombo);
        add(totalScoreLabel);
        add(totalScoreField);
        add(confirmButton);
        
        setTitle("Create a new activity record:");
        setSize(270, 180);
        setLocationRelativeTo(null);
    }
    
    public JButton getButton() {
        return confirmButton;
    }
    
    public int getQuarter() {
        return Integer.parseInt((String)quarterCombo.getSelectedItem());
    }
    
    public String getActivityName() {
        return activityNameField.getText();
    }
    
    public String getTotalScore() {
        return totalScoreField.getText();
    }
    
    public String getActivityType() {
        return (String) activityTypeCombo.getSelectedItem();
    }
}
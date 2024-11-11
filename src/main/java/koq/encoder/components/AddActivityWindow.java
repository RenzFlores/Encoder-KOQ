package koq.encoder.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddActivityWindow extends JFrame {
    
    private JLabel contextLabel;
    private JLabel activityTypeLabel;
    private JLabel totalScoreLabel;
    
    private JTextField totalScoreField;
    
    private JComboBox activityTypeCombo;
    
    private JButton confirmButton;
    
    public AddActivityWindow() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
         
        contextLabel = new JLabel("Create a new activity record:");
        contextLabel.setPreferredSize(new java.awt.Dimension(225, 16));
        
        activityTypeLabel = new JLabel("Activity type:");
        activityTypeLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        activityTypeCombo = new JComboBox();
        activityTypeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seatwork", "Homework", "Performance Task", "Quiz", "Exam" }));
        
        totalScoreLabel = new JLabel("Total score:");
        totalScoreLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        totalScoreField = new JTextField();
        totalScoreField.setPreferredSize(new java.awt.Dimension(100, 22));

        confirmButton = new JButton("OK");
        
        add(contextLabel);
        add(activityTypeLabel);
        add(activityTypeCombo);
        add(totalScoreLabel);
        add(totalScoreField);
        add(confirmButton);
                
        setSize(250, 150);
        setLocationRelativeTo(null);
    }
    
    public JButton getButton() {
        return confirmButton;
    }
    
    public String getTotalScore() {
        return totalScoreField.getText();
    }
    
    public String getActivityType() {
        return (String) activityTypeCombo.getSelectedItem();
    }
}
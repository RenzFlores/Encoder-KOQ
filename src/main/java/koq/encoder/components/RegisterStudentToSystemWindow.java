package koq.encoder.components;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class RegisterStudentToSystemWindow extends JFrame {
    private JLabel firstNameLabel;
    private JLabel middleNameLabel;
    private JLabel lastNameLabel;
    private JLabel lrnLabel;
    private JLabel genderLabel;
    private JLabel dobLabel;
    private JLabel gradeLevelLabel;
    private JLabel strandLabel;
    
    private JTextField firstNameField;
    private JTextField middleNameField;
    private JTextField lastNameField;
    private JTextField lrnField;
    private JTextField dobField;
    
    private JComboBox genderCombo;
    private JComboBox gradeLevelCombo;
    private JComboBox strandCombo;
    
    private JButton confirmButton;
    
    public RegisterStudentToSystemWindow() {
        setPreferredSize(new java.awt.Dimension(300, 350));
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        firstNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        firstNameLabel.setText("First name:");
        firstNameLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        add(firstNameLabel);

        firstNameField.setPreferredSize(new java.awt.Dimension(150, 22));
        add(firstNameField);

        middleNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        middleNameLabel.setText("Middle name:");
        middleNameLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        add(middleNameLabel);

        middleNameField.setPreferredSize(new java.awt.Dimension(150, 22));
        add(middleNameField);

        lastNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lastNameLabel.setText("Last name:");
        lastNameLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        add(lastNameLabel);

        lastNameField.setPreferredSize(new java.awt.Dimension(150, 22));
        add(lastNameField);

        lrnLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lrnLabel.setText("LRN:");
        lrnLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        add(lrnLabel);

        lrnField.setPreferredSize(new java.awt.Dimension(150, 22));
        add(lrnField);

        genderLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        genderLabel.setText("Gender:");
        genderLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        add(genderLabel);

        genderCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        genderCombo.setPreferredSize(new java.awt.Dimension(150, 22));
        add(genderCombo);

        dobLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dobLabel.setText("Date of Birth:");
        dobLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        add(dobLabel);

        dobField.setPreferredSize(new java.awt.Dimension(150, 22));
        add(dobField);

        gradeLevelLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gradeLevelLabel.setText("Grade Level:");
        gradeLevelLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        add(gradeLevelLabel);

        gradeLevelCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "11", "12" }));
        gradeLevelCombo.setPreferredSize(new java.awt.Dimension(150, 22));
        add(gradeLevelCombo);

        strandLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        strandLabel.setText("Strand:");
        strandLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        add(strandLabel);

        strandCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "STEM", "HUMSS", "GAS", "ABM", "ICT", "HE" }));
        strandCombo.setPreferredSize(new java.awt.Dimension(150, 22));
        add(strandCombo);

        confirmButton.setText("OK");
        add(confirmButton);
    }
    
    public String getFirstName() {
        return firstNameField.getText();
    }

    public String getMiddleName() {
        return middleNameField.getText();
    }

    public String getLastName() {
        return lastNameField.getText();
    }

    public String getLrn() {
        return lrnField.getText();
    }

    public String getDob() {
        return dobField.getText();
    }

    public String getGender() {
        return (String) genderCombo.getSelectedItem();
    }

    public int getGradeLevel() {
        return Integer.parseInt( (String) gradeLevelCombo.getSelectedItem());
    }

    public String getStrand() {
        return (String) strandCombo.getSelectedItem();
    }
    
    public JButton getButton() {
        return confirmButton;
    }
}

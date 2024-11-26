package koq.encoder.components;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

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
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        firstNameLabel = new JLabel("First name:");
        firstNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        firstNameLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        firstNameField = new JTextField();
        firstNameField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        middleNameLabel = new JLabel("Middle name:");
        middleNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        middleNameLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        middleNameField = new JTextField();
        middleNameField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        lastNameLabel = new JLabel("Last name:");
        lastNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lastNameLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        lastNameField = new JTextField();
        lastNameField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        lrnLabel = new JLabel("LRN:");
        lrnLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lrnLabel.setPreferredSize(new java.awt.Dimension(80, 16));

        lrnField = new JTextField();
        lrnField.setPreferredSize(new java.awt.Dimension(150, 22));

        genderLabel = new JLabel("Gender:");
        genderLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        genderLabel.setPreferredSize(new java.awt.Dimension(80, 16));

        genderCombo = new JComboBox();
        genderCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        genderCombo.setPreferredSize(new java.awt.Dimension(150, 22));

        dobLabel = new JLabel("Date of Birth:");
        dobLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dobLabel.setPreferredSize(new java.awt.Dimension(80, 16));

        dobField = new JTextField();
        dobField.setPreferredSize(new java.awt.Dimension(150, 22));

        gradeLevelLabel = new JLabel("Grade Level:");
        gradeLevelLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gradeLevelLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        gradeLevelCombo = new JComboBox();
        gradeLevelCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "11", "12" }));
        gradeLevelCombo.setPreferredSize(new java.awt.Dimension(150, 22));
        
        strandLabel = new JLabel("Strand:");
        strandLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        strandLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        strandCombo = new JComboBox();
        strandCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "STEM", "HUMSS", "GAS", "ABM", "ICT", "HE" }));
        strandCombo.setPreferredSize(new java.awt.Dimension(150, 22));
        
        confirmButton = new JButton("OK");
        
        ( (AbstractDocument) lrnField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        add(firstNameLabel);
        add(firstNameField);
        add(middleNameLabel);
        add(middleNameField);
        add(lastNameLabel);
        add(lastNameField);
        add(lrnLabel);
        add(lrnField);
        add(genderLabel);
        add(genderCombo);
        add(dobLabel);
        add(dobField);
        add(gradeLevelLabel);
        add(gradeLevelCombo);
        add(strandLabel);
        add(strandCombo);
        add(confirmButton);
        
        setTitle("Register student to system:");
        setSize(300, 340);
        setLocationRelativeTo(null);
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

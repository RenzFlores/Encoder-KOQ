package koq.encoder.components;

import java.util.stream.IntStream;
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
    
    private JComboBox genderCombo;
    private JComboBox monthCombo;
    private JComboBox dayCombo;
    private JComboBox yearCombo;
    private JComboBox gradeLevelCombo;
    private JComboBox strandCombo;
    
    private JButton confirmButton;
    
    public RegisterStudentToSystemWindow() {
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));
        getContentPane().setBackground(Constants.WINDOW_COLOR_LAYER_0);

        firstNameLabel = new JLabel("First name:");
        firstNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        firstNameLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        firstNameField = new JTextField();
        firstNameField.setPreferredSize(new java.awt.Dimension(180, 22));
        
        middleNameLabel = new JLabel("Middle name:");
        middleNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        middleNameLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        middleNameField = new JTextField();
        middleNameField.setPreferredSize(new java.awt.Dimension(180, 22));
        
        lastNameLabel = new JLabel("Last name:");
        lastNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lastNameLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        lastNameField = new JTextField();
        lastNameField.setPreferredSize(new java.awt.Dimension(180, 22));
        
        lrnLabel = new JLabel("LRN:");
        lrnLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lrnLabel.setPreferredSize(new java.awt.Dimension(80, 16));

        lrnField = new JTextField();
        lrnField.setPreferredSize(new java.awt.Dimension(180, 22));

        genderLabel = new JLabel("Gender:");
        genderLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        genderLabel.setPreferredSize(new java.awt.Dimension(80, 16));

        genderCombo = new JComboBox();
        genderCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        genderCombo.setPreferredSize(new java.awt.Dimension(180, 22));

        dobLabel = new JLabel("Date of Birth:");
        dobLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dobLabel.setPreferredSize(new java.awt.Dimension(80, 16));

        monthCombo = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        monthCombo.setPreferredSize(new java.awt.Dimension(80, 22));
        
        dayCombo = new JComboBox<>(
            IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new)
        );
        dayCombo.setPreferredSize(new java.awt.Dimension(40, 22));
        
        yearCombo = new JComboBox<>(
            IntStream.range(1970, 2025)  // 1970 to 2024
                .map(i -> 2024 - (i - 1970))   // Reverse the range
                .boxed()
                .toArray(Integer[]::new)
        );
        yearCombo.setPreferredSize(new java.awt.Dimension(50, 22));

        gradeLevelLabel = new JLabel("Grade Level:");
        gradeLevelLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gradeLevelLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        gradeLevelCombo = new JComboBox();
        gradeLevelCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "11", "12" }));
        gradeLevelCombo.setPreferredSize(new java.awt.Dimension(180, 22));
        
        strandLabel = new JLabel("Strand:");
        strandLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        strandLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        
        strandCombo = new JComboBox();
        strandCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "STEM", "HUMSS", "ABM", "ICT" }));
        strandCombo.setPreferredSize(new java.awt.Dimension(180, 22));
        
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
        add(monthCombo);
        add(dayCombo);
        add(yearCombo);
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
        return yearCombo.getSelectedItem() + "-" + (monthCombo.getSelectedIndex()+1) + "-" + dayCombo.getSelectedItem();
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

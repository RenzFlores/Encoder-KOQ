package koq.encoder.components;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterFacultyWindow extends JFrame {
    
    private JLabel nameLabel;
    private JLabel facultyIdLabel;
    private JLabel roleLabel;
    private JLabel passwordLabel;
    
    private JTextField nameField;
    private JTextField facultyIdField;
    private JTextField roleField;
    private JPasswordField passwordField;
    private JButton registerButton;
    
    public RegisterFacultyWindow() {
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        nameLabel = new JLabel("Name:");
        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        nameLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        nameField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        facultyIdLabel = new JLabel("Faculty ID:");
        facultyIdLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        facultyIdLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        facultyIdField = new JTextField();
        facultyIdField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        
        roleLabel = new JLabel("Role:");
        roleLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        roleLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        roleField = new JTextField();
        roleField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        passwordLabel = new JLabel("Password:");
        passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        passwordLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        registerButton.setText("Register");
        
        add(nameLabel);
        add(nameField);
        add(facultyIdLabel);
        add(facultyIdField);
        add(roleLabel);
        add(roleField);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
        
        setTitle("Register as a faculty");
        setSize(new java.awt.Dimension(300, 350));
        setLocationRelativeTo(null);
    }
    
    public JTextField getNameField() {
        return nameField;
    }

    public JTextField getFacultyIdField() {
        return facultyIdField;
    }

    public JTextField getRoleField() {
        return roleField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }
}

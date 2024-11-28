package koq.encoder.components;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

public class RegisterStudentWindow extends JFrame {
    
    private JLabel lrnLabel;
    private JLabel emailLabel;
    private JLabel passwordLabel;
    
    private JTextField lrnField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    
    public RegisterStudentWindow() {
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));
        getContentPane().setBackground(Constants.WINDOW_COLOR_LAYER_0);

        lrnLabel = new JLabel("LRN:");
        lrnLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lrnLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        lrnField = new JTextField();
        lrnField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        emailLabel = new JLabel("Email:");
        emailLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        emailLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        emailField = new JTextField();
        emailField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        passwordLabel = new JLabel("Password:");
        passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        passwordLabel.setPreferredSize(new java.awt.Dimension(100, 16));
        
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        registerButton = new JButton("Register");
        
        ( (AbstractDocument) lrnField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        add(lrnLabel);
        add(lrnField);
        add(emailLabel);
        add(emailField);
        add(passwordLabel);
        add(passwordField);
        add(registerButton);
        
        setTitle("Register as a student");
        setSize(new java.awt.Dimension(300, 180));
        setLocationRelativeTo(null);
    }
    
    public String getLrn() {
        return lrnField.getText();
    }

    public String getEmail() {
        return emailField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public JButton getButton() {
        return registerButton;
    }
}

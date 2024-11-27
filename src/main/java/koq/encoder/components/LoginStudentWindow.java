package koq.encoder.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginStudentWindow extends JFrame {
    
    private JLabel emailLabel;
    private JLabel passwordLabel;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginStudentWindow() {
        setLayout(new FlowLayout());
        getContentPane().setBackground(Constants.WINDOW_COLOR_LAYER_0);

        emailLabel = new JLabel("Email:");
        emailLabel.setPreferredSize(new java.awt.Dimension(150, 16));
        add(emailLabel);

        emailField = new JTextField("");
        emailField.setPreferredSize(new java.awt.Dimension(150, 22));
        add(emailField);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setPreferredSize(new java.awt.Dimension(150, 16));
        add(passwordLabel);

        passwordField = new JPasswordField("password");
        passwordField.setPreferredSize(new java.awt.Dimension(150, 22));
        add(passwordField);

        loginButton = new JButton("Login");
        add(loginButton);
        
        setSize(new java.awt.Dimension(220, 180));
        setTitle("Login");
        setLocationRelativeTo(null);
    }
    
    public String getEmail() {
        return emailField.getText();
    }
    
    public char[] getPassword() {
        return passwordField.getPassword();
    }
    
    public JButton getButton() {
        return loginButton;
    }
}

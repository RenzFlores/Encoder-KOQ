package koq.encoder.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFacultyWindow extends JFrame {
    
    private JLabel idLabel;
    private JLabel passwordLabel;
    private JTextField idField;
    private JPasswordField passwordField;
    private JButton loginButton;
    
    public LoginFacultyWindow() {
        setLayout(new FlowLayout());
        getContentPane().setBackground(Constants.WINDOW_COLOR_LAYER_0);

        idLabel = new JLabel("Teacher ID:");
        idLabel.setPreferredSize(new java.awt.Dimension(150, 16));
        add(idLabel);

        idField = new JTextField();
        idField.setPreferredSize(new java.awt.Dimension(150, 22));
        add(idField);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setPreferredSize(new java.awt.Dimension(150, 16));
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new java.awt.Dimension(150, 22));
        add(passwordField);

        loginButton = new JButton("Login");
        add(loginButton);
        
        setSize(new java.awt.Dimension(220, 180));
        setTitle("Login");
        setLocationRelativeTo(null);
    }
    
    public String getId() {
        return idField.getText();
    }
    
    public char[] getPassword() {
        return passwordField.getPassword();
    }
    
    public JButton getButton() {
        return loginButton;
    }
}

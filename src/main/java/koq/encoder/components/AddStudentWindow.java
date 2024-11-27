package koq.encoder.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddStudentWindow extends JFrame {
    
    private JLabel jLabel7;
    private JLabel jLabel8;
    private JLabel jLabel9;
    
    private JTextField jTextField5;
    private JTextField jTextField6;
    
    private JButton jButton3;
    
    public AddStudentWindow() {
        setTitle("Add New Student");
        setLayout(new java.awt.FlowLayout(FlowLayout.CENTER));
        getContentPane().setBackground(Constants.WINDOW_COLOR_LAYER_0);

        jLabel7 = new JLabel("Add new student to class record:");
        jLabel7.setPreferredSize(new java.awt.Dimension(260, 16));
        
        jLabel8 = new JLabel("First name:");
        
        jTextField5 = new JTextField();
        jTextField5.setPreferredSize(new java.awt.Dimension(200, 22));

        jLabel9 = new JLabel("Last name:");

        jTextField6 = new JTextField();
        jTextField6.setPreferredSize(new java.awt.Dimension(200, 22));

        jButton3 = new JButton("OK");

        add(jLabel7);
        add(jLabel8);
        add(jTextField5);
        add(jLabel9);
        add(jTextField6);
        add(jButton3);

        setSize(300, 150);
        setLocationRelativeTo(null);
    }
    
    public JButton getButton() {
        return jButton3;
    }
    
    public String getFirstName() {
        return jTextField5.getText();
    }
    
    public String getLastName() {
        return jTextField6.getText();
    }
}
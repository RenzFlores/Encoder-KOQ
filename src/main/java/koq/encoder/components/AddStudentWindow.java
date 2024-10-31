package koq.encoder.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddStudentWindow extends JFrame {
    
    private JLabel jLabel5;
    private JLabel jLabel4;
    private JLabel jLabel6;
    private JLabel jLabel7;
    
    private JTextField jTextField4;
    private JTextField jTextField3;
    private JComboBox jComboBox1;
    
    private javax.swing.Box.Filler filler;
    
    private JButton jButton2;
    
    public AddStudentWindow() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
         
        jLabel5 = new JLabel("Create a new class record:");
        jLabel5.setPreferredSize(new java.awt.Dimension(200, 16));
        
        jLabel4 = new JLabel("Section:");
        jLabel4.setPreferredSize(new java.awt.Dimension(60, 16));
        
        jTextField4 = new JTextField();
        jTextField4.setPreferredSize(new java.awt.Dimension(150, 22));

        jLabel6 = new JLabel("Subject:");
        jLabel6.setPreferredSize(new java.awt.Dimension(60, 16));

        jTextField3 = new JTextField();
        jTextField3.setPreferredSize(new java.awt.Dimension(150, 22));
        
        jLabel7 = new JLabel("Term:");
        jLabel7.setPreferredSize(new java.awt.Dimension(60, 16));
        jComboBox1 = new JComboBox();
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Quarter", "2nd Quarter", "3rd Quarter", "4th Quarter" }));
        
        filler = new javax.swing.Box.Filler(new java.awt.Dimension(100, 10), new java.awt.Dimension(70, 10), new java.awt.Dimension(100, 10));

        jButton2 = new JButton("OK");
        
        add(jLabel5);
        add(jLabel4);
        add(jTextField4);
        add(jLabel6);
        add(jTextField3);
        add(jLabel7);
        add(jComboBox1);
        add(filler);
        add(jButton2);
                
        setSize(250, 170);
        setLocationRelativeTo(null);
    }
}
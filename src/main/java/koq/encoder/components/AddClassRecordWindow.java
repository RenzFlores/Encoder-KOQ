package koq.encoder.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddClassRecordWindow extends JFrame {
    
    private JLabel jLabel5;
    private JLabel jLabel4;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    
    private JTextField jTextField4;
    private JTextField jTextField3;
    private JTextField jTextField2;
    private JComboBox jComboBox1;
    
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    
    private JButton jButton2;
    
    public AddClassRecordWindow() {
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
        jLabel7.setPreferredSize(new java.awt.Dimension(62, 16));
        jComboBox1 = new JComboBox();
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Quarter", "2nd Quarter", "3rd Quarter", "4th Quarter" }));
        
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(100, 10), new java.awt.Dimension(70, 10), new java.awt.Dimension(100, 10));
        
        jLabel8 = new JLabel("School Year:");
        jLabel8.setPreferredSize(new java.awt.Dimension(60, 16));
        jTextField2 = new JTextField("2024-2025");
        jTextField2.setPreferredSize(new java.awt.Dimension(150, 22));

        jButton2 = new JButton("OK");
        
        add(jLabel5);
        add(jLabel4);
        add(jTextField4);
        add(jLabel6);
        add(jTextField3);
        add(jLabel7);
        add(jComboBox1);
        add(filler1);
        add(jLabel8);
        add(jTextField2);
        add(jButton2);
                
        setSize(250, 200);
        setLocationRelativeTo(null);
    }
    
    public JButton getButton() {
        return jButton2;
    }
    
    public String getSection() {
        return jTextField4.getText();
    }
    
    public String getSubject() {
        return jTextField3.getText();
    }
    
    public String getTerm() {
        return (String) jComboBox1.getSelectedItem();
    }
    
    public String getSY() {
        return (String) jTextField2.getText();
    }
}
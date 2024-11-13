package koq.encoder.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class SetGradeWeightsWindow extends JFrame {
    
    private JLabel jLabel1;
    private JLabel jLabel3;
    private JLabel jLabel2;
    private JLabel jLabel19;
    private JLabel jLabel14;
    private JLabel jLabel18;
    private JLabel jLabel11;
    private JLabel jLabel20;
    private JLabel jLabel12;
    private JLabel jLabel17;
    private JLabel jLabel13;
    private JLabel jLabel16;
    
    private JTextField jTextField1;
    private JTextField jTextField9;
    private JTextField jTextField2;
    private JTextField jTextField7;
    private JTextField jTextField8;
    
    private JSeparator jSeparator1;
    private JSeparator jSeparator2;
    private JSeparator jSeparator3;
    
    private JButton jButton1;
    
    public SetGradeWeightsWindow() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        
        jLabel1 = new JLabel("Set grade weights for this class record:");

        jSeparator1 = new JSeparator();
        jSeparator1.setPreferredSize(new java.awt.Dimension(200, 10));
        
        jLabel3 = new JLabel("Written Works (WW)");
        jLabel3.setPreferredSize(new java.awt.Dimension(130, 16));
        
        jTextField1 = new JTextField();
        jTextField1.setPreferredSize(new java.awt.Dimension(50, 22));
        
        jLabel19 = new JLabel("%");

        jLabel12 = new JLabel("Performance Task (PT)");
        jLabel12.setPreferredSize(new java.awt.Dimension(130, 16));
        
        jTextField7 = new JTextField();
        jTextField7.setPreferredSize(new java.awt.Dimension(50, 22));
        
        jLabel17 = new JLabel("%");

        jLabel13 = new JLabel("Quarterly Assessment (QA)");
        jLabel13.setPreferredSize(new java.awt.Dimension(130, 16));
        
        jTextField8 = new JTextField();
        jTextField8.setPreferredSize(new java.awt.Dimension(50, 22));
        
        jLabel16 = new JLabel("%");
        
        jSeparator3 = new JSeparator();
        jSeparator3.setPreferredSize(new java.awt.Dimension(200, 10));
        
        jButton1 = new JButton("OK");
        
        add(jLabel1);
        add(jSeparator1);
        add(jLabel3);
        add(jTextField1);
        add(jLabel19);
        add(jLabel12);
        add(jTextField7);
        add(jLabel17);
        add(jLabel13);
        add(jTextField8);
        add(jLabel16);
        add(jSeparator3);
        add(jButton1);
        
        setSize(240, 200);
        setLocationRelativeTo(null);
    }
    
    public JButton getButton() {
        return jButton1;
    }
}

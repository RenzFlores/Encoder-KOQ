package koq.encoder.components;

import java.awt.FlowLayout;
import static java.awt.FlowLayout.TRAILING;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddClassRecordWindow extends JFrame {
    
    private JLabel jLabel5;
    private JLabel jLabel9;
    private JLabel jLabel4;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;
    
    private JComboBox jComboBox2;
    private JComboBox jComboBox3;
    private JTextField jTextField4;
    private JTextField jTextField3;
    private JTextField jTextField2;
    private JComboBox jComboBox1;
    
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    
    private JButton jButton2;
    
    public AddClassRecordWindow() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
         
        jLabel5 = new JLabel("Create a new class record:");
        jLabel5.setPreferredSize(new java.awt.Dimension(330, 16));
        
        jLabel9 = new JLabel("Grade Level:");
        jLabel9.setPreferredSize(new java.awt.Dimension(80, 16));
        jLabel9.setHorizontalAlignment(TRAILING);
        
        jComboBox2 = new JComboBox();
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"11", "12"}));
        jComboBox2.setPreferredSize(new java.awt.Dimension(150, 22));
        
        jLabel4 = new JLabel("Section:");
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 16));
        jLabel4.setHorizontalAlignment(TRAILING);
        
        jTextField4 = new JTextField();
        jTextField4.setPreferredSize(new java.awt.Dimension(150, 22));

        jLabel6 = new JLabel("Subject:");
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 16));
        jLabel6.setHorizontalAlignment(TRAILING);
        
        jComboBox3 = new JComboBox();
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
            "21st Century Literature from the Philippines and the World",
            "Komunikasyon at Pananaliksik sa Wika at Kulturang Pilipino",
            "Pagbasa at Pagsusuri ng Iba’t-Ibang Teksto Tungo sa Pananaliksik",
            "Oral Communication",
            "Reading and Writing",
            "Personal Development",
            "Understanding Culture, Society and Politics",
            "Introduction to the Philosophy of the Human Person",
            "Contemporary Philippine Arts from the Regions",
            "Media and Information Literacy",
            "General Math",
            "Statistics and Probability",
            "Earth and Life Science",
            "Physical Science",
            "Physical Education and Health 1",
            "Physical Education and Health 2",
            "Physical Education and Health 3",
            "Physical Education and Health 4"
        }));
        jComboBox3.setPreferredSize(new java.awt.Dimension(210, 22));

        jTextField3 = new JTextField();
        jTextField3.setPreferredSize(new java.awt.Dimension(150, 22));
        
        jLabel7 = new JLabel("Term:");
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 16));
        jLabel7.setHorizontalAlignment(TRAILING);
        jComboBox1 = new JComboBox();
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Quarter", "2nd Quarter", "3rd Quarter", "4th Quarter" }));
        
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(100, 10), new java.awt.Dimension(70, 10), new java.awt.Dimension(100, 10));
        
        
        jLabel8 = new JLabel("School Year:");
        jLabel8.setPreferredSize(new java.awt.Dimension(80, 16));
        jLabel8.setHorizontalAlignment(TRAILING);
        jTextField2 = new JTextField("2024-2025");
        jTextField2.setPreferredSize(new java.awt.Dimension(150, 22));
        
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(100, 10), new java.awt.Dimension(70, 10), new java.awt.Dimension(100, 10));

        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 10), new java.awt.Dimension(50, 10), new java.awt.Dimension(50, 10));
        
        jButton2 = new JButton("OK");
        
        add(jLabel5);
        add(jLabel9);
        add(jComboBox2);
        add(jLabel4);
        add(jTextField4);
        add(jLabel6);
        add(jComboBox3);
        //add(jTextField3);
        add(jLabel7);
        add(jComboBox1);
        add(filler1);
        add(jLabel8);
        add(jTextField2);
        add(filler2);
        add(filler3);
        add(jButton2);
                
        setSize(320, 230);
        setLocationRelativeTo(null);
    }
    
    public String getGradeLevel() {
        return (String) jComboBox2.getSelectedItem();
    }
    
    public JButton getButton() {
        return jButton2;
    }
    
    public String getSection() {
        return jTextField4.getText();
    }
    
    public String getSubject() {
        return (String) jComboBox3.getSelectedItem();
    }
    
    public String getTerm() {
        return (String) jComboBox1.getSelectedItem();
    }
    
    public String getSY() {
        return (String) jTextField2.getText();
    }
}
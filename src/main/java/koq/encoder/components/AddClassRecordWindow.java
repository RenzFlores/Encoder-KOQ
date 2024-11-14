package koq.encoder.components;

import java.awt.FlowLayout;
import static java.awt.FlowLayout.TRAILING;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AddClassRecordWindow extends JFrame {
    
    private JLabel gradeLevelLabel;
    private JLabel jLabel4;
    private JLabel jLabel6;
    private JLabel termLabel;
    private JLabel schoolYearLabel;
    
    private JComboBox gradeLevelCombo;
    private JComboBox subjectCombo;
    private JTextField sectionField;
    private JTextField schoolYearField;
    private JComboBox termCombo;
    
    private javax.swing.Box.Filler filler1;
    private javax.swing.Box.Filler filler2;
    private javax.swing.Box.Filler filler3;
    
    private JButton confirmButton;
    
    public AddClassRecordWindow() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        
        gradeLevelLabel = new JLabel("Grade Level:");
        gradeLevelLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        gradeLevelLabel.setHorizontalAlignment(TRAILING);
        
        gradeLevelCombo = new JComboBox();
        gradeLevelCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {"11", "12"}));
        gradeLevelCombo.setPreferredSize(new java.awt.Dimension(150, 22));
        
        jLabel4 = new JLabel("Section:");
        jLabel4.setPreferredSize(new java.awt.Dimension(80, 16));
        jLabel4.setHorizontalAlignment(TRAILING);
        
        sectionField = new JTextField();
        sectionField.setPreferredSize(new java.awt.Dimension(150, 22));

        jLabel6 = new JLabel("Subject:");
        jLabel6.setPreferredSize(new java.awt.Dimension(80, 16));
        jLabel6.setHorizontalAlignment(TRAILING);
        
        subjectCombo = new JComboBox();
        subjectCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] {
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
        subjectCombo.setPreferredSize(new java.awt.Dimension(210, 22));
        
        termLabel = new JLabel("Term:");
        termLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        termLabel.setHorizontalAlignment(TRAILING);
        termCombo = new JComboBox();
        termCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Quarter", "2nd Quarter", "3rd Quarter", "4th Quarter" }));
        
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(100, 10), new java.awt.Dimension(70, 10), new java.awt.Dimension(100, 10));
        
        
        schoolYearLabel = new JLabel("School Year:");
        schoolYearLabel.setPreferredSize(new java.awt.Dimension(80, 16));
        schoolYearLabel.setHorizontalAlignment(TRAILING);
        schoolYearField = new JTextField("2024-2025");
        schoolYearField.setPreferredSize(new java.awt.Dimension(150, 22));
        
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(100, 10), new java.awt.Dimension(70, 10), new java.awt.Dimension(100, 10));

        filler3 = new javax.swing.Box.Filler(new java.awt.Dimension(50, 10), new java.awt.Dimension(50, 10), new java.awt.Dimension(50, 10));
        
        confirmButton = new JButton("OK");
        
        add(gradeLevelLabel);
        add(gradeLevelCombo);
        add(jLabel4);
        add(sectionField);
        add(jLabel6);
        add(subjectCombo);
        //add(termLabel);
        //add(termCombo);
        //add(filler1);
        add(schoolYearLabel);
        add(schoolYearField);
        add(filler2);
        add(filler3);
        add(confirmButton);
                
        setSize(320, 180);
        setTitle("Add a new class record");
        setLocationRelativeTo(null);
    }
    
    public String getGradeLevel() {
        return (String) gradeLevelCombo.getSelectedItem();
    }
    
    public JButton getButton() {
        return confirmButton;
    }
    
    public String getSection() {
        return sectionField.getText();
    }
    
    public String getSubject() {
        return (String) subjectCombo.getSelectedItem();
    }
    
    public int getTerm() {
        return termCombo.getSelectedIndex()+1;
    }
    
    public String getSY() {
        return (String) schoolYearField.getText();
    }
}
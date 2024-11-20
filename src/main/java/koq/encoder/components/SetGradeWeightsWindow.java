package koq.encoder.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class SetGradeWeightsWindow extends JFrame {
    
    private JLabel writtenWorksLabel;
    private JLabel jLabel19;
    private JLabel performanceTaskLabel;
    private JLabel jLabel17;
    private JLabel quarterlyAssessmentLabel;
    private JLabel jLabel16;
    
    private JTextField wwWeightField;
    private JTextField ptWeightField;
    private JTextField qaWeightField;

    private JSeparator jSeparator3;
    
    private JButton confirmButton;
    
    public SetGradeWeightsWindow() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        
        writtenWorksLabel = new JLabel("Written Work (WW)");
        writtenWorksLabel.setPreferredSize(new java.awt.Dimension(130, 16));
        
        wwWeightField = new JTextField();
        wwWeightField.setPreferredSize(new java.awt.Dimension(50, 22));
        
        jLabel19 = new JLabel("%");

        performanceTaskLabel = new JLabel("Performance Task (PT)");
        performanceTaskLabel.setPreferredSize(new java.awt.Dimension(130, 16));
        
        ptWeightField = new JTextField();
        ptWeightField.setPreferredSize(new java.awt.Dimension(50, 22));
        
        jLabel17 = new JLabel("%");

        quarterlyAssessmentLabel = new JLabel("Quarterly Assessment (QA)");
        quarterlyAssessmentLabel.setPreferredSize(new java.awt.Dimension(130, 16));
        
        qaWeightField = new JTextField();
        qaWeightField.setPreferredSize(new java.awt.Dimension(50, 22));
        
        jLabel16 = new JLabel("%");
        
        jSeparator3 = new JSeparator();
        jSeparator3.setPreferredSize(new java.awt.Dimension(200, 10));
        
        confirmButton = new JButton("Confirm");
        
        add(writtenWorksLabel);
        add(wwWeightField);
        add(jLabel19);
        add(performanceTaskLabel);
        add(ptWeightField);
        add(jLabel17);
        add(quarterlyAssessmentLabel);
        add(qaWeightField);
        add(jLabel16);
        add(jSeparator3);
        add(confirmButton);
        
        setTitle("Set grade weights for this class record:");
        setSize(240, 200);
        setLocationRelativeTo(null);
    }
    
    public String getWwWeightField() {
        return wwWeightField.getText();
    }

    // Getter for ptWeightField
    public String getPtWeightField() {
        return ptWeightField.getText();
    }

    // Getter for qaWeightField
    public String getQaWeightField() {
        return qaWeightField.getText();
    }

    public JButton getButton() {
        return confirmButton;
    }
}

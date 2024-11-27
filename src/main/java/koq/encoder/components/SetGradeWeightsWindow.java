package koq.encoder.components;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

public class SetGradeWeightsWindow extends JFrame {
    
    private JLabel writtenWorksLabel;
    private JLabel wwPercLabel;
    private JLabel performanceTaskLabel;
    private JLabel ptPercLabel;
    private JLabel quarterlyAssessmentLabel;
    private JLabel qaPercLabel;
    
    private JTextField wwWeightField;
    private JTextField ptWeightField;
    private JTextField qaWeightField;

    private JSeparator separator;
    
    private JButton confirmButton;
    
    public SetGradeWeightsWindow(double[] weights) {
        int wwWeight = (int)(weights[0] * 100);
        int ptWeight = (int)(weights[1] * 100);
        int qaWeight = (int)(weights[2] * 100);
                
        setLayout(new FlowLayout(FlowLayout.CENTER));
        getContentPane().setBackground(Constants.WINDOW_COLOR_LAYER_0);
        
        writtenWorksLabel = new JLabel("Written Work (WW)");
        writtenWorksLabel.setPreferredSize(new java.awt.Dimension(130, 16));
        
        wwWeightField = new JTextField(String.valueOf(wwWeight));
        wwWeightField.setPreferredSize(new java.awt.Dimension(50, 22));
        
        wwPercLabel = new JLabel("%");

        performanceTaskLabel = new JLabel("Performance Task (PT)");
        performanceTaskLabel.setPreferredSize(new java.awt.Dimension(130, 16));
        
        ptWeightField = new JTextField(String.valueOf(ptWeight));
        ptWeightField.setPreferredSize(new java.awt.Dimension(50, 22));
        
        ptPercLabel = new JLabel("%");

        quarterlyAssessmentLabel = new JLabel("Quarterly Assessment (QA)");
        quarterlyAssessmentLabel.setPreferredSize(new java.awt.Dimension(130, 16));
        
        qaWeightField = new JTextField(String.valueOf(qaWeight));
        qaWeightField.setPreferredSize(new java.awt.Dimension(50, 22));
        
        qaPercLabel = new JLabel("%");
        
        separator = new JSeparator();
        separator.setPreferredSize(new java.awt.Dimension(200, 10));
        
        confirmButton = new JButton("Confirm");
        
        ( (AbstractDocument) wwWeightField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        ( (AbstractDocument) ptWeightField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        ( (AbstractDocument) qaWeightField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        
        add(writtenWorksLabel);
        add(wwWeightField);
        add(wwPercLabel);
        add(performanceTaskLabel);
        add(ptWeightField);
        add(ptPercLabel);
        add(quarterlyAssessmentLabel);
        add(qaWeightField);
        add(qaPercLabel);
        add(separator);
        add(confirmButton);
        
        setTitle("Set grade weights for this class record:");
        setSize(240, 170);
        setLocationRelativeTo(null);
    }
    
    public int getWwWeightField() {
        if (wwWeightField.getText().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(wwWeightField.getText());
    }

    public int getPtWeightField() {
        if (ptWeightField.getText().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(ptWeightField.getText());
    }

    public int getQaWeightField() {
        if (qaWeightField.getText().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(qaWeightField.getText());
    }

    public JButton getButton() {
        return confirmButton;
    }
}

package koq.encoder.components;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import koq.encoder.mvc.Model;

public class Toolbar extends JToolBar {
    
    JButton addButton;
    JButton removeButton;
    JButton setGradeWeightsButton;
    JButton generateReportButton;

    public Toolbar() {
        Color buttonColor = Color.WHITE;
        
        setPreferredSize(new java.awt.Dimension(880, 50));
        setBackground(new Color(180, 180, 220));
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        
        addButton = new JButton("Add");
        addButton.setName(Model.Actions.ADD_TO_TABLE.name());
        addButton.setPreferredSize(new java.awt.Dimension(90, 30));
        addButton.setBackground(buttonColor);
        addButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        removeButton = new JButton("Remove");
        removeButton.setName(Model.Actions.REMOVE_FROM_TABLE.name());
        removeButton.setPreferredSize(new java.awt.Dimension(90, 30));
        removeButton.setBackground(buttonColor);
        removeButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        setGradeWeightsButton = new JButton("Set Grade Weights");
        setGradeWeightsButton.setName(Model.Actions.EDIT_GRADE_WEIGHTS.name());
        setGradeWeightsButton.setPreferredSize(new java.awt.Dimension(120, 30));
        setGradeWeightsButton.setBackground(buttonColor);
        setGradeWeightsButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        generateReportButton = new JButton("Generate Report Card");
        generateReportButton.setName(Model.Actions.GENERATE_REPORT.name());
        generateReportButton.setPreferredSize(new java.awt.Dimension(120, 30));
        generateReportButton.setBackground(buttonColor);
        generateReportButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        add(addButton);
        //add(removeButton);                UNUSED COMPONENT
        add(setGradeWeightsButton);
        add(generateReportButton);
        
        setFloatable(false);
    }
}
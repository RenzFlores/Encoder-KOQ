/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koq.encoder.components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuWindow extends JFrame {

    private JPanel buttonPanel;
    private JPanel radioButtonPanel;
    
    private JLabel titleLabel;
    
    private JRadioButton teacherOption;
    private JRadioButton studentOption;
    
    private ButtonGroup group;
    
    private JButton loginButton;
    private JButton registerButton;
    
    public MenuWindow() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        titleLabel = new JLabel("grade.edu");
        titleLabel.setFont(new java.awt.Font("Constantia", 1, 72)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        titleLabel.setBackground(Constants.WINDOW_COLOR_LAYER_1);
        titleLabel.setPreferredSize(new Dimension(480, 120));
        
        radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        radioButtonPanel.setPreferredSize(new Dimension(200, 30));
        radioButtonPanel.setBackground(Constants.WINDOW_COLOR_LAYER_0);
        
        teacherOption = new JRadioButton("Teacher");
        teacherOption.setBackground(Constants.WINDOW_COLOR_LAYER_0);
        studentOption = new JRadioButton("Student");
        studentOption.setBackground(Constants.WINDOW_COLOR_LAYER_0);

        group = new ButtonGroup();
        group.add(teacherOption);
        group.add(studentOption);
        
        teacherOption.setSelected(true);
        
        radioButtonPanel.add(teacherOption);
        radioButtonPanel.add(studentOption);
        radioButtonPanel.setBackground(Constants.WINDOW_COLOR_LAYER_0);
        
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        buttonPanel.setPreferredSize(new Dimension(300, 50));
        buttonPanel.setBackground(Constants.WINDOW_COLOR_LAYER_0);
        
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(80, 30));
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(80, 30));
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        
        add(titleLabel);
        add(radioButtonPanel);
        add(buttonPanel);
        
        setTitle("Welcome");
        getContentPane().setBackground(Constants.WINDOW_COLOR_LAYER_0);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public JButton getLoginButton() {
        return loginButton;
    }
    
    public JButton getRegisterButton() {
        return registerButton;
    }
    
    public String getRadioButtonSelection() {
        if (teacherOption.isSelected()) {
            return teacherOption.getText();
        } else {
            return studentOption.getText();
        }
    }
}

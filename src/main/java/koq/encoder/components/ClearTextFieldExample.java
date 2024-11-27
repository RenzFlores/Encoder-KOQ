/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koq.encoder.components;
import javax.swing.*;
import java.awt.*;

public class ClearTextFieldExample {

    public static void main(String[] args) {
        // Create a frame
        JFrame frame = new JFrame("Clear JTextField Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        // Create a JTextField
        JTextField textField = new JTextField(20);
        frame.add(textField);

        // Create a button to clear the JTextField
        JButton clearButton = new JButton("Clear");
        frame.add(clearButton);

        // Add action listener to clear the JTextField when the button is clicked
        clearButton.addActionListener(e -> textField.setText(""));

        // Show the frame
        frame.setVisible(true);
    }
}
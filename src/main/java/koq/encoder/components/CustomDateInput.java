package koq.encoder.components;

import javax.swing.*;
import java.awt.*;
import java.util.stream.IntStream;

public class CustomDateInput {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Custom Date Input");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        // Create combo boxes for day, month, and year
        JComboBox<Integer> dayCombo = new JComboBox<>(
            IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new)
        );
        JComboBox<String> monthCombo = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        JComboBox<Integer> yearCombo = new JComboBox<>(
            IntStream.rangeClosed(1900, 2100).boxed().toArray(Integer[]::new)
        );

        // Add components to frame
        frame.add(dayCombo);
        frame.add(monthCombo);
        frame.add(yearCombo);

        frame.setVisible(true);
    }
}

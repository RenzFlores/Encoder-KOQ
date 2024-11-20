package koq.encoder.components;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

/**
 * Document filter for strictly numeric characters only (1-9)
 */
public class NumericDocumentFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        // Only allow digits to be inserted
        if (string != null && string.matches("\\d+")) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        // Only allow digits to replace
        if (text != null && text.matches("\\d+")) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}

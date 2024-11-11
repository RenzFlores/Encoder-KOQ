package koq.encoder.components;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class NumericDocumentListener implements DocumentListener {
    @Override
    public void insertUpdate(DocumentEvent e) {
        filterInput(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        // No need to filter on remove
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // Not needed for plain text components
    }

    private void filterInput(DocumentEvent e) {
        Document doc = e.getDocument();
        try {
            String text = doc.getText(0, doc.getLength());

            // Check if input is numeric with at most one decimal point
            if (!text.matches("\\d*\\.?\\d*")) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Remove last entered character if it’s invalid
                        doc.remove(e.getOffset(), e.getLength());
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }
}

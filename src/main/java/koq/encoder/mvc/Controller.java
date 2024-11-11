package koq.encoder.mvc;

import classes.Grade;
import koq.encoder.components.AddClassRecordWindow;
import koq.encoder.components.AddStudentWindow;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import koq.encoder.components.AddActivityWindow;
import koq.encoder.mvc.Model.Actions;
import koq.encoder.mvc.Model.Fields;

public class Controller {
    
    private final View view;
    private final Model model;
            
    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        
        JTable table = view.getTable();
        table.getTableHeader().addMouseListener(new HeaderSelector(table));
        table.addMouseListener(new RowSelector(table));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setModel(model.getClassRecord());
        table.setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
        view.resizeTableHeaders();
        view.updateEditPanel(model.getSelectedRow(), model.getSelectedActivity(), model.getClassRecord().getRowAt(model.getSelectedRow()));
        
        // New Table event
        ( (JMenuItem) view.getComponent(Actions.NEWTABLE.name()) ).addActionListener((ActionEvent ev) -> {
            AddClassRecordWindow window = new AddClassRecordWindow();
            window.setVisible(true);
            window.setLocationRelativeTo(null);
        });
        
        // Open File event
        ( (JMenuItem) view.getComponent(Actions.OPENFILE.name()) ).addActionListener((ActionEvent ev) -> {
            System.out.println("open file event");
        });
        
        /** Export File event (UNUSED)
        ( (JMenuItem) view.getComponent(Actions.EXPORTFILE.name()) ).addActionListener((ActionEvent ev) -> {});
        */
        
        // Exit event
        ( (JMenuItem) view.getComponent(Actions.EXIT.name()) ).addActionListener((ActionEvent ev) -> {
            System.exit(0);
        });
        
        // View about event
        ( (JMenuItem) view.getComponent(Actions.VIEWABOUT.name()) ).addActionListener((ActionEvent ev) -> {
            view.showAbout();
        });
        
        // Select previous student event
        ( (JButton) view.getComponent(Actions.PREVIOUSSTUDENT.name()) ).addActionListener((ActionEvent ev) -> {
            System.out.println("select previous student event");
        });
        
        // Select next student event
        ( (JButton) view.getComponent(Actions.NEXTSTUDENT.name()) ).addActionListener((ActionEvent ev) -> {
            System.out.println("select next student event");
        });
        
        // Activity selection listener
        ( (JComboBox) view.getComponent(Fields.SELECT_ACTIVITY.name()) ).addActionListener((ActionEvent ev) -> {
            model.setSelectedActivity(
                ((JComboBox) view.getComponent(Fields.SELECT_ACTIVITY.name())).getSelectedIndex()
            );
            view.updateEditPanel(model.getSelectedRow(), model.getSelectedActivity(), model.getClassRecord().getRowAt(model.getSelectedRow()));
        });
        
        // Grade text field DocumentListener
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).addActionListener((ActionEvent ev) -> {
            int row = model.getSelectedRow();
            int col = model.getSelectedActivity();
            String value = ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getText();
           
            Grade grade = model.getClassRecord().getRowAt(row).getGrades().get(col);
            // Empty field will set grade value to null
            if (value.isBlank()) {
                grade.setGrade(null);
            } 
            // Input exceeds range and will feedback an error
            else if (Double.parseDouble(value) > grade.getMaxGrade() || Double.parseDouble(value) < 0) {
                // Set border color to Red
                ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).setBorder(new LineBorder(java.awt.Color.RED, 1));
                System.out.println("Error: Input must be between 0 and " + grade.getMaxGrade());
            } 
            else {
                // Input accepted
                try {
                    // Clear border color
                    ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).setBorder(null);
                    grade.setGrade(Double.parseDouble(value));
                } 
                // An input of only '.' will result in an exception, relay an error if this happens
                catch (java.lang.NumberFormatException e) {
                    // Set border color to Red
                    ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).setBorder(new LineBorder(java.awt.Color.RED, 1));
                    System.out.println("Error: Invalid input");
                }
            }
            
            // Update the table in view
            model.getClassRecord().fireTableRowsUpdated(row, row);
        });
        
        // Custom DocumentListener for numeric input
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getDocument().addDocumentListener(new NumericDocumentListener());
        
        table.getSelectionModel().addListSelectionListener(event -> {
            // Check if the event is adjusting (to avoid double calls during selection changes)
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
        
                if (selectedRow != -1) {  // Ensure a valid row is selected
                    model.setSelectedRow(selectedRow);
                    view.updateEditPanel(
                        selectedRow, 
                        model.getSelectedActivity(), 
                        model.getClassRecord().getRowAt(selectedRow)
                    );
                }
            }
        });
        
        // Add to Table button clicked event
        JButton addButton = (JButton) view.getComponent(Actions.ADDTOTABLE.name());
        addButton.addActionListener((ActionEvent e) -> {
            // Show context menu right below the button, centered
            view.showContextMenu();
        });
        
        // Remove from Table button clicked event
        ( (JButton) view.getComponent(Actions.REMOVEFROMTABLE.name()) ).addActionListener((ActionEvent ev) -> {
            int response = JOptionPane.showConfirmDialog(
                null,
                "This action cannot be undone, are you sure?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            
            if (response == JOptionPane.YES_OPTION) {
                System.out.println("User chose Yes.");
                // HANDLE EVENT HERE
            } else if (response == JOptionPane.NO_OPTION) {
                System.out.println("User chose No.");
                // HANDLE EVENT HERE
            } else {
                System.out.println("User closed the dialog without making a choice.");
                // HANDLE EVENT HERE
            }
        });
        
        // Add ActionListener to menu items to detect clicks
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddActivityWindow activityWindow = new AddActivityWindow();
                AddStudentWindow studentWindow = new AddStudentWindow();
                JMenuItem source = (JMenuItem) e.getSource();
                
                if (source.getText().equals("Student")) {
                    studentWindow.setVisible(true);
                    
                    studentWindow.getButton().addActionListener(new AddStudentWindowListener(studentWindow));
                } else if (source.getText().equals("Activity")) {
                    /**
                     * TODO: REFACTOR MO TO, WAG BOBO
                     * 
                     * 1. Create addActivityWindow that receives input (Activity type, Max grade)
                     * 2. Create new column based on activity type
                     * 3. Create new activity based on max_grade (and insert to db)
                     * 4. Fill empty cells (Done)
                     * 
                     */
                    
                    activityWindow.setVisible(true);
                    activityWindow.getButton().addActionListener(new AddActivityWindowListener(activityWindow));
                }
            }
        };
        
        // ActionListener for "Add New Class Record" Window
        ActionListener addClassRecordWindowListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        };
        
        // Add Student event
        ( (JMenuItem) view.getComponent(Actions.ADDSTUDENT.name()) ).addActionListener(menuListener);
        // Add Activity event
        ( (JMenuItem) view.getComponent(Actions.ADDACTIVITY.name()) ).addActionListener(menuListener);
        
        // Action to move selected row up
        ( (JButton) view.getComponent( Actions.MOVEROWUP.name() ) ).addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow > 0) {
                model.getClassRecord().moveRow(selectedRow, selectedRow - 1);
                table.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
            }
        });

        // Action to move selected row down
        ( (JButton) view.getComponent( Actions.MOVEROWDOWN.name() ) ).addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < model.getClassRecord().getRowCount() - 1 && selectedRow >= 0) {
                model.getClassRecord().moveRow(selectedRow, selectedRow + 1);
                table.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
            }
        });
        
    }
    
    class NumericDocumentListener implements DocumentListener {
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
    
    // Listener for the confirmation button in AddStudentWindow
    class AddStudentWindowListener implements ActionListener {

        AddStudentWindow window;
        boolean validForm;

        public AddStudentWindowListener(AddStudentWindow window) {
            this.window = window;
            validForm = false;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = window.getFirstName();
            String lastName = window.getLastName();
            validForm = true;

            if (firstName.isBlank() || lastName.isBlank()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please provide the first and last name.",
                    "Invalid form",
                    JOptionPane.WARNING_MESSAGE
                );
                validForm = false;
            }

            if (validForm) {
                model.addStudentToClassRecord(firstName, lastName);
                window.dispose();
            }
        }
        
        public boolean isFormValid() {
            return validForm;
        }
    }
    
    // Listener for the confirmation button in AddStudentWindow
    class AddActivityWindowListener implements ActionListener {

        AddActivityWindow window;

        public AddActivityWindowListener(AddActivityWindow window) {
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String activityType = window.getActivityType();
            String totalScore = window.getTotalScore();
            boolean validForm = true;

            if (totalScore.isBlank()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please provide the total score.",
                    "Invalid form",
                    JOptionPane.WARNING_MESSAGE
                );
                validForm = false;
            }

            if (validForm) {
                switch(activityType) {
                    case "Seatwork":
                        model.addSeatworkToTable();
                        break;
                    case "Homework":
                        model.addHWToTable();
                        break;
                    case "Performance Task":
                        model.addPTToTable();
                        break;
                    case "Quiz":
                        model.addQuizToTable();
                        break;
                    case "Exam":
                        model.addExamToTable();
                        break;
                }
                window.dispose();
            }
        }
    }
    
    // Listener for the confirmation button in AddStudentWindow
    class AddClassRecordWindowListener implements ActionListener {

        AddClassRecordWindow window;

        public AddClassRecordWindowListener(AddClassRecordWindow window) {
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String section = window.getSection();
            String subject = window.getSubject();
            String term = window.getTerm();
            String schoolYear = window.getSY();
            boolean validForm = true;

            if (section.isBlank() || subject.isBlank() || schoolYear.isBlank()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please provide the class section and subject.",
                    "Invalid form",
                    JOptionPane.WARNING_MESSAGE
                );
                validForm = false;
            }

            switch (term) {
                case "1st Quarter":
                    break;
                case "2nd Quarter":
                    break;
                case "3rd Quarter":
                    break;
                case "4th Quarter":
                    break;
            }

            if (validForm) {
                // TODO: ADD CODE 
            }
        }
    }
}

class HeaderSelector extends MouseAdapter
{
    JTable table;
  
    public HeaderSelector(JTable t)
    {
        table = t;
    }
  
    public void mousePressed(MouseEvent e)
    {
        JTableHeader th = (JTableHeader)e.getSource();
        Point p = e.getPoint();
        int col = getColumn(th, p);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(true);
        table.setColumnSelectionInterval(col, col);
    }
  
    private int getColumn(JTableHeader th, Point p)
    {
        TableColumnModel model = th.getColumnModel();
        for(int col = 0; col < model.getColumnCount(); col++)
            if(th.getHeaderRect(col).contains(p))
                return col;
        return -1;
    }
}

class RowSelector extends MouseAdapter
{
    JTable table;
  
    public RowSelector(JTable t)
    {
        table = t;
    }
  
    public void mousePressed(MouseEvent e)
    {
        JTable tb = (JTable)e.getSource();
        int row = tb.rowAtPoint(e.getPoint());
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(true);
        
        // Only select if a valid row was clicked
        if (row != -1) {
            table.setRowSelectionInterval(row, row);
        }
    }
}

class HeaderEditor
{
    HeaderSelector selector;
    String[] items;
  
    public HeaderEditor(HeaderSelector hs)
    {
        items = new String[5];
        for(int j = 0; j < items.length; j++)
        {
            items[j] = "item " + j;
        }
    }
  
    public void showEditor(Component parent, int col, String currentValue)
    {
        System.out.println("header selected");
    }
}
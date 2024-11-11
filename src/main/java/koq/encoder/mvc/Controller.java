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
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import koq.encoder.components.AddActivityWindow;
import koq.encoder.mvc.Model.Actions;
import koq.encoder.mvc.Model.Fields;
import koq.encoder.components.NumericDocumentListener;

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
        view.resizeTableHeaders();
        view.updateEditPanel(model.getSelectedRow(), model.getSelectedActivity(), model.getClassRecord().getRowAt(model.getSelectedRow()));
        
        // Set the row selection after all setup is complete
        SwingUtilities.invokeLater(() -> table.setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow()));
        table.requestFocus(); // Ensure the table has focus to highlight the selection
        
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
            int row = model.getSelectedRow();
            if (row > 0) {
                model.setSelectedRow(row-1);
                table.setRowSelectionInterval(row-1, row-1);
            }
        });
        
        // Select next student event
        ( (JButton) view.getComponent(Actions.NEXTSTUDENT.name()) ).addActionListener((ActionEvent ev) -> {
            int row = model.getSelectedRow();
            if (row < model.getClassRecord().getClassList().size()-1) {
                model.setSelectedRow(row+1);
                table.setRowSelectionInterval(row+1, row+1);
            }
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
        
        // Add document listener so this field can only receive numeric and floating-point values
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
            int response = -1;
            boolean deleteStudent = false;
            
            // Check if only row selection is allowed
            if (table.getRowSelectionAllowed() && !table.getColumnSelectionAllowed()) {
                response = JOptionPane.showConfirmDialog(
                    null,
                    model.getClassRecord().getClassList().get(table.getSelectedRow()).getStudent().getStudentName() + 
                        " will be deleted from this class record and all their recorded grades.\n" + 
                        "This action cannot be undone, confirm?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                deleteStudent = true;
            }

            // Check if only column selection is allowed
            if (!table.getRowSelectionAllowed() && table.getColumnSelectionAllowed()) {
                if (table.getSelectedColumn() > 1) {
                    response = JOptionPane.showConfirmDialog(
                        null,
                        model.getClassRecord().getColumnName(table.getSelectedColumn()) + 
                            " will be deleted from this class record and all recorded grades in this activity.\n" + 
                            "This action cannot be undone, confirm?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    deleteStudent = false;
                }
            }
            
            // Delete student
            if (response == JOptionPane.YES_OPTION && deleteStudent) {
                try {
                    System.out.println("Deleting student...");
                    Row row = model.getClassRecord().getClassList().get(table.getSelectedRow());
                    model.removeStudentFromClass(row.getStudent().getStudentId(), model.getClassRecord().getClassId());
                    for (Grade g: row.getGrades()) {
                        model.deleteGradeInDB(g);
                    }
                    model.getClassRecord().getClassList().remove(row);
                    model.getClassRecord().fireTableRowsDeleted(table.getSelectedRow(), table.getSelectedRow());
                } catch (SQLException e) {}
                
            } 
            // Delete activity
            else if (response == JOptionPane.YES_OPTION && !deleteStudent) {
                try {
                    int activityId = model.getClassRecord().getClassList().get(0).getGrades().get(table.getSelectedColumn()-2).getActivityId();
                    
                    // List to hold the grades to delete
                    ArrayList<Grade> gradesToRemove = new ArrayList<>();
                    
                    for (Row r: model.getClassRecord().getClassList())  {
                        Grade g = r.getGrades().get(table.getSelectedColumn()-2);
                        model.deleteGradeInDB(g);
                        gradesToRemove.add(g);      // Collect grade for removal after iteration
                    }
                    
                    // Now remove the grades from each row after collecting them
                    for (Row r : model.getClassRecord().getClassList()) {
                        r.getGrades().removeAll(gradesToRemove); // Safely remove all grades at once
                    }
                    model.deleteActivity(activityId);
                    
                    // Remove column from table
                    model.getClassRecord().deleteColumn(table.getSelectedColumn());
                    
                } catch (SQLException e) {}
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
                    activityWindow.setVisible(true);
                    activityWindow.getButton().addActionListener(new AddActivityWindowListener(activityWindow));
                }
            }
        };
        
        // ActionListener for "Add New Class Record" Window
        ActionListener addClassRecordWindowListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: ADD CODE
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
            int activityTypeId;
            boolean validForm = true;

            // Empty field will show an error
            if (totalScore.isBlank()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please provide the total score.",
                    "Invalid form",
                    JOptionPane.WARNING_MESSAGE
                );
                validForm = false;
            } 
            // Input is negative and will feedback an error
            else if (Double.parseDouble(totalScore) <= 0) {
                JOptionPane.showMessageDialog(
                    null,
                    "Total score must be more than 0.",
                    "Invalid form",
                    JOptionPane.WARNING_MESSAGE
                );
                validForm = false;
            } 
            else {
                // Input accepted
                try {
                    validForm = true;
                } 
                // An input of only '.' will result in an exception, relay an error if this happens
                catch (java.lang.NumberFormatException exception) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Invalid input.",
                        "Invalid form",
                        JOptionPane.WARNING_MESSAGE
                    );
                    validForm = false;
                }
            }
            
            if (validForm) {
                switch(activityType) {
                    case "Seatwork":
                        model.addSeatworkToTable(Double.parseDouble(totalScore));
                        activityTypeId = 1;
                        break;
                    case "Homework":
                        model.addHWToTable(Double.parseDouble(totalScore));
                        activityTypeId = 2;
                        break;
                    case "Performance Task":
                        model.addPTToTable(Double.parseDouble(totalScore));
                        activityTypeId = 3;
                        break;
                    case "Quiz":
                        model.addQuizToTable(Double.parseDouble(totalScore));
                        activityTypeId = 4;
                        break;
                    default:
                        model.addExamToTable(Double.parseDouble(totalScore));
                        activityTypeId = 5;
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
            String gradeLevel = window.getGradeLevel();
            String section = window.getSection();
            String subject = window.getSubject();
            String term = window.getTerm();
            String schoolYear = window.getSY();
            boolean validForm = true;

            if (section.isBlank() || schoolYear.isBlank()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please provide the class section and school year.",
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
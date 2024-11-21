package koq.encoder.mvc;

import koq.encoder.classes.Grade;
import koq.encoder.components.AddClassRecordWindow;
import koq.encoder.components.AddStudentWindow;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import koq.encoder.classes.Row;
import koq.encoder.components.AddActivityWindow;
import koq.encoder.components.LoginWindow;
import koq.encoder.mvc.Model.Actions;
import koq.encoder.mvc.Model.Fields;
import koq.encoder.components.NumericDocumentListener;
import koq.encoder.components.SetGradeWeightsWindow;

public class Controller {
    
    private final View view;
    private final Model model;
            
    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        
        JTable table = view.getTable(model.getSelectedTab());
        
        // New Table event
        ( (JMenuItem) view.getComponent(Actions.NEW_RECORD.name()) ).addActionListener((ActionEvent ev) -> {
            AddClassRecordWindow window = new AddClassRecordWindow();
            window.setVisible(true);
            window.getButton().addActionListener(new AddClassRecordWindowListener(window));
        });
        
        // Login button event handler
        view.getLoginWindow().getButton().addActionListener((ActionEvent ev) -> {
            LoginWindow window = view.getLoginWindow();
            String id = window.getId();
            char[] password = window.getPassword();
            
            // Check for matching credentials
            if (!(model.checkForLogin(id, password))) {
                JOptionPane.showMessageDialog(
                    null,
                    "Incorrect ID and password",
                    "Invalid form",
                    JOptionPane.WARNING_MESSAGE
                );
            } else {
                view.initEditWindow();
                window.dispose();
            }
        });
        
        // Logout event
        ( (JMenuItem) view.getComponent(Actions.LOGOUT.name()) ).addActionListener((ActionEvent ev) -> {
            model.setCurrentUser(null);
            view.setLoginWindow();
        });
        
        // Open File event
        ( (JMenuItem) view.getComponent(Actions.OPEN_RECORD.name()) ).addActionListener((ActionEvent ev) -> {
            model.setClassRecord(model.getClassRecordInDB(1));
            
            /* OUTDATED
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                model.setClassRecord(model.deserializeClassRecord(fileChooser.getSelectedFile()));
                model.getGradePeriod().initClassList();                                             // Initialize arrayList
                model.initClassRecord(model.getGradePeriod());                                      // Populate arrayList
                model.initTable(view.getTable(model.getSelectedTab()), model.getGradePeriod());     // Set table model
                view.resizeTable(model.getSelectedTab());                                           // Setup table
                view.setWindowTitle(model.getGradePeriod().toString());
                System.out.println("Class Record set to " + fileChooser.getSelectedFile().getName());
            }
            */
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
        
        // View keyboard shortcuts event
        ( (JMenuItem) view.getComponent(Actions.VIEWSHORTCUTS.name()) ).addActionListener((ActionEvent ev) -> {
            // TODO: Add code
        });
        
        // Select previous student event
        ( (JButton) view.getComponent(Actions.PREVIOUS_STUDENT.name()) ).addActionListener((ActionEvent ev) -> {
            /* OUTDATED
            int row = model.getSelectedRow();
            if (model.getGradePeriod() != null) {
                if (row > 0) {
                    model.setSelectedRow(row-1);
                    table.setRowSelectionInterval(row-1, row-1);
                }
            }
            */
        });
        
        // Select next student event
        ( (JButton) view.getComponent(Actions.NEXT_STUDENT.name()) ).addActionListener((ActionEvent ev) -> {
            /* OUTDATED
            int row = model.getSelectedRow();
            if (model.getGradePeriod() != null) {
                if (row < model.getGradePeriod().getClassList().size()-1) {
                    model.setSelectedRow(row+1);
                    table.setRowSelectionInterval(row+1, row+1);
                }
            }
            */
        });
        
        // Tab selection listeners
        view.getTabbedPane().addChangeListener(e -> {
            model.setSelectedTab(view.getTabbedPane().getSelectedIndex());
        });
        
        // Activity selection listener
        ( (JComboBox) view.getComponent(Fields.SELECT_ACTIVITY.name()) ).addActionListener((ActionEvent ev) -> {
            model.setSelectedActivity(
                ((JComboBox) view.getComponent(Fields.SELECT_ACTIVITY.name())).getSelectedIndex()
            );
            //view.updateEditPanel(model.getSelectedTab(), model.getSelectedRow(), model.getSelectedActivity(), model.getGradePeriod().getRowAt(model.getSelectedRow()));
        });
        
        // Grade text field DocumentListener
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).addActionListener((ActionEvent ev) -> {
            int row = model.getSelectedRow();
            int col = model.getSelectedActivity();
            String value = ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getText();
           
            if (model.getClassRecord() != null) {
                Grade grade = model.getGradePeriod(model.getSelectedTab()).getRowAt(row).getGrades().get(col);
                
                // Empty field will set grade value to null
                if (value.isBlank()) {
                    grade.setGrade(null);
                } 
                // An input of only '.' will result in an exception, relay an error if this happens
                else if (value.equals(".")) {
                    // Set border color to Red
                    ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).setBorder(new LineBorder(java.awt.Color.RED, 1));
                    System.out.println("Error: Invalid input");
                }
                // Input exceeds range and will feedback an error
                else if (Double.parseDouble(value) > grade.getMaxGrade() || Double.parseDouble(value) < 0) {
                    // Set border color to Red
                    ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).setBorder(new LineBorder(java.awt.Color.RED, 1));
                    System.out.println("Error: Input must be between 0 and " + grade.getMaxGrade());
                } 
                // Input accepted
                else {
                    // Clear border color
                    ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).setBorder(null);
                    grade.setGrade(Double.parseDouble(value));
                    model.updateGrade(grade.getGradeId(), Double.parseDouble(value));
                }

                // Update the table in view
                model.getGradePeriod(model.getSelectedTab()).fireTableRowsUpdated(row, row);       
            }
        });
        
        // Add document listener so this field can only receive numeric and floating-point values
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getDocument().addDocumentListener(new NumericDocumentListener());
        
        table.getSelectionModel().addListSelectionListener(event -> {
            // Check if the event is adjusting (to avoid double calls during selection changes)
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
        
                if (selectedRow != -1) {  // Ensure a valid row is selected
                    model.setSelectedRow(selectedRow);
                    /*  OUTDATED
                    view.updateEditPanel(
                        model.getSelectedTab(),
                        selectedRow, 
                        model.getSelectedActivity(), 
                        model.getGradePeriod().getRowAt(selectedRow)
                    );
                    */
                }
            }
        });
        
        // Add to Table button clicked event
        ( (JButton) view.getComponent(Actions.ADD_TO_TABLE.name()) ).addActionListener((ActionEvent e) -> {
            /*  OUTDATED
            if (model.getGradePeriod() != null) {
                // Show context menu right below the button, centered
                view.showContextMenu();
            }
            */
        });
        
        // Remove from Table button clicked event
        ( (JButton) view.getComponent(Actions.REMOVE_FROM_TABLE.name()) ).addActionListener((ActionEvent ev) -> {
            int response = -1;
            boolean deleteStudent = false;
            
            /* OUTDATED UPDATE THIS
            // Check if only row selection is allowed
            if (table.getRowSelectionAllowed() && !table.getColumnSelectionAllowed()) {
                response = JOptionPane.showConfirmDialog(
                    null,
                    model.getGradePeriod().getClassList().get(table.getSelectedRow()).getStudent().getStudentName() + 
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
                        model.getGradePeriod().getColumnName(table.getSelectedColumn()) + 
                            " will be deleted from this class record and all recorded grades in this activity.\n" + 
                            "This action cannot be undone, confirm?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    deleteStudent = false;
                }
            }
            */
            
            // Delete student
            
            /*
            if (response == JOptionPane.YES_OPTION && deleteStudent) {
                try {
                    System.out.println("Deleting student...");
                    Row row = model.getGradePeriod().getClassList().get(table.getSelectedRow());
                    model.removeStudentFromClass(row.getStudent().getStudentId(), model.getGradePeriod().getClassId());
                    for (Grade g: row.getGrades()) {
                        model.deleteGradeInDB(g);
                    }
                    model.getGradePeriod().getClassList().remove(row);
                    model.getGradePeriod().fireTableRowsDeleted(table.getSelectedRow(), table.getSelectedRow());
                } catch (SQLException e) {}
            }
            */
            
            // Delete activity
            
            /* OUTDATED UPDATE THIS
            else if (response == JOptionPane.YES_OPTION && !deleteStudent) {
                try {
                    int activityId = model.getActivityIdInDB(
                        model.getGradePeriod().getClassId(), 
                        model.getGradePeriod().getColumnName(table.getSelectedColumn())
                    );
                    
                    // List to hold the grades to delete
                    ArrayList<Grade> gradesToRemove = new ArrayList<>();
                    
                    for (Row r: model.getGradePeriod().getClassList())  {
                        Grade g = r.getGrades().get(table.getSelectedColumn()-2);
                        model.deleteGradeInDB(g);
                        gradesToRemove.add(g);      // Collect grade for removal after iteration
                    }
                    
                    // Now remove the grades from each row after collecting them
                    for (Row r : model.getGradePeriod().getClassList()) {
                        r.getGrades().removeAll(gradesToRemove); // Safely remove all grades at once
                    }
                    model.deleteActivity(activityId);
                    
                    // Remove column from table
                    model.getGradePeriod().deleteColumn(table.getSelectedColumn());
                    
                    view.resizeTable(model.getSelectedTab());
                } catch (SQLException e) {}
            }
            */
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
                view.resizeTable(model.getSelectedTab());
            }
        };
        
        // Add Student event
        ( (JMenuItem) view.getComponent(Actions.ADDSTUDENT.name()) ).addActionListener(menuListener);
        // Add Activity event
        ( (JMenuItem) view.getComponent(Actions.ADDACTIVITY.name()) ).addActionListener(menuListener);
        
        ( (JButton) view.getComponent(Actions.EDIT_GRADE_WEIGHTS.name()) ).addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                /*  OUTDATED
                if (model.getGradePeriod() != null) {
                    SetGradeWeightsWindow gradeWeightsWindow = new SetGradeWeightsWindow();

                    gradeWeightsWindow.setVisible(true);
                    gradeWeightsWindow.getButton().addActionListener(new SetGradeWeightsWindowListener(gradeWeightsWindow));
                }
                */
            }
        });
        
        // Action to move selected row up (UNUSED COMPONENT)
        /*
        ( (JButton) view.getComponent( Actions.MOVE_ROW_UP.name() ) ).addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow > 0) {
                model.getGradePeriod().moveRow(selectedRow, selectedRow - 1);
                table.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
            }
        });
        */

        // Action to move selected row down (UNUSED COMPONENT)
        /*
        ( (JButton) view.getComponent( Actions.MOVE_ROW_DOWN.name() ) ).addActionListener((ActionEvent e) -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < model.getGradePeriod().getRowCount() - 1 && selectedRow >= 0) {
                model.getGradePeriod().moveRow(selectedRow, selectedRow + 1);
                table.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
            }
        });
        */
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
                //model.addStudentToClassRecord(firstName, lastName);
                view.resizeTable(model.getSelectedTab());
                window.dispose();
            }
        }
        
        public boolean isFormValid() {
            return validForm;
        }
    }
    
    // Listener for the confirmation button in AddActivitytWindow
    class AddActivityWindowListener implements ActionListener {

        AddActivityWindow window;

        public AddActivityWindowListener(AddActivityWindow window) {
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String activityName = window.getActivityName();
            String activityType = window.getActivityType();
            String totalScore = window.getTotalScore();
            int activityTypeId;
            boolean validForm = true;

            // Empty field will show an error
            if (activityName.isBlank() || totalScore.isBlank()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please provide the activity name and total score.",
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
                    case "Written Work":
                        //model.addWWToTable(Double.parseDouble(totalScore));
                        activityTypeId = 1;
                        break;
                    case "Performance Task":
                        //model.addPTToTable(Double.parseDouble(totalScore));
                        activityTypeId = 2;
                        break;
                    case "Quarterly Assessment":
                        //model.addQAToTable(Double.parseDouble(totalScore));
                        activityTypeId = 3;
                }
                view.resizeTable(model.getSelectedTab());
                window.dispose();
            }
        }
    }
    
    // Listener for the confirmation button in SetGradeWeightsWindow
    class SetGradeWeightsWindowListener implements ActionListener {

        SetGradeWeightsWindow window;

        public SetGradeWeightsWindowListener(SetGradeWeightsWindow window) {
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: Set variables
            boolean validForm = true;

            // TODO: CODE LOGIC

            if (validForm) {
                // TODO: ADD CODE 
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
            int gradeLevel = Integer.parseInt(window.getGradeLevel());
            String section = window.getSection();
            String subject = window.getSubject();
            int term = window.getTerm();
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

            /**
             * OUTDATED. UPDATE THIS
             */
            
            /*
            if (validForm) {
                if (!model.classRecordExists(gradeLevel, section, subject, term, schoolYear)) {
                    model.addClassRecordInDB(gradeLevel, section, subject, term, schoolYear);
                    int classId = model.getClassIdInDB(gradeLevel, section, subject, term, schoolYear);

                    try {
                        model.setClassRecord(model.getClassRecordInDB(classId));
                    } catch (SQLException err) { err.printStackTrace(); }

                    model.serializeClassRecord(model.getGradePeriod());         // Serialize class record

                    model.getGradePeriod().initClassList();                     // Initialize arrayList
                    model.initClassRecord(model.getGradePeriod());              // Populate arrayList
                    model.initTable(
                        view.getTable(model.getSelectedTab()), model.getGradePeriod());   // Set table model
                    view.resizeTable(model.getSelectedTab());                                         // Setup table
                    view.setWindowTitle(model.getGradePeriod().toString());     // Set window title
                    System.out.println("Class Record set");
                    window.dispose();
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Class record already exists.",
                        "Invalid form",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            }
            */
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
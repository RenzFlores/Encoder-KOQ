package koq.encoder.mvc;

import javax.swing.*;
import java.awt.event.*;
import koq.encoder.components.*;
import koq.encoder.classes.*;
import java.awt.Point;
import java.sql.SQLException;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.text.AbstractDocument;
import koq.encoder.mvc.Model.Actions;
import koq.encoder.mvc.Model.Fields;

public class Controller {
    
    private final View view;
    private final Model model;
            
    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        
        JTable tableQ1 = view.getTable(0);
        JTable tableQ2 = view.getTable(1);
        
        // Set ListSelectionListeners for Grade Sheet Q1, Grade Sheet Q2, and Final Grade table
        setListSelectionListener(2);
        setListSelectionListener(3);
        setListSelectionListener(4);
        
        /**
         * Window listener for exit event
         */
        view.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                model.closeConnection();
                view.getFrame().dispose();
                System.exit(0);
            }
        });
        
        // Login button in menu window for both student and faculty
        view.getMenuWindow().getLoginButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.getMenuWindow().getRadioButtonSelection().equals("Teacher")) {
                    LoginFacultyWindow facultyWindow = new LoginFacultyWindow();
                    
                    facultyWindow.setVisible(true);
                    
                    facultyWindow.getButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String id = facultyWindow.getId();
                            char[] password = facultyWindow.getPassword();

                            // Check for matching credentials
                            if (!(model.checkForFacultyLogin(id, password))) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Incorrect ID and password",
                                    "Invalid form",
                                    JOptionPane.WARNING_MESSAGE
                                );
                            } else {
                                // Login successful. Start the main window and remove the login window
                                ((MenuBar) view.getMenuBar()).setFacultyMode();
                                view.initFacultyWindow();
                                model.setCurrentUser(model.getFaculty(Integer.parseInt(id)));
                                facultyWindow.dispose();
                                view.getMenuWindow().dispose();
                                view.getFrame().setTitle("");
                            }
                        }
                    });
                } else {
                    LoginStudentWindow studentWindow = new LoginStudentWindow();
                    
                    studentWindow.setVisible(true);
                    
                    studentWindow.getButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String email = studentWindow.getEmail();
                            char[] password = studentWindow.getPassword();

                            // Check for matching credentials
                            Student student = model.checkForStudentLogin(email, password);
                            if (student == null) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Incorrect email and password",
                                    "Invalid form",
                                    JOptionPane.WARNING_MESSAGE
                                );
                            } else {
                                // Login successful. Start the main window and remove the login window
                                ((MenuBar) view.getMenuBar()).setStudentMode();
                                view.initStudentWindow();
                                view.getStudentWindow().initWindow(student);
                                model.initStudentWindowTable(view.getStudentWindow().getTable(), student.getStudentId());
                                model.setCurrentUser(student);
                                studentWindow.dispose();
                                view.getMenuWindow().dispose();
                                view.getFrame().setTitle("");
                            }
                        }
                    });
                }
            }
        });
        
        // Register button in menu window fpr both student and faculty
        view.getMenuWindow().getRegisterButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (view.getMenuWindow().getRadioButtonSelection().equals("Teacher")) {
                    RegisterFacultyWindow facultyWindow = new RegisterFacultyWindow();
                    
                    facultyWindow.setVisible(true);
                    
                    facultyWindow.getButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String name = facultyWindow.getFacultyName();
                            String id = facultyWindow.getFacultyId();
                            String role = facultyWindow.getRole();
                            char[] password = facultyWindow.getPassword();

                            if (name.isBlank() || id.isBlank() || role.isBlank() || password.length == 0) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Please provide all the necessary details.",
                                    "Invalid form",
                                    JOptionPane.WARNING_MESSAGE
                                );
                            } else {
                                model.addFacultyToDb(name, Integer.parseInt(id), role, password);
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Registered successfully. You may now login",
                                    "Success",
                                    JOptionPane.DEFAULT_OPTION
                                );
                                facultyWindow.dispose();
                                view.setMenuWindow();
                            }
                        }
                    });
                } else {
                    RegisterStudentWindow studentWindow = new RegisterStudentWindow();
                    
                    studentWindow.setVisible(true);
                    
                    studentWindow.getButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String lrn = studentWindow.getLrn();
                            String email = studentWindow.getEmail();
                            char[] password = studentWindow.getPassword();

                            if (lrn.isBlank() || email.isBlank() || password.length == 0) {
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Please provide all the necessary details.",
                                    "Invalid form",
                                    JOptionPane.WARNING_MESSAGE
                                );
                            } else {
                                try {
                                    model.registerStudentToDb(Long.parseLong(lrn), email, password);
                                    model.getAllStudents();
                                } catch (NullPointerException err) {
                                    JOptionPane.showMessageDialog(
                                        null,
                                        "You are still not registered in the system. Please contact your teacher.",
                                        "Invalid form",
                                        JOptionPane.WARNING_MESSAGE
                                    );
                                    return;
                                }
                                JOptionPane.showMessageDialog(
                                    null,
                                    "Registered successfully. You may now login",
                                    "Success",
                                    JOptionPane.DEFAULT_OPTION
                                );
                                studentWindow.dispose();
                                view.setMenuWindow();
                            }
                        }
                    });
                }
            }
        });
        
        // Logout event
        ( (JMenuItem) view.getComponent(Actions.LOGOUT.name()) ).addActionListener((ActionEvent ev) -> {
            model.setCurrentUser(null);
            model.setClassRecord(null);
            view.resetTables();
            view.setMenuWindow();
        });
        
//--------------------------------
//  Menu bar events
//--------------------------------

        /**
         * New class record event
         */
        ( (JMenuItem) view.getComponent(Actions.NEW_RECORD.name()) ).addActionListener((ActionEvent ev) -> {
            AddClassRecordWindow window = new AddClassRecordWindow();
            window.setVisible(true);
            window.getButton().addActionListener(new AddClassRecordWindowListener(window));
        });
        
        // Open class record event
        ( (JMenuItem) view.getComponent(Actions.OPEN_RECORD.name()) ).addActionListener((ActionEvent ev) -> {
            OpenClassRecordWindow window = new OpenClassRecordWindow();
            JTable table = window.getTable();
            
            model.initOpenClassRecordTable(table, ((Faculty) model.getCurrentUser()).getFacultyId());
            
            window.setVisible(true);
            
            window.getButton().addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (table.getSelectedRow() != -1) {
                        model.setClassRecord(model.getClassRecordInDB((int)table.getValueAt(table.getSelectedRow(), 6)));
                        model.initClassRecord(model.getClassRecord());
                        view.getTable(0).setModel(model.getClassRecord().getGradePeriod(1));
                        view.getTable(1).setModel(model.getClassRecord().getGradePeriod(2));
                        model.setTableListeners(view.getTable(0));
                        model.setTableListeners(view.getTable(1));
                        model.initGradeSheetTable(view.getTable(2), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId(), 1);
                        model.initGradeSheetTable(view.getTable(3), model.getClassRecord().getGradePeriod(2).getRows(), model.getClassRecord().getClassId(), 2);
                        model.initFinalGradeTable(view.getTable(4), model.getClassRecord().getGradePeriod(2).getRows(), model.getClassRecord().getClassId());
                        view.enableTabs();
                        if (!model.getClassRecord().getClassList().isEmpty()) {
                            view.getTable(0).setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
                        }
                        view.resizeAllTables();
                        view.setTabNames(model.getClassRecord().getSemester());
                        
                        view.getFrame().setTitle(model.getClassRecord().toString());
                        
                        window.dispose();
                    }
                }  
            });
        });
        
        // Register Student to System event
        ( (JMenuItem) view.getComponent(Actions.REGISTER_STUDENT_SYSTEM.name()) ).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterStudentToSystemWindow window = new RegisterStudentToSystemWindow();
                window.setVisible(true);
                window.getButton().addActionListener(new RegisterStudentToSystemWindowListener(window));
            }
        });
        
        // Exit event from the menu bar
        ( (JMenuItem) view.getComponent(Actions.EXIT.name()) ).addActionListener((ActionEvent ev) -> {
            model.closeConnection();
            view.getFrame().dispose();
            System.exit(0);
        });
        
        // View about event
        ( (JMenuItem) view.getComponent(Actions.VIEW_ABOUT.name()) ).addActionListener((ActionEvent ev) -> {
            view.showAbout();
        });
        
//--------------------------------
//  Edit panel events
//--------------------------------

        // Select previous student keystroke event
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getActionMap().put("previousStudent", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = model.getSelectedRow();
                
                if (model.getClassRecord() == null) {
                    return;
                }
                
                if (row > 0) {
                    model.setSelectedRow(row-1);
                    view.getTable(model.getSelectedTab()).setRowSelectionInterval(row-1, row-1);
                }
            }
        });
        
        // Select next student keystroke event
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getActionMap().put("nextStudent", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = model.getSelectedRow();
                
                if (model.getClassRecord() == null) {
                    return;
                }
                
                if (row < model.getClassRecord().getClassList().size()-1) {
                    model.setSelectedRow(row+1);
                    view.getTable(model.getSelectedTab()).setRowSelectionInterval(row+1, row+1);
                }
            }
        });
        
        // Select previous activity keystroke event
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getActionMap().put("previousActivity", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = model.getSelectedActivity();
                
                if (model.getClassRecord() == null) {
                    return;
                }
                
                if (selected > 0) {
                    model.setSelectedActivity(selected-1);
                    view.getOutputNumberComboBox().setSelectedIndex(selected-1);
                }
            }
        });
        
        // Select next activity keystroke event
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getActionMap().put("nextActivity", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selected = model.getSelectedActivity();
                
                if (model.getClassRecord() == null) {
                    return;
                }
                
                if (selected < view.getOutputNumberComboBox().getItemCount()-1) {
                    model.setSelectedActivity(selected+1);
                    view.getOutputNumberComboBox().setSelectedIndex(selected+1);
                }
            }
        });
        
        // Event for when grade is entered in the grade text field
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).addActionListener((ActionEvent ev) -> {
            int row = model.getSelectedRow();
            int col = model.getSelectedActivity();
            String value = ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getText();
           
            if (model.getClassRecord() != null) {
                Grade grade = model.getGradePeriod(model.getSelectedTab()+1).getRowAt(row).getGrades().get(col);
                
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
                else if (Double.parseDouble(value) > grade.getTotalScore() || Double.parseDouble(value) < 0) {
                    // Set border color to Red
                    ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).setBorder(new LineBorder(java.awt.Color.RED, 1));
                    System.out.println("Error: Input must be between 0 and " + grade.getTotalScore());
                } 
                // Input accepted
                else {
                    // Clear border color
                    ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).setBorder(null);
                    // Update grade in object
                    grade.setGrade(Integer.parseInt(value));
                    // Update grade in db
                    model.updateGrade(grade.getGradeId(), Integer.parseInt(value));
                    // Recalculate percentage grade in db
                    model.updatePercentageGrade(
                        model.calculatePercentageGrade(
                                grade.getStudentId(), 
                                grade.getActivityTypeId(), 
                                grade.getClassId(), 
                                grade.getQuarter()), 
                        grade.getStudentId(), 
                        grade.getActivityTypeId(),
                        grade.getClassId(), 
                        grade.getQuarter()
                    );
                    model.updateInitialGrade( 
                        grade.getStudentId(), 
                        grade.getClassId(), 
                        grade.getQuarter()
                    );
                    // Dirty update by setting the whole selectedTable model
                    if (grade.getQuarter() == 1) {
                        model.initGradeSheetTable(view.getTable(2), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId(), 1);
                    } else {
                        model.initGradeSheetTable(view.getTable(3), model.getClassRecord().getGradePeriod(2).getRows(), model.getClassRecord().getClassId(), 2);
                    }
                    model.initFinalGradeTable(view.getTable(4), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId());
                    view.resizeAllTables();
                }

                // Update the selectedTable in view
                model.getGradePeriod(model.getSelectedTab()+1).fireTableRowsUpdated(row, row);       
            }
        });
        
        // Grade text field document filter to receive numeric input only
        ( (AbstractDocument) 
                ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getDocument()
        ).setDocumentFilter(new NumericDocumentFilter());

//--------------------------------
//  Table events and listeners
//--------------------------------
        
        // Tab selection change listeners
        view.getTabbedPane().addChangeListener(e -> {
            int index = view.getTabbedPane().getSelectedIndex();
            model.setSelectedTab(index);
            
            if (model.getClassRecord().getClassList().isEmpty()) {
                return;
            }
            
            switch (index) {
                case 0:
                    view.getTable(0).setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
                    //System.out.println("Updating edit panel for tab: " + model.getSelectedTab());
                    view.updateEditPanel(
                            model.getSelectedTab(),
                            0,
                            model.getGradePeriod(1).getRowAt(view.getTable(0).getSelectedRow())
                    );  
                    break;
                case 1:
                    view.getTable(1).setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
                    //System.out.println("Updating edit panel for tab: " + model.getSelectedTab());
                    view.updateEditPanel(
                            model.getSelectedTab(),
                            0,
                            model.getGradePeriod(2).getRowAt(view.getTable(1).getSelectedRow())
                    );
                    break;
                default:
                    view.getTable(index).setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
                    break;
            }
            // Set focus to grade text field
            ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).requestFocusInWindow();
        });
        
        // Activity selection listener
        ( (JComboBox) view.getComponent(Fields.SELECT_ACTIVITY.name()) ).addActionListener((ActionEvent ev) -> {
            model.setSelectedActivity(
                ((JComboBox) view.getComponent(Fields.SELECT_ACTIVITY.name())).getSelectedIndex()
            );
            //System.out.println("Updating edit panel for activity: " + model.getSelectedActivity());
            view.updateEditPanel(
                model.getSelectedTab(), 
                model.getSelectedActivity(), 
                model.getGradePeriod(model.getSelectedTab()+1).getRowAt(model.getSelectedRow())
            );
            
            // Set focus to grade text field
            ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).requestFocusInWindow();
        });
        
        // List selection listener for Grade Period Q1 table
        tableQ1.getSelectionModel().addListSelectionListener(event -> {
            // Check if the event is adjusting (to avoid double calls during selection changes)
            if (!event.getValueIsAdjusting()) {
                
                int selectedRow = tableQ1.getSelectedRow();

                if (selectedRow != -1) { // Ensure a valid row is selected
                    model.setSelectedRow(selectedRow);
                    //System.out.println("Updating edit panel for table Q1: " + model.getSelectedRow());
                    
                    view.updateEditPanel(
                        model.getSelectedTab(),
                        model.getSelectedActivity(),
                        model.getGradePeriod(1).getRowAt(selectedRow)
                    );
                }
            }
        });
        
        // List selection listener for Grade Period Q2 table
        tableQ2.getSelectionModel().addListSelectionListener(event -> {
            // Check if the event is adjusting (to avoid double calls during selection changes)
            if (!event.getValueIsAdjusting()) {
                int selectedRow = tableQ2.getSelectedRow();

                if (selectedRow != -1) { // Ensure a valid row is selected
                    model.setSelectedRow(selectedRow);
                    //System.out.println("Updating edit panel for table Q2: " + model.getSelectedRow());
                    view.updateEditPanel(
                        model.getSelectedTab(),
                        model.getSelectedActivity(),
                        model.getGradePeriod(2).getRowAt(selectedRow)
                    );
                }
            }
            // Set focus to grade text field
            ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).requestFocusInWindow();
        });
        
//--------------------------------
//  Toolbar events
//--------------------------------

        // Add to Table button clicked event
        ( (JButton) view.getComponent(Actions.ADD_TO_TABLE.name()) ).addActionListener((ActionEvent e) -> {
            if (model.getClassRecord() != null) {
                // Show context menu right below the button, centered
                view.showContextMenu();
            }
        });
        
        // Remove from Table button clicked event
        ( (JButton) view.getComponent(Actions.REMOVE_FROM_TABLE.name()) ).addActionListener((ActionEvent ev) -> {
            int response = -1;
            boolean deleteStudent = false;
            JTable table = null;
            final JTable selectedTable;
            
            JDialog dialog = view.createPopupDialog("Deleting. Please wait...");
            
            int selectedTab = model.getSelectedTab();
            if (selectedTab <= 1) {
                table = view.getTable(selectedTab);
                selectedTable = table;
            } else {
                return;
            }
            
            // Check if only row selection is allowed
            if (table.getRowSelectionAllowed() && !table.getColumnSelectionAllowed()) {
                response = JOptionPane.showConfirmDialog(
                    null,
                    model.getClassRecord().getClassList().get(table.getSelectedRow()).getStudentFullName() + 
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
                if (table.getSelectedColumn() > 2 && table.getSelectedColumn() < model.getClassRecord().getGradePeriod(selectedTab+1).getColumnCount()-1) {
                    response = JOptionPane.showConfirmDialog(
                        null,
                        model.getClassRecord().getGradePeriod(selectedTab+1).getColumnName(table.getSelectedColumn()) + 
                            " will be deleted from this class record and all recorded grades in this activity.\n" + 
                            "This action cannot be undone, confirm?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                    );
                    deleteStudent = false;
                }
                else if (table.getSelectedColumn() == model.getClassRecord().getGradePeriod(selectedTab+1).getColumnCount()-1) {
                    response = JOptionPane.showConfirmDialog(
                        null,
                        "Quarterly Assessment is fixed and cannot be removed",
                        "Note",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE
                    );
                    return;
                }
            }
            
            // Delete student
            if (response == JOptionPane.YES_OPTION && deleteStudent) {
                SwingUtilities.invokeLater(() -> {
                    dialog.setVisible(true);
                });

                // Create a new thread
                new Thread(() -> {
                    try {
                        /**
                         * Delete all relevant records in database
                         */
                        Row row = model.getGradePeriod(1).getRows().get(selectedTable.getSelectedRow());
                        model.deleteGradeByStudent(row.getStudent().getStudentId(), model.getClassRecord().getClassId());
                        model.removeStudentFromClass(row.getStudent().getStudentId(), model.getClassRecord().getClassId());
                        model.deleteCalculatedGrades(row.getStudent().getStudentId(), model.getClassRecord().getClassId());
                    } catch (SQLException e) { e.printStackTrace(); }

                    SwingUtilities.invokeLater(() -> {
                        /**
                         * Update all tables inside the program
                         */
                        model.getGradePeriod(1).removeRow(model.getSelectedRow());
                        model.getGradePeriod(2).removeRow(model.getSelectedRow());
                        model.getGradePeriod(1).fireTableRowsDeleted(selectedTable.getSelectedRow(), selectedTable.getSelectedRow());
                        model.getGradePeriod(2).fireTableRowsDeleted(selectedTable.getSelectedRow(), selectedTable.getSelectedRow());
                        // Dirty update by setting the whole selectedTable model on grade sheet Q1, Q2, and final grades
                        model.initGradeSheetTable(view.getTable(2), model.getClassRecord().getGradePeriod(1).getRows(),  model.getClassRecord().getClassId(), 1);
                        model.initGradeSheetTable(view.getTable(3), model.getClassRecord().getGradePeriod(2).getRows(),  model.getClassRecord().getClassId(), 2);
                        model.initFinalGradeTable(view.getTable(4), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId());
                        view.resizeAllTables();
                        
                        if (model.getSelectedRow() == model.getClassRecord().getClassList().size()-1) {
                            model.setSelectedRow(model.getClassRecord().getClassList().size()-1);
                        }
                        dialog.dispose();
                    });
                }).start();
            }
            
            // Delete activity
            else if (response == JOptionPane.YES_OPTION && !deleteStudent) {
                SwingUtilities.invokeLater(() -> {
                    dialog.setVisible(true);
                });

                // Create a new thread
                new Thread(() -> {
                    try {
                        GradePeriod period = model.getGradePeriod(selectedTab+1);

                        int activityId = model.getActivityIdInDB(
                            model.getClassRecord().getClassId(), 
                            period.getColumnName(selectedTable.getSelectedColumn()).split("\\|")[1],
                            selectedTab+1
                        );
                        
                        // Delete all grade records in database
                        model.deleteGradeByActivity(activityId);

                        // 
                        for (Row r: period.getRows())  {
                            Grade g = r.getGrades().get(selectedTable.getSelectedColumn()-3);
                            
                            // Recalculate percentage grade in db
                            model.updatePercentageGrade(
                                model.calculatePercentageGrade(
                                        g.getStudentId(),
                                        g.getActivityTypeId(), 
                                        g.getClassId(), 
                                        g.getQuarter()), 
                                g.getStudentId(), 
                                g.getActivityTypeId(),
                                g.getClassId(), 
                                g.getQuarter()
                            );
                            // Recalculate final grade
                            model.updateInitialGrade( 
                                g.getStudentId(), 
                                g.getClassId(), 
                                g.getQuarter()
                            );
                            
                            r.getGrades().remove(g);
                        }

                        // Delete activity in database
                        model.deleteActivity(activityId);
                    } catch (SQLException e) { e.printStackTrace(); }

                    SwingUtilities.invokeLater(() -> {
                        // Remove column from selectedTable
                        GradePeriod period = model.getGradePeriod(selectedTab+1);
                        period.deleteColumn(selectedTable.getSelectedColumn());
                        period.setColumnNames(model.getActivitiesInDB(model.getClassRecord().getClassId(), selectedTab+1));  
                        
                        /**
                         * Update all tables inside the program
                         */
                        view.resizeTable(model.getSelectedTab());
                        // Dirty update by setting the whole selectedTable model on grade sheet Q1, Q2, and final grades
                        model.initGradeSheetTable(view.getTable(2), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId(), 1);
                        model.initGradeSheetTable(view.getTable(3), model.getClassRecord().getGradePeriod(2).getRows(), model.getClassRecord().getClassId(), 2);
                        model.initFinalGradeTable(view.getTable(4), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId());
                        view.resizeAllTables();
                        dialog.dispose();
                    });
                }).start();
            }
        });
        
        // ActionListener to menu items to detect clicks
        ActionListener menuListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JMenuItem source = (JMenuItem) e.getSource();
                
                if (source.getText().equals("Student")) {
                    AddStudentWindow studentWindow = new AddStudentWindow();
                    JTable studentTable = studentWindow.getTable();
                    
                    model.initAddStudentTable(studentTable);
                    studentWindow.initRowSorter();

                    studentWindow.setVisible(true);

                    studentWindow.getButton().addActionListener(new AddStudentWindowListener(studentWindow));
                } else if (source.getText().equals("Activity")) {
                    AddActivityWindow activityWindow = new AddActivityWindow();
                    activityWindow.setVisible(true);
                    activityWindow.getButton().addActionListener(new AddActivityWindowListener(activityWindow));
                }
                view.resizeTable(model.getSelectedTab());
            }
        };
        
        // Add Student to class record event
        ( (JMenuItem) view.getComponent(Actions.ADDSTUDENT.name()) ).addActionListener(menuListener);
        // Add Activity event
        ( (JMenuItem) view.getComponent(Actions.ADDACTIVITY.name()) ).addActionListener(menuListener);
        
        // Edit grade weights event
        ( (JButton) view.getComponent(Actions.EDIT_GRADE_WEIGHTS.name()) ).addActionListener(new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (model.getClassRecord() != null) {
                    SetGradeWeightsWindow setGradeWeightsWindow = new SetGradeWeightsWindow(model.getGradeWeights(model.getClassRecord().getClassId()));

                    setGradeWeightsWindow.setVisible(true);
                    setGradeWeightsWindow.getButton().addActionListener(new SetGradeWeightsWindowListener(setGradeWeightsWindow));
                }
            }
        });
        
        // Generate report card event
        ( (JButton) view.getComponent(Actions.GENERATE_REPORT.name()) ).addActionListener(new ActionListener() {
            
            int index = model.getSelectedRow();
            
            private void setIndex(int i) {
                index = i;
            }
            
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (model.getClassRecord() != null) {
                    ReportCardWindow reportCardWindow = new ReportCardWindow(true);
                    Student student = model.getClassRecord().getClassList().get(model.getSelectedRow());

                    reportCardWindow.setStudentData(student, model.getClassRecord().getSY());
                    model.initReportCardTable(reportCardWindow.getTableSem1(), student.getStudentId(), model.getClassRecord().getSY(), 1);
                    model.initReportCardTable(reportCardWindow.getTableSem2(), student.getStudentId(), model.getClassRecord().getSY(), 2);
                    reportCardWindow.setVisible(true);
                    
                    reportCardWindow.getPreviousButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (index > 0) {
                                Student prevStudent = model.getClassRecord().getClassList().get(index - 1);
                                reportCardWindow.setStudentData(prevStudent, model.getClassRecord().getSY());
                                model.initReportCardTable(reportCardWindow.getTableSem1(), prevStudent.getStudentId(), model.getClassRecord().getSY(), 1);
                                model.initReportCardTable(reportCardWindow.getTableSem2(), prevStudent.getStudentId(), model.getClassRecord().getSY(), 2);
                                setIndex(index-1);
                            }
                        }
                    });

                    reportCardWindow.getNextButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (index < model.getClassRecord().getClassList().size() - 1 && index >= 0) {
                                Student nextStudent = model.getClassRecord().getClassList().get(index + 1);
                                reportCardWindow.setStudentData(nextStudent, model.getClassRecord().getSY());
                                model.initReportCardTable(reportCardWindow.getTableSem1(), nextStudent.getStudentId(), model.getClassRecord().getSY(), 1);
                                model.initReportCardTable(reportCardWindow.getTableSem2(), nextStudent.getStudentId(), model.getClassRecord().getSY(), 2);
                                setIndex(index+1);
                            }
                        }
                    });
                }
            }
        });

        // Preview report card button event in student window
        view.getStudentWindow().getPreviewReportCardButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String schoolYear = view.getStudentWindow().getSYField();
                Student student = ((Student)model.getCurrentUser());
                ReportCardWindow reportCardWindow = new ReportCardWindow(false);

                reportCardWindow.setStudentData(student, schoolYear);
                model.initReportCardTable(reportCardWindow.getTableSem1(), student.getStudentId(), schoolYear, 1
                );
                model.initReportCardTable(reportCardWindow.getTableSem2(), student.getStudentId(), schoolYear, 2);
                reportCardWindow.setVisible(true);
                    
                JOptionPane.showMessageDialog(
                    null,
                    """
                        This report card is a preview only and is not indicative of your final grades.
                        Please contact your teacher if you have any concerns.""",
                    "Please Note",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        
        // View grades button event in student window
        view.getStudentWindow().getViewGradesButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = view.getStudentWindow().getTable().getSelectedRow();
                
                if (selectedRow != -1) {
                    int classId = (int) view.getStudentWindow().getTable().getValueAt(selectedRow, 6);
                    ViewGradesWindow window = new ViewGradesWindow();
                    
                    model.initViewGradesTable(window.getTableQ1(), ((Student)model.getCurrentUser()).getStudentId(), classId, 1);
                    model.initViewGradesTable(window.getTableQ2(), ((Student)model.getCurrentUser()).getStudentId(), classId, 2);
                    
                    window.setVisible(true);
                    
                    JOptionPane.showMessageDialog(
                        null,
                        "Quarter grades may not be final. Please contact your teacher if you have any concerns.",
                        "Please Note",
                        JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Please select class from the table first.",
                        "Note",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });
    }
    
    // Method to handle selection changes for a table
    private void setListSelectionListener(int tableIndex) {
        JTable table = view.getTable(tableIndex);

        table.getSelectionModel().addListSelectionListener(event -> {
            // Check if the event is adjusting to avoid double calls during selection changes
            if (!event.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();

                // Ensure a valid row is selected
                if (selectedRow != -1) { 
                    model.setSelectedRow(selectedRow);
                }
            }

            // Set focus to grade text field
            ((JTextField) view.getComponent(Fields.EDIT_GRADE.name())).requestFocusInWindow();
        });
    }

//--------------------------------
//  External form events
//--------------------------------
    
    // Listener for the submit button in AddStudentWindow
    class AddStudentWindowListener implements ActionListener {

        AddStudentWindow window;

        public AddStudentWindowListener(AddStudentWindow window) {
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = view.createPopupDialog("Creating records. Please wait...");
            
            if (window.getTable().getSelectedRow() != -1) {
                for (Student s: model.getClassRecord().getClassList()) {
                    if ((int)window.getTable().getValueAt(window.getTable().getSelectedRow(), 5) == s.getStudentId()) {
                        JOptionPane.showMessageDialog(
                            null,
                            "Student already in class record.",
                            "Invalid",
                            JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                }
                
                window.dispose();
                
                SwingUtilities.invokeLater(() -> {
                    dialog.setVisible(true);
                });

                // Create a new thread
                new Thread(() -> {
                    // Add student to model
                    int id = (int) window.getTable().getValueAt(window.getTable().getSelectedRow(), 5);
                    int classId = model.getClassRecord().getClassId();

                    SwingUtilities.invokeLater(() -> {
                        // Add empty grade records to db
                        for (Activity a: model.getActivitiesInDB(classId, 1)) {
                            model.addEmptyGradesToDB(new Integer[]{id}, classId, a.getActivityId());
                        }
                        for (Activity a: model.getActivitiesInDB(classId, 2)) {
                            model.addEmptyGradesToDB(new Integer[]{id}, classId, a.getActivityId());
                        }
                        model.addStudentToClass(id, classId);
                        model.addCalculatedGrades(id, classId);
                        
                        model.setClassRecord(model.getClassRecordInDB(model.getClassRecord().getClassId()));
                        model.initClassRecord(model.getClassRecord());
                        view.getTable(0).setModel(model.getClassRecord().getGradePeriod(1));
                        view.getTable(1).setModel(model.getClassRecord().getGradePeriod(2));
                        model.setTableListeners(view.getTable(0));
                        model.setTableListeners(view.getTable(1));
                        // Dirty update by setting the whole table model
                        model.initGradeSheetTable(view.getTable(2), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId(), 1);
                        model.initGradeSheetTable(view.getTable(3), model.getClassRecord().getGradePeriod(2).getRows(), model.getClassRecord().getClassId(), 2);
                        model.initFinalGradeTable(view.getTable(4), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId());
                        view.resizeAllTables();
                        view.getFrame().setTitle(model.getClassRecord().toString());
                        dialog.dispose();
                    });
                }).start();
            }
        }
    }
    
    // Listener for the submit button in AddActivityWindow
    class AddActivityWindowListener implements ActionListener {

        AddActivityWindow window;

        public AddActivityWindowListener(AddActivityWindow window) {
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int quarter = window.getQuarter();
            String activityName = window.getActivityName();
            String activityType = window.getActivityType();
            String totalScore = window.getTotalScore();
            int activityTypeId;
            boolean validForm = true;
            JDialog dialog = view.createPopupDialog("Creating records. Please wait...");

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
            
            // Total score must be more than 0
            if (Integer.parseInt(totalScore) <= 0) {
                JOptionPane.showMessageDialog(
                    null,
                    "Total score must be more than 0.",
                    "Invalid form",
                    JOptionPane.WARNING_MESSAGE
                );
                validForm = false;
            }
            
            if (validForm) {
                window.dispose();
                
                SwingUtilities.invokeLater(() -> {
                    dialog.setVisible(true);
                });

                // Create a new thread
                new Thread(() -> {
                    switch(activityType) {
                    case "Written Work":
                        model.addWWToTable(activityName, Integer.parseInt(totalScore), quarter);
                        break;
                    case "Performance Task":
                        model.addPTToTable(activityName, Integer.parseInt(totalScore), quarter);
                    }

                    // Update all final grades in database since a new activity is added
                    for (Row r: model.getGradePeriod(quarter).getRows()) {
                        // Recalculate percentage grade in db
                        model.updatePercentageGrade(
                            model.calculatePercentageGrade(
                                    r.getStudent().getStudentId(), 
                                    "Written Work".equals(activityType) ? 1 : 2,
                                    model.getClassRecord().getClassId(), 
                                    quarter
                            ), 
                            r.getStudent().getStudentId(),  
                            "Written Work".equals(activityType) ? 1 : 2,
                            model.getClassRecord().getClassId(), 
                            quarter
                        );
                        model.updateInitialGrade( 
                            r.getStudent().getStudentId(),  
                            model.getClassRecord().getClassId(), 
                            quarter
                        );
                    }

                    SwingUtilities.invokeLater(() -> {
                        model.getGradePeriod(quarter).setColumnNames(model.getActivitiesInDB(model.getClassRecord().getClassId(), quarter));
                        // Dirty update by setting the whole table model
                        model.initGradeSheetTable(view.getTable(quarter+1), model.getClassRecord().getGradePeriod(quarter).getRows(), model.getClassRecord().getClassId(), quarter);
                        model.initFinalGradeTable(view.getTable(4), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId());
                        view.resizeTable(quarter-1);
                        //view.getTable(quarter-1).setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
                        dialog.dispose();
                    });
                }).start();
            }
        }
    }
    
    // Listener for the submit button in RegisterStudentToSystemWindow
    class RegisterStudentToSystemWindowListener implements ActionListener {

        RegisterStudentToSystemWindow window;

        public RegisterStudentToSystemWindowListener(RegisterStudentToSystemWindow window) {
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = window.getFirstName();
            String middleName = window.getMiddleName();
            String lastName = window.getLastName();
            String lrn = window.getLrn();
            String dob = window.getDob();
            String gender = window.getGender();
            String strand = window.getStrand();
            int gradeLevel = window.getGradeLevel();
            
            boolean validForm = true;

            // Empty fields will show an error
            if (firstName.isBlank() || lastName.isBlank() || lrn.isBlank()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please provide all the necessary details.",
                    "Invalid form",
                    JOptionPane.WARNING_MESSAGE
                );
                validForm = false;
            }
            
            if (validForm) {
                model.addStudentToDb(firstName, middleName, lastName, Long.parseLong(lrn), gender, dob, strand, gradeLevel);
                
                JOptionPane.showMessageDialog(
                    null,
                    "Student registered successfully",
                    "Success",
                    JOptionPane.DEFAULT_OPTION
                );
                
                model.getAllStudents();
                
                window.dispose();
            }
        }
    }
    
    // Listener for the submit button in SetGradeWeightsWindow
    class SetGradeWeightsWindowListener implements ActionListener {

        SetGradeWeightsWindow window;

        public SetGradeWeightsWindowListener(SetGradeWeightsWindow window) {
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int wwWeight = window.getWwWeightField();
            int ptWeight = window.getPtWeightField();
            int qaWeight = window.getQaWeightField();
            int total = wwWeight + ptWeight + qaWeight;
            boolean validForm = true;

            if (total != 100) {
                JOptionPane.showMessageDialog(
                    null,
                    "Sum of weights must be equal to 100. The current total is " + total,
                    "Invalid form",
                    JOptionPane.WARNING_MESSAGE
                );
                validForm = false;
            }

            if (validForm) {
                model.updateGradeWeights(model.getClassRecord().getClassId(), wwWeight / 100.0, ptWeight / 100.0, qaWeight / 100.0);
                
                /**
                 * Recalculate grades and update all tables inside the program
                 */
                for (Student s: model.getClassRecord().getClassList()) {
                    model.updateInitialGrade( 
                        s.getStudentId(), 
                        model.getClassRecord().getClassId(), 
                        1
                    );
                    model.updateInitialGrade( 
                        s.getStudentId(), 
                        model.getClassRecord().getClassId(), 
                        2
                    );
                }
                // Dirty update by setting the whole selectedTable model on grade sheet Q1, Q2, and final grades
                model.initGradeSheetTable(view.getTable(2), model.getClassRecord().getGradePeriod(1).getRows(),  model.getClassRecord().getClassId(), 1);
                model.initGradeSheetTable(view.getTable(3), model.getClassRecord().getGradePeriod(2).getRows(),  model.getClassRecord().getClassId(), 2);
                model.initFinalGradeTable(view.getTable(4), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId());
                view.resizeAllTables();
                
                JOptionPane.showMessageDialog(
                    null,
                    "Grade weights updated successfully",
                    "Success",
                    JOptionPane.DEFAULT_OPTION
                );
                
                window.dispose();
            }
        }
    }
    
    // Listener for the submit button in AddClassRecordWindow
    class AddClassRecordWindowListener implements ActionListener {

        AddClassRecordWindow window;

        public AddClassRecordWindowListener(AddClassRecordWindow window) {
            this.window = window;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int gradeLevel = Integer.parseInt(window.getGradeLevel());
            String section = window.getSection();
            int subjectId = window.getSubjectId();
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

            if (validForm) {
                Faculty faculty = ((Faculty)model.getCurrentUser());
                
                if (!model.classRecordExists(faculty.getFacultyId(), gradeLevel, section, subjectId, term, schoolYear)) {
                    model.addClassRecordInDB(faculty.getFacultyId(), gradeLevel, section, subjectId, term, schoolYear);
                    int classId = model.getClassIdInDB(faculty.getFacultyId(), gradeLevel, section, subjectId, term, schoolYear);
                    model.addGradeWeightsToDb(classId);

                    model.setClassRecord(model.getClassRecordInDB(classId));
                    model.initClassRecord(model.getClassRecord());
                    
                    model.addQAToTable("Quarterly Exam", 50, 1);
                    model.addQAToTable("Quarterly Exam", 50, 2);
                    
                    view.getTable(0).setModel(model.getClassRecord().getGradePeriod(1));
                    view.getTable(1).setModel(model.getClassRecord().getGradePeriod(2));
                    model.setTableListeners(view.getTable(0));
                    model.setTableListeners(view.getTable(1));
                    model.initGradeSheetTable(view.getTable(2), model.getClassRecord().getGradePeriod(1).getRows(), model.getClassRecord().getClassId(), 1);
                    model.initGradeSheetTable(view.getTable(3), model.getClassRecord().getGradePeriod(2).getRows(), model.getClassRecord().getClassId(), 2);
                    model.initFinalGradeTable(view.getTable(4), model.getClassRecord().getGradePeriod(2).getRows(), model.getClassRecord().getClassId());
                    view.enableTabs();
                    if (!model.getClassRecord().getClassList().isEmpty()) {
                        view.getTable(0).setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
                    }
                    view.resizeAllTables();
                    view.setTabNames(model.getClassRecord().getSemester());

                    view.getFrame().setTitle(model.getClassRecord().toString());

                    window.dispose();
                    
                    //System.out.println("Class Record set");
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
        }
    }
}

/**
 * Custom mouse listener for table, allowing column selection when headers are clicked
 */
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

/**
 * Custom mouse listener for table, defaulting to row selection when cells are clicked
 */
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
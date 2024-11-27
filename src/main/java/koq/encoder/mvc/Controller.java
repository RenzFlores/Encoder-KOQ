package koq.encoder.mvc;

import koq.encoder.components.*;
import koq.encoder.classes.*;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
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
         
        /**
         * New Table event
         */
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
                // Login successful. Start the main window and remove the login window
                view.initEditWindow();
                model.setCurrentUser(model.getFaculty(Integer.parseInt(id)));
                window.dispose();
            }
        });
        
        // Logout event
        ( (JMenuItem) view.getComponent(Actions.LOGOUT.name()) ).addActionListener((ActionEvent ev) -> {
            model.setCurrentUser(null);
            model.setClassRecord(null);
            view.resetTables();
            view.setLoginWindow();
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
                        
                        window.dispose();
                    }
                }  
            });
        });
        
        /** Export File event (UNUSED)
        ( (JMenuItem) view.getComponent(Actions.EXPORTFILE.name()) ).addActionListener((ActionEvent ev) -> {});
        */
        
        // Exit event from the menu bar
        ( (JMenuItem) view.getComponent(Actions.EXIT.name()) ).addActionListener((ActionEvent ev) -> {
            model.closeConnection();
            view.getFrame().dispose();
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
        
        // Tab selection listeners
        view.getTabbedPane().addChangeListener(e -> {
            int index = view.getTabbedPane().getSelectedIndex();
            model.setSelectedTab(index);
            
            if (model.getClassRecord().getClassList().isEmpty()) {
                return;
            }
            
            switch (index) {
                case 0:
                    view.getTable(0).setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
                    view.updateEditPanel(
                            model.getSelectedTab(),
                            0,
                            model.getGradePeriod(1).getRowAt(view.getTable(0).getSelectedRow())
                    );  break;
                case 1:
                    view.getTable(1).setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
                    view.updateEditPanel(
                            model.getSelectedTab(),
                            0,
                            model.getGradePeriod(2).getRowAt(view.getTable(1).getSelectedRow())
                    );  break;
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
            view.updateEditPanel(
                model.getSelectedTab(), 
                model.getSelectedActivity(), 
                model.getGradePeriod(model.getSelectedTab()+1).getRowAt(model.getSelectedRow())
            );
        });
        
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
        
        // Select previous activity keystroke event
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
        
        // Grade text field DocumentListener
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
        
        // Add document filter so this field can only receive numeric values
        ( (AbstractDocument) 
                ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getDocument()
        ).setDocumentFilter(new NumericDocumentFilter());
        
        // List selection listener for Grade Period Q1 table
        tableQ1.getSelectionModel().addListSelectionListener(event -> {
            // Check if the event is adjusting (to avoid double calls during selection changes)
            if (!event.getValueIsAdjusting()) {
                int selectedRow = tableQ1.getSelectedRow();

                if (selectedRow != -1) { // Ensure a valid row is selected
                    model.setSelectedRow(selectedRow);
                    view.updateEditPanel(
                        model.getSelectedTab(),
                        model.getSelectedActivity(),
                        model.getGradePeriod(1).getRowAt(selectedRow)
                    );
                }
            }
            // Set focus to grade text field
            ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).requestFocusInWindow();
        });
        
        // List selection listener for Grade Period Q2 table
        tableQ2.getSelectionModel().addListSelectionListener(event -> {
            // Check if the event is adjusting (to avoid double calls during selection changes)
            if (!event.getValueIsAdjusting()) {
                int selectedRow = tableQ2.getSelectedRow();

                if (selectedRow != -1) { // Ensure a valid row is selected
                    model.setSelectedRow(selectedRow);
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
        
        // List selection listener for Grade Sheet Q1 table
        view.getTable(2).getSelectionModel().addListSelectionListener(event -> {
            // Check if the event is adjusting (to avoid double calls during selection changes)
            if (!event.getValueIsAdjusting()) {
                int selectedRow = view.getTable(2).getSelectedRow();

                if (selectedRow != -1) { // Ensure a valid row is selected
                    model.setSelectedRow(selectedRow);
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
        
        // List selection listener for Grade Sheet Q2 table
        view.getTable(2).getSelectionModel().addListSelectionListener(event -> {
            // Check if the event is adjusting (to avoid double calls during selection changes)
            if (!event.getValueIsAdjusting()) {
                int selectedRow = view.getTable(2).getSelectedRow();

                if (selectedRow != -1) { // Ensure a valid row is selected
                    model.setSelectedRow(selectedRow);
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
        
        // List selection listener for Final Grade table
        view.getTable(5).getSelectionModel().addListSelectionListener(event -> {
            // Check if the event is adjusting (to avoid double calls during selection changes)
            if (!event.getValueIsAdjusting()) {
                int selectedRow = view.getTable(2).getSelectedRow();

                if (selectedRow != -1) { // Ensure a valid row is selected
                    model.setSelectedRow(selectedRow);
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
        
        // Add FocusListener on JTable so the edit text field will always focus whenever the table is clicked
        view.getTable(model.getSelectedTab()).addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // Set focus to grade text field
                ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).requestFocusInWindow();
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });

        
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

        // Add Student to System event
        ( (JMenuItem) view.getComponent(Actions.REGISTER_STUDENT_SYSTEM.name()) ).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterStudentToSystemWindow window = new RegisterStudentToSystemWindow();
                window.setVisible(true);
                window.getButton().addActionListener(new RegisterStudentToSystemWindowListener(window));
            }
        });
        
        // Add Student event
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
                    ReportCardWindow reportCardWindow = new ReportCardWindow(model.getClassRecord().getClassList().get(model.getSelectedRow()));

                    reportCardWindow.setVisible(true);
                    
                    reportCardWindow.getPreviousButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (index > 0) {
                                reportCardWindow.setData(model.getClassRecord().getClassList().get(index - 1));
                                setIndex(index-1);
                            }
                        }
                    });

                    reportCardWindow.getNextButton().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (index < model.getClassRecord().getClassList().size() - 1 && index >= 0) {
                                reportCardWindow.setData(model.getClassRecord().getClassList().get(index + 1));
                                setIndex(index+1);
                            }
                        }
                    });
                }
            }
        });
        
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        };

        // Action to move selected row up (UNUSED COMPONENT)
        /*
        ( (JButton) view.getComponent( Actions.MOVE_ROW_UP.name() ) ).addActionListener((ActionEvent e) -> {
            int selectedRow = selectedTable.getSelectedRow();
            if (selectedRow > 0) {
                model.getGradePeriod().moveRow(selectedRow, selectedRow - 1);
                selectedTable.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
            }
        });
        */

        // Action to move selected row down (UNUSED COMPONENT)
        /*
        ( (JButton) view.getComponent( Actions.MOVE_ROW_DOWN.name() ) ).addActionListener((ActionEvent e) -> {
            int selectedRow = selectedTable.getSelectedRow();
            if (selectedRow < model.getGradePeriod().getRowCount() - 1 && selectedRow >= 0) {
                model.getGradePeriod().moveRow(selectedRow, selectedRow + 1);
                selectedTable.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
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
    
    // Listener for the confirmation button in AddActivityWindow
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
                        view.getTable(quarter-1).setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
                        dialog.dispose();
                    });
                }).start();
            }
        }
    }
    
    // Listener for the confirmation button in RegisterStudentToSystemWindow
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
            if (firstName.isBlank() || lastName.isBlank() || lrn.isBlank() || dob.isBlank()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please provide all the necessary details.",
                    "Invalid form",
                    JOptionPane.WARNING_MESSAGE
                );
                validForm = false;
            }
            
            if (validForm) {
                // TODO: ADD CODE HERE
                
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
                        view.getTable(model.getSelectedTab()), model.getGradePeriod());   // Set selectedTable model
                    view.resizeTable(model.getSelectedTab());                                         // Setup selectedTable
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
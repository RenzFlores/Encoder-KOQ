package koq.encoder.mvc;

import koq.encoder.components.*;
import javax.swing.*;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import koq.encoder.classes.Row;
import koq.encoder.mvc.Model.Actions;
import koq.encoder.mvc.Model.Fields;

public class View {

    private final JFrame frame;

    private final MenuBar menuBar;
    private final FacultyWindow facultyWindow;
    
    private LoginFacultyWindow loginWindow;
    private MenuWindow menuWindow;
    private StudentWindow studentWindow;
    
    private final AddContextMenu addContextMenu;
    
    // ArrayList containing all window components. Used for retrieving components
    // inside Controller
    private final ArrayList<Component> componentList;

    public View() {

        // Set look and feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        frame = new JFrame("Encoder");                           
        frame.setSize(1280, 720);
        frame.setLayout(new CardLayout());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
        
        menuBar = new MenuBar();
        frame.setJMenuBar(menuBar);
        
        facultyWindow = new FacultyWindow();
        studentWindow = new StudentWindow();
        loginWindow = new LoginFacultyWindow();
        menuWindow = new MenuWindow();
        
        addContextMenu = new AddContextMenu();
        addContextMenu.setName("Add Context Menu");
        
        componentList = new ArrayList();
        initializeComponentList();
        
        // Set keystrokes for table editing
        ( (JTextField) getComponent(Fields.EDIT_GRADE.name())).getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("UP"), "previousStudent");
        ( (JTextField) getComponent(Fields.EDIT_GRADE.name())).getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("DOWN"), "nextStudent");
        ( (JTextField) getComponent(Fields.EDIT_GRADE.name())).getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("LEFT"), "previousActivity");
        ( (JTextField) getComponent(Fields.EDIT_GRADE.name())).getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("RIGHT"), "nextActivity");
       
        setMenuWindow();
    }
    
    public void setMenuWindow() {
        frame.remove(studentWindow);
        frame.remove(facultyWindow);
        frame.setVisible(false);
        menuWindow.setVisible(true);
    }
    
    public void initFacultyWindow() {
        frame.setJMenuBar(menuBar);
        frame.remove(studentWindow);
        frame.add(facultyWindow);
        facultyWindow.disableWindow();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public void initStudentWindow() {
        frame.setJMenuBar(menuBar);
        frame.remove(facultyWindow);
        frame.add(studentWindow);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Get all components in componentList. Called upon program start.
     * NOTE: Adding more components in the program may require them to explicitly be added here
     * or else it won't be able to be retrieved by Controller
    */
    private void initializeComponentList() {
        // Grade editor
        componentList.addAll(Arrays.asList(( (EditPanel) facultyWindow.getGradeEditor() ).getContainer().getComponents()));
        
        // Toolbar
        componentList.addAll(Arrays.asList(facultyWindow.getToolbar().getComponents()));
        
        // Table editor
        //componentList.addAll(Arrays.asList(
        //        tableEditor.getContainer().getComponents()));

        // Table view
        // componentList.add(( (TableView) tableEditor).getTab());

        // "Add to table" context menu
        componentList.add(addContextMenu);
        componentList.addAll(Arrays.asList(addContextMenu.getComponents()));
        
        // Add all menus
        for (Component m : frame.getJMenuBar().getComponents()) {
            componentList.add(m);
            // Add all menu items
            for (int j = 0; j < ((JMenu)m).getMenuComponentCount(); j++) {
                componentList.add(((JMenu)m).getMenuComponent(j));
            }
        }
    }
    
    /** 
     * This will return a component by its assigned name
     * @params {String} componentName
     * @return Component with matching name
    */
    public Component getComponent(String componentName) {
        Component component = null;
        
        // Find component from componentList
        for (Component c : componentList) {
            String name = c.getName();
            
            if (name != null) {
                if (name.equals(componentName)) {
                    component = c;
                    break;
                }
            }
        }
        
        // Return component if found. Otherwise throw RuntimeException
        if (component != null) {
            return component;
        }
        else {
            throw new RuntimeException("Component " + componentName + " not found");
        }
    }
    
    public LoginFacultyWindow getLoginWindow() {
        return loginWindow;
    }
    
    public void enableTabs() {
        facultyWindow.enableWindow();
    }
    
    public void resetTables() {
        ( (TableView) facultyWindow.getTableView()).resetTables();
    }
    
    public JFrame getFrame() {
        return frame;
    }
    
    public MenuWindow getMenuWindow() {
        return menuWindow;
    }
    
    public JMenuBar getMenuBar() {
        return menuBar;
    }
    
    public JDialog createPopupDialog(String message) {
        JDialog dialog = new JDialog(frame, "Processing", true);
        JLabel label = new JLabel(message);
        label.setHorizontalAlignment(SwingConstants.CENTER); 
        label.setPreferredSize(new java.awt.Dimension(200, 100));
        
        dialog.setLayout(new java.awt.BorderLayout());
        dialog.add(label, java.awt.BorderLayout.CENTER);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        
        return dialog;
    }
    
    public void showContextMenu() {
        JButton addButton = (JButton) getComponent(Actions.ADD_TO_TABLE.name());
        addContextMenu.show(addButton,
            addButton.getX() + (int) addButton.getPreferredSize().getWidth()/2, 
            addButton.getY() + (int) addButton.getPreferredSize().getHeight()/2);
    }
    
    public void showAbout() {
        JDialog dialog = new JDialog((java.awt.Frame) null, "About", true);
            dialog.setSize(450, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setLayout(new java.awt.BorderLayout());
            dialog.getContentPane().setBackground(Constants.WINDOW_COLOR_LAYER_0);

            dialog.add(new JLabel("""
                <html>
                  <body style="text-align:center">
                  <h2>Technological Institute of the Philippines</h2>
                  <h4>CS 002 - Advanced Object-Oriented Programming</h4>
                  <h5>As part of the study:</h5>
                  <h3><b>Development of a Grade Encoding System for the Assessment of Senior High School Students</b></h3>
                    <p style="text-align:justify">This grade encoding program aims to facilitate Senior High School teachers that
                       may face several challenges with the complexity of managing and encoding of student grades.
                       By streamlining the grading process, this program seeks to support educators in
                       efficiently adapting to the updated K-12 grading system.
                    </i>
                    </p>
                  <h3><b>Developers</b></h3>
                    <p>Lorenz Chiong</p>
                    <p>Renz Ken Flores</p>
                    <p>Richard Daniel Sarmiento</p></body>
                </html>
                """)
            );
            
            dialog.setVisible(true);
    }
    
    public void setStudentNameInEditor(String name) {
        ( (EditPanel) facultyWindow.getGradeEditor() ).getStudentNameContent().setText(name);
    }
    
    /* TODO: Change
    public void setStudentClassInEditor(String name) {
        ( (EditPanel) editorWindow.getGradeEditor() ).getStudentClassContent().setText(name);
    }
    */
    
    public String getOutputTypeInEditor() {
        return (String) ( (EditPanel) facultyWindow.getGradeEditor() ).getOutputTypeCombo().getSelectedItem();
    }
    
    public int getOutputNumberInEditor() {
        return (int) ( (EditPanel) facultyWindow.getGradeEditor() ).getOutputNumberCombo().getSelectedItem();
    }
    
    public void setOutputNumberInEditor(int index) {
        getOutputNumberComboBox().setSelectedIndex(index);
    }
    
    public JComboBox getOutputNumberComboBox() {
        return (JComboBox) getComponent(Model.Fields.SELECT_ACTIVITY.name());
    }
    
    public void setWindowTitle(String title) {
        frame.setTitle(title);
    }
    
    public JTable getTable(int table) {
        return ((FacultyWindow) getEditorWindow()).getTable(table);
    }
    
    public Integer getGradeFieldValue() {
        return Integer.valueOf(
          ( (JTextField) getComponent(Fields.EDIT_GRADE.name()) ).getText()
        );
    }
    
    public JTextField getGradeField() {
        return ( (JTextField) getComponent(Fields.EDIT_GRADE.name()) );
    }
    
    public void setGradeFieldValue(String value) {
        ( (JTextField) getComponent(Fields.EDIT_GRADE.name()) ).setText(value);
    }
    
    public void setMaxGradeLabel(String value) {
        ( (JLabel) getComponent(Fields.MAX_GRADE.name()) ).setText("/" + value);
    }
    
    public String getTableValueAt(JTable table, int row, int col) {
        return String.valueOf(table.getModel().getValueAt(row, col));
    }
            
    public FacultyWindow getEditorWindow() {
        return facultyWindow;
    }
    
    public StudentWindow getStudentWindow() {
        return studentWindow;
    }
    
    public JTabbedPane getTabbedPane() {
        return facultyWindow.getTableView();
    }
    
    public void setTabNames(int semester) {
        JTabbedPane tableView = getTabbedPane();
        if (semester == 1) {
            tableView.setTitleAt(0, "Q1 Grading Period");
            tableView.setTitleAt(1, "Q2 Grading Period");
            tableView.setTitleAt(2, "Q1 Grading Sheet");
            tableView.setTitleAt(3, "Q2 Grading Sheet");
        } else if (semester == 2) {
            tableView.setTitleAt(0, "Q3 Grading Period");
            tableView.setTitleAt(1, "Q4 Grading Period");
            tableView.setTitleAt(2, "Q3 Grading Sheet");
            tableView.setTitleAt(3, "Q4 Grading Sheet");
        }
    }
        
    public void resizeTable(int selectedTab) {
        JTable table = getTable(selectedTab);
        
        WrappedHeaderRenderer headerRenderer = new WrappedHeaderRenderer();
        StudentNameRenderer studentNameRenderer = new StudentNameRenderer();

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        if (table.getColumnModel().getColumnCount() > 0) {
            table.getColumnModel().getColumn(1).setCellRenderer(studentNameRenderer);

            table.getTableHeader().setBackground(Color.LIGHT_GRAY);

            // Set column 1 width (Student number)
            table.getColumnModel().getColumn(0).setPreferredWidth(20);
            table.getColumnModel().getColumn(0).setMaxWidth(20);
            // Set column 2 width (Student name)
            table.getColumnModel().getColumn(1).setPreferredWidth(150);
            table.getColumnModel().getColumn(1).setMaxWidth(150);
            // Set column 3 width (Student gender)
            table.getColumnModel().getColumn(2).setPreferredWidth(30);
            table.getColumnModel().getColumn(2).setMaxWidth(30);
            table.setRowHeight(30);
        } 

        // Set each column to be unresizable
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setResizable(false);
        }

        table.revalidate();
        table.repaint();
    }
    
    public void resizeAllTables() {
        resizeTable(0);
        resizeTable(1);
        resizeTable(2);
        resizeTable(3);
        resizeTable(4);
    }
    
    /**
     * Set editor panel values
     */
    public void updateEditPanel(int selectedTab, int selectedActivity, Row selectedRow) {
        JTable table = facultyWindow.getTable(selectedTab);
        
        if (selectedTab > 1) {
            return;
        }
        
        /**
         * Set combo box to contain the list of activities in the current table
         */
        JComboBox selectActivity = getOutputNumberComboBox();
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        // Populate the combo box starting from 4th column to last column
        for (int i = 3; i < table.getModel().getColumnCount(); i++) {
            comboModel.addElement(table.getModel().getColumnName(i));
        }
        selectActivity.setModel(comboModel);

        // Set selected item in view
        try {
            setOutputNumberInEditor(selectedActivity);
        } catch (java.lang.IllegalArgumentException e) {}
        
        if (selectedRow == null) {
            setStudentNameInEditor("");
        } else {
            setStudentNameInEditor(selectedRow.getStudent().getStudentNameFormatted());
        }
        
        // Update grade text field with the value of current selection
        Integer value = selectedRow.getGrades().get(selectActivity.getSelectedIndex()).getGrade();
        if (value != null) {
            setGradeFieldValue(String.valueOf(value));
        } else {
            setGradeFieldValue("");
        }

        // Update the value of max grade with the value of current selected activity
        String s;
        try {
            if (selectedRow.getGrades().get(selectActivity.getSelectedIndex()) == null) {
                s = "";
            } else {
                s = String.valueOf(selectedRow.getGrades().get(selectActivity.getSelectedIndex()).getTotalScore());
            }
            setMaxGradeLabel(s);
        } catch (java.lang.IndexOutOfBoundsException e) {}
    }
}

// This class contains all the menu bar components
class MenuBar extends JMenuBar {

    JMenu menuFile;
    JMenu menuEdit;
    JMenu menuAccount;
    JMenu menuHelp;
    JMenuItem menuNewTable;
    JMenuItem menuOpenFile;
    JMenuItem menuRegisterStudent;
    JMenuItem menuExportFile;
    JMenuItem menuExit;
    JMenuItem menuEditGradeWeights;
    JMenuItem menuCreateReportCard;
    JMenuItem menuLogout;
    JMenuItem menuKeyboardShortcuts;
    JMenuItem menuAbout;
    
    public MenuBar() {
        // Instantiate the first menu option(File) and its items
        menuFile = new JMenu("Record");
        menuNewTable  = new JMenuItem("New Class Record");
        menuOpenFile  = new JMenuItem("Open Class Record");
        menuRegisterStudent  = new JMenuItem("Register Student to System");
        menuExportFile  = new JMenuItem("Export File");                         // UNUSED
        menuExit  = new JMenuItem("Exit");
        
        // Set keystrokes
        menuNewTable.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuOpenFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuExportFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        
        menuFile.add(menuNewTable);
        menuFile.add(menuOpenFile);       
        menuFile.add(menuRegisterStudent);       
        //menuFile.add(menuExportFile);         UNUSED
        menuFile.add(menuExit);
        
        // Instantiate the second menu options(View) and its items
        menuEdit = new JMenu("Edit");                                           // UNUSED
        menuEditGradeWeights = new JMenuItem("Edit Grade Weights");             // UNUSED
        menuCreateReportCard = new JMenuItem("Create Student Report Card");     // UNUSED

        // Set keystrokes
        //menuEditGradeWeights.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        
        //menuEdit.add(menuEditGradeWeights);
        
        menuAccount = new JMenu("Account");
        menuLogout = new JMenuItem("Logout");
        
        menuAccount.add(menuLogout);
        
        // Instantiate the third menu options(Help) and its items
        menuHelp = new JMenu("Help");
        menuKeyboardShortcuts = new JMenuItem("Keyboard Shortcuts");
        menuAbout = new JMenuItem("About");
        
        // menuHelp.add(menuKeyboardShortcuts);
        menuHelp.add(menuAbout);
        
        // Set actions to each JMenuItem
        menuNewTable.setName(Actions.NEW_RECORD.name());
        menuOpenFile.setName(Actions.OPEN_RECORD.name());
        menuRegisterStudent.setName(Actions.REGISTER_STUDENT_SYSTEM.name());
        menuExportFile.setName(Actions.EXPORTFILE.name());
        menuLogout.setName(Actions.LOGOUT.name());
        menuExit.setName(Actions.EXIT.name());
        menuKeyboardShortcuts.setName(Actions.VIEW_SHORTCUTS.name());
        menuAbout.setName(Actions.VIEW_ABOUT.name());
        
        // Add all menus to menu bar
        add(menuFile);
        //add(menuEdit);        // UNUSED
        add(menuAccount);
        add(menuHelp);
    }
    
    public void setStudentMode() {
        remove(menuFile);
        add(menuAccount);
        add(menuHelp);
    }
    
    public void setFacultyMode() {
        add(menuFile);
        //add(menuEdit);        // UNUSED
        add(menuAccount);
        add(menuHelp);
    }
}

class AddContextMenu extends JPopupMenu {
    public AddContextMenu() {
        setName(Actions.ADD_MENU_CLICKED.name());
        
        JMenuItem addStudent = new JMenuItem("Student");
        addStudent.setName(Actions.ADDSTUDENT.name());
        
        JMenuItem addActivity = new JMenuItem("Activity");
        addActivity.setName(Actions.ADDACTIVITY.name());
        
        // Add components
        add(addStudent);
        add(addActivity);
    }
}
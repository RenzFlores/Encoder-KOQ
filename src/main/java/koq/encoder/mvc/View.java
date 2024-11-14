package koq.encoder.mvc;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import koq.encoder.classes.Row;
import koq.encoder.mvc.Model.Actions;
import koq.encoder.components.EditPanel;
import koq.encoder.components.EditorWindow;
import koq.encoder.components.FilterPanel;
import koq.encoder.components.StudentNameRenderer;
import koq.encoder.components.WrappedHeaderRenderer;
import koq.encoder.mvc.Model.Fields;

public class View {

    private final JFrame frame;

    private final MenuBar menuBar;
    private final EditorWindow editorWindow;
    
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
         
        frame = new JFrame("Grade Encoder");                           
        frame.setSize(1280, 720);                 
        frame.setLayout(new CardLayout());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        menuBar = new MenuBar();
        frame.setJMenuBar(menuBar);
        
        editorWindow = new EditorWindow();
        frame.add(editorWindow);
        
        addContextMenu = new AddContextMenu();
        addContextMenu.setName("Add Context Menu");
        
        componentList = new ArrayList();
        initializeComponentList();
        
        frame.pack();
    }
    
    /**
     * Get all components in componentList. Called upon program start.
     * NOTE: Adding more components in the program may require them to explicitly be added here
     * or else it won't be able to be retrieved by Controller
    */
    private void initializeComponentList() {
        // Grade editor
        componentList.addAll(Arrays.asList(
                ( (EditPanel) editorWindow.getGradeEditor() ).getContainer().getComponents()));
        
        componentList.addAll(Arrays.asList(
                editorWindow.getToolbar().getComponents()));
        
        // Table editor
        //componentList.addAll(Arrays.asList(
        //        tableEditor.getContainer().getComponents()));
        
        // Toolbar
        //componentList.addAll(Arrays.asList(
        //        ( (Toolbar) tableEditor.getToolbar()).getComponents()));
        
        // Table view
        // componentList.add(( (TableView) tableEditor).getTab());
        
        // Filter
        componentList.addAll(Arrays.asList(
                ( (FilterPanel) editorWindow.getFilter() ).getContainer().getComponents()));
        
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
        ( (EditPanel) editorWindow.getGradeEditor() ).getStudentNameContent().setText(name);
    }
    
    /* TODO: Change
    public void setStudentClassInEditor(String name) {
        ( (EditPanel) editorWindow.getGradeEditor() ).getStudentClassContent().setText(name);
    }
    */
    
    public String getOutputTypeInEditor() {
        return (String) ( (EditPanel) editorWindow.getGradeEditor() ).getOutputTypeCombo().getSelectedItem();
    }
    
    public int getOutputNumberInEditor() {
        return (int) ( (EditPanel) editorWindow.getGradeEditor() ).getOutputNumberCombo().getSelectedItem();
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
    
    public JTable getTable(int term) {
        return ((EditorWindow) getEditorWindow()).getTable(term);
    }
    
    public Double getGradeFieldValue() {
        return Double.parseDouble(
          ( (JTextField) getComponent(Fields.EDIT_GRADE.name()) ).getText()
        );
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
            
    public EditorWindow getEditorWindow() {
        return editorWindow;
    }
    
    public JTabbedPane getTabbedPane() {
        return editorWindow.getTableView();
    }
        
    public void resizeTable(int selectedTab) {
        JTable table = getTable(selectedTab);
        
        if (table.getTableHeader() != null) {
            WrappedHeaderRenderer headerRenderer = new WrappedHeaderRenderer();
            StudentNameRenderer studentNameRenderer = new StudentNameRenderer();

            for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
            }

            if (table.getColumnModel().getColumnCount() > 0) {

                table.getColumnModel().getColumn(1).setCellRenderer(studentNameRenderer);

                table.getTableHeader().setBackground(Color.LIGHT_GRAY);

                table.getColumnModel().getColumn(0).setPreferredWidth(20);
                table.getColumnModel().getColumn(0).setMaxWidth(20);
                table.getColumnModel().getColumn(1).setPreferredWidth(150);
                table.getColumnModel().getColumn(1).setMaxWidth(150);

                table.setRowHeight(30);
            }

            // Set each column to be unresizable
            for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setResizable(false);
            }

            table.revalidate();
            table.repaint();
        }
    }
    
    /**
     * Set editor panel values
     */
    public void updateEditPanel(int selectedTab, int selectedRowIndex, int selectedActivity, Row selectedRow) {
        JTable table = editorWindow.getTable(selectedTab);
        
        if (selectedRow == null) {
            setStudentNameInEditor("");
        } else {
            setStudentNameInEditor(getTableValueAt(table, selectedRowIndex, 1));
        }
        
        // UNUSED, might remove later
        // setStudentClassInEditor((String) ((EditorWindow) getEditorWindow()).getTabNameAt(0));
        
        /**
         * Set combo box to contain the list of activities in the current table
         */
        JComboBox selectActivity = getOutputNumberComboBox();
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        // Populate the combo box
        for (int i = 2; i < table.getModel().getColumnCount()-1; i++) {
            comboModel.addElement(table.getModel().getColumnName(i));
        }
        selectActivity.setModel(comboModel);
        
        // Set selected item in view
        try {
            setOutputNumberInEditor(selectedActivity);
        } catch (java.lang.IllegalArgumentException e) {}       // Ignore and do nothing instead
        
        // Update grade text field with the value of current selection
        String value = getTableValueAt(table, selectedRowIndex, selectActivity.getSelectedIndex()+2);
        if (value.equals("null")) {
            setGradeFieldValue("");
        } else {
            setGradeFieldValue(value);
        }
        
        // Update the value of max grade with the value of current selected activity
        String s;
        try {
            if (selectedRow.getGrades().get(selectActivity.getSelectedIndex()) == null) {
                s = "";
            } else {
                s = String.valueOf(selectedRow.getGrades().get(selectActivity.getSelectedIndex()).getMaxGrade());
            }
            setMaxGradeLabel(s);
        } catch (java.lang.IndexOutOfBoundsException e) {}
    }
}

// This class contains all the menu bar components
class MenuBar extends JMenuBar {

    JMenu menuFile;
    JMenu menuEdit;
    JMenu menuHelp;
    JMenuItem menuNewTable;
    JMenuItem menuOpenFile;
    JMenuItem menuExportFile;
    JMenuItem menuExit;
    JMenuItem menuEditGradeWeights;
    JMenuItem menuCreateReportCard;
    JMenuItem menuKeyboardShortcuts;
    JMenuItem menuAbout;
    
    public MenuBar() {
        // Instantiate the first menu option(File) and its items
        menuFile = new JMenu("File");
        menuNewTable  = new JMenuItem("New Class Record");
        menuOpenFile  = new JMenuItem("Open Class Record");
        menuExportFile  = new JMenuItem("Export File");                         // UNUSED
        menuExit  = new JMenuItem("Exit");
        
        // Set keystrokes
        menuNewTable.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuOpenFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuExportFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        
        menuFile.add(menuNewTable);
        menuFile.add(menuOpenFile);       
        //menuFile.add(menuExportFile);         UNUSED
        menuFile.add(menuExit);
        
        // Instantiate the second menu options(View) and its items
        menuEdit = new JMenu("Edit");                                           // UNUSED
        menuEditGradeWeights = new JMenuItem("Edit Grade Weights");             // UNUSED
        menuCreateReportCard = new JMenuItem("Create Student Report Card");     // UNUSED

        // Set keystrokes
        //menuEditGradeWeights.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        
        //menuEdit.add(menuEditGradeWeights);
        
        // Instantiate the third menu options(Help) and its items
        menuHelp = new JMenu("Help");
        menuKeyboardShortcuts = new JMenuItem("Keyboard Shortcuts");
        menuAbout = new JMenuItem("About");
        
        menuHelp.add(menuKeyboardShortcuts);
        menuHelp.add(menuAbout);
        
        // Set actions to each JMenuItem
        menuNewTable.setName(Actions.NEW_RECORD.name());
        menuOpenFile.setName(Actions.OPEN_RECORD.name());
        menuExportFile.setName(Actions.EXPORTFILE.name());
        menuExit.setName(Actions.EXIT.name());
        menuKeyboardShortcuts.setName(Actions.VIEWSHORTCUTS.name());
        menuAbout.setName(Actions.VIEWABOUT.name());
        
        // Add all menus to menu bar
        add(menuFile);
        //add(menuEdit);        // UNUSED
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
        
        /** UNUSED CODE
        JMenuItem addAssignment = new JMenuItem("Assignment");
        addAssignment.setName(Actions.ADDASSIGNMENT.name());
        
        JMenuItem addPT = new JMenuItem("Performance Task");
        addPT.setName(Actions.ADDPT.name());
        
        JMenuItem addQuiz = new JMenuItem("Quiz");
        addQuiz.setName(Actions.ADDQUIZ.name());
        
        JMenuItem addExam = new JMenuItem("Exam");
        addExam.setName(Actions.ADDEXAM.name());
        */
        
        // Add components
        add(addStudent);
        add(addActivity);
    }
}
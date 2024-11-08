package koq.encoder.mvc;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.TableColumn;
import koq.encoder.mvc.Model.Actions;
import koq.encoder.components.EditPanel;
import koq.encoder.components.FilterPanel;
import koq.encoder.components.TableView;
import koq.encoder.mvc.Model.Fields;


public class View {

    private final JFrame frame;
    // private JPanel editPanel;

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
        
        TableEditorPanel tableEditor = (TableEditorPanel) editorWindow.getTableEditor();
        
        // Table editor
        componentList.addAll(Arrays.asList(
                tableEditor.getContainer().getComponents()));
        
        // Toolbar
        componentList.addAll(Arrays.asList(
                ( (Toolbar) tableEditor.getToolbar()).getContainer().getComponents()));
        
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
        JButton addButton = (JButton) getComponent(Actions.ADDTOTABLE.name());
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
    
    public void setStudentClassInEditor(String name) {
        ( (EditPanel) editorWindow.getGradeEditor() ).getStudentClassContent().setText(name);
    }
    
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
    
    public JTable getTable() {
        return ((EditorWindow) getEditorWindow()).getTable();
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
    
    public String getTableValueAt(int row, int col) {
        return String.valueOf(getTable().getModel().getValueAt(row, col));
    }
            
    public EditorWindow getEditorWindow() {
        return editorWindow;
    }
    
    /**
     * Set editor panel values
     */
    public void updateEditPanel(int selectedRowIndex, int selectedActivity, Row selectedRow) {
        
        setStudentNameInEditor(
            getTableValueAt(selectedRowIndex, 0)
        );
        
        System.out.println();
        System.out.println(getTableValueAt(selectedRowIndex, 0));
        
        // TODO: Change index to conform with multiple tabs
        setStudentClassInEditor((String) ((EditorWindow) getEditorWindow()).getTabNameAt(0));
        
        /**
         * Set combo box to contain the list of activities in the current table
         */
        JComboBox selectActivity = getOutputNumberComboBox();
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        // Populate the combo box
        for (int i = 1; i < getTable().getModel().getColumnCount(); i++) {
            comboModel.addElement(getTable().getModel().getColumnName(i));
        }
        selectActivity.setModel(comboModel);
        
        // Set selected item in view
        setOutputNumberInEditor(selectedActivity);
        
        // Update grade text field with the value of current selection
        setGradeFieldValue(
            getTableValueAt(selectedRowIndex, selectActivity.getSelectedIndex()+1)
        );
        
        // Update the value of max grade with the value of current selected activity
        String s;
        if (selectedRow.getGrades().get(selectActivity.getSelectedIndex()) == null) {
            s = "";
        } else {
            s = String.valueOf(selectedRow.getGrades().get(selectActivity.getSelectedIndex()).getMaxGrade());
        }
        
        setMaxGradeLabel(s);
        
        System.out.println("edit panel updated");
    }
}

// This class contains all the menu bar components
class MenuBar extends JMenuBar {

    JMenu menuFile;
    JMenu menuView;
    JMenu menuHelp;
    JMenuItem menuNewTable;
    JMenuItem menuOpenFile;
    JMenuItem menuExportFile;
    JMenuItem menuExit;
    JMenuItem menuPref;
    JMenuItem menuKeyboardShortcuts;
    JMenuItem menuAbout;
    
    public MenuBar() {
        // Instantiate the first menu option(File) and its items
        menuFile = new JMenu("File");
        menuNewTable  = new JMenuItem("New Table");
        menuOpenFile  = new JMenuItem("Open File");
        menuExportFile  = new JMenuItem("Export File");
        menuExit  = new JMenuItem("Exit");
        
        // Set keystrokes
        menuNewTable.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuOpenFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        menuExportFile.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_DOWN_MASK | java.awt.event.InputEvent.CTRL_DOWN_MASK));
        
        menuFile.add(menuNewTable);
        menuFile.add(menuOpenFile);       
        menuFile.add(menuExportFile);
        menuFile.add(menuExit);
        
        // Instantiate the second menu options(View) and its items
        menuView = new JMenu("View");
        menuPref = new JMenuItem("Preferences");

        // Set keystrokes
        menuPref.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        
        menuView.add(menuPref);
        
        // Instantiate the third menu options(Help) and its items
        menuHelp = new JMenu("Help");
        menuKeyboardShortcuts = new JMenuItem("Keyboard Shortcuts");
        menuAbout = new JMenuItem("About");
        
        menuHelp.add(menuKeyboardShortcuts);
        menuHelp.add(menuAbout);
        
        // Set actions to each JMenuItem
        menuNewTable.setName(Actions.NEWTABLE.name());
        menuOpenFile.setName(Actions.OPENFILE.name());
        menuExportFile.setName(Actions.EXPORTFILE.name());
        menuExit.setName(Actions.EXIT.name());
        menuAbout.setName(Actions.VIEWABOUT.name());
        
        // Add all menus to menu bar
        add(menuFile);
        add(menuView);
        add(menuHelp);
    }
}

class AddContextMenu extends JPopupMenu {
    public AddContextMenu() {
        setName(Actions.ADDMENUCLICKED.name());
        
        JMenuItem addStudent = new JMenuItem("Student");
        addStudent.setName(Actions.ADDSTUDENT.name());
        
        JMenuItem addActivity = new JMenuItem("Activity");
        addActivity.setName(Actions.ADDACTIVITY.name());
        
        JMenuItem addAssignment = new JMenuItem("Assignment");
        addAssignment.setName(Actions.ADDASSIGNMENT.name());
        
        JMenuItem addPT = new JMenuItem("Performance Task");
        addPT.setName(Actions.ADDPT.name());
        
        JMenuItem addQuiz = new JMenuItem("Quiz");
        addQuiz.setName(Actions.ADDQUIZ.name());
        
        JMenuItem addExam = new JMenuItem("Exam");
        addExam.setName(Actions.ADDEXAM.name());
        
        // Add components
        add(addStudent);
        add(addActivity);
        add(addAssignment);
        add(addPT);
        add(addQuiz);
        add(addExam);
    }
}

class Toolbar extends JPanel {
    
    JPanel containerPanel;
    JButton addButton;
    JButton removeButton;
    
    public JPanel getContainer() {
        return containerPanel;
    }
    
    public Toolbar() {
        setPreferredSize(new java.awt.Dimension(670, 80));
        setLayout(new CardLayout());
        
        containerPanel = new JPanel();
        containerPanel.setPreferredSize(new java.awt.Dimension(250, 500));
        
        FlowLayout containerLayout = new FlowLayout(FlowLayout.LEFT, 10, 20);
        containerLayout.setAlignOnBaseline(true);
        
        containerPanel.setLayout(containerLayout);
        
        addButton = new JButton("Add");
        addButton.setName(Actions.ADDTOTABLE.name());
        addButton.setPreferredSize(new java.awt.Dimension(90, 40));
        
        removeButton = new JButton("Remove");
        removeButton.setName(Actions.REMOVEFROMTABLE.name());
        removeButton.setPreferredSize(new java.awt.Dimension(90, 40));
        
        containerPanel.add(addButton);
        containerPanel.add(removeButton);
        
        add(containerPanel);
    }
}

class TableEditorPanel extends JPanel {
    
    JPanel containerPanel;
    Toolbar toolbar;
    TableView tableView;
    
    public JPanel getToolbar() {
        return toolbar;
    }
    
    public JTable getTable() {
        return ((TableView) tableView).getTable();
    }
    
    public String getTabNameAt(int index) {
        return ((TableView) tableView).getTitleAt(index);
    }
    
    public JPanel getContainer() {
        return containerPanel;
    }
    
    public TableEditorPanel() {
        setPreferredSize(new java.awt.Dimension(680, 710));
        setLayout(new CardLayout());
        
        containerPanel = new JPanel();
        containerPanel.setPreferredSize(new java.awt.Dimension(250, 500));
        containerPanel.setLayout(new FlowLayout());
        containerPanel.setBackground(new Color(200, 200, 240));
        
        toolbar = new Toolbar();
        containerPanel.add(toolbar);
        
        tableView = new TableView();
        containerPanel.add(tableView);
        
        add(containerPanel);
    }    
}

class EditorWindow extends JPanel {
    
    private final EditPanel editPanel;
    private final TableEditorPanel tableEditorPanel;
    private final FilterPanel filterPanel;
    
    public JPanel getGradeEditor() {
        return editPanel;
    }
    
    public JPanel getTableEditor() {
        return tableEditorPanel;
    }
    
    public JTable getTable() {
        return ((TableEditorPanel) getTableEditor()).getTable();
    }
    
    public JPanel getFilter() {
        return filterPanel;
    }
    
    public String getTabNameAt(int index) {
        return ( (TableEditorPanel) getTableEditor() ).getTabNameAt(index);
    }
    
    public EditorWindow() {
        setPreferredSize(new Dimension(1300, 720));
        setBackground(new Color(20, 20, 60));           // DEBUG
        
        editPanel = new EditPanel();
        tableEditorPanel = new TableEditorPanel();
        filterPanel = new FilterPanel();
        
        add(editPanel);
        add(tableEditorPanel);
        add(filterPanel);
    }
}


package koq.encoder.mvc;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import koq.encoder.mvc.Model.Actions;
import koq.encoder.mvc.Model.Fields;

/**
 *
 * @author KOQ
 */
public class Controller {
    
    private final View view;
    private final Model model;
            
    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        
        JTable table = view.getTable();
        table.getTableHeader().addMouseListener(new HeaderSelector(table));
        table.addMouseListener(new RowSelector(table));
        table.setModel(model.getClassRecord());
        table.setRowSelectionInterval(model.getSelectedRow(), model.getSelectedRow());
        updateEditPanel();
        
        // New Table event
        ( (JMenuItem) view.getComponent(Actions.NEWTABLE.name()) ).addActionListener((ActionEvent ev) -> {
            System.out.println("new table event");
        });
        
        // Open File event
        ( (JMenuItem) view.getComponent(Actions.OPENFILE.name()) ).addActionListener((ActionEvent ev) -> {
            System.out.println("open file event");
        });
        
        // Export File event
        ( (JMenuItem) view.getComponent(Actions.EXPORTFILE.name()) ).addActionListener((ActionEvent ev) -> {
            System.out.println("export file event");
        });
        
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
            updateEditPanel();
        });
        
        // Grade text field DocumentListener
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).addActionListener((ActionEvent ev) -> {
            int row = model.getSelectedRow();
            int col = model.getSelectedActivity();
            String value = ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).getText();
           
            Grade grade = model.getClassRecord().getRowAt(row).getGrades().get(col);
            if (grade == null) {
                // TODO: Create new Grade instance
                System.out.println("TODO: Create a new Grade instance here");
                // model.getClassRecord().getRowAt(row).getGrades().get(col) = new Grade(1, 1, 1, );
            } else if (value.isBlank()) {
                grade.setGrade(null);
            } else {
                grade.setGrade(Double.parseDouble(value));
            }
            
            // BUG: MUST CREATE INSTANCE FIRST IF CELL IS NULL
            view.getTable().getModel().setValueAt(model.getClassRecord().getValueAt(row, col+1), row, col+1);
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
        
        // Add Activity event
        ( (JMenuItem) view.getComponent(Actions.ADDACTIVITY.name()) ).addActionListener((ActionEvent ev) -> {
            // TODO: Add code
        });
        
        // Add Assignment event
        ( (JMenuItem) view.getComponent(Actions.ADDASSIGNMENT.name()) ).addActionListener((ActionEvent ev) -> {
            System.out.println("Add assignment");
        });
        
        // Add Performance Task event
        ( (JMenuItem) view.getComponent(Actions.ADDPT.name()) ).addActionListener((ActionEvent ev) -> {
            System.out.println("Add exam");
        });
        
        // Add Quiz event
        ( (JMenuItem) view.getComponent(Actions.ADDQUIZ.name()) ).addActionListener((ActionEvent ev) -> {
            JFrame window = new AddStudentWindow();
            window.setVisible(true);
            
            List<String> columns = model.getClassRecord().getColumns().stream()
                    .filter(e -> e.contains("Quiz")).collect(Collectors.toList());
            String newActivityName;
            int index;
            
            if (!columns.isEmpty()) {
                String[] s = columns.getLast().split(" ");
                newActivityName = s[0] + " " + (Integer.parseInt(s[s.length-1])+1);
                index = model.getClassRecord().findColumn(columns.getLast());
                
                // new Activity(newActivityName, 5.0, 10.0, 1);       DUMMY CODE
            } else {
                newActivityName = "Quiz 1";
                columns = model.getClassRecord().getColumns().stream()
                    .filter(e -> e.contains("Performance Task")).collect(Collectors.toList());
                index = model.getClassRecord().findColumn(columns.getLast());
            }
            
            // Insert new column
            model.getClassRecord().getColumns().add(index+1, newActivityName);
            view.getTable().getColumnModel().addColumn(
                    new TableColumn(index));
            
            // Set column names
            for (var v: model.getClassRecord().getColumns()) { 
                int i = model.getClassRecord().findColumn(v);
                view.getTable().getColumnModel().getColumn(i).setHeaderValue(v);
            }

            for (Row r: model.getClassRecord().getClassList()) {
                // Fill all cells inside column to be empty
                r.getGrades().add(index, null);
            }
            
            updateEditPanel();
        });
        
        // Add Exam event
        ( (JMenuItem) view.getComponent(Actions.ADDEXAM.name()) ).addActionListener((ActionEvent ev) -> {
            List<String> columns = model.getClassRecord().getColumns().stream()
                    .filter(e -> e.contains("Exam")).collect(Collectors.toList());
            String newActivityName;
            
            if (!columns.isEmpty()) {
                String[] s = columns.getLast().split(" ");
                newActivityName = s[0] + " " + (Integer.parseInt(s[s.length-1])+1);
                
                // new Activity(newActivityName, 5, 10.0, "1st");       DUMMY CODE
            } else {
                newActivityName = "Exam 1";
            }
            
            // Get index
            int index = model.getClassRecord().findColumn(columns.getLast());

            // Insert new column
            model.getClassRecord().getColumns().add(index+1, newActivityName);
            view.getTable().getColumnModel().addColumn(
                    new TableColumn(index));

            // Set column names
            for (var v: model.getClassRecord().getColumns()) { 
                int i = model.getClassRecord().findColumn(v);
                view.getTable().getColumnModel().getColumn(i).setHeaderValue(v);
            }
            
            for (Row r: model.getClassRecord().getClassList()) {
                // Fill all cells inside column to be empty
                r.getGrades().add(index, null);
            }
            
            updateEditPanel();
        });
        
    }
    
    /**
     * Set editor panel values
     */
    private void updateEditPanel() {
        
        view.setStudentNameInEditor(
            view.getTableValueAt(model.getSelectedRow(), 0)
        );
        
        // TODO: Change index to conform with multiple tabs
        view.setStudentClassInEditor((String) ((EditorWindow) view.getEditorWindow()).getTabNameAt(0));
        
        /**
         * Set combo box to contain the list of activities in the current table
         */
        JComboBox selectActivity = view.getOutputNumberComboBox();
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        // Populate the combo box
        for (int i = 1; i < view.getTable().getModel().getColumnCount(); i++) {
            comboModel.addElement(view.getTable().getModel().getColumnName(i));
        }
        selectActivity.setModel(comboModel);
        
        // Set selected item in view
        view.setOutputNumberInEditor(model.getSelectedActivity());
        
        // Update grade text field with the value of current selection
        view.setGradeFieldValue(
            view.getTableValueAt(model.getSelectedRow(), selectActivity.getSelectedIndex()+1)
        );
        
        // Update the value of max grade with the value of current selected activity
        String s;
        Row r = (Row) model.getClassRecord().getRowAt(model.getSelectedRow());
        if (r.getGrades().get(selectActivity.getSelectedIndex()) == null) {
            s = "";
        } else {
            s = String.valueOf(r.getGrades().get(selectActivity.getSelectedIndex()).getMaxGrade());
        }
        
        view.setMaxGradeLabel(s);
        
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
        table.setRowSelectionInterval(row, row);
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
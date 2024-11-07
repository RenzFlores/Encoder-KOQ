package koq.encoder.mvc;

import classes.Grade;
import koq.encoder.components.AddActivityWindow;
import koq.encoder.components.AddStudentWindow;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
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
        view.updateEditPanel(model.getSelectedRow(), model.getSelectedActivity(), model.getClassRecord().getRowAt(model.getSelectedRow()));
        
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
            view.updateEditPanel(model.getSelectedRow(), model.getSelectedActivity(), model.getClassRecord().getRowAt(model.getSelectedRow()));
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
                } else {
                    activityWindow.setVisible(true);
                    
                    activityWindow.getButton().addActionListener(new AddActivityWindowListener(activityWindow, model));
                }
            }
        };
        
        // Add Student event
        ( (JMenuItem) view.getComponent(Actions.ADDSTUDENT.name()) ).addActionListener(menuListener);
        // Add Activity event
        ( (JMenuItem) view.getComponent(Actions.ADDACTIVITY.name()) ).addActionListener(menuListener);
        // Add Assignment event
        ( (JMenuItem) view.getComponent(Actions.ADDASSIGNMENT.name()) ).addActionListener(menuListener);
        // Add Performance Task event
        ( (JMenuItem) view.getComponent(Actions.ADDPT.name()) ).addActionListener(menuListener);
        
        // Add Quiz event
        ( (JMenuItem) view.getComponent(Actions.ADDQUIZ.name()) ).addActionListener((ActionEvent ev) -> {
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
            
            view.updateEditPanel(model.getSelectedRow(), model.getSelectedActivity(), model.getClassRecord().getRowAt(model.getSelectedRow()));
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
            
            view.updateEditPanel(model.getSelectedRow(), model.getSelectedActivity(), model.getClassRecord().getRowAt(model.getSelectedRow()));
        });
        
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
            }
        }
        
        public boolean isFormValid() {
            return validForm;
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

// Listener for the confirmation button in AddStudentWindow
class AddActivityWindowListener implements ActionListener {
    
    AddActivityWindow window;
    Model model;
    
    public AddActivityWindowListener(AddActivityWindow window, Model model) {
        this.window = window;
        this.model = model;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String section = window.getSection();
        String subject = window.getSubject();
        String term = window.getTerm();
        boolean validForm = true;
        
        if (section.isBlank() || subject.isBlank()) {
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
            /*
            model.setActivitySection();
            model.setActivitySubject();
            model.setActivityTerm();
            */
        }
    }
}
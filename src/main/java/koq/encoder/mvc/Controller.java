package koq.encoder.mvc;

import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
            String value = ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name())).getText();
           
            if (value.isBlank()) {
                model.getClassRecord().getRowAt(row).getGrades().get(col).setGrade(0.0);
            } else {
                model.getClassRecord().getRowAt(row).getGrades().get(col).setGrade(Double.parseDouble(value));
            }
            
            model.getClassRecord().fireTableCellUpdated(row, col);
            view.getTable().getModel().setValueAt(model.getClassRecord().getValueAt(row, col+1), row, col+1);
        });
        
        // Row selection model
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only allow single row selection
        
        view.getTable().setRowSelectionAllowed(true);
        view.getTable().setColumnSelectionAllowed(false);

        // Event listener for row selection
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Ignore extra processing during value changes
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = view.getTable().getSelectedRow();
                    if (selectedRow != -1) {
                        // Change selection
                        model.setSelectedRow(selectedRow);
                        
                        // Update all fields in edit panel
                        updateEditPanel();
                    }
                }
            }
        });
        
        /*
        // Column selection model
        ListSelectionModel columnSelectionModel = table.getColumnModel().getSelectionModel();
        columnSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Event listener for column selection
        columnSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                // Ignore extra processing during value changes
                if (!e.getValueIsAdjusting()) {
                    int selectedColumn = view.getTable().getSelectedColumn();
                    if (selectedColumn != -1) {
                        model.setSelectedColumn(selectedColumn);
                        
                        // Update all fields in edit panel
                        updateEditPanel();
                        
                        // String columnName = view.getTable().getColumnName(selectedColumn);
                    }
                }
            }
        });
        */
        
        // Add to Table button clicked event
        JButton addButton = (JButton) view.getComponent(Actions.ADDTOTABLE.name());
        addButton.addActionListener((ActionEvent e) -> {
            // Show context menu right below the button, centered
            view.showContextMenu();
        });
        
        // Remove from Table button clicked event
        ( (JButton) view.getComponent(Actions.REMOVEFROMTABLE.name()) ).addActionListener((ActionEvent ev) -> {
            System.out.println("remove from table event");
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
            System.out.println("Add quiz");
        });
        
        // Add Exam event
        ( (JMenuItem) view.getComponent(Actions.ADDEXAM.name()) ).addActionListener((ActionEvent ev) -> {
            System.out.println("Add exam");
        });
        
    }
    
    private void updateEditPanel() {
        // Set editor panel values
        view.setStudentNameInEditor(
            (String) view.getTable().getModel().getValueAt(model.getSelectedRow(), 0)
        );
        
        // TODO: Change index to conform with multiple tabs
        view.setStudentClassInEditor((String) ((EditorWindow) view.getEditorWindow()).getTabNameAt(0));
        
        JComboBox selectActivity = ( (JComboBox) view.getComponent(Fields.SELECT_ACTIVITY.name()) );
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel();
        
        for (int i = 1; i < view.getTable().getModel().getColumnCount(); i++) {
            comboModel.addElement(view.getTable().getModel().getColumnName(i));
        }
        
        selectActivity.setModel(comboModel);
        selectActivity.setSelectedIndex(model.getSelectedActivity());
        
        // Update grade text field with the value of current selection
        ( (JTextField) view.getComponent(Fields.EDIT_GRADE.name()) ).setText(
            String.valueOf(view.getTable().getModel().getValueAt(model.getSelectedRow(), selectActivity.getSelectedIndex()+1))
        );
        
        // Update the value of max grade with the value of current selected activity
        Row r = (Row) model.getClassRecord().getRowAt(model.getSelectedRow());
        ( (JLabel) view.getComponent(Fields.MAX_GRADE.name()) ).setText(
            "/" + String.valueOf(r.getGrades().get(selectActivity.getSelectedIndex()).getMaxGrade())
        );
        
    }
}
package koq.encoder.mvc;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author KOQ
 */
public class Controller {
    
    private final View view;
    private final Model model;
    
    public enum Actions {
        NEWTABLE,
        OPENFILE,
        EXPORTFILE,
        EXIT,
        ADDTOTABLE,
        REMOVEFROMTABLE,
        ADDACTIVITY,
        ADDASSIGNMENT,
        ADDPT,
        ADDQUIZ,
        ADDEXAM
    }
    
    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;
        
        JTable table = view.getTable();
        table.setModel(model.getClassRecord());
        view.setStudentNameInEditor(model.getTableValueAtCurrentSelection());
        
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
        
        // Row selection model
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Only allow single row selection

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
                        
                        // Set editor panel values
                        view.setStudentNameInEditor((String) model.getTableValueAtCurrentSelection());
                        // TODO: Change index to conform with multiple tabs
                        view.setStudentClassInEditor((String) ((EditorWindow) view.getEditorWindow()).getTabNameAt(0));
                    }
                }
            }
        });
        
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
                        String columnName = view.getTable().getColumnName(selectedColumn);
                        System.out.println("Selected Column: " + selectedColumn + ", Column Name: " + columnName);
                    }
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
}
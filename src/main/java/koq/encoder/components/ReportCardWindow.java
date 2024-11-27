package koq.encoder.components;

import javax.swing.*;

public class ReportCardWindow extends JFrame {
    public ReportCardWindow() {
        reportCard.setPreferredSize(new java.awt.Dimension(500, 500));
        reportCard.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel2.setText("Name:");
        reportCard.add(jLabel2);

        jLabel11.setText("Renz Ken Flores");
        jLabel11.setPreferredSize(new java.awt.Dimension(450, 16));
        reportCard.add(jLabel11);

        jLabel14.setText("Grade:");
        reportCard.add(jLabel14);

        jLabel15.setText("12");
        jLabel15.setPreferredSize(new java.awt.Dimension(450, 16));
        reportCard.add(jLabel15);

        jLabel21.setText("Section:");
        reportCard.add(jLabel21);

        jLabel22.setText("Newton");
        jLabel22.setPreferredSize(new java.awt.Dimension(440, 16));
        reportCard.add(jLabel22);

        jLabel18.setText("School Year:");
        reportCard.add(jLabel18);

        jLabel19.setText("2024-2025");
        jLabel19.setPreferredSize(new java.awt.Dimension(420, 16));
        reportCard.add(jLabel19);

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("REPORT ON LEARNING PROGRESS AND ACHIEVEMENT");
        jLabel23.setPreferredSize(new java.awt.Dimension(500, 16));
        reportCard.add(jLabel23);

        jLabel24.setText("First Semester");
        jLabel24.setPreferredSize(new java.awt.Dimension(100, 16));
        reportCard.add(jLabel24);

        jScrollPane1.setPreferredSize(new java.awt.Dimension(490, 275));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Pagbasa at Pagsusuri ng Iba’t-Ibang Teksto Tungo sa Pananaliksik", "93", "94", "94"},
                {"21st Century Literature from the Philippines and the World", "94", "93", null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "SUBJECTS", "Quarter 1", "Quarter 2", "Semester Final Grades"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setPreferredSize(new java.awt.Dimension(490, 100));
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        reportCard.add(jScrollPane1);

        jLabel25.setText("Second Semester");
        reportCard.add(jLabel25);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(490, 275));

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Pagbasa at Pagsusuri ng Iba’t-Ibang Teksto Tungo sa Pananaliksik", "93", "94", "94"},
                {"21st Century Literature from the Philippines and the World", "94", "93", null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "SUBJECTS", "Quarter 1", "Quarter 2", "Semester Final Grades"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable2.setPreferredSize(new java.awt.Dimension(490, 100));
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(200);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable2.getColumnModel().getColumn(2).setResizable(false);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable2.getColumnModel().getColumn(3).setResizable(false);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        reportCard.add(jScrollPane2);
    }
}

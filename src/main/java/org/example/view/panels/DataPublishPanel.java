package org.example.view.panels;

import org.example.utils.JDBCUtils;
import org.example.utils.Utility;
import org.example.view.forms.PublishDataForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

public class DataPublishPanel extends JPanel {
    private JTable tableOverview;
    private static DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JPanel labelPanel;
    private JLabel labelTitle;
    private JLabel labelHint;
    private JPanel panelCenter;


    public DataPublishPanel() {
        initialize();
    }

    private void initialize() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        labelPanel.setBackground(Color.WHITE);
        labelTitle = new JLabel("Data Publishing");
        labelTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        labelHint = new JLabel("(Click on one of the following for details)");
        labelHint.setFont(new Font("SansaSerif", Font.PLAIN, 16));
        labelPanel.add(labelTitle);

        add(labelPanel, BorderLayout.NORTH);

        panelCenter = new JPanel(new BorderLayout(10, 10));
        panelCenter.setBackground(Color.WHITE);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // padding

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Date and Time");
        tableModel.addColumn("Duration");
        tableModel.addColumn("Notes");
        tableModel.addColumn("Price");

        tableOverview = new JTable(tableModel);
        tableOverview.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tableOverview.setRowHeight(22);
        tableOverview.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));

        tableOnAction();

        JDBCUtils.insertIntoTableSessions();

        rowSorter = new TableRowSorter<>(tableModel);
        tableOverview.setRowSorter(rowSorter);

        JScrollPane scrollPane = new JScrollPane(tableOverview);
        panelCenter.add(scrollPane, BorderLayout.CENTER);

        add(panelCenter, BorderLayout.CENTER);
    }

    private void tableOnAction() {
        tableOverview.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tableOverview.getSelectedRow() != -1) {
                int selectedRow = tableOverview.getSelectedRow();
                int modelRow = tableOverview.convertRowIndexToModel(selectedRow); // ako je sort

                int id = (int) tableModel.getValueAt(modelRow, 0);

                try{
                    new PublishDataForm();
                }catch (Exception ex){
                    Utility.throwMessage("Error", "Form Publish Data could not be created");
                }

                System.out.println(id);
            }
        });

    }

    public static void addSession(Object[] rowData) {
        tableModel.addRow(rowData);
    }
}

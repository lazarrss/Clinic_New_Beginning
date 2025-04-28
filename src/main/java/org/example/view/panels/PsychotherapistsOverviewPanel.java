package org.example.view.panels;

import org.example.utils.JDBCUtils;
import org.example.view.placeholder.PlaceholderTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PsychotherapistsOverviewPanel extends JPanel {
    private JTable tableOverview;
    private static DefaultTableModel tableModel;
    private JTextField textFieldSearch;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JPanel labelPanel;
    private JLabel labelTitle;
    private JPanel panelCenter;
    private JPanel panelSearch;


    public PsychotherapistsOverviewPanel() {
        initialize();
    }

    private void initialize() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        labelPanel.setBackground(Color.WHITE);
        labelTitle = new JLabel("Psychotherapists");
        labelTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        labelPanel.add(labelTitle);

        add(labelPanel, BorderLayout.NORTH);

        panelCenter = new JPanel(new BorderLayout(10, 10));
        panelCenter.setBackground(Color.WHITE);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // padding

        panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelSearch.setBackground(Color.WHITE);

        textFieldSearch = new PlaceholderTextField("Search by first name...");

        textFieldSearch.setFont(new Font("SansSerif", Font.PLAIN, 16));
        panelSearch.add(textFieldSearch);

        panelCenter.add(panelSearch, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Unique Citizen Identification Number");
        tableModel.addColumn("Date of Birth");
        tableModel.addColumn("Place of Residence");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Psychologist");

        tableOverview = new JTable(tableModel);
        tableOverview.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tableOverview.setRowHeight(22);
        tableOverview.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));

        JDBCUtils.insertIntoTablePsychotherapistOverview();

        rowSorter = new TableRowSorter<>(tableModel);
        tableOverview.setRowSorter(rowSorter);

        JScrollPane scrollPane = new JScrollPane(tableOverview);
        panelCenter.add(scrollPane, BorderLayout.CENTER);

        add(panelCenter, BorderLayout.CENTER);

        tfOnAction();
    }

    private void tfOnAction() {
        textFieldSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String text = textFieldSearch.getText();
                if (text.trim().isEmpty()) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter(STR."(?i)\{text}", 1));
                }
            }
        });
        textFieldSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textFieldSearch.getText().equals("Search by first name...")) {
                    textFieldSearch.setText("");
                    textFieldSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textFieldSearch.getText().isEmpty()) {
                    textFieldSearch.setForeground(Color.GRAY);
                    textFieldSearch.setText("Search by first name...");
                }
            }
        });
    }

    public static void addPsychotherapist(Object[] rowData) {
        tableModel.addRow(rowData);
    }

//    public void clearPsychotherapists() {
//        tableModel.setRowCount(0);
//    }
}

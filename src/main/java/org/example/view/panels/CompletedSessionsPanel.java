package org.example.view.panels;

import org.example.utils.JDBCUtils;
import org.example.utils.Utility;
import org.example.view.forms.NotesAndTestsForm;
import org.example.view.placeholder.PlaceholderTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CompletedSessionsPanel extends JPanel {

    private JTable tableOverview;
    private static DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JPanel labelPanel;
    private JLabel labelTitle;
    private JLabel labelHint;
    private JPanel panelCenter;
    private JButton buttonDetails;
    private JPanel panelButton;
    private JPanel panelNorth;


    public CompletedSessionsPanel() {
        initialize();
    }

    private void initialize() {
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));

        labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
        labelPanel.setBackground(Color.WHITE);
        labelTitle = new JLabel("Completed Sessions");
        labelTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        labelHint = new JLabel("(Click on one of the following for details)");
        labelHint.setFont(new Font("SansaSerif", Font.PLAIN, 16));
        labelPanel.add(labelTitle);
        labelPanel.add(labelHint);

        buttonDetails = new JButton("Details");
        buttonDetails.setBackground(new Color(41, 128, 185));
        buttonDetails.setForeground(Color.WHITE);
        buttonDetails.setFocusPainted(false);
        buttonDetails.setFont(new Font("SansSerif", Font.BOLD, 14));
        panelButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
        panelButton.add(buttonDetails);
        panelButton.setBackground(Color.WHITE);

        panelNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 150, 20));
        panelNorth.add(labelPanel);
        panelNorth.add(panelButton);
        panelNorth.setBackground(Color.WHITE);
        add(panelNorth, BorderLayout.NORTH);

        panelCenter = new JPanel(new BorderLayout(10, 10));
        panelCenter.setBackground(Color.WHITE);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // padding

//        panelSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
//        panelSearch.setBackground(Color.WHITE);
//
//        textFieldSearch = new PlaceholderTextField("Search by first name...");
//
//        textFieldSearch.setFont(new Font("SansSerif", Font.PLAIN, 16));
//        panelSearch.add(textFieldSearch);
//
//        panelCenter.add(panelSearch, BorderLayout.NORTH);

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

        JDBCUtils.insertIntoTableCompletedSeassions();

        rowSorter = new TableRowSorter<>(tableModel);
        tableOverview.setRowSorter(rowSorter);

        JScrollPane scrollPane = new JScrollPane(tableOverview);
        panelCenter.add(scrollPane, BorderLayout.CENTER);

        add(panelCenter, BorderLayout.CENTER);

        buttonOnAction();
    }

    public static void addSession(Object[] rowData) {
        tableModel.addRow(rowData);
    }
    private void buttonOnAction(){
        buttonDetails.addActionListener(e ->{
            if(tableOverview.getSelectedRow() == -1)
                Utility.throwMessage("Error", "Please select a row");
            try{
                int id = (int) tableModel.getValueAt(tableOverview.getSelectedRow(), 0); // cuvamo id
                new NotesAndTestsForm(id);
            }catch (Exception ex) {
                Utility.throwMessage("Error", ex.getMessage());
                return;
            }
        });
    }

}

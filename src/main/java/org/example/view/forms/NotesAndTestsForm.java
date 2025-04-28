package org.example.view.forms;

import org.example.utils.JDBCUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class NotesAndTestsForm extends JFrame {
    private static int seansa_id;
    private JTable tableOverview;
    private static DefaultTableModel tableModel;
    private JPanel panelMain;
    private JLabel labelTitle;
    private JLabel labelNotes;
    private JTextArea textAreaNotes;
    private JScrollPane scrollPaneNotes;
    private JLabel labelTests;
    private JLabel labelPublish;
    private JButton buttonClose;
    private static DefaultTableModel tableModelPublish;
    private JTable tablePublish;
    private JScrollPane scrollPublish;

    public NotesAndTestsForm(int id) {
        seansa_id = id;
        initializeAll();
        setVisible(true);
    }
    private void initializeAll() {
        setTitle("Session Details - ID: " + seansa_id);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 700);
        setLocationRelativeTo(null);

        panelMain = new JPanel();
        panelMain.setBackground(Color.WHITE);
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        labelTitle = new JLabel("Session Overview");
        labelTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTitle.setForeground(new Color(52, 73, 94));
        panelMain.add(labelTitle);
        panelMain.add(Box.createRigidArea(new Dimension(0, 20)));

        labelNotes = new JLabel("Notes:");
        labelNotes.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelNotes.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(labelNotes);

        String notes = JDBCUtils.getNotes();
        if(notes == null) notes = "There is no data for notes";

        textAreaNotes = new JTextArea();
        textAreaNotes.setText(notes);
        textAreaNotes.setLineWrap(true);
        textAreaNotes.setWrapStyleWord(true);
        textAreaNotes.setEditable(false);
        textAreaNotes.setBackground(new Color(245, 245, 245));
        textAreaNotes.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));

        scrollPaneNotes = new JScrollPane(textAreaNotes);
        scrollPaneNotes.setPreferredSize(new Dimension(600, 150));
        scrollPaneNotes.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(scrollPaneNotes);
        panelMain.add(Box.createRigidArea(new Dimension(0, 20)));

        labelTests = new JLabel("Completed Tests:");
        labelTests.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelTests.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(labelTests);

//        String[] columnNames = {"Test Name", "Result"};
//        tableTest = new JTable(new String[][]{}, columnNames);
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Test Name");
        tableModel.addColumn("Result");
        tableOverview = new JTable(tableModel);
        tableOverview.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tableOverview.setRowHeight(22);
        tableOverview.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        tableOverview.setEnabled(false);
        JScrollPane scrollTestovi = new JScrollPane(tableOverview);
        scrollTestovi.setPreferredSize(new Dimension(600, 100));
        scrollTestovi.setAlignmentX(Component.CENTER_ALIGNMENT);

        JDBCUtils.insertIntoTableTestInfo();

        panelMain.add(scrollTestovi);
        panelMain.add(Box.createRigidArea(new Dimension(0, 20)));

        labelPublish = new JLabel("Publishing Info:");
        labelPublish.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelPublish.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(labelPublish);

        tableModelPublish = new DefaultTableModel();
        tableModelPublish.addColumn("Date");
        tableModelPublish.addColumn("Published To");
        tableModelPublish.addColumn("Type");

        tablePublish = new JTable(tableModelPublish);
        tablePublish.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablePublish.setRowHeight(22);
        tablePublish.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 16));
        tablePublish.setEnabled(false);

        JDBCUtils.insertIntoTablePublish();

        scrollPublish = new JScrollPane(tablePublish);
        scrollPublish.setPreferredSize(new Dimension(600, 80));
        scrollPublish.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelMain.add(scrollPublish);
        panelMain.add(Box.createRigidArea(new Dimension(0, 20)));

        buttonClose = new JButton("Close");
        buttonClose.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonClose.setBackground(new Color(41, 18, 126));
        buttonClose.setForeground(Color.WHITE);
        buttonClose.setFocusPainted(false);
        buttonClose.setFont(new Font("SansSerif", Font.BOLD, 14));
        buttonClose.addActionListener(e -> dispose());

        panelMain.add(buttonClose);

        add(panelMain);
    }

    public static void addTest(Object[] rowData) {
        tableModel.addRow(rowData);
    }
    public static void addPublishData(Object[] rowData){
        tableModelPublish.addRow(rowData);
    }
    public static int getSeansa_id() {
        return seansa_id;
    }
}

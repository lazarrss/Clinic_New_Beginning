package org.example.view.panels;

import org.example.utils.JDBCUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PaymentsAndDebtsPanel extends JPanel {

    private JLabel labelTitle;
    private JTable tablePayments;
    private static DefaultTableModel tableModel;
    private JScrollPane scrollPaneTable;

    public PaymentsAndDebtsPanel() {
        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        labelTitle = new JLabel("Client Payments and Outstanding Debts");
        labelTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        labelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTitle.setForeground(new Color(44, 62, 80));

        add(labelTitle);
        add(Box.createRigidArea(new Dimension(0, 20)));

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{
                "Client Name", "Clients Phone Number", "Payment Purpose", "Amount", "Payment Method",
                "Currency", "Payment Date", "Is Overdue", "Days Overdue"
        });

        tablePayments = new JTable(tableModel);
        tablePayments.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablePayments.setRowHeight(24);
        tablePayments.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        tablePayments.setFillsViewportHeight(true);
        tablePayments.setEnabled(false);

        JDBCUtils.insertIntoTablePayments();

        scrollPaneTable = new JScrollPane(tablePayments);
        scrollPaneTable.setPreferredSize(new Dimension(900, 300));
        scrollPaneTable.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(scrollPaneTable);
    }

    public static void addSession(Object[] rowData) {
        tableModel.addRow(rowData);
    }

}

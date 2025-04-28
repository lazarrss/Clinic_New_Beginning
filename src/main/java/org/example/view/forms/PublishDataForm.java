package org.example.view.forms;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;

public class PublishDataForm extends JFrame {
    private JPanel panelMain;
    private JLabel labelDate;
    private JLabel labelPublishedTo;
    private JLabel labelType;
    private JDateChooser dateChooserPublishDate;
    private JTextField textPublishedTo;
    private JTextField textType;
    private JButton buttonInsertData;

    public PublishDataForm() {
        initializeAll();
        setVisible(true);
    }

    private void initializeAll() {
        setTitle("Insert Publish Data");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);

        panelMain = new JPanel();
        panelMain.setBackground(Color.WHITE);
        panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));
        panelMain.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Date label and picker
        labelDate = new JLabel("Select Date:");
        labelDate.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelDate.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(labelDate);
        panelMain.add(Box.createRigidArea(new Dimension(0, 10)));

        dateChooserPublishDate = new JDateChooser();
        dateChooserPublishDate.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateChooserPublishDate.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateChooserPublishDate.setPreferredSize(new Dimension(200, 30));
        panelMain.add(dateChooserPublishDate);
        panelMain.add(Box.createRigidArea(new Dimension(0, 20)));

        // Published To label and textfield
        labelPublishedTo = new JLabel("Published To:");
        labelPublishedTo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelPublishedTo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(labelPublishedTo);
        panelMain.add(Box.createRigidArea(new Dimension(0, 10)));

        textPublishedTo = new JTextField();
        textPublishedTo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textPublishedTo.setMaximumSize(new Dimension(300, 30));
        textPublishedTo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(textPublishedTo);
        panelMain.add(Box.createRigidArea(new Dimension(0, 20)));

        // Type label and textfield
        labelType = new JLabel("Type:");
        labelType.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelType.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(labelType);
        panelMain.add(Box.createRigidArea(new Dimension(0, 10)));

        textType = new JTextField();
        textType.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textType.setMaximumSize(new Dimension(300, 30));
        textType.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(textType);
        panelMain.add(Box.createRigidArea(new Dimension(0, 30)));

        // Insert button
        buttonInsertData = new JButton("Insert Data");
        buttonInsertData.setBackground(new Color(41, 128, 185));
        buttonInsertData.setForeground(Color.WHITE);
        buttonInsertData.setFocusPainted(false);
        buttonInsertData.setFont(new Font("SansSerif", Font.BOLD, 14));
        buttonInsertData.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelMain.add(buttonInsertData);

        add(panelMain);

        // Set initial focus
        textPublishedTo.requestFocusInWindow();
    }
}

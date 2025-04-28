package org.example.view.forms;

import com.toedter.calendar.JDateChooser;
import org.example.utils.JDBCUtils;
import org.example.utils.Utility;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class PublishDataForm extends JFrame {
    private static int session_id;
    private JPanel panelMain;
    private JLabel labelDate;
    private JLabel labelPublishedTo;
    private JLabel labelType;
    private JDateChooser dateChooserPublishDate;
    private JTextField textPublishedTo;
    private JTextField textType;
    private JButton buttonInsertData;

    public PublishDataForm(int id) {
        session_id = id;
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

        labelDate = new JLabel("Select Date:");
        labelDate.setFont(new Font("SansSerif", Font.PLAIN, 16));
        labelDate.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelMain.add(labelDate);
        panelMain.add(Box.createRigidArea(new Dimension(0, 10)));

        dateChooserPublishDate = new JDateChooser();
        dateChooserPublishDate.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateChooserPublishDate.setAlignmentX(Component.CENTER_ALIGNMENT);
        dateChooserPublishDate.setPreferredSize(new Dimension(200, 30));
        dateChooserPublishDate.setDateFormatString("yyyy-MM-dd");
        panelMain.add(dateChooserPublishDate);
        panelMain.add(Box.createRigidArea(new Dimension(0, 20)));

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

        buttonInsertData = new JButton("Insert Data");
        buttonInsertData.setBackground(new Color(41, 128, 185));
        buttonInsertData.setForeground(Color.WHITE);
        buttonInsertData.setFocusPainted(false);
        buttonInsertData.setFont(new Font("SansSerif", Font.BOLD, 14));
        buttonInsertData.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonOnAction();

        panelMain.add(buttonInsertData);

        add(panelMain);

//        textPublishedTo.requestFocusInWindow();
    }

    private void buttonOnAction() {
        buttonInsertData.addActionListener(e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String dateofPublish = sdf.format(dateChooserPublishDate.getDate());
                String publishedTo = textPublishedTo.getText();
                String typeofPublish = textType.getText();
                JDBCUtils.insertDataPublish(dateofPublish, publishedTo, typeofPublish);
            } catch (Exception ex) {
                Utility.throwMessage("Error", "Please fill all fields correctly.");
            }
        });

    }

    public static int getSession_id() {
        return session_id;
    }
}

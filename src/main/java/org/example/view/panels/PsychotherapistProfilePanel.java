package org.example.view.panels;

import com.toedter.calendar.JDateChooser;
import org.example.model.Psychotherapist;
import org.example.utils.JDBCUtils;
import org.example.utils.Utility;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class PsychotherapistProfilePanel extends JPanel {
    private JLabel labelName = new JLabel("Name:");
    private JLabel labelLastname = new JLabel("Lastname:");
    private JLabel labelUCIN = new JLabel("UCIN:");
    private JLabel labelDOB = new JLabel("Date of Birth:");
    private JLabel labelPOR = new JLabel("Place of Residence:");
    private JLabel labelPhoneNumber = new JLabel("Phone Number:");
    private JLabel labelPsychologist = new JLabel("Psychologist:");

    private JDateChooser dateChooserDOB = new JDateChooser();
    private JButton buttonSaveProfile = new JButton("Save Profile");

    private JTextField textFieldName = new JTextField(20);
    private JTextField textFieldLastname = new JTextField(20);
    private JTextField textFieldUCIN = new JTextField(20);
//    private JTextField textFieldDOB = new JTextField(20);
    private JTextField textFieldPOR = new JTextField(20);
    private JTextField textFieldPhoneNumber = new JTextField(20);
    private JCheckBox checkBoxPsychologist = new JCheckBox();

    public PsychotherapistProfilePanel() {
        initialize();
    }

    private void initialize() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        textFiledSets();
        dateChooserDOB.setDateFormatString("yyyy-MM-dd");

        gbc.gridx = 0; gbc.gridy = 0;
        add(labelName, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        add(textFieldName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(labelLastname, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        add(textFieldLastname, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(labelUCIN, gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        add(textFieldUCIN, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(labelDOB, gbc);

//        gbc.gridx = 1; gbc.gridy = 3;
//        add(textFieldDOB, gbc);

        gbc.gridx = 1; gbc.gridy = 3;
        add(dateChooserDOB, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        add(labelPOR, gbc);

        gbc.gridx = 1; gbc.gridy = 4;
        add(textFieldPOR, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        add(labelPhoneNumber, gbc);

        gbc.gridx = 1; gbc.gridy = 5;
        add(textFieldPhoneNumber, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        add(labelPsychologist, gbc);

        gbc.gridx = 1; gbc.gridy = 6;
        add(checkBoxPsychologist, gbc);

//        buttonSaveProfile = new JButton();
        buttonSaveProfile.setFont(new Font("SansSerif", Font.BOLD, 14));
        buttonSaveProfile.setBackground(new Color(52, 152, 219)); // Blue color
        buttonSaveProfile.setForeground(Color.WHITE);
        buttonSaveProfile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        buttonSaveProfile.setFocusPainted(false);
        buttonSaveProfile.setPreferredSize(new Dimension(150, 40));

        gbc.gridx = 1; gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        add(buttonSaveProfile, gbc);
        buttonOnAction();
    }

    private void buttonOnAction() {
        buttonSaveProfile.addActionListener(e ->{
            short psychologist = (short) (checkBoxPsychologist.isSelected() ? 1 : 0);
            Utility.updateUserModel(textFieldName.getText(), textFieldLastname.getText(), textFieldUCIN.getText(), dateChooserDOB.getDate(),
                    textFieldPOR.getText(), textFieldPhoneNumber.getText(), psychologist);
            java.sql.Date sqlDOB = new java.sql.Date(dateChooserDOB.getDate().getTime());
            JDBCUtils.updateUserDatabase(textFieldName.getText(), textFieldLastname.getText(), textFieldUCIN.getText(), sqlDOB,
                    textFieldPOR.getText(), textFieldPhoneNumber.getText(), psychologist);

        });
    }

    private void textFiledSets() {
        Font font = new Font("SansSerif", Font.PLAIN, 16);

        textFieldName.setFont(font);
        textFieldLastname.setFont(font);
        textFieldUCIN.setFont(font);
//        textFieldDOB.setFont(font);
        dateChooserDOB.setFont(font);
        textFieldPOR.setFont(font);
        textFieldPhoneNumber.setFont(font);
        checkBoxPsychologist.setFont(font);

        textFieldName.setText(Psychotherapist.getInstance().getName());
        textFieldLastname.setText(Psychotherapist.getInstance().getLastname());
        textFieldUCIN.setText(Psychotherapist.getInstance().getUCIN());
//        textFieldDOB.setText(Psychotherapist.getInstance().getDOB().toString());
        dateChooserDOB.setDate(Psychotherapist.getInstance().getDOB());
        textFieldPOR.setText(Psychotherapist.getInstance().getPOR());
        textFieldPhoneNumber.setText(Psychotherapist.getInstance().getPhoneNumber());
        //checkBoxPsychologist.setText(String.valueOf(Psychotherapist.getInstance().getPsychologist()));
        checkBoxPsychologist.setSelected(Psychotherapist.getInstance().getPsychologist() == 1);
    }
}

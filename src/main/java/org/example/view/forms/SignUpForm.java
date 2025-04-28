package org.example.view.forms;

import com.toedter.calendar.JDateChooser;
import org.example.utils.JDBCUtils;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SignUpForm extends JFrame {

    private JLabel labelName;
    private JTextField textFieldName;
    private JLabel labelLastname;
    private JTextField textFieldLastname;
    private JLabel labelUCIN;
    private JTextField textFieldUCIN;
    private JLabel labelDateOfBirth;
    private JDateChooser dateChooserDOB;
    private JLabel labelPlaceOfResidence;
    private JTextField textFieldResidence;
    private JLabel labelPhoneNumber;
    private JTextField textFieldPhoneNumber;
    private JLabel labelPsychologist;
    private JCheckBox checkBoxPsychologist;

    private JButton buttonSignup;

    public SignUpForm(){
        initialize();
        initializeFields();
//        dateChooserDOB = new JDateChooser();
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.add(dateChooserDOB);
//        add(panel);
//        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
//        try {
//            Date date = sdf.parse("15.04.2000");
//            dateChooserDOB.setDate(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(dateChooserDOB.getDate());

        setVisible(true);
    }

    private void initializeFields() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        labelName = new JLabel("Enter your name:");
        textFieldName = new JTextField(20);

        labelLastname = new JLabel("Enter your lastname:");
        textFieldLastname = new JTextField(20);

        labelUCIN = new JLabel("Enter your Unique Citizen Identification Number:");
        textFieldUCIN = new JTextField(20);

        labelDateOfBirth = new JLabel("Enter your date of birth (year-month-day):");
        dateChooserDOB = new JDateChooser();
        dateChooserDOB.setDateFormatString("yyyy-MM-dd");

        labelPlaceOfResidence = new JLabel("Enter your place of residence:");
        textFieldResidence = new JTextField(20);

        labelPhoneNumber = new JLabel("Enter your phone number:");
        textFieldPhoneNumber = new JTextField(20);

        labelPsychologist = new JLabel("Psychologist?");
        checkBoxPsychologist = new JCheckBox();

        buttonSignup = new JButton("Sign Up");
        buttonOnAction();

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(labelName, gbc);
        gbc.gridx = 1;
        panel.add(textFieldName, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(labelLastname, gbc);
        gbc.gridx = 1;
        panel.add(textFieldLastname, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(labelUCIN, gbc);
        gbc.gridx = 1;
        panel.add(textFieldUCIN, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(labelDateOfBirth, gbc);
        gbc.gridx = 1;
        panel.add(dateChooserDOB, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(labelPlaceOfResidence, gbc);
        gbc.gridx = 1;
        panel.add(textFieldResidence, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(labelPhoneNumber, gbc);
        gbc.gridx = 1;
        panel.add(textFieldPhoneNumber, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        panel.add(labelPsychologist, gbc);
        gbc.gridx = 1;
        panel.add(checkBoxPsychologist, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonSignup, gbc);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);
    }

    private void buttonOnAction() {
        buttonSignup.addActionListener(e ->{
            String name = textFieldName.getText();
            String lastname = textFieldLastname.getText();
            String UCIN = textFieldUCIN.getText();

            Date selectedDate = dateChooserDOB.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String DOB = sdf.format(selectedDate);

            String POR = textFieldResidence.getText();
            String phoneNumber = textFieldPhoneNumber.getText();
            String psychologist = String.valueOf(checkBoxPsychologist.isSelected() ? 1 : 0);

            JDBCUtils.insertPsychotherapist(name, lastname, UCIN, DOB, POR, phoneNumber, psychologist);
        });
    }


    private void initialize(){
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize((int) (screenWidth * 0.33), (int) (screenHeight * 0.5));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Clinic New Beginning");
    }
}

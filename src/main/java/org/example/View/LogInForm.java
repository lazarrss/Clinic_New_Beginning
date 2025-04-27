package org.example.View;

import org.example.Utils.JDBCUtils;
import org.example.Utils.Utility;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

public class LogInForm extends JFrame {
    private JLabel labelTitle;
    private JLabel labelFooter;
    private JTextField textFieldUsername;
    private JTextField passwordFieldPassword;
    private JButton buttonLogin;
    private JButton buttonRegister;
    private JLabel labelUsername;
    private JLabel labelPassword;

    public LogInForm(){
        initialize();
        initializeFields();
        setVisible(true);
    }

    private void initialize(){
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize((int) (screenWidth * 0.2), (int) (screenHeight * 0.45));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Clinic New Beginning");
    }

    private void initializeFields() {

        labelTitle = new JLabel("Clinic 'New Beginning'");
        labelTitle.setFont(new Font("Arial", Font.BOLD, 20));
        labelTitle.setHorizontalAlignment(SwingConstants.CENTER);

        labelFooter = new JLabel("© 2025 New Beginning");
        labelFooter.setFont(new Font("Arial", Font.PLAIN, 10));
        labelFooter.setHorizontalAlignment(SwingConstants.CENTER);

        labelUsername = new JLabel("Name:");
        textFieldUsername = new JTextField(13);

        labelPassword = new JLabel("Password (UCIN/JMBG):");
        passwordFieldPassword = new JTextField(13);

        buttonLogin = new JButton("Log In");
        buttonRegister = new JButton("Sign Up");

        textFieldUsername.setText("Marko");
        passwordFieldPassword.setText("2303198512345");

        buttonsOnAction();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Naslov
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;
        panel.add(labelTitle, gbc);

        // Red 1
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(labelUsername, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(textFieldUsername, gbc);

        // Red 2
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(labelPassword, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordFieldPassword, gbc);

        // Red 3
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 10));
        buttonsPanel.add(buttonLogin);
        buttonsPanel.add(buttonRegister);
        panel.add(buttonsPanel, gbc);

        // Red 4
        gbc.gridy++;
        panel.add(labelFooter, gbc);

        add(panel);
    }


    private void buttonsOnAction() {
        buttonLogin.addActionListener(e -> {
            // ako postoji u database otvori novu formu ako ne izbaci gresku
            String name = textFieldUsername.getText();
            String password = passwordFieldPassword.getText();
            if(JDBCUtils.existsInDatabase(name, password)){
                try{
                    new MainForm();
                }catch (Exception ex){
                    Utility.throwMessage("Error", ex.getMessage());
                }
            }else{
                Utility.throwMessage("Error", "Invalid UCIN/JMBG or name");
            }
        });
        buttonRegister.addActionListener(e -> {
            new SignUpForm();
        });
    }
}

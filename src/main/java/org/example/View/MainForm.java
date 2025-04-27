package org.example.View;

import javax.swing.*;
import java.awt.*;

public class MainForm extends JFrame {

    public MainForm(){
        initialize();



        setVisible(true);
    }

    private void initialize(){
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize((int) (screenWidth * 0.75), (int) (screenHeight * 0.75));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Clinic New Beginning");
    }

}

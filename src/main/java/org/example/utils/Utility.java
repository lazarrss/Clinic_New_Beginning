package org.example.utils;

import javax.swing.*;

public class Utility {
    public static void throwMessage(String title, String content){
        JOptionPane.showMessageDialog(null, content, title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}

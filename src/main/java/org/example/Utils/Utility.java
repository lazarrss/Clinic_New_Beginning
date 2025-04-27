package org.example.Utils;

import javax.swing.*;

public class Utility {
    public static void throwException(String title, String content){
        JOptionPane.showMessageDialog(null, content, title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}

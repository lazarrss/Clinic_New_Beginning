package org.example.Utils;

import javax.swing.*;
import java.awt.*;

public class Utility {
    public static void throwMessage(String title, String content){
        JOptionPane.showMessageDialog(null, content, title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}

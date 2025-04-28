package org.example.utils;

import org.example.model.Psychotherapist;

import javax.swing.*;

public class Utility {
    public static void throwMessage(String title, String content){
        JOptionPane.showMessageDialog(null, content, title,
                JOptionPane.INFORMATION_MESSAGE);
    }
    public static void updateUserModel(String name, String lastname, String UCIN, java.util.Date DOB, String POR, String phoneNumber, short psychologist) {
        Psychotherapist.getInstance().setName(name);
        Psychotherapist.getInstance().setLastname(lastname);
        Psychotherapist.getInstance().setUCIN(UCIN);
        Psychotherapist.getInstance().setDOB(DOB);
        Psychotherapist.getInstance().setPOR(POR);
        Psychotherapist.getInstance().setPhoneNumber(phoneNumber);
        Psychotherapist.getInstance().setPsychologist(psychologist);
    }
}

package org.example;

import org.example.utils.JDBCUtils;
import org.example.view.forms.LogInForm;

public class Main{
    public static void main(String[] args) {
        try{
            JDBCUtils.connect();
            new LogInForm();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
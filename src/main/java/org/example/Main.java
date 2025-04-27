package org.example;

import org.example.Utils.JDBCUtils;
import org.example.View.LogInForm;

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
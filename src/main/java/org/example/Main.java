package org.example;

import org.example.View.LogInForm;

public class Main{
    public static void main(String[] args) {
        try{
            new LogInForm();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
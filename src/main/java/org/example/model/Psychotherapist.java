package org.example.model;

import java.util.Date;

public class Psychotherapist {
    private static Psychotherapist user;
    private int id;
    private String name;
    private String lastname;
    private String UCIN; // JMBG
    private Date DOB;
    private String POR; // place of residence / prebivaliste
    private String phoneNumber;
    private short psychologist;

    private Psychotherapist(){
    }
    public static Psychotherapist getInstance() {
        if (user == null) {
            user = new Psychotherapist();
        }
        return user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUCIN() {
        return UCIN;
    }

    public void setUCIN(String UCIN) {
        this.UCIN = UCIN;
    }

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public String getPOR() {
        return POR;
    }

    public void setPOR(String POR) {
        this.POR = POR;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public short getPsychologist() {
        return psychologist;
    }

    public void setPsychologist(short psychologist) {
        this.psychologist = psychologist;
    }
}

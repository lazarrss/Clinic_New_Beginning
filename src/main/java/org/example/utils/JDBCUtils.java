package org.example.utils;

import jdk.jshell.execution.Util;
import org.example.model.Psychotherapist;
import org.example.view.forms.NotesAndTestsForm;
import org.example.view.forms.PublishDataForm;
import org.example.view.panels.*;

import java.sql.*;
import java.util.Properties;

public class JDBCUtils {
    public static Connection connection = null;
    public static String URL = "jdbc:mysql://localhost:3306/novi_pocetak";

    public static void connect(){
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "");
        try{
            connection = DriverManager.getConnection(URL, properties);
        } catch (SQLException e) {
            Utility.throwMessage("Connection error", e.getMessage());
        }
    }
    public static boolean existsInDatabase(String username, String JMBG){
        try{
//            String query = STR."select* from psihoterapeut where ime = '\{username}' and JMBG = '\{JMBG}'";
            String query = "select* from psihoterapeut where ime = ? and JMBG = ?";
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            st.setString(1, username);
            st.setString(2, JMBG);
            //setUser(query);
//            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String lastname = rs.getString(3);
                String UCIN = rs.getString(4);
                Date DOB = rs.getDate(5);
                String POR = rs.getString(6);
                String phoneNumber = rs.getString(7);
                short psychologist = rs.getShort(8);
                Psychotherapist.getInstance().setId(id);
                Utility.updateUserModel(name, lastname, UCIN, DOB, POR, phoneNumber, psychologist);
                connection.commit();
                return true;
            }else{
                connection.commit();
                return false;
            }

        }catch (Exception sqlException){
            Utility.throwMessage("SQL Error", sqlException.getMessage());
        }
        return false;
    }

    public static void insertPsychotherapist(String name, String lastname, String UCIN, String DOB, String POR, String phoneNumber, String psychologist){
        try{
            String query = "call insert_psihoterapeut(?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement ps = connection.prepareStatement(query);
            connection.setAutoCommit(false);

            ps.setString(1, name);
            ps.setString(2, lastname);
            ps.setString(3, UCIN);
            ps.setString(4, DOB);
            ps.setString(5, POR);
            ps.setString(6, phoneNumber);
            ps.setString(7, psychologist);

            ps.executeUpdate();
            connection.commit();
            Utility.throwMessage("Success", "A new psychotherapist has been added to the database 'New Beginning'");
        }catch (SQLException ex){
            Utility.throwMessage("Error", ex.getMessage());
        }
    }
    public static void updateUserDatabase(String name, String lastname, String UCIN, Date DOB, String POR, String phoneNumber, short psychologist) {
        try{

//            String query = "UPDATE psihoterapeut SET ime = ?, prezime = ?, JMBG = ?, datum_rodjenja = ?, prebivaliste = ?, broj_telefona = ?, psiholog = ? WHERE psihoterapeut_id = ?'"+Psychotherapist.getInstance().getId()+"'";
            String query = "call update_psihoterapeut(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setString(2, lastname);
            statement.setString(3, UCIN);
            statement.setDate(4, DOB);
            statement.setString(5, POR);
            statement.setString(6, phoneNumber);
            statement.setShort(7, psychologist);
            statement.setInt(8, Psychotherapist.getInstance().getId());
            int affectedRows = statement.executeUpdate();
            if(affectedRows > 0)
                Utility.throwMessage("Success", "Profile updated successfully");
            else Utility.throwMessage("Error", "Profile could not be updated");
        }catch (Exception ex){
            Utility.throwMessage("SQL Error", ex.getMessage());
        }
    }
    public static void insertIntoTablePsychotherapistOverview(){

        try{
            String query = "select* from psihoterapeut";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String lastname = rs.getString(3);
                String UCIN = rs.getString(4);
                Date DOB = rs.getDate(5);
                String POR = rs.getString(6);
                String phoneNumber = rs.getString(7);
                short psychologist = rs.getShort(8);
                PsychotherapistsOverviewPanel.addPsychotherapist(new Object[]{id, name, lastname, UCIN, DOB, POR, phoneNumber, psychologist});
            }
//            Utility.throwMessage("Success", "All values inserted into table");
        }catch (Exception ex){
            Utility.throwMessage("Error", ex.getMessage());
        }
    }

    public static void insertIntoTableNewClientApplication() {
        try{
//            String query = "select distinct k.klijent_id, k.ime, k.prezime, k.datum_rodjenja, k.pol, k.email, k.broj_telefona from klijent k " +
//                    "join Prijava p on p.klijent_id = k.klijent_id " +
//                    "join seansa s on s.seansa_id = p.Seansa_seansa_id " +
//                    "where k.klijent_id = " + Psychotherapist.getInstance().getId();
//            String query = STR."select distinct k.klijent_id, k.ime, k.prezime, k.datum_rodjenja, k.pol, k.email, k.broj_telefona from klijent k join Prijava p on p.klijent_id = k.klijent_id join seansa s on s.seansa_id = p.Seansa_seansa_id where k.klijent_id = \{Psychotherapist.getInstance().getId()}";
            String query = "select distinct k.klijent_id, k.ime, k.prezime, k.datum_rodjenja, k.pol, k.email, k.broj_telefona from klijent k join Prijava p on p.klijent_id = k.klijent_id join seansa s on s.seansa_id = p.Seansa_seansa_id where s.Psihoterapeut_psihoterapeut_id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            st.setInt(1, Psychotherapist.getInstance().getId());
//            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String lastname = rs.getString(3);
                Date DOB = rs.getDate(4);
                String gender = rs.getString(5);
                String email = rs.getString(6);
                String phoneNumber = rs.getString(7);
                NewClientApplicationPanel.addClient(new Object[]{id, name, lastname, DOB, gender, email, phoneNumber});
            }
            connection.commit();
        }catch (Exception ex) {
            Utility.throwMessage("SQL Error", ex.getMessage());
        }
    }

    public static void insertIntoTableCompletedSessions() {
        try{
//            String query = STR."select distinct k.klijent_id, k.ime, k.prezime, k.datum_rodjenja, k.pol, k.email, k.broj_telefona from klijent k join Prijava p on p.klijent_id = k.klijent_id join seansa s on s.seansa_id = p.Seansa_seansa_id where k.klijent_id = \{Psychotherapist.getInstance().getId()}";
            String query = "select s.seansa_id, s.dan_vreme, s.trajanje_minuti, s.beleske, c.trenutna_cena from seansa s " +
                    "join psihoterapeut p on p.psihoterapeut_id=s.Psihoterapeut_psihoterapeut_id " +
                    "left join cena c on c.cena_id = s.Cena_cena_id " +
                    "where s.dan_vreme < NOW() and p.psihoterapeut_id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            st.setInt(1, Psychotherapist.getInstance().getId());
//            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                int id = rs.getInt(1);
                String dateTime = rs.getTimestamp(2).toString();
                int duration = rs.getInt("trajanje_minuti");
                String notes = rs.getString("beleske");
                float price = rs.getFloat("trenutna_cena");
                CompletedSessionsPanel.addSession(new Object[]{id, dateTime, duration, notes, price});
            }
            connection.commit();
        }catch (Exception ex) {
//            ex.printStackTrace();
            Utility.throwMessage("SQL Error", ex.getMessage());
        }
    }

    public static void insertIntoTableUpcomingSessions() {
        try{
//            String query = STR."select distinct k.klijent_id, k.ime, k.prezime, k.datum_rodjenja, k.pol, k.email, k.broj_telefona from klijent k join Prijava p on p.klijent_id = k.klijent_id join seansa s on s.seansa_id = p.Seansa_seansa_id where k.klijent_id = \{Psychotherapist.getInstance().getId()}";
            String query = "select s.*, c.trenutna_cena from seansa s " +
                    "join psihoterapeut p on p.psihoterapeut_id=s.Psihoterapeut_psihoterapeut_id " +
                    "left join cena c on c.cena_id = s.Cena_cena_id " +
                    "where s.dan_vreme >= NOW() and p.psihoterapeut_id = ?";
//            Statement st = connection.createStatement();
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            st.setInt(1, Psychotherapist.getInstance().getId());
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                int id = rs.getInt(1);
                String dateTime = rs.getTimestamp(2).toString();
                int duration = rs.getInt("trajanje_minuti");
                String notes = rs.getString("beleske");
                if(notes == null)
                    notes = "...";
                float price = rs.getFloat("trenutna_cena");
                UpcomingSessionsPanel.addSession(new Object[]{id, dateTime, duration, notes, price});
            }
            connection.commit();
        }catch (Exception ex) {
//            ex.printStackTrace();
            Utility.throwMessage("SQL Error", ex.getMessage());
        }
    }

    public static String getNotes() {
        try {
//            String query = STR."select s.beleske from seansa s where seansa_id = \{NotesAndTestsForm.getSeansa_id()}";
            String query = "select s.beleske from seansa s where seansa_id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            st.setInt(1, NotesAndTestsForm.getSeansa_id());
//            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                connection.commit();
                return rs.getString("beleske");
            }
            else Utility.throwMessage("Error", "No results found for notes with id = " + NotesAndTestsForm.getSeansa_id());
        }catch (Exception ex){
            Utility.throwMessage("Error", ex.getMessage());
        }

        return "";
    }

    public static void insertIntoTableTestInfo() {
        try{
            String query = "select pt.naziv, rezultat from psiholoski_test pt " +
                    "left join izrada_testa it on it.Psiholoski_test_psiholoski_test_id = pt.psiholoski_test_id " +
                    "where it.Seansa_seansa_id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            st.setInt(1, NotesAndTestsForm.getSeansa_id());
//            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                String name = rs.getString("naziv");
                int result = rs.getInt("rezultat");
                NotesAndTestsForm.addTest(new Object[]{name, result});
            }
            connection.commit();
        }catch (Exception ex){
            Utility.throwMessage("SQL Error", ex.getMessage());
        }
    }

    public static void insertIntoTablePublish() {
        try {
            String query = "select op.datum_objavljivanja, op.kome_je_objavljeno, vo.naziv from objavljivanje_podataka op " +
                    "left join vrsta_objavljivanja vo on op.Vrsta_objavljivanja_vrsta_objavljivanja_id = vo.vrsta_objavljivanja_id " +
                    "where op.Seansa_seansa_id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            st.setInt(1, NotesAndTestsForm.getSeansa_id());
//            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                Date dateofPublish = rs.getDate("datum_objavljivanja");
                String whoGotIt = rs.getString("kome_je_objavljeno");
                String typeofPublish = rs.getString("naziv");
                NotesAndTestsForm.addPublishData(new Object[]{dateofPublish, whoGotIt, typeofPublish});
            }
            connection.commit();
        }catch (Exception ex){
            Utility.throwMessage("SQL Error", ex.getMessage());
        }
    }

    public static void insertIntoTableSessions() {
        try{
            String query = "select s.*, c.trenutna_cena from seansa s " +
                "join psihoterapeut p on p.psihoterapeut_id=s.Psihoterapeut_psihoterapeut_id " +
                "left join cena c on c.cena_id = s.Cena_cena_id " +
                "where p.psihoterapeut_id = ?";
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            st.setInt(1, Psychotherapist.getInstance().getId());
//            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery();
            while (rs.next()){
                int id = rs.getInt(1);
                String dateTime = rs.getTimestamp(2).toString();
                int duration = rs.getInt("trajanje_minuti");
                String notes = rs.getString("beleske");
                if(notes == null)
                    notes = "...";
                float price = rs.getFloat("trenutna_cena");
                DataPublishPanel.addSession(new Object[]{id, dateTime, duration, notes, price});
            }
            connection.commit();
        }catch (Exception ex) {
//            ex.printStackTrace();
            Utility.throwMessage("SQL Error", ex.getMessage());
        }
    }

    public static void insertDataPublish(String dateofPublish, String publishedTo, String typeofPublish) {
        try {
            String query = "call insert_objavljivanje_podataka (?, ?, ?, ?)";
            int type_id = returnIdFromDatabase(typeofPublish);
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            java.sql.Date date = java.sql.Date.valueOf(dateofPublish);
            st.setDate(1, date);
            st.setString(2, publishedTo);
            st.setInt(3, type_id);
            st.setInt(4, PublishDataForm.getSession_id());
            int affectedRows = st.executeUpdate();
            connection.commit();
            if(affectedRows > 0)
                Utility.throwMessage("Success", "Data Publish table updated successfully");
            else Utility.throwMessage("Error", "Data Publish table could not be updated");
        }catch (Exception ex){
            Utility.throwMessage("SQL Error", ex.getMessage());
        }
    }

    private static int returnIdFromDatabase(String type) {
        try{
            String query = "select vrsta_objavljivanja_id from vrsta_objavljivanja where naziv = ?";
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            st.setString(1, type);
//            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery();
            int id = 0;
            if(rs.next())
                id = rs.getInt("vrsta_objavljivanja_id");
            else{
                String subquery = "select count(vrsta_objavljivanja_id) as 'IDS' from vrsta_objavljivanja;";
                Statement newSt = connection.createStatement();
                ResultSet newResultSet = newSt.executeQuery(subquery);
                if(newResultSet.next())
                    id = newResultSet.getInt("IDS") + 1;
            }
            insertNewTypeOfPublish(id, type);
            connection.commit();
            return id;
        }catch (Exception ex){
            Utility.throwMessage("SQL Error returnidfromdatabase", ex.getMessage());
        }
        return 0;
    }

    private static void insertNewTypeOfPublish(int id, String type) {
        try {
            String query = "call insert_vrsta_objavljivanja (?, ?)";
            PreparedStatement st = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            st.setInt(1, id);
            st.setString(2, type);
            int affectedRows = st.executeUpdate();
            if(affectedRows > 0)
                Utility.throwMessage("Success", "Type of publish table updated successfully");
            else Utility.throwMessage("Error", "Type of publish table could not be updated");
            connection.commit();
        }catch (Exception ex){
            Utility.throwMessage("SQL Error insertnewtype", ex.getMessage());
        }
    }

    public static void insertIntoTablePayments() {
        try{
            String query = "call new_proc(?)";
            PreparedStatement st = connection.prepareStatement(query);
            st.setInt(1, Psychotherapist.getInstance().getId());
//            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                String name = rs.getString("ime");
                String phoneNumber = rs.getString("broj_telefona");
//                String purpose = rs.getString("svrha");
                short installment = rs.getShort("rata");
                double price = rs.getDouble("iznos");
                String payment_method = rs.getString("nacin_placanja");
                Date dateofPayment = rs.getDate("datum_placanja");
                String currency = rs.getString("Valuta_valuta_id");
                double paymentPrice = rs.getDouble("Cena_za_uplatu");
                String overdue = rs.getString("prekoracenje");
                PaymentsAndDebtsPanel.addSession(new Object[]{name, phoneNumber, installment, price, payment_method, dateofPayment, currency, paymentPrice, overdue});
            }
        }catch (Exception ex){
            Utility.throwMessage("SQL Error", ex.getMessage());
        }
    }
}

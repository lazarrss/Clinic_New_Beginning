package org.example.utils;

import org.example.model.Psychotherapist;
import org.example.view.panels.PsychotherapistsOverviewPanel;

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
            String query = STR."select* from psihoterapeut where ime = '\{username}' and JMBG = '\{JMBG}'";
            setUser(query);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            return rs.next();
        }catch (Exception sqlException){
            Utility.throwMessage("SQL Error", sqlException.getMessage());
        }
        return false;
    }

    private static void setUser(String query) {
        try{
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
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
//                System.out.println(id);
//                System.out.println(name);
//                System.out.println(lastname);
//                System.out.println(UCIN);
//                System.out.println(DOB);
//                System.out.println(psychologist);
            }else Utility.throwMessage("Error", "Invalid user / no user found");

        } catch (SQLException e) {
            Utility.throwMessage("SQL Error", e.getMessage());
        }
    }

    public static void insertPsychotherapist(String name, String lastname, String UCIN, String DOB, String POR, String phoneNumber, String psychologist){
        try{
            String query = "insert into psihoterapeut(ime, prezime, JMBG, datum_rodjenja, prebivaliste, broj_telefona, psiholog)" +
                    "values (?, ?, ?, ?, ?, ?, ?)";
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
        }catch (Exception ex){
            Utility.throwMessage("Error", ex.getMessage());
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

    public static void updateUserDatabase(String name, String lastname, String UCIN, Date DOB, String POR, String phoneNumber, short psychologist) {
        try{
//            String query = "update psihoterapeut " +
//                    "set name = '"+name+"', prezime = '"+lastname+"', JMBG = '"+UCIN+"', datum_rodjenja = '"+DOB+"', prebivaliste = '"+POR+"'," +
//                    "broj_telefona = '"+phoneNumber+"', psiholog = '"+psychologist+"'" +
//                    "where psihoterapeut_id = "+Psychotherapist.getInstance().getId();
//            String query = STR."update psihoterapeut set name = '\{name}', prezime = '\{lastname}', JMBG = '\{UCIN}', datum_rodjenja = '\{DOB}', prebivaliste = '\{POR}',broj_telefona = '\{phoneNumber}', psiholog = '\{psychologist}'where psihoterapeut_id = \{Psychotherapist.getInstance().getId()}";
//            Statement ps = connection.createStatement();
//            ResultSet rs = ps.executeQuery(query);
//            if(rs.next())
//                Utility.throwMessage("Success", "Profile updated successfully");
//            else Utility.throwMessage("Error", "Profile could not be updated");
            String sql = "UPDATE psihoterapeut SET ime = ?, prezime = ?, JMBG = ?, datum_rodjenja = ?, prebivaliste = ?, broj_telefona = ?, psiholog = ? WHERE psihoterapeut_id = '"+Psychotherapist.getInstance().getId()+"'";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, name);
            statement.setString(2, lastname);
            statement.setString(3, UCIN);
            statement.setDate(4, DOB);
            statement.setString(5, POR);
            statement.setString(6, phoneNumber);
            statement.setShort(7, psychologist);
            int affectedRows = statement.executeUpdate();
            if(affectedRows > 0)
                Utility.throwMessage("Success", "Profile updated successfully");
            else Utility.throwMessage("Error", "Profile could not be updated");
        }catch (Exception ex){
            Utility.throwMessage("SQL Error", ex.getMessage());
        }
    }
}

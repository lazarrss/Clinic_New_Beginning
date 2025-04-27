package org.example.Utils;

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
        String query = STR."select JMBG, ime from psihoterapeut where ime = '\{username}' and JMBG = '\{JMBG}'";
        try{
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            return rs.next();
        }catch (Exception sqlException){
            Utility.throwMessage("SQL Error", sqlException.getMessage());
        }
        return false;
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
}

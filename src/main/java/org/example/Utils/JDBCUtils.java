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
            throw new RuntimeException(e);
        }
    }
    public static boolean existsInDatabase(String username, String JMBG){
        connect();
        String query = STR."select JMBG, ime from psihoterapeut where ime = '\{username}' and JMBG = '\{JMBG}'";
        try{
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            if(rs.next()) return true;
            return false;
        }catch (Exception sqlException){
            Utility.throwException("SQL Error", "Unknown error");
        }
        return false;
    }
}

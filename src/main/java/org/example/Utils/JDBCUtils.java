package org.example.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
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
}

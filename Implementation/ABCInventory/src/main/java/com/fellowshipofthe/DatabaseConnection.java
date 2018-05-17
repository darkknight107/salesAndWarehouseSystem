package com.fellowshipofthe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Connection connect() throws SQLException, ClassNotFoundException {
        Connection conn= null;

        String url= "jdbc::mysql://sqlserverabc-inventory-database.cotussqxqzdd.ap-southeast-2.rds.amazonaws.com: 3306";
        String username="abcinventory";
        String password="abcinventory";

        Class.forName("com.mysql.jdbc.Driver");
        conn= DriverManager.getConnection("jdbc:mysql://" +
                "abc-inventory-database.cotussqxqzdd.ap-southeast-2.rds.amazonaws.com:3306/abcinventorydatabase?user=abcinventory&password=abcinventory");
        System.out.println("Connected to database!");

        return conn;
    }
}

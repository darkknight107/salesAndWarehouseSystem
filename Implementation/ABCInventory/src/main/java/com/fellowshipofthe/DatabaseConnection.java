package com.fellowshipofthe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Connection connect() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException{
        Connection conn= null;

        Class.forName("com.mysql.jdbc.Driver").newInstance();
        conn= DriverManager.getConnection("jdbc:mysql://" +
                "abc-inventory-database.cotussqxqzdd.ap-southeast-2.rds.amazonaws.com:3306/abcinventorydatabase?user=abcinventory&password=abcinventory&useSSL=false");
        System.out.println("Connected to database!");

        return conn;
    }
}

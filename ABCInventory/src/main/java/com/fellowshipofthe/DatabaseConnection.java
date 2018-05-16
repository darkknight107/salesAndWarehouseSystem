package com.fellowshipofthe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public Connection connect() throws SQLException, ClassNotFoundException {
        Connection conn= null;

        String url= "jdbc:sqlserverabc-inventory-database.cotussqxqzdd.ap-southeast-2.rds.amazonaws.com: 3306";
        String username="abcinventory";
        String password="abcinventory";

        Class.forName("com.mysql.jdbc.Driver");
        conn= DriverManager.getConnection(url,username,password);
        System.out.println("Connected to database!");

        return conn;
    }
}

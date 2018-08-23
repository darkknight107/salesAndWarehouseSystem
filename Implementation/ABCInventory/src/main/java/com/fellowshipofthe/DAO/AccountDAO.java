package com.fellowshipofthe.DAO;

import com.fellowshipofthe.DatabaseConnection;
import com.fellowshipofthe.entityClasses.SearchAccount;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class AccountDAO {
    private static DatabaseConnection dbconnet;
    private static Connection conn;


    public AccountDAO() {}


    public Boolean  searchAccount(String username, String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
            dbconnet = new DatabaseConnection();
            conn = dbconnet.connect();
            System.out.println(username + password);
            String sql = " SELECT Staff.userName, \n" +
                    "   Staff.pWord \n" +
                    "FROM Staff \n" +
                    "WHERE Staff.userName = \"" + username + "\" AND Staff.pWord =  \"" + password + "\";";
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);

            if (resultSet.next()) {
                conn.close ( );
                stmt.close ( );
                System.out.println ("matched");
                return true;
            }
            else{
                conn.close ();
                stmt.close ();
                System.out.println ("not found");
                return false;
            }


    }


}


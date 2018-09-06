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


    public String  searchAccount(String username, String password) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String locationID = null;
        dbconnet = new DatabaseConnection();
        conn = dbconnet.connect();
        System.out.println(username + password);
        String sql = " SELECT Staff.userName, \n" +
                "   Staff.pWord, \n" +
                " Staff.locationID \n" +
                "FROM Staff \n" +
                "WHERE Staff.userName = \"" + username + "\" AND Staff.pWord =  \"" + password + "\";";
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(sql);

        if (resultSet.next()) {

            System.out.println ("matched");
            SearchAccount searchAccount = new SearchAccount ();
            searchAccount.setLocationID (resultSet.getString (3));
            locationID = searchAccount.getLocationID ();
            System.out.println (searchAccount.getLocationID ());
            conn.close ();
            stmt.close ();
            return locationID;

        }
        else{
            System.out.println ("not found");
            return locationID;
        }


    }


}

package com.fellowshipofthe.DAO;

import com.fellowshipofthe.DatabaseConnection;
import com.fellowshipofthe.entityClasses.SearchAccount;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class AccountDAO {
    private static List<SearchAccount> userAccounts;
    private static DatabaseConnection dbconnet;
    private static Connection conn;


    public AccountDAO() {userAccounts = new ArrayList<>();}


    public static List<SearchAccount>  searchAccount(String username, String password) {
        try {
            dbconnet = new DatabaseConnection();
            conn = dbconnet.connect();
            String sql = " SELECT USER.username, \n" +
                    "   AND USER.password. \n" +
                    "FROM user \n" ;
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                SearchAccount userAccount = new SearchAccount();
                userAccount.setUsername(resultSet.getString(2));
                userAccount.setPassword(resultSet.getString(4));

                userAccounts.add(userAccount);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Always make sure result sets and statements are closed,
            // and the connection is returned to the pool
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException ignore) {
            }
        }
        return userAccounts;
    }


}


package com.fellowshipofthe.DAO;

import com.fellowshipofthe.DatabaseConnection;
import com.fellowshipofthe.entityClasses.Staff;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StaffDAO {
    //variables to store staff details
    String userName;
    String password;
    String firstName;
    String middleName;
    String lastName;
    String locationID;
    String contact;
    String dateOfBirth;
    String address;
    String email;
    //jdbc variables
    DatabaseConnection dbconnet;
    Connection conn;
    Statement stmt;

    public String addStaff(Staff newStaff) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        userName= newStaff.getUserName();
        password= newStaff.getPassword();
        firstName= newStaff.getFirstName();
        middleName= newStaff.getMiddleName();
        lastName= newStaff.getLastName();
        locationID= newStaff.getLocationID();
        contact= newStaff.getContact();
        dateOfBirth= newStaff.getDateOfBirth();
        address= newStaff.getAddress();
        email= newStaff.getEmail();

        //opening a connection with the database and creating a statement
        dbconnet = new DatabaseConnection();
        conn = dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sqlQueryToCheckUName= "SELECT userName FROM Staff WHERE userName= \"" + userName + "\"";
        ResultSet resultSet= stmt.executeQuery(sqlQueryToCheckUName);
        if(resultSet.next()){
            conn.close();
            stmt.close();
            System.out.println("Username already exists!");
            return "exists";
        }
        else{
            String sql= "Insert into Staff(userName, pWord, firstName, middleName, lastName," +
                    "locationID, contact, dateOfBirth, address, email)" +
                    "values(\"" + userName + "\", sha2(\"" + password + "\", 512), \"" + firstName + "\", \" \n" +
                     middleName + "\", \"" + lastName + "\", \"" + locationID + "\", \"" + contact + "\", \" \n" +
                     dateOfBirth + "\", \"" + address + "\", \"" + email + "\");";
            int result= stmt.executeUpdate(sql);
            if (result > 0){
                conn.close();
                stmt.close();
                System.out.println("Staff Added!");
                return "true";
            }
            else{
                System.out.println("Error! Staff could not be added!");
                return "false";
            }

        }

    }
}

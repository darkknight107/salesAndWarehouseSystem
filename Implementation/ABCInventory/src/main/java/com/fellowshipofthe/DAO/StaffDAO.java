package com.fellowshipofthe.DAO;

import com.fellowshipofthe.DatabaseConnection;
import com.fellowshipofthe.entityClasses.Product;
import com.fellowshipofthe.entityClasses.Staff;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
    List<Staff> staffSearchResult;

    //initializing list through constructor to avoid null pointer exception
    public StaffDAO(){
        staffSearchResult= new ArrayList<>();

    }

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

    public Boolean deleteStaff(String userName) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "delete from Staff where userName=\""+ userName + "\";";
        stmt.executeUpdate(sql);
        conn.close();
        stmt.close();
        System.out.println("Staff Deleted!");
        return true;
    }

    public List<Staff> viewAllStaff() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String sql= "select userName, firstName, lastName, locationID, contact, dateOfBirth, address, email\n" +
                "from Staff;";
        processSQL(sql);
        return staffSearchResult;
    }

    public List<Staff> searchStaff(String name) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String sql= "select userName, firstName, lastName, locationID, contact, dateOfBirth, address, email \n" +
                "from Staff \n" +
                "where userName= \"" + name + "\" or firstName= \"" + name + "\" or  lastName=\"" + name + "\";";
        processSQL(sql);
        return staffSearchResult;
    }

    public void processSQL(String sqlQuery) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        dbconnet = new DatabaseConnection();
        conn = dbconnet.connect();
        stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(sqlQuery);
       // System.out.println(resultSet.getString(1));
        while (resultSet.next()){
            Staff searchedStaff= new Staff();
            searchedStaff.setUserName(resultSet.getString(1));
            //searchedStaff.setMiddleName("");
            //searchedStaff.setPassword("");
            searchedStaff.setFirstName(resultSet.getString(2));
            searchedStaff.setLastName(resultSet.getString(3));
            searchedStaff.setLocationID(resultSet.getString(4));
            searchedStaff.setContact(resultSet.getString(5));
            searchedStaff.setDateOfBirth(resultSet.getString(6));
            searchedStaff.setAddress(resultSet.getString(7));
            searchedStaff.setEmail(resultSet.getString(8));
            staffSearchResult.add(searchedStaff);
        }
        conn.close();
        stmt.close();
    }

    //allowing users to update staff details using userName(not allowing users to change productCode)
    public Boolean updateStaff(Staff updatedStaff) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "UPDATE Staff \n" +
                "SET firstName = \"" + updatedStaff.getFirstName() + "\", lastName = \"" + updatedStaff.getLastName() + "\",\n" +
                "locationID= \"" + updatedStaff.getLocationID() + "\", contact= \""+ updatedStaff.getContact() + "\", \n" +
                "dateOfBirth= \""+ updatedStaff.getDateOfBirth()+ " \", address= \"" + updatedStaff.getAddress()+ "\", \n" +
                "email = \"" + updatedStaff.getEmail() + "\" WHERE userName= \"" + updatedStaff.getUserName() + "\"; ";
        int i= stmt.executeUpdate(sql);
        conn.close();
        stmt.close();
        if (i > 0){
            System.out.println("Staff updated!");
            return true;
        }
        else{
            System.out.println("Error! Staff could not be updated!");
            return false;
        }
    }
}

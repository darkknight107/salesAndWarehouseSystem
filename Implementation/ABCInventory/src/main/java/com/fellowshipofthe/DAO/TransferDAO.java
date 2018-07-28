package com.fellowshipofthe.DAO;

import com.fellowshipofthe.DatabaseConnection;
import com.fellowshipofthe.entityClasses.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TransferDAO {

    DatabaseConnection dbconnet = new DatabaseConnection();
    Connection conn;
    Statement stmt;
    List<StoredProduct> storedProducts;

    public TransferDAO(){
        storedProducts = new ArrayList<StoredProduct>();
    }

    public Boolean addTransfer(List<Transfer> transferList) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        //opening a connection with the database and creating a statement
        conn = dbconnet.connect();
        stmt= conn.createStatement();

        for (Transfer t : transferList) {
            String sqlAddTransfer= "INSERT into Transfer (sendingLocationID, destinationLocationID, transferDate, status, description)" +
                    " VALUES (\"" + t.getSendingLocationID() +"\",\""+ t.getDestinationLocationID()+ "\",\"" + t.getTransferDate() + "\",\""+ t.getStatus() + "\",\"" + t.getDescription() +"\");";
            stmt.executeUpdate(sqlAddTransfer);
        }

        stmt.close();
        conn.close();
        System.out.println("Transfers Added!");
        return true;
    }

    public String getCurrentTransferID() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        //opening a connection with the database and creating a statement
        conn = dbconnet.connect();
        stmt= conn.createStatement();
        String transferID="";
        String sqlGetCurrentTransferID = "SELECT `AUTO_INCREMENT`" +
                "FROM  INFORMATION_SCHEMA.TABLES" +
        "WHERE TABLE_SCHEMA = 'abcinventorydatabase'" +
        "AND   TABLE_NAME   = 'Transfer';";
        ResultSet resultSet = stmt.executeQuery(sqlGetCurrentTransferID);
        while (resultSet.next()) {
            transferID = resultSet.getString(1);
        }
        conn.close();
        stmt.close();
        return transferID;
    }

    public Boolean addTransferItem(List<TransferItem> transferList) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        //opening a connection with the database and creating a statement
        conn = dbconnet.connect();
        stmt= conn.createStatement();
        int transferID = Integer.parseInt(getCurrentTransferID()) - 1;

        for (TransferItem ti : transferList) {
            String sqlAddTransferItem= "INSERT into TransferItem" +
                    " VALUES (\"" + transferID +"\",\""+ ti.getProductItemCode()+ "\",\"" + ti.getProductQuantity() +"\");";
            stmt.executeUpdate(sqlAddTransferItem);
        }

        stmt.close();
        conn.close();
        System.out.println("Transfers Item Added!");
        return true;
    }

    public List<StoredProduct> viewAllStoredProducts() {

        String viewAllStoredProductsSqlQuery = "SELECT * from StoredProduct;";

        executeSearchStoredProductSQLQueries(viewAllStoredProductsSqlQuery);

        return storedProducts;

    }

    public List<StoredProduct> searchStoredProduct(String productItemCode) {

        String searchStoredProductsSqlQuery = " SELECT * from StoredProduct WHERE productItemCode = \"" + productItemCode +"\";";

        executeSearchStoredProductSQLQueries(searchStoredProductsSqlQuery);

        return storedProducts;

    }

    public List<StoredProduct> searchStoredProductByLocation(String locationID) {

        String searchStoredProductsSqlQuery = " SELECT * from StoredProduct WHERE locationID = \"" + locationID +"\";";

        executeSearchStoredProductSQLQueries(searchStoredProductsSqlQuery);

        return storedProducts;

    }

    public void executeSearchStoredProductSQLQueries(String sqlQuery){
        try {
            dbconnet = new DatabaseConnection();
            conn = dbconnet.connect();
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            while (resultSet.next()) {
                StoredProduct storedProduct = new StoredProduct();
                storedProduct.setProductItemCode(resultSet.getString(1));
                storedProduct.setLocationID(resultSet.getString(2));
                storedProduct.setProductQuantity(resultSet.getString(3));

                storedProducts.add(storedProduct);
            }
        }catch(SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException  e){
            e.printStackTrace();
        } finally
        {
            // Always make sure result sets and statements are closed,
            // and the connection is returned to the pool
            try
            {
                if (conn != null)
                    conn.close ();
                if (stmt != null)
                    stmt.close();
            }
            catch (SQLException ignore)
            {
            }
        }
    }

    public String updateTransferItemsQuantity(List<TransferItem> transferList) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        int i = 0;
        for (TransferItem ti : transferList){
            String sql= "UPDATE StoredProduct \n" +
                    "SET productQuantity = productQuantity - \"" + ti.getProductQuantity() + "\""+ "\n" +
                    "WHERE productItemCode= \"" + ti.getProductItemCode() + "\"" + " AND locationID = \"" + ti.getLocationID() +"\"; ";
            i = stmt.executeUpdate(sql);
        }
        conn.close();
        stmt.close();
        if (i > 0){
            System.out.println("Product Item updated!");
            return "updated";
        }
        else{
            System.out.println("Error! Product Item could not be updated!");
            return "fail";
        }
    }

}

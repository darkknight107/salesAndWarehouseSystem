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
    List<Transfer> transferList;
    List<TransferItem> transferItemList;

    //Constructor without any elements
    public TransferDAO(){
        storedProducts = new ArrayList<StoredProduct>();
        transferList = new ArrayList<Transfer>();
        transferItemList = new ArrayList<TransferItem>();
    }

    //add Transfer to database
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

    //get the current transfer ID for adding
    public String getCurrentTransferID() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        //opening a connection with the database and creating a statement
        conn = dbconnet.connect();
        stmt= conn.createStatement();
        String transferID="";
        String sqlGetCurrentTransferID = "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'abcinventorydatabase' AND TABLE_NAME = 'Transfer';";
        ResultSet resultSet = stmt.executeQuery(sqlGetCurrentTransferID);
        resultSet.beforeFirst();
        resultSet.next();
        transferID = resultSet.getString(1);

        conn.close();
        stmt.close();
        return transferID;
    }

    // add transfer item to database
    public Boolean addTransferItem(List<StoredProduct> storedProducts) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {



        int transferID = Integer.parseInt(getCurrentTransferID()) - 1;
        System.out.println(transferID);

        for (StoredProduct st : storedProducts) {
            //opening a connection with the database and creating a statement
            conn = dbconnet.connect();
            stmt= conn.createStatement();

            String sqlAddTransferItem= "INSERT into TransferItem" +
                    " VALUES (\"" + transferID +"\",\""+ st.getProductItemCode()+ "\",\"" + st.getProductQuantity() +"\");";
            stmt.executeUpdate(sqlAddTransferItem);

            stmt.close();
            conn.close();
        }
        System.out.println("Transfers Item Added!");
        return true;
    }

    // display all stored products
    public List<StoredProduct> viewAllStoredProducts() {

        String viewAllStoredProductsSqlQuery = "SELECT * from StoredProduct;";

        executeSearchStoredProductSQLQueries(viewAllStoredProductsSqlQuery);

        return storedProducts;

    }

    // search stored product by product item code
    public List<StoredProduct> searchStoredProduct(String productItemCode) {

        String searchStoredProductsSqlQuery = " SELECT * from StoredProduct WHERE productItemCode = \"" + productItemCode +"\";";

        executeSearchStoredProductSQLQueries(searchStoredProductsSqlQuery);

        return storedProducts;

    }

    // search stored product by location id
    public List<StoredProduct> searchStoredProductByLocation(String locationID) {

        String searchStoredProductsSqlQuery = " SELECT * from StoredProduct WHERE locationID = \"" + locationID +"\";";

        executeSearchStoredProductSQLQueries(searchStoredProductsSqlQuery);

        return storedProducts;

    }

    // search stored product by combination codes
    public List<StoredProduct> searchStoredProductByCombinationCodes(String locationID, String productItemCode) {

        String searchStoredProductsSqlQuery = "SELECT * from StoredProduct WHERE locationID = \"" + locationID +"\" AND productItemCode = \"" + productItemCode + "\";";

        executeSearchStoredProductSQLQueries(searchStoredProductsSqlQuery);

        return storedProducts;

    }

    // execute all the sql query for Stored Products
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

    // update transfer item quantity after sent products
    public String updateTransferItemsQuantitySend(List<StoredProduct> storedProducts) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        int i = 0;
        for (StoredProduct st : storedProducts){
            String sql= "UPDATE StoredProduct \n" +
                    "SET productQuantity = productQuantity - \"" + st.getProductQuantity() + "\""+ "\n" +
                    "WHERE productItemCode= \"" + st.getProductItemCode() + "\"" + " AND locationID = \"" + st.getLocationID() +"\"; ";
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

    // display all transfer
    public List<Transfer> viewAllTransfer() {

        String viewAllTransferSqlQuery = "SELECT * from Transfer;";

        executeSearchTransferSQLQueries(viewAllTransferSqlQuery);

        return transferList;

    }

    // display all transfer
    public List<Transfer> searchTransfer(String transferID) {

        String searchTransferSqlQuery = "SELECT * from Transfer WHERE transferID = \"" + transferID + "\";";

        executeSearchTransferSQLQueries(searchTransferSqlQuery);

        return transferList;

    }

    // display sending transfer
    public List<Transfer> displaySendingTransfer() {
        String status = "Sending";
        String displaySendingTransferSqlQuery = "SELECT * from Transfer WHERE status = \"" + status +"\";";

        executeSearchTransferSQLQueries(displaySendingTransferSqlQuery);

        return transferList;

    }

    // search sending transfer
    public List<Transfer> searchSendingTransfer(String transferID) {
        String status = "Sending";
        String searchSendingTransferSqlQuery = "SELECT * from Transfer WHERE status = \"" + status + "\" AND  transferID = \"" + transferID + "\";";

        executeSearchTransferSQLQueries(searchSendingTransferSqlQuery);

        return transferList;
    }


    // display sending transfer item
    public List<TransferItem> displaySendingTransferItem(String transferID) {
        String displaySendingTransferItemSqlQuery = "SELECT * from TransferItem WHERE transferID = \"" + transferID +"\";";

        executeSearchTransferItemSQLQueries(displaySendingTransferItemSqlQuery);

        return transferItemList;

    }

    // update stored product quantity after accept products
    public String updateTransferItemsQuantityAccept(List<StoredProduct> storedProducts) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        int i = 0;
        for (StoredProduct st : storedProducts){
            String sql= "UPDATE StoredProduct \n" +
                    "SET productQuantity = productQuantity + \"" + st.getProductQuantity() + "\""+ "\n" +
                    "WHERE productItemCode= \"" + st.getProductItemCode() + "\"" + " AND locationID = \"" + st.getLocationID() +"\"; ";
            i = stmt.executeUpdate(sql);
        }
        conn.close();
        stmt.close();
        if (i > 0){
            System.out.println("Stored Product updated!");
            return "updated";
        }
        else{
            System.out.println("Error! Stored Product could not be updated!");
            return "fail";
        }
    }

    // update transfer after sent products
    public String updateTransferStatusAccept(String transferID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        String status = "Accepted";
        int i = 0;
        String sql= "UPDATE Transfer \n" +
                "SET status = \"" + status + "\""+ "\n" +
                "WHERE transferID= \"" + transferID +"\"; ";
        i = stmt.executeUpdate(sql);
        conn.close();
        stmt.close();
        if (i > 0){
            System.out.println("Transfer updated");
            return "updated";
        }
        else{
            System.out.println("Error! Transfer could not be updated!");
            return "fail";
        }
    }

    // execute all the sql query for Transfer
    public void executeSearchTransferSQLQueries(String sqlQuery){
        try {
            dbconnet = new DatabaseConnection();
            conn = dbconnet.connect();
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            while (resultSet.next()) {
                Transfer transfer = new Transfer();
                transfer.setTransferID(resultSet.getString(1));
                transfer.setSendingLocationID(resultSet.getString(2));
                transfer.setDestinationLocationID(resultSet.getString(3));
                transfer.setTransferDate(resultSet.getString(4));
                transfer.setStatus(resultSet.getString(5));
                transfer.setDescription(resultSet.getString(6));

                transferList.add(transfer);
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

    // execute all the sql query for Stored Products
    public void executeSearchTransferItemSQLQueries(String sqlQuery){
        try {
            dbconnet = new DatabaseConnection();
            conn = dbconnet.connect();
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            while (resultSet.next()) {
                TransferItem transferItem = new TransferItem();
                transferItem.setTransferID(resultSet.getString(1));
                transferItem.setProductItemCode(resultSet.getString(2));
                transferItem.setProductQuantity(resultSet.getString(3));

                transferItemList.add(transferItem);
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

}

package com.fellowshipofthe.DAO;

import com.fellowshipofthe.DatabaseConnection;
import com.fellowshipofthe.entityClasses.Transfer;
import com.fellowshipofthe.entityClasses.TransferItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TransferDAO {

    DatabaseConnection dbconnet;
    Connection conn;
    Statement stmt;

    public Boolean addTransfer(List<Transfer> transferList) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        //opening a connection with the database and creating a statement
        dbconnet = new DatabaseConnection();
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

    public Boolean addTransferItem(List<TransferItem> transferList) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {

        //opening a connection with the database and creating a statement
        dbconnet = new DatabaseConnection();
        conn = dbconnet.connect();
        stmt= conn.createStatement();

        for (TransferItem ti : transferList) {
            String sqlAddTransferItem= "INSERT into TransferItem" +
                    " VALUES (\"" + ti.getTransferID() +"\",\""+ ti.getProductItemCode()+ "\",\"" + ti.getProductQuantity() +"\");";
            stmt.executeUpdate(sqlAddTransferItem);
        }

        stmt.close();
        conn.close();
        System.out.println("Transfers Item Added!");
        return true;
    }

}

package com.fellowshipofthe.transferProduct;

import com.fellowshipofthe.DAO.TransferDAO;
import com.fellowshipofthe.entityClasses.Transfer;
import com.fellowshipofthe.entityClasses.TransferItem;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("transferproduct")
public class TransferResource {
    TransferDAO transferDAO = new TransferDAO();

    @POST
    @Path("/addtransfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean addTransfer(List<Transfer> newTransfer) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addTransfer called!");
        return transferDAO.addTransfer(newTransfer);
    }

    @POST
    @Path("/addtransferitem")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean addTransferItem(List<TransferItem> newTransferItem) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addTransferItem called!");
        return transferDAO.addTransferItem(newTransferItem);
    }
}

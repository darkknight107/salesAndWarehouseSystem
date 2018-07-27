package com.fellowshipofthe.transferProduct;

import com.fellowshipofthe.DAO.ProductDAO;
import com.fellowshipofthe.DAO.TransferDAO;
import com.fellowshipofthe.entityClasses.SearchProduct;
import com.fellowshipofthe.entityClasses.StoredProduct;
import com.fellowshipofthe.entityClasses.Transfer;
import com.fellowshipofthe.entityClasses.TransferItem;

import javax.ws.rs.*;
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
        if(transferDAO.addTransferItem(newTransferItem)) {
            transferDAO.updateTransferItemsQuantity(newTransferItem);
            return true;
        }else{
            return false;
        }
    }

    // Client calls the view all stored products
    @GET
    @Path("/viewallstoredproducts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StoredProduct> viewAllStoredProducts() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("view all stored products called!");

        return transferDAO.viewAllStoredProducts();

    }

    // Client calls the search product
    @GET
    @Path("/searchstoredproducts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StoredProduct>searchStoredProduct(@QueryParam("productitemcode") String productItemCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println(" product called!");

        return transferDAO.searchStoredProduct(productItemCode);

    }
}

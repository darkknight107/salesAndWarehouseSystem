package com.fellowshipofthe.transferProduct;

import com.fellowshipofthe.DAO.TransferDAO;
import com.fellowshipofthe.entityClasses.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("transferproduct")
public class TransferProductResources {
    TransferDAO transferDAO = new TransferDAO();

    // add new Transfer
    @POST
    @Path("/addtransfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean addTransfer(List<Transfer> newTransfer) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addTransfer called!");
        return transferDAO.addTransfer(newTransfer);
    }

    // add new transfer item
    @POST
    @Path("/addtransferitem")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean addTransferItem(List<StoredProduct> storedProducts) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addTransferItem called!");
        return transferDAO.addTransferItem(storedProducts);
    }

    // Client calls the view all stored products
    @GET
    @Path("/viewallstoredproducts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StoredProduct> viewAllStoredProducts() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("view all stored products called!");

        return transferDAO.viewAllStoredProducts();

    }

    // Client calls the search stored product
    @GET
    @Path("/searchstoredproducts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StoredProduct>searchStoredProduct(@QueryParam("productitemcode") String productItemCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("search product called!");

        return transferDAO.searchStoredProduct(productItemCode);

    }

    // Client calls the search stored product by location id
    @GET
    @Path("/searchstoredproductsbylocation")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StoredProduct>searchStoredProductByLocation(@QueryParam("locationID") String locationID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("search product by location called!");

        return transferDAO.searchStoredProductByLocation(locationID);

    }

    // Client calls the search stored product by combination codes
    @GET
    @Path("/searchstoredproductsbycominationcodes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<StoredProduct>searchStoredProductByCombinationCodes(@QueryParam("locationID") String locationID, @QueryParam("productItemCode") String productItemCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("search product by combination code called!");

        return transferDAO.searchStoredProductByCombinationCodes(locationID,productItemCode);

    }

    // update product quantity after sent products
    @Path("/updateproductquantity")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTransferItemsQuantity(List<StoredProduct> storedProducts) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("updateProduct called!");
        return transferDAO.updateTransferItemsQuantitySend(storedProducts);
    }

    // Client calls the display sending transfers
    @GET
    @Path("/displaysendingtransfer")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transfer> displaySendingTransfer(@QueryParam("destinationLocationID") String destinationLocationID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("display sending transfers called!");

        return transferDAO.displaySendingTransfer(destinationLocationID);
    }

    // Client calls the display sending transfer item
    @GET
    @Path("/searchsendingtransfer")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transfer>searchSendingTransfer(@QueryParam("transferID") String transferID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("search sending transfer called!");

        return transferDAO.searchSendingTransfer(transferID);

    }

    // Client calls the display sending transfer item
    @GET
    @Path("/displaysendingtransferitem")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TransferItem>displaySendingTransferItem(@QueryParam("transferID") String transferID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("display sending transfer item called!");

        return transferDAO.displaySendingTransferItem(transferID);

    }

    // Client calls the display sending transfer item
    @GET
    @Path("/viewalltransfer")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transfer>viewAllTransfer() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("display sending transfer item called!");

        return transferDAO.viewAllTransfer();

    }

    // Client calls the display sending transfer item
    @GET
    @Path("/searchTransfer")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transfer>searchTransfer(@QueryParam("transferID") String transferID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("display sending transfer item called!");

        return transferDAO.searchTransfer(transferID);
    }

    // Client calls the filter datetime for transfer
    @GET
    @Path("/searchtransferbydatetimerange")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transfer>searchTransferByDatetimeRange(@QueryParam("fromdate") String fromDate, @QueryParam("todate") String toDate) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("search transfer by datetime range called!");

        return transferDAO.searchTransferByDatetimeRange(fromDate,toDate);
    }

    // Client calls the filter datetime for searching transfer
    @GET
    @Path("/searchselectedtransferbydatetimerange")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transfer>searchSelectedTransferByDatetimeRange(@QueryParam("fromdate") String fromDate, @QueryParam("todate") String toDate, @QueryParam("transferid") String transferID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

        System.out.println("search selected transfer by datetime range called!");

        return transferDAO.searchSelectedTransferByDatetimeRange(fromDate,toDate,transferID);
    }

        // update stored product quantity after accept products
    @Path("/updatetransferitemquantityaccept")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTransferItemsQuantityAccept(List<StoredProduct> storedProducts) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("update Transfer Item quantity accept called!");
        return transferDAO.updateTransferItemsQuantityAccept(storedProducts);
    }

    // update transfer status after accept product
    @Path("/updatetransferstatusaccept")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTransfersStatusAccept(String transferID) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("update Transfer status called!");
        System.out.println(transferID);
        return transferDAO.updateTransferStatusAccept(transferID);
    }

    //resource for adding stored product
    @POST
    @Path("/addstoredproduct")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addStoredProduct(List<StoredProduct> storedProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addStoredProduct called!");
        return transferDAO.addStoredProduct(storedProduct);
    }
}

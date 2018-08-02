package com.fellowshipofthe.manageProduct;

import com.fellowshipofthe.DAO.ProductDAO;
import com.fellowshipofthe.entityClasses.Product;
import com.fellowshipofthe.entityClasses.ProductItem;
import com.fellowshipofthe.entityClasses.StoredProduct;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;


@Path("update")
public class EditProductResource {

    @Path("updateproduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateProduct(Product updatedProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        ProductDAO productDAO= new ProductDAO();
        System.out.println("updateProduct called!");
        return productDAO.updateProduct(updatedProduct);
    }

    @Path("updatestoredproduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateStoredProduct(StoredProduct updatedStoredProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        ProductDAO productDAO= new ProductDAO();
        System.out.println("updateStoredProduct called!");
        return productDAO.updateStoredProduct(updatedStoredProduct);
    }

}

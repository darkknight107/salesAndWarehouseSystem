package com.fellowshipofthe.manageProduct;

import com.fellowshipofthe.DAO.ProductDAO;
import com.fellowshipofthe.entityClasses.Product;
import com.fellowshipofthe.entityClasses.ProductItem;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;


@Path("update")
public class EditProductResource {
    ProductDAO productDAO= new ProductDAO();
    @Path("updateProduct")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateProduct(Product updatedProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("updateProduct called!");
        return productDAO.updateProduct(updatedProduct);
    }

}

package com.fellowshipofthe.manageProduct;

import com.fellowshipofthe.searchProduct.ProductDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("manageproduct")
public class AddProductResource {
    ProductDAO product = new ProductDAO();

    @POST
    @Path("addproduct/{productCode}/{productName}/{price}/{description}/")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public String addProduct(@FormParam("productCode") String productCode,
                             @FormParam("productName") String productName,
                             @FormParam("price") String price,
                             @FormParam("description") String description) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addProduct called!");
        return product.addProduct(productCode, productName, price, description);
    }
}

package com.fellowshipofthe.manageProduct;

import com.fellowshipofthe.entityClasses.Product;
import com.fellowshipofthe.entityClasses.ProductItem;
import com.fellowshipofthe.entityClasses.StoredProduct;
import com.fellowshipofthe.DAO.ProductDAO;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;

@Path("manageproduct")
public class AddProductResource {
    ProductDAO productDAO = new ProductDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addProduct(Product newProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addProduct called!");
        //calling productDAO to connect with database and insert new product
        return productDAO.addProduct(newProduct);
    }

    //resource for adding productItem
    @POST
    @Path("addproductitem")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addProductItem(ProductItem newProductItem) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addProductItem called!");
        return productDAO.addProductItem(newProductItem);
    }

    //resource for adding stored product (allocating product to location)
    @POST
    @Path("addstoredproduct")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean addStoredProduct(StoredProduct newStoredProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addStoredProduct called!");
        return productDAO.addStoredProduct(newStoredProduct);
    }


}

package com.fellowshipofthe.manageProduct;

import com.fellowshipofthe.entityClasses.Product;
import com.fellowshipofthe.searchProduct.SearchProductDAO;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Path("manageproduct")
public class AddProductResource {
    SearchProductDAO productDAO = new SearchProductDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean addProduct(Product newProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addProduct called!");
        //creating variables to store new product details
        String newCode= newProduct.getProductCode();
        String newName= newProduct.getProductName();
        String newPrice= newProduct.getPrice();
        String newDescription= newProduct.getDescription();
        //creating and array list to store each String value of the product details
        ArrayList<String> newProductList = new ArrayList<>();
        //adding new product details to the arraylist
        newProductList.add(newCode);
        newProductList.add(newName);
        newProductList.add(newPrice);
        newProductList.add(newDescription);
        //check if arraylist items are valid
        System.out.println(newProductList.get(0) + newProductList.get(1) + newProductList.get(2) + newProductList.get(3));
        //calling productDAO to connect with database and insert new product
        return productDAO.addProduct(newProductList);
    }
}

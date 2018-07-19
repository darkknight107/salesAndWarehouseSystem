package com.fellowshipofthe.searchProduct;

import com.fellowshipofthe.DAO.ProductDAO;
import com.fellowshipofthe.entityClasses.SearchProduct;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("searchproduct")
public class SearchProductResource{

    // Client calls the search product
    @GET
    @Path("/searchproductcode")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SearchProduct>searchProduct(@QueryParam("productcode") String productCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ProductDAO productDAO = new ProductDAO();

        System.out.println(" product called!");

        return productDAO.searchProduct(productCode);

    }



    // Client calls the view product details by location id
    @GET
    @Path("/viewproductitem")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SearchProduct> viewProductItem(@QueryParam("productcode") String productItemCode, @QueryParam("locationID") String locationID) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ProductDAO productDAO = new ProductDAO();

        System.out.println("view product items called!");

        return productDAO.viewProductItem(productItemCode,locationID);

    }

    // Client calls the view all products
    @GET
    @Path("/viewallproducts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SearchProduct> viewAllProducts() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ProductDAO productDAO = new ProductDAO();

        System.out.println("view all products called!");

        return productDAO.viewAllProducts();

    }

    // Client calls search product items in a specific product code
    @GET
    @Path("/searchProductItemsInProductCode")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SearchProduct> searchProductItemsInProductCode(@QueryParam("productcode") String productCode, @QueryParam("productitemcode") String productItemCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ProductDAO productDAO = new ProductDAO();

        System.out.println("Search Product Items In Product Code call called!");

        return productDAO.searchProductItemsInProductCode(productCode,productItemCode);

    }


}

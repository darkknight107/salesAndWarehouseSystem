package com.fellowshipofthe.searchProduct;

import com.fellowshipofthe.DAO.ProductDAO;
import com.fellowshipofthe.entityClasses.Product;
import com.fellowshipofthe.entityClasses.ProductItem;
import com.fellowshipofthe.entityClasses.SearchProduct;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("searchproduct")
public class SearchProductResource{

    // Client calls the view all product
    @GET
    @Path("/viewallproducts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product>viewAllProduct() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ProductDAO productDAO = new ProductDAO();

        System.out.println(" View all product called!");

        return productDAO.viewAllProducts();

    }

    // Client calls the search product code
    @GET
    @Path("/searchproductcode")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product>searchProductCode(@QueryParam("productcode") String productCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ProductDAO productDAO = new ProductDAO();

        System.out.println(" Search product code called!");

        return productDAO.searchProductCode(productCode);

    }

    // Client calls the view searched product items
    @GET
    @Path("/viewsearchedproductitems")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductItem>viewsearchedProductItem(@QueryParam("productcode") String productCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ProductDAO productDAO = new ProductDAO();

        System.out.println(" View searched product items called!");

        return productDAO.viewSeachedProductItems(productCode);

    }

    // Client calls the search product item code
    @GET
    @Path("/searchproductitemcode")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductItem>searchProductItemCode(@QueryParam("productitemcode") String productItemCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ProductDAO productDAO = new ProductDAO();

        System.out.println(" Search product item code called!");

        return productDAO.searchProductItemCode(productItemCode);

    }

    // Client calls view product item details
    @GET
    @Path("/viewproductitemdetails")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SearchProduct>viewproductitemdetails(@QueryParam("productitemcode") String productItemCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        ProductDAO productDAO = new ProductDAO();

        System.out.println(" View product item details called!");

        return productDAO.viewProductItemDetails(productItemCode);

    }

}

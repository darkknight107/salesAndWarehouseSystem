package com.fellowshipofthe;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("searchproduct")
public class SearchProductResource{

    SearchProductDAO searchProductDAO;

//    public SearchProductResource() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
//
//    }

    @GET
    @Path("searchproductcode/{productcode}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SearchProduct> searchProduct(@PathParam("productcode") String productCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        searchProductDAO= new SearchProductDAO();
//        List searchedProductList = null;
//        String productDetails = null;
        System.out.println("search product called!");

        return searchProductDAO.searchProduct(productCode);

//        Gson gson = new Gson();
//        productDetails = gson.toJson(searchedProductList);
//        return productDetails;
    }


}

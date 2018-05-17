package com.fellowshipofthe;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;

@Path("searchproduct")
public class SearchProductResource{

    SearchProductDAO searchProductDAO;

    public SearchProductResource() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        searchProductDAO= new SearchProductDAO();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public ProductItem searchProduct() throws SQLException{
        System.out.println("search product called!");
        return searchProductDAO.searchProduct("S1100");

    }


}

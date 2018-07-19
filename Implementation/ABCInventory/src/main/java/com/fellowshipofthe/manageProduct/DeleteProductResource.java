package com.fellowshipofthe.manageProduct;


import com.fellowshipofthe.searchProduct.SearchProductDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;


@Path("delete")
public class DeleteProductResource {

    //resource for deleting product
    @DELETE
    @Path("deleteproduct/{productcode}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean deleteProduct(@PathParam("productcode") String productCode) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("deleteProduct called!");
        SearchProductDAO searchProductDAO= new SearchProductDAO();
        return searchProductDAO.deleteProduct(productCode);
    }

}

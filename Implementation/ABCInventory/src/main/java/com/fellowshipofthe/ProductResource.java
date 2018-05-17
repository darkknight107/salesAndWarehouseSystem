package com.fellowshipofthe;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("products")
public class ProductResource {
    ProductDAO repo;

    public ProductResource() throws SQLException, ClassNotFoundException {
        repo= new ProductDAO();
    }
    @GET
    @Produces(MediaType.APPLICATION_XML)
    //@Produces(MediaType.APPLICATION_JSON)
    public List<Product> getProduct() throws SQLException {
        System.out.println("get MockProduct called!");
        return repo.getProducts();
    }

}

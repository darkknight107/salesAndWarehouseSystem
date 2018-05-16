package com.fellowshipofthe;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("name")
public class MockProductResource {
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<MockProduct> getProduct(){
        System.out.println("get MockProduct called!");

        MockProduct product1= new MockProduct();
        product1.setProductName("Sydney Tee");
        product1.setProductCode(100);
        product1.setPrice(59);
        product1.setDescription("This is a mock tshirt.");

        MockProduct product2= new MockProduct();
        product2.setProductName("Rock Jeans");
        product2.setProductCode(200);
        product2.setPrice(99);
        product2.setDescription("This is a mock pant.");

        List<MockProduct> products= Arrays.asList(product1, product2);
        return products;
    }

}

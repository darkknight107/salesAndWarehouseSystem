package manageProduct;

import com.fellowshipofthe.SearchProduct;
import com.fellowshipofthe.SearchProductDAO;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("/manage")
public class AddProductResource {
    SearchProductDAO product = new SearchProductDAO();

    @POST
   // @Path("/addproduct")
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

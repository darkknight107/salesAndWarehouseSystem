package manageProduct;

import com.fellowshipofthe.SearchProduct;
import com.fellowshipofthe.SearchProductDAO;
import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;

import javax.print.attribute.standard.Media;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.SQLException;
import java.util.List;

@Path("/addproduct")
public class AddProductResource {
    SearchProductDAO product = new SearchProductDAO();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addProduct(List<SearchProduct> productDetails) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        System.out.println("addProduct called!");
        return product.addProduct(productDetails);

    }


}

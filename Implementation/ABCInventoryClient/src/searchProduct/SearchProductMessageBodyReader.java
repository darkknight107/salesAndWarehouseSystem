package searchProduct;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class SearchProductMessageBodyReader implements MessageBodyReader<List<SearchProduct>>{

//    @Override
    public boolean isReadable(Class<?> type, Type type1, Annotation[] antns,
                              MediaType mt) {
        return true;
    }

//    @Override
    public List<SearchProduct> readFrom(Class<List<SearchProduct>> type, Type type1, Annotation[] antns, MediaType mt, MultivaluedMap<String, String> mm, InputStream in) throws IOException, WebApplicationException {
        if (mt.getType().equals("application") && mt.getSubtype().equals("json")) {
            SearchProduct searchProduct = new SearchProduct();
            List<SearchProduct> customers = new ArrayList();
            JsonParser parser = Json.createParser(in);
            while (parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch (event) {
                    case START_OBJECT:
                        searchProduct = new SearchProduct();
                        break;
                    case END_OBJECT:
                        customers.add(searchProduct);
                        break;
                    case KEY_NAME:
                        String key = parser.getString();
                        parser.next();
                        switch (key) {
                            case "productCode":
                                searchProduct.setProductCode(parser.getString());
                                break;
                            case "productName":
                                searchProduct.setProductName(parser.getString());
                                break;
                            case "productQuantity":
                                searchProduct.setProductQuantity(parser.getString());
                                break;
                            case "price":
                                searchProduct.setPrice(parser.getString());
                                break;
                            case "locationID":
                                searchProduct.setLocationID(parser.getString());
                                break;
                            case "locationName":
                                searchProduct.setLocationName(parser.getString());
                                break;
                            case "locationAddress":
                                searchProduct.setLocationAddress(parser.getString());
                                break;
                            case "phone":
                                searchProduct.setPhone(parser.getString());
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
            return customers;
        }
        throw new UnsupportedOperationException("Not supported MediaType: " + mt);
    }

}

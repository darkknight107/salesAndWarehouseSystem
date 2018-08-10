package searchProduct;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import entityClass.ProductItem;
import entityClass.SearchProduct;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import manageProduct.AppScreen;
import manageProduct.UpdateProductController;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchProductItemDetailsController {
    // Initialize variables
    @FXML
    private TableView<SearchProduct> tblViewSearchedProductDetails;
    @FXML
    private BorderPane mainPanel;

    AppScreen screen = new AppScreen();

    ObservableList<SearchProduct> data;

    // Variables for create the client
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<SearchProduct>> listc = new GenericType<List<SearchProduct>>() {
    };
    List<SearchProduct> searchProductList;

    String getProductURL;

    // Initialize method for handle the action when pressing button
    public void initialize(URL url, ResourceBundle rb){
    }

    // Show all the products and product items
    public void showAllProductItemDetails(String productItemCode){
        //connect to the server to retrieve the data
        data = tblViewSearchedProductDetails.getItems();
        data.clear();

        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        webResourceGet = client.resource("http://localhost:8080/rest/searchproduct/viewproductitemdetails").queryParam("productitemcode", productItemCode);
        response = webResourceGet.get(ClientResponse.class);
        searchProductList = response.getEntity(listc);
        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }
        if (searchProductList.isEmpty()) {
            screen.alertMessages("Non-Existent Product", "Product does not exist!");
        } else {
            for (SearchProduct s : searchProductList) {
                data.add(s);
            }
        }
    }
}
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
import entityClass.StoredProduct;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import manageProduct.AppScreen;
import manageProduct.ManageProductController;
import manageProduct.UpdateProductController;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchProductItemDetailsController {
    // Initialize variables
    @FXML
    AnchorPane anchorPane;
    @FXML
    private TableView<SearchProduct> tblViewSearchedProductDetails;
    @FXML
    private BorderPane mainPanel;
    @FXML
    private TableColumn quantityColumn;
    BorderPane pane;

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
    public void editQuantity(){
        quantityColumn.setEditable(true);
    }
    public void handleEditCommit() throws IOException {

        TableCell<StoredProduct, StoredProduct> cell = new TableCell<StoredProduct, StoredProduct>() {
            @Override
            //the buttons are only displayed for the row have data
            public void updateItem(StoredProduct storedProduct, boolean empty) {
                super.updateItem(storedProduct, empty);
            }
        };
        String productItemCode = tblViewSearchedProductDetails.getSelectionModel().getSelectedItem().getProductItemCode();
        String locationID = tblViewSearchedProductDetails.getSelectionModel().getSelectedItem().getLocationID();
        String quantity = tblViewSearchedProductDetails.getSelectionModel().getSelectedItem().getProductQuantity();
        tblViewSearchedProductDetails.getSelectionModel().select(cell.getIndex());
        StoredProduct updateStoredProduct= new StoredProduct();
        updateStoredProduct.setProductItemCode(productItemCode);
        updateStoredProduct.setLocationID(locationID);
        updateStoredProduct.setProductQuantity(quantity);
        //creating a new client to send post request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client= Client.create(clientConfig);
        String updateURL= "http://localhost:8080/rest/update/updatestoredproduct";
        WebResource webResourcePost= client.resource(updateURL);
        //use the object passed as a parameter to send a request
        ClientResponse response= webResourcePost.type("application/json").put(ClientResponse.class, updateStoredProduct);
        response.bufferEntity();
        String responseValue= response.getEntity(String.class);
        AppScreen screen= new AppScreen();
        if (responseValue.equals("updated")){
            screen.alertMessages("Product Updated!", "The Product Quantity of " + productItemCode + " has been updated in " + locationID);
            FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageProduct.fxml"));
            pane = loader.load();
            ManageProductController manageProductController= loader.getController();
            manageProductController.showAllProducts();
            anchorPane.getChildren().setAll(pane);
        }
        else{
            screen.alertMessages("Error!", "Product could not be updated!");
        }



    }
}
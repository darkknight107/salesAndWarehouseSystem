package manageProduct;


import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import searchProduct.SearchProductController;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AddProductController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField productCodeField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField descriptionField;
    WebTarget target;

    @FXML
    public void handleAddProductNext() throws ExecutionException, InterruptedException {

        //creating variables to get product details entered by user in text fields
        String productCode= productCodeField.getText();
        String productName= productNameField.getText();
        String price= priceField.getText();
        String description= descriptionField.getText();

        //creating a new client to send post request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client= Client.create(clientConfig);

        //set textfield values to Product Entity
        Product newProduct= new Product();
        newProduct.setProductCode(productCode);
        newProduct.setProductName(productName);
        newProduct.setPrice(price);
        newProduct.setDescription(description);
        String postURL= "http://localhost:8080/rest/manageproduct";
        WebResource webResourcePost= client.resource(postURL);
        ClientResponse response= webResourcePost.type("application/json").post(ClientResponse.class, newProduct);
        response.bufferEntity();
        String x= response.getEntity(String.class);
        System.out.println(x);
        AppScreen screen= new AppScreen();
        if (x.equals("true")){
            screen.alertMessages("Product Added", "Product has been added.");
            clearTextFields();
        }
        else{
            screen.alertMessages("Error", "Error! Please try again.");
            clearTextFields();
        }
    }
    private void clearTextFields(){
        productCodeField.setText("");
        productNameField.setText("");
        priceField.setText("");
        descriptionField.setText("");
    }
}

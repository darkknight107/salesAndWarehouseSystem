package manageProduct;


import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

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
    public void handleAddProductNext() throws ExecutionException, InterruptedException, IOException {

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
            AnchorPane pane= FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AddProductItem.fxml"));
            anchorPane.getChildren().setAll(pane);
        }
        else{
            screen.alertMessages("Error", "Error! Please try again.");
            clearTextFields();
        }
    }
    @FXML
    private void clearTextFields(){
        productCodeField.setText("");
        productNameField.setText("");
        priceField.setText("");
        descriptionField.setText("");
    }
    @FXML
    public void handleFromExistingProduct() throws IOException {
        BorderPane pane= FXMLLoader.load(getClass().getClassLoader().getResource("fxml/SearchProductFXML.fxml"));
        anchorPane.getChildren().setAll(pane);
    }

}

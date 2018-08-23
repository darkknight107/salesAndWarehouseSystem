package manageProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;


public class UpdateProductController {
    //update product fxml elements
    @FXML
    AnchorPane anchorPane;
    @FXML
    private TextField productCodeField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField descriptionField;
    private BorderPane pane;
    //method to set existing values to the field
    @FXML
    public void setData(Product updatedProduct){
        productCodeField.setText(updatedProduct.getProductCode());
        productCodeField.setDisable(true);
        productNameField.setText(updatedProduct.getProductName());
        priceField.setText(updatedProduct.getPrice());
        descriptionField.setText(updatedProduct.getDescription());
    }

    @FXML
    public void handleFinalizeUpdateButton() throws IOException {
        String productCode= productCodeField.getText();
        String productName= productNameField.getText();
        String price= priceField.getText();
        String description= descriptionField.getText();
        Product updatedProduct= new Product();
        updatedProduct.setProductCode(productCode);
        updatedProduct.setProductName(productName);
        updatedProduct.setPrice(price);
        updatedProduct.setDescription(description);

        //creating a new client to send post request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client= Client.create(clientConfig);
        String updateURL= "http://localhost:8080/rest/update/updateproduct";
        WebResource webResourcePost= client.resource(updateURL);
        //use the object passed as a parameter to send a request
        ClientResponse response= webResourcePost.type("application/json").put(ClientResponse.class, updatedProduct);
        response.bufferEntity();
        String responseValue= response.getEntity(String.class);
        AppScreen screen= new AppScreen();
        if (responseValue.equals("updated")){
            screen.alertMessages("Product Updated!", "The Product " + productCode + " has been updated!");
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

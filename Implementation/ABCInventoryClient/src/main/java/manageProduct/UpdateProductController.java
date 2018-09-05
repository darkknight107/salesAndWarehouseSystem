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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Button backButton;
    @FXML
    private Button mainMenuButton;
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
    public void handleBackButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageProduct.fxml"));
        pane = loader.load();
        ManageProductController controller= loader.getController();
        controller.showAllProducts();
        anchorPane.getChildren().setAll(pane);
    }

    public void handleMainMenuButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
        AnchorPane aPane = loader.load();
        anchorPane.getChildren().setAll(aPane);
    }
    public void handleLogoutButton() throws IOException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        alert.setTitle("Confirmation");
        if (alert.getResult()== ButtonType.YES) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SearchAccount.fxml"));
            AnchorPane aPane = loader.load();
            anchorPane.getChildren().setAll(aPane);
        }
    }

}

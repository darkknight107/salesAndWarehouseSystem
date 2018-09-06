package manageProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.ProductItem;
import entityClass.StoredProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class AddProductItemController {
    @FXML
    BorderPane borderPane;
    @FXML
    TextField productCodeField;
    @FXML
    ComboBox sizeComboBox;
    @FXML
    ComboBox locationComboBox;
    @FXML
    TextField quantityField;
    @FXML
    Button addButton;
    @FXML
    AnchorPane pane;
    AppScreen screen = new AppScreen();



    public void setSelectedProductCode(String code){
        productCodeField.setText(code);
    }

    public void setComboBoxValues(){
        ObservableList<String> sizeList= FXCollections.observableArrayList("XS","S", "M", "L", "XL");
        ObservableList<String> locationList= FXCollections.observableArrayList("Newtown Warehouse", "Epping Store", "Oxford Store");
        sizeComboBox.setItems(sizeList);
        locationComboBox.setItems(locationList);
    }

    public void handleAddButton() {
        if (sizeComboBox.getValue() != null && locationComboBox.getValue() != null && !(quantityField.getText().isEmpty())){
            String locationID = null;
            String productItemCode = null;
            String productCode = productCodeField.getText();
            String size = (String) sizeComboBox.getValue();
            String quantity = quantityField.getText();
            switch ((String) locationComboBox.getValue()) {
                case "Newtown Warehouse":
                    locationID = "WRH1";
                    break;
                case "Epping Store":
                    locationID = "STR2";
                    break;
                case "Oxford Store":
                    locationID = "STR1";
                    break;
            }
            switch (size) {
                case "XS":
                    productItemCode = productCode + "100";
                    break;
                case "S":
                    productItemCode = productCode + "200";
                    break;
                case "M":
                    productItemCode = productCode + "300";
                    break;
                case "L":
                    productItemCode = productCode + "400";
                    break;
                case "XL":
                    productItemCode = productCode + "500";
                    break;
            }
            //setting new productItem
            ProductItem newProductItem = new ProductItem();
            newProductItem.setProductItemCode(productItemCode);
            newProductItem.setProductCode(productCode);
            newProductItem.setProductSize(size);

            //calling clientRequest to send a request to the server
            String response = clientRequest(newProductItem, "addproductitem");
            System.out.println(response);

            //setting new storedProduct
            StoredProduct newStoredProduct = new StoredProduct();
            newStoredProduct.setProductItemCode(productItemCode);
            newStoredProduct.setLocationID(locationID);
            newStoredProduct.setProductQuantity(quantity);

            //calling the client request method to send a request to the server
            String response1 = clientRequest(newStoredProduct, "addstoredproduct");
            System.out.println(response1);

            if (response1.equals("true")) {
                screen.alertMessages("Items Added", productItemCode + " has been added to " + locationID + ".");
                //clearing all values for user
                sizeComboBox.getSelectionModel().clearSelection();
                locationComboBox.getSelectionModel().clearSelection();
                quantityField.clear();
            }
            else{
                screen.alertMessages("Error occured!", "You may have not entered all the required values or check your internet  connection. Please try again.");
            }
        }
        else {
            screen.alertMessages("Values not entered!", "You have not entered all values! Please enter all values and try again.");
        }

    }
    public String clientRequest(Object entity, String path){
        //creating a new client to send post request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client= Client.create(clientConfig);
        String postURL= "http://localhost:8080/rest/manageproduct/" + path;
        WebResource webResourcePost= client.resource(postURL);
        //use the object passed as a parameter to send a request
        ClientResponse response= webResourcePost.type("application/json").post(ClientResponse.class, entity);
        response.bufferEntity();
        String responseValue= response.getEntity(String.class);
        return responseValue;
    }
    public void handleBackButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageProduct.fxml"));
        AnchorPane aPane = loader.load();
        ManageProductController controller= loader.getController();
        controller.showAllProducts();
        borderPane.getChildren().setAll(aPane);
    }

    public void handleMainMenuButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
        pane = loader.load();
        borderPane.getChildren().setAll(pane);
    }
    public void handleLogoutButton() throws IOException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        alert.setTitle("Confirmation");
        if (alert.getResult()== ButtonType.YES){
            FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/SearchAccount.fxml"));
            pane = loader.load();
            borderPane.getChildren().setAll(pane);
        }

    }

}

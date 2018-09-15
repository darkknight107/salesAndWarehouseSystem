package manageProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.ProductItem;
import entityClass.StoredProduct;
import homePage.HomePageController;
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
        resetStyle();
        if (sizeComboBox.getValue() != null && locationComboBox.getValue() != null && !(quantityField.getText().isEmpty())){
            String locationID = null;
            String productItemCode = null;
            String productCode = productCodeField.getText();
            String size = (String) sizeComboBox.getValue();
            String quantity = quantityField.getText();
            if(new AddProductController().isNumeric(quantity) == true){
                double numericQuantity= Double.parseDouble(quantity);
                if(!(numericQuantity< 0)){
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
                else{
                    screen.alertMessages("Error!", "Quantity cannot be a negative value!");
                    quantityField.setText("");
                    quantityField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
                }
            }
            else{
                screen.alertMessages("Invalid quantity!", "Quantity cannot be a non numeric value!");
                quantityField.setText("");
                quantityField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }

        }
        else {
            screen.alertMessages("Values not entered!", "You have not entered all values! Please enter all values and try again.");
            if (sizeComboBox.getValue()== null) {
                sizeComboBox.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (locationComboBox.getValue()== null) {
                locationComboBox.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (quantityField.getText().isEmpty()) {
                quantityField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
        }

    }
    public void resetStyle(){
        sizeComboBox.setStyle(null);
        locationComboBox.setStyle(null);
        quantityField.setStyle(null);
    }
    public String clientRequest(Object entity, String path){
        //creating a new client to send post request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client= Client.create(clientConfig);
        String postURL= "http://abcinventoryserver.ap-southeast-2.elasticbeanstalk.com/rest/manageproduct/" + path;
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
        HomePageController controller= loader.getController();
        controller.checkStaff();
        borderPane.getChildren().setAll(pane);
    }

}

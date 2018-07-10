package manageProduct;


import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import entityClass.ProductItem;
import entityClass.StoredProduct;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javax.ws.rs.client.WebTarget;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;

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
    @FXML
    private Label productCodeLabel;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Button nextButton;
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
            clearTextFields();
            handleAddProductItem(newProduct.getProductCode());
            //AnchorPane pane= FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AddProductItem.fxml"));
            //anchorPane.getChildren().setAll(pane);
            //productNameLabel.setText("Product Item Code");

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

    public void handleAddProductItem(String productCode){
        productCodeField.setText(productCode);
        priceLabel.setText("Size");
        productNameLabel.setVisible(false);
        productNameField.setVisible(false);
        descriptionLabel.setVisible(false);
        descriptionField.setVisible(false);

        final String[] productItemCode = {null};
        final String[] productSize = new String[1];
        nextButton.setOnAction((ActionEvent event)-> {
            //since productName changed to productItem code field once product is added and product item needs to be added
            productSize[0] = priceField.getText();
            //conditional statements to set rear value of product item code according to product size
            if (productSize[0].equals("XS")){
                productItemCode[0] = productCode +"100";
            }
            else if (productSize[0].equals("S")){
                productItemCode[0] = productCode + "200";
            }
            else if (productSize[0].equals("M")){
                productItemCode[0] = productCode + "300";
            }
            else if (productSize[0].equals("L")){
                productItemCode[0] = productCode + "400";
            }
            else if (productSize[0].equals("XL")){
                productItemCode[0] = productCode + "500";
            }

            //creating a new client to send post request
            ClientConfig clientConfig= new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client= Client.create(clientConfig);

            //set textfield values to ProductItem Entity
            ProductItem newProductItem= new ProductItem();
            newProductItem.setProductCode(productCode);

            newProductItem.setProductItemCode(productItemCode[0]);
            //since product price changed to product size once product is added and product item needs to be added
            newProductItem.setProductSize(productSize[0]);
            String postURL= "http://localhost:8080/rest/manageproduct/addproductitem";
            WebResource webResourcePost= client.resource(postURL);
            ClientResponse response= webResourcePost.type("application/json").post(ClientResponse.class, newProductItem);
            response.bufferEntity();
            String x= response.getEntity(String.class);
            System.out.println(x);
            AppScreen screen= new AppScreen();
            if (x.equals("true")){

                screen.alertMessages("Product Item Added!", "Added product Item code is " + productItemCode[0]);
                clearTextFields();
                handleAddStoredProduct(newProductItem.getProductItemCode());
            }
            else{
                screen.alertMessages("Error", "Error! Please try again.");
                clearTextFields();
            }
        });



    }
    public void handleAddStoredProduct(String productItemCode){
        productCodeLabel.setText("Item Code");
        productCodeField.setText(productItemCode);
        priceLabel.setText("Location");
        priceField.setPromptText("Please enter a valid location");
        productNameLabel.setVisible(true);
        productNameLabel.setText("Quantity");
        productNameField.setVisible(true);
        productNameField.setPromptText("Please enter the quantity");
        AppScreen screen= new AppScreen();
        final String [] locationName = {null};
        final String[] locationID = {null};
        final String [] productQuantity= {null};
        nextButton.setOnAction((ActionEvent event)->{

            locationName[0]= priceField.getText();
            if (locationName[0].equals("Oxford Store")){
                locationID[0] = "STR1";
            }
            else if(locationName[0].equals("Epping")){
                locationID[0]= "STR2";
            }
            else if(locationName[0].equals("Newtown")){
                locationID[0]= "WRH1";
            }
            /*else{
                screen.alertMessages("Invalid Location", "Please enter a valid location name!");
            }*/
            productQuantity[0]= productNameField.getText();

            //creating a new client to send post request
            ClientConfig clientConfig= new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client= Client.create(clientConfig);

            //set textfield values to ProductItem Entity
            StoredProduct newStoredProduct= new StoredProduct();
            newStoredProduct.setProductItemCode(productItemCode);

            newStoredProduct.setLocationID(locationID[0]);
            //since product price changed to product size once product is added and product item needs to be added
            newStoredProduct.setProductQuantity(productQuantity[0]);
            String postURL= "http://localhost:8080/rest/manageproduct/addstoredproduct";
            WebResource webResourcePost= client.resource(postURL);
            ClientResponse response= webResourcePost.type("application/json").post(ClientResponse.class, newStoredProduct);
            response.bufferEntity();
            String x= response.getEntity(String.class);
            System.out.println(x);
            if (x.equals("true")){
                screen.alertMessages("Product Allocated to location", "Proucts have been added to " + locationID[0]);
                clearTextFields();
            }

        });

    }

}

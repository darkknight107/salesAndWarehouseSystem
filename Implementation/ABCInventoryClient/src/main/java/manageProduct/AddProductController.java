package manageProduct;


import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import entityClass.ProductItem;
import entityClass.StoredProduct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


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
    ComboBox comboBox;

    @FXML
    public void handleAddProductNext() throws ExecutionException, InterruptedException, IOException {
        //creating variables to get product details entered by user in text fields
        String productCode= productCodeField.getText();
        String productName= productNameField.getText();
        String price= priceField.getText();
        String description= descriptionField.getText();

        //set textfield values to Product Entity
        Product newProduct= new Product();
        newProduct.setProductCode(productCode);
        newProduct.setProductName(productName);
        newProduct.setPrice(price);
        newProduct.setDescription(description);

        //call the clientRequest method to send a request to the server
        String response= clientRequest(newProduct,"");
        System.out.println(response);
        AppScreen screen= new AppScreen();
        if (response.equals("true")){
            screen.alertMessages("Product Added", "Product has been added.");
            clearTextFields();
            //load text fields and labels for adding product item
            handleAddProductItem(newProduct.getProductCode());
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
        //changing button, label and prompt texts for adding product item
        productCodeField.setText(productCode);
        productCodeField.setDisable(true);
        priceLabel.setText("Size");
        //creating a combo box to select size
        ObservableList<String> sizeOptions= FXCollections.observableArrayList("XS", "S", "M", "L", "XL");
        comboBox= new ComboBox(sizeOptions);
        anchorPane.getChildren().add(comboBox);
        comboBox.setLayoutX((anchorPane.getWidth())/2);
        comboBox.setLayoutY((anchorPane.getHeight())/2);
        priceField.setVisible(false);
        productNameLabel.setVisible(false);
        productNameField.setVisible(false);
        descriptionLabel.setVisible(false);
        descriptionField.setVisible(false);
        //creating an appscreen for alert messages
        AppScreen screen= new AppScreen();
        //variables to store required values for product item
        final String[] productItemCode = {null};
        final String[] productSize = new String[1];
        final String[] repeat = {"Yes"};
        while (repeat[0].equals("Yes")){

                productSize[0] = (String) comboBox.getValue();
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
            nextButton.setOnAction((ActionEvent event)-> {

                //set textfield values to ProductItem Entity
                ProductItem newProductItem= new ProductItem();
                newProductItem.setProductCode(productCode);
                newProductItem.setProductItemCode(productItemCode[0]);
                newProductItem.setProductSize(productSize[0]);

                //calling clientRequest to send a request to the server
                String response= clientRequest(newProductItem, "addproductitem");
                System.out.println(response);

                if (response.equals("true")){
                    screen.alertMessages("Product Item Added!", "Added product Item code is " + productItemCode[0]);
                    Alert alert= new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Add more sizes?");
                    alert.setContentText("Do you want to add another size for product "+ productCode + "?");
                    ButtonType yesButton= new ButtonType("Yes", ButtonBar.ButtonData.YES);
                    ButtonType noButton= new ButtonType("No", ButtonBar.ButtonData.NO);
                    alert.getButtonTypes().setAll(yesButton, noButton);
                    alert.showAndWait().ifPresent(type ->{
                        if (type == ButtonType.YES){
                            repeat[0] = "Yes";
                        }
                        else{
                            clearTextFields();
                            handleAddStoredProduct(newProductItem.getProductItemCode());
                            repeat[0]= "No";
                        }
                    });

                }
                else{
                    screen.alertMessages("Error", "Error! Please try again.");
                    clearTextFields();
                }
            });


        }

    }

    public void handleAddStoredProduct(String productItemCode){
        //changing label, text field and prompt values for adding stored product
        productCodeLabel.setText("Item Code");
        productCodeField.setText(productItemCode);
        priceLabel.setText("Location");
        priceField.setPromptText("Please enter a valid location");
        productNameLabel.setVisible(true);
        productNameLabel.setText("Quantity");
        productNameField.setVisible(true);
        productNameField.setPromptText("Please enter the quantity");
        //putting location names in combo box
        comboBox.getItems().clear();
        ObservableList<String> locationOptions= FXCollections.observableArrayList("Newtown", "Epping", "Oxford Store");
        comboBox.setItems(locationOptions);

        //creating an app screen object for alert messages
        AppScreen screen= new AppScreen();
        //variables to store required values
        final String [] locationName = {null};
        final String[] locationID = {null};
        final String [] productQuantity= {null};

        //on pressing next button do the following
        nextButton.setOnAction((ActionEvent event)->{
            productQuantity[0]= productNameField.getText();
            locationName[0]= (String) comboBox.getValue();
            //getting and validating the location name entered with the available location
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

            //set textfield values to Stored Product Entity
            StoredProduct newStoredProduct= new StoredProduct();
            newStoredProduct.setProductItemCode(productItemCode);
            newStoredProduct.setLocationID(locationID[0]);
            newStoredProduct.setProductQuantity(productQuantity[0]);
            //calling the client request method to send a request to the server
            String response= clientRequest(newStoredProduct, "addstoredproduct");
            System.out.println(response);
            if (response.equals("true")){
                screen.alertMessages("Product Allocated to location", "Products have been added to " + locationID[0]);
                clearTextFields();
            }
            else{
                screen.alertMessages("Cannot Add Product!", "The product could not be added to the entered location. Please try again.");
            }
        });
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
}

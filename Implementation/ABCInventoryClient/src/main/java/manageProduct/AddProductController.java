package manageProduct;


import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.*;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import entityClass.ProductItem;
import entityClass.StoredProduct;
import homePage.HomePageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import searchProduct.SearchProductController;


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
    @FXML
    private Button resetButton;
    @FXML
    private Button addFromExistingButton;

    ComboBox sizeComboBox;
    ComboBox locationComboBox;

    @FXML
    public void handleAddProductNext() throws ExecutionException, InterruptedException, IOException {
        //creating variables to get product details entered by user in text fields
        String productCode= productCodeField.getText();
        String productName= productNameField.getText();
        String price= priceField.getText();
        String description= descriptionField.getText();
        AppScreen screen= new AppScreen();
        //validate entered productCode
        if (productCode.matches("[A-Z][0-9]")){
            //set textfield values to Product Entity
            Product newProduct= new Product();
            newProduct.setProductCode(productCode);
            newProduct.setProductName(productName);
            newProduct.setPrice(price);
            newProduct.setDescription(description);

            //call the clientRequest method to send a request to the server
            String response= clientRequest(newProduct,"");
            System.out.println(response);

            //test if product already exists
            //add if does not exist
            if (response.equals("exists")){
                screen.alertMessages("Product already exists.", "The Product entered already exists.");
                clearTextFields();
                //load text fields and labels for adding product item for existing product
                handleAddProductItem(newProduct.getProductCode());
            }

            else if(response.equals("true")){
                screen.alertMessages("Product Added.", "Product " + newProduct.getProductCode() + " has been added.");
                clearTextFields();
                //load text fields and labels for adding product item
                handleAddProductItem(newProduct.getProductCode());
            }
            else{
                screen.alertMessages("Error!", "An Error occurred. Please try again!");
            }
        }
        else{
            screen.alertMessages("Invalid Code", "Please use a valid code. Code must contain one Alphabet followed by a numeral! \n" +
                    "For instance: D4 is a valid code!");
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
        HomePageController controller= new HomePageController();
        controller.handleSearchProduct();
    }

    public void handleAddProductItem(String productCode){
        productCodeField.setText(productCode);
        productCodeField.setDisable(true);
        productNameLabel.setText("Size");
        productNameField.setVisible(false);
        priceLabel.setText("Location");
        priceField.setVisible(false);
        descriptionLabel.setText("Quantity");
        descriptionField.setPromptText("Enter Quantity");
        nextButton.setText("Add");
        //various buttons, fields and combo boxes required for adding product item and allocating it to location
        ObservableList<String> sizeOptions= FXCollections.observableArrayList("XS", "S", "M", "L", "XL");
        sizeComboBox= new ComboBox(sizeOptions);
        ObservableList<String> locationOptions= FXCollections.observableArrayList("Newtown", "Epping", "Oxford Store");
        locationComboBox= new ComboBox(locationOptions);
        anchorPane.getChildren().add(locationComboBox);
        GridPane gridPane= new GridPane();
        gridPane.add(productCodeLabel, 0, 0);
        gridPane.add(productCodeField, 1, 0);
        gridPane.add(productNameLabel, 0, 1);
        gridPane.add(sizeComboBox, 1,1);
        gridPane.add(priceLabel, 0, 2);
        gridPane.add(locationComboBox, 1, 2);
        gridPane.add(descriptionLabel, 0, 3);
        gridPane.add(descriptionField, 1,3);
        gridPane.add(nextButton, 0, 4);
        gridPane.add(resetButton, 1, 4);
        gridPane.add(addFromExistingButton, 2, 4);

        anchorPane.getChildren().add(gridPane);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setLayoutX(anchorPane.getWidth()/2);
        gridPane.setLayoutY(anchorPane.getHeight()/2);
        final String[] productItemCode = {null};
        final String[] productSize = new String[1];
        final String [] locationName = {null};
        final String[] locationID = {null};
        final String [] productQuantity= {null};

        nextButton.setOnAction((ActionEvent event)->{
            locationName[0]= (String) locationComboBox.getValue();
            productSize[0]= (String) sizeComboBox.getValue();
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

            //set textfield values to ProductItem Entity
            ProductItem newProductItem= new ProductItem();
            newProductItem.setProductCode(productCode);
            newProductItem.setProductItemCode(productItemCode[0]);
            newProductItem.setProductSize(productSize[0]);

            //calling clientRequest to send a request to the server
            String response= clientRequest(newProductItem, "addproductitem");
            System.out.println(response);
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
            productQuantity[0]= descriptionField.getText();
            StoredProduct newStoredProduct= new StoredProduct();
            newStoredProduct.setProductItemCode(productItemCode[0]);
            newStoredProduct.setLocationID(locationID[0]);
            newStoredProduct.setProductQuantity(productQuantity[0]);
            //calling the client request method to send a request to the server
            String response1= clientRequest(newStoredProduct, "addstoredproduct");
            System.out.println(response1);
            System.out.println("Product Item added and allocated!");
            AppScreen screen= new AppScreen();
            screen.alertMessages("Items Added", newProductItem.getProductItemCode() + " has been added to " + locationID[0] + ".");
            //clearing all values for user
            sizeComboBox.getSelectionModel().clearSelection();
            //sizeComboBox.getItems().clear();
            locationComboBox.getSelectionModel().clearSelection();
            //locationComboBox.getItems().clear();
            descriptionField.clear();
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

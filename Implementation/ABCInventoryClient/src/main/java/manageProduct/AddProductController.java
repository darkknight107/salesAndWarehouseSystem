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
import javafx.scene.layout.Pane;


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

    ComboBox sizeComboBox;
    ComboBox locationComboBox;
    BorderPane bPane;
    Product newProduct;
    AppScreen screen;

    public AddProductController(){
        newProduct= new Product();
        screen= new AppScreen();
    }

    @FXML
    public void handleAddProductNext() throws ExecutionException, InterruptedException, IOException {
        resetTextStyle();
        //creating variables to get product details entered by user in text fields
        String productCode= productCodeField.getText();
        String productName= productNameField.getText();
        String price= priceField.getText();
        String description= descriptionField.getText();

        if (!(productCode.isEmpty()) && !(productName.isEmpty()) && !(price.isEmpty()) && !(description.isEmpty())){

            //validate entered productCode
            if (productCode.matches("[A-Z][0-9]")){
                if(isNumeric(price) == true && !(Double.parseDouble(price) < 0)){
                    //set textfield values to Product Entity

                    newProduct.setProductCode(productCode);
                    newProduct.setProductName(productName);
                    newProduct.setPrice(price);
                    newProduct.setDescription(description);

                    //send post request to add the new product
                    ClientConfig clientConfig= new DefaultClientConfig();
                    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
                    Client client= Client.create(clientConfig);
                    String postURL= "http://localhost:8080/rest/manageproduct";
                    WebResource webResourcePost= client.resource(postURL);
                    //use the object passed as a parameter to send a request
                    ClientResponse responseValue= webResourcePost.type("application/json").post(ClientResponse.class, newProduct);
                    responseValue.bufferEntity();
                    String response= responseValue.getEntity(String.class);
                    System.out.println(response);

                    //test if product already exists
                    //add if does not exist
                    if (response.equals("exists")){
                        screen.alertMessages("Product already exists.", "The Product entered already exists.");
                        clearTextFields();
                        //load text fields and labels for adding product item for existing product
                        //handleAddProductItem(newProduct.getProductCode());
                        goToAddProductItem();
                    }

                    else if(response.equals("true")){
                        screen.alertMessages("Product Added.", "Product " + newProduct.getProductCode() + " has been added.");
                        clearTextFields();
                        //load text fields and labels for adding product item
                        //handleAddProductItem(newProduct.getProductCode());

                        goToAddProductItem();
                    }
                    else{
                        screen.alertMessages("Error!", "Error please check your internet  connection or try again later.");
                    }
                }
                else{
                    screen.alertMessages("Invalid Price", "Please enter a valid price value!");
                    priceField.setText("");
                    priceField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
                }

            }
            else{
                screen.alertMessages("Invalid Code", "Please use a valid code. Code must contain one Alphabet followed by a numeral! \n" +
                        "For instance: D4 is a valid code!");
                productCodeField.setText("");
                productCodeField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
        }
        else{
            screen.alertMessages("Values not entered!", "You have not entered all the values. Please enter all values and try again.");
            if (productCodeField.getText().isEmpty()) {
                productCodeField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (productNameField.getText().isEmpty()) {
                productNameField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (priceField.getText().isEmpty()) {
                priceField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (descriptionField.getText().isEmpty()) {
                descriptionField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
        }

    }

    @FXML
    private void clearTextFields(){
        productCodeField.setText("");
        productNameField.setText("");
        priceField.setText("");
        descriptionField.setText("");
    }
    public void resetTextStyle(){
        productNameField.setStyle(null);
        productCodeField.setStyle(null);
        priceField.setStyle(null);
        descriptionField.setStyle(null);
    }

    @FXML
    public void handleFromExistingProduct() throws IOException {
        HomePageController controller= new HomePageController();
        controller.handleSearchProduct();
    }

    public void goToAddProductItem() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddProductItem.fxml"));
        bPane = loader.load();
        anchorPane.getChildren().setAll(bPane);
        AddProductItemController addProductItemController= loader.<AddProductItemController>getController();
        addProductItemController.setSelectedProductCode(newProduct.getProductCode());
        addProductItemController.setComboBoxValues();
    }

    public static boolean isNumeric(String str){
        try{
            double d= Double.parseDouble(str);
        }
        catch(NumberFormatException nfe){
            return false;
        }
        return true;
        }

    public void handleBackButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageProduct.fxml"));
        AnchorPane pane = loader.load();
        ManageProductController controller= loader.getController();
        controller.showAllProducts();
        anchorPane.getChildren().setAll(pane);
    }

    public void handleMainMenuButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
        AnchorPane aPane = loader.load();
        HomePageController controller= loader.getController();
        controller.checkStaff();
        anchorPane.getChildren().setAll(aPane);
    }

}

package searchProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import entityClass.SearchProduct;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import manageProduct.AppScreen;
import manageProduct.UpdateProductController;
import transferProduct.AcceptTransferItemController;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchProductController {
    // Initialize variables
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Product> tblSearchProductCode;
    @FXML
    private TableColumn displayView;
    @FXML
    private AnchorPane anchorPane;
    private static String SELECTED_PRODUCT_CODE;

    AppScreen screen = new AppScreen();

    ObservableList<Product> data;

    // Variables for create the client
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<Product>> listc = new GenericType<List<Product>>() {
    };
    List<Product> productList;

    // Initialize method for handle the action when pressing button
    public void initialize(URL url, ResourceBundle rb){
        handleSearchProductCodeAction();
    }

    @FXML
    // Event handler for search button
    private void handleSearchProductCodeAction(){

        // GET request to searchproductcode resource with a query parameter
        String code = txtSearch.getText().toUpperCase();

        if (code.matches("[A-Z][0-9]")){
            searchProductCode("searchproductcode","productcode", code);
        }
        else if(code.equals("")){
            showAllProducts();
        } else {
            screen.alertMessages("Wrong Format Input","Please enter the code in the right format! \nEg: Produc Code: S1");
        }
    }

    // Search Product
    private  void searchProductCode(String path ,String searchField, String code) {
        data.clear();
        webResourceGet = client.resource("http://localhost:8080/rest/searchproduct/" + path).queryParam(searchField, code);
        response = webResourceGet.get(ClientResponse.class);
        productList = response.getEntity(listc);
        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }
        if (productList.isEmpty()) {
            screen.alertMessages("Non-Existent Product", "Product does not exist!");
        } else {
            for (Product p : productList) {
                data.add(p);
            }
        }
    }

    //Add the button for each row
    private void addDetailButton(){
        // Create the "Detail" button for each row and define the action for it
        displayView.setCellFactory(col ->{
            Image image = new Image("image/DetailButtonIcon.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(15);
            imageView.setFitWidth(15);

            Button viewButton = new Button("", imageView);
            Tooltip tooltip = new Tooltip("View product items of this product");
            tooltip.setShowDelay(Duration.millis(10));
            viewButton.setTooltip(tooltip);

            HBox hBox= new HBox(viewButton);
            TableCell<Product, Product> cell = new TableCell<Product, Product>() {
                @Override
                //the buttons are only displayed for the row have data
                public void updateItem(Product product, boolean empty) {
                    super.updateItem(product, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hBox);
                    }
                }
            };
            // When clicked the button, the button will show the product item of selected product
            viewButton.setOnAction(e -> {
                tblSearchProductCode.getSelectionModel().select(cell.getIndex());
                String selectedProductCode = tblSearchProductCode.getSelectionModel().getSelectedItem().getProductCode();
                setSELECTED_PRODUCT_CODE(selectedProductCode);
                //load text fields and labels for adding product item
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SearchProductItemFXML.fxml"));
                AnchorPane pane = null;
                try {
                    pane = loader.load();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                SearchProductItemController myController = loader.getController();

                //Set Data to FXML through controller
                myController.showAllProductItems(selectedProductCode);
                anchorPane.getChildren().setAll(pane);
            });
            return cell ;
        });
        tblSearchProductCode.getColumns().add(0,displayView);
    }

    // Show all the products and product items
    public void showAllProducts(){
        //connect to the server to retrieve the data
        data = tblSearchProductCode.getItems();
        data.clear();

        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        searchProductCode("viewallproducts","","");
        tblSearchProductCode.getColumns().remove(displayView);
        addDetailButton();
    }

    //Gettter and Setter for SELECTED_PRODUCT_CODE
    public void setSELECTED_PRODUCT_CODE(String SELECTED_PRODUCT_CODE){
        this.SELECTED_PRODUCT_CODE = SELECTED_PRODUCT_CODE;
    }
    public String getSELECTED_PRODUCT_CODE(){
        return SELECTED_PRODUCT_CODE;
    }

    public void handleBackButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
        AnchorPane pane = loader.load();
        anchorPane.getChildren().setAll(pane);
    }

    public void handleMainMenuButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
        AnchorPane pane = loader.load();
        anchorPane.getChildren().setAll(pane);
    }
}

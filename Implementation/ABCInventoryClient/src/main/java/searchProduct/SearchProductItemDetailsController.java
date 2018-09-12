package searchProduct;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import entityClass.ProductItem;
import entityClass.SearchProduct;
import entityClass.StoredProduct;
import homePage.HomePageController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import manageProduct.AppScreen;
import manageProduct.ManageProductController;
import manageProduct.UpdateProductController;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchProductItemDetailsController {
    // Initialize variables
    @FXML
    AnchorPane anchorPane;
    @FXML
    private TableView<SearchProduct> tblViewSearchedProductDetails;
    @FXML
    private BorderPane mainPanel;
    @FXML
    private TableColumn quantityColumn;
    BorderPane pane;
    AnchorPane aPane;

    AppScreen screen = new AppScreen();

    ObservableList<SearchProduct> data;

    // Variables for create the client
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<SearchProduct>> listc = new GenericType<List<SearchProduct>>() {
    };
    List<SearchProduct> searchProductList;

    String getProductURL;
    StoredProduct updateStoredProduct;
    String productItemCode;
    String quantity;
    String locationID;
    Boolean quantityEdited;

    public SearchProductItemDetailsController(){
        updateStoredProduct= new StoredProduct();
        quantityEdited= false;
    }

    // Show all the products and product items
    public void showAllProductItemDetails(String productItemCode){
        //connect to the server to retrieve the data
        data = tblViewSearchedProductDetails.getItems();
        data.clear();

        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        webResourceGet = client.resource("http://localhost:8080/rest/searchproduct/viewproductitemdetails").queryParam("productitemcode", productItemCode);
        response = webResourceGet.get(ClientResponse.class);
        searchProductList = response.getEntity(listc);
        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }
        if (searchProductList.isEmpty()) {
            screen.alertMessages("Non-Existent Product", "Product does not exist!");
        } else {
            for (SearchProduct s : searchProductList) {
                data.add(s);
            }
        }
    }
    public void editQuantity(){
        tblViewSearchedProductDetails.setEditable(true);
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        Button commitButton= new Button("Confirm");
        mainPanel.setBottom(commitButton);
        commitButton.setOnAction(e ->{
            TableCell<StoredProduct, StoredProduct> cell = new TableCell<StoredProduct, StoredProduct>() {
                @Override
                //the buttons are only displayed for the row have data
                public void updateItem(StoredProduct storedProduct, boolean empty) {
                    super.updateItem(storedProduct, empty);
                }
            };
            productItemCode = tblViewSearchedProductDetails.getSelectionModel().getSelectedItem().getProductItemCode();
            locationID = tblViewSearchedProductDetails.getSelectionModel().getSelectedItem().getLocationID();
            tblViewSearchedProductDetails.getSelectionModel().select(cell.getIndex());
            updateStoredProduct.setProductItemCode(productItemCode);
            updateStoredProduct.setLocationID(locationID);
            quantity = tblViewSearchedProductDetails.getSelectionModel().getSelectedItem().getProductQuantity();

            if (quantityEdited == false){
                System.out.println("Non edited");
                updateStoredProduct.setProductQuantity(quantity);
                screen.alertMessages("Edit Fail", "Please press enter to commit quantity edit value.");
            }
            else{
                System.out.println("Edited value");
            }

            //creating a new client to send post request
            ClientConfig clientConfig= new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client= Client.create(clientConfig);
            String updateURL= "http://localhost:8080/rest/update/updatestoredproduct";
            WebResource webResourcePost= client.resource(updateURL);
            //use the object passed as a parameter to send a request
            ClientResponse response= webResourcePost.type("application/json").put(ClientResponse.class, updateStoredProduct);
            response.bufferEntity();
            String responseValue= response.getEntity(String.class);
            if (responseValue.equals("updated")){
                screen.alertMessages("Product Updated!", "The Product Quantity of " + productItemCode + " has been updated in " + locationID);
                FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageProduct.fxml"));
                try {
                    AnchorPane aPane = loader.load();

                ManageProductController manageProductController= loader.getController();
                manageProductController.showAllProducts();
                anchorPane.getChildren().setAll(aPane);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else{
                screen.alertMessages("Error!", "Product could not be updated!");
            }
        });

    }

    @FXML
    private void onCommit(TableColumn.CellEditEvent<StoredProduct,String> productStringCellEditEvent){
        System.out.println("Edit Commited!");
        this.quantityEdited= true;
        quantity = tblViewSearchedProductDetails.getSelectionModel().getSelectedItem().getProductQuantity();
        updateStoredProduct.setProductQuantity(productStringCellEditEvent.getNewValue());
    }

    public void handleBackButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/SearchProductItemFXML.fxml"));
        aPane = loader.load();
        SearchProductItemController controller= loader.getController();
        controller.showAllProductItems(controller.getSelectedProductCode());
        anchorPane.getChildren().setAll(aPane);
    }

    public void handleMainMenuButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
        aPane = loader.load();
        HomePageController controller= loader.getController();
        controller.checkStaff();
        anchorPane.getChildren().setAll(aPane);
    }
}
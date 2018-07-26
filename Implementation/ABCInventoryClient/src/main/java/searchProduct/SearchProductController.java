package searchProduct;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.SearchProduct;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import manageProduct.AppScreen;

import javax.ws.rs.WebApplicationException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchProductController {
    // Initialize variables
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<SearchProduct> tblSearchProduct;
    @FXML
    private TableColumn size;
    @FXML
    private  TableColumn productItemCode;
    @FXML
    private TableColumn displayView;

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

    // Initialize method for handle the action when pressing button
    public void initialize(URL url, ResourceBundle rb){
        handleSearchProductCodeAction();
    }

    @FXML
    // Event handler for search button
    private void handleSearchProductCodeAction(){

        // GET request to searchproductcode resource with a query parameter
        String code = txtSearch.getText().toUpperCase();
        getProductURL = "http://localhost:8080/rest/searchproduct/searchproductcode/";


        if (code.matches("[A-Z][0-9]")){
            searchProductByCodes(getProductURL,"productcode", code);
            clearproductItems();
            addDetailButton();
        }else if(code.matches("[A-Z][0-9]100") || code.matches("[A-Z][0-9]200") || code.matches("[A-Z][0-9]300")){
            searchProductByCodes(getProductURL,"productcode", code);
            clearproductItems();
            addProductItems();
        }
        else if(code.equals("")){
            showAllProducts();
        } else {
            screen.alertMessages("Wrong Format Input","Please enter the code in the right format! \nEg: Produc Code: S1, Product Item Code: S1100 ");
        }
    }

    // Search Product or Product Item
    private  void searchProductByCodes(String URL ,String searchField, String code) {
        data.clear();
        webResourceGet = client.resource(URL).queryParam(searchField, code);
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

    //Add the button for each row
    private void addDetailButton(){
        // Create the "Detail" button for each row and define the action for it
        displayView.setCellFactory(col ->{
            Button viewButton = new Button("Detail");
            Button deleteButton= new Button("Delete");
            HBox hBox= new HBox(viewButton, deleteButton);
            TableCell<SearchProduct, SearchProduct> cell = new TableCell<SearchProduct, SearchProduct>() {
                @Override
                //the buttons are only displayed for the row have data
                public void updateItem(SearchProduct product, boolean empty) {
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
                tblSearchProduct.getSelectionModel().select(cell.getIndex());
                webResourceGet = client.resource("http://localhost:8080/rest/searchproduct/viewproductitem/").queryParam("productcode", tblSearchProduct.getSelectionModel().getSelectedItem().getProductCode()).queryParam("locationID",tblSearchProduct.getSelectionModel().getSelectedItem().getLocationID());
                response = webResourceGet.get(ClientResponse.class);
                searchProductList = response.getEntity(listc);

                data.clear();
                tblSearchProduct.getColumns().remove(displayView);
                addProductItems();

                if (response.getStatus() != 200) {
                    throw new WebApplicationException();
                }
                for (SearchProduct s : searchProductList) {
                    data.add(s);
                }
            });
            //when user click delete button do this
            deleteButton.setOnAction(e ->{
                tblSearchProduct.getSelectionModel().select(cell.getIndex());
                //getting the selected product code
                String toDeleteProduct= tblSearchProduct.getSelectionModel().getSelectedItem().getProductCode();
                System.out.println(toDeleteProduct);
                //creating a new client to delete the selected product
                ClientConfig clientConfig= new DefaultClientConfig();
                clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
                Client client= Client.create(clientConfig);
                WebResource deleteResource= client.resource("http://localhost:8080/rest/delete/deleteproduct/"+ toDeleteProduct);
                //converting the response to string
                ClientResponse response= deleteResource.delete(ClientResponse.class);
                response.bufferEntity();
                String responseValue= response.getEntity(String.class);
                if (responseValue.equals("true")){
                    screen.alertMessages("Product Deleted!", "The Product " + toDeleteProduct + " has been deleted.");
                }
                else{
                    screen.alertMessages("Error!", "An error occurred. Could not delete the product!");
                }
            });
            return cell ;
        });
        tblSearchProduct.getColumns().add(0,displayView);
    }

    // Update the table depends on the searching input
    private void addProductItems(){
        tblSearchProduct.getColumns().add(1,productItemCode);
        tblSearchProduct.getColumns().add(2,size);
    }

    // Update the table depends on the searching input
    private void clearproductItems(){
        tblSearchProduct.getColumns().remove(size);
        tblSearchProduct.getColumns().remove(productItemCode);
        tblSearchProduct.getColumns().remove(displayView);
    }

    // Show all the products and product items
    public void showAllProducts(){
        //connect to the server to retrieve the data
        data = tblSearchProduct.getItems();
        data.clear();

        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        getProductURL="http://localhost:8080/rest/searchproduct/viewallproducts/";
        searchProductByCodes(getProductURL,"","");
        tblSearchProduct.getColumns().remove(displayView);
    }
}
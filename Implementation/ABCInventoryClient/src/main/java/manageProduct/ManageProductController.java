package manageProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import entityClass.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageProductController{
    AnchorPane pane;
    @FXML
    BorderPane borderPane;
    @FXML
    TextField searchField;
    @FXML
    Button searchButton;
    @FXML
    Button addNewProductButton;
    @FXML
    TableView<Product> productView;
    @FXML
    TableColumn actionColumn;

    private ObservableList<Product> data;
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<Product>> listc= new GenericType<List<Product>>() {};
    // listc;
    private List<Product> productList;
    AppScreen screen= new AppScreen();

    public void ManageProductController(){
        data= FXCollections.observableArrayList();
        productList= new ArrayList<>();
    }

    public void displayActionButtons() {
        // Create the "Detail" button for each row and define the action for it
        actionColumn.setCellFactory(col -> {
            Button updateButton = new Button("Edit");
            Button deleteButton = new Button("Delete");
            Button addProductItem= new Button("Add PI");
            Button showStoredProducts= new Button ("Show SP");
            HBox hBox = new HBox(updateButton, deleteButton, addProductItem, showStoredProducts);
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
            deleteButton.setOnAction(e ->{
                productView.getSelectionModel().select(cell.getIndex());
                //getting the selected staff username
                String toDeleteProduct= productView.getSelectionModel().getSelectedItem().getProductCode();
                System.out.println(toDeleteProduct);
                Alert alert= new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete product " + toDeleteProduct + "?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                alert.setTitle("Warning!");
                if (alert.getResult()== ButtonType.YES){
                    //creating a new client to delete the selected product
                    ClientConfig clientConfig= new DefaultClientConfig();
                    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
                    Client client= Client.create(clientConfig);
                    WebResource deleteResource= client.resource("http://localhost:8080/rest/delete/deleteproduct/"+ toDeleteProduct);
                    //converting the response to string
                    ClientResponse response= deleteResource.delete(ClientResponse.class);
                    response.bufferEntity();
                    if (response.getStatus() == 200){
                        screen.alertMessages("Product Deleted!", "The Product " + toDeleteProduct + " has been deleted.");
                        showAllProducts();
                    }
                    else{
                        screen.alertMessages("Error!", "An error occurred. Could not delete the Product!");
                    }
                }
            });
            updateButton.setOnAction(e ->{
                try {
                    productView.getSelectionModel().select(cell.getIndex());
                    //getting the selected staff details and putting it into a staff object
                    Product selectedProduct= new Product();
                    selectedProduct.setProductCode(productView.getSelectionModel().getSelectedItem().getProductCode());
                    selectedProduct.setProductName(productView.getSelectionModel().getSelectedItem().getProductName());
                    selectedProduct.setPrice(productView.getSelectionModel().getSelectedItem().getPrice());
                    selectedProduct.setDescription(productView.getSelectionModel().getSelectedItem().getDescription());

                    //passing data from selected staff to be updated to UpdateStaffController and displaying update staff page
                    FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/UpdateProduct.fxml"));
                    pane = loader.load();
                    borderPane.getChildren().setAll(pane);
                    UpdateProductController updateProductController= loader.<UpdateProductController>getController();
                    updateProductController.setData(selectedProduct);
                }
                catch (IOException e1) {
                }
            });
            addProductItem.setOnAction(e ->{
                try {
                    productView.getSelectionModel().select(cell.getIndex());
                    //getting the selected staff username
                    String selectedProduct= productView.getSelectionModel().getSelectedItem().getProductCode();
                    BorderPane bPane;
                    FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddProductItem.fxml"));
                    bPane = loader.load();
                    borderPane.getChildren().setAll(bPane);
                    AddProductItemController addProductItemController= loader.<AddProductItemController>getController();
                    addProductItemController.setSelectedProductCode(selectedProduct);
                    addProductItemController.setComboBoxValues();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            showStoredProducts.setOnAction(e -> {

            });
            return cell;
        });
    }

    public void handleAddNewProductButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddProduct.fxml"));
        AnchorPane pane= loader.load();
        borderPane.getChildren().setAll(pane);
    }

    private  void searchProduct(String URL , String searchField, String code) {
        data.clear();
        webResourceGet = client.resource(URL).queryParam(searchField, code);
        response = webResourceGet.get(ClientResponse.class);
        productList = response.getEntity(listc);
        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }
        if (productList.isEmpty()) {
            screen.alertMessages("Product does not exist", "Sorry the Product you entered does not exist!");
        } else {
            for (Product p : productList) {
                data.add(p);
            }
        }
    }
    public void showAllProducts(){
        data= productView.getItems();
        data.clear();
        //creating a new client to send request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);
        String url="http://localhost:8080/rest/searchproduct/viewallproducts";
        searchProduct(url, "", "");
        displayActionButtons();
    }
    public void handleSearchButton(){
        data= productView.getItems();
        data.clear();
        String code= searchField.getText().toUpperCase();
        if (code.matches("[A-Z][0-9]")){
            //creating a new client to send request
            ClientConfig clientConfig= new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            client= Client.create(clientConfig);
            String url= "http://localhost:8080/rest/searchproduct/searchproductcode";
            searchProduct(url, "productcode", code);
        }
        else if(code.equals("")){
            showAllProducts();
        } else {
            screen.alertMessages("Wrong format!","Please enter the code in the right format! \nEg: Product Code: S1");
        }

    }



}
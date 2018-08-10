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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import manageProduct.AppScreen;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;

public class SearchProductItemController {

    // Initialize variables
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<ProductItem> tblSearchProductItemCode;
    @FXML
    private TableColumn displayView;
    @FXML
    private AnchorPane anchorPane;

    AppScreen screen = new AppScreen();
    SearchProductController searchProductController = new SearchProductController();

    ObservableList<ProductItem> data;

    // Variables for create the client
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<ProductItem>> listc = new GenericType<List<ProductItem>>() {
    };
    List<ProductItem> productItemList;


    public void handleSearchProductItemCodeAction(){
        // GET request to searchproductitemcode resource with a query parameter
        String code = txtSearch.getText().toUpperCase();

        if(code.matches("[A-Z][0-9]100") || code.matches("[A-Z][0-9]200") || code.matches("[A-Z][0-9]300")){
            searchProductItemCode("searchproductitemcode","productitemcode", code);
        }
        else if(code.equals("")){
            showAllProductItems(searchProductController.getSELECTED_PRODUCT_CODE());
        } else {
            screen.alertMessages("Wrong Format Input","Please enter the code in the right format! \nEg: Produc Code: S1, Product Item Code: S1100 ");
        }
    }

    // Search Product
    private  void searchProductItemCode(String path ,String searchField, String code) {
        data.clear();
        webResourceGet = client.resource("http://localhost:8080/rest/searchproduct/" + path).queryParam(searchField, code);
        response = webResourceGet.get(ClientResponse.class);
        productItemList = response.getEntity(listc);
        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }
        if (productItemList.isEmpty()) {
            screen.alertMessages("Non-Existent Product", "Product does not exist!");
        } else {
            for (ProductItem pi : productItemList) {
                data.add(pi);
            }
        }
    }

    public void showAllProductItems(String productCode){
        //connect to the server to retrieve the data
        data = tblSearchProductItemCode.getItems();
        data.clear();

        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        searchProductItemCode("viewsearchedproductitems","productcode",searchProductController.getSELECTED_PRODUCT_CODE());
        tblSearchProductItemCode.getColumns().remove(displayView);
        addDetailButton();
    }

    //Add the button for each row
    private void addDetailButton(){
        // Create the "Detail" button for each row and define the action for it
        displayView.setCellFactory(col ->{
            Button viewButton = new Button("Detail");
            HBox hBox= new HBox(viewButton);
            TableCell<ProductItem, ProductItem> cell = new TableCell<ProductItem, ProductItem>() {
                @Override
                //the buttons are only displayed for the row have data
                public void updateItem(ProductItem productItem, boolean empty) {
                    super.updateItem(productItem, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hBox);
                    }
                }
            };
            // When clicked the button, the button will show the product item of selected product
            viewButton.setOnAction(e -> {
                tblSearchProductItemCode.getSelectionModel().select(cell.getIndex());
                String selectedProductItemCode = tblSearchProductItemCode.getSelectionModel().getSelectedItem().getProductItemCode();
                //load text fields and labels for adding product item
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SearchProductItemDetailsFXML.fxml"));
                AnchorPane pane = null;
                try {
                    pane = loader.load();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                SearchProductItemDetailsController myController = loader.getController();

                //Set Data to FXML through controller
                myController.showAllProductItemDetails(selectedProductItemCode);
                anchorPane.getChildren().setAll(pane);
            });
            return cell ;
        });
        tblSearchProductItemCode.getColumns().add(0,displayView);
    }

}

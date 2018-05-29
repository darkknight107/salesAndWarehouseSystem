package searchProduct;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.security.auth.callback.Callback;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
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
    private  TextField txtSearchProductItemCode;
    @FXML
    private TableColumn size;
    @FXML
    private  TableColumn productItemCode;
    @FXML
    private TableColumn displayView;
    WebTarget clientTarget = null;
    ObservableList<SearchProduct> data;


    // Initialize method for handle the action when pressing button
    public void initialize(URL url, ResourceBundle rb){
        handleSearchProductCodeAction();
    }

    @FXML
    private void handleSearchProductCodeAction(){
        // Get the data from Webservice
        data = tblSearchProduct.getItems();
        data.clear();
        Client client = ClientBuilder.newClient();
        client.register(SearchProductMessageBodyReader.class);

        // Create the "Detail" button for each row and define the action for it
        displayView.setCellFactory(col ->{
            Button viewButton = new Button("Detail");
            TableCell<SearchProduct, SearchProduct> cell = new TableCell<SearchProduct, SearchProduct>() {
                @Override
                //the buttons are only displayed for the row have data
                public void updateItem(SearchProduct product, boolean empty) {
                    super.updateItem(product, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(viewButton);
                    }
                }
            };

            // When clicked the button, the button will show the product item of selected product
            viewButton.setOnAction(e -> {
                tblSearchProduct.getSelectionModel().select(cell.getIndex());
                viewProductItems(tblSearchProduct.getSelectionModel().getSelectedItem().getProductCode(),tblSearchProduct.getSelectionModel().getSelectedItem().getLocationID());
            });

            return cell ;
        });
        if(txtSearch.getText().length() == 0 && txtSearchProductItemCode.getText().length() == 0){
            alertMessages("No Product Searched","Please input the Product Code/ Product Item Code you want to search.");
            clearTextFields();
        }
        else if(txtSearch.getText().length() > 0 && txtSearchProductItemCode.getText().length() > 0){
            alertMessages("Duplicate Search","Cannot search the Product Code and Product Item Code at the same time.");
            clearTextFields();
        }
//         The results will be displayed when user searching the product code or product item code
        else if (txtSearch.getText().length() > 0  && txtSearchProductItemCode.getText().length() == 0) {
            clientTarget = client.target("http://abcinventoryserver.ap-southeast-2.elasticbeanstalk.com/rest/searchproduct/searchproductcode/{beginBy}");
            clientTarget = clientTarget.resolveTemplate("beginBy", txtSearch.getText());
            clearproductItems();
            tblSearchProduct.getColumns().add(0,displayView);
        }
        else if (txtSearch.getText().length() == 0  && txtSearchProductItemCode.getText().length() > 0) {
            clientTarget = client.target("http://abcinventoryserver.ap-southeast-2.elasticbeanstalk.com/rest/searchproduct/searchproductcode/{beginBy}");
            clientTarget = clientTarget.resolveTemplate("beginBy", txtSearchProductItemCode.getText());
            clearproductItems();
            addProductItems();
        }
        // Get the json data from web service
        GenericType<List<SearchProduct>> listc = new GenericType<List<SearchProduct>>() {
        };
        List<SearchProduct> searchProductList = clientTarget.request("application/json").get(listc);
        if(searchProductList.isEmpty()){
            alertMessages("Non-Existent Product", "Product does not exist!");
        }else {
            if (txtSearch.getText().length() > 0 || txtSearchProductItemCode.getText().length() > 0) {
                // Add data to SearchProduct for displaying
                for (SearchProduct s : searchProductList) {
                    data.add(s);
                    System.out.println(s.toString());
                }
                clearTextFields();
            }
        }
        }


    // When clicked the button, the button will show the product item of selected product
    private void viewProductItems(String productCode, String locationID){
//        ObservableList<SearchProduct> data = tblSearchProduct.getItems();
        data.clear();
        Client client = ClientBuilder.newClient();
        client.register(SearchProductMessageBodyReader.class);
        clientTarget = client.target("http://abcinventoryserver.ap-southeast-2.elasticbeanstalk.com/rest/searchproduct/viewproductitems/{beginBy}");
        String s = productCode + "-" + locationID;
        clientTarget = clientTarget.resolveTemplate("beginBy", s);
        tblSearchProduct.getColumns().remove(displayView);
        addProductItems();
        GenericType<List<SearchProduct>> listc = new GenericType<List<SearchProduct>>() {
        };
        List<SearchProduct> searchProductList = clientTarget.request("application/json").get(listc);

        for (SearchProduct a : searchProductList) {
            data.add(a);
            System.out.println(a.toString());
        }
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

    private void clearTextFields(){
        txtSearch.setText("");
        txtSearchProductItemCode.setText("");
    }

    private void alertMessages(String title, String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
        clearTextFields();
        return;
    }
}
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


    public void initialize(URL url, ResourceBundle rb){
        handleSearchProductCodeAction();
    }

    @FXML
    private void handleSearchProductCodeAction(){

        ObservableList<SearchProduct> data = tblSearchProduct.getItems();
        data.clear();
        Client client = ClientBuilder.newClient();
        client.register(SearchProductMessageBodyReader.class);

        displayView.setCellFactory(col ->{
            Button viewButton = new Button("Detail");
            TableCell<SearchProduct, SearchProduct> cell = new TableCell<SearchProduct, SearchProduct>() {
                @Override
                public void updateItem(SearchProduct product, boolean empty) {
                    super.updateItem(product, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(viewButton);
                    }
                }
            };

            viewButton.setOnAction(e -> {
                tblSearchProduct.getSelectionModel().select(cell.getIndex());
                System.out.println(tblSearchProduct.getSelectionModel().getSelectedItem().getProductCode());
                System.out.println(tblSearchProduct.getSelectionModel().getSelectedItem().getLocationID());
                viewProductItems(tblSearchProduct.getSelectionModel().getSelectedItem().getProductCode(),tblSearchProduct.getSelectionModel().getSelectedItem().getLocationID());
            });

            return cell ;
        });

        if (txtSearch.getText().length() > 0  && txtSearchProductItemCode.getText().length() == 0) {
            clientTarget = client.target("http://abcinventoryserver.ap-southeast-2.elasticbeanstalk.com/rest/searchproduct/searchproductcode/{beginBy}");
            clientTarget = clientTarget.resolveTemplate("beginBy", txtSearch.getText());
            clearproductItems();
            tblSearchProduct.getColumns().add(0,displayView);
        } else if (txtSearch.getText().length() == 0  && txtSearchProductItemCode.getText().length() > 0) {
            clientTarget = client.target("http://abcinventoryserver.ap-southeast-2.elasticbeanstalk.com/rest/searchproduct/searchproductcode/{beginBy}");
            clientTarget = clientTarget.resolveTemplate("beginBy", txtSearchProductItemCode.getText());
            clearproductItems();
            addProductItems();
        }
        GenericType<List<SearchProduct>> listc = new GenericType<List<SearchProduct>>() {
        };
        List<SearchProduct> searchProductList = clientTarget.request("application/json").get(listc);

        for (SearchProduct s : searchProductList) {
            data.add(s);
            System.out.println(s.toString());
        }
    }

    private void viewProductItems(String productCode, String locationID){
        ObservableList<SearchProduct> data = tblSearchProduct.getItems();
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

    private void addProductItems(){
        tblSearchProduct.getColumns().add(1,productItemCode);
        tblSearchProduct.getColumns().add(2,size);
    }
    private void clearproductItems(){
        tblSearchProduct.getColumns().remove(size);
        tblSearchProduct.getColumns().remove(productItemCode);
        tblSearchProduct.getColumns().remove(displayView);
        txtSearch.setText("");
        txtSearchProductItemCode.setText("");
    }
}
package transferProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import entityClass.SearchProduct;
import entityClass.StoredProduct;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.List;

public class TransferItemController {

    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<SearchProduct> tblCurrentStoredProduct;
    @FXML
    private TableView<SearchProduct> tblCart;

    ObservableList<StoredProduct> dataCurrentStoredProduct;
    ObservableList<StoredProduct> dataCart;

    // Variables for create the client
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<StoredProduct>> listc = new GenericType<List<StoredProduct>>() {
    };
    List<StoredProduct> storedProductList;

    String getProductURL;

    @FXML
    public void handleSearchProductCodeAction(){

    }

    @FXML
    public void handleAddToCartAction(){}
}

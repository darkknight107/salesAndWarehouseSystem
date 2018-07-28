package transferProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.SearchProduct;
import entityClass.StoredProduct;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import manageProduct.AppScreen;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

public class TransferItemController {

    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<StoredProduct> tblCurrentStoredProduct;
    @FXML
    private TableView<StoredProduct> tblCart;
    @FXML
    private TableColumn productQuantityCart;

    ObservableList<StoredProduct> dataStoredProduct;
    ObservableList<StoredProduct> dataCart;

    // Variables for create the client
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<StoredProduct>> listc = new GenericType<List<StoredProduct>>() {
    };
    List<StoredProduct> storedProductList;
    AppScreen screen = new AppScreen();

    String getProductURL;



    @FXML
    public void handleSearchProductCodeAction(){
        String code = txtSearch.getText().toUpperCase();
        getProductURL = "http://localhost:8080/rest/transferproduct/searchstoredproducts/";

        if(code.matches("[A-Z][0-9]100") || code.matches("[A-Z][0-9]200") || code.matches("[A-Z][0-9]300")){
            searchStoredProductByCodes(getProductURL,"productitemcode", code);
        }
        else if(code.matches("[A-Z][A-Z][A-Z][0-9]")){
            searchStoredProductByCodes("http://localhost:8080/rest/transferproduct/searchstoredproductsbylocation/","locationID", code);
        }
        else if(code.equals("")){
            showAllStoredProducts();
        } else {
            screen.alertMessages("Wrong Format Input","Please enter the code in the right format! \nEg: Product Item Code: S1100, Location ID: WRH1");
        }
    }

    @FXML
    public void handleAddToCartAction(){
        String locationID = tblCurrentStoredProduct.getSelectionModel().getSelectedItem().getLocationID();
        String productItemCode = tblCurrentStoredProduct.getSelectionModel().getSelectedItem().getProductItemCode();
        String productQuantity = "1";
        Boolean flag = false;
        StoredProduct cartStoredProduct = new StoredProduct(productItemCode,locationID,productQuantity);;

        dataCart= tblCart.getItems();

        if(dataCart.isEmpty()) {
            dataCart.add(cartStoredProduct);
        }else{
            for (StoredProduct st: dataCart) {
                if ((locationID == st.getLocationID()) & (productItemCode == st.getProductItemCode())) {
                    screen.alertMessages("Dupplicate Product", "The product has been added to cart!");
                    flag = true;
                }
            }
            if (flag == false) {
                dataCart.add(cartStoredProduct);
            }
        }

        tblCart.setEditable(true);
        productQuantityCart.setCellFactory(TextFieldTableCell.forTableColumn());


    }

    @FXML
    private void onEditChange(TableColumn.CellEditEvent<StoredProduct,String> productStringCellEditEvent){
        StoredProduct storedProduct = tblCart.getSelectionModel().getSelectedItem();
        String locationIDCart = tblCart.getSelectionModel().getSelectedItem().getLocationID();
        String productItemCodeCart = tblCart.getSelectionModel().getSelectedItem().getProductItemCode();
        String productQuantityCart = productStringCellEditEvent.getNewValue();
        Boolean flag = false;
        int qtyCart = Integer.parseInt(productQuantityCart);

        for (StoredProduct st: dataStoredProduct) {
            int qtyStoredProduct = Integer.parseInt(st.getProductQuantity());
            if ((locationIDCart == st.getLocationID()) & (productItemCodeCart == st.getProductItemCode())) {
                if (qtyCart>qtyStoredProduct) {
                    screen.alertMessages("Not Enough Product", "The quantity of product should be less than the quantity of current stock");
                    flag = true;
                    // workaround for refreshing rendered values
                    productStringCellEditEvent.getTableView().getColumns().get(2).setVisible(false);
                    productStringCellEditEvent.getTableView().getColumns().get(2).setVisible(true);
                }
            }
        }
        if (flag == false) {
            storedProduct.setProductQuantity(productStringCellEditEvent.getNewValue());
        }
    }

    @FXML
    public void handleRemoveFromCartAction(){
        dataCart.remove(tblCart.getSelectionModel().getSelectedItem());
    }



    @FXML
    public void handleSendProducts(){

    }

    // Search Product or Product Item
    private  void searchStoredProductByCodes(String URL ,String searchField, String code) {
        dataStoredProduct.clear();
        webResourceGet = client.resource(URL).queryParam(searchField, code);
        response = webResourceGet.get(ClientResponse.class);
        storedProductList = response.getEntity(listc);
        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }
        if (storedProductList.isEmpty()) {
            screen.alertMessages("Non-Existent Product", "Product does not exist!");
        } else {
            for (StoredProduct s : storedProductList) {
                dataStoredProduct.add(s);
            }
        }
    }

    public void showAllStoredProducts(){
        //connect to the server to retrieve the data
        dataStoredProduct = tblCurrentStoredProduct.getItems();
        dataStoredProduct.clear();

        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        getProductURL="http://localhost:8080/rest/transferproduct/viewallstoredproducts/";
        searchStoredProductByCodes(getProductURL,"","");
    }
}

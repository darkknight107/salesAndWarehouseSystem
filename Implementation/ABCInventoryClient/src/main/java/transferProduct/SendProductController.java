package transferProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import entityClass.StoredProduct;
import entityClass.Transfer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import manageProduct.AppScreen;
import searchAccount.SearchAccountController;
import searchProduct.SearchProductItemController;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SendProductController {
    //initialize variables
    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox cbDestinationLocation;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<StoredProduct> tblCurrentStoredProduct;
    @FXML
    private TableView<StoredProduct> tblCart;
    @FXML
    private TableColumn productQuantityCart;
    @FXML
    private TableColumn locationName;
    @FXML
    AnchorPane anchorPane;

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

    SearchAccountController searchAccountController = new SearchAccountController();

    String getProductURL;
    String code = "";
    String currentSendLocation = searchAccountController.getCurrentLocationId();



    //event handler for search button
    @FXML
    public void handleSearchProductCodeAction(){
        code = txtSearch.getText().toUpperCase();
        getProductURL = "http://localhost:8080/rest/transferproduct/searchstoredproducts/";

        if(!code.isEmpty()){
            searchStoredProductByCombinationCodes("http://localhost:8080/rest/transferproduct/searchstoredproductsbycominationcodes/",currentSendLocation,code);
        }else if(code.isEmpty()){

            if(code.matches("[A-Z][0-9]100") || code.matches("[A-Z][0-9]200") || code.matches("[A-Z][0-9]300")){
                searchStoredProductByCodes(getProductURL,"productitemcode", code);
            }
            else if(code.equals("")){
                searchStoredProductByCodes("http://localhost:8080/rest/transferproduct/searchstoredproductsbylocation/","locationID", currentSendLocation);
            } else {
                screen.alertMessages("Wrong Format Input","Please enter the code in the right format! \nEg: Product Item Code: S1100, Location ID: WRH1");
            }
        }

    }

    //event handler for send location combo box
    public void handleComboBoxLocation(){
            cbDestinationLocation.getItems().removeAll();
            switch (currentSendLocation){
                case "WRH1":
                    cbDestinationLocation.getItems().add("Oxford Store");
                    cbDestinationLocation.getItems().add("Epping Store");
                    break;
                case "STR1":
                    cbDestinationLocation.getItems().add("Newtown Warehouse");
                    cbDestinationLocation.getItems().add("Epping Store");
                    break;
                case "STR2":
                    cbDestinationLocation.getItems().add("Newtown Warehouse");
                    cbDestinationLocation.getItems().add("Oxford Store");
                    break;
                default:
                    break;
            }

    }

    //event handler for add to cart button
    @FXML
    public void handleAddToCartAction(){

        String locationID = tblCurrentStoredProduct.getSelectionModel().getSelectedItem().getLocationID();
        String productItemCode = tblCurrentStoredProduct.getSelectionModel().getSelectedItem().getProductItemCode();
        String storedProductQuantity = tblCurrentStoredProduct.getSelectionModel().getSelectedItem().getProductQuantity();
        String productQuantity = "1";

        Boolean flag = false;
        StoredProduct cartStoredProduct = new StoredProduct(productItemCode,locationID,productQuantity);;

        dataCart= tblCart.getItems();
        if (!cbDestinationLocation.getSelectionModel().isEmpty()) {
            if(Integer.parseInt(storedProductQuantity)<= 0){
                screen.alertMessages("Not Enough Product", "The selected product is out of stock at this moment, please select other products!");
                flag = true;
            }else if(dataCart.isEmpty()) {
                dataCart.add(cartStoredProduct);
            }else{
                for (StoredProduct st: dataCart) {
                    if ((locationID.equals(st.getLocationID())) & (productItemCode.equals(st.getProductItemCode()))) {
                        screen.alertMessages("Dupplicate Product", "The product has been added to cart!");
                        flag = true;
                    }else if(!locationID.equals(st.getLocationID())){
                        screen.alertMessages("Multiple Send Location", "The items should be sent from 1 location at a time.");
                        flag = true;
                        break;
                    }
                }
                if (flag == false) {
                    dataCart.add(cartStoredProduct);
                }
            }
        } else{
            screen.alertMessages("Invalid Destination Location", "Please select the destination location");
        }

        tblCart.setEditable(true);
        productQuantityCart.setCellFactory(TextFieldTableCell.forTableColumn());


    }

    //event handler for table column
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

    //event handler or remove from cart button
    @FXML
    public void handleRemoveFromCartAction(){
        dataCart.remove(tblCart.getSelectionModel().getSelectedItem());
    }



    //event handler for send button
    @FXML
    public void handleSendProducts() throws IOException {
        if(addTransfer() == true) {
            addTransferItem();
        } else {
            screen.alertMessages("Invalid Location", "Please select location before send!");
        }
    }

    //add Transfer to database
    public Boolean addTransfer(){
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Boolean flag = false;

        //creating variables to get product details entered by user in text fields
        String destinationLocation= cbDestinationLocation.getValue().toString();
        String destinationLocationID = "";
        switch (destinationLocation){
            case "Newtown Warehouse":
                destinationLocationID = "WRH1";
                break;
            case "Oxford Store":
                destinationLocationID = "STR1";
                break;
            case "Epping Store":
                destinationLocationID = "STR2";
                break;
            default:
                break;
        }
        String date = dtf.format(cal.getTime());
        String status= "Sending";
        String description= txtDescription.getText();

        //set textfield values to Product Entity
        Transfer t = new Transfer(currentSendLocation,destinationLocationID,date,status,description);

        List<Transfer> transferList = new ArrayList<Transfer>();
        transferList.add(t);

        //call the clientRequest method to send a request to the server
        String response= clientRequestPost(transferList,"addtransfer");
        System.out.println(response);
        if (response.equals("true")){
            flag = true;
        }

        return flag;
    }

    //add TransferItem to database
    public void addTransferItem() throws IOException {
        StoredProduct storedProduct;
        List<StoredProduct> storedProductList = new ArrayList<StoredProduct>();
        //creating variables to get product details entered by user in text fields
        for (StoredProduct st : dataCart) {
            String productItemCode= st.getProductItemCode();
            String locationID= st.getLocationID();
            String productQuantity = st.getProductQuantity();
            storedProduct = new StoredProduct(productItemCode,locationID,productQuantity);
            storedProductList.add(storedProduct);
        }

        //call the clientRequest method to send a request to the server
        String responseAddTransferItem = clientRequestPost(storedProductList,"addtransferitem");
        if (responseAddTransferItem.equals("true")){
            String responseUpdate = clientRequestPut(storedProductList,"updateproductquantity");
            screen.alertMessages("Transfer Items Added", "Transfer Items has been added.");
            dataCart.clear();

            //load text fields and labels for adding product item
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SendProductFXML.fxml"));
            AnchorPane pane = loader.load();

            SendProductController myController = loader.getController();

            //Set Data to FXML through controller
            myController.showAllStoredProducts();
            anchorPane.getChildren().setAll(pane);
        }
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

    //search the stored product by combining locationID and productItemCode
    private  void searchStoredProductByCombinationCodes(String URL, String locationID, String productItemCode) {
        dataStoredProduct.clear();
        webResourceGet = client.resource(URL).queryParam("locationID",locationID).queryParam("productItemCode",productItemCode);
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

    //client requests for insert
    public String clientRequestPost(Object entity, String path){
        String postURL= "http://localhost:8080/rest/transferproduct/" + path;
        WebResource webResourcePost= client.resource(postURL);
        //use the object passed as a parameter to send a request
        response= webResourcePost.type("application/json").post(ClientResponse.class, entity);
        response.bufferEntity();
        String responseValue= response.getEntity(String.class);
        return responseValue;
    }

    //client request for update
    public String clientRequestPut(Object entity, String path){
        String postURL= "http://localhost:8080/rest/transferproduct/" + path;
        WebResource webResourcePost= client.resource(postURL);
        //use the object passed as a parameter to send a request
        response= webResourcePost.type("application/json").put(ClientResponse.class, entity);
        response.bufferEntity();
        String responseValue= response.getEntity(String.class);
        return responseValue;
    }

    //show all the Stored Product when initialize the sceen
    public void showAllStoredProducts(){
        //connect to the server to retrieve the data
        dataStoredProduct = tblCurrentStoredProduct.getItems();
        String test1 = tblCurrentStoredProduct.getItems().toString();
        System.out.println(test1);
        dataStoredProduct.clear();

        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        searchStoredProductByCodes("http://localhost:8080/rest/transferproduct/searchstoredproductsbylocation/","locationID", currentSendLocation);
        handleComboBoxLocation();
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

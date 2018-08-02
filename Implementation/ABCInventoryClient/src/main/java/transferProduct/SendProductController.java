package transferProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.StoredProduct;
import entityClass.Transfer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import manageProduct.AppScreen;

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
    private ComboBox cbSendLocation;
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

    String getProductURL;
    String sendLocation="";
    String code = "";



    //event handler for search button
    @FXML
    public void handleSearchProductCodeAction(){
        code = txtSearch.getText().toUpperCase();
        getProductURL = "http://localhost:8080/rest/transferproduct/searchstoredproducts/";

        if(!code.isEmpty() & !sendLocation.isEmpty()){
            searchStoredProductByCombinationCodes("http://localhost:8080/rest/transferproduct/searchstoredproductsbycominationcodes/",sendLocation,code);
        }else if(code.isEmpty() || sendLocation.isEmpty()){

            if(code.matches("[A-Z][0-9]100") || code.matches("[A-Z][0-9]200") || code.matches("[A-Z][0-9]300")){
                searchStoredProductByCodes(getProductURL,"productitemcode", code);
            }
            else if(code.equals("")){
                searchStoredProductByCodes("http://localhost:8080/rest/transferproduct/searchstoredproductsbylocation/","locationID", cbSendLocation.getSelectionModel().getSelectedItem().toString());
            } else {
                screen.alertMessages("Wrong Format Input","Please enter the code in the right format! \nEg: Product Item Code: S1100, Location ID: WRH1");
            }
        }

    }

    //event handler for send location combo box
    @FXML
    public void handleComboBoxLocation(){
        sendLocation = cbSendLocation.getValue().toString();
            switch (sendLocation){
                case "WRH1":
                    cbDestinationLocation.getItems().removeAll(cbSendLocation.getItems());
                    cbDestinationLocation.getItems().add("STR1");
                    cbDestinationLocation.getItems().add("STR2");
                    break;
                case "STR1":
                    cbDestinationLocation.getItems().removeAll(cbSendLocation.getItems());
                    cbDestinationLocation.getItems().add("WRH1");
                    cbDestinationLocation.getItems().add("STR2");
                    break;
                case "STR2":
                    cbDestinationLocation.getItems().removeAll(cbSendLocation.getItems());
                    cbDestinationLocation.getItems().add("WRH1");
                    cbDestinationLocation.getItems().add("STR1");
                    break;
                default:
                    break;
            }
        System.out.println(cbDestinationLocation.getItems());

        if(code.isEmpty()){
        searchStoredProductByCodes("http://localhost:8080/rest/transferproduct/searchstoredproductsbylocation/","locationID", sendLocation);}
        else{
            handleSearchProductCodeAction();
        }

    }

    //event handler for add to cart button
    @FXML
    public void handleAddToCartAction(){
        String locationID = tblCurrentStoredProduct.getSelectionModel().getSelectedItem().getLocationID();
        String productItemCode = tblCurrentStoredProduct.getSelectionModel().getSelectedItem().getProductItemCode();
        String productQuantity = "1";
        String sendingLocation;
        Boolean flag = false;
        StoredProduct cartStoredProduct = new StoredProduct(productItemCode,locationID,productQuantity);;

        dataCart= tblCart.getItems();
        if (!cbSendLocation.getSelectionModel().isEmpty()) {
            if(dataCart.isEmpty()) {
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
            screen.alertMessages("Invalid Sending Location", "Please select the sending location");
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
        String date = dtf.format(cal.getTime());
        String status= "Sending";
        String description= txtDescription.getText();

        //set textfield values to Product Entity
        Transfer t = new Transfer(sendLocation,destinationLocation,date,status,description);

        List<Transfer> transferList = new ArrayList<Transfer>();
        transferList.add(t);

        //call the clientRequest method to send a request to the server
        String response= clientRequestPost(transferList,"addtransfer");
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
        dataStoredProduct.clear();

        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        getProductURL="http://localhost:8080/rest/transferproduct/viewallstoredproducts/";
        searchStoredProductByCodes(getProductURL,"","");
    }
}

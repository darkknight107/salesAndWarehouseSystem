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
import entityClass.Transfer;
import entityClass.TransferItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import manageProduct.AppScreen;

import javax.ws.rs.WebApplicationException;
import java.util.List;

public class AcceptProductController {

    //initialize variables
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Transfer> tblTransfer;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TableColumn displayView;

    ObservableList<Transfer> data;
    ObservableList<TransferItem> dataTransferItems;
    List<Transfer> transferList;
    List<TransferItem> transferItemList;

    // Variables for create the client
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<Transfer>> listc = new GenericType<List<Transfer>>() {
    };
    GenericType<List<TransferItem>> listcTransferItems = new GenericType<List<TransferItem>>() {
    };
    AppScreen screen = new AppScreen();

    String getTransferURL;
    public void handleSearchTransferAction(){
        System.out.println(tblTransfer.getSelectionModel().getSelectedItem().getTransferID());
    }

    // Search Transfer or Transfer ID
    private  void searchTransferTransferID(String URL ,String searchField, String code) {
        data.clear();
        webResourceGet = client.resource(URL).queryParam(searchField, code);
        response = webResourceGet.get(ClientResponse.class);
        transferList = response.getEntity(listc);
        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }
        if (transferList.isEmpty()) {
            screen.alertMessages("Non-Existent Transfer", "Transfer does not exist!");
        } else {
            for (Transfer t : transferList) {
                data.add(t);
            }
        }
    }

    //show all the Transfer when initialize the sceen
    public void showAllTransfer(){
        //connect to the server to retrieve the data
        data = tblTransfer.getItems();
        data.clear();
        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        getTransferURL="http://localhost:8080/rest/transferproduct/displaysendingtransfer/";
        searchTransferTransferID(getTransferURL,"","");
        addDetailButton();
    }

    //Add the button for each row
    private void addDetailButton(){
        // Create the "Detail" button for each row and define the action for it
        displayView.setCellFactory(col ->{
            Button viewButton = new Button("Detail");
            TableCell<Transfer, Transfer> cell = new TableCell<Transfer, Transfer>() {
                @Override
                //the buttons are only displayed for the row have data
                public void updateItem(Transfer transfer, boolean empty) {
                    super.updateItem(transfer, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(viewButton);
                    }
                }
            };
            // When clicked the button, the button will show the product item of selected product
//            viewButton.setOnAction(e -> {
//                tblTransfer.getSelectionModel().select(cell.getIndex());
//                webResourceGet = client.resource("http://localhost:8080/rest/transferproduct/displaySendingTransferItem/").queryParam("transferID", tblTransfer.getSelectionModel().getSelectedItem().getTransferID());
//                response = webResourceGet.get(ClientResponse.class);
//                transferItemList = response.getEntity(listcTransferItems);
//
//                dataTransferItems.clear();
//                tblTransfer.getColumns().remove(displayView);
//
//                if (response.getStatus() != 200) {
//                    throw new WebApplicationException();
//                }
//                for (TransferItem s : transferItemList) {
//                    dataTransferItems.add(s);
//                }
//            });

            return cell ;
        });
//        tblTransfer.getColumns().add(0,displayView);
    }

}

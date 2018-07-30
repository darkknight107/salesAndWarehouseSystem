package transferProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Transfer;
import entityClass.TransferItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import manageProduct.AppScreen;

import javax.ws.rs.WebApplicationException;
import java.util.List;

public class AcceptTransferItemController {
    //initialize variables
    @FXML
    private TableView<TransferItem> tblTransferItem;
    @FXML
    AnchorPane anchorPane;

    ObservableList<TransferItem> data;
    List<TransferItem> transferItemList;

    // Variables for create the client
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<TransferItem>> listc = new GenericType<List<TransferItem>>() {
    };
    AppScreen screen = new AppScreen();

    String getTransferItemURL;

    public void handleAcceptProducts(){

    }

    // Search Transfer or Transfer ID
    private  void searchTransferItem(String URL ,String searchField, String code) {
        data.clear();
        webResourceGet = client.resource(URL).queryParam(searchField, code);
        response = webResourceGet.get(ClientResponse.class);
        transferItemList = response.getEntity(listc);
        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }
        if (transferItemList.isEmpty()) {
            screen.alertMessages("Non-Existent Transfer", "Transfer does not exist!");
        } else {
            for (TransferItem ti : transferItemList) {
                data.add(ti);
            }
        }
    }

    //show all the Transfer when initialize the sceen
    public void showAllTransferItem(String transferID){
        //connect to the server to retrieve the data
        data = tblTransferItem.getItems();
        data.clear();
        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        getTransferItemURL="http://localhost:8080/rest/transferproduct/displaySendingTransferItem/";
        searchTransferItem(getTransferItemURL,"transferID",transferID);
    }
}

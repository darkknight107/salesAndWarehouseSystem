/*
package reportTransfer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.StoredProduct;
import entityClass.TransferItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import manageProduct.AppScreen;
import transferProduct.AcceptProductController;
import transferProduct.SendProductController;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportTransferItemController {

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

    //show all the Transfer when initialize the sceen
    public void showAllTransferItem(String transferID){
        //connect to the server to retrieve the data
        data = tblTransferItem.getItems();
        data.clear();
        //creating a new client to send get request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);

        getTransferItemURL="http://localhost:8080/rest/transferproduct/displaysendingtransferitem/";
        searchTransferItem(getTransferItemURL,"transferID",transferID);
    }

    // Search Transfer or Transfer ID
    private  void searchTransferItem(String URL ,String searchField, String code) {
        data.clear();
        webResourceGet = client.resource(URL).queryParam(searchField, code);
        response = webResourceGet.get(ClientResponse.class);
        System.out.println(response);
        transferItemList = response.getEntity(listc);
        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }
        if (transferItemList.isEmpty()) {
            screen.alertMessages("Non-Existent Transfer Item", "Transfer Item does not exist!");
        } else {
            for (TransferItem ti : transferItemList) {
                data.add(ti);
            }
        }
    }


}
*/

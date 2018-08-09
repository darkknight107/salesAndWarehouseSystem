package reportTransfer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Transfer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import manageProduct.AppScreen;
import transferProduct.AcceptTransferItemController;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;

public class ReportTransferController {

    //initialize variables
    @FXML
    private TextField txtSearch;
    @FXML
    private TableView<Transfer> tblTransfer;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TableColumn displayView;

    private static String SELECTED_DESTINATION_LOCATION_ID;

    ObservableList<Transfer> data;
    List<Transfer> transferList;

    // Variables for create the client
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<Transfer>> listc = new GenericType<List<Transfer>>() {
    };
    AppScreen screen = new AppScreen();

    String getTransferURL;

    @FXML
    public void handleSearchTransferAction(){
        String transferID = txtSearch.getText();
        if(transferID.equals("")){
            showAllTransfer();
        }else {
            searchTransfer("http://localhost:8080/rest/transferproduct/searchTransfer/", "transferID", transferID);
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

        getTransferURL="http://localhost:8080/rest/transferproduct/viewalltransfer/";
        searchTransfer(getTransferURL,"","");
        addDetailButton();
    }

    // Search Transfer or Transfer ID
    private  void searchTransfer(String URL ,String searchField, String code) {
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
            viewButton.setOnAction(e -> {
                tblTransfer.getSelectionModel().select(cell.getIndex());
                String selectedTransferID = tblTransfer.getSelectionModel().getSelectedItem().getTransferID();
                //load text fields and labels for adding product item
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ReportTransferItemFXML.fxml"));
                AnchorPane pane = null;
                try {
                    pane = loader.load();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                AcceptTransferItemController myController = loader.getController();

                //Set Data to FXML through controller
                myController.showAllTransferItem(selectedTransferID);
                anchorPane.getChildren().setAll(pane);
            });

            return cell ;
        });
    }
}

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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import manageProduct.AppScreen;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
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

    public void handleSearchTransferAction(){
        String transferID = txtSearch.getText();
        if(transferID.equals("")){
            showAllTransfer();
        }else {
            searchTransfer("http://localhost:8080/rest/transferproduct/searchsendingtransfer/", "transferID", transferID);
        }
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
        searchTransfer(getTransferURL,"","");
        addDetailButton();
    }

    //Add the button for each row
    private void addDetailButton(){
        // Create the "Detail" button for each row and define the action for it
        displayView.setCellFactory(col ->{
            Image image = new Image("image/DetailButtonIcon.png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(15);
            imageView.setFitWidth(15);

            Button viewButton = new Button("", imageView);
            Tooltip tooltip = new Tooltip("View products in this transfer");
            tooltip.setShowDelay(Duration.millis(10));
            viewButton.setTooltip(tooltip);

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
                String selectedDestination = tblTransfer.getSelectionModel().getSelectedItem().getDestinationLocationID();
                setSELECTED_DESTINATION_LOCATION_ID(selectedDestination);
                //load text fields and labels for adding product item
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AcceptTransferItemFXML.fxml"));
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

    //Gettter and Setter for SELECTED_DESTINATION_LOCATION_ID
    public void setSELECTED_DESTINATION_LOCATION_ID(String SELECTED_DESTINATION_LOCATION_ID){
        this.SELECTED_DESTINATION_LOCATION_ID = SELECTED_DESTINATION_LOCATION_ID;
    }
    public String getSELECTED_DESTINATION_LOCATIO_ID(){
        return SELECTED_DESTINATION_LOCATION_ID;
    }

}

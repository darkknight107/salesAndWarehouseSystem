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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.Duration;
import manageProduct.AppScreen;
import transferProduct.AcceptTransferItemController;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
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
    @FXML
    DatePicker fromDatePicker;
    @FXML
    DatePicker toDatePicker;

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
        LocalDate fromDate = fromDatePicker.getValue();
        LocalDate toDate = toDatePicker.getValue();

        if(transferID.equals("")){
            if(fromDate == null & toDate == null){
                showAllTransfer();
            }
            else if(fromDate != null & toDate == null) {
                toDate = LocalDate.now();
                System.out.println(toDate);
                filterDatetime(fromDate.toString(), toDate.toString());
            }
            else if(fromDate == null & toDate != null){
                fromDate = LocalDate.of(2000,1,1);
                filterDatetime(fromDate.toString(),toDate.toString());
            }
            else if(fromDate != null & toDate != null){
                filterDatetime(fromDate.toString(),toDate.toString());
            }
        }else if (!transferID.equals("")){
            if (fromDate == null & toDate == null){
                searchTransfer("http://localhost:8080/rest/transferproduct/searchTransfer/", "transferID", transferID);
            }
            else if(fromDate != null & toDate == null){
                toDate = LocalDate.now();
                filterDatetimeOnSearchingTransfer(fromDate.toString(),toDate.toString(),transferID);
            }
            else if(fromDate == null & toDate != null){
                fromDate = LocalDate.of(2000,1,1);
                filterDatetime(fromDate.toString(),toDate.toString());
            }
            else if(fromDate != null & toDate != null){
                filterDatetimeOnSearchingTransfer(fromDate.toString(),toDate.toString(),transferID);
            }
        }
    }

    //Cannot select the from date behind the to date
    @FXML
    public void handleFromDatePicker(){
        setEndDateBounds(toDatePicker, fromDatePicker.getValue());
    }

    //Cannot select the to date behind the from date
    @FXML
    public void handleToDatePicker(){
        setBeginDateBounds(fromDatePicker, toDatePicker.getValue());
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

    //Filter Datetime when user dont search transferID
    public void filterDatetime(String fromDate, String toDate){
        data.clear();
        webResourceGet = client.resource("http://localhost:8080/rest/transferproduct/searchtransferbydatetimerange/").queryParam("fromdate", fromDate).queryParam("todate",toDate);
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

    //Filter Datetime when user searching transferID
    public void filterDatetimeOnSearchingTransfer(String fromDate, String toDate,String transferID){
        data.clear();
        webResourceGet = client.resource("http://localhost:8080/rest/transferproduct/searchselectedtransferbydatetimerange/").queryParam("fromdate", fromDate).queryParam("todate",toDate).queryParam("transferid",transferID);
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

    public static void setBeginDateBounds(DatePicker begin_date, LocalDate end_date ){
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {

                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        boolean cond = (!item.isBefore(end_date));
                        if (cond){
                            setDisable(true);
                            setStyle("-fx-background-color: #d3d3d3;");
                        }else{
                            setDisable(false);
                            setStyle("-fx-background-color: #CCFFFF;");
                            setStyle("-fx-font-fill: black;");
                        }
                    }
                };
            }
        };
        begin_date.setDayCellFactory(dayCellFactory);
    }

    public static void setEndDateBounds(DatePicker end_date, LocalDate begin_date ){
         final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {

            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {

                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        boolean cond = (!item.isAfter(begin_date));
                        if (cond){
                            setDisable(true);
                            setStyle("-fx-background-color: #d3d3d3;");
                        }else{
                            setDisable(false);
                            setStyle("-fx-background-color: #CCFFFF;");
                            setStyle("-fx-font-fill: black;");
                        }
                    }
                };
            }
        };
        end_date.setDayCellFactory(dayCellFactory);
    }
}

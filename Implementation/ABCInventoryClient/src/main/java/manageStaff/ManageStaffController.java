package manageStaff;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Staff;
import entityClass.Transfer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import manageProduct.AppScreen;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;

public class ManageStaffController {
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField searchField;
    @FXML
    Button addNewStaffButton;
    @FXML
    Button searchStaffButton;
    @FXML
    TableView<Staff> staffTable;
    @FXML
    TableColumn actionColumn;

    private ObservableList<Staff> data= FXCollections.observableArrayList();
    Client client;
    WebResource webResourceGet;
    ClientResponse response;
    GenericType<List<Staff>> listc = new GenericType<List<Staff>>() {};
    private List<Staff> staffList;
    AppScreen screen= new AppScreen();

    public void displayActionButtons() {
        // Create the "Detail" button for each row and define the action for it
        actionColumn.setCellFactory(col -> {
            Button editButton = new Button("Edit");
            Button deleteButton = new Button("Delete");
            HBox hBox = new HBox(editButton, deleteButton);
            TableCell<Staff, Staff> cell = new TableCell<Staff, Staff>() {
                @Override
                //the buttons are only displayed for the row have data
                public void updateItem(Staff staff, boolean empty) {
                    super.updateItem(staff, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hBox);
                    }
                }

            };
            deleteButton.setOnAction(e ->{
                staffTable.getSelectionModel().select(cell.getIndex());
                //getting the selected product code
                String toDeleteStaff= staffTable.getSelectionModel().getSelectedItem().getUserName();
                System.out.println(toDeleteStaff);
                Alert alert= new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete staff " + toDeleteStaff + "?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                alert.setTitle("Warning!");
                if (alert.getResult()== ButtonType.YES){
                    //creating a new client to delete the selected product
                    ClientConfig clientConfig= new DefaultClientConfig();
                    clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
                    Client client= Client.create(clientConfig);
                    WebResource deleteResource= client.resource("http://localhost:8080/rest/managestaff/deletestaff/"+ toDeleteStaff);
                    //converting the response to string
                    ClientResponse response= deleteResource.delete(ClientResponse.class);
                    response.bufferEntity();
                    if (response.getStatus() == 200){
                        screen.alertMessages("Staff Deleted!", "The Staff " + toDeleteStaff + " has been deleted.");
                        showAllStaff();
                    }
                    else{
                        screen.alertMessages("Error!", "An error occurred. Could not delete the product!");
                    }
                }
            });
            return cell;
        });
    }

    public void handleAddNewStaffButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddStaff.fxml"));
        AnchorPane pane= loader.load();
        anchorPane.getChildren().setAll(pane);
    }

    private  void searchStaff(String URL ,String searchField, String code) {
        data.clear();
        webResourceGet = client.resource(URL).queryParam(searchField, code);
        response = webResourceGet.get(ClientResponse.class);
        staffList = response.getEntity(listc);
        if (response.getStatus() != 200) {
            throw new WebApplicationException();
        }
        if (staffList.isEmpty()) {
            screen.alertMessages("Staff does not exists!", "Error! Staff does not exist.");
        } else {
            for (Staff s : staffList) {
                data.add(s);
            }
        }
    }
    public void showAllStaff(){
        data= staffTable.getItems();
        data.clear();
        //creating a new client to send request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);
        String url="http://localhost:8080/rest/managestaff/viewallstaff";
        searchStaff(url, "", "");
        displayActionButtons();
    }
    public void handleSearchStaffButton(){
        data= staffTable.getItems();
        data.clear();
        //creating a new client to send request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client= Client.create(clientConfig);
        String url= "http://localhost:8080/rest/managestaff/searchstaff";
        searchStaff(url, "name", searchField.getText());
    }
}

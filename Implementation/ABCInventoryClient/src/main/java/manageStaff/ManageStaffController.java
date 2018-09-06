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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import manageProduct.AppScreen;
import manageProduct.UpdateProductController;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.List;

public class ManageStaffController {
    AnchorPane pane;
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
            //Tooltip and image icon for edit button
            Image editImage = new Image("image/UpdateButtonIcon.png");
            ImageView editImageView = new ImageView(editImage);
            editImageView.setFitHeight(15);
            editImageView.setFitWidth(15);

            Button updateButton = new Button("", editImageView);
            Tooltip editTooltip = new Tooltip("Update details for this staff");
            editTooltip.setShowDelay(Duration.millis(10));
            updateButton.setTooltip(editTooltip);

            //Tooltip and image icon for delete button
            Image deleteImage = new Image("image/DeleteButtonIcon.png");
            ImageView deleteImageView = new ImageView(deleteImage);
            deleteImageView.setFitHeight(15);
            deleteImageView.setFitWidth(15);

            Button deleteButton = new Button("", deleteImageView);
            Tooltip deleteTooltip = new Tooltip("Delete this staff detail");
            deleteTooltip.setShowDelay(Duration.millis(10));
            deleteButton.setTooltip(deleteTooltip);

            HBox hBox = new HBox(updateButton, deleteButton);
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
                //getting the selected staff username
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
                        screen.alertMessages("Error!", "An error occurred. Could not delete the staff!");
                    }
                }
            });
            updateButton.setOnAction(e ->{
                try {
                staffTable.getSelectionModel().select(cell.getIndex());
                //getting the selected staff details and putting it into a staff object
                Staff staffToBeUpdated= new Staff();
                staffToBeUpdated.setUserName(staffTable.getSelectionModel().getSelectedItem().getUserName());
                staffToBeUpdated.setFirstName(staffTable.getSelectionModel().getSelectedItem().getFirstName());
                staffToBeUpdated.setLastName(staffTable.getSelectionModel().getSelectedItem().getLastName());
                staffToBeUpdated.setLocationID(staffTable.getSelectionModel().getSelectedItem().getLocationID());
                staffToBeUpdated.setContact(staffTable.getSelectionModel().getSelectedItem().getContact());
                staffToBeUpdated.setDateOfBirth(staffTable.getSelectionModel().getSelectedItem().getDateOfBirth());
                staffToBeUpdated.setAddress(staffTable.getSelectionModel().getSelectedItem().getAddress());
                staffToBeUpdated.setEmail(staffTable.getSelectionModel().getSelectedItem().getEmail());

                //passing data from selected staff to be updated to UpdateStaffController and displaying update staff page
                FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/UpdateStaff.fxml"));
                pane = loader.load();
                anchorPane.getChildren().setAll(pane);
                UpdateStaffController updateStaffController= loader.<UpdateStaffController>getController();
                updateStaffController.setStaffData(staffToBeUpdated);
                }
                catch (IOException e1) {
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

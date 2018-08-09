package manageStaff;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import entityClass.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import manageProduct.AppScreen;

import java.io.IOException;
import java.time.LocalDate;

public class UpdateStaffController {
    //initializing variables
    AnchorPane pane;
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField userNameField;
    @FXML
    Hyperlink changePasswordLink;
    @FXML
    TextField firstNameField;
    @FXML
    TextField lastNameField;
    @FXML
    ComboBox locationComboBox;
    @FXML
    TextField contactField;
    @FXML
    DatePicker dateOfBirthPicker;
    @FXML
    TextField addressField;
    @FXML
    TextField emailField;
    ObservableList<String> locationList;

    public void handleUpdateButton() throws IOException {
        Staff updatedStaff= new Staff();
        String locationID = new AddStaffController().convertLocationName((String) locationComboBox.getValue());
        updatedStaff.setLocationID(locationID);
        updatedStaff.setUserName(userNameField.getText());
        updatedStaff.setFirstName(firstNameField.getText());
        updatedStaff.setLastName(lastNameField.getText());
        updatedStaff.setContact(contactField.getText());
        updatedStaff.setDateOfBirth(String.valueOf(dateOfBirthPicker.getValue()));
        updatedStaff.setAddress(addressField.getText());
        updatedStaff.setEmail(emailField.getText());

        //creating a new client to send put request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client= Client.create(clientConfig);
        String updateURL= "http://localhost:8080/rest/managestaff/updatestaff";
        WebResource webResourcePost= client.resource(updateURL);
        //use the object passed as a parameter to send a request
        ClientResponse response= webResourcePost.type("application/json").put(ClientResponse.class, updatedStaff);
        response.bufferEntity();
        //String responseValue= response.getEntity(String.class);
        AppScreen screen= new AppScreen();
        if (response.getStatus()== 200){
            screen.alertMessages("Staff Updated!", "Staff " + userNameField.getText() + " has been updated!");
            FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageStaff.fxml"));
            pane = loader.load();
            ManageStaffController controller= loader.<ManageStaffController>getController();
            controller.showAllStaff();
            anchorPane.getChildren().setAll(pane);
        }
        else{
            screen.alertMessages("Error!", "Product could not be updated!");
        }

    }
    public void setStaffData(Staff staff){
        //setting the items that can be selected in the combo box
        locationList= FXCollections.observableArrayList("Newtown Warehouse", "Epping Store", "Oxford Store");
        locationComboBox.setItems(locationList);
        //putting values of the staff to be updated in the field which  is editable
        //converting locationID to locationName
        String locationID= staff.getLocationID();
        switch (locationID){
            case "WRH1":
                locationComboBox.setValue("Newtown Warehouse");
                break;
            case "STR1":
                locationComboBox.setValue("Epping Store");
                break;
            case "STR2":
                locationComboBox.setValue("Oxford Store");
                break;
        }
        userNameField.setText(staff.getUserName());
        //not allowing user to  change username
        userNameField.setDisable(true);
        firstNameField.setText(staff.getFirstName());
        lastNameField.setText(staff.getLastName());
        contactField.setText(staff.getContact());
        System.out.println(staff.getDateOfBirth());
        System.out.println("a" + staff.getDateOfBirth() + "a");
        if (staff.getDateOfBirth().equals("null")){
            dateOfBirthPicker.setValue(null);
        }
        else{
            dateOfBirthPicker.setValue(LocalDate.parse(staff.getDateOfBirth()));
        }
        addressField.setText(staff.getAddress());
        emailField.setText(staff.getEmail());
    }

    public void handleChangePasswordLink(){


    }
}

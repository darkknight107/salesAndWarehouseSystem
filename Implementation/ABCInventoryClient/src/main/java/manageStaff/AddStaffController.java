package manageStaff;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Staff;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import manageProduct.AddProductController;
import manageProduct.AppScreen;


public class AddStaffController {
    //listing fxml elements
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField firstNameField;
    @FXML
    TextField middleNameField;
    @FXML
    TextField lastNameField;
    @FXML
    TextField emailField;
    @FXML
    TextField phoneField;
    @FXML
    TextField addressField;
    @FXML
    DatePicker dobPicker;
    @FXML
    TextField userNameField;
    @FXML
    PasswordField passwordField;
    @FXML
    ComboBox locationComboBox;

    public void addStaff(){
        String locationName= (String) locationComboBox.getValue();
        String locationID = null;
        switch (locationName){
            case "Newtown Warehouse":
                locationID= "WRH1";
                break;
            case "Epping Store":
                locationID= "STR1";
                break;
            case "Oxford Store":
                locationID= "STR2";
                break;
        }
        Staff staff= new Staff();
        staff.setFirstName(firstNameField.getText());
        staff.setMiddleName(middleNameField.getText());
        staff.setLastName(lastNameField.getText());
        staff.setEmail(emailField.getText());
        staff.setContact(phoneField.getText());
        staff.setAddress(addressField.getText());
        staff.setDateOfBirth(String.valueOf(dobPicker.getValue()));
        staff.setUserName(userNameField.getText());
        staff.setPassword(passwordField.getText());
        staff.setLocationID(locationID);
        AppScreen screen= new AppScreen();

        //creating a new client to send post request
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client= Client.create(clientConfig);
        String postURL= "http://localhost:8080/rest/managestaff/addstaff";
        WebResource webResourcePost= client.resource(postURL);
        //use the object passed as a parameter to send a request
        ClientResponse response= webResourcePost.type("application/json").post(ClientResponse.class, staff);
        if (response.getStatus() == 200){
            screen.alertMessages("Staff Added!", "Staff " + staff.getUserName() + " added!");

        }
        else{
            screen.alertMessages("Error!", "An Error occured! Please try again.");
        }
    }
    public void displayLocation(){
        ObservableList<String> locationList= FXCollections.observableArrayList("Newtown Warehouse", "Epping Store", "Oxford Store");
        locationComboBox.setItems(locationList);
    }
}

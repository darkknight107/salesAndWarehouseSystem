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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import manageProduct.AddProductController;
import manageProduct.AppScreen;

import java.io.IOException;


public class AddStaffController {
    //initializing variables
    AnchorPane pane;
    //listing fxml elements
    @FXML
    AnchorPane anchorPane;
    @FXML
    TextField firstNameField;
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
    PasswordField passwordField1;
    @FXML
    ComboBox locationComboBox;
    Staff staff;
    AppScreen screen;

    public AddStaffController(){
        staff= new Staff();
        screen= new AppScreen();
    }

    public void addStaff() throws IOException {
        if (firstNameField.getText() != null && lastNameField.getText() != null && emailField !=null && phoneField.getText() != null && userNameField != null && passwordField != null && passwordField1 != null && locationComboBox.getValue() != null){
            staff.setFirstName(firstNameField.getText());
            staff.setLastName(lastNameField.getText());
            staff.setEmail(emailField.getText());
            staff.setContact(phoneField.getText());
            staff.setAddress(addressField.getText());
            String locationID= convertLocationName((String) locationComboBox.getValue());
            staff.setDateOfBirth(String.valueOf(dobPicker.getValue()));
            System.out.println(String.valueOf(dobPicker.getValue()));
            staff.setUserName(userNameField.getText());
            staff.setPassword(passwordField.getText());
            staff.setLocationID(locationID);
            if(passwordField.getText().equals(passwordField1.getText())){
                passwordField.setStyle(null);
                passwordField1.setStyle(null);
                staff.setPassword(passwordField.getText());
            }
            else{
                passwordField.setStyle("-fx-border-color:red; -fx-border-width: 2px");
                passwordField1.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            }


            //creating a new client to send post request
            ClientConfig clientConfig= new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client= Client.create(clientConfig);
            String postURL= "http://localhost:8080/rest/managestaff/addstaff";
            WebResource webResourcePost= client.resource(postURL);
            //use the object passed as a parameter to send a request
            ClientResponse response= webResourcePost.type("application/json").post(ClientResponse.class, staff);
            response.bufferEntity();
            String responseValue= response.getEntity(String.class);
            if (responseValue.equals("true")){
                screen.alertMessages("Staff Added!", "Staff " + staff.getUserName() + " added!");
                FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageStaff.fxml"));
                pane = loader.load();
                ManageStaffController controller= loader.<ManageStaffController>getController();
                controller.showAllStaff();
                anchorPane.getChildren().setAll(pane);
            }
            else if(responseValue.equals("exists")){
                screen.alertMessages("Username already exists!", "The username entered already exists. Please enter a unique username.");
                userNameField.setText("");
            }
            else{
                screen.alertMessages("Error!", "An Error occured! Please try again.");
            }
        }
        else {
            screen.alertMessages("Incomplete Field!", "Please fill in all the required fields.");

            if (firstNameField.getText() == null) {
                firstNameField.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            }
            if (lastNameField.getText() == null) {
                lastNameField.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            }
            if (emailField.getText() == null) {
                emailField.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            }
            if (phoneField.getText() == null) {
                phoneField.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            }
            if (userNameField.getText() == null) {
                userNameField.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            }
            if (passwordField.getText() == null) {
                passwordField.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            }
            if (passwordField1.getText() == null) {
                passwordField1.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            }
            if (locationComboBox.getValue() == null) {
                locationComboBox.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            }
        }

    }
    public void displayLocation(){
        ObservableList<String> locationList= FXCollections.observableArrayList("Newtown Warehouse", "Epping Store", "Oxford Store");
        locationComboBox.setItems(locationList);
    }
    public String convertLocationName(String locationName){
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
        return locationID;
    }
    /*public void verifyPassword(){
        if(passwordField.getText().equals(passwordField1.getText())){
            passwordField.setStyle(null);
            passwordField1.setStyle(null);
            staff.setPassword(passwordField.getText());
        }
        else{
            passwordField.setStyle("-fx-border-color:red; -fx-border-width: 2px");
            passwordField1.setStyle("-fx-border-color:red; -fx-border-width: 2px");
        }

    }*/
}

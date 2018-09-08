package manageStaff;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Staff;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import manageProduct.AddProductController;
import manageProduct.AppScreen;

import java.io.IOException;

import static java.lang.System.exit;


public class AddStaffController {
    //initializing variables
    AnchorPane pane;
    //listing fxml elements
    @FXML
    AnchorPane anchorPane;
    @FXML
    Button addStaffButton;
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
        resetTextStyle();
        if (!(firstNameField.getText().isEmpty()) && !(lastNameField.getText().isEmpty()) && !(emailField.getText().isEmpty()) &&
                !(phoneField.getText().isEmpty()) && !(userNameField.getText().isEmpty()) && !(passwordField.getText().isEmpty())
                && !(passwordField1.getText().isEmpty()) && locationComboBox.getValue() != null){

            staff.setFirstName(firstNameField.getText());
            staff.setLastName(lastNameField.getText());
            staff.setEmail(emailField.getText());
            staff.setContact(phoneField.getText());
            staff.setAddress(addressField.getText());
            String locationID= convertLocationName((String) locationComboBox.getValue());
            staff.setDateOfBirth(String.valueOf(dobPicker.getValue()));
            staff.setUserName(userNameField.getText());
            if (passwordField.getText().equals(passwordField1.getText())){
                staff.setPassword(passwordField.getText());
                staff.setLocationID(locationID);
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
            else{
                screen.alertMessages("Error!", "The passwords you entered do not match. Please try again.");
                passwordField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
                passwordField1.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
                passwordField.setText("");
                passwordField1.setText("");
            }

        }
        else {
            screen.alertMessages("Incomplete Field!", "Please fill in all the required fields.");

            if (firstNameField.getText().isEmpty()) {
                firstNameField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (lastNameField.getText().isEmpty()) {
                lastNameField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (emailField.getText().isEmpty()) {
                emailField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (phoneField.getText().isEmpty()) {
                phoneField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (userNameField.getText().isEmpty()) {
                userNameField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (passwordField.getText().isEmpty()) {
                passwordField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (passwordField1.getText().isEmpty()) {
                passwordField1.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (locationComboBox.getValue() == null) {
                locationComboBox.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
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
                locationID= "STR2";
                break;
            case "Oxford Store":
                locationID= "STR1";
                break;
        }
        return locationID;
    }

    public void handleResetButton(){
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        dobPicker.setValue(null);
        addressField.setText("");
        phoneField.setText("");
        userNameField.setText("");
        passwordField.setText("");
        passwordField1.setText("");
        locationComboBox.setValue(null);

    }

    public void resetTextStyle(){
        firstNameField.setStyle(null);
        lastNameField.setStyle(null);
        emailField.setStyle(null);
        phoneField.setStyle(null);
        userNameField.setStyle(null);
        passwordField.setStyle(null);
        passwordField1.setStyle(null);
        locationComboBox.setStyle(null);
    }

    public void handleBackButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageStaff.fxml"));
        AnchorPane pane = loader.load();
        ManageStaffController controller= loader.getController();
        controller.showAllStaff();
        anchorPane.getChildren().setAll(pane);
    }

    public void handleMainMenuButton() throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
        AnchorPane pane = loader.load();
        anchorPane.getChildren().setAll(pane);
    }

}

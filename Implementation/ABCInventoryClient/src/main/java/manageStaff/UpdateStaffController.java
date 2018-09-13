package manageStaff;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Product;
import entityClass.Staff;
import homePage.HomePageController;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import manageProduct.AppScreen;


import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static javafx.scene.control.ButtonBar.ButtonData.OK_DONE;

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
    Staff staff;
    ObservableList<String> locationList;
    AppScreen screen;
    public UpdateStaffController(){
        screen= new AppScreen();
        staff= new Staff();
    }

    public void handleUpdateButton() throws IOException {
        resetTextStyle();
        if (!(firstNameField.getText().isEmpty()) && !(lastNameField.getText().isEmpty()) && !(emailField.getText().isEmpty()) &&
                !(contactField.getText().isEmpty()) && !(userNameField.getText().isEmpty()) && locationComboBox.getValue() != null){
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
        else{
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
            if (contactField.getText().isEmpty()) {
                contactField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (userNameField.getText().isEmpty()) {
                userNameField.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
            if (locationComboBox.getValue() == null) {
                locationComboBox.setStyle("-fx-border-color:red; -fx-border-width: 0.5px");
            }
        }
    }
    public void resetTextStyle(){
        firstNameField.setStyle(null);
        lastNameField.setStyle(null);
        emailField.setStyle(null);
        contactField.setStyle(null);
        userNameField.setStyle(null);
        locationComboBox.setStyle(null);
    }
    public void setStaffData(Staff staff){
        //setting the items that can be selected in the combo box
        this.staff= staff;
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
                locationComboBox.setValue("Oxford Store");
                break;
            case "STR2":
                locationComboBox.setValue("Epping Store");
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
        //creating a dialog
        final Stage dialog= new Stage();
        dialog.setTitle("Change Password");

        //different elements in the dialog box
        Label displayLabel= new Label("Enter new password:");
        displayLabel.setFont(Font.font(null, FontWeight.BOLD, 14));
        Button updateButton= new Button("Update");
        Button cancelButton= new Button("Cancel");
        PasswordField newPassword= new PasswordField();
        newPassword.setPromptText("Enter a new password");
        PasswordField confirmPassword= new PasswordField();
        confirmPassword.setPromptText("Confirm password");

        //adding fields to a gridpane
        GridPane grid= new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.add(displayLabel, 0, 0);
        grid.add(new Label("New Password:"), 0, 1);
        grid.add(newPassword, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPassword, 1, 2);
        grid.add(updateButton, 0, 3);
        grid.add(cancelButton, 1, 3);

        //using boolean bind to disable update button when password fields are empty
        BooleanBinding bb= new BooleanBinding() {
            {
                super.bind(newPassword.textProperty(), confirmPassword.textProperty());
            }
            @Override
            protected boolean computeValue() {
                return (newPassword.getText().isEmpty() || confirmPassword.getText().isEmpty());
            }
        };
        updateButton.disableProperty().bind(bb);

        //do this when updateButton is pressed
        updateButton.setOnAction(e-> {
            //validate password
            if (newPassword.getText().equals(confirmPassword.getText())){

                // Create Jersey client
                ClientConfig clientConfig = new DefaultClientConfig();
                clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
                Client client = Client.create(clientConfig);
                // GET request to update password resource with a query parameter
                String updatePasswordURL= "http://localhost:8080/rest/managestaff/updatepassword";
                WebResource webResourceGet = client.resource(updatePasswordURL).queryParam("username", userNameField.getText()).queryParam("password", newPassword.getText());
                ClientResponse response = webResourceGet.put(ClientResponse.class);
                Boolean responseValue= response.getEntity(Boolean.class);
                if (responseValue == true) {
                    screen.alertMessages("Password updated", "User "+ userNameField.getText()+ "'s password has been updated!");
                    try {
                        dialog.close();
                        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageStaff.fxml"));
                        pane = loader.load();
                        dialog.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                else{
                    screen.alertMessages("Error", "Password could not be updated!");
                    dialog.close();
                }
            }
            else{
                screen.alertMessages("Re-enter password", "Passwords do not match. Please try again.");
                newPassword.setText("");
                confirmPassword.setText("");
            }

            });
        cancelButton.setOnAction(e ->{
            dialog.close();
        });
        //display the dialog
        HBox dialogHBox= new HBox(20);
        dialogHBox.setAlignment(Pos.CENTER);
        dialogHBox.getChildren().add(grid);
        Scene dialogScene= new Scene(dialogHBox, 500, 150);
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) anchorPane.getScene().getWindow());
        dialog.show();

    }
    public void handleCancelButton(){
        setStaffData(staff);
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
        HomePageController controller= loader.getController();
        controller.checkStaff();
        anchorPane.getChildren().setAll(pane);
    }




}

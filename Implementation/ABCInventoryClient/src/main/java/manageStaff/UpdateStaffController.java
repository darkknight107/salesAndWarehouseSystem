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
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import manageProduct.AppScreen;
import sun.security.util.Password;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.time.LocalDate;

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
    ObservableList<String> locationList;
    AppScreen screen;
    public UpdateStaffController(){
        screen= new AppScreen();
    }

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

        Dialog<Pair<Password, Password>> dialog= new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Please enter a new password: ");

        Button updateButton= new Button(("Update"));
        Button cancelButton= new Button("Cancel");

        PasswordField newPassword= new PasswordField();
        newPassword.setPromptText("Enter a new password");
        PasswordField confirmPassword= new PasswordField();
        confirmPassword.setPromptText("Confirm password");

        GridPane grid= new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("New Password:"), 0, 0);
        grid.add(newPassword, 1, 0);
        grid.add(new Label("Confirm Password:"), 0, 1);
        grid.add(confirmPassword, 1, 1);
        grid.add(updateButton, 0, 2);
        grid.add(cancelButton, 1, 2);

        if (newPassword.getText()== null && confirmPassword.getText() == null){
            updateButton.setDisable(true);
        }
        dialog.showAndWait();

        updateButton.setOnAction(e->{
            // Create Jersey client
            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client = Client.create(clientConfig);

            // GET request to findBook resource with a query parameter
            String updatePasswordURL= "http://localhost:8080/rest/managestaff/updatepassword";
            WebResource webResourceGet = client.resource(updatePasswordURL).queryParam(userNameField.getText(), newPassword.getText());
            ClientResponse response = webResourceGet.get(ClientResponse.class);

            if (response.getStatus()== 200) {
                screen.alertMessages("Password upadted", "User "+ userNameField.getText()+ "'s password has been updated!");
                try {
                    FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageStaff.fxml"));
                    pane = loader.load();
                    ManageStaffController controller= loader.<ManageStaffController>getController();
                    controller.showAllStaff();
                    anchorPane.getChildren().setAll(pane);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            else{
                screen.alertMessages("Error", "Password could not be updated!");
            }
        });







    }
}

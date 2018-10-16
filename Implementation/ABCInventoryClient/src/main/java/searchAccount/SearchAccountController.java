package searchAccount;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import homePage.HomePageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import manageProduct.AppScreen;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.*;
import java.security.MessageDigest;

public class SearchAccountController {

    @FXML
    TextField usernameTextField;

    @FXML
    AnchorPane anchorPane;

    @FXML
    PasswordField passwordTextField;

    @FXML
    Button btnLogin;

    @FXML
    Button  btnClear;

    AppScreen appScreen = new AppScreen ();

    //Variables to create the client
    Client client;
    WebResource webResource;
    ClientResponse response;

    String getAccountURL;
    private static String CURRENT_LOCATION_ID;
    int count = 0;
    // Initialize method for handle the
    public void initialize(URL url,ResourceBundle rb) throws Exception {handleSearchAccountCodeAction (); }

    @FXML
    public void handleSearchAccountCodeAction() throws Exception {
        String actualPassword = null;
        String username = usernameTextField.getText ();
        String password = passwordTextField.getText ();
        System.out.println ("Got Data");
        getAccountURL = "http://abcinventoryserver.ap-southeast-2.elasticbeanstalk.com/rest/searchAccount/account/";
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);
        StringBuffer sb = new StringBuffer ();
        try{
            MessageDigest md = MessageDigest.getInstance ("SHA-512");
            md.update (password.getBytes());
            byte byteData[] = md.digest ();
            for (int i = 0; i < byteData.length; i++){
                sb.append(Integer.toString ((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
        }
        catch (Exception ex){
            throw new Exception (ex);
        }
        String passToHex =  sb.toString ();
        searchAccountAction (getAccountURL, "username", username, "password", passToHex);
    }

    private void searchAccountAction(String URL, String usernameField,String username, String passwordField, String password) throws IOException {
        System.out.println ("passed in action");
        //creating a new client to send get request
        webResource = client.resource(URL).queryParam(usernameField, username).queryParam (passwordField,password);
        response = webResource.get(ClientResponse.class);
        String responseValue= response.getEntity (String.class);
        System.out.println (responseValue);
        if(responseValue.equals ("WRH1")){
            setCurrentLocationId(responseValue);
            FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
            AnchorPane pane= loader.load();
            anchorPane.getChildren().setAll(pane);
            HomePageController controller= loader.<HomePageController>getController();
            controller.setUserType("WRH1");
            controller.checkStaff();
            System.out.println ("Location of the employee" + responseValue);

        }
        else if(responseValue.equals ("STR1") || responseValue.equals ("STR2")){
            setCurrentLocationId(responseValue);
            FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
            AnchorPane pane= loader.load();
            anchorPane.getChildren().setAll(pane);
            HomePageController controller= loader.<HomePageController>getController();
            controller.setUserType("STR");

            controller.checkStaff();
        }
        else if (responseValue.equals("false")){

            appScreen.alertMessages ("Unsuccessful","Username or Password does not match.");
        }
        else{
            appScreen.alertMessages ("Error!", "An error occurred. Please make sure you are connected to the internet.");
        }

    }


    @FXML
    private void clear(ActionEvent event){
        usernameTextField.setText ("");
        passwordTextField.setText ("");
    }

    public String getCurrentLocationId() {
        return CURRENT_LOCATION_ID;
    }

    public void setCurrentLocationId(String currentLocationId) {
        CURRENT_LOCATION_ID = currentLocationId;
    }
}

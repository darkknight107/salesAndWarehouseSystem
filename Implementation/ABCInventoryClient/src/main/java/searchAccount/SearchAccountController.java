package searchAccount;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import homePage.HomePageController;
import javafx.animation.ParallelTransition;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
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
    int count = 0;
    // Initialize method for handle the
    public void initialize(URL url,ResourceBundle rb) throws Exception {handleSearchAccountCodeAction (); }

    @FXML
    public void handleSearchAccountCodeAction() throws Exception {
        String actualPassword = null;
        String username = usernameTextField.getText ();
        String password = passwordTextField.getText ();
        System.out.println ("Got Data");
        getAccountURL = "http://localhost:8080/rest/searchAccount/account/";
        ClientConfig clientConfig= new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        client = Client.create(clientConfig);
        if(!username.matches ("\\b[a-zA-Z][a-zA-Z\\-._]{3,}\\b")){
            System.out.println ("Error in user");
            appScreen.alertMessages ("Error","Wrong Input Type");
        }
        else if(password.length ()<4){
            appScreen.alertMessages ("ERROR","Password did not match the Criteria");

        }
        else {
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


    }


    private void searchAccountAction(String URL, String usernameField,String username, String passwordField, String password) throws IOException {
        System.out.println ("passed in action");
        //creating a new client to send get request
        webResource = client.resource(URL).queryParam(usernameField, username).queryParam (passwordField,password);
        response = webResource.get(ClientResponse.class);
        String responseValue= response.getEntity (String.class);
        System.out.println (responseValue);
        if(responseValue.equals ("WRH1")){
            FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
            AnchorPane pane= loader.load();
            anchorPane.getChildren().setAll(pane);
            HomePageController controller= loader.<HomePageController>getController();
            controller.setUserType("WRH1");
            System.out.println ("Location of the employee" + responseValue);

        }
        else if(responseValue.equals ("STR1") || responseValue.equals ("STR2")){
            FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomePageFXML.fxml"));
            AnchorPane pane= loader.load();
            anchorPane.getChildren().setAll(pane);
            HomePageController controller= loader.<HomePageController>getController();
            controller.storeStaffOptionsDisable ();
            controller.setUserType("STR");

        }
        else if (!responseValue.isEmpty ()){

            appScreen.alertMessages ("Unsuccessful","Username or Password does not match.");
        }
        else{
            appScreen.alertMessages ("Error!", "An error occurred. Please try again!");
        }

    }


    @FXML
    private void clear(ActionEvent event){

        usernameTextField.setText ("");
        passwordTextField.setText ("");
    }



}

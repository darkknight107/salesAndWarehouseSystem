package transferProduct;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import entityClass.Transfer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import manageProduct.AppScreen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransferController {

    @FXML
    private TextArea txtDescription;
    @FXML
    private ComboBox cbSendLocation;
    @FXML
    private ComboBox cbDestinationLocation;


    //Get current date and formatting the date



    @FXML
    public void handleNextButton() {
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        //creating variables to get product details entered by user in text fields
        String sendLocation= cbSendLocation.getValue().toString();
        String destinationLocation= cbDestinationLocation.getValue().toString();
        String date = dtf.format(cal.getTime());
        String status= "Send";
        String description= txtDescription.getText();

        //set textfield values to Product Entity
        Transfer t = new Transfer(sendLocation,destinationLocation,date,status,description);

        List<Transfer> transferList = new ArrayList<Transfer>();
        transferList.add(t);

        //call the clientRequest method to send a request to the server
        String response= clientRequest(transferList,"addtransfer");
        AppScreen screen= new AppScreen();
        if (response.equals("true")){
            screen.alertMessages("Transfer Added", "Transfer has been added.");
            //load text fields and labels for adding product item
        }
    }

    private String clientRequest(Object entity, String path) {
        //creating a new client to send post request
        ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        Client client = Client.create(clientConfig);
        String postURL = "http://localhost:8080/rest/transferproduct/" + path;
        WebResource webResourcePost = client.resource(postURL);
        //use the object passed as a parameter to send a request
        ClientResponse response = webResourcePost.type("application/json").post(ClientResponse.class, entity);
        response.bufferEntity();
        String responseValue = response.getEntity(String.class);
        return responseValue;
    }
}

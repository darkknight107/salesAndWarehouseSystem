package homePage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import searchProduct.SearchProductController;
import transferProduct.TransferItemController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController {
    //initializing variables
    @FXML
    private AnchorPane anchorPane;

    public void initialize(URL url, ResourceBundle rb) throws IOException {

    }
    @FXML
    public void handleSearchProduct() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SearchProductFXML.fxml"));
        BorderPane pane = loader.load();

        SearchProductController myController = loader.getController();

        //Set Data to FXML through controller
        myController.showAllProducts();
        anchorPane.getChildren().setAll(pane);
    }
    @FXML
    public void handleManageProduct() throws IOException{
        //creating anchor pane for ManageProduct (add product) console
        AnchorPane pane= FXMLLoader.load(getClass().getClassLoader().getResource("fxml/ManageProduct.fxml"));
        anchorPane.getChildren().setAll(pane);
    }

    @FXML
    public void handleSendProduct() throws IOException{
        //load text fields and labels for adding product item
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/TransferItemFXML.fxml"));
        AnchorPane pane = loader.load();

        TransferItemController myController = loader.getController();

        //Set Data to FXML through controller
        myController.showAllStoredProducts();
        anchorPane.getChildren().setAll(pane);
    }


}

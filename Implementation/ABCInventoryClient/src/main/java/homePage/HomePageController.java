package homePage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import manageStaff.ManageStaffController;
import reportTransfer.ReportTransferController;
import searchProduct.SearchProductController;
import transferProduct.AcceptProductController;
import transferProduct.SendProductController;

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
//        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SearchProductItemDetailsFXML.fxml"));
//        BorderPane pane = loader.load();
//
//        SearchProductItemDetailsController myController = loader.getController();
//
//        //Set Data to FXML through controller
//        myController.showAllProducts();
//        anchorPane.getChildren().setAll(pane);

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SearchProductFXML.fxml"));
        AnchorPane pane = loader.load();

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
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SendProductFXML.fxml"));
        AnchorPane pane = loader.load();

        SendProductController myController = loader.getController();

        //Set Data to FXML through controller
        myController.showAllStoredProducts();
        anchorPane.getChildren().setAll(pane);
    }

    @FXML
    public void handleAcceptProduct() throws IOException {
        //load text fields and labels for adding product item
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AcceptTransferFXML.fxml"));
        AnchorPane pane = loader.load();

        AcceptProductController myController = loader.getController();

        //Set Data to FXML through controller
        myController.showAllTransfer();
        anchorPane.getChildren().setAll(pane);
    }

    @FXML
    public void handleManageStaff() throws IOException {

        FXMLLoader loader= new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageStaff.fxml"));
        AnchorPane pane= loader.load();
        anchorPane.getChildren().setAll(pane);
        ManageStaffController controller= loader.<ManageStaffController>getController();
        controller.showAllStaff();
    }

    @FXML
    public void handleGenerateReport() throws IOException {
        //load text fields and labels for adding product item
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ReportTransferFXML.fxml"));
        AnchorPane pane = loader.load();

        ReportTransferController myController = loader.getController();

        //Set Data to FXML through controller
        myController.showAllTransfer();
        anchorPane.getChildren().setAll(pane);
    }
}
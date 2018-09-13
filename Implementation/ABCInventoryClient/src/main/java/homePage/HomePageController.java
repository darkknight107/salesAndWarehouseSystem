package homePage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import manageProduct.ManageProductController;
import manageStaff.ManageStaffController;
import reportTransfer.ReportTransferController;
import searchProduct.SearchProductController;
import transferProduct.AcceptProductController;
import transferProduct.SendProductController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HomePageController {
    //initializing variables
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label loggedInAs;
    @FXML
    Button manageProduct;

    @FXML
    Button manageStaff;
    static Boolean loggedInAsWarehouseStaff= true;


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
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ManageProduct.fxml"));
        AnchorPane pane = loader.load();

        ManageProductController myController = loader.getController();

        //Set Data to FXML through controller
        myController.showAllProducts();
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

    public void handleLogoutButton() throws IOException {
        Alert alert= new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        alert.setTitle("Confirmation");
        if (alert.getResult()== ButtonType.YES) {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SearchAccount.fxml"));
            AnchorPane aPane = loader.load();
            anchorPane.getChildren().setAll(aPane);
        }
    }

    public void setUserType(String locationID){
        loggedInAs.setStyle("-fx-color: white;");
        if(locationID.equals("WRH1")){
            setLoggedInAsWarehouseStaff(true);
            loggedInAs.setText("Warehouse Staff");
        }
        else{
            setLoggedInAsWarehouseStaff(false);
            loggedInAs.setText("Store Staff");
        }

    }
    public void checkStaff(){
        if (getLoggedInAsWarehouseStaff()== false){
            manageProduct.setDisable(true);
            manageStaff.setDisable(true);
        }
    }

    public static Boolean getLoggedInAsWarehouseStaff() {
        return loggedInAsWarehouseStaff;
    }

    public static void setLoggedInAsWarehouseStaff(Boolean loggedInAsWarehouseStaff) {
        HomePageController.loggedInAsWarehouseStaff = loggedInAsWarehouseStaff;
    }
}

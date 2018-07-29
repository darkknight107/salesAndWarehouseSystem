package manageProduct;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManageProductController {
        //initializing variables
        @FXML
        private AnchorPane anchorPane;

        public void initialize(URL url, ResourceBundle rb) throws IOException {

        }
        @FXML
        public void handleAddProduct() throws IOException {
            //Creating border pane for SearchProductFXML root pane
            AnchorPane pane= FXMLLoader.load(getClass().getClassLoader().getResource("fxml/AddProduct.fxml"));

            anchorPane.getChildren().setAll(pane);
        }
}
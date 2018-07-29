package homePage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SearchProductMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/HomePageFXML.fxml"));
        primaryStage.setTitle("ABC Inventory Management Tool");
        primaryStage.setScene(new Scene(root, 1400, 750));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

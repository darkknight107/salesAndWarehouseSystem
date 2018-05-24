package searchProduct;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchProductController {
    @FXML
    private TextField txtSearch;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<SearchProduct> tblSearchProduct;
    @FXML
    private  TextField txtSearchProductItemCode;

    public void initialize(URL url, ResourceBundle rb){
        handleSearchProductCodeAction();
    }

    @FXML
    private void handleSearchProductCodeAction(){
        WebTarget clientTarget = null;
        ObservableList<SearchProduct> data = tblSearchProduct.getItems();
        data.clear();
        Client client = ClientBuilder.newClient();
        client.register(SearchProductMessageBodyReader.class);
        if (txtSearch.getText().length() > 0  && txtSearchProductItemCode.getText().length() == 0) {
            clientTarget = client.target("http://abcinventoryserver.ap-southeast-2.elasticbeanstalk.com/rest/searchproduct/searchproductcode/{beginBy}");
            clientTarget = clientTarget.resolveTemplate("beginBy", txtSearch.getText());
            tblSearchProduct.getColumns().remove(1);
            tblSearchProduct.getColumns().remove(2);
        } else if (txtSearch.getText().length() == 0  && txtSearchProductItemCode.getText().length() > 0) {
            clientTarget = client.target("http://abcinventoryserver.ap-southeast-2.elasticbeanstalk.com/rest/searchproduct/searchproductcode/{beginBy}");
            clientTarget = clientTarget.resolveTemplate("beginBy", txtSearchProductItemCode.getText());
        }
        GenericType<List<SearchProduct>> listc = new GenericType<List<SearchProduct>>() {
        };
        List<SearchProduct> searchProductList = clientTarget.request("application/json").get(listc);

        for (SearchProduct s : searchProductList) {
            data.add(s);
            System.out.println(s.toString());
        }
    }
}
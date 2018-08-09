package com.fellowshipofthe.DAO;

import com.fellowshipofthe.DatabaseConnection;
import com.fellowshipofthe.entityClasses.Product;
import com.fellowshipofthe.entityClasses.ProductItem;
import com.fellowshipofthe.entityClasses.SearchProduct;
import com.fellowshipofthe.entityClasses.StoredProduct;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    // initialize the variables
    List<SearchProduct> searchProducts;
    DatabaseConnection dbconnet;
    Connection conn;
    Statement stmt;

    //initializing list through constructor to avoid null pointer exception
    public ProductDAO() {
        searchProducts= new ArrayList<SearchProduct>();
    }
    //     Search Product Item or Product Item Code
    public List<SearchProduct> searchProduct(String code){
        String searchProductSqlQuery = " SELECT p.productCode, \n" +
                "        pi.productItemCode, \n" +
                "        pi.productSize, \n" +
                "        p.productName, \n" +
                "        sum(sp.productQuantity) 'productQuantity',\n" +
                "        p.price,\n" +
                "        sp.locationID,\n" +
                "        l.locationName,\n" +
                "        l.locationAddress, \n" +
                "        l.phone, \n" +
                "        p.description\n" +
                "FROM StoredProduct sp\n" +
                "\tJOIN ProductItem pi ON sp.productItemCode = pi.productItemCode\n" +
                "    JOIN Product p ON pi.productCode = p.productCode\n" +
                "    JOIN Location l ON sp.locationID = l.locationID\n" +
                "WHERE pi.productItemCode = \"" + code + "\" OR p.productCode = \"" + code + "\"" +
                "group by l.locationID;";
        executeSearchProductSQLQueries(searchProductSqlQuery);
        return searchProducts;
    }

    // View the product items of selected product code
    public List<SearchProduct> viewProductItem(String productCode, String locationID) {

        String viewProductItemSqlQuery = " SELECT p.productCode, \n" +
                "        pi.productItemCode, \n" +
                "        pi.productSize, \n" +
                "        p.productName, \n" +
                "        sp.productQuantity,\n" +
                "        p.price,\n" +
                "        sp.locationID,\n" +
                "        l.locationName,\n" +
                "        l.locationAddress, \n" +
                "        l.phone, \n" +
                "        p.description\n" +
                "FROM StoredProduct sp\n" +
                "\tJOIN ProductItem pi ON sp.productItemCode = pi.productItemCode\n" +
                "    JOIN Product p ON pi.productCode = p.productCode\n" +
                "    JOIN Location l ON sp.locationID = l.locationID\n" +
                "WHERE p.productCode = \"" + productCode + "\" AND l.locationID = \"" + locationID + "\";";
        executeSearchProductSQLQueries(viewProductItemSqlQuery);

        return searchProducts;

    }

    //View all products
    public List<SearchProduct> viewAllProducts() {

        String viewAllProductsSqlQuery = " SELECT pi.productCode, \n" +
                "        pi.productItemCode, \n" +
                "        pi.productSize, \n" +
                "        p.productName, \n" +
                "        sp.productQuantity,\n" +
                "        p.price,\n" +
                "        sp.locationID,\n" +
                "        l.locationName,\n" +
                "        l.locationAddress, \n" +
                "        l.phone, \n" +
                "        p.description\n" +
                "FROM StoredProduct sp\n" +
                "\tJOIN ProductItem pi ON sp.productItemCode = pi.productItemCode\n" +
                "    JOIN Product p ON pi.productCode = p.productCode\n" +
                "    JOIN Location l ON sp.locationID = l.locationID;";

        executeSearchProductSQLQueries(viewAllProductsSqlQuery);

        return searchProducts;

    }

    //Search Product Items in a specific Product
    public List<SearchProduct> searchProductItemsInProductCode(String productCode, String productItemCode) {

        String searchProductItemsInProductCodeSqlQuery = " SELECT p.productCode, \n" +
                "        pi.productItemCode, \n" +
                "        pi.productSize, \n" +
                "        p.productName, \n" +
                "        sum(sp.productQuantity) 'productQuantity',\n" +
                "        p.price,\n" +
                "        sp.locationID,\n" +
                "        l.locationName,\n" +
                "        l.locationAddress, \n" +
                "        l.phone, \n" +
                "        p.description\n" +
                "FROM StoredProduct sp\n" +
                "\tJOIN ProductItem pi ON sp.productItemCode = pi.productItemCode\n" +
                "    JOIN Product p ON pi.productCode = p.productCode\n" +
                "    JOIN Location l ON sp.locationID = l.locationID\n" +
                "WHERE p.productCode = \"" + productCode + "\" AND pi.productItemCode = \"" + productItemCode + "\"" +
                "group by l.locationID;";

        executeSearchProductSQLQueries(searchProductItemsInProductCodeSqlQuery);

        return searchProducts;

    }

    //Execute the queries for Search Product methods
    public void executeSearchProductSQLQueries(String sqlQuery){
        try {
            dbconnet = new DatabaseConnection();
            conn = dbconnet.connect();
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            while (resultSet.next()) {
                SearchProduct viewProductItem = new SearchProduct();
                viewProductItem.setProductCode(resultSet.getString(1));
                viewProductItem.setProductItemCode(resultSet.getString(2));
                viewProductItem.setProductSize(resultSet.getString(3));
                viewProductItem.setProductName(resultSet.getString(4));
                viewProductItem.setProductQuantity(resultSet.getInt(5));
                viewProductItem.setPrice(resultSet.getString(6));
                viewProductItem.setLocationID(resultSet.getString(7));
                viewProductItem.setLocationName(resultSet.getString(8));
                viewProductItem.setLocationAddress(resultSet.getString(9));
                viewProductItem.setPhone(resultSet.getString(10));
                viewProductItem.setDescription(resultSet.getString(11));

                searchProducts.add(viewProductItem);
            }
        }catch(SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException  e){
            e.printStackTrace();
        } finally
        {
            // Always make sure result sets and statements are closed,
            // and the connection is returned to the pool
            try
            {
                if (conn != null)
                    conn.close ();
                if (stmt != null)
                    stmt.close();
            }
            catch (SQLException ignore)
            {
            }
        }
    }
    //method to access database and add new product to the database
    public String addProduct(Product newProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        //creating new variables to store new product details
        String code= newProduct.getProductCode();
        String name= newProduct.getProductName();
        String price= newProduct.getPrice();
        String description= newProduct.getDescription();
        //opening a connection with the database and creating a statement
        dbconnet = new DatabaseConnection();
        conn = dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sqlQueryToCheckProduct= "SELECT productCode FROM Product WHERE productCode= \"" + code + "\"";
        ResultSet resultSet= stmt.executeQuery(sqlQueryToCheckProduct);
        if(resultSet.next()){
            conn.close();
            stmt.close();
            System.out.println("Product Already Exists.");
            return "exists";
        }
        else{
            String sql= "INSERT INTO Product (productCode, productName, price, description)" +
                    "VALUES (\"" + code +"\",\""+ name+ "\",\"" + price + "\",\"" + description +"\");";
            System.out.println(code + name + price + description);
            stmt.executeUpdate(sql);
            conn.close();
            stmt.close();
            System.out.println("Product Added!");
            return "true";
        }

    }

    //method to access database and add new product item to the database
    public String addProductItem(ProductItem newProductItem) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        //productItemCode
        String itemCode= newProductItem.getProductItemCode();
        //productCode
        String code= newProductItem.getProductCode();
        String itemSize= newProductItem.getProductSize();
        //opening a connection with the database and creating a statement
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        //check if productItem already exists
        String sqlQueryToCheckProductItem= "SELECT productItemCode FROM ProductItem WHERE productItemCode= \"" + itemCode + "\"";
        ResultSet resultSet= stmt.executeQuery(sqlQueryToCheckProductItem);
        if(resultSet.next()){
            conn.close();
            stmt.close();
            System.out.println("Product Item Already Exists.");
            return "true";
        }
        else{
            String sql= "INSERT INTO ProductItem (productItemCode, productCode, productSize)" +
                    "VALUES (\"" + itemCode +"\",\""+ code+ "\",\"" + itemSize + "\");";
            System.out.println(itemCode + code + itemSize);
            stmt.executeUpdate(sql);
            conn.close();
            stmt.close();
            System.out.println("ProductItem Added!");
            return "true";

        }

    }

    //method to access database and add new stored product to the database
    public Boolean addStoredProduct(StoredProduct newStoredProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String itemCode= newStoredProduct.getProductItemCode();
        String locationID= newStoredProduct.getLocationID();
        String quantity= newStoredProduct.getProductQuantity();
        //opening a connection with the database and creating a statement
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "INSERT INTO StoredProduct(productItemCode, locationID, productQuantity)" +
                "VALUES (\"" + itemCode +"\",\""+ locationID+ "\",\"" + quantity + "\");";
        System.out.println(itemCode + locationID + quantity);
        stmt.executeUpdate(sql);
        conn.close();
        stmt.close();
        System.out.println("StoredProduct Added!");
        return true;
    }

    //method to access database and delete product
    public String deleteProduct(String productCode) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "delete from Product where productCode=\""+ productCode + "\";";
        stmt.executeUpdate(sql);
        conn.close();
        stmt.close();
        System.out.println("Product Deleted!");
        return "true";
    }

    //method to access database and delete productitem
    public String deleteProductItem(String productItemCode) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "DELETE FROM ProductItem WHERE productItemCOde= \"" + productItemCode + "\";";
        int i= stmt.executeUpdate(sql);
        conn.close();
        stmt.close();
        if (i>0){
            System.out.println("Product Item Deleted!");
            return "deleted";
        }
        else{
            System.out.println("Error!");
            return "fail";
        }
    }

    //allowing users to update product details using product code (not allowing users to change productCode)
    public String updateProduct(Product updatedProduct) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String productCode= updatedProduct.getProductCode();
        String name= updatedProduct.getProductName();
        String price= updatedProduct.getPrice();
        String description= updatedProduct.getDescription();
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "UPDATE Product \n" +
                "SET productName = \"" + name + "\"" + ", price = \"" + price + "\"" + ", description= \"" + description + "\""+ "\n" +
                "WHERE productCode= \"" + productCode + "\"; ";
        int i= stmt.executeUpdate(sql);
        conn.close();
        stmt.close();
        if (i > 0){
            System.out.println("Product updated!");
            return "updated";
        }
        else{
            System.out.println("Error! Product could not be updated!");
            return "fail";
        }
    }

    //updating the stored product (Only allowing user to change the quantity of the product item in a particular location)
    public String updateStoredProduct(StoredProduct updatedStoredProduct) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String productItemCode= updatedStoredProduct.getProductItemCode();
        String locationID= updatedStoredProduct.getLocationID();
        String quantity= updatedStoredProduct.getProductQuantity();
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "UPDATE StoredProduct \n" +
                "SET productQuantity = \"" + quantity + "\" \n" +
                "WHERE productItemCode= \"" + productItemCode + "\" & locationID= \"" + locationID + "\"; ";
        int i= stmt.executeUpdate(sql);
        conn.close();
        stmt.close();
        if(i>0){
            System.out.println("StoredProduct (Quantity) updated!");
            return "updated";
        }
        else{
            System.out.println("Error! StoredProduct (Quantity) could not be updated!");
            return "fail";
        }

    }
}

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
    List<Product> products;
    List<ProductItem> productItems;
    DatabaseConnection dbconnet;
    Connection conn;
    Statement stmt;

    //initializing list through constructor to avoid null pointer exception
    public ProductDAO() {
        searchProducts= new ArrayList<SearchProduct>();
        products = new ArrayList<Product>();
        productItems = new ArrayList<ProductItem>();
    }
    //     View all Product
    public List<Product> viewAllProducts(){
        String viewAllProductSqlQuery = "SELECT * FROM Product;";
        executeSearchProductSQLQueries(viewAllProductSqlQuery);
        return products;
    }

    // Search Product Code
    public List<Product> searchProductCode(String productCode){
        String viewAllProductSqlQuery = "SELECT * FROM Product WHERE productCode = \"" + productCode + "\";";
        executeSearchProductSQLQueries(viewAllProductSqlQuery);
        return products;
    }

    // View searched Product Item
    public List<ProductItem> viewSeachedProductItems(String productCode){
        String viewSearchedProductItemsSqlQuery = "SELECT * FROM ProductItem WHERE productCode = \"" + productCode + "\";";
        executeSearchProductItemSQLQueries(viewSearchedProductItemsSqlQuery);
        return productItems;
    }

    // Search Product Item Code
    public List<ProductItem> searchProductItemCode(String productItemCode){
        String searchProductItemCodeSqlQuery = "SELECT * FROM ProductItem WHERE productItemCode = \"" + productItemCode + "\";";
        executeSearchProductItemSQLQueries(searchProductItemCodeSqlQuery);
        return productItems;
    }

    // View the product item details
    public List<SearchProduct> viewProductItemDetails(String productItemCode) {

        String viewProductItemDetails = " SELECT sp.productItemCode, \n" +
                "        sp.productQuantity, \n" +
                "        l.locationID, \n" +
                "        l.locationName, \n" +
                "        l.locationAddress,\n" +
                "        l.phone\n" +
                "FROM StoredProduct sp\n" +
                "JOIN Location l ON sp.locationID = l.locationID\n" +
                "WHERE sp.productItemCode = \"" + productItemCode + "\";";
        executeViewProductItemDetailSQLQueries(viewProductItemDetails);

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
                Product product = new Product();
                product.setProductCode(resultSet.getString(1));
                product.setProductName(resultSet.getString(2));
                product.setPrice(resultSet.getString(3));
                product.setDescription(resultSet.getString(4));

                products.add(product);
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

    //Execute the queries for Search Product Item methods
    public void executeSearchProductItemSQLQueries(String sqlQuery){
        try {
            dbconnet = new DatabaseConnection();
            conn = dbconnet.connect();
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            while (resultSet.next()) {
                ProductItem productItem = new ProductItem();
                productItem.setProductItemCode(resultSet.getString(1));
                productItem.setProductCode(resultSet.getString(2));
                productItem.setProductSize(resultSet.getString(3));

                productItems.add(productItem);
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

    //Execute the queries for View Product Item Details methods
    public void executeViewProductItemDetailSQLQueries(String sqlQuery){
        try {
            dbconnet = new DatabaseConnection();
            conn = dbconnet.connect();
            stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sqlQuery);
            while (resultSet.next()) {
                SearchProduct searchProduct = new SearchProduct();
                searchProduct.setProductItemCode(resultSet.getString(1));
                searchProduct.setProductQuantity(resultSet.getString(2));
                searchProduct.setLocationID(resultSet.getString(3));
                searchProduct.setLocationName(resultSet.getString(4));
                searchProduct.setLocationAddress(resultSet.getString(5));
                searchProduct.setPhone(resultSet.getString(6));

                searchProducts.add(searchProduct);
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
        System.out.println("a " + productCode);
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
                "WHERE productItemCode= \"" + productItemCode + "\" AND locationID= \"" + locationID + "\"; ";
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

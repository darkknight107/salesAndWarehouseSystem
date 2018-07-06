package com.fellowshipofthe.searchProduct;

import com.fellowshipofthe.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SearchProductDAO {
    List<SearchProduct> searchProducts;
    List<SearchProduct> viewProductItems;
    DatabaseConnection dbconnet;
    Connection conn;

    public SearchProductDAO() {
        searchProducts= new ArrayList<SearchProduct>();
        viewProductItems = new ArrayList<SearchProduct>();

    }
    public List<SearchProduct> searchProduct(String code){
        try {
            dbconnet = new DatabaseConnection();
            conn = dbconnet.connect();
            String sql = " SELECT p.productCode, \n" +
                    "        pi.productItemCode, \n" +
                    "        pi.productSize, \n" +
                    "        p.productName, \n" +
                    "        sum(sp.productQuantity) 'productQuantity',\n" +
                    "        p.price,\n" +
                    "        sp.locationID,\n" +
                    "        l.locationName,\n" +
                    "        l.locationAddress, \n" +
                    "        l.phone\n" +
                    "FROM StoredProduct sp\n" +
                    "\tJOIN ProductItem pi ON sp.productItemCode = pi.productItemCode\n" +
                    "    JOIN Product p ON pi.productCode = p.productCode\n" +
                    "    JOIN Location l ON sp.locationID = l.locationID\n" +
                    "WHERE pi.productItemCode = \"" + code + "\" OR p.productCode = \"" + code + "\"" +
                    "group by l.locationID;";
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                SearchProduct searchProduct = new SearchProduct();
                searchProduct.setProductCode(resultSet.getString(1));
                searchProduct.setProductItemCode(resultSet.getString(2));
                searchProduct.setProductSize(resultSet.getString(3));
                searchProduct.setProductName(resultSet.getString(4));
                searchProduct.setProductQuantity(resultSet.getInt(5));
                searchProduct.setPrice(resultSet.getString(6));
                searchProduct.setLocationID(resultSet.getString(7));
                searchProduct.setLocationName(resultSet.getString(8));
                searchProduct.setLocationAddress(resultSet.getString(9));
                searchProduct.setPhone(resultSet.getString(10));

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
            }
            catch (SQLException ignore)
            {
            }
        }
        return searchProducts;
    }
    public List<SearchProduct> viewProductItems(String productCode, String locationID) {
        try {
            dbconnet = new DatabaseConnection();
            conn = dbconnet.connect();
            String sql = " SELECT p.productCode, \n" +
                    "        pi.productItemCode, \n" +
                    "        pi.productSize, \n" +
                    "        p.productName, \n" +
                    "        sp.productQuantity,\n" +
                    "        p.price,\n" +
                    "        sp.locationID,\n" +
                    "        l.locationName,\n" +
                    "        l.locationAddress, \n" +
                    "        l.phone\n" +
                    "FROM StoredProduct sp\n" +
                    "\tJOIN ProductItem pi ON sp.productItemCode = pi.productItemCode\n" +
                    "    JOIN Product p ON pi.productCode = p.productCode\n" +
                    "    JOIN Location l ON sp.locationID = l.locationID\n" +
                    "WHERE p.productCode = \"" + productCode + "\" AND l.locationID = \"" + locationID + "\";";
            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql);
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

                viewProductItems.add(viewProductItem);
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
            }
            catch (SQLException ignore)
            {
            }
        }
        return viewProductItems;

    }
    //method to access database and add new product to the database
    public Boolean addProduct(List<String> newProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        //creating new variables to store new product details
        String code= newProduct.get(0);
        String name= newProduct.get(1);
        String price= newProduct.get(2);
        String description= newProduct.get(3);
        //opening a connection with the database and creating a statement
        dbconnet = new DatabaseConnection();
        conn = dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "INSERT INTO Product (productCode, productName, price, description)" +
                "VALUES (\"" + code +"\",\""+ name+ "\",\"" + price + "\",\"" + description +"\");";
        System.out.println(code + name + price + description);
        stmt.executeUpdate(sql);
        conn.close();
        System.out.println("Product Added!");
        return true;
    }

    //method to access database and add new product item to the database
    public Boolean addProductItem(List<String> newProductItem) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String itemCode= newProductItem.get(0);
        String code= newProductItem.get(1);
        String itemSize= newProductItem.get(2);
        //opening a connection with the database and creating a statement
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "INSERT INTO ProductItem (productItemCode, productCode, productSize)" +
                "VALUES (\"" + itemCode +"\",\""+ code+ "\",\"" + itemSize + "\");";
        System.out.println(itemCode + code + itemSize);
        stmt.executeUpdate(sql);
        conn.close();
        System.out.println("ProductItem Added!");
        return true;
    }

    //method to access database and add new stored product to the database
    public Boolean addStoredProduct(List<String> newStoredProduct) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        String itemCode= newStoredProduct.get(0);
        String locationID= newStoredProduct.get(1);
        String quantity= newStoredProduct.get(2);
        //opening a connection with the database and creating a statement
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "INSERT INTO StoredProduct(productItemCode, locationID, productQuantity)" +
                "VALUES (\"" + itemCode +"\",\""+ locationID+ "\",\"" + quantity + "\");";
        System.out.println(itemCode + locationID + quantity);
        stmt.executeUpdate(sql);
        conn.close();
        System.out.println("StoredProduct Added!");
        return true;
    }
}

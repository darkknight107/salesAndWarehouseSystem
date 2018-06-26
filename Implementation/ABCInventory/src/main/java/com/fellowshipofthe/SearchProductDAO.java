package com.fellowshipofthe;

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
    public String addProduct(List <SearchProduct> productDetails) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        SearchProduct product= new SearchProduct();
        product.setProductCode(String.valueOf(productDetails.get(0)));
        product.setProductName(String.valueOf(productDetails.get(1)));
        product.setPrice(String.valueOf(productDetails.get(2)));
        product.setDescription(String.valueOf(productDetails.get(3)));
        String productCode= product.getProductCode();
        String name= product.getProductName();
        String price= product.getPrice();
        String description= product.getDescription();
        //opening a connection with the database and creating a statement
        dbconnet = new DatabaseConnection();
        conn = dbconnet.connect();
        Statement stmt= conn.createStatement();
        String sql= "INSERT INTO Product (productCode, productName, price, description)" +
                "VALUES (" +productCode +","+ name+ "," + price + "," + description +")";
        stmt.executeUpdate(sql);
        conn.close();
        return("Product Added!");
    }
}

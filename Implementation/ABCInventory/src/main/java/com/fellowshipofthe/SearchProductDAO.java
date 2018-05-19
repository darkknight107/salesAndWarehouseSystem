package com.fellowshipofthe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SearchProductDAO {
    List<SearchProduct> searchProducts;
    DatabaseConnection dbconnet;
    Connection conn;

    public SearchProductDAO() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        searchProducts= new ArrayList<SearchProduct>();
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
    }
    public List<SearchProduct> searchProduct(String code) throws SQLException {
        //String sql= "select * from Product where productCode= \""+ code + "\"";
        String sql= " SELECT p.productCode, \n" +
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
                "WHERE p.productCode = \""+ code +"\"" +
                "group by l.locationID;";
        Statement stmt= conn.createStatement();
        ResultSet resultSet= stmt.executeQuery(sql);
        while(resultSet.next()){
            SearchProduct searchProduct= new SearchProduct();
            searchProduct.setProductCode(resultSet.getString(1));
            searchProduct.setProductName(resultSet.getString(2));
            searchProduct.setProductQuantity(resultSet.getInt(3));
            searchProduct.setPrice(resultSet.getString(4));
            searchProduct.setLocationID(resultSet.getString(5));
            searchProduct.setLocationName(resultSet.getString(6));
            searchProduct.setLocationAddress(resultSet.getString(7));
            searchProduct.setPhone(resultSet.getString(8));

            searchProducts.add(searchProduct);
        }
        return searchProducts;
    }
}

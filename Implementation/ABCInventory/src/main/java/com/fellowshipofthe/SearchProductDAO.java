package com.fellowshipofthe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SearchProductDAO {
    List<Product> products;
    DatabaseConnection dbconnet;
    Connection conn;

    public SearchProductDAO() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        products= new ArrayList<Product>();
        dbconnet= new DatabaseConnection();
        conn= dbconnet.connect();
    }
    public ProductItem searchProduct(String code) throws SQLException {
        //String sql= "select * from Product where productCode= \""+ code + "\"";
        String sql= " SELECT * from ProductItem where productItemCode=\""+ code + "\"";
        Statement stmt= conn.createStatement();
        ResultSet resultSet= stmt.executeQuery(sql);

        ProductItem productItem= new ProductItem();
        productItem.setProductItemCode(resultSet.getString(1));
        productItem.setSize(resultSet.getString(2));
        return productItem;

    }
}

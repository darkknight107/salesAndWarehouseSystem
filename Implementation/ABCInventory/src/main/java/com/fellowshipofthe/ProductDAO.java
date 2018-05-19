package com.fellowshipofthe;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    List<Product> products;
    DatabaseConnection dbConnect;
    Connection conn;
    public ProductDAO() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        //initializing arraylist
        products = new ArrayList<Product>();
        //creating new  connection
        dbConnect = new DatabaseConnection();
        //connecting to database
        conn = dbConnect.connect();
    }
    public List<Product> getProducts() throws SQLException {
        String sql="select * from Product";
        Statement st= conn.createStatement();
        ResultSet rs= st.executeQuery(sql);
        while(rs.next()){
            Product product= new Product();
            product.setProductCode(rs.getString(1));
            product.setProductName(rs.getString(2));
            product.setPrice(rs.getString(3));
            product.setDescription(rs.getString(4));

            products.add(product);
        }
        return products;
    }
}

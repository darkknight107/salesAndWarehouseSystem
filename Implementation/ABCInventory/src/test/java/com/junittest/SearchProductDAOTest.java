package com.junittest;

import com.fellowshipofthe.SearchProduct;
import com.fellowshipofthe.SearchProductDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchProductDAOTest {
    @Mock
    private DataSource dataSource;
    @Mock
    private Connection connection;
    @Mock
    private PreparedStatement stmt;
    @Mock
    private ResultSet rs;

    private SearchProduct searchProduct;

    @Before
    public void setUp() throws Exception {
        assertNotNull(dataSource);
        //when(connection.prepareStatement(any(String.class))).thenReturn(stmt);
       // when(dataSource.getConnection()).thenReturn(connection);

        searchProduct= new SearchProduct();
        searchProduct.setProductItemCode("S3100");
        searchProduct.setProductSize("XS");
        searchProduct.setProductCode("S3");
        searchProduct.setProductName("Surry Coat");
        searchProduct.setProductQuantity(20);
        searchProduct.setPrice("200");
        searchProduct.setLocationID("WRH3");
        searchProduct.setLocationName("Surry Hills");
        searchProduct.setLocationAddress("2 Howard Street");
        searchProduct.setPhone("344556");

        /*when(rs.first()).thenReturn(true);
        when(rs.getString(1)).thenReturn(searchProduct.getProductItemCode());
        when(rs.getString(2)).thenReturn(searchProduct.getProductSize());
        when(rs.getString(3)).thenReturn(searchProduct.getProductCode());
        when(rs.getString(4)).thenReturn(searchProduct.getProductName());
        when(rs.getInt(5)).thenReturn(searchProduct.getProductQuantity());
        when(rs.getString(6)).thenReturn(searchProduct.getPrice());
        when(rs.getString(7)).thenReturn(searchProduct.getLocationID());
        when(rs.getString(8)).thenReturn(searchProduct.getLocationName());
        when(rs.getString(9)).thenReturn(searchProduct.getLocationAddress());
        when(rs.getString(10)).thenReturn(searchProduct.getPhone());*/

    }

    @Test
    public void searchProductTest() {
        SearchProductDAO dao= new SearchProductDAO();
        dao.searchProduct(searchProduct.getProductCode());

        assertEquals(searchProduct.getProductItemCode(),"S3100");

    }
}
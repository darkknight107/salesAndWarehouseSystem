package com.junittest;

import com.fellowshipofthe.SearchProduct;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.TestCase.assertEquals;

public class SearchProductTest {
    SearchProduct searchProduct= null;
    Field productItemField= null;
    Field productSizeField= null;
    Field productCodeField= null;

    @Before
    public void setUp() throws Exception {
        searchProduct= new SearchProduct();
    }
    @Test
    public void testSetProductItemCode() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setProductItemCode("A1");
        //then
        productItemField= searchProduct.getClass().getDeclaredField("productItemCode");
        productItemField.setAccessible(true);
        assertEquals("Testing setProductItemCode", productItemField.get(searchProduct), "A1");
    }
    @Test
    public void testGetProductItemCode() throws NoSuchFieldException, IllegalAccessException {
        //given
        productItemField = searchProduct.getClass().getDeclaredField("productItemCode");
        productItemField.setAccessible(true);
        productItemField.set(searchProduct, "A1100");
        //when
        final String result= searchProduct.getProductItemCode();
        //then
        assertEquals("testing getProductItemCode", result, "A1100");
    }
    @Test
    public void testSetProductSize() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setProductSize("M");
        //then
        productSizeField= searchProduct.getClass().getDeclaredField("productSize");
        productSizeField.setAccessible(true);
        assertEquals("Testing setProductSize", productSizeField.get(searchProduct), "M");
    }
    @Test
    public void testGetProductSize() throws NoSuchFieldException, IllegalAccessException {
        //given
        productSizeField= searchProduct.getClass().getDeclaredField("productSize");
        productSizeField.setAccessible(true);
        productSizeField.set(searchProduct, "XS");
        //when
        final String result= searchProduct.getProductSize();
        //then
        assertEquals("testing getProductSize", result, "XS");
    }
    @Test
    public void testSetProductCode() throws NoSuchFieldException, IllegalAccessException {
        //when
        searchProduct.setProductCode("C1");
        //then
        productCodeField= searchProduct.getClass().getDeclaredField("productCode");
        productCodeField.setAccessible(true);
        assertEquals("Testing setProductCode", productCodeField.get(searchProduct), "C1");
    }
    @Test
    public void testGetProductCode() throws NoSuchFieldException, IllegalAccessException {
        //given
        productCodeField= searchProduct.getClass().getDeclaredField("productCode");
        productCodeField.setAccessible(true);
        productCodeField.set(searchProduct,"C2");
        //when
        final String result= searchProduct.getProductCode();
        //then
        assertEquals("Testing getProductCode", result, "C2");
    }

}